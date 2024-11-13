package com.example.clipdownloader1.entity;

import com.example.clipdownloader1.dto.ClipInfoDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;
/**멤버가 다운받은 클립에 대한 정보*/
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "clip")
public class Clip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //다운로드 가능한 원본 vod url
    private String clipSrcUrl;
    //스트리머 닉네임
    private String streamer;
    //클립퍼 닉네임
    private String clipperName;
    //클립 원본 url
    private String originalUrl;
    //치지직 videoId
    private String videoId;
    //클립명
    private String clipTitle;
    //클립 썸네일 url
    private String clipThumbnailUrl;
    //클립생성 시간
    private LocalDateTime createdDateTime;
    //재생횟수
    private int readCount;

    public static Clip fromDto(ClipInfoDto dto){
        return Clip.builder()
                .id(dto.getId())
                .clipSrcUrl(dto.getClipSrcUrl())
                .streamer(dto.getStreamer())
                .clipperName(dto.getClipperName())
                .originalUrl(dto.getOriginalUrl())
                .videoId(dto.getVideoId())
                .clipTitle(dto.getClipTitle())
                .clipThumbnailUrl(dto.getClipThumbnailUrl())
                .createdDateTime(dto.getCreatedDateTime())
                .readCount(dto.getReadCount())
                .build();
    }
}
