package org.example.rest_back.user.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.rest_back.config.jwt.JwtUtils;
import org.example.rest_back.exception.PasswordNotEqualException;
import org.example.rest_back.user.domain.User;
import org.example.rest_back.exception.InvalidPasswordException;
import org.example.rest_back.exception.UserAlreadyExistsException;
import org.example.rest_back.exception.UserNotFoundException;
import org.example.rest_back.user.dto.*;
import org.example.rest_back.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //SecurityConfig에서 등록한 Bean 으로 DI (pw 암호화 관련)
    private final JwtUtils jwtUtils;

    // 회원가입
    @Transactional
    public void signUp(RegistrationDto requestDto) {
        String userId = requestDto.getUserId();
        String password = requestDto.getPassword();
        String name = requestDto.getName();
        String nickname = requestDto.getNickName();
        String email = requestDto.getEmail();
        String dateOfBirth = requestDto.getDateOfBirth();

        // 아이디 중복 검사를 하지 않고 바로 폼 제출을 누르는 케이스가 존재할수도 있기에
        // 중복 체크 로직을 회원가입에서도 재확인용으로 한번 더 추가.
        Optional<User> checkUserId = userRepository.findByMemberId(userId);

        if (checkUserId.isPresent()) {
            throw new UserAlreadyExistsException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(userId, encodedPassword, name, nickname, email,dateOfBirth);
        userRepository.save(user);
    }

    // 아이디 중복 확인
    public void duplicationCheck(IdDuplicationDto idDuplicationDto) {
        String userID = idDuplicationDto.getUserId();
        Optional<User> checkUserId = userRepository.findByMemberId(userID);

        if (checkUserId.isPresent()) {
            throw new UserAlreadyExistsException("이미 존재하는 아이디입니다.");
        }
    }

    // 로그인
    public void login(LoginDto loginDto, HttpServletResponse response) {
        String userId = loginDto.getUserId();
        String password = loginDto.getPassword();

        User user = userRepository.findByMemberId(userId)
                .orElseThrow(() -> new UserNotFoundException("아이디 혹은 비밀번호가 일치하지 않습니다."));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성 및 응답 헤더에 추가
        String token = jwtUtils.createToken(user);
        response.setHeader(JwtUtils.AUTHORIZATION_HEADER, token);
    }

    // 아이디 찾기
    public String findId(FindIdDto findIdDto) {
        String name = findIdDto.getName();
        String dateOfBirth = findIdDto.getDateOfBirth();

        User user = userRepository.findByNameAndDateOfBirth(name, dateOfBirth)
                .orElseThrow(() -> new UserNotFoundException("입력된 정보의 아이디를 찾지 못했습니다."));
        // Dto 의 성명, 생년월일로 user 를 찾고 없으면 Exceptin Throw
        return user.getMemberId();
    }

    // 비밀번호 변경 전, Dto 를 통해서 아이디 객체 확인
    public User beforeChangePw(BeforeChangePwDto beforeChangePwDto) {

        String userId = beforeChangePwDto.getUserId();
        String name = beforeChangePwDto.getName();
        String dateOfBirth = beforeChangePwDto.getDateOfBirth();

        User user = userRepository.findByMemberIdAndNameAndDateOfBirth(userId, name, dateOfBirth)
                .orElseThrow(() -> new UserNotFoundException("입력된 정보의 아이디를 찾지 못했습니다."));
        // Dto 의 Id, 이름, 생년월일로 user 를 찾고 없으면 Exception Throw

        return user;
    }

    // 비밀번호 변경
    @Transactional
    public void changePw(ChangePwDto changePwDto, User user) {

        String newPw = changePwDto.getNewPw();
        String newPwConfirm = changePwDto.getNewPwConfirm();

        if (newPw.equals(newPwConfirm)) {
            String encodedPassword = passwordEncoder.encode(newPwConfirm);
            user.changePassword(encodedPassword);
        } else {
            throw new PasswordNotEqualException("비밀번호의 두 입력 필드가 일치하지 않습니다.");
        }
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(UserDeleteDto userDeleteDto, String token) {

        String pw = userDeleteDto.getPw();
        String pwConfirm = userDeleteDto.getPwConfirm();

        if (!pw.equals(pwConfirm)) {
            throw new PasswordNotEqualException("비밀번호의 두 입력 필드가 일치하지 않습니다.");
        }

        if (StringUtils.hasText(token)) { // JWT 토큰 있는지 확인
            if (jwtUtils.validateToken(token)) { // JWT 토큰 검증
                 Claims claims = jwtUtils.getUserInfoFromToken(token); // 유효한 토큰에서 사용자 정보 가져오기

                // JWT에서 user_id 추출
                Long userId = claims.get("user_id", Long.class);
                if (userId == null) {
                    throw new UserNotFoundException("유효한 사용자 ID를 찾을 수 없습니다.");
                }
                // user_id를 사용하여 사용자 삭제
                userRepository.deleteById(userId);

            } else {
                throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
            }
        } else { // 해당하는 Else 는 hasText 가  false 일 때 분기하는 부분.
            throw new IllegalArgumentException("사용자 확인에 실패했습니다.");
        }
    }

    // 이메일 변경 ( 마이페이지에서 접근 -> Token 값 필요 )
    @Transactional
    public void changeEmail(EmailDto emailDto, String token) {

        String newEmail = emailDto.getEmail();


        if (StringUtils.hasText(token)) { // JWT 토큰 있는지 확인
            if (jwtUtils.validateToken(token)) { // JWT 토큰 검증
                Claims claims = jwtUtils.getUserInfoFromToken(token); // 유효한 토큰에서 사용자 정보 가져오기

                // 기존의 Email 추출
                String oldEmail = claims.get("email", String.class);

                // 추출한 Email 로 User 객체 탐색
                Optional<User> userOptional = userRepository.findByEmail(oldEmail);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.changeEmail(newEmail);
                } else {
                    throw new IllegalArgumentException("유효한 사용자 ID를 찾을 수 없습니다.");
                }
            } else {
                throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
            }
        } else { // 해당하는 Else 는 hasText 가  false 일 때 분기하는 부분.
            throw new IllegalArgumentException("사용자 확인에 실패했습니다.");
        }
    }
}

