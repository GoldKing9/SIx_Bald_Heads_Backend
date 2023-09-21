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

            if(!file.exists()) {
                log.info("파일 존재 하지 않음");
                throw new GlobalException(ErrorCode.FILE_NOT_FOUND);
            }
        try{
             BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
             String str;

             while((str = reader.readLine()) != null){
                sb.append(str).append("\n");
             }

        }catch (Exception e){
            throw new GlobalException(ErrorCode.FILE_IOEXCEPTION);
        }
        return sb.toString();
    }

    public void deleteFileContents(RequestFileDTO requestFileDTO, LoginUser loginUser) {
        String fileName = requestFileDTO.getFileName();
        String path =  PathUtils.absolutePath(loginUser.getUser().getId(), requestFileDTO.getPath());
        File file = new File(path, fileName);

        if(file.exists()){
            if(file.delete()){
                log.info("파일 삭제 성공");
            }else{
                log.info("파일 삭제 실패");
                throw new GlobalException(ErrorCode.FILE_DELETE_FAIL);
            }
        }else{
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
