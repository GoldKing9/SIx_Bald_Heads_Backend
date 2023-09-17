package com.sixbald.webide.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    @Column(unique = true, length = 100)
    private String email;
    @Column(unique = true, length = 100)
    private String nickname;
    private String profileImgUrl;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public User(Long id, String password, String email, String nickname, UserRole role, String profileImgUrl) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.profileImgUrl = profileImgUrl;
    }
}
