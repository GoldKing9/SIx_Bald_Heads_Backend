package com.sixbald.webide.common;

import lombok.Getter;

public class PathUtils {
    public static String absolutePath(Long userId, String path){
        return "/root/" + userId + path;
    }
}