package com.sixbald.webide.file;


import com.sixbald.webide.common.PathUtils;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.file.dto.request.RenameFileRequestDTO;
import com.sixbald.webide.file.dto.request.RequestFileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class FileService {


    // 파일 생성
    public void createFile(LoginUser loginUser, RequestFileDTO requestFileDTO) throws IOException {
        String path = requestFileDTO.getPath();
        String fileName = requestFileDTO.getFileName();
        String abPath = PathUtils.absolutePath(loginUser.getUser().getId(), path);

        File file = new File(abPath, fileName);
        log.info("path : '{}', fileName : '{}'", path, fileName);

        if (!file.exists()) { // 파일이 존재하지 않으면 생성
            try {
                if (file.createNewFile())
                    log.info("파일 생성 성공");
                else
                    log.info("파일 생성 실패");
            } catch (IOException e) { // 디스크 공간이 부족하거나 파일 시스템에 문제가 있을 때 발생
                throw new IOException("파일 생성이 불가능합니다.", e);
            }
        } else { // 파일이 이미 존재하면
            log.info("파일이 이미 존재합니다.");
        }
    }

    // 파일 이름 수정
    public void renameFile(LoginUser loginUser, RenameFileRequestDTO renameFileRequestDTO) {
        String path = renameFileRequestDTO.getPath();
        String fileName = renameFileRequestDTO.getFileName();
        String fileRename = renameFileRequestDTO.getFileRename();
        String abPath = PathUtils.absolutePath(loginUser.getUser().getId(), path);

        File originFile = new File(abPath, fileName);
        File renamedFile = new File(abPath, fileRename);
        log.info("abPath : '{}', fileRename : '{}'", renamedFile.getPath(), fileRename);

        if (originFile.exists()) {
            if (originFile.renameTo(renamedFile))
                log.info("파일 이름 수정 성공");
            else
                log.info("파일 이름 수정 실패");
        } else {
            log.info("이름을 수정할 파일이 없습니다.");
        }
    }


}
