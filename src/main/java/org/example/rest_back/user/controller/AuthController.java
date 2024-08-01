package org.example.rest_back.user.controller;

import com.amazonaws.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rest_back.config.jwt.JwtUtils;
import org.example.rest_back.exception.UserAlreadyExistsException;
import org.example.rest_back.exception.UserNotFoundException;
import org.example.rest_back.user.domain.User;
import org.example.rest_back.user.dto.*;
import org.example.rest_back.user.service.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final VerificationService verificationService;

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailService userDetailService;
    private final PwRestTokenService pwRestTokenService;

    // 회원가입 API
    @PostMapping("/signUp")
    public ResponseEntity<MsgResponseDto> signUp(@RequestBody @Valid RegistrationDto registrationDto) {
        // 중복 확인을 하지 않고 회원가입 하는 케이스 핸들링 추가
        try {
            userService.signUp(registrationDto);
            return ResponseEntity.ok(new MsgResponseDto("회원가입이 완료되었습니다.", HttpStatus.OK.value()));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MsgResponseDto("이미 존재하는 아이디입니다.", HttpStatus.CONFLICT.value()));
        }
    }
    // 아이디 중복 검사 API
    @PostMapping("/duplicationCheck")
    public ResponseEntity<MsgResponseDto> duplicationCheck(@RequestBody @Valid IdDuplicationDto idDuplicationDto) {
        try {
            userService.duplicationCheck(idDuplicationDto);
            // 만약에 서비스 로직에서 예외가 발생한다면 -> 아래 캐치를 통해서 예외처리
            return ResponseEntity.ok(new MsgResponseDto("사용 가능한 아이디입니다.", HttpStatus.OK.value()));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MsgResponseDto("이미 존재하는 아이디입니다.", HttpStatus.CONFLICT.value()));
        }
    }

    // 로그인 API
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

        // access Token 과 Refresh Token 을 생성하고
        String jwt = jwtUtils.createToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        HttpHeaders headers = new HttpHeaders();
        // HttpHeaders 객체를 생성하고
        headers.set(jwtUtils.AUTHORIZATION_HEADER, jwtUtils.BEARER_PREFIX + jwt);
        // "Authorization": "Bearer <토큰>" 형식으로 헤더를 추가
        // Authorization_header : Authorization ( JWT 토큰을 담기 위한 표준 헤더 양식 )
        // Bearer_prefix : bearer ( 토큰 유형 명시 접두사 ) -> 즉 토큰을 응답 헤더에 포함시킴.

        return ResponseEntity.ok(new JwtResponseDto("로그인에 성공하였습니다.", HttpStatus.OK.value(),jwt,refreshToken));
    }

    // 아이디 찾기 API
    @PostMapping("/findId")
    public ResponseEntity<MsgResponseDto> findId(@RequestBody @Valid FindIdDto findIdDto) {
        try{
            String idFound = userService.findId(findIdDto);
            return ResponseEntity.ok(new MsgResponseDto(idFound, HttpStatus.OK.value()));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MsgResponseDto("입력하신 정보의 아이디를 찾지 못했습니다.", HttpStatus.BAD_REQUEST.value()));
        }
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


    // 비밀번호 변경 (1) ( 아이디, 성명, 생년월일 필드를 통한 사용자 확인 )
    @PostMapping("/beforeChangePw")
    public ResponseEntity<Map<String, Object>> beforeChangePw(@RequestBody @Valid BeforeChangePwDto beforeChangePwDto) {
        try {
            User user = userService.beforeChangePw(beforeChangePwDto);
            String resetToken = UUID.randomUUID().toString();
            pwRestTokenService.saveToken(resetToken, user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "사용자 확인 성공, 이메일 인증을 진행하세요.");
            response.put("status", HttpStatus.OK.value());
            response.put("token", resetToken);

            return ResponseEntity.ok(response);

            // 기존의 MsgResponseDto에는 해당 token 을 넣을수가 없어서 일단 급한대로 하고
            // 후에 dto 다시 만들어서 리팩토링 할것.

        } catch (UserNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "입력하신 정보의 사용자를 찾을 수 없습니다.");
            response.put("status", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 비밀번호 변경 (2) 는 이메일 인증 ( 기존 로직 활용 )

    // 이후 비밀번호 변경 (3) : 실질적인 변경
    @PostMapping("/pwReset")
    public ResponseEntity<MsgResponseDto> resetPassword(@RequestBody @Valid ChangePwDto changePwDto) {
        try {
            User user = pwRestTokenService.getUserByToken(changePwDto.getResetToken());
            // 토큰 값으로 사용자 찾고 토큰값을 넣지 않았다면 401 return

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MsgResponseDto("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED.value()));
            }

            userService.changePw(changePwDto, user);
            return ResponseEntity.ok(new MsgResponseDto("비밀번호가 성공적으로 변경되었습니다.", HttpStatus.OK.value()));
        } catch (Exception e) {
            // 그 외의 다른 Exception 들은 해당 블록에서 전부 처리.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MsgResponseDto("비밀번호 변경 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

}
