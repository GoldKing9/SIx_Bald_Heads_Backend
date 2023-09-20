package com.sixbald.webide.common;

public class PathUtils {

    public static String absolutePath(Long userId, String path){
        return "/root/" + userId + path;
    }
    private PathUtils() {
        throw new IllegalStateException("Utility class");
    }
}