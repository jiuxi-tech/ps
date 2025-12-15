package com.jiuxi.shared.common.serializer;

import cn.hutool.crypto.asymmetric.KeyType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.jiuxi.common.util.AesUtils;
import com.jiuxi.common.util.SmUtils;
import com.jiuxi.shared.common.annotation.Encryption;
import com.jiuxi.shared.common.enums.EncryTypeEnum;

import java.io.IOException;
import java.util.Objects;

/**
 * @Description: 加密注解实现
 * @ClassName: EncryptionSerialize
 * @Author: pdd
 * @Date: 2020-09-01 19:37
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class EncryptionSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private EncryTypeEnum style;

    public EncryptionSerialize() {
    }

    public EncryptionSerialize(final EncryTypeEnum style) {
        this.style = style;
    }

    @Override
    public void serialize(String val, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 加密解密
        switch (style) {
            case SM4_STYLE:
                // sm4对称加密
                SmUtils.encryptHexSM4(val);
                break;
            case SM2_STYLE:
                // sm2 公钥加密加密
                SmUtils.encryptHexSM2(val, KeyType.PublicKey);
                break;
            case SM3_STYLE:
                // sm3摘要加密
                SmUtils.digestHexSM3(val);
                break;
            case AES_STYLE:
                val = AesUtils.encryptHex(val);
                break;
            default:
                // AES加密
                SmUtils.encryptHexSM4(val);
                break;
        }

        gen.writeString(val);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        // 为空直接跳过
        if (property == null) {
            return prov.findNullValueSerializer(null);
        }
        // 非 String 类直接跳过
        if (!Objects.equals(property.getType().getRawClass(), String.class)) {
            return prov.findValueSerializer(property.getType(), property);
        }
        Encryption encryption = property.getAnnotation(Encryption.class);
        if (encryption == null) {
            encryption = property.getContextAnnotation(Encryption.class);
        }
        if (encryption != null) {
            // 如果能得到注解，就将注解的 value 传入 EncryptionSerialize
            return new EncryptionSerialize(encryption.style());
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
