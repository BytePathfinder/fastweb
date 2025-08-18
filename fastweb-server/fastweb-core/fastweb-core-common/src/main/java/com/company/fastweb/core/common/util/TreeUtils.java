package com.company.fastweb.core.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 树形结构工具类
 *
 * @author FastWeb
 */
@Slf4j
public class TreeUtils {

    /**
     * 构建树形结构
     *
     * @param list         数据列表
     * @param rootId       根节点ID
     * @param idGetter     ID获取器
     * @param parentGetter 父ID获取器
     * @param childSetter  子节点设置器
     * @param <T>          数据类型
     * @param <ID>         ID类型
     * @return 树形结构列表
     */
    public static <T, ID> List<T> buildTree(List<T> list, ID rootId,
                                            Function<T, ID> idGetter,
                                            Function<T, ID> parentGetter,
                                            TreeChildSetter<T> childSetter) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        // 按父ID分组
        Map<ID, List<T>> parentMap = list.stream()
            .collect(Collectors.groupingBy(parentGetter));

        // 递归构建树
        return buildTreeRecursive(parentMap.get(rootId), parentMap, idGetter, childSetter);
    }

    /**
     * 递归构建树形结构
     */
    private static <T, ID> List<T> buildTreeRecursive(List<T> nodes,
                                                      Map<ID, List<T>> parentMap,
                                                      Function<T, ID> idGetter,
                                                      TreeChildSetter<T> childSetter) {
        if (nodes == null || nodes.isEmpty()) {
            return new ArrayList<>();
        }

        return nodes.stream().map(node -> {
            ID nodeId = idGetter.apply(node);
            List<T> children = buildTreeRecursive(parentMap.get(nodeId), parentMap, idGetter, childSetter);
            childSetter.setChildren(node, children);
            return node;
        }).collect(Collectors.toList());
    }

    /**
     * 树形结构转扁平列表
     *
     * @param treeList    树形结构列表
     * @param childGetter 子节点获取器
     * @param <T>         数据类型
     * @return 扁平列表
     */
    public static <T> List<T> treeToList(List<T> treeList, Function<T, List<T>> childGetter) {
        if (treeList == null || treeList.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> result = new ArrayList<>();
        for (T node : treeList) {
            result.add(node);
            List<T> children = childGetter.apply(node);
            if (children != null && !children.isEmpty()) {
                result.addAll(treeToList(children, childGetter));
            }
        }
        return result;
    }

    /**
     * 查找树节点
     *
     * @param treeList    树形结构列表
     * @param targetId    目标ID
     * @param idGetter    ID获取器
     * @param childGetter 子节点获取器
     * @param <T>         数据类型
     * @param <ID>        ID类型
     * @return 找到的节点，未找到返回null
     */
    public static <T, ID> T findNode(List<T> treeList, ID targetId,
                                     Function<T, ID> idGetter,
                                     Function<T, List<T>> childGetter) {
        if (treeList == null || treeList.isEmpty()) {
            return null;
        }

        for (T node : treeList) {
            if (Objects.equals(idGetter.apply(node), targetId)) {
                return node;
            }
            List<T> children = childGetter.apply(node);
            if (children != null && !children.isEmpty()) {
                T found = findNode(children, targetId, idGetter, childGetter);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    /**
     * 获取节点路径
     *
     * @param treeList    树形结构列表
     * @param targetId    目标ID
     * @param idGetter    ID获取器
     * @param childGetter 子节点获取器
     * @param <T>         数据类型
     * @param <ID>        ID类型
     * @return 从根到目标节点的路径
     */
    public static <T, ID> List<T> getNodePath(List<T> treeList, ID targetId,
                                              Function<T, ID> idGetter,
                                              Function<T, List<T>> childGetter) {
        if (treeList == null || treeList.isEmpty()) {
            return new ArrayList<>();
        }

        for (T node : treeList) {
            List<T> path = new ArrayList<>();
            if (findNodePath(node, targetId, idGetter, childGetter, path)) {
                return path;
            }
        }
        return new ArrayList<>();
    }

    /**
     * 递归查找节点路径
     */
    private static <T, ID> boolean findNodePath(T node, ID targetId,
                                                Function<T, ID> idGetter,
                                                Function<T, List<T>> childGetter,
                                                List<T> path) {
        path.add(node);
        
        if (Objects.equals(idGetter.apply(node), targetId)) {
            return true;
        }

        List<T> children = childGetter.apply(node);
        if (children != null && !children.isEmpty()) {
            for (T child : children) {
                if (findNodePath(child, targetId, idGetter, childGetter, path)) {
                    return true;
                }
            }
        }

        path.remove(path.size() - 1);
        return false;
    }

    /**
     * 获取所有叶子节点
     *
     * @param treeList    树形结构列表
     * @param childGetter 子节点获取器
     * @param <T>         数据类型
     * @return 叶子节点列表
     */
    public static <T> List<T> getLeafNodes(List<T> treeList, Function<T, List<T>> childGetter) {
        if (treeList == null || treeList.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> leafNodes = new ArrayList<>();
        for (T node : treeList) {
            List<T> children = childGetter.apply(node);
            if (children == null || children.isEmpty()) {
                leafNodes.add(node);
            } else {
                leafNodes.addAll(getLeafNodes(children, childGetter));
            }
        }
        return leafNodes;
    }

    /**
     * 树子节点设置器接口
     */
    @FunctionalInterface
    public interface TreeChildSetter<T> {
        void setChildren(T node, List<T> children);
    }
}