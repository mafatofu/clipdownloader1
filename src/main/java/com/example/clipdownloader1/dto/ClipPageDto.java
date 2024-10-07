package com.example.clipdownloader1.dto;

import lombok.*;

import java.util.List;

/**상위 10개의 클립 페이지 호출 시 필요한 정보를 나타내는 DTO*/
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClipPageDto {
    List<ClipInfoDto> clipInfoDtoList;
    NextPageDto nextPageDto;
}
