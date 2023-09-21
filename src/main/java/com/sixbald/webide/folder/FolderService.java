package com.sixbald.webide.folder;

import com.sixbald.webide.common.PathUtils;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.folder.dto.PathRequest;
import com.sixbald.webide.folder.dto.response.Node;
import com.sixbald.webide.folder.dto.response.Type;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FolderService {

    public void create(LoginUser loginUser, PathRequest request) {
        String absolute = PathUtils.absolutePath(loginUser.getUser().getId(), request.getPath() + "/" + request.getFolderName());
        Path path = Paths.get(absolute);

        if(Files.exists(path)){
            throw new GlobalException(ErrorCode.DUPLICATED_FOLDER);
        }

        try {
            Files.createDirectory(path);
        }
        catch (FileAlreadyExistsException e){
            throw new GlobalException(ErrorCode.DUPLICATED_FOLDER, "이미 존재하는 폴더입니다.");
        }
        catch (IOException e) {
            throw new GlobalException(ErrorCode.FOLDER_OPERATION_FAILED, "폴더 생성에 실패했습니다.");
        }
    }

    public void deleteFolder(LoginUser loginUser, String path) {
        try {
            FileUtils.deleteDirectory(new File(PathUtils.absolutePath(loginUser.getUser().getId(), path)));
        }
        catch (IOException e) {
            throw new GlobalException(ErrorCode.FOLDER_OPERATION_FAILED, "폴더 삭제에 실패했습니다.");
        }
    }

    public Node getTree(LoginUser loginUser, String path) {
        List<Path> lines;

        try {
            lines = Files.walk(Paths.get(PathUtils.absolutePath(loginUser.getUser().getId(), path)), 2)
                    .toList();
        }
        catch(IOException e){
            throw new GlobalException(ErrorCode.FOLDER_NOT_FOUND, "존재하지 않는 폴더입니다." + PathUtils.absolutePath(loginUser.getUser().getId(), path));
        }

        Node node = Node.builder()
                .name(lines.get(0).getFileName().toString())
                .path(PathUtils.parsePath(lines.get(0)))
                .type(isFile(lines.get(0).getFileName().toString()) ? Type.FILE : Type.FOLDER)
                .build();

        int nodeDepth = lines.get(0).getNameCount();

        lines = lines.subList(1, lines.size());
        Map<String, Boolean> childMap = new HashMap<>();

        for(int i=lines.size()-1; i>=0; i--){
            Path line = lines.get(i);
            int depth = line.getNameCount();

            if(line.getFileName().toString().equals(".DS_Store")) continue;

            if(depth == nodeDepth+2){
                childMap.put(line.getName(line.getNameCount()-2).toString(), true);
                continue;
            }

            Node.Child child = Node.Child.builder()
                    .name(line.getFileName().toString())
                    .path(PathUtils.parsePath(line))
                    .type(isFile(line.getFileName().toString()) ? Type.FILE : Type.FOLDER)
                    .hasChild(childMap.getOrDefault(line.getFileName().toString(), false))
                    .build();

            node.getChildren().add(child);
        }

        return node;
    }

    public boolean isFile(String name){
        return name.contains(".");
    }
}
