package com.sixbald.webide.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixbald.webide.common.Response;
import com.sixbald.webide.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.sixbald.webide.exception.ErrorCode.*;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (authException.getClass().equals(UsernameNotFoundException.class)) {
            setResponse(response, EMAIL_NOT_FOUND);
        } else if (authException.getClass().equals(BadCredentialsException.class)) {
            setResponse(response, PASSWORD_NOT_FOUND);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        ResponseDto<ErrorResponse> fail = ResponseDto.fail(errorCode.getStatus(), errorCode.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(Response.error(errorCode.getStatus().toString(), errorCode.getMessage())));

    }
}
