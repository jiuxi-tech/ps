package com.jiuxi.shared.common.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.*;

/**
 * 树形结构工具类
 * 提供扁平数据转树形结构等功能
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public final class TreeUtils {

    private TreeUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 树形结构接口
     * 需要构建树形结构的对象需实现此接口
     */
    public interface TreeNode {
        /**
         * 获取节点ID
         */
        Object getTreeId();
        
        /**
         * 获取父节点ID
         */
        Object getTreeParentId();
        
        /**
         * 获取子节点列表
         */
        List<? extends TreeNode> getTreeChildren();
        
        /**
         * 设置子节点列表
         */
        void setTreeChildren(List<? extends TreeNode> children);
        
        /**
         * 是否为叶子节点
         */
        Boolean getTreeLeaf();
        
        /**
         * 设置是否为叶子节点
         */
        void setTreeLeaf(Boolean leaf);
        
        /**
         * 获取排序字段
         */
        default BigDecimal getTreeSort() {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 将扁平数据转换为树结构
     * 
     * @param list 扁平数据列表
     * @param <T>  树节点类型
     * @return 树形结构列表
     */
    public static <T extends TreeNode> List<T> buildTree(List<T> list) {
        return buildTree(list, true);
    }

    /**
     * 将扁平数据转换为树结构
     * 
     * @param list        扁平数据列表
     * @param setTreeLeaf 是否设置叶子节点标识
     * @param <T>         树节点类型
     * @return 树形结构列表
     */
    @SuppressWarnings("unchecked")
    public static <T extends TreeNode> List<T> buildTree(List<T> list, boolean setTreeLeaf) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Object, T> nodeMap = new HashMap<>();
        List<T> rootNodes = new ArrayList<>();

        // 第一步：建立ID到节点的映射，并初始化叶子节点标识
        for (T node : list) {
            if (setTreeLeaf) {
                node.setTreeLeaf(true);
            }
            nodeMap.put(node.getTreeId(), node);
        }

        // 第二步：构建父子关系
        for (T node : list) {
            Object parentId = node.getTreeParentId();
            
            if (parentId == null || "".equals(parentId) || "0".equals(String.valueOf(parentId))) {
                // 根节点
                rootNodes.add(node);
            } else {
                // 子节点
                T parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    if (setTreeLeaf) {
                        parentNode.setTreeLeaf(false);
                    }
                    
                    List<T> children = (List<T>) parentNode.getTreeChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                        parentNode.setTreeChildren(children);
                    }
                    children.add(node);
                }
            }
        }

        // 第三步：对每个层级的节点进行排序
        sortTreeNodes(rootNodes);
        for (T rootNode : rootNodes) {
            sortChildrenRecursively(rootNode);
        }

        return rootNodes;
    }

    /**
     * 递归排序子节点
     */
    @SuppressWarnings("unchecked")
    private static <T extends TreeNode> void sortChildrenRecursively(T node) {
        List<T> children = (List<T>) node.getTreeChildren();
        if (children != null && !children.isEmpty()) {
            sortTreeNodes(children);
            for (T child : children) {
                sortChildrenRecursively(child);
            }
        }
    }

    /**
     * 对树节点列表进行排序
     */
    private static <T extends TreeNode> void sortTreeNodes(List<T> nodes) {
        if (nodes != null && nodes.size() > 1) {
            nodes.sort((o1, o2) -> {
                BigDecimal sort1 = o1.getTreeSort();
                BigDecimal sort2 = o2.getTreeSort();
                
                if (sort1 == null && sort2 == null) {
                    return 0;
                } else if (sort1 == null) {
                    return 1;
                } else if (sort2 == null) {
                    return -1;
                } else {
                    return sort1.compareTo(sort2);
                }
            });
        }
    }

    /**
     * 获取树的所有叶子节点
     * 
     * @param treeList 树形结构列表
     * @param <T>      树节点类型
     * @return 所有叶子节点列表
     */
    @SuppressWarnings("unchecked")
    public static <T extends TreeNode> List<T> getAllLeafNodes(List<T> treeList) {
        List<T> leafNodes = new ArrayList<>();
        if (treeList != null) {
            for (T node : treeList) {
                collectLeafNodes(node, leafNodes);
            }
        }
        return leafNodes;
    }

    /**
     * 递归收集叶子节点
     */
    @SuppressWarnings("unchecked")
    private static <T extends TreeNode> void collectLeafNodes(T node, List<T> leafNodes) {
        if (Boolean.TRUE.equals(node.getTreeLeaf())) {
            leafNodes.add(node);
        } else {
            List<T> children = (List<T>) node.getTreeChildren();
            if (children != null) {
                for (T child : children) {
                    collectLeafNodes(child, leafNodes);
                }
            }
        }
    }

    /**
     * 根据ID查找树节点
     * 
     * @param treeList 树形结构列表
     * @param id       节点ID
     * @param <T>      树节点类型
     * @return 找到的节点，未找到返回null
     */
    @SuppressWarnings("unchecked")
    public static <T extends TreeNode> T findNodeById(List<T> treeList, Object id) {
        if (treeList == null || id == null) {
            return null;
        }
        
        for (T node : treeList) {
            T found = findNodeByIdRecursively(node, id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * 递归查找节点
     */
    @SuppressWarnings("unchecked")
    private static <T extends TreeNode> T findNodeByIdRecursively(T node, Object id) {
        if (Objects.equals(node.getTreeId(), id)) {
            return node;
        }
        
        List<T> children = (List<T>) node.getTreeChildren();
        if (children != null) {
            for (T child : children) {
                T found = findNodeByIdRecursively(child, id);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    /**
     * 获取节点的所有祖先节点ID
     * 
     * @param treeList 树形结构列表
     * @param id       节点ID
     * @param <T>      树节点类型
     * @return 祖先节点ID列表（从根到父节点）
     */
    public static <T extends TreeNode> List<Object> getAncestorIds(List<T> treeList, Object id) {
        List<Object> ancestors = new ArrayList<>();
        T node = findNodeById(treeList, id);
        if (node != null) {
            collectAncestorIds(treeList, node.getTreeParentId(), ancestors);
            Collections.reverse(ancestors);
        }
        return ancestors;
    }

    /**
     * 递归收集祖先节点ID
     */
    private static <T extends TreeNode> void collectAncestorIds(List<T> treeList, Object parentId, List<Object> ancestors) {
        if (parentId != null && !"".equals(parentId) && !"0".equals(String.valueOf(parentId))) {
            ancestors.add(parentId);
            T parentNode = findNodeById(treeList, parentId);
            if (parentNode != null) {
                collectAncestorIds(treeList, parentNode.getTreeParentId(), ancestors);
            }
        }
    }

    // ================ 向后兼容的抽象类和接口 ================

    /**
     * @deprecated 使用 {@link TreeNode} 接口替代
     */
    @Deprecated
    public static abstract class Tree implements TreeNode {
        @JsonIgnore
        private List<Tree> treeChildren;
        @JsonIgnore
        private Boolean treeLeaf;

        @Override
        @SuppressWarnings("unchecked")
        public List<? extends TreeNode> getTreeChildren() {
            return (List<? extends TreeNode>) treeChildren;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void setTreeChildren(List<? extends TreeNode> children) {
            this.treeChildren = (List<Tree>) children;
        }

        @Override
        public Boolean getTreeLeaf() {
            return treeLeaf;
        }

        @Override
        public void setTreeLeaf(Boolean leaf) {
            this.treeLeaf = leaf;
        }

        // 子类需要实现的抽象方法
        @Override
        public abstract Object getTreeId();
        
        @Override
        public abstract Object getTreeParentId();
    }
}