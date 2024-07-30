package org.example.rest_back.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.rest_back.user.service.UserDetailService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // 요청의 JWT를 확인하고, 유효성을 검증 -> 유효한 JWT 가 발견되면 Security Context에 인증 정보 설정
    // 요청이 들어올 때 마다 토큰을 검사하하고 유효성 확인
    // 만료된 토큰의 경우 ExpiredJwtException 을 발생시켜 entryPoint 로 넘어가도록 함

    //EntryPoint 와 Filter 이 둘을 Security 에서 설정해주면 된다.

    private final JwtUtils jwtUtils;
    private final UserDetailService userDetailService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailService userDetailService) {
        this.jwtUtils = jwtUtils;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = jwtUtils.getJwtFromHeader(request);
        // http 요청 헤더에서 JWT를 추출하고

        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // jwt 가 존재하고, ContextHolder에 인증 정보가 없으면 Jwt를 검증 (validate)
            try {
                if (jwtUtils.validateToken(jwt)) {
                    UserDetails userDetails = userDetailService.loadUserByUsername(jwtUtils.getUserInfoFromToken(jwt).getSubject());
                    //loadByUsername 으로 사용자 정보를 가져오고
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //AuthenticationToken 생성
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    // contextHolder 에 해당 설정을 적용
                }
            } catch (ExpiredJwtException e) {
                // 401 상태코드와 메세지 응답
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 만료되었습니다");
                return;
                // HttpServletResponse.SC_UNAUTHORIZED : 401 status 를 의미
                //sendError : 응답을 클라이언트에게 즉시 전달 및 처리 중지

                // Fe : 사용자 인증되지 않음을 확인 ( 로그아웃 혹은 로그인 페이지 재랜더링 )
            } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다.");
            return;
        }
        }

        filterChain.doFilter(request, response);
    }
}
