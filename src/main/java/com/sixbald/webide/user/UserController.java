package com.sixbald.webide.user;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.user.dto.request.UserSignupDto;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Response<Void> signup(@RequestBody @Valid UserSignupDto dto){
        userService.signup(dto);
        return Response.success("회원가입 성공");
    }
}
