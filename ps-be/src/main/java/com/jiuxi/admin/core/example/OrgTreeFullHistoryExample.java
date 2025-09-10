package com.jiuxi.admin.core.example;

import com.alibaba.fastjson.JSONObject;
import com.jiuxi.admin.core.bean.OrgTreeChangeHistory;
import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.admin.core.service.OrgTreeChangeHistoryService;
import com.jiuxi.admin.core.service.TpDeptBasicinfoService;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.common.constant.TpConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 组织机构历史记录全节点树功能示例
 * 演示如何使用新的 recordChangeWithFullTree 方法记录完整的组织架构变更历史
 * 
 * @author 系统生成
 * @since 1.0
 */
@Component
public class OrgTreeFullHistoryExample {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OrgTreeFullHistoryExample.class);
    
    @Autowired
    private TpDeptBasicinfoService tpDeptBasicinfoService;
    
    @Autowired
    private OrgTreeChangeHistoryService orgTreeChangeHistoryService;
    
    /**
     * 演示新增部门时记录完整树结构的历史
     */
    public void demonstrateAddDeptWithFullTree() {
        LOGGER.info("=== 演示新增部门时记录完整树结构的历史 ===");
        
        try {
            // 构造一个测试部门数据
            TpDeptBasicinfoVO deptVO = new TpDeptBasicinfoVO();
            deptVO.setDeptFullName("测试部门-全树历史");
            deptVO.setDeptSimpleName("测试部门");
            deptVO.setDeptType("部门");
            deptVO.setPdeptId(TpConstant.NODE.TOP_NODE_ID);
            deptVO.setCategory(TpConstant.Category.ORG);
            
            // 模拟操作用户ID
            String operatorId = "1001";
            
            // 执行新增操作（这将自动记录包含完整树的变更历史）
            LOGGER.info("执行新增部门操作...");
            TpDeptBasicinfoVO result = tpDeptBasicinfoService.add(deptVO, operatorId, TpConstant.Category.ORG);
            
            if (result != null) {
                LOGGER.info("新增部门成功，部门ID: {}", result.getDeptId());
                
                // 查询最新的变更历史记录
                OrgTreeChangeHistory latestHistory = orgTreeChangeHistoryService.getLatestVersion();
                if (latestHistory != null) {
                    LOGGER.info("最新变更历史记录ID: {}", latestHistory.getId());
                    LOGGER.info("操作类型: {}", latestHistory.getOperationType());
                    LOGGER.info("变更前完整树是否为空: {}", latestHistory.getBeforeFullTree() == null);
                    LOGGER.info("变更后完整树是否存在: {}", latestHistory.getAfterFullTree() != null);
                }
            }
            
        } catch (Exception e) {
            LOGGER.error("演示新增部门失败", e);
        }
    }
    
    /**
     * 演示修改部门时记录完整树结构的历史
     */
    public void demonstrateUpdateDeptWithFullTree(String deptId) {
        LOGGER.info("=== 演示修改部门时记录完整树结构的历史 ===");
        
        try {
            // 获取现有部门信息
            TpDeptBasicinfoVO deptVO = tpDeptBasicinfoService.view(deptId);
            if (deptVO == null) {
                LOGGER.warn("部门不存在: {}", deptId);
                return;
            }
            
            // 修改部门名称
            String originalName = deptVO.getDeptFullName();
            deptVO.setDeptFullName(originalName + "-已修改");
            
            // 模拟操作用户ID
            String operatorId = "1001";
            
            // 执行修改操作（这将自动记录包含完整树的变更历史）
            LOGGER.info("执行修改部门操作...");
            TpDeptBasicinfoVO result = tpDeptBasicinfoService.update(deptVO, operatorId);
            
            if (result != null) {
                LOGGER.info("修改部门成功，部门ID: {}", result.getDeptId());
                
                // 查询最新的变更历史记录
                OrgTreeChangeHistory latestHistory = orgTreeChangeHistoryService.getLatestVersion();
                if (latestHistory != null) {
                    LOGGER.info("最新变更历史记录ID: {}", latestHistory.getId());
                    LOGGER.info("操作类型: {}", latestHistory.getOperationType());
                    LOGGER.info("变更前完整树是否存在: {}", latestHistory.getBeforeFullTree() != null);
                    LOGGER.info("变更后完整树是否存在: {}", latestHistory.getAfterFullTree() != null);
                }
            }
            
        } catch (Exception e) {
            LOGGER.error("演示修改部门失败", e);
        }
    }
    
    /**
     * 演示删除部门时记录完整树结构的历史
     */
    public void demonstrateDeleteDeptWithFullTree(String deptId) {
        LOGGER.info("=== 演示删除部门时记录完整树结构的历史 ===");
        
        try {
            // 模拟操作用户ID
            String operatorId = "1001";
            
            // 执行删除操作（这将自动记录包含完整树的变更历史）
            LOGGER.info("执行删除部门操作...");
            int result = tpDeptBasicinfoService.removeById(deptId, operatorId);
            
            if (result > 0) {
                LOGGER.info("删除部门成功，部门ID: {}", deptId);
                
                // 查询最新的变更历史记录
                OrgTreeChangeHistory latestHistory = orgTreeChangeHistoryService.getLatestVersion();
                if (latestHistory != null) {
                    LOGGER.info("最新变更历史记录ID: {}", latestHistory.getId());
                    LOGGER.info("操作类型: {}", latestHistory.getOperationType());
                    LOGGER.info("变更前完整树是否存在: {}", latestHistory.getBeforeFullTree() != null);
                    LOGGER.info("变更后完整树是否存在: {}", latestHistory.getAfterFullTree() != null);
                }
            }
            
        } catch (Exception e) {
            LOGGER.error("演示删除部门失败", e);
        }
    }
    
    /**
     * 演示获取完整组织机构树
     */
    public void demonstrateGetFullTree() {
        LOGGER.info("=== 演示获取完整组织机构树 ===");
        
        try {
            // 获取完整的政府组织机构树
            List<TreeNode> fullTree = tpDeptBasicinfoService.getFullTree(null, TpConstant.Category.ORG);
            
            LOGGER.info("获取到的完整树节点数量: {}", fullTree.size());
            
            // 打印树结构的简要信息
            printTreeStructure(fullTree, 0);
            
        } catch (Exception e) {
            LOGGER.error("演示获取完整树失败", e);
        }
    }
    
    /**
     * 递归打印树结构
     */
    private void printTreeStructure(List<TreeNode> nodes, int level) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }
        
        StringBuilder indentBuilder = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indentBuilder.append("  ");
        }
        String indent = indentBuilder.toString();
        for (TreeNode node : nodes) {
            LOGGER.info("{}|- {} (ID: {})", indent, node.getText(), node.getId());
            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                printTreeStructure(node.getChildren(), level + 1);
            }
        }
    }
    
    /**
     * 演示查询变更历史记录
     */
    public void demonstrateQueryChangeHistory() {
        LOGGER.info("=== 演示查询变更历史记录 ===");
        
        try {
            // 获取总记录数
            Long totalCount = orgTreeChangeHistoryService.countTotal();
            LOGGER.info("总变更历史记录数: {}", totalCount);
            
            // 获取最新的变更记录
            OrgTreeChangeHistory latestRecord = orgTreeChangeHistoryService.getLatestVersion();
            if (latestRecord != null) {
                LOGGER.info("最新变更记录:");
                LOGGER.info("  ID: {}", latestRecord.getId());
                LOGGER.info("  操作类型: {}", latestRecord.getOperationType());
                LOGGER.info("  操作时间: {}", latestRecord.getOperationTime());
                LOGGER.info("  操作用户ID: {}", latestRecord.getOperatorId());
                LOGGER.info("  是否包含完整树: {}", 
                    latestRecord.getBeforeFullTree() != null || latestRecord.getAfterFullTree() != null);
            }
            
        } catch (Exception e) {
            LOGGER.error("演示查询变更历史失败", e);
        }
    }
    
    /**
     * 运行所有演示
     */
    public void runAllDemonstrations() {
        LOGGER.info("开始运行组织机构历史记录全节点树功能演示...");
        
        // 1. 演示获取完整树
        demonstrateGetFullTree();
        
        // 2. 演示查询变更历史
        demonstrateQueryChangeHistory();
        
        // 3. 演示新增部门
        demonstrateAddDeptWithFullTree();
        
        // 注意：实际的修改和删除演示需要具体的部门ID，这里不执行
        // demonstrateUpdateDeptWithFullTree("具体的部门ID");
        // demonstrateDeleteDeptWithFullTree("具体的部门ID");
        
        LOGGER.info("组织机构历史记录全节点树功能演示完成！");
    }
}