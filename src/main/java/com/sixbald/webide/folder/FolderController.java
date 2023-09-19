package com.sixbald.webide.folder;

import com.sixbald.webide.common.Response;
import com.sixbald.webide.folder.dto.PathRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/folder")
    public Response<Void> createFolder(@RequestBody PathRequest request){
        folderService.create(request);
        return Response.success("폴더 생성 성공");
    }
}
