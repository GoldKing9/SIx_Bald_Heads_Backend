package com.sixbald.webide.user.dto.request;

import com.sixbald.webide.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupRequest {

    @NotBlank(message = "필수 입력사항입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;
    @NotBlank(message = "필수 입력사항입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,12}$", message = "비밀번호는 8~12자의 숫자와 대소문자로만 구성되어야 합니다.")
    private String password;
    @NotBlank(message = "필수 입력사항입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{4,10}$", message = "닉네임 형식에 맞게 작성해주세요.")
    private String nickname;
    private boolean emailValid;
    private boolean nicknameValid;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .password(encodedPassword)
                .build();
    }
}
