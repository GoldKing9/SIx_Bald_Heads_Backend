package com.sixbald.webide.config.jwt;

public class JwtProperties {
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24L; // 1일
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7L; // 7일
    public static final long REFRESH_TOKEN_EXPIRE_TIME_FROM_REDIS = 60 * 60 * 24 * 7L; // 7일
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
}
