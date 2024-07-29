package org.example.rest_back.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rest_back.user.dto.EmailDto;
import org.example.rest_back.user.dto.MsgResponseDto;
import org.example.rest_back.user.dto.VerificationCheckDto;
import org.example.rest_back.user.service.VerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;
    private final Map<String, String> verificationCodeMap = new ConcurrentHashMap<>();

    /*
     * 단순 변수 (String)으로 처리했을 때, 문제가 없어 보여도 여러 사용자가 동시에 인증을 보낼 경우
     * 마지막으로 생성된 코드로 덮어씌워짐
     * 또한 컨트롤러 인스턴스 필드로 존재 -> 컨트롤러 인스턴스가 유지되는 동안만 유효함
     * 사용자 별로, 인증 코드를 저장해야 여러 사용자가 동시에 인증 시도에 문제가 생기지 않음 -> HashMap 으로 구현
    */

    // 이메일 인증 코드 전송 API
    @PostMapping("/send")
    public ResponseEntity<MsgResponseDto> sendVerificationCode(@Valid @RequestBody EmailDto emailDto) {
        try {
            String verificationCode = verificationService.sendEmail(emailDto.getEmail());
            verificationCodeMap.put(emailDto.getEmail(), verificationCode);
            // key는 사용자의 이메일, value는 인증코드와 매칭
            return ResponseEntity.ok(new MsgResponseDto("이메일로 인증 코드가 전송되었습니다.", HttpStatus.OK.value()));
        } catch (Exception e) {  // catch MessagingException 및 기타 예외
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MsgResponseDto("이메일 전송 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // 인증 코드 일치 여부 확인 API
    @PostMapping("/check")
    public ResponseEntity<MsgResponseDto> verificationCodeCheck(@RequestBody VerificationCheckDto verificationCheckDto) {

        // Dto 를 통해서 전달받은 email -> 인증번호를 받았다면 해당하는 키 값으로 get
        String verificationCode = verificationCodeMap.get(verificationCheckDto.getEmail());

        // 이후에 Http Request로 사용자가 입력한 문자열과 비교
        if (verificationCode != null && verificationCode.equals(verificationCheckDto.getCode())) {
            return ResponseEntity.ok(new MsgResponseDto("이메일 인증에 성공하였습니다.", HttpStatus.OK.value()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MsgResponseDto("인증 코드가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED.value()));
        }
    }
}
