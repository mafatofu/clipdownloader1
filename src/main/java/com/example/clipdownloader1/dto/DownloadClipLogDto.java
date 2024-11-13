package com.example.clipdownloader1.dto;

import com.example.clipdownloader1.entity.DownloadClipLog;
import lombok.*;

import java.time.LocalDateTime;

/**회원들이 다운받은 클립에 대한 정보 dto*/
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DownloadClipLogDto {
    private Long id;
    private Long memberId;
    private Long clipId;
    //다운로드한 시간
    private LocalDateTime downloadedTime;
    //다시 다운로드한 시간
    private LocalDateTime updatedDownloadTime;

    public static DownloadClipLogDto fromEntity(DownloadClipLog entity){
        return DownloadClipLogDto.builder()
                .id(entity.getId())
                .memberId(entity.getMember().getId())
                .clipId(entity.getClip().getId())
                .downloadedTime(entity.getDownloadedTime())
                .updatedDownloadTime(entity.getUpdatedDownloadTime())
                .build();
    }
}
