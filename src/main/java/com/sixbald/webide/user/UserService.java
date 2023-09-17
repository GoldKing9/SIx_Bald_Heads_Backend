package com.sixbald.webide.user;

import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.config.utils.JwtUtils;
import com.sixbald.webide.domain.User;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.repository.UserRepository;
import com.sixbald.webide.user.dto.request.UserLoginRequest;
import com.sixbald.webide.user.dto.request.UserSignupRequest;
import com.sixbald.webide.user.dto.response.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Transactional
    public void signup(UserSignupRequest dto) {
        if(!dto.isEmailValid()){
            throw new GlobalException(ErrorCode.UNCHECKED_EMAIL_VALID);
        }else if(!dto.isNicknameValid()){
            throw new GlobalException(ErrorCode.UNCHECKED_NICKNAME_VALID);
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        User user = dto.toEntity();
        userRepository.save(user);
    }

    @Transactional
    public UserLoginResponse login(UserLoginRequest request){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String token = jwtUtils.generateJwt(loginUser);

        return UserLoginResponse.builder()
                .userId(loginUser.getUser().getId())
                .nickname(loginUser.getUser().getNickname())
                .accessToken(token)
                .build();
    }
}
