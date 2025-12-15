package com.jiuxi.common.util;

import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @ClassName: EncryptUtil
 * @Description: 加密工具类
 * @Author: Qdd
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public class EncryptUtil {
    
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String DEFAULT_AES_KEY = "MySecretKey12345";
    
    /**
     * MD5加密
     */
    public static String md5(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(text.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * SHA-256加密
     */
    public static String sha256(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("SHA-256加密失败", e);
        }
    }
    
    /**
     * SHA-512加密
     */
    public static String sha512(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("SHA-512加密失败", e);
        }
    }
    
    /**
     * 字节数组转十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    
    /**
     * 生成AES密钥
     */
    public static String generateAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("生成AES密钥失败", e);
        }
    }
    
    /**
     * AES加密（使用默认密钥）
     */
    public static String aesEncrypt(String text) {
        return aesEncrypt(text, DEFAULT_AES_KEY);
    }
    
    /**
     * AES加密
     */
    public static String aesEncrypt(String text, String key) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }
    
    /**
     * AES解密（使用默认密钥）
     */
    public static String aesDecrypt(String encryptedText) {
        return aesDecrypt(encryptedText, DEFAULT_AES_KEY);
    }
    
    /**
     * AES解密
     */
    public static String aesDecrypt(String encryptedText, String key) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return null;
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }
    
    /**
     * Base64编码
     */
    public static String base64Encode(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Base64解码
     */
    public static String base64Decode(String encodedText) {
        if (encodedText == null || encodedText.isEmpty()) {
            return null;
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(encodedText);
            return new String(decoded, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Base64解码失败", e);
        }
    }
    
    /**
     * 生成随机盐值
     */
    public static String generateSalt() {
        return generateSalt(16);
    }
    
    /**
     * 生成指定长度的随机盐值
     */
    public static String generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * 密码加盐哈希
     */
    public static String hashWithSalt(String password, String salt) {
        if (password == null || salt == null) {
            return null;
        }
        return sha256(password + salt);
    }
    
    /**
     * 验证密码
     */
    public static boolean verifyPassword(String password, String salt, String hashedPassword) {
        if (password == null || salt == null || hashedPassword == null) {
            return false;
        }
        String hash = hashWithSalt(password, salt);
        return hashedPassword.equals(hash);
    }
    
    /**
     * 简单密码强度检查
     * @deprecated 请使用 {@link com.jiuxi.security.core.service.PasswordValidationService#getPasswordStrengthLevel(String)}
     *             该方法使用硬编码的强度标准，建议迁移到配置化的验证服务
     */
    @Deprecated
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
    
    /**
     * 获取密码强度等级
     * @return 1-弱, 2-中, 3-强
     * @deprecated 请使用 {@link com.jiuxi.security.core.service.PasswordValidationService#getPasswordStrengthLevel(String)}
     *             该方法使用硬编码的强度标准，建议迁移到配置化的验证服务
     */
    @Deprecated
    public static int getPasswordStrength(String password) {
        if (password == null || password.length() < 6) {
            return 1;
        }
        
        int score = 0;
        
        // 长度检查
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;
        
        // 字符类型检查
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':,.<>?].*");
        
        if (hasUpper) score++;
        if (hasLower) score++;
        if (hasDigit) score++;
        if (hasSpecial) score++;
        
        if (score <= 2) return 1; // 弱
        if (score <= 4) return 2; // 中
        return 3; // 强
    }
}