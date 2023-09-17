package com.sixbald.webide.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RequestNickname {
    @Schema(description = "유저 닉네임", example = "sosak")
    private String nickname;
    private boolean nicknameValid;
}
