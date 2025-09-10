package com.jiuxi.monitor.client.core.util;

import cn.hutool.core.util.StrUtil;

import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Formatter;

/**
 * @ClassName: MacAddressUtil
 * @Description:
 * @Author 杨占锐
 * @Date 2024/11/14 17:47
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public class MacAddressUtil {

    /**
     * 本机mac地址缓存，避免多次获取
     */
    private static volatile String mac_addr = "";

    /**
     * 获取本机mac地址
     *
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/11/14 18:04
     */
    public static String getMacAddr() {

        if (StrUtil.isNotBlank(mac_addr)) {
            return mac_addr;
        }

        String result = "";

        try {

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac == null) {
                    continue;
                }
                StringBuilder sb = new StringBuilder();
                Formatter formatter = new Formatter(sb);
                for (byte b : mac) {
                    formatter.format("%02X:", b);
                }
                String macAddress = sb.toString();
                if (macAddress.length() > 0) {
                    result += macAddress;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StrUtil.isBlank(result)) {
            // 默认值
            result = "default:";
        }
        if (result.endsWith(":")) {
            result = result.substring(0, result.length() -1);
        }
        // 添加到缓存
        mac_addr = result;
        return result;
    }
}
