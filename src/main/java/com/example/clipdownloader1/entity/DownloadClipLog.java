package com.example.clipdownloader1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/**회원들이 다운받은 클립에 대한 정보*/
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "downloadLog")
public class DownloadClipLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Clip clip;

    //다운로드한 시간
    private LocalDateTime downloadedTime;

    //다시 다운로드한 시간
    private LocalDateTime updatedDownloadTime;

    @PrePersist
    public void onCreate(){
        this.downloadedTime = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {this.updatedDownloadTime = LocalDateTime.now(); }

    public static DownloadClipLog fromOthers(Member member, Clip clip){
        return DownloadClipLog.builder()
                .member(member)
                .clip(clip)
                .build();
    }
}
