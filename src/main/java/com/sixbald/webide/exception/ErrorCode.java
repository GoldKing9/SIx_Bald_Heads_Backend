package com.sixbald.webide.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    UNCHECKED_EMAIL_VALID(HttpStatus.BAD_REQUEST, "이메일 중복체크 누락"),
    UNCHECKED_NICKNAME_VALID(HttpStatus.BAD_REQUEST, "닉네임 중복체크 누락")
    ;

    private HttpStatus status;
    private String message;
}
