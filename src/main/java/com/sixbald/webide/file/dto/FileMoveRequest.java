package com.sixbald.webide.file.dto;

import lombok.Getter;

@Getter
public class FileMoveRequest {
    private String currentPath;
    private String movePath;
}

