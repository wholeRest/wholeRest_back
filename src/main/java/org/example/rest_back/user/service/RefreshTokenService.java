package org.example.rest_back.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.rest_back.config.jwt.JwtUtils;
import org.example.rest_back.user.domain.RefreshToken;
import org.example.rest_back.user.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils; // JwtUtils를 통해 토큰 검증 및 생성
    private final UserDetailService userDetailService;

    private static final Duration REFRESH_TOKEN_EXPIRATION_TIME = Duration.ofDays(1); // 7일

    @Transactional
    public String createRefreshToken(String userId) {
        // Refresh Token 생성 및 데이터베이스에 생성
        String token = UUID.randomUUID().toString(); // 유니크한 토큰 생성
        LocalDateTime expiryDate = LocalDateTime.now().plus(REFRESH_TOKEN_EXPIRATION_TIME);
        // 만료일 설정 ( 현재 시간으로 부터 1일 ) 

        RefreshToken refreshToken = new RefreshToken(token, userId, expiryDate);
        refreshTokenRepository.save(refreshToken);

        return token;
    }

    public boolean validateRefreshToken(String token) {
        // 주어진 리프레시 토큰이 유효한지 확인하는 로직
        return refreshTokenRepository.findByToken(token)
                .map(refreshToken -> refreshToken.getExpiryDate().isAfter(LocalDateTime.now()))
                .orElse(false);
        // Optional 객체의 값을 변환할 때 map 사용
        // Optional 객체가 값을 가지고 있을 때 특정 함수 처리를 통해 새로운 Optional 반환
        // 비어있다면 그대로 비어있는 객체 반환

    }

    public Optional<RefreshToken> getRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public String getUserIdFromRefreshToken(String refreshToken) {
        // 주어진 Refresh 토큰에서 사용자 id 를 추출.
        // ( 리프레시 토큰이 유효한 경우 호출 )
        return refreshTokenRepository.findByToken(refreshToken)
                .map(RefreshToken::getUserId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));
    }

}