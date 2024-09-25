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
    private String clipTitle;
    private String clipThumbnailUrl;
    private LocalDateTime downloadTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany(mappedBy = "clip", fetch = FetchType.LAZY)
    private List<Likes> Likes;

}
