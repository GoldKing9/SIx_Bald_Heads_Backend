package com.sixbald.webide.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Response<T> {

    private int status;
    private String message;
    private T data;

    public static Response<Void> error(int status, String message){
        return new Response<>(status, message, null);
    }

    public static Response<Void> runtimeError(String message){
        return new Response<>(500, message, null);
    }
}