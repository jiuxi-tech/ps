package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 服务器证书实体类
 *
 * @author 系统生成
 * @since 2025-09-25
 */
@TableName("tp_server_cert")
public class TpServerCert {

    /**
     * 证书ID
     */
    @TableId(type = IdType.INPUT)
    private String certId;

    /**
     * 证书名称
     */
    private String certName;

    /**
     * 证书描述
     */
    private String certDesc;

    /**
     * PEM证书文件内容（BASE64编码）
     */
    private String pemContent;

    /**
     * 私钥文件内容（BASE64编码）
     */
    private String keyContent;

    /**
     * 绑定域名（JSON数组字符串）
     */
    private String domainNames;

    /**
     * 发证机构
     */
    private String issuer;

    /**
     * 公用名(CN)
     */
    private String subjectCn;

    /**
     * 组织(O)
     */
    private String subjectO;

    /**
     * 组织单位(OU)
     */
    private String subjectOu;

    /**
     * 颁发日期
     */
    private LocalDateTime issueDate;

    /**
     * 到期日期
     */
    private LocalDateTime expireDate;

    /**
     * 状态：0-未应用，1-已应用
     */
    private Integer status;

    /**
     * 是否正在使用：0-否，1-是
     */
    private Integer isInUse;

    /**
     * 是否过期：0-否，1-是
     */
    private Integer isExpired;

    /**
     * 应用时间
     */
    private LocalDateTime appliedTime;

    /**
     * 创建人ID
     */
    private String createPersonId;

    /**
     * 创建人姓名
     */
    private String createPersonName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    private String updatePersonId;

    /**
     * 更新人姓名
     */
    private String updatePersonName;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否有效：0-删除，1-有效
     */
    private Integer actived;

    // Getter and Setter methods

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getCertDesc() {
        return certDesc;
    }

    public void setCertDesc(String certDesc) {
        this.certDesc = certDesc;
    }

    public String getPemContent() {
        return pemContent;
    }

    public void setPemContent(String pemContent) {
        this.pemContent = pemContent;
    }

    public String getKeyContent() {
        return keyContent;
    }

    public void setKeyContent(String keyContent) {
        this.keyContent = keyContent;
    }

    public String getDomainNames() {
        return domainNames;
    }

    public void setDomainNames(String domainNames) {
        this.domainNames = domainNames;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSubjectCn() {
        return subjectCn;
    }

    public void setSubjectCn(String subjectCn) {
        this.subjectCn = subjectCn;
    }

    public String getSubjectO() {
        return subjectO;
    }

    public void setSubjectO(String subjectO) {
        this.subjectO = subjectO;
    }

    public String getSubjectOu() {
        return subjectOu;
    }

    public void setSubjectOu(String subjectOu) {
        this.subjectOu = subjectOu;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsInUse() {
        return isInUse;
    }

    public void setIsInUse(Integer isInUse) {
        this.isInUse = isInUse;
    }

    public Integer getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Integer isExpired) {
        this.isExpired = isExpired;
    }

    public LocalDateTime getAppliedTime() {
        return appliedTime;
    }

    public void setAppliedTime(LocalDateTime appliedTime) {
        this.appliedTime = appliedTime;
    }

    public String getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(String createPersonId) {
        this.createPersonId = createPersonId;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUpdatePersonId() {
        return updatePersonId;
    }

    public void setUpdatePersonId(String updatePersonId) {
        this.updatePersonId = updatePersonId;
    }

    public String getUpdatePersonName() {
        return updatePersonName;
    }

    public void setUpdatePersonName(String updatePersonName) {
        this.updatePersonName = updatePersonName;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getActived() {
        return actived;
    }

    public void setActived(Integer actived) {
        this.actived = actived;
    }
}