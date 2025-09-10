package com.jiuxi.core.core.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jiuxi.core.core.enums.EncryTypeEnum;
import com.jiuxi.core.core.jackson.EncryptionSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 加密注解，字段加密
 * @ClassName: encryption
 * @Author: pand
 * @Date: 2020-09-01 19:24
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
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
