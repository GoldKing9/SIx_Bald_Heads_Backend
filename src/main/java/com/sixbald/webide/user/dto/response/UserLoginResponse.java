package com.sixbald.webide.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginResponse {
    private Long userId;
    private String nickname;
    // TODO UserEntity role 수정 후 반영..
    private String role;
    private String accessToken;

    @Builder
    public UserLoginResponse(Long userId, String nickname, String role, String accessToken) {
        this.userId = userId;
        this.nickname = nickname;
        this.role = "ROLE_USER";
        this.accessToken = accessToken;
    }
}
