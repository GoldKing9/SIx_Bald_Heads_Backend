package com.sixbald.webide.code.dto.request;

import lombok.Getter;

@Getter
public class SaveCodeRequest {
    private String path;
    private String fileName;
    private String content;
}
