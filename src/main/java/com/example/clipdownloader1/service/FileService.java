package com.example.clipdownloader1.service;

import com.example.clipdownloader1.dto.ClipInfoDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**파일입출력 관련 서비스*/
@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final ClipService clipService;
    String clipsLocation = "src/main/resources/tempClips";
    //
    String clipsFilePath = "static/tempClips/";
    @Value("${spring.servlet.multipart.location}")
    String location;

    /**클립 링크를 바로 다운로드하는 기능*/
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
            log.info(LocalDateTime.now().format(dtf)+" : -------------"+clipTitle+" 다운로드 시작!-------------");
            log.info("---------다운로드 경로: "+clipPath+"---------");
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
    /**다운로드 링크 클릭 시 다운로드*/
    public ResponseEntity<Resource> chzzkClipDirectDownloadToProject(
            String clipSrcUrl,
            String clipTitle
    ) throws IOException {
        //클립명에 특수문자 제거
        clipTitle = clipTitle.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]", "");

        //파일 다운로드
        //원본 클립영상을 받아와 resource로 저장.
        //ResponseEntity에 resource와 header값, 상태를 넣어 return
        InputStreamResource resource;
        InputStream in = new URL(clipSrcUrl).openStream();
        //프로젝트로 저장 없이 바로 받아오기
        resource = new InputStreamResource(in);
        log.info("clip title : " + clipTitle);
        log.info("-------------클립 가져오기 완료!-------------");
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+URLEncoder.encode(clipTitle+".mp4", "UTF-8"))
                .body(resource);

    }

    /**정적파일을 가져와 그 파일명과 파일사이즈를 이용하여 httpHeader를 만듦*/
    public HttpHeaders getHttpHeader(Path path, String fileName) throws IOException {
        String contentType = Files.probeContentType(path); // content type setting
        String encodedFilename = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+URLEncoder.encode(fileName, "UTF-8"));
        httpHeaders.add(HttpHeaders.CONTENT_LENGTH, Files.size(path) + "");
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, contentType);
        return httpHeaders;
    }
    /**정적파일 다운로드*/
    public ResponseEntity<Resource> downloadFile(String OUTPUT_FILE_PATH) {
        Path path = Paths.get(OUTPUT_FILE_PATH);

        try{
            HttpHeaders httpHeaders = getHttpHeader(path, path.toFile().getName());
            Resource resource = new UrlResource(path.toUri());

            //Resource resource = new UrlResource(path.toUri());

            //Resource resource = new InputStreamResource(Files.newInputStream(path)); // save file resource
            log.info("resource : " +resource);
            return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }


}
