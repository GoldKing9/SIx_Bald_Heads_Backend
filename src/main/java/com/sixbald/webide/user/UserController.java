package com.sixbald.webide.user;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.user.dto.request.RequestNickname;
import com.sixbald.webide.user.dto.response.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.sixbald.webide.user.dto.request.EmailCheckRequest;
import com.sixbald.webide.user.dto.request.NicknameRequest;
import com.sixbald.webide.user.dto.request.PasswordRequest;
import com.sixbald.webide.user.dto.request.SendEmailRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@Tag(name="유저 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    // 프로필 조회
    @GetMapping("/profile")
    @Operation(summary = "프로필 조회")
    public Response<UserDTO> findUserInfo(){
        Long userId = 1L; // 임시
        UserDTO data = userService.getUserInfo(userId);
        return Response.success("회원 조회에 성공하셨습니다", data);
    }

    //프로필 이미지 수정
    @PutMapping("/profile/image")
    @Operation(summary = "프로필 이미지 수정")
    public Response<Void> updateImage(@NotNull @RequestParam MultipartFile imageUrl) {
        Long userId = 1L; // 임시
        userService.updateUserProfileImage(userId, imageUrl);
        return Response.success("프로필 이미지 수정 성공");
    }

    // 프로필 닉네임 수정
    @PutMapping("/profile/nickname")
    @Operation(summary = "프로필 닉네임 수정")
    public Response<Void> updateNickname(@RequestBody RequestNickname requestNickname){
        //validation 으로 true, false
        Long userId = 1L; // 임시

        return userService.updateNickname(userId, requestNickname);
    }
  
    @PostMapping("/nickcheck")
    public Response<Void> nicknameCheck(@RequestBody NicknameRequest request) {
        return userService.nicknameCheck(request);
    }

    //TODO @Authentication 처리해줘야 한다.
    @PostMapping("/password")
    public Response<Void> passwordEdit(@RequestBody PasswordRequest request) {
        return userService.passwordEdit(request);
    }

    @PostMapping("/sendmail")
    public Response<Void> sendMail(@RequestBody SendEmailRequest request) {
        return userService.sendMail(request);
    }

    @PostMapping("/emailcheck")
    public Response<Void> emailCheck(@RequestBody EmailCheckRequest request) {
        return userService.emailCheck(request);
    }
}
