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

    @Builder
    public User(Long id, String password, String email, String nickname, String profileImgUrl) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.profileImgUrl = "https://play-lh.googleusercontent.com/38AGKCqmbjZ9OuWx4YjssAz3Y0DTWbiM5HB0ove1pNBq_o9mtWfGszjZNxZdwt_vgHo=w480-h960-rw";
    }
}
