package com.jiuxi.admin.core.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组织机构变更历史记录VO
 *
 * @author AI Assistant
 * @date 2024-01-26
 */
public class OrgTreeChangeHistoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作ID（主键，自增）
     */
    private Long id;

    /**
     * 操作类型（CREATE/UPDATE/DELETE/QUERY）
     */
    private String operationType;

    /**
     * 操作类型名称
     */
    private String operationTypeName;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    /**
     * 操作用户ID
     */
    private Long operatorUserId;

    /**
     * 操作用户名称
     */
    private String operatorUserName;

    /**
     * 变更前的组织机构树JSON数据
     */
    private String beforeData;

    /**
     * 变更后的组织机构树JSON数据
     */
    private String afterData;

    /**
     * 变更前的完整组织机构节点树JSON数据
     */
    private String beforeFullTree;

    /**
     * 变更后的完整组织机构节点树JSON数据
     */
    private String afterFullTree;

    /**
     * 版本号
     */
    private Long version;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 组织/部门名称
     */
    private String orgName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationTypeName() {
        return operationTypeName;
    }

    public void setOperationTypeName(String operationTypeName) {
        this.operationTypeName = operationTypeName;
    }

    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }

    public Long getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(Long operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

    public String getOperatorUserName() {
        return operatorUserName;
    }

    public void setOperatorUserName(String operatorUserName) {
        this.operatorUserName = operatorUserName;
    }

    public String getBeforeData() {
        return beforeData;
    }

    public void setBeforeData(String beforeData) {
        this.beforeData = beforeData;
    }

    public String getAfterData() {
        return afterData;
    }

    public void setAfterData(String afterData) {
        this.afterData = afterData;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getBeforeFullTree() {
        return beforeFullTree;
    }

    public void setBeforeFullTree(String beforeFullTree) {
        this.beforeFullTree = beforeFullTree;
    }

    public String getAfterFullTree() {
        return afterFullTree;
    }

    public void setAfterFullTree(String afterFullTree) {
        this.afterFullTree = afterFullTree;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}