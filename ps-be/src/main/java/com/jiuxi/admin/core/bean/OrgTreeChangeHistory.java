package com.jiuxi.admin.core.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组织机构树变更历史记录表实体类
 * 用于存储组织机构树的所有变更历史
 *
 * @author 系统生成
 * @since 1.0
 */
// @ApiModel(value = "OrgTreeChangeHistory", description = "组织机构树变更历史记录表")
@TableName("org_tree_change_history")
public class OrgTreeChangeHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作ID（主键，自增）
     */
    // @ApiModelProperty(value = "操作ID（主键，自增）")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作类型（CREATE/UPDATE/DELETE/QUERY）
     */
    // @ApiModelProperty(value = "操作类型（CREATE/UPDATE/DELETE/QUERY）")
    @TableField("operation_type")
    private String operationType;

    /**
     * 操作时间（默认当前时间）
     */
    // @ApiModelProperty(value = "操作时间（默认当前时间）")
    @TableField("operation_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    /**
     * 操作用户ID（外键关联用户表）
     */
    // @ApiModelProperty(value = "操作用户ID（外键关联用户表）")
    @TableField("operator_id")
    private Long operatorId;

    /**
     * 变更前的组织机构树JSON数据
     */
    // @ApiModelProperty(value = "变更前的组织机构树JSON数据")
    @TableField("before_data")
    private String beforeData;

    /**
     * 变更后的组织机构树JSON数据
     */
    // @ApiModelProperty(value = "变更后的组织机构树JSON数据")
    @TableField("after_data")
    private String afterData;

    /**
     * 变更前的完整组织机构节点树JSON数据
     */
    // @ApiModelProperty(value = "变更前的完整组织机构节点树JSON数据")
    @TableField("before_full_tree")
    private String beforeFullTree;

    /**
     * 变更后的完整组织机构节点树JSON数据
     */
    // @ApiModelProperty(value = "变更后的完整组织机构节点树JSON数据")
    @TableField("after_full_tree")
    private String afterFullTree;

    /**
     * 版本号（数据库字段，不参与业务逻辑，用ID代替版本功能）
     * 设置默认值以满足数据库约束
     */
    @TableField("version")
    private Long version = 1L;

    /**
     * 部门ID（数据库字段，不参与业务逻辑）
     * 设置默认值以满足数据库约束
     */
    @TableField("dept_id")
    private Long deptId = 0L;



    /**
     * 默认构造函数
     */
    public OrgTreeChangeHistory() {}

    /**
     * 带参构造函数
     */
    public OrgTreeChangeHistory(String operationType, Long operatorId, String beforeData, 
                               String afterData) {
        this.operationType = operationType;
        this.operatorId = operatorId;
        this.beforeData = beforeData;
        this.afterData = afterData;
        this.operationTime = LocalDateTime.now();
    }

    /**
     * 带全节点树参数的构造函数
     */
    public OrgTreeChangeHistory(String operationType, Long operatorId, String beforeData, 
                               String afterData, String beforeFullTree, String afterFullTree) {
        this.operationType = operationType;
        this.operatorId = operatorId;
        this.beforeData = beforeData;
        this.afterData = afterData;
        this.beforeFullTree = beforeFullTree;
        this.afterFullTree = afterFullTree;
        this.operationTime = LocalDateTime.now();
    }

    // Getter and Setter methods
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

    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
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

    @Override
    public String toString() {
        return "OrgTreeChangeHistory{" +
                "id=" + id +
                ", operationType='" + operationType + '\'' +
                ", operationTime=" + operationTime +
                ", operatorId=" + operatorId +
                ", beforeData='" + beforeData + '\'' +
                ", afterData='" + afterData + '\'' +
                ", beforeFullTree='" + beforeFullTree + '\'' +
                ", afterFullTree='" + afterFullTree + '\'' +
                ", version=" + version +
                ", deptId=" + deptId +
                '}';
    }

    /**
     * 操作类型常量
     */
    public static class OperationType {
        public static final String CREATE = "CREATE";
        public static final String UPDATE = "UPDATE";
        public static final String DELETE = "DELETE";
        public static final String QUERY = "QUERY";
    }
}