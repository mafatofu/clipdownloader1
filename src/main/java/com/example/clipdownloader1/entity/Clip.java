package com.example.clipdownloader1.entity;

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
    private String clipSrcUrl;
    private String streamer;
    private String clipperName;
    private String originalUrl;
    //치지직 videoId
    private String videoId;
    //클립명
    private String clipTitle;
    //클립 썸네일 url
    private String clipThumbnailUrl;
    //생성일
    private LocalDateTime createdDateTime;
    //다운로드한 시간
    private LocalDateTime downloadedTime;
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
}
