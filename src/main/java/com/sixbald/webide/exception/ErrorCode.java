package com.sixbald.webide.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    EXPIRED_AUTHENTICATION_TIME(HttpStatus.BAD_REQUEST, "인증시간이 만료되었습니다."),
    MISMATCHED_CODE(HttpStatus.BAD_REQUEST, "코드가 일치하지 않습니다");


    private HttpStatus status;
    private String message;
}
