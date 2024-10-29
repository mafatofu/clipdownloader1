package com.example.clipdownloader1.service;

import com.example.clipdownloader1.auth.CustomMemberDetails;
import com.example.clipdownloader1.auth.CustomMemberDetailsManager;
import com.example.clipdownloader1.dto.MemberDto;
import com.example.clipdownloader1.entity.Member;
import com.example.clipdownloader1.repo.MemberRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepo memberRepo;
    private final CustomMemberDetailsManager customMemberDetailsManager;
    /**회원가입 서비스*/
    @Transactional
    public void join(MemberDto memberDto){
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(memberDto.getPassword())
                .nickName(memberDto.getNickName())
                .build();

        customMemberDetailsManager.createUser(
            CustomMemberDetails.builder()
                    .member(member)
                    .build()
        );
    }

    //닉네임 중복확인용
    public boolean duplicateCkForNickname(String nickName) {
        //중복확인용
        return memberRepo.existsByNickName(nickName);

    }
    //이메일 중복확인용
    public boolean duplicateCkForEmail(String email) {
        //중복확인용
        return memberRepo.existsByEmail(email);

    }
}
