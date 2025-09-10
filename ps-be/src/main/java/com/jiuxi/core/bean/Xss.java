package com.jiuxi.core.bean;

/**
 * @Description: xss防御配置项
 * @ClassName: Xss
 * @Author: pand
 * @Date: 2020-09-09 17:39
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class Xss {

    private Boolean enable = true;

    private String[] excludePaths = {"/sys/c-json/**"};

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String[] getExcludePaths() {
        return excludePaths;
    }

    public void setExcludePaths(String[] excludePaths) {
        this.excludePaths = excludePaths;
    }
}
