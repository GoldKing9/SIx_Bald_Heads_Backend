package com.sixbald.webide.user;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.user.dto.request.UserLoginRequest;
import com.sixbald.webide.user.dto.request.UserSignupRequest;
import com.sixbald.webide.user.dto.response.UserLoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Response<Void> signup(@RequestBody @Valid UserSignupRequest dto){
        userService.signup(dto);
        return Response.success("회원가입 성공");
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        return Response.success("로그인에 성공했습니다.", userService.login(request));
    }

    @GetMapping("/test")
    public void test(@AuthenticationPrincipal LoginUser loginUser){
        System.out.println(loginUser.getUser().getEmail());
    }
}
