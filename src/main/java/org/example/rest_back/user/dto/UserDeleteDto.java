package org.example.rest_back.user.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDeleteDto {

    @NotBlank(message = "비밀번호 입력 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String pw;

    @NotBlank(message = "비밀번호 확인 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String pwConfirm;
}
