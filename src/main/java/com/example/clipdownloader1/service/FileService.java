package com.example.clipdownloader1.service;

import com.example.clipdownloader1.repo.ClipRepo;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**파일입출력 관련 서비스*/
@Service
@RequiredArgsConstructor
public class FileService {
    private final ClipRepo clipRepo;
    private final ClipService clipService;

    /**크롤링해온 클립 링크를 바로 다운로드하는 기능(240924 현재 다운로드 안됨)*/
    public int chzzkClipDirectDownload(
            String clipSrcUrl,
            HttpServletRequest request,
            HttpServletResponse response
            ){
        int result = 0;
        //String clipSrcUrl = crawlingService.chzzkClipCrawling(clipUrl);

        try {
            //TODO 크롤링서비스 추가하여, 원본클립명으로 다운로드
            String fileName = "clipDownload" + ".mp4";
            URLConnection fetchWebsite = new URL(clipSrcUrl).openConnection();
            InputStream in = new BufferedInputStream(fetchWebsite.getInputStream());
            ServletOutputStream out = response.getOutputStream();

            response.setHeader( "Content-Transfer-Encoding", "binary" );
            response.setContentType( "application/x-download" ); //이거 없으면 다운로드 안됨..
            response.setContentType("application/octet-stream"); //다운로드 받는 타입이 파일 이라는 것을 명시
            response.setHeader("Content-Disposition", "filename=" + fileName + ";");//파일 명을 정하는 곳

            IOUtils.copy(in, out); // 실질적으로 파일을 다운로드(copy)하는 곳

            out.flush();
            out.close();
            in.close();
            result = 1;
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("파일 저장 에러 발생");
        }
        return result;
    }
}
