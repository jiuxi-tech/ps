package com.jiuxi.module.org.app.command.dto;

import com.jiuxi.module.org.domain.model.entity.DepartmentType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 部门创建DTO
 * 用于部门创建命令的数据传输
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class DepartmentCreateDTO {
    
    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 100, message = "部门名称长度不能超过100个字符")
    private String deptName;
    
    /**
     * 部门简称
     */
    @Size(max = 50, message = "部门简称长度不能超过50个字符")
    private String deptSimpleName;
    
    /**
     * 部门全称
     */
    @Size(max = 200, message = "部门全称长度不能超过200个字符")
    private String deptFullName;
    
    /**
     * 父部门ID
     */
    private String parentDeptId;
    
    /**
     * 部门类型
     */
    private DepartmentType type;
    
    /**
     * 部门负责人
     */
    private String managerId;
    
    /**
     * 联系电话
     */
    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    private String contactPhone;
    
    /**
     * 联系地址
     */
    @Size(max = 200, message = "联系地址长度不能超过200个字符")
    private String address;
    
    /**
     * 部门描述
     */
    @Size(max = 500, message = "部门描述长度不能超过500个字符")
    private String description;
    
    /**
     * 排序序号
     */
    private Integer orderIndex;
    
    // Constructors
    public DepartmentCreateDTO() {}
    
    public DepartmentCreateDTO(String deptName, DepartmentType type) {
        this.deptName = deptName;
        this.type = type;
    }
    
    // Getters and Setters
    public String getDeptName() {
        return deptName;
    }
    
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
    public String getDeptSimpleName() {
        return deptSimpleName;
    }
    
    public void setDeptSimpleName(String deptSimpleName) {
        this.deptSimpleName = deptSimpleName;
    }
    
    public String getDeptFullName() {
        return deptFullName;
    }
    
    public void setDeptFullName(String deptFullName) {
        this.deptFullName = deptFullName;
    }
    
    public String getParentDeptId() {
        return parentDeptId;
    }
    
    public void setParentDeptId(String parentDeptId) {
        this.parentDeptId = parentDeptId;
    }
    
    public DepartmentType getType() {
        return type;
    }
    
    public void setType(DepartmentType type) {
        this.type = type;
    }
    
    public String getManagerId() {
        return managerId;
    }
    
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    @Override
    public String toString() {
        return "DepartmentCreateDTO{" +
                "deptName='" + deptName + '\'' +
                ", deptSimpleName='" + deptSimpleName + '\'' +
                ", deptFullName='" + deptFullName + '\'' +
                ", parentDeptId='" + parentDeptId + '\'' +
                ", type=" + type +
                ", managerId='" + managerId + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", orderIndex=" + orderIndex +
                '}';
    }
}