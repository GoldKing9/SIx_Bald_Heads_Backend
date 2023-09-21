package com.sixbald.webide.common;

import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PathUtils {

    private PathUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String absolutePath(Long userId, String path){
        return "/root/" + userId + path;
    }

    public static String parsePath(Path path){
        String stringPath = path.toString();
        int start = stringPath.indexOf("/src");
        return stringPath.substring(start);
    }

    public static void createUserDirectory(Long userId) {
        File file = new File("/root/"+userId+"/src");

        try {
            FileUtils.forceMkdir(file);
        } catch (IOException e) {
            throw new GlobalException(ErrorCode.FOLDER_OPERATION_FAILED, "유저 폴더 생성에 실패했습니다.");
        }

    }


}