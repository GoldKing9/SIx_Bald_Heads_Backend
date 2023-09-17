package com.sixbald.webide.user.dto.request;

import com.sixbald.webide.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignupRequest {

    @Schema(description = "유저 이메일", example = "skinhead@bald.com")
    @Email(message = "유효하지 않은 이메일 형식입니다.",
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;
    @Schema(description = "유저 비밀번호", example = "blingbling12")
    @NotEmpty(message = "비밀번호 입력은 필수 입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9]).{8,12}$", message = "유효하지 않은 비밀번호 형식입니다.")
    private String password;
    @Schema(description = "유저 닉네임", example = "최강대머리")
    @NotEmpty(message = "닉네임 입력은 필수 입니다.")
    @Pattern(regexp = "^[A-Za-z0-9가-힇]{4,10}$", message = "유효하지 않은 닉네임 형식입니다.")
    private String nickname;
    private boolean emailValid;
    private boolean nicknameValid;

    @Builder
    public UserSignupRequest(String email, String password, String nickname, boolean emailValid, boolean nicknameValid) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.emailValid = emailValid;
        this.nicknameValid = nicknameValid;
    }

    public User toEntity(){
        return User.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .build();
    }
}
