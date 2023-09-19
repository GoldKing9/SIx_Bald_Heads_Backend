package com.sixbald.webide.file.dto.request;

import lombok.Getter;

@Getter
public class RenameFileRequestDTO {
    private String path;
    private String fileName;
    private String fileRename;
}
