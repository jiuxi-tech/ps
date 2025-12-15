package com.jiuxi.admin.core.bean.entity;

import java.io.Serializable;

/**
 * @ClassName: TpMenuHistory
 * @Description: 菜单修改历史记录表
 * @Author: AI Assistant
 * @Date: 2025-01-29
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TpMenuHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 历史记录ID
     */
    private String historyId;

    /**
     * 菜单ID
     */
    private String menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 操作类型：ADD-新增，UPDATE-修改，DELETE-删除
     */
    private String operationType;

    /**
     * 节点修改前数据（JSON格式）
     */
    private String nodeDataBefore;

    /**
     * 节点修改后数据（JSON格式）
     */
    private String nodeDataAfter;

    /**
     * 全表修改前数据（JSON格式）
     */
    private String fullTreeBefore;

    /**
     * 全表修改后数据（JSON格式）
     */
    private String fullTreeAfter;

    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 操作时间（yyyyMMddHHmmss）
     */
    private String operationTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 修改人
     */
    private String updator;

    // Getter and Setter methods
    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getNodeDataBefore() {
        return nodeDataBefore;
    }

    public void setNodeDataBefore(String nodeDataBefore) {
        this.nodeDataBefore = nodeDataBefore;
    }

    public String getNodeDataAfter() {
        return nodeDataAfter;
    }

    public void setNodeDataAfter(String nodeDataAfter) {
        this.nodeDataAfter = nodeDataAfter;
    }

    public String getFullTreeBefore() {
        return fullTreeBefore;
    }

    public void setFullTreeBefore(String fullTreeBefore) {
        this.fullTreeBefore = fullTreeBefore;
    }

    public String getFullTreeAfter() {
        return fullTreeAfter;
    }

    public void setFullTreeAfter(String fullTreeAfter) {
        this.fullTreeAfter = fullTreeAfter;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    @Override
    public String toString() {
        return "TpMenuHistory{" +
                "historyId='" + historyId + '\'' +
                ", menuId='" + menuId + '\'' +
                ", menuName='" + menuName + '\'' +
                ", operationType='" + operationType + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", operationTime='" + operationTime + '\'' +
                '}';
    }
}