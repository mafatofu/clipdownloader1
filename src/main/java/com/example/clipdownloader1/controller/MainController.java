package com.example.clipdownloader1.controller;

import com.example.clipdownloader1.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//테스트용 컨트롤러
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
    private final MainService mainService;
    @GetMapping
    public String home(){
        return "downloader1/home";
    }
    //클립 url을 받아서 클립 다운로드
    @GetMapping("/clipDownload")
    @ResponseBody
    public String clipDownload(
            @RequestParam(required = true)
            String clipUrl,
            @RequestHeader HttpHeaders headers
    ) throws IOException {
        //TODO : jsoup 활용하여, url크롤링
        String test = clipUrl;
        //다운로드 로직 탄 후에 home으로 이동 or 그냥 다운로드만 되도록 하기
        HttpURLConnection urlConnection = null;
        URL url = new URL(clipUrl);
        urlConnection = (HttpURLConnection) url.openConnection();
        return "downloader1/home";
    }
}
