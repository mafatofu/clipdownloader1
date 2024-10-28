package com.example.clipdownloader1.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AuthenticationFailureHandler customFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                //사이트간 요청위조 방지
                //Get이 아닌 요청 시 csrf토큰여부를 확인
                //CsrfToken을 쿠키에 보관하여 JavaScript 기반 응용 프로그램을 지원
//                .csrf((csrf) -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .formLogin(formLogin -> formLogin
                        // 로그인 페이지
                        .loginPage("/login")
                        // "/login" 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행.
                        // 결과적으로 컨트롤러에 따로 "/login"을 구현하지 않아도 괜찮다.
                        // 이 로그인 과정에서 필요한 것이 있기 때문에 auth 패키지를 파서 PrincipalDetails 을 만들어줘야한다.
                        .loginProcessingUrl("/login")
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                //인증성공시
                                response.sendRedirect("/");
                            }
                        })
                        // 인증/인가 실패시 작동하는 핸들러
                        .failureHandler(customFailureHandler)
                )
                .logout(logout -> logout
                        //로그아웃 url
                        .logoutUrl("/logout")

                )
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(
                                        //기본 url
                                        "/",
                                        //aws 상태 체크
                                        "/healthcheck",
                                        //회원가입페이지
                                        "/join",
                                        //로그인
                                        "/login",
                                        //클립동영상띄워주기
                                        "/clipDownload",
                                        //멀티 다운로드
                                        "/multiDownload",
                                        //파일 직접다운로드
                                        "/clipDownloadDirect",
                                        "/clipDownloadDirect/{clipUid}",
                                        //스트리머로 검색
                                        "/multiDownload/{streamerName}/{orderType}",
                                        //상위 클립 이전 / 다음페이지
                                        "/multiDownload/{streamerName}/{orderType}/{pageCount}",
                                        //여러 개의 파일을 한번에 다운로드
                                        "/multiDownload/clipDownloadDirectMulti",
                                        //템플릿 관련
                                        "/css/**",
                                        "/js/**",
                                        "/fonts/**",
                                        "/img/**",
                                        //클립 관련
                                        "/tempClips/**"
                                ).permitAll()

                );


        return http.build();

    }
}
