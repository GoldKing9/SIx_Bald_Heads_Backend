package com.sixbald.webide.user.dto.response;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String nickname;
    private String email;
    private String imageUrl;
}
