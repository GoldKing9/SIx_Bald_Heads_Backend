package com.sixbald.webide.user;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.user.dto.request.NicknameRequest;
import com.sixbald.webide.user.dto.request.PasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.Authenticator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/nickcheck")
    public Response<Void> nicknameCheck(@RequestBody NicknameRequest request) {
        return userService.nicknameCheck(request);
    }

    //TODO @Authentication 처리해줘야 한다.
    @PostMapping("/api/auth/passwordk")
    public Response<Void> passwordEdit(@RequestBody PasswordRequest request) {
        return userService.passwordEdit(request);
    }


}
