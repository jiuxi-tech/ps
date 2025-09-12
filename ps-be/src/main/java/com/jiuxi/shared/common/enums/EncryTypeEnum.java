package com.jiuxi.shared.common.enums;

/**
 * 加密类型
 *
 * @author pand
 * @date 2020-09-01 19:28
 */
public enum EncryTypeEnum {
    /**
     * 国密sm2非对称加密
     */
    SM2_STYLE,

    /**
     * 国密sm3摘要加密
     */
    SM3_STYLE,

    /**
     * 国密sm4对称加密
     */
    SM4_STYLE,

    /**
     * aes加密
     */
    AES_STYLE;

}
