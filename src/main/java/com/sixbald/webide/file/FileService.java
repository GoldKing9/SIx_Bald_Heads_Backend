package com.sixbald.webide.file;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.file.dto.FileMoveRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class FileService {
    public void moveFile(FileMoveRequest request) {
        String currentPath = request.getCurrentPath();
        String movePath = request.getMovePath();
        String fileName = request.getFileName();

        File currnetFile = new File(currentPath, fileName);
        File moveFile = new File(movePath, fileName);

        if (!moveFile.exists()) {
            try {
                FileUtils.moveFile(currnetFile, moveFile);
                Response.success("파일 이동 성공");
            }
            catch (IOException e) {
                log.info("파일 이동 실패");
                e.printStackTrace();
            }
        } else
            throw new GlobalException(ErrorCode.CANNOT_EXIST_FILE);
    }
}
