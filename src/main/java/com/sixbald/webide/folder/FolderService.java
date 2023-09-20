package com.sixbald.webide.folder;

import com.sixbald.webide.common.PathUtils;
import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.folder.dto.request.FolderRenameRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class FolderService {
    public void renameFolder(LoginUser loginUser,FolderRenameRequest request) {
        Long userId = loginUser.getUser().getId();
        String path = request.getPath();

        String realPath = PathUtils.absolutePath(userId, path);
        String folderName = request.getFolderName();
        String folderRename = request.getFolderRename();

        File oldFile = new File(realPath, folderName);
        File newFile = new File(realPath, folderRename);

        if (oldFile.exists()) {
            if (!newFile.exists()) {
                if (oldFile.renameTo(newFile)) {
                    Response.success("폴더 이름 변경 성공");
                } else {
                    log.info("폴더 이름 변경 실패");
                }
            } else {
                throw new GlobalException(ErrorCode.ALREADY_USING_FOLDER_NAME);
            }
        } else {
            log.info("변경할 폴더가 없습니다.");
        }

    }
}
