package com.example.clipdownloader1.controller;

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

    private final MainService mainService;
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
            String clipUrl,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        //selenium을 활용한 크롤링
        String WEB_DRIVER_ID = "webdriver.chrome.driver";
        String WEB_DRIVER_PATH = "C:\\chromeDriver\\chromedriver.exe";

        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        //크롬창을 띄우지 않고 작업
        options.addArguments("headless");

        WebDriver driver = new ChromeDriver(options);

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
        } finally {
            driver.close();
        }
        //다운로드 가능한 비디오 url을 찾았으면, 그 url을 view단으로 넘겨주기
        //redirectAttributes.addFlashAttribute("videoSrcUrl", clipSrcUrl);
        return clipSrcUrl;
    }
}
