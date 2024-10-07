package com.example.clipdownloader1.dto;

import lombok.*;

/**이전 / 다음 페이지 호출 시 필요한 정보를 전달하기 위한 DTO*/
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NextPageDto {
    String clipUid;
    int readCount;
}
