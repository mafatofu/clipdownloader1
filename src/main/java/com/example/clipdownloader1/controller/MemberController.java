package com.example.clipdownloader1.controller;

import com.example.clipdownloader1.dto.MemberDto;
import com.example.clipdownloader1.dto.ResponseMessage;
import com.example.clipdownloader1.dto.StatusEnum;
import com.example.clipdownloader1.dto.UpdateDto;
import com.example.clipdownloader1.facade.AuthenticationFacade;
import com.example.clipdownloader1.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**로그인 / 회원가입 관련 컨트롤러*/
@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final AuthenticationFacade authFacade;
    /**로그인 페이지*/
    @GetMapping("/login")
    public String loginForm(
            Model model,
            @RequestParam(value="error", required = false)
            String error,
            @RequestParam(value = "exception", required = false)
            String exception
    ){
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "downloader1/loginForm";
    }
    /**회원가입 페이지*/
    @GetMapping("/join")
    public String joinForm(){
        return "downloader1/join";
    }
    /**회원가입*/
    @PostMapping("/join")
    public ResponseEntity<ResponseMessage> join(
            @RequestBody
            MemberDto memberDto
    ){
        ResponseMessage message = new ResponseMessage();
        try {
            memberService.join(memberDto);
            message.setMessage("회원가입 성공");
            message.setStatus(StatusEnum.OK);
        } catch (Exception e){
            log.info("-------------------회원가입 처리 에러발생-------------------");
            e.printStackTrace();
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**마이페이지*/
    @GetMapping("/myPage")
    public String myPage(Model model){
        MemberDto memberDto = memberService.readMember(authFacade.getAuth().getName());
        model.addAttribute("member", memberDto);
        model.addAttribute("userId", authFacade.getAuth().getName());
        return "downloader1/myPage";
    }
    /**마이페이지 수정*/
    @PutMapping("/myPage/update")
    @ResponseBody
    public ResponseEntity<UpdateDto> updateMyPage(
            @RequestBody
            UpdateDto updateDto
    ){
        try {
            memberService.updateMember(authFacade.getAuth().getName(), updateDto);
        } catch (Exception e){
            log.info("------------------------회원정보 수정 에러------------------------");
            e.printStackTrace();
        }
        return new ResponseEntity<UpdateDto>(HttpStatus.OK);

    }
    /**계정 삭제*/
    @DeleteMapping("/myPage/delete/{email}")
    @ResponseBody
    public ResponseEntity<Boolean> deleteMember(
            @PathVariable("email")
            String email,
            HttpServletResponse response,
            HttpSession session
    ){
        boolean isDeleted = false;
        try {
            isDeleted = memberService.deleteMember(email);
            // + 쿠키 삭제
            // 같은 쿠키가 이미 존재하면 덮어쓰기 된다.
            Cookie cookie = new Cookie("JSESSIONID", "");
            cookie.setMaxAge(0); // 0초 생존 -> 삭제
            response.addCookie(cookie); // 요청 객체를 통해서 클라이언트에게 전달
            session.invalidate();//세션무효화
        } catch (Exception e){
            log.info("------------------------회원정보 삭제 에러------------------------");
            e.printStackTrace();
        }
        
        return new ResponseEntity<Boolean>(isDeleted, HttpStatus.OK);

    }

    //닉네임 중복확인
    @PostMapping("/duplicateCkForNickname")
    @ResponseBody
    public boolean duplicateCkForNickname(
            @RequestParam("nickName")
            String nickName
    ){
        return memberService.duplicateCkForNickname(nickName);
    }


    //이메일 중복확인
    @PostMapping("/duplicateCkForEmail")
    @ResponseBody
    public boolean duplicateCkForEmail(
            @RequestParam("email")
            String email
    ){
        return memberService.duplicateCkForEmail(email);
    }

}
