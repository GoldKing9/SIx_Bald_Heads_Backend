package com.sixbald.webide.user;

import com.sixbald.webide.domain.User;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.repository.UserRepository;
import com.sixbald.webide.user.dto.request.UserSignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Transactional
    public void signup(UserSignupDto dto) {
        if(!dto.isEmailValid()){
            throw new GlobalException(ErrorCode.UNCHECKED_EMAIL_VALID);
        }else if(!dto.isNicknameValid()){
            throw new GlobalException(ErrorCode.UNCHECKED_NICKNAME_VALID);
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        User user = dto.toEntity();
        userRepository.save(user);
    }
}
