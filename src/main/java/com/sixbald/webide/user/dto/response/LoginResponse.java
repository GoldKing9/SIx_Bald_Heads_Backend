package com.sixbald.webide.user.dto.response;

import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.domain.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
    private Long userId;
    private String nickname;
    private UserRole role;
    private String accessToken;
    private String refreshToken;

    public LoginResponse(LoginUser loginUser, String accessToken, String refreshToken) {
        this.userId = loginUser.getUser().getId();
        this.nickname = loginUser.getUser().getNickname();
        this.role = loginUser.getUser().getRole();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

