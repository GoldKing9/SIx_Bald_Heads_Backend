package com.sixbald.webide.user;

import com.sixbald.webide.config.EncoderConfig;
import com.sixbald.webide.domain.User;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.repository.UserRepository;
import com.sixbald.webide.user.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void signup(SignupRequest request) {

        //이메일, 닉네임 검사완료 여부 체크 (비밀번호는 중복검사 아니고 유효성 검사니까 안 해 주나?)
        if (!request.isEmailValid()) {
            throw new GlobalException(ErrorCode.INVALID_EMAIL);
        } else if (!request.isNicknameValid()) {
            throw new GlobalException(ErrorCode.INVALID_NICKNAME);
        }

        User user = request.toEntity(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }
}
