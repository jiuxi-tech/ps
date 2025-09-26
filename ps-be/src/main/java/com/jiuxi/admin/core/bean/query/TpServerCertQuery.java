package com.jiuxi.admin.core.bean.query;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 服务器证书查询对象
 *
 * @author 系统生成
 * @since 2025-09-25
 */
public class TpServerCertQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分页页码
     */
    private Long current = 1L;

    /**
     * 分页大小
     */
    private Long size = 10L;

    /**
     * 证书名称（模糊查询）
     */
    private String certName;

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
     * 创建开始时间
     */
    private LocalDateTime createTimeStart;

    /**
     * 创建结束时间
     */
    private LocalDateTime createTimeEnd;

    /**
     * 到期开始时间
     */
    private LocalDateTime expireDateStart;

    /**
     * 到期结束时间
     */
    private LocalDateTime expireDateEnd;

    /**
     * 创建人姓名（模糊查询）
     */
    private String createPersonName;

    /**
     * 发证机构（模糊查询）
     */
    private String issuer;

    /**
     * 公用名（模糊查询）
     */
    private String subjectCn;

    // Getter and Setter methods

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
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

    public LocalDateTime getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(LocalDateTime createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public LocalDateTime getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(LocalDateTime createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public LocalDateTime getExpireDateStart() {
        return expireDateStart;
    }

    public void setExpireDateStart(LocalDateTime expireDateStart) {
        this.expireDateStart = expireDateStart;
    }

    public LocalDateTime getExpireDateEnd() {
        return expireDateEnd;
    }

    public void setExpireDateEnd(LocalDateTime expireDateEnd) {
        this.expireDateEnd = expireDateEnd;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
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
}