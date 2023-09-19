package com.sixbald.webide.folder;

import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.folder.dto.PathRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FolderService {

    public void create(PathRequest request) {
        String path = request.getPath() + "/" + request.getFolderName();

        try {
            Files.createDirectory(Paths.get(path));
        } catch (IOException e) {
            throw new GlobalException(ErrorCode.CREATION_FAILED, "폴더 생성에 실패했습니다.");
        }
    }
}
