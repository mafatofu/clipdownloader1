package com.example.clipdownloader1.dto;

import com.example.clipdownloader1.entity.Likes;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikesDto {
    private Long id;
    private boolean status;
    private LocalDateTime createdDate;
    private Long memberId;
    private Long clipId;

    public static LikesDto entity(Likes entity){
        return LikesDto.builder()
                .id(entity.getId())
                .status(entity.isStatus())
                .createdDate(entity.getCreatedDate())
                .memberId(entity.getMember().getId())
                .clipId(entity.getClip().getId())
                .build();
    }
}
