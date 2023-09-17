package com.sixbald.webide.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.domain.User;
import com.sixbald.webide.domain.UserRole;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
    private final Key key; // JWT 서명 및 검증에 사용할 키
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JwtUtils(@Value("${jwt.app.jwtSecretKey}")String secretKey) {
        // 주어진 secretKey를 BASE64 디코딩하여 바이트 배열로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // 변환된 바이트 배열로부터 HMAC-SHA 키를 생성
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        this.key = Keys.hmacShaKeyFor(keyBytes); 256비트 이상이 아닌가봄
    }

    public String createAccessTokenFromLoginUser(LoginUser loginUser) {
        return Jwts.builder()
                // 토큰에 포함될 클레임(클레임은 토큰의 내용)을 설정
                .claim("id", loginUser.getUser().getId())
                .claim("email",loginUser.getUser().getEmail())
                .claim("nickname",loginUser.getUser().getNickname())
                .claim("role", loginUser.getUser().getRole())
                // 토큰의 발급 시간을 설정
                .setIssuedAt(new Date())
                // 토큰의 만료 시간을 설정 (질문하기)
                .setExpiration(new Date((new Date()).getTime() + JwtProperties.ACCESS_TOKEN_EXPIRE_TIME))
                // 토큰을 서명함. 서명은 비밀키를 사용하여 토큰의 무결성을 보장
                .signWith(key, SignatureAlgorithm.HS256)
                // 생성된 JWT 토큰을 문자열로 변환하여 반환
                .compact();
    }

    // HTTP 요청에서 JWT 토큰을 파싱하고 헤더에서 추출하는 메서드
    public String parseJwtToken(HttpServletRequest request) {

        // HTTP 요청 헤더에서 "Autorization" 헤더의 값을 가져옴
        String token = request.getHeader(JwtProperties.HEADER_STRING);

        // 가져온 토큰 값이 비어 있지 않고 지정된 접두사(Bearer )로 시작하는 경우
        if (StringUtils.hasText(token) && token.startsWith(JwtProperties.TOKEN_PREFIX)) {
            // 접두사를 제거하여 순수한 JWT 토큰 값ㅇ르 얻음
            token = token.replace(JwtProperties.TOKEN_PREFIX, "");
        }
        return token;
    }

    // JWT 토큰을 유효성 검사하고 결과를 반환하는 메서드
    public boolean validateJwtToken(String token) throws IOException {
        try {
            // JWT 파서를 생성하고, 비밀 키를 사용하여 JWT 토큰을 검증하도록 설정
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            // 토큰이 유효하면 true 반환
            return true;
        } catch (MalformedJwtException e) { // 토큰이 부적절하거나 손상되었을 때 발생하는 예외
            throw new GlobalException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) { // JWT 토큰이 만료되었을 때 발생하는 예외
            throw new GlobalException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (UnsupportedJwtException e) {  // 지원되지 않는 JWT 토큰일 때 발생하는 예외
            throw new GlobalException(ErrorCode.UNSUPPORTED_JWT_TOKEN);
        } finally {
            // JWT 토큰이 유효하지 않은 경우 false를 반환
            return false;
        }
    }

    // 응답에 오류 메시지를 설정하여 응답을 보내는 메서드
//    private void setResponse(HttpServletResponse response, ErrorCode errorCode, Exception e) throws IOException {
//        // 에러 메시지를 로깅
//        log.error("error message {}", errorCode.getMessage(), e);
//        // HTTP 응답의 Content-Type을 JSON으로 설정
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        // HTTP 응답의 문자 인코딩을 UTF-8로 설정
//        response.setCharacterEncoding("UTF-8");
//        // HTTP 응답의 상태 코드를 SC_UNAUTHORIZED(401 Unauthorized)로 설정
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        // ErrorResponse를 생성하고 JSON 형태로 응답
//        Response<GlobalException> fail = Response.error(errorCode.getStatus(), errorCode.getMessage());
//        response.getWriter().write(objectMapper.writeValueAsString(fail));
//    }

    // JWT 토큰을 사용하여 LoginUser 객체를 인증하고 반환 (JWT 토큰을 파싱하고 검증)
    public LoginUser verify(String token) {

        // 토큰을 파싱하고 클레임(claims)을 얻어옴
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 클레임에서 사용자 정보 추출
        Long id = claims.get("id", Long.class);
        String email = claims.get("email", String.class);
        String nickname = claims.get("nickname", String.class);
        String role = claims.get("role", String.class);

        // 추출한 사용자 정보를 이용하여 User 객체를 생성
        User user = User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .role(UserRole.valueOf(role))
                .build();

        // 생성한 User 객체를 사용하여 LoginUser 객체를 생성하고 반환
        return new LoginUser(user);
    }

}
