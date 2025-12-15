package com.jiuxi.common.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName: TreeUtil
 * @Description:
 * @author: jiuxx
 * @Date: 2020/11/19  8:54
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class CommonTreeUtil {

    private CommonTreeUtil() {
    }

    /**
     * 将扁平数据转换为树结构
     *
     * @param list
     * @return java.util.List<T>
     * @author jiuxx
     * @date 2024/5/27 17:34
     */
    public static <T extends Tree> List<T> buildTree(List<T> list) {
        return buildTree(list, true);
    }

    /**
     * 将扁平数据转换为树结构
     *
     * @param list         将扁平数据
     * @param setTreeLeaf  是否设置leaf
     * @return java.util.List<T>
     * @author jiuxx
     * @date 2024/6/4 19:55
     */
    public static <T extends Tree> List<T> buildTree(List<T> list, boolean setTreeLeaf) {

        Map<Object, T> tempmap = new HashMap<>();
        // 保存到map中，以备下面组装数据使用
        for (T model : list) {
            if (setTreeLeaf) {
                // 默认为叶子节点
                model.setTreeLeaf(true);
            }
            tempmap.put(model.getTreeId(), model);
        }

        // 组装数据
        List<T> result = new ArrayList<>();
        for (T model : list) {
            T tree = tempmap.get(model.getTreePid());
            if (tree != null && !model.getTreeId().equals(model.getTreePid())) {
                if (tree.getTreeChildren() == null) {
                    tree.setTreeChildren(new ArrayList<>());
                    if (setTreeLeaf) {
                        // 修改为非叶子节点
                        tree.setTreeLeaf(false);
                    }
                }
                tree.getTreeChildren().add(model);
                tree.getTreeChildren().sort(comparator());
            } else {
                result.add(model);
            }
        }

        return result;
    }

    /**
     * 将扁平数据转换为树结构
     *
     * @param list
     * @return java.util.List<T>
     * @author jiuxx
     * @date 2024/5/27 17:34
     */
    @Deprecated
    public static <T extends Tree> List<T> makeTree(List<T> list) {
        return buildTree(list);
    }

    /**
     * 排序
     * <pre>
     *     1. getOrderNo 不为空，则根据getOrderNo的返回值进行排序
     *     2. getOrderNo 为空，则根据getTreeId的返回值进行排序
     * </pre>
     * @author jiuxx
     * @date 2023/11/1 16:41
     */
    private static Comparator<Tree> comparator() {
        return (tree1, tree2) -> {
            if (tree1.getOrderNo() == null || tree2.getOrderNo() == null) {
                if (tree1.getTreeId() instanceof String) {
                    return ((String) tree1.getTreeId()).compareTo((String) tree2.getTreeId());
                }
                return 0;
            }
            return tree1.getOrderNo().compareTo(tree2.getOrderNo());
        };
    }

    /**
     * 树 接口，实现此接口的数据都可以组装为树结构
     * @author jiuxx
     */
    public interface Tree<T>{
        @JsonIgnore
        Object getTreeId();
        @JsonIgnore
        Object getTreePid();
        @JsonIgnore
        List<T> getTreeChildren();
        @JsonIgnore
        void setTreeChildren(List<T> list);
        @JsonIgnore
        void setTreeLeaf(boolean bool);
        @JsonIgnore
        default BigDecimal getOrderNo() {
            return null;
        }
    }
}
