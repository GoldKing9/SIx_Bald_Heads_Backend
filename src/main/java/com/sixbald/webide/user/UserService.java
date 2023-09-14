package com.sixbald.webide.user;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.domain.User;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.repository.UserRepository;
import com.sixbald.webide.user.dto.request.RequestNickname;
import com.sixbald.webide.user.dto.response.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final S3Service s3Service;
    @Transactional(readOnly = true)
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

    @Transactional
    public void updateUserProfileImage(Long userId, MultipartFile imageUrl) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GlobalException(ErrorCode.NOT_FOUND_USER)
        );
        if(!user.getProfileImgUrl().isEmpty()){
            log.info("기존 이미지 경로 : {}", user.getProfileImgUrl());
            String result = s3Service.deleteFile(user.getProfileImgUrl()); //삭제 로직
            log.info("기존 프로필 이미지 삭제 :{}", result);
        }

        String imgPath = s3Service.upload(imageUrl); // 업로드
        log.info("새로 업로드된 이미지 경로 : {}", imgPath);
        user.updateImage(imgPath);
        userRepository.save(user);
    }
    @Transactional
    public Response<Void> updateNickname(Long userId, RequestNickname requestNickname) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GlobalException(ErrorCode.NOT_FOUND_USER)
        );
        String updateNickname = requestNickname.getNickname();
        if(userRepository.existsByNickname(updateNickname)){
            throw new GlobalException(ErrorCode.DUPLICATED_NICKNAME);
        }
        user.updateNickname(updateNickname);
        userRepository.save(user);

        return Response.success("프로필 닉네임 수정 성공");
    }
}
