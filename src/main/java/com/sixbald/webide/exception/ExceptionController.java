package com.sixbald.webide.exception;

import com.sixbald.webide.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = GlobalException.class)
    public ResponseEntity<Response<Void>> handleGlobalExceptionHandler(GlobalException e) {
        log.error("error occur: {}" , e.getStackTrace());
        log.error("error occur: {}" , e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Response<Void>> runtimeExceptionHandler(RuntimeException e) {
        log.error("error occur: {}" , e.getStackTrace());
        log.error("error occur : {}", e.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.error(e.getMessage()));
    }
}
