package com.sixbald.webide.common;

import java.io.File;

public class PathUtils {

    public static String absolutePath(Long userId, String path){
        return "/root/" + userId +"/src"+ path;
    }
}