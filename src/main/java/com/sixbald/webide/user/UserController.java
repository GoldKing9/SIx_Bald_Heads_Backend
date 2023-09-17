package com.sixbald.webide.user;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.user.dto.request.UserLoginRequest;
import com.sixbald.webide.user.dto.request.UserSignupRequest;
import com.sixbald.webide.user.dto.response.UserLoginResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 서비스")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @ApiResponses(value={
            @ApiResponse(description = "회원가입 성공", responseCode = "200"),
            @ApiResponse(description = "회원가입 실패", responseCode = "400")
    })
    @PostMapping("/signup")
    public Response<Void> signup(@RequestBody @Valid UserSignupRequest dto){
        userService.signup(dto);
        return Response.success("회원가입 성공");
    }

    @ApiResponses(value={
            @ApiResponse(description = "로그인 성공", responseCode = "200"),
            @ApiResponse(description = "로그인 실패", responseCode = "400")
    })
    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        return Response.success("로그인에 성공했습니다.", userService.login(request));
    }

    @ApiResponses(value={
            @ApiResponse(description = "로그아웃 성공", responseCode = "200")
    })
    @PostMapping("/logout")
    public Response<Void> logout(){
        return Response.success("로그아웃 되었습니다.");
    }
}
