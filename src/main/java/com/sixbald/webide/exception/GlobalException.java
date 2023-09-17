package com.sixbald.webide.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GlobalException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    public GlobalException(ErrorCode code){
        this.errorCode = code;
        this.message = null;
    }

    public String getMessage() {
        if(this.message == null){
            return this.errorCode.getMessage();
        }

        return message;
    }
}
