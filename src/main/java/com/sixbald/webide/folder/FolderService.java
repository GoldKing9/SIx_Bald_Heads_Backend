package com.sixbald.webide.folder;

import com.sixbald.webide.common.PathUtils;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.folder.dto.request.RequestFolderDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class FolderService {
    public void moveFolderAndFile(RequestFolderDTO requestFolderDTO, LoginUser loginUser) {
        String currentPath = "/Users/kyeongseon/Documents/Spring"+PathUtils.absolutePath(loginUser.getUser().getId(), requestFolderDTO.getCurrentPath()); // 임시로 경로를 박아두었습니다
        log.info("현재 경로 : {}", currentPath);
        String movePath = "/Users/kyeongseon/Documents/Spring"+PathUtils.absolutePath(loginUser.getUser().getId(), requestFolderDTO.getMovePath());
        log.info("현재 경로 : {}", movePath);
        File currentFolder = new File(currentPath);
        File moveFolder = new File(movePath);
        try{
            FileUtils.moveDirectoryToDirectory(currentFolder, moveFolder, true); //true일 경우 : 상위 디렉토리가 없는 경우 자동 생성
        }catch (IOException e){
            throw new GlobalException(ErrorCode.DUPLICATED_FOLDER_NAME);
        }

    }
}
