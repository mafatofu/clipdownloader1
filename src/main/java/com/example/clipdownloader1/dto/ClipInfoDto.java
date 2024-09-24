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
    String clipSrcUrl;
    private String streamer;
    private String clipName;
    private String originalUrl;
}
