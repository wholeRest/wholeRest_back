package org.example.rest_back.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailDto {

    @NotBlank(message = "이메일 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;
}
