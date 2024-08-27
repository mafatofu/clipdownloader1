package com.example.clipdownloader1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Builder
@DynamicInsert //insert 시 명시해주지 않아도 기본값 적용되도록
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clipName;
    private String clipMaker;
    private String streamer;
}
