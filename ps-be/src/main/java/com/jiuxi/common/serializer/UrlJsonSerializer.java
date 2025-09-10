package com.jiuxi.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * @Description: URL解码
 * @ClassName: UrlJsonSerializer
 * @Author: pand
 * @Date: 2021-09-18 11:21
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class UrlJsonSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(URLDecoder.decode(value, "UTF-8"));
    }
}
