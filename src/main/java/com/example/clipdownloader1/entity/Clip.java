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
    //다운로드한 시간
    private LocalDateTime downloadedTime;
    //다시 다운로드한 시간
    private LocalDateTime updatedDownloadTime;
    //재생횟수
    private int readCount;

    //사용자 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @PrePersist
    public void onCreate(){
        this.downloadedTime = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {this.updatedDownloadTime = LocalDateTime.now(); }

    public static Clip fromDto(ClipInfoDto dto, Member member){
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
                .downloadedTime(dto.getDownloadedTime())
                .updatedDownloadTime(dto.getUpdatedDownloadTime())
                .readCount(dto.getReadCount())
                .member(member)
                .build();
    }
}
