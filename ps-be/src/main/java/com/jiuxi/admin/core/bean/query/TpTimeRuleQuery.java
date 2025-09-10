package com.jiuxi.admin.core.bean.query;

/**
 * 登录时间段控制规则查询条件
 *
 * @author system
 * @date 2025-01-20
 */
public class TpTimeRuleQuery {

    /**
     * 规则名称（模糊查询）
     */
    private String ruleName;

    /**
     * 规则状态（0：禁用，1：启用）
     */
    private Integer status;

    /**
     * 是否允许登录：1-允许，0-拒绝
     */
    private Integer allowLogin;

    /**
     * 开始时间（查询范围起始）
     */
    private String startTimeFrom;

    /**
     * 开始时间（查询范围结束）
     */
    private String startTimeTo;

    /**
     * 结束时间（查询范围起始）
     */
    private String endTimeFrom;

    /**
     * 结束时间（查询范围结束）
     */
    private String endTimeTo;

    /**
     * 创建人ID
     */
    private String creatorId;

    /**
     * 机构ID（别名，用于兼容）
     */
    private String institutionId;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;

    // Getter and Setter methods

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAllowLogin() {
        return allowLogin;
    }

    public void setAllowLogin(Integer allowLogin) {
        this.allowLogin = allowLogin;
    }

    public String getStartTimeFrom() {
        return startTimeFrom;
    }

    public void setStartTimeFrom(String startTimeFrom) {
        this.startTimeFrom = startTimeFrom;
    }

    public String getStartTimeTo() {
        return startTimeTo;
    }

    public void setStartTimeTo(String startTimeTo) {
        this.startTimeTo = startTimeTo;
    }

    public String getEndTimeFrom() {
        return endTimeFrom;
    }

    public void setEndTimeFrom(String endTimeFrom) {
        this.endTimeFrom = endTimeFrom;
    }

    public String getEndTimeTo() {
        return endTimeTo;
    }

    public void setEndTimeTo(String endTimeTo) {
        this.endTimeTo = endTimeTo;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}