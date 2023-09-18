package com.sixbald.webide.config;

import com.sixbald.webide.config.auth.LoginUserService;
import com.sixbald.webide.config.filter.JwtTokenFilter;
import com.sixbald.webide.config.utils.JwtUtils;
import com.sixbald.webide.exception.CustomEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginUserService loginUserService;
    private final CustomEntryPoint customEntryPoint;
    private final JwtUtils jwtUtils;
    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내 서버가 응답할 때, json을 자바스크립트에서 처리할 수 있게 할지를 설정
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*"); // 모든 header에 응답 허용
        config.addAllowedMethod("*"); //모든 method 요청을 허용
        config.addExposedHeader("*");
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(corsFilter())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/signup", "/api/auth/login", "/api/auth/emailcheck", "/api/auth/sendmail",
                                "/api/auth/reissue", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .exceptionHandling().authenticationEntryPoint(customEntryPoint)
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(loginUserService);
        authenticationProvider.setPasswordEncoder(encodePassword());
        return authenticationProvider;
    }
}
