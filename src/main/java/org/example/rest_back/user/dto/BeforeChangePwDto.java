package org.example.rest_back.user.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BeforeChangePwDto {

    @NotBlank(message = "아이디 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String userId;

    @NotBlank(message = "성명 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String name;

    @NotBlank(message = "생년월일 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String dateOfBirth;


}
