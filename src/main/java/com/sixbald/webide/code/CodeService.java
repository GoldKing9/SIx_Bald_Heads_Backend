package com.sixbald.webide.code;

import com.sixbald.webide.code.dto.request.SaveCodeRequest;
import com.sixbald.webide.code.dto.response.SaveCodeResponse;
import com.sixbald.webide.common.PathUtils;
import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
            log.info(fileName + "에 내용 추가 성공");

        } catch (IOException e) {
            e.printStackTrace();
            log.error(fileName + "에 내용 추가 실패: " + e.getMessage());
        }

        SaveCodeResponse saveCodeDto = SaveCodeResponse
                .builder()
                .path(realPath)
                .fileName(fileName)
                .fileContents(content)
                .build();

        return Response.success("파일 저장 완료", saveCodeDto);
    }
}
