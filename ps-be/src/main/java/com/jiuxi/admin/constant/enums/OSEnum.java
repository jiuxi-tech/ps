package com.jiuxi.admin.constant.enums;

/**
 * @Description: 操作系统类型
 * @ClassName: OSEnum
 * @Author: pdd
 * @Date: 2020-10-14 14:53
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public enum OSEnum {

    ANY("any"),
    LINUX("linux"),
    MAC_OS("mac os"),
    MAC_OS_X("mac os x"),
    WINDOWS("windows"),
    OS2("os/2"),
    SOLARIS("solaris"),
    SUNOS("sunos"),
    MPEIX("mpe/ix"),
    HP_UX("hp-ux"),
    AIX("aix"),
    OS390("os/390"),
    FREEBSD("freebsd"),
    IRIX("irix"),
    DIGITAL_UNIX("digital unix"),
    NETWARE_411("netware"),
    OSF1("osf1"),
    OPENVMS("openvms"),
    OTHERS("others");

    OSEnum(String osName) {
        this.osName = osName;
    }

    private String osName;
    public String getOsName() {
        return osName;
    }
    public void setOsName(String osName) {
        this.osName = osName;
    }
}
