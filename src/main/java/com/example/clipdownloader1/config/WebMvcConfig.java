package com.example.clipdownloader1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    // 개발 시점에 사용 가능한 코드.
// 개발 시점에 사용 가능한 코드.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // “*“같은 와일드카드를 사용
                .allowedMethods("GET", "POST") // 허용할 HTTP method
                .allowedHeaders("*")
                .exposedHeaders("Content-Disposition")
                .allowCredentials(true); // 쿠키 인증 요청 허용
    }
    // 배포 시점에 사용 가능한 코드.
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry
//                .addResourceHandler("/files/**")
//                //.addResourceLocations("file:/opt/files/");
//                // 윈도우라면
//                .addResourceLocations("file:///C:/opt/files/");
//
//    }
}
