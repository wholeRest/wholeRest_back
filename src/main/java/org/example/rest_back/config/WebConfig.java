package org.example.rest_back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    // 우선 개발환경 -> 전역적으로 모든 Cors 해제
    // 나중에 프론트가 배포하면 -> allowedOrigins 로 도메인 허용하는 부분 수정.
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // 모든 도메인 허용
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                        .allowedHeaders("*");
                // allowCredentials(true) -> 모든 도메인 허용 시 사용 불가
            }
        };
    }
}
