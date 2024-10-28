package com.example.clipdownloader1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**로그인 / 회원가입 관련 컨트롤러*/
@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    /**로그인 페이지*/
    @GetMapping("/login")
    public String login(
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
    public String join(){
        return "downloader1/join";
    }

}
