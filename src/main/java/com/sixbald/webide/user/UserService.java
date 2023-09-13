package com.sixbald.webide.user;

import com.sixbald.webide.domain.User;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.repository.UserRepository;
import com.sixbald.webide.user.dto.response.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDTO getUserInfo(Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GlobalException(ErrorCode.NOT_FOUND_USER)
        );

        return UserDTO.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .userId(user.getId())
                .imageUrl(user.getProfileImgUrl())
                .build();

    }
}
