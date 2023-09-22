package com.sixbald.webide.file;


import com.sixbald.webide.common.PathUtils;
import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.file.dto.FileMoveRequest;
import com.sixbald.webide.file.dto.request.RenameFileRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.sixbald.webide.file.dto.request.RequestFileDTO;
import com.sixbald.webide.file.dto.response.ResponseFileDTO;

import java.io.*;

import org.springframework.beans.factory.annotation.Value;


@Slf4j
@Service
public class FileService {

    public String getFileContents(RequestFileDTO requestFileDTO, LoginUser loginUser) {

        String path = PathUtils.absolutePath(loginUser.getUser().getId(), requestFileDTO.getPath());
        String fileName = requestFileDTO.getFileName();
        log.info("fileName : {}", fileName);
        log.info("path : {}", path);

        File file = new File(path, fileName);
        StringBuilder sb = new StringBuilder();

        if (!file.exists()) {
            log.info("파일 존재 하지 않음");
            throw new GlobalException(ErrorCode.FILE_NOT_FOUND);
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String str;

            while ((str = reader.readLine()) != null) {
                sb.append(str).append("\n");
            }

        } catch (Exception e) {
            throw new GlobalException(ErrorCode.FILE_IOEXCEPTION);
        }
        return sb.toString();
    }

    public void deleteFileContents(String path, LoginUser loginUser) {
        int idx = path.lastIndexOf("/");
        String fileName = path.substring(idx + 1);
        path = PathUtils.absolutePath(loginUser.getUser().getId(), path.substring(0, idx));
        log.info("fileName : {}", fileName);
        log.info("path : {}", path);
        File file = new File(path, fileName);

        if (file.exists()) {
            if (file.delete()) {
                log.info("파일 삭제 성공");
            } else {
                log.info("파일 삭제 실패");
                throw new GlobalException(ErrorCode.FILE_DELETE_FAIL);
            }
        } else {
            log.info("삭제할 파일이 없습니다");
            throw new GlobalException(ErrorCode.FILE_NOT_FOUND);
        }
    }


    public void createFile(LoginUser loginUser, RequestFileDTO requestFileDTO) throws IOException {
        String path = requestFileDTO.getPath();
        String fileName = requestFileDTO.getFileName();
        String abPath = PathUtils.absolutePath(loginUser.getUser().getId(), path);

        File file = new File(abPath, fileName);
        log.info("path : '{}', fileName : '{}'", file.getPath(), fileName);

        try {
            // 파일이 이미 존재하는지 확인
            if (file.exists()) {
                log.info("같은 이름의 파일은 존재할 수 없습니다.");
                throw new GlobalException(ErrorCode.CANNOT_EXIST_FILE);
            }

            // 파일이 존재하지 않으면 파일 생성
            if (file.createNewFile()) {
                log.info("파일 생성 성공");
            } else {
                log.info("파일 생성 실패");
                throw new GlobalException(ErrorCode.FILE_CREATE_FAIL);
            }

        } catch (IOException e) { // 해당 경로나 폴더 없음, 디스크 공간 부족, 파일 시스템 등
            log.info("파일 입출력시 문제가 발생했습니다");
            throw new GlobalException(ErrorCode.FILE_IOEXCEPTION);

        }
    }


    public void renameFile(LoginUser loginUser, RenameFileRequestDTO renameFileRequestDTO) {
        String path = renameFileRequestDTO.getPath();
        String fileName = renameFileRequestDTO.getFileName();
        String fileRename = renameFileRequestDTO.getFileRename();
        String abPath = PathUtils.absolutePath(loginUser.getUser().getId(), path);

        File oldFile = new File(abPath, fileName);
        File newFile = new File(abPath, fileRename);
        log.info("abPath : '{}', fileRename : '{}'", newFile.getPath(), fileRename);

        try {
            // 기존 파일이 존재하는지 확인
            if (!oldFile.exists()) {
                log.info("파일이 존재하지 않습니다");
                throw new GlobalException(ErrorCode.FILE_NOT_FOUND);
            }

            // 새 파일 이름이 이미 사용 중인지 확인
            if (newFile.exists()) {
                log.info("이미 사용중인 파일 이름 입니다.");
                throw new GlobalException(ErrorCode.ALREADY_USING_FILE_NAME);
            }

            // 파일 이름 수정
            if (oldFile.renameTo(newFile)) {
                log.info("파일 이름 수정 성공");
            } else {
                log.info("파일 이름 수정 실패");
                throw new GlobalException(ErrorCode.FAIL_TO_RENAME_FILE);
            }

        } catch (NullPointerException e) {
            log.error("NullPointerException 발생. 적절한 파라미터인지 확인하세요.");
            throw new GlobalException(ErrorCode.NULL_POINTER_EXCEPTION);
        }
    }

    public Response<Void> moveFile(LoginUser loginUser, FileMoveRequest request) {
        Long userId = loginUser.getUser().getId();

        String currentPath = request.getCurrentPath();
        String movePath = request.getMovePath();

        String realCurrentPath = PathUtils.absolutePath(userId, currentPath);
        String realMovePath = PathUtils.absolutePath(userId, movePath);

        try {
            File currnetFile = FileUtils.getFile(realCurrentPath);
            File moveFile = FileUtils.getFile(realMovePath);
            FileUtils.moveFileToDirectory(currnetFile, moveFile, false);
            return Response.success("파일 이동 성공");

        } catch (FileNotFoundException e) {
            throw new GlobalException(ErrorCode.FILE_NOT_FOUND);

        } catch (IOException e) {
            return Response.error("파일 이동 실패");
        }
    }
}
