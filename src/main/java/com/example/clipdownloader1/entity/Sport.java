package com.example.clipdownloader1.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
//객체가 레디스로 저장될때, 레디스의 Hash 자료구조를 통해서 저장되도록 하는 애노테이션
//value : key값 설정.
//timeToLive 객체가 만료되는 시간
@RedisHash(value = "sport", timeToLive = 300)
public class Sport {
    @Id
    private Long id;
    private String name;

}
