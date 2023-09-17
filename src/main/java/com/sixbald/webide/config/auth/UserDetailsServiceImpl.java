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
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // 시큐리티로 로그인이 될 때, 시큐리티가 loadUserByUsername() 실행해서 username을 체크! (이 경우엔 email)
    // 없으면 오류
    // 있으면 정상적으로 시큐리티 컨텍스트 내부 세션에 로그인 된 세션이 만들어짐
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 고민: Optioanl 처리 해 줘야 하나?
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorCode.EMAIL_NOT_FOUND));
        return new LoginUser(user);
    }
}
