package com.example.clipdownloader1.service;

import com.example.clipdownloader1.config.chzzkUrls;
import com.example.clipdownloader1.dto.ClipInfoDto;
import com.example.clipdownloader1.dto.StreamerClipSearchDto;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClipService {
    private final MainService mainService;
    private final chzzkUrls chzzkUrls;
    //스트리머 검색 후 uid를 특정지어 가져올 수 있는 html class
    private static String streamerSearchParticle = "channel_item_wrapper__CT2Qw";
    public WebDriver crawlingStandard(){
        //selenium을 활용한 크롤링
        String WEB_DRIVER_ID = "webdriver.chrome.driver";
        String WEB_DRIVER_PATH = "C:\\chromeDriver\\chromedriver.exe";

        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        //크롬창을 띄우지 않고 작업
        options.addArguments("headless");

        WebDriver driver = new ChromeDriver(options);

        return driver;
    }
    /**셀레니움을 활용한 클립원본url 뽑아내는 기능*/
    public String chzzkClipCrawling(String clipUrl){

        WebDriver driver = crawlingStandard();

        String clipSrcUrl = "";
        try {
            //드라이버 시작
            driver.get(clipUrl); //크롤링할 사이트 url
            Thread.sleep(1000);
            //iframe element로 스위치 // 다시 원래대로 돌아올때는 driver.switch_to.default_content()
            driver.switchTo().frame(driver.findElement(By.className("clip_viewer_section__Z3FbJ")));
            //iframe 안의 원하는 엘리멘트 찾기
            WebElement we = driver.findElement(By.className("webplayer-internal-video"));
            //엘리멘트 안의 특정 요소 뽑기 : 클립 소스 url(다운로드 가능)
            clipSrcUrl = we.getAttribute("src");

            //TODO url을 통한 다운로드
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("크롤링 에러 발생");
        } finally {
            driver.quit();
        }

        return clipSrcUrl;
    }
    /**HttpURLConnection 연결 메서드* */
    public HttpURLConnection httpUrlConnectSimple(String searchUrl) throws IOException {
        // 웹 페이지 URL 생성
        URL url = new URL(searchUrl);
        // HttpURLConnection 객체 생성
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 연결 옵션 설정
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");//한글때매
        connection.setConnectTimeout(5000); // 연결 타임아웃 설정
        connection.setReadTimeout(5000);    // 읽기 타임아웃 설정
        connection.setUseCaches(false);     // 캐시 사용하지 않음
        connection.setDoInput(true);        // 입력 스트림 사용
        // 연결 수행
        int responseCode = connection.getResponseCode();
        //연결이 성공한 경우
        if (responseCode != HttpURLConnection.HTTP_OK) {
            System.out.println("통신에 실패! 응답 코드 : " + responseCode);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "에러발생, 연결불가");
        }
        return connection;
    }
    /**HttpURLConnection으로 json data를 받아 Map으로 반환*/
    public Map<String, Object> jsonDataReceiveToMap(
            HttpURLConnection connection
    ) throws Exception {
        //connection에서 videoUid추출
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder content = new StringBuilder();
        //json 데이터 넣기
        while ((line = reader.readLine()) != null) {
            content.append(line).append('\n');
        }
        //연결 종료
        connection.disconnect();

        //json to map
        Map<String, Object> takeVideoMap = new HashMap<String, Object>();
        takeVideoMap = mainService.jsonToMap(content.toString());
        //BufferedReader close
        reader.close();
        return takeVideoMap;
    }

    /**HttpURLConnection으로 data를 받아 json으로 반환*/
    public String ConnectionDataToString(
            HttpURLConnection connection
    ) throws Exception {
        //connection에서 videoUid추출
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line, result = "";
        //json 데이터 넣기
        //window.$__videoMeta가 포함되어있으면 넣고 break
        //받은 문장은 subString이나 split등으로 잘라서, json형태로 만들기
        //만든 뒤에 map으로 변경해주고, map에서 원하는 값 찾기
        while ((line = reader.readLine()) != null) {
            if (line.contains("window.$__videoMeta")) {
                //가져온 데이터에서 json부분만 성형
                result = line.split("</script>")[0].split("__videoMeta = ")[1];
                break;
            }
        }
        //연결 종료
        //connection.disconnect();
        //BufferedReader close
        reader.close();
        return result;
    }

    /**HttpURLConnection으로 selenium을 사용하지 않고 html 소스 가져오기
    *             1. 유저가 클립링크를 입력 0
    *             2. 입력된 클립링크의 뒤에서 첫번째 '/'까지의 내용(클립id) 따로 추출 0
    *             3. 추출한 clipId로 url요청
    *                     - json형태의 response 중에서, videoId부분 따로 추출
    *             4. 추출한 clipId와 videoId로 url요청
    *                     - 원본 vod주소가 있는 html문서 획득
    *             5. html 문서 파싱 - 원본 vod주소를 획득
     */
    //@Value("${spring.servlet.multipart.location}")
    String location;
    public ClipInfoDto chzzkClipInfoTake(
            String clipUrl
    ) throws Exception {
        //return용 DTO
        ClipInfoDto clipInfoDto = new ClipInfoDto();
        //clipUid 추출
        String[] clipUrlSplit = clipUrl.split("/");
        String clipUid = clipUrlSplit[clipUrlSplit.length - 1];
        //추출한 clipId로 url요청
        String takeVideoUrl = "https://api.chzzk.naver.com/service/v1/clips/"
                                +clipUid+"/detail?optionalProperties=COMMENT";
        //첫번째 요청
        HttpURLConnection connection = httpUrlConnectSimple(takeVideoUrl);
        //요청으로 받은 json data를 map으로 받음
        Map<String, Object> takeVideoMap = jsonDataReceiveToMap(connection);
        //클립 제목
        String clipTitle = (String) ((HashMap<?, ?>)takeVideoMap.get("content")).get("clipTitle");
        //썸네일 url
        String thumbnailImageUrl = (String) ((HashMap<?, ?>)takeVideoMap.get("content")).get("thumbnailImageUrl");
        //만든날짜
        String createdDate = ((HashMap<?, ?>) takeVideoMap.get("content")).get("createdDate").toString();
        //2023-12-19 22:45:20
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdDateTime = LocalDateTime.parse(createdDate, dtf);

        //추출한 videoId
        String videoId = ((HashMap<?, ?>) takeVideoMap.get("content")).get("videoId").toString();
        //추출한 videoId로 원본 video url요청
        String takeSrcUrl = "https://m.naver.com/shorts/?mediaId="+videoId+
                "&serviceType=CHZZK&mediaType=&recType=CHZZK&recId=%7B%22seedClipUID%22%3A%22"+clipUid+
                "%22%2C%22fromType%22%3A%22GLOBAL%22%2C%22listType%22%3A%22RECOMMEND%22%7D&b" +
                "logId=&docNo=&adAllowed=Y&feedBlock=&enableReverse=false" +
                "&notInterestedMediaIds=&notInterestedChannelIds=&panelType=sdk_chzzk" +
                "&entryPoint=&stmsId=&clickNsc=chzzk_url_clip" +
                "&clickArea=clip_item&adUnitId=chzzk_shortformviewer_web&viewerInfo=chzzk_shortformviewer_web" +
                "&adCtrl=P&theme=light&viewMode=mobile&sdkTargetId=PLAYER-SDK-0-45&env=&embed=true";

        //두번째 요청
        HttpURLConnection connection2 = httpUrlConnectSimple(takeSrcUrl);
        //재사용을 위한 비우기
        takeVideoMap.clear();
        
        //요청으로 받은 connection data에서 원본 클립 주소가 들어있는 json부분만 String으로 받음
        //json형태의 String을 map으로 받음
        String jsonString = ConnectionDataToString(connection2);
        takeVideoMap = mainService.jsonToMap(jsonString);
         //여러겹으로 둘러쌓인 데이터 풀어내어, 원본 영상 url 뽑아내기
        String clipSrcUrl =
                (String) ((Map)(((List)((Map)((Map)((Map)((Map)((Map)takeVideoMap.get("card"))
                        .get("content")).get("vod")).get("playback")).get("videos"))
                        .get("list"))).get(0)).get("source");
        //스트리머이름
        String streamer = (String) ((Map)((List)((Map)
                takeVideoMap.get("card")).get("shortFormBanners")).get(0)).get("title");
        //클리퍼 닉네임
        String clipperName = (String) ((Map)((Map)(((Map)
                takeVideoMap.get("card")).get("interaction"))).get("subscription")).get("name");
        //srcUrl 클립제목 썸네일 스트리머이름 클립 딴사람이름 싹다 묶어서 dto로

        clipInfoDto.setClipSrcUrl(clipSrcUrl);
        clipInfoDto.setStreamer(streamer);
        clipInfoDto.setClipperName(clipperName);
        clipInfoDto.setOriginalUrl(clipUrl);
        clipInfoDto.setClipTitle(clipTitle);
        clipInfoDto.setClipThumbnailUrl(thumbnailImageUrl);
        clipInfoDto.setCreatedDateTime(createdDateTime);
        
        //연결종료
        connection.disconnect();
        connection2.disconnect();

        return clipInfoDto;
    }


    /**스트리머 이름 검색 시 정렬기준에 따른 10개의 상위 클립을 가져옴
     * 정렬기준은 인기순 / 최신순
     * /multiDownload/{스트리머이름}/{정렬기준}
     * 정렬기준 디폴트는 인기순
     * */
    public List<ClipInfoDto> streamerClipSearchService(
            String streamerName,
            String orderType
    ) throws Exception {
        //1. 스트리머 이름으로 uid를 가져온다.
        HttpURLConnection connection =
                httpUrlConnectSimple(chzzkUrls.streamerSearchUrl(streamerName));
        Map<String, Object> streamerInfoMap = jsonDataReceiveToMap(connection);
        //첫번째 연결 종료
        connection.disconnect();
        //map에서 uid를 하나 가져오기
        String uid = "";
        //2. 스트리머 uid로 검색한 결과를 10개씩 페이징하여 가져옴

        List<ClipInfoDto> clipInfoDtoList = new ArrayList<ClipInfoDto>();
        //스트리머 데이터
        Map<String, Object> extractMap
                = (Map<String, Object>) ((Map)((List)((Map)streamerInfoMap.get("content")).get("data")).get(0)).get("channel");
        String streamerUid = (String) extractMap.get("channelId");
        //2. 가져온 스트리머 uid로 클립 여러개 가져오기
        //10개를 기준으로 가져오기
        HttpURLConnection connection2 =
                httpUrlConnectSimple(chzzkUrls.streamerClipSearch(streamerUid, orderType, 10));

        Map<String, Object> streamerClipsMap = jsonDataReceiveToMap(connection2);

        List<Map<String, Object>> clipsMapList =
                (List<Map<String, Object>>) ((Map)streamerClipsMap.get("content")).get("data");
        
        //List<dto>로 담기
        for (int i = 0; i < clipsMapList.size(); i++) {
            ClipInfoDto dto = ClipInfoDto.builder()
                    .clipThumbnailUrl((String) clipsMapList.get(i).get("thumbnailImageUrl"))
                    .clipTitle((String) clipsMapList.get(0).get("clipTitle"))
                    .readCount((Integer) clipsMapList.get(i).get("readCount"))
                    .build();

            clipInfoDtoList.add(dto);
        }
        //TODO 좋아요 개수는 나중에 추가
        return clipInfoDtoList;
    }


    /**스트리머 이름 검색 시 정렬기준에 따른 10개의 상위 클립 이전 / 다음페이지 가져오는 서비스*/
    public List<ClipInfoDto> streamerClipSearchService(
            StreamerClipSearchDto streamerClipSearchDto
    ) throws Exception {
        List<ClipInfoDto> clipInfoDtoList = new ArrayList<ClipInfoDto>();
        //clipUID와 readCount값을 활용하여 이전 / 다음페이지의 10개 클립 가져오기

        HttpURLConnection connection =
                httpUrlConnectSimple(
                        chzzkUrls.streamerClipSearch(
                                streamerClipSearchDto.getStreamerUid(),
                                streamerClipSearchDto.getOrderType(),
                                10,
                                streamerClipSearchDto.getClipUID(),
                                streamerClipSearchDto.getReadCount()
                        )
                );
        //TODO connection으로 가져온 데이터 가공하여 return
        //map으로 가져온 10개의 클립 데이터를 List<dto>로 받기
        Map<String, Object> streamerClipInfoMap = jsonDataReceiveToMap(connection);
        
        return clipInfoDtoList;
    }


}
