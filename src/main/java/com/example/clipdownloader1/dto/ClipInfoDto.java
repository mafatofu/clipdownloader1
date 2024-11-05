package com.example.clipdownloader1.dto;

import com.example.clipdownloader1.entity.Clip;
import lombok.*;

import java.time.LocalDateTime;

/**프론트단에서 클립 직접 다운로드 시 url 정보를 가져오는 DTO*/
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClipInfoDto {
    private Long id;
    //원본 vod url
    private String clipSrcUrl;
    private String streamer;
    private String clipperName;
    //치지직 기본 클립 url
    private String originalUrl;
    //치지직 videoId
    private String videoId;
    private String clipTitle;
    private String clipThumbnailUrl;
    //클립생성시간
    private LocalDateTime createdDateTime;
    //다운로드한 시간
    private LocalDateTime downloadedTime;
    //다시 다운로드한 시간
    private LocalDateTime updatedDownloadTime;
    //재생횟수
    private int readCount;
    private Long memberId;

    public static ClipInfoDto fromEntity(Clip entity){
        return ClipInfoDto.builder()
                .id(entity.getId())
                .clipSrcUrl(entity.getClipSrcUrl())
                .streamer(entity.getStreamer())
                .clipperName(entity.getClipperName())
                .originalUrl(entity.getOriginalUrl())
                .videoId(entity.getVideoId())
                .clipTitle(entity.getClipTitle())
                .clipThumbnailUrl(entity.getClipThumbnailUrl())
                .createdDateTime(entity.getCreatedDateTime())
                .downloadedTime(entity.getDownloadedTime())
                .updatedDownloadTime(entity.getUpdatedDownloadTime())
                .readCount(entity.getReadCount())
                .memberId(entity.getMember().getId())
                .build();

    }
}
