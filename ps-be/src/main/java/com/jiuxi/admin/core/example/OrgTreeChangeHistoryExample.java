package com.jiuxi.admin.core.example;

import com.alibaba.fastjson.JSONObject;
import com.jiuxi.admin.core.service.OrgTreeChangeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 组织机构变更历史使用示例
 * 展示如何使用新增的全节点树功能
 * 
 * @author AI Assistant
 * @date 2024-01-27
 */
@Service
public class OrgTreeChangeHistoryExample {

    @Autowired
    private OrgTreeChangeHistoryService orgTreeChangeHistoryService;

    /**
     * 示例：记录组织机构变更（包含全节点树）
     */
    public void recordDeptChangeExample() {
        // 模拟变更前的单个部门数据
        String beforeData = "{\n" +
                "  \"deptId\": \"123\",\n" +
                "  \"deptName\": \"技术部\",\n" +
                "  \"parentId\": \"100\"\n" +
                "}";

        // 模拟变更后的单个部门数据
        String afterData = "{\n" +
                "  \"deptId\": \"123\",\n" +
                "  \"deptName\": \"研发技术部\",\n" +
                "  \"parentId\": \"100\"\n" +
                "}";

        // 模拟变更前的完整组织树
        String beforeFullTree = "{\n" +
                "  \"id\": \"1\",\n" +
                "  \"name\": \"总公司\",\n" +
                "  \"children\": [\n" +
                "    {\n" +
                "      \"id\": \"100\",\n" +
                "      \"name\": \"技术中心\",\n" +
                "      \"children\": [\n" +
                "        {\n" +
                "          \"id\": \"123\",\n" +
                "          \"name\": \"技术部\",\n" +
                "          \"children\": []\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // 模拟变更后的完整组织树
        String afterFullTree = "{\n" +
                "  \"id\": \"1\",\n" +
                "  \"name\": \"总公司\",\n" +
                "  \"children\": [\n" +
                "    {\n" +
                "      \"id\": \"100\",\n" +
                "      \"name\": \"技术中心\",\n" +
                "      \"children\": [\n" +
                "        {\n" +
                "          \"id\": \"123\",\n" +
                "          \"name\": \"研发技术部\",\n" +
                "          \"children\": []\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // 使用新的方法记录变更（包含全节点树）
        Long historyId = orgTreeChangeHistoryService.recordChangeWithFullTree(
                "UPDATE",
                1001L, // 操作用户ID
                beforeData,
                afterData,
                beforeFullTree,
                afterFullTree
        );

        if (historyId != null) {
            System.out.println("记录变更成功，历史记录ID: " + historyId);
        } else {
            System.out.println("记录变更失败");
        }
    }

    /**
     * 示例：在TpDeptBasicinfoServiceImpl中的实际使用方式
     */
    public void actualUsageExample(String operationType, Long operatorId, 
                                 Object beforeDeptData, Object afterDeptData,
                                 Object beforeTreeData, Object afterTreeData) {
        try {
            // 将对象转换为JSON字符串
            String beforeData = beforeDeptData != null ? JSONObject.toJSONString(beforeDeptData) : null;
            String afterData = afterDeptData != null ? JSONObject.toJSONString(afterDeptData) : null;
            String beforeFullTree = beforeTreeData != null ? JSONObject.toJSONString(beforeTreeData) : null;
            String afterFullTree = afterTreeData != null ? JSONObject.toJSONString(afterTreeData) : null;

            // 记录组织架构变更历史（包含完整节点树）
            orgTreeChangeHistoryService.recordChangeWithFullTree(
                operationType,
                operatorId,
                beforeData,
                afterData,
                beforeFullTree,
                afterFullTree
            );
        } catch (Exception e) {
            // 记录变更历史失败不应该影响主业务，只记录警告日志
            System.err.println("记录组织架构变更历史失败，但不影响主业务: " + e.getMessage());
        }
    }
}