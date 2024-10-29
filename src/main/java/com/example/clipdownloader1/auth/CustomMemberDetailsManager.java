package com.example.clipdownloader1.auth;

import com.example.clipdownloader1.entity.Authority;
import com.example.clipdownloader1.entity.Member;
import com.example.clipdownloader1.repo.MemberRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomMemberDetailsManager implements UserDetailsManager {
    private final MemberRepo memberRepo;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepo.findByEmail(username);
        if (optionalMember.isEmpty()){
            throw new UsernameNotFoundException(username);
        }
        log.info("user email(id) : {}", optionalMember.get().getEmail());
        Member member = optionalMember.get();

        CustomMemberDetails customMemberDetails = CustomMemberDetails.builder()
                .member(member)
                .build();

        return  customMemberDetails;
    }
    @Override
    public void createUser(UserDetails user) {

        //같은 아이디가 있는지 확인
        if (userExists(user.getUsername())){
            log.info("이미 존재하는 아이디입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //클래스확인 후
        if (user instanceof CustomMemberDetails customMemberDetails){
            Member member = Member.builder()
                    .email(customMemberDetails.getMember().getEmail())
                    .password(passwordEncoder.encode(customMemberDetails.getMember().getPassword()))
                    .nickName(customMemberDetails.getMember().getNickName())
                    .authority(Authority.ROLE_INACTIVE_USER) //비활성사용자로 회원가입
                    .build();

            log.info("회원가입. id : {}",member.getEmail());
            memberRepo.save(member);
        } else {
            throw new IllegalArgumentException("유저 생성 에러! UserDetails를 체크해주세요");
        }
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return memberRepo.existsByEmail(username);
    }

}
