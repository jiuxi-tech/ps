package com.jiuxi.mybatis.bean;

import java.util.HashMap;

/**
 * @ClassName: Dynamic
 * @Description: 动态数据源配置
 * @Author: Ypp
 * @Date: 2022/2/22 14:37
 * @Copyright: 2022 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class Dynamic {

    /** 是否启用，默认： false ， 不启用 */
    private boolean enable;

    /** 默认数据源  */
    private String defaultSource;

    /** 多数据源的配置集合 */
    private HashMap<String, DataSourceConfig>  datasourceConfigs = new HashMap<>();


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public HashMap<String, DataSourceConfig> getDatasourceConfigs() {
        return datasourceConfigs;
    }

    public void setDatasourceConfigs(HashMap<String, DataSourceConfig> datasourceConfigs) {
        this.datasourceConfigs = datasourceConfigs;
    }

    public String getDefaultSource() {
        return defaultSource;
    }

    public void setDefaultSource(String defaultSource) {
        this.defaultSource = defaultSource;
    }
}
