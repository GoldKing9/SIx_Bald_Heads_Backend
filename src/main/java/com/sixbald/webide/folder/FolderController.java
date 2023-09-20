package com.sixbald.webide.folder;

import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.folder.dto.request.FolderRenameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;

    @PutMapping("/rename")
    public void renameFolder(@AuthenticationPrincipal LoginUser loginUser, @RequestBody FolderRenameRequest request) {
        folderService.renameFolder(loginUser,request);
    }
}
