package org.example.rest_back.user.controller;

import jakarta.validation.Valid;
import org.example.rest_back.user.config.jwt.JwtUtils;
import org.example.rest_back.user.domain.exception.UserAlreadyExistsException;
import org.example.rest_back.user.dto.*;
import org.example.rest_back.user.service.RefreshTokenService;
import org.example.rest_back.user.service.UserDetailService;
import org.example.rest_back.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailService userDetailService;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          RefreshTokenService refreshTokenService, UserDetailService userDetailService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.userDetailService = userDetailService;
    }

    // 회원가입 
    @PostMapping("/signUp")
    public ResponseEntity<MsgResponseDto> signUp(@RequestBody @Valid RegistrationDto registrationDto) {
        // 중복 확인을 하지 않고 회원가입 하는 케이스 핸들링 추가
        try {
            userService.signUp(registrationDto);
            return ResponseEntity.ok(new MsgResponseDto("회원가입이 완료되었습니다.", HttpStatus.OK.value()));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MsgResponseDto("이미 존재하는 아이디입니다.",HttpStatus.CONFLICT.value()));
        }
    }
    @PostMapping("/duplicationCheck")
    public ResponseEntity<MsgResponseDto> duplicationCheck(@RequestBody @Valid IdDuplicationDto idDuplicationDto) {
        try {
            userService.duplicationCheck(idDuplicationDto);
            // 만약에 서비스 로직에서 예외가 발생한다면 -> 아래 캐치를 통해서 예외처리
            return ResponseEntity.ok(new MsgResponseDto("사용 가능한 아이디입니다.",HttpStatus.OK.value()));
        }catch(UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MsgResponseDto("이미 존재하는 아이디입니다.",HttpStatus.CONFLICT.value()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUserId(),
                        loginDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtUtils.createToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        return ResponseEntity.ok(new JwtResponseDto("로그인에 성공하였습니다.", HttpStatus.OK.value(),jwt,refreshToken));
    }


    // 액세스 토큰 만료 이후, 재발급 받으려면 만료된 Access Token을 감지 (프론트단 작업) 및
    // 이를 Refresh Token을 사용해 새로운 Access Token을 발급
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refreshAccessToken(@RequestBody RefreshTokenRequestDto requestDto) {

        String refreshToken = requestDto.getRefreshToken();
        // refreshToken 을 받아서
        boolean isValid = refreshTokenService.validateRefreshToken(refreshToken);
        // 해당 리프레시 토큰이 유효한지 판단하고
        if (!isValid) {
            // 유효하지 않을때
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtResponseDto("유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED.value(), null, null));
        }

        //유효하다면, refreshToken으로 부터 유저 정보를 받아와 새로운 access 토큰 return
        String userId = refreshTokenService.getUserIdFromRefreshToken(refreshToken);
        UserDetails userDetails = userDetailService.loadUserByUsername(userId);
        String newAccessToken = jwtUtils.createToken(userDetails);

        return ResponseEntity.ok(new JwtResponseDto("새로운 엑세스 토큰이 발급되었습니다", HttpStatus.OK.value(), newAccessToken, refreshToken));
    }
}
