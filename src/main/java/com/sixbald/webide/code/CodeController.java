package com.sixbald.webide.code;

import com.sixbald.webide.code.dto.request.SaveCodeRequest;
import com.sixbald.webide.code.dto.response.SaveCodeResponse;
import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/editor")
@RequiredArgsConstructor
public class CodeController {
    private final CodeService codeService;

    @PostMapping
    public Response<SaveCodeResponse> saveCode(@AuthenticationPrincipal LoginUser loginUser, @RequestBody SaveCodeRequest request) {
        return codeService.saveCode(loginUser,request);
    }
}
