package com.sixbald.webide.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordRequest {
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9]).{8,12}$", message = "유효하지 않은 비밀번호 형식입니다.")
    private String password;
}
