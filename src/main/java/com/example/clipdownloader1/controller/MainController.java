package com.example.clipdownloader1.controller;

import com.example.clipdownloader1.config.chzzkUrls;
import com.example.clipdownloader1.dto.ClipInfoDto;
import com.example.clipdownloader1.dto.ClipPageDto;
import com.example.clipdownloader1.service.ClipService;
import com.example.clipdownloader1.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

//테스트용 컨트롤러
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class MainController {
    private final ClipService clipService;
    private final FileService fileService;
    private final chzzkUrls chzzkUrls;
    @GetMapping
    public String home(
            Model model
    ){
        return "downloader1/home";
    }
    /**클립 url을 받아서 다운로드가능한 주소를 프론트로 리턴*/
    @GetMapping("/clipDownload")
    @ResponseBody
    public ResponseEntity<ClipInfoDto> clipDownload(
            @RequestParam(required = true)
            String clipUrl
    ) throws Exception {
        //HttpURLConnection으로 다운로드 가능한 원본영상 가져오기
        ClipInfoDto clipInfoDto = clipService.chzzkClipInfoTake(clipUrl);
        return ResponseEntity.ok(clipInfoDto);

    }
    /**한 명의 치지직 스트리머 이름을 입력창에 입력시,
     해당하는 스트리머의 클립들이 모두 나타남
     나타난 클립들을 다운로드*/
    @GetMapping("/multiDownload")
    public String multiDownload(){
        return "downloader1/multiDownload";
    }

    /**스트리머 이름 검색 시 정렬기준에 따른 10개의 상위 클립을 가져오기*/
    @GetMapping("/multiDownload/{streamerName}/{orderType}")
    public String streamerClipSearch(
            @PathVariable String streamerName,
            @PathVariable String orderType,
            Model model
    ) throws Exception {
        ClipPageDto clipPageDto = new ClipPageDto();
        //받아온 스트리머명과 정렬기준으로 검색
        try {
            clipPageDto =
                    clipService.streamerClipSearchService(streamerName,orderType);
        } catch (Exception e){
            System.out.println("--------------스트리머 검색 에러--------------");
            e.printStackTrace();
        }
        //결과를 view단으로 넘기기
        model.addAttribute("clipInfoDtoList",clipPageDto.getClipInfoDtoList());
        model.addAttribute("streamerName", streamerName);
        model.addAttribute("streamerNameUtf8", URLEncoder.encode(streamerName, StandardCharsets.UTF_8));
        model.addAttribute("orderType", orderType);
        //페이지 이동을 위한 맨 마지막 클립의 uid와 readCount
        model.addAttribute("nextClipUid", clipPageDto.getNextPageDto().getClipUid());
        model.addAttribute("nextReadCount", clipPageDto.getNextPageDto().getReadCount());
        //이전페이지 이동을 위한 전 페이지 맨 마지막 클립의 uid와 readCount
        model.addAttribute("preventClipUid", clipPageDto.getNextPageDto().getClipUid());
        model.addAttribute("preventReadCount", clipPageDto.getNextPageDto().getReadCount());
        //이전페이지 이동을 위한 전전 페이지 맨 마지막 클립의 uid와 readCount
        model.addAttribute("oldClipUid", clipPageDto.getNextPageDto().getClipUid());
        model.addAttribute("oldReadCount", clipPageDto.getNextPageDto().getReadCount());
        //페이지카운트
        model.addAttribute("pageCount", 1);

        return "downloader1/multiDownload";

    }
    /** 스트리머 이름 검색 시 정렬기준에 따른 10개의 상위 클립 이전 / 다음페이지
    //dto로 front단의 데이터를 받아온다.*/
    @GetMapping("/multiDownload/{streamerName}/{orderType}/{pageCount}")
    public String streamerClipSearch(
            @PathVariable String streamerName,
            @PathVariable String orderType,
            @PathVariable Integer pageCount,
            //마지막 clip UID
            @RequestParam(value = "nextClipUid", defaultValue = "notExist", required = false)
            String nextClipUid,
            //마지막 clip의 조회수
            @RequestParam(value = "nextReadCount", defaultValue = "000", required = false)
            int nextReadCount,
            //전페이지의 마지막 clip UID
            @RequestParam(value = "preventClipUid", defaultValue = "notExist", required = false)
            String preventClipUid,
            //전페이지의 마지막 clip의 조회수
            @RequestParam(value = "preventReadCount", defaultValue = "0", required = false)
            int preventReadCount,
            //전전페이지의 마지막 clip UID
            @RequestParam(value = "oldClipUid", defaultValue = "notExist", required = false)
            String oldClipUid,
            //전전페이지의 마지막 clip의 조회수
            @RequestParam(value = "oldReadCount", defaultValue = "0", required = false)
            int oldReadCount,
            //이전페이지 or 다음페이지 호출인지 체크
            @RequestParam(value = "pageCk", defaultValue = "next", required = false)
            String pageCk,
            Model model
    ) throws Exception {
        //받아온 스트리머명과 정렬기준으로 검색

        ClipPageDto clipPageDto = new ClipPageDto();
        //첫페이지 요청 시
        if (pageCount == 1){
            clipPageDto =
                    clipService.streamerClipSearchService(streamerName,orderType);
        } else {
            //이전 / 다음페이지 호출 체크
            if ("prevent".equals(pageCk)){
                clipPageDto =
                        clipService.streamerClipSearchService(streamerName,orderType);
            } else if ("next".equals(pageCk)){
                clipPageDto =
                        clipService.streamerClipSearchService(streamerName,orderType,nextClipUid,nextReadCount);
            }
        }
        //결과를 view단으로 넘기기
        //상위 클립 10개 정보
        model.addAttribute("clipInfoDtoList",clipPageDto.getClipInfoDtoList());
        //스트리머명
        model.addAttribute("streamerName", streamerName);
        //정렬기준
        model.addAttribute("orderType", orderType);
        //다음 페이지 이동을 위한 맨 마지막 클립의 uid와 readCount
        model.addAttribute("nextClipUid", clipPageDto.getNextPageDto().getClipUid());
        model.addAttribute("nextReadCount", clipPageDto.getNextPageDto().getReadCount());
        //페이지카운트
        model.addAttribute("pageCount", pageCount);
        //클립 url src
        model.addAttribute("clipUrlSrc", chzzkUrls.clipUrlSrc());
        return "downloader1/multiDownload";

    }


    /**
     * 다운로드 버튼 클릭 시 PC에 바로 저장 컨트롤러
     */
    @GetMapping(value="/clipDownloadDirect", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> clipDownloadDirect(
            @RequestParam String clipSrcUrl,
            @RequestParam String clipTitle
    ) throws IOException {
        ResponseEntity<Resource> responseEntity;
        responseEntity = fileService.chzzkClipDirectDownloadToProject(clipSrcUrl, clipTitle);
        return responseEntity;
    }



    /**여러 개의 클립을 한 번에 다운로드 컨트롤러*/
    @PostMapping("/multiDownload/clipDownloadDirectMulti")
    @ResponseBody
    public ResponseEntity<List<String>> clipDownloadDirectMulti(
            @RequestBody
            List<ClipInfoDto> checkedClipList
    ) throws Exception {
        List<String> resultList = new ArrayList<>();
        int downloadResult = 0;
        //들어온 클립 수만큼 다운로드 횟수 반복
        //map으로 클립제목 : 0/1(실패/성공) 여부 저장하여 돌려주기
        for (int i = 0; i < checkedClipList.size(); i++) {
            //클립 srcUrl 가져와야 함
            checkedClipList.get(i).setClipSrcUrl(
                    clipService.findVodUrl(checkedClipList.get(i).getOriginalUrl(), checkedClipList.get(i).getVideoId()));
            //downloadResult = fileService.chzzkClipDirectDownload(checkedClipList.get(i));
            //TODO 여러개의 responseEntity를 어떻게 돌려줘야하는가?
            //downloadResult = fileService.chzzkClipDirectDownloadToProject(checkedClipList.get(i));
            //다운로드 성공
        }
        return new ResponseEntity<List<String>>(resultList, HttpStatus.OK);
    }
}
