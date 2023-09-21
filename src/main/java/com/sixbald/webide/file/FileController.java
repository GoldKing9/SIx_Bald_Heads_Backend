package com.sixbald.webide.file;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.file.dto.FileMoveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file-tree/file")
public class FileController {
    private final FileService fileService;

    @PutMapping("/move")
    public Response<Void> moveFile(@AuthenticationPrincipal LoginUser loginUser, @RequestBody FileMoveRequest request) {
        return fileService.moveFile(loginUser,request);
    }

}
