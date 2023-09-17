package com.sixbald.webide.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {
    @Schema(description = "유저 이메일", example = "skinhead@bald.com")
    private String email;
    @Schema(description = "유저 비밀번호", example = "blingbling12")
    private String password;
}
