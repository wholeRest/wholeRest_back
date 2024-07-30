package org.example.rest_back.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailDto {

    @NotBlank(message = "이메일은 필수 입력 필드입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;
}
