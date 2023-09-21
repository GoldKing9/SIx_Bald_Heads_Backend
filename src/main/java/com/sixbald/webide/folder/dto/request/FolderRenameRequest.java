package com.sixbald.webide.folder.dto.request;

import lombok.Getter;

@Getter
public class FolderRenameRequest {
    private String path;
    private String folderName;
    private String folderRename;
}



