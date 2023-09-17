package com.sixbald.webide.user;

import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.config.jwt.JwtUtils;
import com.sixbald.webide.domain.User;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.repository.RefreshTokenRepository;
import com.sixbald.webide.repository.UserRepository;
import com.sixbald.webide.user.dto.request.LoginRequest;
import com.sixbald.webide.user.dto.request.SignupRequest;
import com.sixbald.webide.user.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;

    public void signup(SignupRequest request) {

        // 이메일, 닉네임 검사완료 여부 체크 (비밀번호는 중복검사 아니고 유효성 검사니까 안 해 주나?)
        if (!request.isEmailValid()) {
            throw new GlobalException(ErrorCode.INVALID_EMAIL);
        } else if (!request.isNicknameValid()) {
            throw new GlobalException(ErrorCode.INVALID_NICKNAME);
        }

        User user = request.toEntity(bCryptPasswordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        // 사용자가 제공한 이메일과 패스워드를 사용하여 인증 토큰을 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        // AuthenticationManager를 사용하여 인증을 시도하고 결과를 얻음
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 현재의 보안 컨텍스트에 인증 정보를 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증된 사용자 정보를 가져옴
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // JWT 토큰 유틸리티를 사용하여 AccessToken을 생성
        String accessToken = jwtUtils.createAccessTokenFromLoginUser(loginUser);

        // LoginResponse 객체를 생성하여 반환
        return new LoginResponse(loginUser, accessToken, "refreshToken");
    }
}
