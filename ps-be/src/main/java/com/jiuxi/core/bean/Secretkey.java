package com.jiuxi.core.bean;

/**
 * @Description: 国密加密算法配置项
 * @ClassName: Sm
 * @Author: pdd
 * @Date: 2020-09-07 17:26
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class Secretkey {

    /**
     * sm2生成私钥
     */
    private String sm2_private_key = "308193020100301306072a8648ce3d020106082a811ccf5501822d04793077020101042074b24bfa8a55127c0fcbe87d7a112e398fd291fd72314e1c56e8d575664a1148a00a06082a811ccf5501822da14403420004be3415fd3a7231fa23e4cfdf6f857b0f3137e75692f7b5011d459afc0cdd7741676dca32ca6489cfe0f0fd43b5e9f9f0f77c7997630ba1142c725178a9181558";

    /**
     * sm2生成公钥
     */
    private String sm2_public_key = "3059301306072a8648ce3d020106082a811ccf5501822d03420004be3415fd3a7231fa23e4cfdf6f857b0f3137e75692f7b5011d459afc0cdd7741676dca32ca6489cfe0f0fd43b5e9f9f0f77c7997630ba1142c725178a9181558";

    /**
     * 通常由于js端提供的 SM2代码实现的方案，都是直接使用的私钥的d值和公钥的q值直接进行的加解密，所以后端返回的是从公钥里面提取的q值，以q值做为js端的加密公钥
     * 私钥的d值
     */
    private String sm2_private_dkey = "74b24bfa8a55127c0fcbe87d7a112e398fd291fd72314e1c56e8d575664a1148";

    /**
     * 通常由于js端提供的 SM2代码实现的方案，都是直接使用的私钥的d值和公钥的q值直接进行的加解密，所以后端返回的是从公钥里面提取的q值，以q值做为js端的加密公钥
     * 公钥的q值
     */
    private String sm2_public_qkey = "04be3415fd3a7231fa23e4cfdf6f857b0f3137e75692f7b5011d459afc0cdd7741676dca32ca6489cfe0f0fd43b5e9f9f0f77c7997630ba1142c725178a9181558";

    /**
     * 生成sm3需要的盐
     */
    private String sm3_salt = "Q51h6XuN0kdEhAF+";

    /**
     * 生成sm4需要的key
     */
    private String sm4_secretKey = "4OEn4idkYQ51h6Xu";
    /**
     * 生成sm4需要的盐
     */
    private String sm4_secretIv = "ntf3R4YlTIJH4MsV";

    /**
     * 生成sm4需要的key
     */
    private String aes_secretKey;
    /**
     * 生成sm4需要的盐
     */
    private String aes_secretIv;

    public String getSm2_public_key() {
        return sm2_public_key;
    }

    public void setSm2_public_key(String sm2_public_key) {
        this.sm2_public_key = sm2_public_key;
    }

    public String getSm2_private_key() {
        return sm2_private_key;
    }

    public void setSm2_private_key(String sm2_private_key) {
        this.sm2_private_key = sm2_private_key;
    }

    public String getSm2_private_dkey() {
        return sm2_private_dkey;
    }

    public void setSm2_private_dkey(String sm2_private_dkey) {
        this.sm2_private_dkey = sm2_private_dkey;
    }

    public String getSm2_public_qkey() {
        return sm2_public_qkey;
    }

    public void setSm2_public_qkey(String sm2_public_qkey) {
        this.sm2_public_qkey = sm2_public_qkey;
    }

    public String getSm3_salt() {
        return sm3_salt;
    }

    public void setSm3_salt(String sm3_salt) {
        this.sm3_salt = sm3_salt;
    }

    public String getSm4_secretKey() {
        return sm4_secretKey;
    }

    public void setSm4_secretKey(String sm4_secretKey) {
        this.sm4_secretKey = sm4_secretKey;
    }

    public String getSm4_secretIv() {
        return sm4_secretIv;
    }

    public void setSm4_secretIv(String sm4_secretIv) {
        this.sm4_secretIv = sm4_secretIv;
    }

    public String getAes_secretKey() {
        return aes_secretKey;
    }

    public void setAes_secretKey(String aes_secretKey) {
        this.aes_secretKey = aes_secretKey;
    }

    public String getAes_secretIv() {
        return aes_secretIv;
    }

    public void setAes_secretIv(String aes_secretIv) {
        this.aes_secretIv = aes_secretIv;
    }
}
