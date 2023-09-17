package com.sixbald.webide.repository;

import com.sixbald.webide.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {



    boolean existsByNickname(String updateNickname);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

}
