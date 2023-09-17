package com.sixbald.webide.config.auth;

import com.sixbald.webide.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class LoginUser implements UserDetails { // UserDetails는 사용자의 정보를 담는 인터페이스
    private final User user; // User 객체를 저장하기 위한 필드

    // 해당 유저의 권한 목록
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 역할(role)을 기반으로 권한을 생성하여 반환 (GrantedAuthority 인터페이스는 스프링 시큐리티에서 사용되며 권한 정보를 나타냄. 권한 정보를 ArrayList에 담음)
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_" + user.getRole()); // "ROLE_" 접두어를 붙여 권한 생성
        return authorities;
    }

    // 비밀번호
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 보통 PK 값인데 여기선 email
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        // 자격 증명(비밀번호)이 만료되지 않았음을 나타냄
        return true;
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        // 계정이 활성화되어 있음을 나타냄
        return true;
        // EX) 1년이 지난 회원을 휴면계정으로 바꾸고 싶을 때
        //     현재시간 - 로그인시간 = 1년 초과? => return false
    }
}
