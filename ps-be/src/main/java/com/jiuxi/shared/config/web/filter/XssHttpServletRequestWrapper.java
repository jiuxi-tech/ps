package com.jiuxi.shared.config.web.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ypp
 * @description: XSS过滤处理
 * @date 2020/7/20 15:15
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 没被包装过的HttpServletRequest（特殊场景，需要自己过滤）
     */
    HttpServletRequest orgRequest;

    HtmlFilter htmlFilter;

    public XssHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        htmlFilter = HtmlFilter.create();
    }

    /**
     * 重写 getParameter 方法，将参数名与参数进行 xss 过滤
     * @author Ypp
     * @date 2022/5/25 20:37
     * @param name
     * @return java.lang.String
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    /**
     * 重写 getParameterValues 方法，将参数名与参数进行 xss 过滤
     * @author Ypp
     * @date 2022/5/25 20:37
     * @param name
     * @return java.lang.String[]
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (parameters == null || parameters.length == 0) {
            return null;
        }

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = xssEncode(parameters[i]);
        }
        return parameters;
    }

    /**
     * 重写 getParameterMap 方法，将参数名与参数进行 xss 过滤
     * @author Ypp
     * @date 2022/5/25 20:38
     * @param
     * @return java.util.Map<java.lang.String,java.lang.String[]>
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new LinkedHashMap<>();
        Map<String, String[]> parameters = super.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            for (int i = 0; i < values.length; i++) {
                values[i] = xssEncode(values[i]);
            }
            map.put(key, values);
        }
        return map;
    }

    /**
     * 重写 getHeader 方法，将参数名与参数进行 xss 过滤
     * @author Ypp
     * @date 2022/5/25 20:38
     * @param name
     * @return java.lang.String
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    private String xssEncode(String input) {
        return htmlFilter.clean(input);
    }

    /**
     * 获取最原始的request
     * @author Ypp
     * @date 2022/5/25 20:39
     * @param
     * @return javax.servlet.http.HttpServletRequest
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取最原始的request
     * @author Ypp
     * @date 2022/5/25 20:39
     * @param request
     * @return javax.servlet.http.HttpServletRequest
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest request) {
        if (request instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) request).getOrgRequest();
        }
        return request;
    }

}
