package com.example.clipdownloader1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;

import java.util.List;
/**멤버 정보*/
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String nickName;
    private Authority authority;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Clip> clips;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Likes> Likes;
}
