package com.sixbald.webide.user.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class UploadFileDTO {
    @Parameter(
            description = "multipart/form-data 형식의 이미지를 받습니다. 이때 key 값은 multipartFile 입니다.",
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
    )
    MultipartFile imageUrl;
}
