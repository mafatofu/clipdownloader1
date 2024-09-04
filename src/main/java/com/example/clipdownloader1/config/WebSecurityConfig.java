package com.example.clipdownloader1.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                //사이트간 요청위조 방지
                //Get이 아닌 요청 시 csrf토큰여부를 확인
                //CsrfToken을 쿠키에 보관하여 JavaScript 기반 응용 프로그램을 지원
//                .csrf((csrf) -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(
                                        "/",
                                        "/clipDownload",
                                        //템플릿 관련
                                        "/css/**",
                                        "/js/**",
                                        "/fonts/**",
                                        "/img/**"
                                ).permitAll()
                );
        return http.build();
    }
}
