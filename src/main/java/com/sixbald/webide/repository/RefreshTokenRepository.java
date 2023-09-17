package com.sixbald.webide.repository;

import com.sixbald.webide.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,String> {
    Optional<RefreshToken> findByAccessToken(String refreshToken); // 만료된 리프레시 토큰으로 loginUser 찾는 건가?
}
