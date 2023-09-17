package com.sixbald.webide.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일 인증을 완료해주세요."),
    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "닉네임 중복검사를 완료해주세요."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "아이디가 일치하지 않습니다."),
    PASSWORD_NOT_FOUND(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레시 토큰 시간이 만료되었습니다."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다.");


    private HttpStatus status;
    private String message;
}
