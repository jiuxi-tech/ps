package com.jiuxi.core.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.jiuxi.common.util.SensitiveUtils;
import com.jiuxi.core.core.annotation.SensitiveInfo;
import com.jiuxi.core.core.enums.SensitiveType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * @Description: 字段脱敏
 * 在属性上添加 ： @JsonSerialize(using = SensitiveInfoSerialize.class)
 * @ClassName: SensitiveInfoFormatter
 * @Author: pand
 * @Date: 2020-08-28 16:34
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class SensitiveInfoSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveType type;

    private int start;

    private int end;

    public SensitiveInfoSerialize() {
    }

    public SensitiveInfoSerialize(final SensitiveType type, final int start, final int end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    @Override
    public void serialize(String val, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        // 加密
        if (StringUtils.isNotBlank(val)) {
            switch (type) {
                case CHINESE:
                    val = SensitiveUtils.chineseName(val, start, end);
                    break;
                case ID_CARD:
                    val = SensitiveUtils.idCardNum(val, start, end);
                    break;
                case FIXED_PHONE:
                    val = SensitiveUtils.fixedPhone(val, start, end);
                    break;
                case MOBILE_PHONE:
                    val = SensitiveUtils.mobilePhone(val, start, end);
                    break;
                case ADDRESS:
                    val = SensitiveUtils.address(val, start, end);
                    break;
                case EMAIL:
                    val = SensitiveUtils.email(val, start, end);
                    break;
                case BANK_CARD:
                    val = SensitiveUtils.bankCard(val, start, end);
                    break;
                default:
                    val = SensitiveUtils.other(val, start, end);
                    break;
            }
        }

        jsonGenerator.writeString(val);
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
        SensitiveInfo sensitiveInfo = property.getAnnotation(SensitiveInfo.class);
        if (sensitiveInfo == null) {
            sensitiveInfo = property.getContextAnnotation(SensitiveInfo.class);
        }
        if (sensitiveInfo != null) {
            // 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize
            return new SensitiveInfoSerialize(sensitiveInfo.type(), sensitiveInfo.start(), sensitiveInfo.end());
        }
        return prov.findValueSerializer(property.getType(), property);
    }

}
