package com.jiuxi.admin.core.bean.query;

/**
 * @ClassName: TpSystemConfigQuery
 * @Description: 系统配置查询条件
 * @Author: Qdd
 * @Date: 2024-01-26
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
public class TpSystemConfigQuery {

    /**
     * 配置键（支持模糊查询）
     */
    private String configKey;

    /**
     * 配置值（支持模糊查询）
     */
    private String configValue;

    /**
     * 描述（支持模糊查询）
     */
    private String description;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}