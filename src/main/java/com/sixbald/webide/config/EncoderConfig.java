package com.sixbald.webide.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncoderConfig {
    //BCryptPasswordEncoder와 SecurityConfig는 꼭 다른 클래스에 선언해야 함
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
