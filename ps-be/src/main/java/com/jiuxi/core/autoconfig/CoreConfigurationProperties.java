package com.jiuxi.core.autoconfig;

import cn.hutool.core.util.ArrayUtil;
import com.jiuxi.core.bean.HttpClientPoolConfig;
import com.jiuxi.core.bean.Secretkey;
import com.jiuxi.core.bean.Threadpool;
import com.jiuxi.core.bean.Xss;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Optional;

@ConfigurationProperties(prefix = "topinfo.core")
public class CoreConfigurationProperties {

    /**
     * xss 防御配置
     */
    private Xss xss;

    /**
     * 国密算法配置项
     */
    private Secretkey secretkey;

    /**
     * 线程池配置
     */
    private Threadpool threadpool;

    /**
     * httpClient 连接池配置
     */
    private HttpClientPoolConfig httpClientPoolConfig;

    /**
     * 默认不过滤路径
     */
    private String[] defaultExcludePath = {"/static/**", "/error/**", "/platform/**"};

    /**
     * 获取排除认证路径
     *
     * @return java.lang.String：排除认证的路径集合
     * @author pand
     * @date 2020-08-24 10:57
     */
    public String[] getXssExcludePathStr() {
        // 默认排除的请求
        // 配置文件中排除的请求
        String[] excludePath = this.getXss().getExcludePaths();
        // 合并
        excludePath = ArrayUtil.addAll(excludePath, defaultExcludePath);

        return excludePath;
    }


    public Xss getXss() {
        xss = Optional.ofNullable(xss).orElse(new Xss());
        return xss;
    }

    public void setXss(Xss xss) {
        this.xss = xss;
    }

    public Secretkey getSecretkey() {
        secretkey = Optional.ofNullable(secretkey).orElse(new Secretkey());
        return secretkey;
    }

    public void setSecretkey(Secretkey secretkey) {
        this.secretkey = secretkey;
    }

    public Threadpool getThreadpool() {
        threadpool = Optional.ofNullable(threadpool).orElse(new Threadpool());
        return threadpool;
    }

    public void setThreadpool(Threadpool threadpool) {
        this.threadpool = threadpool;
    }

    public HttpClientPoolConfig getHttpClientPoolConfig() {
        httpClientPoolConfig = Optional.ofNullable(httpClientPoolConfig).orElse(new HttpClientPoolConfig());
        return httpClientPoolConfig;
    }

    public void setHttpClientPoolConfig(HttpClientPoolConfig httpClientPoolConfig) {
        this.httpClientPoolConfig = httpClientPoolConfig;
    }
}
