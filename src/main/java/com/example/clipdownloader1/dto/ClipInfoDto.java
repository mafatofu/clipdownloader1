package com.example.clipdownloader1.dto;

import lombok.*;

import java.time.LocalDateTime;

/**프론트단에서 클립 직접 다운로드 시 url 정보를 가져오는 DTO*/
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClipInfoDto {
    //원본 vod url
    private String clipSrcUrl;
    private String streamer;
    private String clipperName;
    //치지직 기본 클립 url
    private String originalUrl;
    private String clipTitle;
    private String clipThumbnailUrl;
    private LocalDateTime createdDateTime;

}
