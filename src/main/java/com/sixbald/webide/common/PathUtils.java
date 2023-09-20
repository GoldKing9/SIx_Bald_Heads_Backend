package com.sixbald.webide.common;

public class PathUtils {

    private PathUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String absolutePath(Long userId, String path){
        return "/root/" + userId + path;
    }
}