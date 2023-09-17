package com.sixbald.webide.config.utils;

import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.domain.User;
import com.sixbald.webide.exception.CustomEntryPoint;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTime;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwt(LoginUser loginUser) {

        return Jwts.builder()
                .claim("id", loginUser.getUser().getId())
                .claim("email", loginUser.getUser().getEmail())
                .claim("nickname", loginUser.getUser().getNickname())
                .claim("role", "ROLE_USER")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public String parseJwt(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");

        if (StringUtils.hasText(jwt) && jwt.startsWith("Bearer ")) {
            jwt = jwt.replace("Bearer ", "");
        }

        return jwt;
    }

    public boolean validationJwt(String token, HttpServletResponse response) throws IOException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); //토큰에 대한 검증 로직
            return true;
        } catch (ExpiredJwtException e) {
            log.error("토큰 만료 {}", e.getMessage());
            CustomEntryPoint.setResponse(response, "Token Expired", "토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            log.error("토큰 유효성 검사 실패 {}", e.getMessage());
            CustomEntryPoint.setResponse(response, "Invalid Token", "유효하지 않은 토큰");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰 {}", e.getMessage());
            CustomEntryPoint.setResponse(response, "Unsupported Token", "지원하지 않는 토큰");
        } catch (SignatureException e){
            log.error("토큰 변조 {}", e.getMessage());
            CustomEntryPoint.setResponse(response, "Token Signature is tempered", "변조된 토큰");
        }

        return false;
    }

    public LoginUser getUser(String jwt) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        Long id = claims.get("id", Long.class);
        String email = claims.get("email", String.class);
        String nickname = claims.get("nickname", String.class);

        User user = User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .build();

        return new LoginUser(user);
    }
}
