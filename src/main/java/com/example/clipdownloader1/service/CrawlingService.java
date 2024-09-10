package com.example.clipdownloader1.service;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrawlingService {
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
        } finally {
            driver.quit();
        }

        return clipSrcUrl;
    }
}
