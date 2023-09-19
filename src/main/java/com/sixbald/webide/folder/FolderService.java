package com.sixbald.webide.folder;

import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.folder.dto.PathRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Slf4j
@Service
public class FolderService {

    public void create(PathRequest request) {
        Path path = Paths.get(request.getPath() + "/" + request.getFolderName());
        if(Files.exists(path)){
            throw new GlobalException(ErrorCode.DUPLICATED_FOLDER);
        }

        try {
            Files.createDirectory(path);
        }
        catch (FileAlreadyExistsException e){
            throw new GlobalException(ErrorCode.DUPLICATED_FOLDER, "이미 존재하는 폴더입니다.");
        }
        catch (IOException e) {
            throw new GlobalException(ErrorCode.FOLDER_OPERATION_FAILED, "폴더 생성에 실패했습니다.");
        }
    }

    public void deleteFolder(String path) {
        try {
            FileUtils.deleteDirectory(new File(path));
        }
        catch (IOException e) {
            throw new GlobalException(ErrorCode.FOLDER_OPERATION_FAILED, "폴더 삭제에 실패했습니다.");
        }
    }
}
