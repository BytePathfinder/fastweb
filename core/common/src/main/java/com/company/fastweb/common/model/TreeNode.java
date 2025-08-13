package com.company.fastweb.common.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 树节点模型
 */
@Data
public class TreeNode<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private T id;
    private T parentId;
    private String name;
    private List<TreeNode<T>> children;

    public TreeNode() {
    }

    public TreeNode(T id, T parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }
}