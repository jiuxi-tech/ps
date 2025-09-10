package com.jiuxi.admin.autoconfig;

import com.jiuxi.admin.bean.SmsCode;
import com.jiuxi.common.bean.LogicalDisk;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Optional;

@ConfigurationProperties(prefix = "topinfo.admin")
public class AdminConfigurationProperties {

    /**
     * 认证
     */
    private SmsCode smsCode;

    /**
     * 附件存储路径逻辑盘
     */
    @Deprecated
    private Map<String, LogicalDisk> logicalDisk;

    /**
     *  第三方应用ids
     */
    private String [] thirdids;

    public SmsCode getSmsCode() {
        smsCode = Optional.ofNullable(smsCode).orElse(new SmsCode());
        return smsCode;
    }

    public void setSmsCode(SmsCode smsCode) {
        this.smsCode = smsCode;
    }

    /**
     * 方法过期，附件统一采用文件服务器存储
     * @author 杨攀
     * @date 2021/12/31 13:49
     * @param
     * @return java.util.Map<java.lang.String,com.jiuxi.common.bean.LogicalDisk>
     */
    @Deprecated
    public Map<String, LogicalDisk> getLogicalDisk() {
        return logicalDisk;
    }

    public void setLogicalDisk(Map<String, LogicalDisk> logicalDisk) {
        this.logicalDisk = logicalDisk;
    }

    public String[] getThirdids() {
        return thirdids;
    }

    public void setThirdids(String[] thirdids) {
        this.thirdids = thirdids;
    }
}
