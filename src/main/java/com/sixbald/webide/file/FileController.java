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
@RequestMapping("/api/v1/file-tree")
public class FileController {
    private final FileService fileService;


    // 해당 파일 가져오기
    @GetMapping("/file")
    public Response<String> readContents(
            @ModelAttribute RequestFileDTO requestFileDTO,
            @AuthenticationPrincipal LoginUser loginUser
    ){
        return Response.success("해당 파일 조회를 성공했습니다", fileService.getFileContents(requestFileDTO, loginUser));
    }
    // 파일 삭제
    @DeleteMapping("/file")
    public Response<Void> deleteContents(
            @ModelAttribute RequestFileDTO requestFileDTO,
            @AuthenticationPrincipal LoginUser loginUser
    ){
        fileService.deleteFileContents(requestFileDTO, loginUser);
        return Response.success("해당 파일 삭제를 성공했습니다");
    }
}
