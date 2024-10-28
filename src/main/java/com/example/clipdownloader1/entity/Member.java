package com.example.clipdownloader1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;

import java.time.LocalDate;
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
    private String imgUrl;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Clip> clips;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Likes> Likes;
}
