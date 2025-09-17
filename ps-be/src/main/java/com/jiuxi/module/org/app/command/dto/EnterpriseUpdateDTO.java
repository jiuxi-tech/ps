package com.jiuxi.module.org.app.command.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * 企业更新DTO
 * 用于企业更新命令的数据传输
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class EnterpriseUpdateDTO {
    
    @NotBlank(message = "企业全称不能为空")
    @Size(max = 200, message = "企业全称长度不能超过200字符")
    private String entFullName;
    
    @Size(max = 100, message = "企业简称长度不能超过100字符")
    private String entSimpleName;
    
    @NotBlank(message = "统一社会信用代码不能为空")
    @Pattern(regexp = "^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$", 
             message = "统一社会信用代码格式不正确")
    private String entUnifiedCode;
    
    @Size(max = 50, message = "企业类型长度不能超过50字符")
    private String entType;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String entPhone;
    
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100字符")
    private String entEmail;
    
    @Size(max = 500, message = "企业地址长度不能超过500字符")
    private String entAddress;
    
    @Size(max = 50, message = "法定代表人长度不能超过50字符")
    private String entLegalPerson;
    
    @Size(max = 50, message = "企业负责人长度不能超过50字符")
    private String entManager;
    
    @Size(max = 1000, message = "企业描述长度不能超过1000字符")
    private String entDescription;
    
    private String entLongitude; // 经度
    
    private String entLatitude; // 纬度
    
    // Constructors
    public EnterpriseUpdateDTO() {}
    
    public EnterpriseUpdateDTO(String entFullName, String entUnifiedCode) {
        this.entFullName = entFullName;
        this.entUnifiedCode = entUnifiedCode;
    }
    
    // Getters and Setters
    public String getEntFullName() {
        return entFullName;
    }
    
    public void setEntFullName(String entFullName) {
        this.entFullName = entFullName;
    }
    
    public String getEntSimpleName() {
        return entSimpleName;
    }
    
    public void setEntSimpleName(String entSimpleName) {
        this.entSimpleName = entSimpleName;
    }
    
    public String getEntUnifiedCode() {
        return entUnifiedCode;
    }
    
    public void setEntUnifiedCode(String entUnifiedCode) {
        this.entUnifiedCode = entUnifiedCode;
    }
    
    public String getEntType() {
        return entType;
    }
    
    public void setEntType(String entType) {
        this.entType = entType;
    }
    
    public String getEntPhone() {
        return entPhone;
    }
    
    public void setEntPhone(String entPhone) {
        this.entPhone = entPhone;
    }
    
    public String getEntEmail() {
        return entEmail;
    }
    
    public void setEntEmail(String entEmail) {
        this.entEmail = entEmail;
    }
    
    public String getEntAddress() {
        return entAddress;
    }
    
    public void setEntAddress(String entAddress) {
        this.entAddress = entAddress;
    }
    
    public String getEntLegalPerson() {
        return entLegalPerson;
    }
    
    public void setEntLegalPerson(String entLegalPerson) {
        this.entLegalPerson = entLegalPerson;
    }
    
    public String getEntManager() {
        return entManager;
    }
    
    public void setEntManager(String entManager) {
        this.entManager = entManager;
    }
    
    public String getEntDescription() {
        return entDescription;
    }
    
    public void setEntDescription(String entDescription) {
        this.entDescription = entDescription;
    }
    
    public String getEntLongitude() {
        return entLongitude;
    }
    
    public void setEntLongitude(String entLongitude) {
        this.entLongitude = entLongitude;
    }
    
    public String getEntLatitude() {
        return entLatitude;
    }
    
    public void setEntLatitude(String entLatitude) {
        this.entLatitude = entLatitude;
    }
    
    @Override
    public String toString() {
        return "EnterpriseUpdateDTO{" +
                "entFullName='" + entFullName + '\'' +
                ", entSimpleName='" + entSimpleName + '\'' +
                ", entUnifiedCode='" + entUnifiedCode + '\'' +
                ", entType='" + entType + '\'' +
                ", entPhone='" + entPhone + '\'' +
                ", entEmail='" + entEmail + '\'' +
                ", entAddress='" + entAddress + '\'' +
                ", entLegalPerson='" + entLegalPerson + '\'' +
                ", entManager='" + entManager + '\'' +
                ", entDescription='" + entDescription + '\'' +
                ", entLongitude='" + entLongitude + '\'' +
                ", entLatitude='" + entLatitude + '\'' +
                '}';
    }
}