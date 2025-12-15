package com.jiuxi.mybatis.bean;

/**
 * @ClassName: TpLicenceVO
 * @Description: 许可证信息
 * @Author: pdd
 * @Date: 2023/10/18 13:59
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class SecurityLicenceVO {

    /**
     * 系统序列号
     */
    private String licenceId;

    /**
     * 失效日期,yyyyMMdd
     */
    private String expiringDate;

    /**
     * 系统名称(如: 广东iot系统,山东粉尘系统)
     */
    private String systemName;

    /**
     * sm3加密的hashCode值
     */
    private String hashCode;

    /**
     * 扩展字段01: extend01,2025.01.03增加绑定mac地址,这里存储根据mac地址生成的序列号.
     */
    private String systemSerialNumber;

    /**
     * 许可证里的序列号
     */
    private String sysSerialNumber;

    public String getLicenceId() {
        return licenceId;
    }

    public void setLicenceId(String licenceId) {
        this.licenceId = licenceId;
    }

    public String getExpiringDate() {
        return expiringDate;
    }

    public void setExpiringDate(String expiringDate) {
        this.expiringDate = expiringDate;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getSystemSerialNumber() {
        return systemSerialNumber;
    }

    public void setSystemSerialNumber(String systemSerialNumber) {
        this.systemSerialNumber = systemSerialNumber;
    }

    public String getSysSerialNumber() {
        return sysSerialNumber;
    }

    public void setSysSerialNumber(String sysSerialNumber) {
        this.sysSerialNumber = sysSerialNumber;
    }
}
