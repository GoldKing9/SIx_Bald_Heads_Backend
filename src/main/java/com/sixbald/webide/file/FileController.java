package com.sixbald.webide.file;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;

import com.sixbald.webide.file.dto.FileMoveRequest;
import com.sixbald.webide.file.dto.request.RenameFileRequestDTO;
import com.sixbald.webide.file.dto.request.RequestFileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file-tree")
public class FileController {
    private final FileService fileService;

    @PutMapping("/file/move")
    public Response<Void> moveFile(@AuthenticationPrincipal LoginUser loginUser, @RequestBody FileMoveRequest request) {
        return fileService.moveFile(loginUser,request);
    }


    @GetMapping("/file")
    public Response<String> readContents(
            @ModelAttribute RequestFileDTO requestFileDTO,
            @AuthenticationPrincipal LoginUser loginUser
    ){
        return Response.success("해당 파일 조회를 성공했습니다", fileService.getFileContents(requestFileDTO, loginUser));
    }

    @DeleteMapping("/file")
    public Response<Void> deleteContents(
            @ModelAttribute RequestFileDTO requestFileDTO,
            @AuthenticationPrincipal LoginUser loginUser
    ){
        fileService.deleteFileContents(requestFileDTO, loginUser);
        return Response.success("해당 파일 삭제를 성공했습니다");
    }

    @PostMapping("/file")
    public Response<Void> createFile(@RequestBody RequestFileDTO requestFileDTO, @AuthenticationPrincipal LoginUser loginUser) throws IOException {
        fileService.createFile(loginUser, requestFileDTO);
        return Response.success("파일 생성 성공");
    }


    @PutMapping("/file/rename")
    public Response<Void> renameFile(@RequestBody RenameFileRequestDTO renameFileRequestDTO, @AuthenticationPrincipal LoginUser loginUser) {
        fileService.renameFile(loginUser, renameFileRequestDTO);
        return Response.success("파일 이름 수정 성공");

    }

}
