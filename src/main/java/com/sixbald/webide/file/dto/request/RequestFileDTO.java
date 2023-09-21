package com.sixbald.webide.file.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Getter
@Setter
@NoArgsConstructor
public class RequestFileDTO {
    private String path;
    private String fileName;
}
