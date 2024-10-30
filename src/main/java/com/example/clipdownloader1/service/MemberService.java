package com.example.clipdownloader1.service;

import com.example.clipdownloader1.auth.CustomMemberDetails;
import com.example.clipdownloader1.auth.CustomMemberDetailsManager;
import com.example.clipdownloader1.dto.MemberDto;
import com.example.clipdownloader1.dto.UpdateDto;
import com.example.clipdownloader1.entity.Member;
import com.example.clipdownloader1.repo.MemberRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepo memberRepo;
    private final PasswordEncoder passwordEncoder;
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
    /**이메일로 회원정보 하나 dto로 가져오기*/
    public MemberDto readMember(String email){
        Optional<Member> optionalMember = memberRepo.findByEmail(email);
        if(optionalMember.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        Member member = optionalMember.get();
        return MemberDto.fromEntity(member);
    }
    /**이메일로 회원정보 하나 entity로 가져오기*/
    public Member readMemberToEntity(String email){
        Optional<Member> optionalMember = memberRepo.findByEmail(email);
        if(optionalMember.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        return optionalMember.get();
    }



    @Transactional
    public void updateMember(String email, UpdateDto updateDto){
        Member member = readMemberToEntity(email);
        //null과 공백 체크 and 값이 변경되었다면 수정
        if (!ObjectUtils.isEmpty(updateDto.getPassword()) &&
                !updateDto.getPassword().equals(member.getPassword())) {
            member.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }
        if (!ObjectUtils.isEmpty(updateDto.getNickName()) &&
                !updateDto.getNickName().equals(member.getNickName())) {
            member.setNickName(updateDto.getNickName());
        }
        memberRepo.save(member);
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
