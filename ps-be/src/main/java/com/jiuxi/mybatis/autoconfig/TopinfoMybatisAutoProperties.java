package com.jiuxi.mybatis.autoconfig;

import com.jiuxi.mybatis.bean.DataSourceConfig;
import com.jiuxi.mybatis.bean.Dynamic;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "topinfo.mybatis")
public class TopinfoMybatisAutoProperties {

    /** 机房 id 取值 (0~31)   */
    private long datacenterId;


    /** 机器 id 取值 (0~31) */
    private long machineId;


    /** config 数据源 */
    private DataSourceConfig datasourceConfig;


    /** 多住户模式, 默认不是租户模式: fasle */
    private String tenant = "false";

    /** 动态数据源 */
    private Dynamic dynamic;

    /** 忽略不需要添加租户条件的表 */
    private List<String> ignoreTenantTables = new ArrayList<String>();
    
    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public List<String> getIgnoreTenantTables() {
        return ignoreTenantTables;
    }

    public void setIgnoreTenantTables(List<String> ignoreTenantTables) {
        this.ignoreTenantTables = ignoreTenantTables;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
    }

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public DataSourceConfig getDatasourceConfig() {
        return datasourceConfig;
    }

    public void setDatasourceConfig(DataSourceConfig datasourceConfig) {
        this.datasourceConfig = datasourceConfig;
    }

    public Dynamic getDynamic() {
        return dynamic;
    }

    public void setDynamic(Dynamic dynamic) {
        this.dynamic = dynamic;
    }
}
