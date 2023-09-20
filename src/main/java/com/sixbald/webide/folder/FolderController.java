package com.sixbald.webide.folder;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.folder.dto.PathRequest;
import com.sixbald.webide.folder.dto.response.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file-tree")
public class FolderController {

    private final FolderService folderService;

    @GetMapping
    public Response<Node> getTree(@AuthenticationPrincipal LoginUser loginUser, @RequestParam(required = false, defaultValue = "/src") String path){
        return Response.success("파일 조회 성공", folderService.getTree(loginUser, path));
    }

    @PostMapping("/folder")
    public Response<Void> createFolder(@AuthenticationPrincipal LoginUser loginUser, @RequestBody PathRequest request){
        folderService.create(loginUser, request);
        return Response.success("폴더 생성 성공");
    }

    @DeleteMapping("/folder")
    public Response<Void> deleteFolder(@AuthenticationPrincipal LoginUser loginUser, String path){
        folderService.deleteFolder(loginUser, path);
        return Response.success("폴더 삭제 성공");
    }
}
