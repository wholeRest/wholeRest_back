package org.example.rest_back.user.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.rest_back.config.jwt.JwtUtils;
import org.example.rest_back.exception.InvalidPasswordException;
import org.example.rest_back.exception.UserAlreadyExistsException;
import org.example.rest_back.exception.UserNotFoundException;
import org.example.rest_back.user.domain.User;
import org.example.rest_back.user.dto.IdDuplicationDto;
import org.example.rest_back.user.dto.LoginDto;
import org.example.rest_back.user.dto.RegistrationDto;
import org.example.rest_back.user.repository.UserRepository;
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

        // 아이디 중복 검사를 하지 않고 바로 폼 제출을 누르는 케이스가 존재할수도 있기에
        // 중복 체크 로직을 회원가입에서도 재확인용으로 한번 더 추가.
        Optional<User> checkUserId = userRepository.findByMemberId(userId);

        if (checkUserId.isPresent()) {
            throw new UserAlreadyExistsException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(userId, encodedPassword, name, nickname, email, phoneNumber);
        userRepository.save(user);
    }

    // 아이디 중복 확인
    public void duplicationCheck(IdDuplicationDto idDuplicationDto){
        String userID = idDuplicationDto.getUserId();
        Optional<User> checkUserId = userRepository.findByMemberId(userID);

        if(checkUserId.isPresent()){
            throw new UserAlreadyExistsException("이미 존재하는 아이디입니다.");
        }
    }

    // 로그인
    public void login(LoginDto loginDto, HttpServletResponse response) {
        String userId = loginDto.getUserId();
        String password = loginDto.getPassword();

        User user = userRepository.findByMemberId(userId)
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
