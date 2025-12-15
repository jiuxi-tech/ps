package com.jiuxi.common.util;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.*;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.symmetric.SM4;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;

/**
 * @Description: 国密加密算法，hutool工具实现。
 * 1、sm2非对称加密，登陆传输账号密码使用。
 * 2、sm3摘要算法，用来加密密码
 * 3、sm4对称加密，用来加密数据传输
 * @ClassName: SmUtils
 * @Author: pdd
 * @Date: 2020-09-07 14:35
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class SmUtils {


    private static final Logger LOGGER = LoggerFactory.getLogger(SmUtils.class);


    private static String sm3_salt = "Q51h6XuN0kdEhAF+";


    private static byte[] salts_sm3 = sm3_salt.getBytes();

    /**
     * 对象
     */
    private volatile static SM2 sm2;
    private volatile static SM3 sm3;
    private volatile static SM4 sm4;


    public static SM2 newInstanceSM2(String privateKey, String publicKey) {
        if (sm2 == null) {
            synchronized (SmUtils.class) {
                if (sm2 == null) {
                    sm2 = SmUtil.sm2(privateKey, publicKey);
                    // sm2的加解密时有两种方式即 C1C2C3、 C1C3C2，
                    sm2.setMode(SM2Engine.Mode.C1C3C2);
                }
            }
        }
        return sm2;
    }

    public static SM4 newInstanceSM4(String secretKey, String secretIv) {
        if (sm4 == null) {
            synchronized (SmUtils.class) {
                if (sm4 == null) {
                    byte[] key = secretKey.getBytes();
                    byte[] vi = secretIv.getBytes();
                    sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, key, vi);
                }
            }
        }
        return sm4;
    }

    /**
     * 生成SM2公钥，私钥，公钥Q，私钥D <br/>
     * 通常由于js端提供的 SM2代码实现的方案，都是直接使用的私钥的d值和公钥的q值直接进行的加解密，
     * 所以后端返回的是从公钥里面提取的q值，以q值做为js端的加密公钥，从私钥提取d值，作为js端的解密私钥
     * @author Ypp
     * @date 2021/5/13 16:29
     * @param
     * @return void
     */
    public static void createSm2Key() {

        SM2 sm = SmUtil.sm2();

        // sm2的加解密时有两种方式即 C1C2C3、 C1C3C2，
        sm.setMode(SM2Engine.Mode.C1C3C2);

        // 生成私钥
        String privateKey = HexUtil.encodeHexStr(sm.getPrivateKey().getEncoded());
        LOGGER.info("私钥: {}", privateKey);
        // 生成公钥
        String publicKey = HexUtil.encodeHexStr(sm.getPublicKey().getEncoded());
        LOGGER.info("公钥: {}", publicKey);

        // 生成私钥D
        String privateKeyD = HexUtil.encodeHexStr(BCUtil.encodeECPrivateKey(sm.getPrivateKey())); // ((BCECPrivateKey) privateKey).getD().toByteArray();
        LOGGER.info("私钥D: {}", privateKeyD);

        // 生成公钥Q，以q值做为js端的加密公钥
        String publicKeyQ = HexUtil.encodeHexStr(((BCECPublicKey) sm.getPublicKey()).getQ().getEncoded(false));
        LOGGER.info("公钥Q: {}", publicKeyQ);
    }



    /**
     * 公钥/私钥 加密
     *
     * @param content: 需要加密的数据
     * @param keyType: KeyType.PrivateKey：私钥加密  KeyType.PublicKey：公钥加密
     * @return java.lang.String
     * @author pand
     * @date 2020-09-07 15:28
     */
    public static String encryptHexSM2(String content, KeyType keyType) {
        return sm2.encryptHex(content, keyType);
    }

    /**
     * 公钥/私钥 解密
     *
     * @param content: 需要解密的数据
     * @param keyType: KeyType.PrivateKey：私钥解密  KeyType.PublicKey：公钥解密
     * @return java.lang.String
     * @author pand
     * @date 2020-09-07 15:35
     */
    public static String decryptStrSM2(String content, KeyType keyType) {
        return sm2.decryptStr(content, keyType);
    }

    /**
     * 数据签名
     *
     * @param content: 需要签名的数据
     * @return java.lang.String
     * @author pand
     * @date 2020-09-07 15:46
     */
    public static String singnHexSM2(String content) {
        return sm2.signHex(HexUtil.encodeHexStr(content));
    }

    /**
     * 验签
     *
     * @param content: 原数据
     * @param sign:    签名数据
     * @return java.lang.String
     * @author pand
     * @date 2020-09-07 15:47
     */
    public static boolean verifyHexSM2(String content, String sign) {
        return sm2.verifyHex(HexUtil.encodeHexStr(content), sign);
    }

    /**
     * 数据摘要
     *
     * @param content: 原数据
     * @return java.lang.String
     * @author pand
     * @date 2020-09-07 15:50
     */
    public static String digestHexSM3(String content) {
        SM3 sm3 = new SM3(salts_sm3);
        return sm3.digestHex(content);
    }

    /**
     * 将明文密码跟加密后的密码进行匹配，如果一致返回true,否则返回false
     *
     * @param password       明文密码
     * @param encodePassword 密闻
     * @return
     */
    public static boolean match(String password, String encodePassword) {
        String encodePwd = digestHexSM3(password);
        return StrUtil.equals(encodePwd, encodePassword);
    }

    /**
     * 对称加密数据
     *
     * @param content: 原数据
     * @return java.lang.String
     * @author pand
     * @date 2020-09-07 15:51
     */
    public static String encryptHexSM4(String content) {
        if (StrUtil.isBlank(content)) {
            return "";
        }
        return sm4.encryptHex(content);
    }

    /**
     * 对称解密数据
     *
     * @param content: 原密文数据
     * @return java.lang.String
     * @author pand
     * @date 2020-09-07 15:53
     */
    public static String decryptStrSM4(String content) {
        if (StrUtil.isBlank(content)) {
            return "";
        }
        return sm4.decryptStr(content);
    }

}
