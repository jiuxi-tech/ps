package com.jiuxi.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: PhoneEncryptionUtils
 * @Description: 手机号加密工具类，专门用于tp_person_basicinfo表phone字段的对称加密
 * @Author: ps-bmp
 * @Date: 2024/01/20
 * @Copyright: 2024 www.tuxun.net Inc. All rights reserved.
 */
public class PhoneEncryptionUtils {

    private static final Logger logger = LoggerFactory.getLogger(PhoneEncryptionUtils.class);

    /**
     * AES加密实例
     */
    private static volatile AES aes;

    /**
     * 固定密钥，用于手机号加密（16位）
     */
    private static final String SECRET_KEY = "PhoneEncrypt2024";

    /**
     * 固定IV，用于手机号加密（16位）
     */
    private static final String SECRET_IV = "PhoneIV123456789";

    /**
     * 初始化AES实例
     */
    private static void initAES() {
        if (aes == null) {
            synchronized (PhoneEncryptionUtils.class) {
                if (aes == null) {
                    try {
                        byte[] key = SECRET_KEY.getBytes("UTF-8");
                        byte[] iv = SECRET_IV.getBytes("UTF-8");
                        aes = new AES(Mode.CBC, Padding.PKCS5Padding, key, iv);
                        logger.info("PhoneEncryptionUtils AES实例初始化成功");
                    } catch (Exception e) {
                        logger.error("PhoneEncryptionUtils AES实例初始化失败", e);
                        throw new RuntimeException("手机号加密工具初始化失败", e);
                    }
                }
            }
        }
    }

    /**
     * 加密手机号
     * 
     * @param phone 明文手机号
     * @return 加密后的十六进制字符串，如果输入为空则返回空字符串
     */
    public static String encrypt(String phone) {
        if (StrUtil.isBlank(phone)) {
            return "";
        }
        
        try {
            initAES();
            String encrypted = aes.encryptHex(phone.trim());
            logger.debug("手机号加密成功，原始长度: {}, 加密后长度: {}", phone.length(), encrypted.length());
            return encrypted;
        } catch (Exception e) {
            logger.error("手机号加密失败，phone: {}", phone, e);
            throw new RuntimeException("手机号加密失败", e);
        }
    }

    /**
     * 解密手机号
     * 
     * @param encryptedPhone 加密的十六进制字符串
     * @return 解密后的明文手机号，如果输入为空则返回空字符串
     */
    public static String decrypt(String encryptedPhone) {
        if (StrUtil.isBlank(encryptedPhone)) {
            return "";
        }
        
        try {
            initAES();
            String decrypted = aes.decryptStr(encryptedPhone.trim());
            logger.debug("手机号解密成功，加密长度: {}, 解密后长度: {}", encryptedPhone.length(), decrypted.length());
            return decrypted;
        } catch (Exception e) {
            logger.error("手机号解密失败，encryptedPhone: {}", encryptedPhone, e);
            // 解密失败时返回原值，避免系统崩溃
            logger.warn("手机号解密失败，返回原始值");
            return encryptedPhone;
        }
    }

    /**
     * 批量加密手机号
     * 
     * @param phones 明文手机号数组
     * @return 加密后的手机号数组
     */
    public static String[] encryptBatch(String[] phones) {
        if (phones == null || phones.length == 0) {
            return new String[0];
        }
        
        String[] encrypted = new String[phones.length];
        for (int i = 0; i < phones.length; i++) {
            encrypted[i] = encrypt(phones[i]);
        }
        return encrypted;
    }

    /**
     * 批量解密手机号
     * 
     * @param encryptedPhones 加密的手机号数组
     * @return 解密后的手机号数组
     */
    public static String[] decryptBatch(String[] encryptedPhones) {
        if (encryptedPhones == null || encryptedPhones.length == 0) {
            return new String[0];
        }
        
        String[] decrypted = new String[encryptedPhones.length];
        for (int i = 0; i < encryptedPhones.length; i++) {
            decrypted[i] = decrypt(encryptedPhones[i]);
        }
        return decrypted;
    }

    /**
     * 检查字符串是否为加密格式（简单判断：长度大于11且为十六进制）
     * 
     * @param phone 待检查的字符串
     * @return true表示可能是加密格式，false表示可能是明文
     */
    public static boolean isEncrypted(String phone) {
        if (StrUtil.isBlank(phone)) {
            return false;
        }
        
        // 简单判断：加密后的手机号长度通常大于11位，且为十六进制字符
        return phone.length() > 11 && phone.matches("^[0-9a-fA-F]+$");
    }

    /**
     * 安全的解密方法，自动判断是否已加密
     * 
     * @param phone 可能是明文或密文的手机号
     * @return 明文手机号
     */
    public static String safeDecrypt(String phone) {
        if (StrUtil.isBlank(phone)) {
            return "";
        }
        
        // 如果看起来像加密数据，则尝试解密
        if (isEncrypted(phone)) {
            return decrypt(phone);
        }
        
        // 否则直接返回原值
        return phone;
    }
}