package com.sixbald.webide.config.auth;

import com.sixbald.webide.domain.User;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public LoginUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new GlobalException(ErrorCode.USER_NOT_FOUND, "아이디가 일치하지 않습니다")
        );

        return new LoginUser(user);
    }
}
