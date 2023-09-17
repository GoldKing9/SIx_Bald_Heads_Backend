package com.sixbald.webide.domain;

import com.sixbald.webide.config.auth.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@AllArgsConstructor
@Getter
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 3)
public class RefreshToken {

    @Id
    private String id;
    private LoginUser loginUser;

    @Indexed
    private String refreshToken;
}
