package com.sixbald.webide.file;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.file.dto.request.RequestFileDTO;
import com.sixbald.webide.file.dto.response.ResponseFileDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileController {
    private final FileService fileService;


    // 해당 파일 가져오기
    @GetMapping("/files")
    public Response<ResponseFileDTO> readContents(
            @RequestBody RequestFileDTO requestFileDTO,
            @AuthenticationPrincipal LoginUser loginUser
    ){
        return Response.success("해당 파일 조회를 성공했습니다", fileService.getFileContents(requestFileDTO));
    }
}
