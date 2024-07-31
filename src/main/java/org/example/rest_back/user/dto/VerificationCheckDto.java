package org.example.rest_back.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerificationCheckDto {
    
    @NotBlank(message = "이메일 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String email;
    @NotBlank(message = "인증번호 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String code;
}
