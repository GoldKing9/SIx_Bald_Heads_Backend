package com.sixbald.webide.code.dto.response;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveCodeResponse {
    private String path;
    private String fileName;
    private String fileContents;

}
