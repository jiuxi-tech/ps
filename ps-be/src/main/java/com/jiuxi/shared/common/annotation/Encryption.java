package com.jiuxi.shared.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jiuxi.shared.common.enums.EncryTypeEnum;
import com.jiuxi.shared.common.serializer.EncryptionSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 加密注解，字段加密
 * @ClassName: encryption
 * @Author: pdd
 * @Date: 2020-09-01 19:24
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = EncryptionSerialize.class)
public @interface Encryption {

    /**
     * 加解密方式：默认国密sm4对称加密，EncryTypeEnum.SM4_STYLE
     *
     * @return
     */
    EncryTypeEnum style() default EncryTypeEnum.SM4_STYLE;

}
