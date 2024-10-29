package com.example.clipdownloader1.dto;

import com.example.clipdownloader1.entity.Authority;
import com.example.clipdownloader1.entity.Clip;
import com.example.clipdownloader1.entity.Likes;
import com.example.clipdownloader1.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

//dto
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String password;
    private String nickName;
    private Authority authority;
    //private String imgUrl;
    private List<Clip> clips;
    private List<Likes> likes;
    public static MemberDto fromEntity(Member entity){
        return MemberDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .nickName(entity.getNickName())
                .authority(entity.getAuthority())
                //.imgUrl(entity.getImgUrl())
                .clips(entity.getClips())
                .likes(entity.getLikes())
                .build();
    }
}
