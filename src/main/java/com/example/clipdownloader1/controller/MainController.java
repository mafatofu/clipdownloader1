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
import java.util.List;

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
        return "downloader1/multiDownload";

    }
    /** 스트리머 이름 검색 시 정렬기준에 따른 10개의 상위 클립 이전 / 다음페이지
    //dto로 front단의 데이터를 받아온다.*/
    @GetMapping("/multiDownload/{streamerName}/{orderType}/otherPage")
    public String streamerClipSearch(
            StreamerClipSearchDto streamerClipSearchDto,
            Model model
    ) throws Exception {
        //받아온 스트리머명과 정렬기준으로 검색
        HttpStatus statusResult = HttpStatus.NOT_FOUND;
        List<ClipInfoDto> clipInfoDtoList = new ArrayList<ClipInfoDto>();
        try {
            clipInfoDtoList =
                    clipService.streamerClipSearchService(streamerClipSearchDto);
            statusResult = HttpStatus.OK;
        } catch (Exception e){
            System.out.println("--------------스트리머 검색 에러--------------");
            e.printStackTrace();
        }
        //결과를 view단으로 넘기기
        model.addAttribute("clipInfoDtoList",clipInfoDtoList);
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


}
