package com.sixbald.webide.user;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.user.dto.request.EmailCheckRequest;
import com.sixbald.webide.user.dto.request.NicknameRequest;
import com.sixbald.webide.user.dto.request.PasswordRequest;
import com.sixbald.webide.user.dto.request.SendEmailRequest;
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
