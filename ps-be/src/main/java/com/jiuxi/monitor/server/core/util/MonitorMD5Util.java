package com.jiuxi.monitor.server.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: MonitorMD5Util
 * @Description:
 * @Author 杨占锐
 * @Date 2024/11/18 9:58
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public class MonitorMD5Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorMD5Util.class);

    /**
     * 生成md5
     *
     * @param str
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/11/18 10:02
     */
    public static String createMd5(String str) {

        try {

            // 创建MD5加密实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 执行加密操作
            byte[] messageDigest = md.digest(str.getBytes());

            // 将得到的散列值转换为十六进制
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }

            // 返回MD5散列值
            return sb.toString();

        } catch (Exception e) {
            LOGGER.error("生成md5失败，str: {}", str);
            return "";
        }
    }

}
