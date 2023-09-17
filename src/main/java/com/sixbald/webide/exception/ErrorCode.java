package com.sixbald.webide.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 입니다"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    S3BUCKET_ERROR(HttpStatus.UNAUTHORIZED, "S3버킷에서 업로드 또는 삭제에 실패하였습니다");

    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    EXPIRED_AUTHENTICATION_TIME(HttpStatus.BAD_REQUEST, "인증시간이 만료되었습니다."),
    MISMATCHED_CODE(HttpStatus.BAD_REQUEST, "코드가 일치하지 않습니다");



    private HttpStatus status;
    private String message;
}
