package com.sixbald.webide.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDTO {
    @Schema(description = "유저 아이디", example = "1")
    private Long userId;
    @Schema(description = "유저 닉네임", example = "sosak")
    private String nickname;
    @Schema(description = "유저 이메일", example = "sosak@gmail.com")
    private String email;
    @Schema(description = "유저 프로필 이미지 url", example = "sosak.jpg")
    private String imageUrl;
}
