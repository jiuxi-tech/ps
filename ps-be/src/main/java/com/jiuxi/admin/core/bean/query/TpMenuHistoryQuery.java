package com.jiuxi.admin.core.bean.query;

/**
 * @ClassName: TpMenuHistoryQuery
 * @Description: 菜单历史查询条件
 * @Author: Qdd
 * @Date: 2025-08-30
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
public class TpMenuHistoryQuery {

    /**
     * 菜单ID
     */
    private String menuId;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    // 兼容方法
    public Integer getPageNum() {
        return current != null ? current : 1;
    }

    public Integer getPageSize() {
        return size != null ? size : 10;
    }

    @Override
    public String toString() {
        return "TpMenuHistoryQuery{" +
                "menuId='" + menuId + '\'' +
                ", operationType='" + operationType + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", current=" + current +
                ", size=" + size +
                '}';
    }
}