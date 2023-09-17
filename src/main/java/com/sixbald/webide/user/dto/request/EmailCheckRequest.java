package com.sixbald.webide.user.dto.request;

import lombok.Getter;

@Getter
public class EmailCheckRequest {
    private String code;
    private String email;
}
