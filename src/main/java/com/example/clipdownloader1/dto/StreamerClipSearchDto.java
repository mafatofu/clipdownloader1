package com.example.clipdownloader1.dto;

import lombok.*;
/**스트리머 이름으로 클립 여러개를 나타내는 url요청 시 사용하는 DTO*/
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StreamerClipSearchDto {
    String streamerName;
    String streamerUid;
    String orderType;
    int size;
    String clipUID;
    int readCount;
}
