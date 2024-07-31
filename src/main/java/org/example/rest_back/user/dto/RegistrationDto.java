package
org.example.rest_back.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class RegistrationDto {

    @NotBlank(message = "아이디 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String userId;

    @NotBlank(message = "비밀번호 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String password;

    @NotBlank(message = "이름은 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String name;

    @NotBlank(message = "닉네임 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String nickName;

    @NotBlank(message = "이메일 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String email;

    @NotBlank(message = "핸드폰 번호 필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private String phoneNumber;

}
