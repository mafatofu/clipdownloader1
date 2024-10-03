package com.example.clipdownloader1.controller;

import com.example.clipdownloader1.dto.ClipInfoDto;
import com.example.clipdownloader1.dto.StreamerClipSearchDto;
import com.example.clipdownloader1.service.ClipService;
import com.example.clipdownloader1.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//테스트용 컨트롤러
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
    private final ClipService clipService;
    private final FileService fileService;
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
        //받아온 스트리머명과 정렬기준으로 검색
        HttpStatus statusResult = HttpStatus.NOT_FOUND;
        List<ClipInfoDto> clipInfoDtoList = new ArrayList<ClipInfoDto>();
        try {
            clipInfoDtoList =
                    clipService.streamerClipSearchService(streamerName,orderType);
            statusResult = HttpStatus.OK;
        } catch (Exception e){
            System.out.println("--------------스트리머 검색 에러--------------");
            e.printStackTrace();
        }
        //결과를 view단으로 넘기기
        model.addAttribute("clipInfoDtoList",clipInfoDtoList);
        model.addAttribute("streamerName", streamerName);
        model.addAttribute("orderType", orderType);
        //페이지 이동을 위한 맨 마지막 클립의 uid와 readCount
        model.addAttribute("lastClipUid",
                clipInfoDtoList.get(clipInfoDtoList.size()-1).getOriginalUrl());
        model.addAttribute("lastReadCount",
                clipInfoDtoList.get(clipInfoDtoList.size()-1).getReadCount());
        return "downloader1/multiDownload";

    }
    /** 스트리머 이름 검색 시 정렬기준에 따른 10개의 상위 클립 이전 / 다음페이지
    //dto로 front단의 데이터를 받아온다.*/
    @GetMapping("/multiDownload/{streamerName}/{orderType}/otherPage")
    public String streamerClipSearch(
            @PathVariable String streamerName,
            @PathVariable String orderType,
            @RequestParam(value = "clipUid") String clipUid,
            @RequestParam(value = "readCount") int readCount,
            Model model
    ) throws Exception {
        //받아온 스트리머명과 정렬기준으로 검색
        HttpStatus statusResult = HttpStatus.NOT_FOUND;
        List<ClipInfoDto> clipInfoDtoList = new ArrayList<ClipInfoDto>();
        try {
            clipInfoDtoList =
                    clipService.streamerClipSearchService(streamerName,orderType, clipUid, readCount);
            statusResult = HttpStatus.OK;
        } catch (Exception e){
            System.out.println("--------------스트리머 검색 에러--------------");
            e.printStackTrace();
        }
        //결과를 view단으로 넘기기
        model.addAttribute("clipInfoDtoList",clipInfoDtoList);
        model.addAttribute("streamerName", streamerName);
        model.addAttribute("orderType", orderType);

        //페이지 이동을 위한 맨 마지막 클립의 uid와 readCount
        //TODO
        model.addAttribute("lastClipUid",
                clipInfoDtoList.get(clipInfoDtoList.size()-1).getOriginalUrl());
        model.addAttribute("lastReadCount",
                clipInfoDtoList.get(clipInfoDtoList.size()-1).getReadCount());
        return "downloader1/multiDownload";

    }


    /**다운로드 버튼 클릭 시 PC에 바로 저장 컨트롤러*/
    @PostMapping("/clipDownloadDirect")
    @ResponseBody
    public ResponseEntity<Integer> clipDownloadDirect(
            @RequestBody ClipInfoDto clipInfoDto
    ) throws IOException {
        HttpStatus statusResult = HttpStatus.NOT_FOUND;
        //service단의 결과에 따라 분기하기
        int result = fileService.chzzkClipDirectDownload(clipInfoDto);
        if (result == 1)
            statusResult = HttpStatus.OK;

        return new ResponseEntity<Integer>(result, statusResult);
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
            downloadResult = fileService.chzzkClipDirectDownload(checkedClipList.get(i));
            //다운로드 성공
            if (downloadResult == 1){
                resultList.add(checkedClipList.get(i).getClipTitle() + ": 다운로드 성공");
            } else
                resultList.add(checkedClipList.get(i).getClipTitle() + ": 다운로드 실패");
        }
        return new ResponseEntity<List<String>>(resultList, HttpStatus.OK);
    }
}
