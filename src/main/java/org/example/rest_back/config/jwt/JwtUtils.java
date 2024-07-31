package org.example.rest_back.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.rest_back.user.domain.User;
import org.example.rest_back.user.service.UserDetailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Service
public class JwtUtils {
    public static final String AUTHORIZATION_HEADER = "Authorization"; // HTTP 요청 헤더에서 사용되는 헤더의 이름
    public static final String BEARER_PREFIX = "Bearer "; // JWT 토큰이 Bearer 접두어와 함께 제공됨
    private static final long TOKEN_EXPIRATION_TIME = 60 * 60 * 1000L; // 1시간

    @Value("${jwt.secret.key}")
    private String secretKey; // application.properties 또는 application.yml에서 설정된 비밀 키

    private static Key key; // 비밀 키를 Key 객체로 변환
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256; // HS256 해시 알고리즘

    @PostConstruct
    public void init() {
        // 빈 인스턴스 생성 후 DI 이후 호출
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes); // Base64로 인코딩된 비밀 키를 Key 객체로 변환
    }

    public String createToken(UserDetails userDetails) {
        // User 객체를 통해 JWT 토큰 생성
        Date now = new Date();

        // User 객체로 변환
        User user = (User) userDetails;

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // 사용자 ID를 주제로 설정
                .claim("email", user.getEmail()) // 이메일을 클레임으로 추가
                .claim("user_id", user.getUser_id()) // user_id를 클레임으로 추가 ( 사용자 일련번호 )
                .setExpiration(new Date(now.getTime() + TOKEN_EXPIRATION_TIME)) // 토큰 만료 시간 설정
                .setIssuedAt(now) // 토큰 발급 시간 설정
                .signWith(key, SIGNATURE_ALGORITHM) // 서명 설정
                .compact(); // JWT 생성
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        // HTTP 요청에서 JWT 추출
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        // 토큰은 Authorization 헤더에 포함되어있음.
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            // Bearer 접두어로 시작하는지 확인하고
            return bearerToken.substring(BEARER_PREFIX.length()); // 접두어를 제거한 후 반환
        }
        return null;
    }

    public boolean validateToken(String token) {
        // jwt 유효성 검사
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // 서명이 유효한지 검사
            return true;
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었다면, ExpiredJwtException을 던지고
            log.error("Expired JWT token, 만료된 JWT 토큰입니다.");
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        // Jwt 의 페이로드를 return 하는 형태
        // ( 페이로드 : 토큰에서 사용할 정보의 조각들 Claim )
        // 여기서 메소드의 반환형은 Claims. 여기서 Claims 은 토큰을 만들때의 claim 값만 지칭하는것이 아님 !
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Authentication getAuthentication(String token, UserDetailService userDetailService) {
        // 토큰 기반으로 인증 정보 가져오기
        Claims claims = getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        // UserDetailsService를 통해 UserDetails 객체를 가져와서
        UserDetails userDetails = userDetailService.loadUserByUsername(memberId);

        // 인증된 사용자 반환.
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null, // 두번째 파라미터 : credential(자격증명)
                //JWT 에서는 credential 관련 설정을 사용하지 않기 때문에 null!
                userDetails.getAuthorities() // 사용자 권한을 설정.
        );
    }
}
