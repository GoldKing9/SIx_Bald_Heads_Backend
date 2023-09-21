package com.sixbald.webide.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 입니다"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
    S3BUCKET_ERROR(HttpStatus.UNAUTHORIZED, "S3버킷에서 업로드 또는 삭제에 실패하였습니다"),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    EXPIRED_AUTHENTICATION_TIME(HttpStatus.BAD_REQUEST, "인증시간이 만료되었습니다."),
    MISMATCHED_CODE(HttpStatus.BAD_REQUEST, "코드가 일치하지 않습니다"),
    UNCHECKED_EMAIL_VALID(HttpStatus.BAD_REQUEST, "이메일 중복체크 누락"),
    UNCHECKED_NICKNAME_VALID(HttpStatus.BAD_REQUEST, "닉네임 중복체크 누락"),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레쉬 토큰이 만료되었습니다."),
    ALREADY_USING_PASSWORD(HttpStatus.UNAUTHORIZED, "이미 사용중인 비밀번호 입니다."),
    FOLDER_MOVE_FAIL(HttpStatus.UNAUTHORIZED, "폴더 이동에 실패하였습니다"),
    NOT_FOUND_FOLDER(HttpStatus.UNAUTHORIZED, "폴더가 존재하지 않습니다");
    private HttpStatus status;
    private String message;
}
