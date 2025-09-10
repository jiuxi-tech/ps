package com.jiuxi.core.core.filter;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName: InputStreamHttpServletRequestWrapper
 * @Description:  输入流 HttpServletRequestWrapper 包装类，
 * @Author: 杨攀
 * @Date: 2023/11/1 10:27
 * @Copyright: 2023 www.tuxun.net Inc. All rights reserved.
 *
 * 因为ServletInputStream被读取后无法第二次再读取了，所以要把读取过的内容存下来，
 * 然后需要的时候对外提供可被重复读取的ByteArrayInputStream，所以重写 ServletInputStream 的 getInputStream()方法
 *
 */
public class InputStreamHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 缓存 HTTP body
     */
    private byte[] body;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public InputStreamHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = StreamUtils.copyToByteArray(request.getInputStream());
    }

    /**
     * 重新 getInputStream
     * @author 杨攀
     * @date 2023/11/1 10:30
     * @param
     * @return javax.servlet.ServletInputStream
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {

        // 创建字节数组输入流
        InputStream bodyStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return bodyStream.read();
            }
        };
    }
}
