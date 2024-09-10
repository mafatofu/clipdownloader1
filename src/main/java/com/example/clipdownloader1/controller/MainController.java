package com.example.clipdownloader1.controller;

import com.example.clipdownloader1.service.CrawlingService;
import com.example.clipdownloader1.service.MainService;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

//테스트용 컨트롤러
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
    private final CrawlingService crawlingService;
    @GetMapping
    public String home(
            Model model
    ){
        return "downloader1/home";
    }
    //클립 url을 받아서 클립 다운로드
    @GetMapping("/clipDownload")
    @ResponseBody
    public String clipDownload(
            @RequestParam(required = true)
            String clipUrl
    ) throws IOException {
        //selenium을 활용한 크롤링
        return crawlingService.chzzkClipCrawling(clipUrl);

    }
}
