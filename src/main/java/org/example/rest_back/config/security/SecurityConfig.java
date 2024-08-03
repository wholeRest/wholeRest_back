package org.example.rest_back.config.security;

import org.example.rest_back.config.jwt.JwtAuthenticationEntryPoint;
import org.example.rest_back.config.jwt.JwtAuthenticationFilter;
import org.example.rest_back.config.jwt.JwtUtils;
import org.example.rest_back.user.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    // SecurityConfig : API 엔드포인트 접근 관련 설정
    private final UserDetailService userDetailService;
    private final JwtUtils jwtUtils;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(UserDetailService userDetailService, JwtUtils jwtUtils, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.userDetailService = userDetailService;
        this.jwtUtils = jwtUtils;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**","/h2-console/**") // 필터체인 적용 범위 설정
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        // 인증이 필요한 경로들 ( 토큰 재발급, 이메일 변경, 아이디 삭제는 토큰 필요 ) 
                        .requestMatchers("/api/auth/refresh", "/api/auth/changeEmail", "/api/auth/deleteUser").authenticated()
                        // 인증 없이 접근 허용할 경로들
                        .requestMatchers("/api/auth/**").permitAll()
                        // 다시 여기서 permitAll 을 하면, 위의 3가지의 설정이 덮어씌워져서, 허용되는거라고 착각할 수 있으나
                        // 인증에 필요한 경로는 우선적으로 명시해주고, permitAll 로 전역설정의 순서를 따라야 한다고 함 (gpt - 확실치않음. 계속 우기는데 의심스러움)
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/verification/**").permitAll() // 이메일 인증 관련 허용
                        // 인증이 필요한 경로들
                        .requestMatchers(
                                "/api/todo/**",
                                "/api/schedule/**",
                                "/api/event/**",
                                "/api/medicine/**",
                                "/api/checkup/**",
                                "/api/caution/**",
                                "/api/posts/**",
                                "/api/likes/**",
                                "/api/comments/**",
                                "/api/images/**"
                        ).authenticated()
                        .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패 시 처리
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안 함
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtils, userDetailService), UsernamePasswordAuthenticationFilter.class); // JWT 필터 등록

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화 방식
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
