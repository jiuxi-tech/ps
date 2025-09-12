package com.jiuxi.common.bean;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName: InputStreamRequestBody
 * @Description: 重写 writeTo, 以支持上传流的方式 InputStream
 * @Author: 杨攀
 * @Date: 2022/6/22 13:06
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class InputStreamRequestBody extends RequestBody {

    private InputStream inputStream;

    private MediaType mediaType;

    private InputStreamRequestBody(InputStream inputStream, MediaType mediaType) {
        this.inputStream = inputStream;
        this.mediaType = mediaType;
    }


    public static RequestBody create(InputStream inputStream, MediaType mediaType) {
        return new InputStreamRequestBody(inputStream, mediaType);
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mediaType;
    }

    @Override
    public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
        Source source= Okio.source(inputStream);
        bufferedSink.writeAll(source);
    }
}
