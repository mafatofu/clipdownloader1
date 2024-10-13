package com.example.clipdownloader1.service;

import com.example.clipdownloader1.dto.ClipInfoDto;
import com.example.clipdownloader1.repo.ClipRepo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jsoup.HttpStatusException;
import org.springframework.beans.factory.annotation.Value;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**파일입출력 관련 서비스*/
@Service
@RequiredArgsConstructor
public class FileService {
    /**클립 링크를 바로 다운로드하는 기능*/
    @Value("${spring.servlet.multipart.location}")
    String location;
    public int chzzkClipDirectDownload(
            ClipInfoDto clipInfoDto
            ) throws IOException {
        //결과 리턴용 변수
        int result = 0;
        //클립명에 시간붙여주는 용 변수
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        LocalDateTime aa = LocalDateTime.now();
        //디렉토리 생성을 위한 path
        Path directoryPath = Paths.get(location);
        //디렉토리가 존재하지 않는다면 생성해주기
        if (!Files.isDirectory(directoryPath)){
            Files.createDirectory(directoryPath);
        }
        //2. 클립명. 겹치지 않게 초단위 시간을 붙여줌.
            //2.1 클립명에 특수문자 제거
        String clipTitle = clipInfoDto.getClipTitle().replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]", " ");
        String OUTPUT_FILE_PATH = location +"\\"+ clipTitle+"_"+aa.format(dtf) +".mp4";
        Path clipPath = Paths.get(OUTPUT_FILE_PATH);
        if (Files.exists(clipPath)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "클립명이 겹칩니다. 같은 클립 다운로드 연타");
        }
        //파일 다운로드
        try(InputStream in = new URL(clipInfoDto.getClipSrcUrl()).openStream()){
            System.out.println(LocalDateTime.now().format(dtf)+" : -------------"+clipTitle+" 다운로드 시작!-------------");
            System.out.println("---------다운로드 경로: "+clipPath+"---------");
            Files.copy(in, clipPath);
            result = 1;
            System.out.println(LocalDateTime.now().format(dtf)+" : -------------다운로드 완료!-------------");
        } catch (MalformedURLException e) {
            System.out.println("영상가져오는데 에러!");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("파일입출력 에러!");
            throw new RuntimeException(e);
        }
        return result;
    }
}
