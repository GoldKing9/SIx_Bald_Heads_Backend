package com.sixbald.webide.exception;

import com.sixbald.webide.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = GlobalException.class)
    public Response<Void> handleGlobalExceptionHandler(GlobalException e) {
        log.error("error occur: {}" , e.getStackTrace());
        log.error("error occur: {}" , e.toString());
        return Response.error(e.getErrorCode().getStatus().value(), e.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public Response<Void> runtimeExceptionHandler(RuntimeException e) {
        log.error("error occur: {}" , e.getStackTrace());
        log.error("error occur : {}", e.toString());
        return Response.runtimeError(e.getMessage());
    }
}
