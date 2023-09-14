package com.sixbald.webide.user;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.user.dto.request.SignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Response<Void> signup(@Valid @RequestBody SignupRequest request) {
        userService.signup(request);
        return Response.success("회원가입에 성공했습니다.", null);
    }

}
