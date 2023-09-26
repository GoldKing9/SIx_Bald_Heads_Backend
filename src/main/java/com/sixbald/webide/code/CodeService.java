package com.sixbald.webide.code;

import com.sixbald.webide.code.dto.request.SaveCodeRequest;
import com.sixbald.webide.code.dto.response.SaveCodeResponse;
import com.sixbald.webide.common.PathUtils;
import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

@Service
@Slf4j
public class CodeService {
    public Response<SaveCodeResponse> saveCode(LoginUser loginuser, SaveCodeRequest request) {
        Long userId = loginuser.getUser().getId();
        String path = request.getPath();

        String realPath = PathUtils.absolutePath(userId, path);
        String fileName = request.getFileName();
        String content = request.getContent();

        File saveFile = new File(realPath, fileName);
        try {
            FileWriter writer = new FileWriter(saveFile, false);
            writer.write(content);
            writer.close();

            SaveCodeResponse saveCodeDto = SaveCodeResponse
                    .builder()
                    .path(PathUtils.parsePath(Path.of(realPath)))
                    .fileName(fileName)
                    .fileContents(content)
                    .build();

            return Response.success("파일 저장 완료", saveCodeDto);

        } catch (IOException e) {
            e.printStackTrace();
            throw new GlobalException(ErrorCode.FALI_TO_SAVE_FILE);
        }

    }
}
