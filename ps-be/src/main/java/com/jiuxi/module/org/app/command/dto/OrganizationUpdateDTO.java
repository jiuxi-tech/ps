package com.jiuxi.module.org.app.command.dto;

import com.jiuxi.module.org.domain.model.entity.OrganizationType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 组织更新DTO
 * 用于组织更新命令的数据传输
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class OrganizationUpdateDTO {
    
    @NotBlank(message = "组织名称不能为空")
    @Size(max = 200, message = "组织名称长度不能超过200字符")
    private String organizationName;
    
    @Size(max = 50, message = "组织代码长度不能超过50字符")
    private String organizationCode;
    
    private OrganizationType organizationType;
    
    private String parentOrganizationId;
    
    @Size(max = 1000, message = "描述长度不能超过1000字符")
    private String description;
    
    private Integer orderIndex = 0;
    
    // Constructors
    public OrganizationUpdateDTO() {}
    
    public OrganizationUpdateDTO(String organizationName, OrganizationType organizationType) {
        this.organizationName = organizationName;
        this.organizationType = organizationType;
    }
    
    // Getters and Setters
    public String getOrganizationName() {
        return organizationName;
    }
    
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
    
    public String getOrganizationCode() {
        return organizationCode;
    }
    
    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }
    
    public OrganizationType getOrganizationType() {
        return organizationType;
    }
    
    public void setOrganizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
    }
    
    public String getParentOrganizationId() {
        return parentOrganizationId;
    }
    
    public void setParentOrganizationId(String parentOrganizationId) {
        this.parentOrganizationId = parentOrganizationId;
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
        return "OrganizationUpdateDTO{" +
                "organizationName='" + organizationName + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", organizationType=" + organizationType +
                ", parentOrganizationId='" + parentOrganizationId + '\'' +
                ", description='" + description + '\'' +
                ", orderIndex=" + orderIndex +
                '}';
    }
}