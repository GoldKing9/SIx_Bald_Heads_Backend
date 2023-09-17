package com.sixbald.webide.config.jwt;

import com.sixbald.webide.config.auth.LoginUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // HTTP 요청에서 JWT 토큰을 추출
        String token = jwtUtils.parseJwtToken(request);

        // 추출한 토큰이 유효하다면 (검증이 성공하면)
        if (token != null && jwtUtils.validateJwtToken(token)) {
            // 주어진 토큰을 파싱하고 그 안에 포함된 사용자 정보를 추출
            LoginUser loginuser = jwtUtils.verify(token);
            // 사용자 정보와 권한 목록을 받아서 사용자를 인증, 사용자를 Spring Security의 Authentication 객체로 만듦
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginuser, null, loginuser.getAuthorities());
            // 현재 사용자의 Authentication 객체를 설정(현재 사용자로 설정한 것). 이후의 요청 처리에서 사용자의 권한을 확인하는 데 사용
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }
}
