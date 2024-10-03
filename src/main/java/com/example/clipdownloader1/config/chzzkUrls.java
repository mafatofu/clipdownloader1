package com.example.clipdownloader1.config;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
/**치지직 관련 connection에 사용되는 url들을 모아둔 클래스*/
public class chzzkUrls {
    /**치지직 스트리머 검색url
     * 해당 결과에서 스트리머의 uid를 얻어낼 수 있다.
     * */
    public String streamerSearchUrl(
            String streamerName
    ) throws UnsupportedEncodingException {
        return "https://api.chzzk.naver.com/service/v1/search/channels?keyword="
                + URLEncoder.encode(streamerName, "UTF-8")
                +"&offset=0&size=1";
    }
    /**치지직 클립 여러개 보기
     * 스트리머이름 / 정렬기준 / 가져오는 사이즈 입력 필요
     * */
    public String streamerClipSearch(
            String streamerUid,
            String orderType,
            int size
    ){
        String returnUrl = "";

        //https://api.chzzk.naver.com/service/v1/channels/f5c058c445257fa60fc75f91d52712fc/clips?orderType=POPULAR&size=10
        return "https://api.chzzk.naver.com/service/v1/channels/" +
                streamerUid+"/clips?" +
                "orderType="+orderType+
                "&size="+size;
    }
    /**치지직 클립 여러개 보기(이전 / 다음페이지)*/
    public String streamerClipSearch(
            String streamerUid,
            String orderType,
            int size,
            String clipUID,
            int readCount
    ){
        String returnUrl = "";
        //https://api.chzzk.naver.com/service/v1/channels/{streamerUid}/clips?orderType={orderType}&size={size}&clipUID={latestClipUid}&readCount={readCount}
        return "https://api.chzzk.naver.com/service/v1/channels/" +
                streamerUid+"/clips?" +
                "orderType="+orderType+
                "&size="+size+
                "&clipUID=" +clipUID+
                "&readCount="+readCount;
    }

    /**치지직 클립 주소*/
    public String clipUrl(
            String clipUid
    ){
        return "https://chzzk.naver.com/clips/og5Bsv3XM1" + clipUid;
    }

    /**clipUid와 videoId로 원본 video url요청*/
    public String takeSrcUrl(
            String clipUid,
            String videoId
    ){
        return "https://m.naver.com/shorts/?mediaId="+videoId+
                "&serviceType=CHZZK&mediaType=&recType=CHZZK&recId=%7B%22seedClipUID%22%3A%22"+clipUid+
                "%22%2C%22fromType%22%3A%22GLOBAL%22%2C%22listType%22%3A%22RECOMMEND%22%7D&b" +
                "logId=&docNo=&adAllowed=Y&feedBlock=&enableReverse=false" +
                "&notInterestedMediaIds=&notInterestedChannelIds=&panelType=sdk_chzzk" +
                "&entryPoint=&stmsId=&clickNsc=chzzk_url_clip" +
                "&clickArea=clip_item&adUnitId=chzzk_shortformviewer_web&viewerInfo=chzzk_shortformviewer_web" +
                "&adCtrl=P&theme=light&viewMode=mobile&sdkTargetId=PLAYER-SDK-0-45&env=&embed=true";
    }
}

