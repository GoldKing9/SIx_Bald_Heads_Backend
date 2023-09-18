package com.sixbald.webide.file;

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

    public ResponseFileDTO getFileContents(RequestFileDTO requestFileDTO) {
        String fileName = requestFileDTO.getFileName();
        String path = requestFileDTO.getPath();
        log.info("fileName : {}", fileName);
        log.info("path : {}", path);

        File file = new File(path, fileName);
        StringBuilder sb = new StringBuilder();
        try{
            //파일 존재 확인
            if(!file.exists()){
               if(file.createNewFile()){
                   log.info("파일 생성");
               }else{
                   log.info("파일 생성 실패");
               }
            }else{
                   log.info("파일 이미 존재함");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String str;

            while((str = reader.readLine()) != null){
                sb.append(str).append("\n");
            }
        }catch (Exception e){
            throw new GlobalException(ErrorCode.FILE_NOT_FOUND);
        }
        return ResponseFileDTO.builder()
                .contents(sb.toString())
                .build();
    }
}
