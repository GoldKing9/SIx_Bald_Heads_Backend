package com.sixbald.webide.file;

import com.sixbald.webide.file.dto.FileMoveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    @PostMapping("/path")
    public void moveFile(@RequestBody FileMoveRequest request) {
        fileService.moveFile(request);
    }

}
