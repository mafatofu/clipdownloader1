package com.example.clipdownloader1.dto;

import com.example.clipdownloader1.entity.Clip;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClipDto {
    private Long id;
    private String streamer;
    private String clipName;
    private String originalUrl;
    private String extractUrl;
    private LocalDateTime createdDateTime;

    public static ClipDto fromEntity(Clip entity){
        return ClipDto.builder()
                .id(entity.getId())
                .streamer(entity.getStreamer())
                .clipName(entity.getClipTitle())
                .originalUrl(entity.getOriginalUrl())
                .extractUrl(entity.getClipSrcUrl())
                .createdDateTime(entity.getCreatedDateTime())
                .build();
    }
}
