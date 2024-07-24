package org.example.rest_back.user.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.rest_back.user.config.jwt.JwtUtils;
import org.example.rest_back.user.domain.User;
import org.example.rest_back.user.domain.exception.InvalidPasswordException;
import org.example.rest_back.user.domain.exception.UserAlreadyExistsException;
import org.example.rest_back.user.domain.exception.UserNotFoundException;
import org.example.rest_back.user.dto.LoginDto;
import org.example.rest_back.user.dto.RegistrationDto;
import org.example.rest_back.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //SecurityConfig에서 등록한 Bean 으로 DI (pw 암호화 관련)
    private final JwtUtils jwtUtils;

    // 회원가입
    public void signUp(RegistrationDto requestDto) {
        String userId = requestDto.getUserId();
        String password = requestDto.getPassword();
        String name = requestDto.getName();
        String nickname = requestDto.getNickName();
        String email = requestDto.getEmail();
        String phoneNumber = requestDto.getPhoneNumber();

        // 아이디 중복 확인 로직
        Optional<User> checkUserId = userRepository.findByUserId(userId);

        if (checkUserId.isPresent()) {
            throw new UserAlreadyExistsException("이미 중복된 사용자가 존재합니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(userId, encodedPassword, name, nickname, email, phoneNumber);
        userRepository.save(user);
    }

    // 로그인
    public void login(LoginDto loginDto, HttpServletResponse response) {
        String userId = loginDto.getUserId();
        String password = loginDto.getPassword();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 아이디입니다."));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성 및 응답 헤더에 추가
        String token = jwtUtils.createToken(user);
        response.setHeader(JwtUtils.AUTHORIZATION_HEADER, token);
    }
}
