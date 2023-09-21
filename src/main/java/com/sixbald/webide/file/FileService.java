package com.sixbald.webide.file;

import com.sixbald.webide.common.PathUtils;
import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.file.dto.FileMoveRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class FileService {

    public Response<Void> moveFile(LoginUser loginUser, FileMoveRequest request) {
        Long userId = loginUser.getUser().getId();

        String currentPath = request.getCurrentPath();
        String movePath = request.getMovePath();
        String fileName = request.getFileName();

        String realCurrentPath = PathUtils.absolutePath(userId, currentPath);
        String realMovePath = PathUtils.absolutePath(userId, movePath);

        File currnetFile = new File(realCurrentPath, fileName);
        File moveFile = new File(realMovePath, fileName);

        try {
            if (!moveFile.exists()) {
                FileUtils.moveFile(currnetFile, moveFile);
                return Response.success("파일 이동 성공");
            } else {
                throw new GlobalException(ErrorCode.CANNOT_EXIST_FILE);
            }
        } catch (IOException e) {
            return Response.error("파일 이동 실패");
        }
    }
}
