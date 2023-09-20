package com.sixbald.webide.folder.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Node {

    private String name;
    private String path;
    private Type type;
    private List<Node> children = new ArrayList<>();

    @Builder
    public Node(String name, String path, Type type, List<Node> children) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.children = children;
    }
}
