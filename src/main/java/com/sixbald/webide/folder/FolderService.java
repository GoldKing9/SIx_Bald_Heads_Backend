package com.sixbald.webide.folder;

import com.sixbald.webide.common.PathUtils;
import com.sixbald.webide.common.Response;
import com.sixbald.webide.config.auth.LoginUser;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import com.sixbald.webide.folder.dto.PathRequest;
import com.sixbald.webide.folder.dto.request.FolderRenameRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

import com.sixbald.webide.folder.dto.request.RequestFolderDTO;

import com.sixbald.webide.folder.dto.response.Node;
import com.sixbald.webide.folder.dto.response.Type;
import org.apache.commons.io.FileUtils;


import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class FolderService {

    public void moveFolderAndFile(RequestFolderDTO requestFolderDTO, LoginUser loginUser) {
        String currentPath = PathUtils.absolutePath(loginUser.getUser().getId(), requestFolderDTO.getCurrentPath());
        log.info("현재 경로 : {}", currentPath);
        String movePath = PathUtils.absolutePath(loginUser.getUser().getId(), requestFolderDTO.getMovePath());
        log.info("현재 경로 : {}", movePath);
        File currentFolder = new File(currentPath);
        File moveFolder = new File(movePath);
        try {
            FileUtils.moveDirectoryToDirectory(currentFolder, moveFolder, false); //true일 경우 : 상위 디렉토리가 없는 경우 자동 생성

        } catch (FileNotFoundException e) {
            log.info("FileNotFoundException");
            throw new GlobalException(ErrorCode.FOLDER_NOT_FOUND);
        } catch (IOException e) {
            log.info("IOException");
            throw new GlobalException(ErrorCode.FOLDER_MOVE_FAIL);
        }
    }


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

    public Response<Void> renameFolder(LoginUser loginUser, FolderRenameRequest request) {
        Long userId = loginUser.getUser().getId();
        String path = request.getPath();

        String realPath = PathUtils.absolutePath(userId, path);
        String folderName = request.getFolderName();
        String folderRename = request.getFolderRename();

        File oldFile = new File(realPath, folderName);
        File newFile = new File(realPath, folderRename);

        try {
            if (oldFile.exists()) {
                if (!newFile.exists()) {
                    if (oldFile.renameTo(newFile)) {
                        return Response.success("폴더 이름 변경 성공");
                    } else {
                        return Response.error("폴더 이름 변경 실패");
                    }
                } else {
                    throw new GlobalException(ErrorCode.ALREADY_USING_FOLDER_NAME);
                }
            } else {
                return Response.error("변경할 폴더가 없습니다.");
            }
        } catch (Exception e) {
            return Response.error("오류 발생: " + e.getMessage());
        }
    }
}
