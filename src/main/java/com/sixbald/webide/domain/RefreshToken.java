package com.sixbald.webide.domain;

import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.config.jwt.JwtProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;


@RedisHash(value = "refresh") //  이 클래스가 레디스 해시 형식으로 저장될 것임을 나타냄. 레디스 해시의 이름은 refresh
@Getter
public class RefreshToken {
    @Id
    private Long id;
    private LoginUser loginUser; // // 레디스에 value로 저장됨?
    @TimeToLive
    private Long expiration = JwtProperties.REFRESH_TOKEN_EXPIRE_TIME_FROM_REDIS;
    @Indexed // 이 어노테이션이 있어야 해당 필드 값으로 데이터를 찾아올 수 있음
    private String refreshToken; // 레디스에 key로 저장됨

    @Builder
    public RefreshToken(LoginUser loginUser, String refreshToken) {
        this.loginUser = loginUser;
        this.refreshToken = refreshToken;
    }
}
