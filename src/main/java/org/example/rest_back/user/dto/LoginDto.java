package org.example.rest_back.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginDto {
    @NotBlank(message = "아이디 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String userId;

    @NotBlank(message = "비밀번호 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String password;
}
