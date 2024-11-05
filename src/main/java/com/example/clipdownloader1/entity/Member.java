package com.example.clipdownloader1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    //private String imgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    @PrePersist // insert시 동작 / 비영속 -> 영속
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
    @PreUpdate // update시 동작
    public void onUpdate(){
        this.updateAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Clip> clips;

}
