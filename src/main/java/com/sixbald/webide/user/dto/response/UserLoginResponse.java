package com.sixbald.webide.user.dto.response;

import com.sixbald.webide.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginResponse {
    @Schema(description = "유저 아이디", example = "1")
    private Long userId;
    @Schema(description = "유저 닉네임", example = "최강대머리")
    private String nickname;
    @Schema(description = "유저 역할", example = "ROLE_USER")
    private Role role;
    @Schema(description = "accessToken 정보", example = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZW1haWwiOiJkb3JpYW5Acm9rLnJvayIsIm5pY2tuYW1lIjoi7LWc7ZWY66Gd66GdIiwicm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTY5NDc4NDk2MSwiZXhwIjoxNjk0Nzg0OTcxfQ.cRFMYkBO8E77FxHbyro_m5nmeJkmpKnHATslvAtPRc0")
    private String accessToken;
    private String refreshToken;

    @Builder
    public UserLoginResponse(Long userId, String nickname, Role role, String accessToken, String refreshToken) {
        this.userId = userId;
        this.nickname = nickname;
        this.role = Role.USER;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
