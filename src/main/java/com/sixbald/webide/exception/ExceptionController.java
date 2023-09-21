package com.sixbald.webide.exception;

import com.sixbald.webide.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<Response<Void>> IOExceptionHandler(IOException e) {
        log.error("error occur: {}" , e.getStackTrace());
        log.error("error occur : {}", e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(e.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<List<Response<Void>>> argsValidHandler(MethodArgumentNotValidException e) {
        log.error("error occur: {}" , e.getStackTrace());
        log.error("error occur : {}", e.toString());
        List<Response<Void>> errors = new ArrayList<>();
        e.getFieldErrors().stream()
                .forEach(error -> errors.add(Response.error(error.getField(), error.getDefaultMessage())));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

}
