package com.sixbald.webide.file;

import com.sixbald.webide.common.PathUtils;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.file.dto.request.RequestFileDTO;
import com.sixbald.webide.file.dto.response.ResponseFileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
@Slf4j
@Service
public class FileService {

    public String getFileContents(RequestFileDTO requestFileDTO, LoginUser loginUser) {
//        String path = "/Users/kyeongseon/Documents/Spring"+PathUtils.absolutePath(loginUser.getUser().getId(), requestFileDTO.getPath()); //test중
        String path = PathUtils.absolutePath(loginUser.getUser().getId(), requestFileDTO.getPath());
        String fileName = requestFileDTO.getFileName();
        log.info("fileName : {}", fileName);
        log.info("path : {}", path);

        File file = new File(path, fileName);
        StringBuilder sb = new StringBuilder();
            //파일 존재 확인
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
}
