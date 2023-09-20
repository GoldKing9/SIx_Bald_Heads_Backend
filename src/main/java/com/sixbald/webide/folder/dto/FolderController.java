package com.sixbald.webide.folder.dto;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.folder.FolderService;
import com.sixbald.webide.folder.dto.request.RequestFolderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FolderController {
    private final FolderService folderService;

    //폴더 이동
    @PutMapping("/folders/path")
    public Response<Void> moveFolder(
            @RequestBody RequestFolderDTO requestFolderDTO,
            @AuthenticationPrincipal LoginUser loginUser
            ){
        folderService.moveFolderAndFile(requestFolderDTO, loginUser);
        return Response.success("폴더 이동에 성공했습니다");
    }
}
