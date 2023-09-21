package com.sixbald.webide.folder.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestFolderDTO {
    private String currentPath;
    private String movePath;
    private String folderName;

}
