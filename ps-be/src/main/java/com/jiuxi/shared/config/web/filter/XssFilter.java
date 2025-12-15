package com.jiuxi.shared.config.web.filter;

import com.jiuxi.core.autoconfig.CoreConfigurationProperties;
import com.jiuxi.common.util.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ypp
 * @description: XSS过滤
 * @date 2020/7/20 15:14
 */
public class XssFilter implements Filter {

    private CoreConfigurationProperties properties;

    public XssFilter(CoreConfigurationProperties properties) {
        this.properties = properties;
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // http置HttpOnly=true
        httpResponse.addHeader("Set-Cookie", "Path=/; HttpOnly");
        // https置HttpOnly=true
        httpResponse.addHeader("Set-Cookie", "Path=/; Secure; HttpOnly");

        HttpServletRequest httpRequest = (HttpServletRequest) request;


        String contentType = request.getContentType();
        if (StringUtils.isNotBlank(contentType) && contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            // 对上传不进行xss过滤
            chain.doFilter(request, response);
        } else if (StringUtils.isNotBlank(contentType) && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {

            // 如果是 application/json
            // 解决: request.getInputStream()只能读取一次的问题
            InputStreamHttpServletRequestWrapper inputStreamRequestWrapper = new InputStreamHttpServletRequestWrapper(httpRequest);
            chain.doFilter(inputStreamRequestWrapper, httpResponse);
        }else {

            String url = httpRequest.getServletPath();
            // 普通请求 进行xss过滤
            if (RequestUtils.checkPath(url, properties.getXssExcludePathStr())) {
                chain.doFilter(request, response);
            } else {
                XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
                chain.doFilter(xssRequest, response);
            }
        }
    }

    @Override
    public void destroy() {
    }

}
