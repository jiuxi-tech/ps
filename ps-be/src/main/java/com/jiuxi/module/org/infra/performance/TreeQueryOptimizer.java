package com.jiuxi.module.org.infra.performance;

import com.jiuxi.module.org.domain.model.aggregate.Department;
import com.jiuxi.module.org.domain.model.aggregate.Organization;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 树查询性能优化器
 * 提供高效的树形结构构建和查询优化
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component
public class TreeQueryOptimizer {
    
    /**
     * 高效构建部门树
     * 使用一次查询 + 内存构建的方式，避免递归数据库查询
     * 
     * @param allDepartments 所有部门列表（平铺结构）
     * @return 构建好的树形结构
     */
    public List<Department> buildDepartmentTree(List<Department> allDepartments) {
        if (allDepartments == null || allDepartments.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 使用Map进行快速查找，时间复杂度O(1)
        Map<String, Department> deptMap = allDepartments.stream()
                .collect(Collectors.toMap(Department::getDeptId, dept -> dept));
        
        // 构建父子关系映射
        Map<String, List<Department>> childrenMap = new HashMap<>();
        List<Department> rootDepartments = new ArrayList<>();
        
        for (Department dept : allDepartments) {
            String parentId = dept.getParentDeptId();
            
            if (!StringUtils.hasText(parentId) || !deptMap.containsKey(parentId)) {
                // 根部门
                rootDepartments.add(dept);
            } else {
                // 子部门
                childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(dept);
            }
        }
        
        // 递归设置子部门
        setChildrenRecursively(rootDepartments, childrenMap);
        
        // 按层级和排序号排序
        return rootDepartments.stream()
                .sorted(this::compareDepartments)
                .collect(Collectors.toList());
    }
    
    /**
     * 高效构建组织树
     * 
     * @param allOrganizations 所有组织列表（平铺结构）
     * @return 构建好的树形结构
     */
    public List<Organization> buildOrganizationTree(List<Organization> allOrganizations) {
        if (allOrganizations == null || allOrganizations.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 使用Map进行快速查找
        Map<String, Organization> orgMap = allOrganizations.stream()
                .collect(Collectors.toMap(Organization::getOrganizationId, org -> org));
        
        // 构建父子关系映射
        Map<String, List<Organization>> childrenMap = new HashMap<>();
        List<Organization> rootOrganizations = new ArrayList<>();
        
        for (Organization org : allOrganizations) {
            String parentId = org.getParentOrganizationId();
            
            if (!StringUtils.hasText(parentId) || !orgMap.containsKey(parentId)) {
                // 根组织
                rootOrganizations.add(org);
            } else {
                // 子组织
                childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(org);
            }
        }
        
        // 递归设置子组织
        setOrgChildrenRecursively(rootOrganizations, childrenMap);
        
        // 按层级排序
        return rootOrganizations.stream()
                .sorted(this::compareOrganizations)
                .collect(Collectors.toList());
    }
    
    /**
     * 查找节点的所有祖先
     * 使用路径信息进行快速查找，避免递归查询
     * 
     * @param nodeId 节点ID
     * @param nodePath 节点路径
     * @param allNodes 所有节点的Map
     * @return 祖先节点列表
     */
    public <T> List<T> findAncestors(String nodeId, String nodePath, Map<String, T> allNodes) {
        List<T> ancestors = new ArrayList<>();
        
        if (!StringUtils.hasText(nodePath)) {
            return ancestors;
        }
        
        // 解析路径获取祖先ID
        String[] pathParts = nodePath.split("/");
        for (String ancestorId : pathParts) {
            if (StringUtils.hasText(ancestorId) && !ancestorId.equals(nodeId)) {
                T ancestor = allNodes.get(ancestorId);
                if (ancestor != null) {
                    ancestors.add(ancestor);
                }
            }
        }
        
        return ancestors;
    }
    
    /**
     * 查找节点的所有后代
     * 使用路径匹配进行批量过滤
     * 
     * @param nodePath 节点路径
     * @param allNodes 所有节点列表
     * @param pathExtractor 路径提取函数
     * @return 后代节点列表
     */
    public <T> List<T> findDescendants(String nodePath, List<T> allNodes, 
                                      java.util.function.Function<T, String> pathExtractor) {
        if (!StringUtils.hasText(nodePath) || allNodes == null) {
            return new ArrayList<>();
        }
        
        String searchPrefix = nodePath + "/";
        
        return allNodes.stream()
                .filter(node -> {
                    String path = pathExtractor.apply(node);
                    return StringUtils.hasText(path) && path.startsWith(searchPrefix);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 批量路径计算
     * 当节点移动或层级变化时，批量计算新的路径
     * 
     * @param changedNodes 变更的节点列表
     * @param pathSetter 路径设置函数
     * @param pathGetter 路径获取函数
     * @param parentIdGetter 父ID获取函数
     * @param nodeIdGetter 节点ID获取函数
     */
    public <T> void batchUpdatePaths(List<T> changedNodes,
                                   java.util.function.BiConsumer<T, String> pathSetter,
                                   java.util.function.Function<T, String> pathGetter,
                                   java.util.function.Function<T, String> parentIdGetter,
                                   java.util.function.Function<T, String> nodeIdGetter) {
        
        Map<String, T> nodeMap = changedNodes.stream()
                .collect(Collectors.toMap(nodeIdGetter, node -> node));
        
        // 多次遍历直到所有路径都计算完成
        boolean hasChanges;
        int maxIterations = 100; // 防止无限循环
        int iterations = 0;
        
        do {
            hasChanges = false;
            iterations++;
            
            for (T node : changedNodes) {
                String currentPath = pathGetter.apply(node);
                String parentId = parentIdGetter.apply(node);
                String nodeId = nodeIdGetter.apply(node);
                
                String newPath;
                if (!StringUtils.hasText(parentId)) {
                    // 根节点
                    newPath = nodeId;
                } else {
                    T parent = nodeMap.get(parentId);
                    if (parent != null) {
                        String parentPath = pathGetter.apply(parent);
                        if (StringUtils.hasText(parentPath)) {
                            newPath = parentPath + "/" + nodeId;
                        } else {
                            // 父节点路径还未计算，跳过
                            continue;
                        }
                    } else {
                        // 父节点不在变更列表中，使用父ID作为路径前缀
                        newPath = parentId + "/" + nodeId;
                    }
                }
                
                if (!Objects.equals(currentPath, newPath)) {
                    pathSetter.accept(node, newPath);
                    hasChanges = true;
                }
            }
        } while (hasChanges && iterations < maxIterations);
    }
    
    /**
     * 递归设置部门子节点
     */
    private void setChildrenRecursively(List<Department> departments, 
                                      Map<String, List<Department>> childrenMap) {
        for (Department dept : departments) {
            List<Department> children = childrenMap.get(dept.getDeptId());
            if (children != null && !children.isEmpty()) {
                // 排序子部门
                children.sort(this::compareDepartments);
                dept.setChildren(children);
                
                // 递归处理子部门
                setChildrenRecursively(children, childrenMap);
            }
        }
    }
    
    /**
     * 递归设置组织子节点
     */
    private void setOrgChildrenRecursively(List<Organization> organizations, 
                                         Map<String, List<Organization>> childrenMap) {
        for (Organization org : organizations) {
            List<Organization> children = childrenMap.get(org.getOrganizationId());
            if (children != null && !children.isEmpty()) {
                // 排序子组织
                children.sort(this::compareOrganizations);
                org.setSubOrganizations(children);
                
                // 递归处理子组织
                setOrgChildrenRecursively(children, childrenMap);
            }
        }
    }
    
    /**
     * 部门比较器
     */
    private int compareDepartments(Department a, Department b) {
        // 首先按层级排序
        int levelCompare = Integer.compare(
                a.getDeptLevel() != null ? a.getDeptLevel() : 0,
                b.getDeptLevel() != null ? b.getDeptLevel() : 0
        );
        
        if (levelCompare != 0) {
            return levelCompare;
        }
        
        // 然后按排序号排序
        int orderCompare = Integer.compare(
                a.getOrderIndex() != null ? a.getOrderIndex() : Integer.MAX_VALUE,
                b.getOrderIndex() != null ? b.getOrderIndex() : Integer.MAX_VALUE
        );
        
        if (orderCompare != 0) {
            return orderCompare;
        }
        
        // 最后按名称排序
        return a.getDeptName().compareTo(b.getDeptName());
    }
    
    /**
     * 组织比较器
     */
    private int compareOrganizations(Organization a, Organization b) {
        // 首先按层级排序
        int levelCompare = Integer.compare(
                a.getOrganizationLevel() != null ? a.getOrganizationLevel() : 0,
                b.getOrganizationLevel() != null ? b.getOrganizationLevel() : 0
        );
        
        if (levelCompare != 0) {
            return levelCompare;
        }
        
        // 然后按名称排序
        return a.getOrganizationName().compareTo(b.getOrganizationName());
    }
}