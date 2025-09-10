package com.jiuxi.core.autoconfig;

import cn.hutool.json.JSONUtil;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.core.bean.Threadpool;
import com.jiuxi.core.core.customizer.TopinfoTomcatConnectorCustomizer;
import com.jiuxi.core.core.filter.XssFilter;
import com.jiuxi.core.core.pool.TopinfoGlobalThreadPool;
import com.jiuxi.core.core.runner.CoreCommandLineRunner;
import com.jiuxi.core.core.service.RateLimiterCacheService;
import com.jiuxi.core.core.service.impl.RateLimiterCacheServiceImpl;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: SecurityAutoConfiguration
 * @Description: 认证及鉴权自动配置
 * @Author: 杨攀
 * @Date: 2020/5/22 18:33
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
@Configuration
@EnableConfigurationProperties({CoreConfigurationProperties.class})
@Import(cn.hutool.extra.spring.SpringUtil.class)
@EnableAsync
@EnableCaching
@AutoConfigureBefore(TaskExecutionAutoConfiguration.class)
@ComponentScan({"com.jiuxi.core.core.aop","com.jiuxi.core.core.controller"})
public class CoreAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreAutoConfiguration.class);

    @Autowired
    private CoreConfigurationProperties properties;

    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        boolean enable = true;
        if (null != properties.getXss() && !properties.getXss().getEnable()) {
            enable = false;
        }
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new XssFilter(properties));
        // 拦截所有
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("XssFilter");
        registrationBean.setOrder(1);
        registrationBean.setEnabled(enable);
        return registrationBean;
    }

    /**
     * 核心线程数：cpu * 2
     * 最大线程数：默认3000；线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
     * 缓冲队列：默认200；用来缓冲执行任务的队列
     * 允许线程的空闲时间60秒：超过了核心线程数之外的线程，在空闲时间到达之后会被销毁
     * 线程池对拒绝任务的处理策略：使用默认策略
     * 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
     * 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
     */
    @Bean
    public TopinfoGlobalThreadPool globalThreadPool() {
        return new TopinfoGlobalThreadPool(properties);
    }

    /**
     * spring 原生线程池配置
     * <p>
     * 核心线程数10：线程池创建时初始化的线程数
     * 最大线程数20：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
     * 缓冲队列200：用来缓冲执行任务的队列
     * 允许线程的空闲时间60秒：超过了核心线程数之外的线程，在空闲时间到达之后会被销毁
     * 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
     * 线程池对拒绝任务的处理策略：此处采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，该策略会直接在execute方法的调用线程中运行被拒绝的任务；如果执行程序已被关闭，则会丢弃该任务
     * 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
     * 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
     */
    @Bean(AsyncAnnotationBeanPostProcessor.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    public ThreadPoolTaskExecutor taskExecutor() {
        Threadpool threadpool = properties.getThreadpool();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小
        executor.setCorePoolSize(threadpool.getCorePoolSize());
        // 最大线程数
        executor.setMaxPoolSize(threadpool.getMaxPoolSize());
        // 队列容量
        executor.setQueueCapacity(threadpool.getCapacity());
        // 活跃时间
        executor.setKeepAliveSeconds(threadpool.getKeepAliveTime());
        // 线程名字前缀
        executor.setThreadNamePrefix("Topinfo-Async-");
        // 拒绝策略
        /*executor.setRejectedExecutionHandler((r, executor1) -> {
            if (!executor1.isShutdown()) {
                try {
                    LOGGER.warn("topinfo-executor-队列阻塞,请排查问题或调整连接池的参数...");
                    executor1.getQueue().put(r);
                } catch (InterruptedException e) {
                    LOGGER.error("拒绝策略报错！{}", ExceptionUtils.getStackTrace(e));
                }
            }
        });*/
        // 拒绝策略
        executor.setRejectedExecutionHandler((r, e) -> {
            // 相当于重写了 ThreadPoolExecutor.CallerRunsPolicy() 方法，添加了日志打印，方便排查问题
            if (!e.isShutdown()) {
                LOGGER.error("Topinfo-Async-队列已经满了,请排查程序问题或调整连接池的参数...");
                r.run();
            }
        });
        // 调度器shutdown被调用时等待当前被调度的任务完成
        // executor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时长
        // executor.setAwaitTerminationSeconds(60);
        return executor;
    }

    /**
     * 定时任务线程池配置
     *
     * @param taskSchedulingProperties
     * @param taskSchedulerCustomizers
     * @return org.springframework.boot.task.TaskSchedulerBuilder
     * @author pand
     * @date 2021-08-24 10:46
     */
    @Bean
    public TaskSchedulerBuilder taskSchedulerBuilder(TaskSchedulingProperties taskSchedulingProperties,
                                                     ObjectProvider<TaskSchedulerCustomizer> taskSchedulerCustomizers) {
        Threadpool threadpool = properties.getThreadpool();
        TaskSchedulerBuilder builder = new TaskSchedulerBuilder();
        builder = builder.poolSize(threadpool.getSchedulerCorePoolSize());
        TaskSchedulingProperties.Shutdown shutdown = taskSchedulingProperties.getShutdown();
        builder = builder.awaitTermination(shutdown.isAwaitTermination());
        builder = builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());
        builder = builder.threadNamePrefix("topinfo-scheduling-");
        builder = builder.customizers(taskSchedulerCustomizers);
        return builder;
    }

    /**
     * 各种加密密钥配置，初始化需要加载的加密工具类
     *
     * @return com.jiuxi.security.core.crypto.SecretKeyCommandLineRunner
     * @author pand
     * @date 2020-09-08 14:42
     */
    @Bean
    public CoreCommandLineRunner coreCommandLineRunner() {
        return new CoreCommandLineRunner();
    }


    /**
     * httpClientTemplate
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return createRestTemplate(factory);
    }


    private RestTemplate createRestTemplate(ClientHttpRequestFactory factory) {
        //1.实例化使用httpclient的RestTemplate
        RestTemplate restTemplate = new RestTemplate(factory);
        //2.采用RestTemplate内部的MessageConverter
        //重新设置StringHttpMessageConverter字符集，解决中文乱码问题
        modifyDefaultCharset(restTemplate);
        //3.设置错误处理器
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }

    /**
     * 创建HTTP客户端工厂
     */
    @Bean(name = "clientHttpRequestFactory")
    public ClientHttpRequestFactory clientHttpRequestFactory() {

        //1.检查配置maxTotalConnection 和 maxConnectionPerRoute
        if (properties.getHttpClientPoolConfig().getMaxTotalConnect() <= 0) {
            throw new IllegalArgumentException("invalid maxTotalConnection: " + properties.getHttpClientPoolConfig().getMaxTotalConnect());
        }
        if (properties.getHttpClientPoolConfig().getMaxConnectPerRoute() <= 0) {
            throw new IllegalArgumentException("invalid maxConnectionPerRoute: " + properties.getHttpClientPoolConfig().getMaxConnectPerRoute());
        }
        //2.创建clientHttpRequestFactory
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient());
        // 连接超时
        clientHttpRequestFactory.setConnectTimeout(properties.getHttpClientPoolConfig().getConnectTimeout());
        // 数据读取超时时间，即SocketTimeout
        clientHttpRequestFactory.setReadTimeout(properties.getHttpClientPoolConfig().getReadTimeout());
        // 从连接池获取请求连接的超时时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
        clientHttpRequestFactory.setConnectionRequestTimeout(properties.getHttpClientPoolConfig().getConnectionRequestTimout());
        return clientHttpRequestFactory;
    }


    /**
     * 配置httpClient
     *
     * @return
     */
    @Bean
    public CloseableHttpClient httpClient() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        try {
            //设置信任ssl访问
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();
            httpClientBuilder.setSSLContext(sslContext);
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    // 注册http和https请求
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslConnectionSocketFactory).build();
            //使用Httpclient连接池的方式配置(推荐)，同时支持netty，okHttp以及其他http框架
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // 最大连接数
            poolingHttpClientConnectionManager.setMaxTotal(properties.getHttpClientPoolConfig().getMaxTotalConnect());
            // 同路由并发数
            poolingHttpClientConnectionManager.setDefaultMaxPerRoute(properties.getHttpClientPoolConfig().getMaxConnectPerRoute());
            //配置连接池
            httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
            // 重试次数
            httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(properties.getHttpClientPoolConfig().getRetryTimes(), true));

            //设置默认请求头
            List<Header> headers = getDefaultHeaders();
            httpClientBuilder.setDefaultHeaders(headers);
            //设置长连接保持策略
            httpClientBuilder.setKeepAliveStrategy(connectionKeepAliveStrategy());

            //设置后台线程剔除失效连接
            httpClientBuilder.evictExpiredConnections();
            httpClientBuilder.evictIdleConnections(properties.getHttpClientPoolConfig().getMaxIdleTime(), TimeUnit.MILLISECONDS);

            // 启用CloseableHttpClient的清理线程
            return httpClientBuilder.build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            LOGGER.error("初始化HTTP连接池出错", e);
        }
        return null;
    }


    /**
     * 配置长连接保持策略
     *
     * @return
     */
    private ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return (response, context) -> {
            // Honor 'keep-alive' header
            HeaderElementIterator it = new BasicHeaderElementIterator(
                    response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                LOGGER.info("HeaderElement:{}", JSONUtil.toJsonStr(he));
                String param = he.getName();
                String value = he.getValue();
                if (value != null && "timeout".equalsIgnoreCase(param)) {
                    try {
                        return Long.parseLong(value) * 1000;
                    } catch (NumberFormatException ignore) {
                        LOGGER.error("解析长连接过期时间异常", ignore);
                    }
                }
            }

            HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
            Map<String, Integer> map = Optional.ofNullable(properties.getHttpClientPoolConfig().getKeepAliveTargetHost()).orElseGet(HashMap::new);
            Optional<Map.Entry<String, Integer>> any = map.entrySet().stream().filter(ent -> ent.getKey().equalsIgnoreCase(target.getHostName())).findAny();
            return any.map(en -> en.getValue() * 1000L).orElse(3 * 1000L);
        };
    }

    /**
     * 设置请求头
     *
     * @return
     */
    private List<Header> getDefaultHeaders() {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        headers.add(new BasicHeader("Accept-Language", "zh-CN"));
        headers.add(new BasicHeader("Connection", "Keep-Alive"));
        return headers;
    }

    /**
     * 修改默认的字符集类型为utf-8
     *
     * @param restTemplate
     */
    private void modifyDefaultCharset(RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        HttpMessageConverter<?> converterTarget = null;
        for (HttpMessageConverter<?> item : converterList) {
            if (StringHttpMessageConverter.class == item.getClass()) {
                converterTarget = item;
                break;
            }
        }
        if (null != converterTarget) {
            converterList.remove(converterTarget);
        }
        Charset defaultCharset = Charset.forName(properties.getHttpClientPoolConfig().getCharset());
        converterList.add(1, new StringHttpMessageConverter(defaultCharset));
    }

    /**
     * 1、解决内嵌的Tomcat，在get路径中添加特殊字符参数导致请求访问失败的问题，错误信息：IllegalArgumentException: Invalid character found in the request target. The valid characters are defined in RFC 7230 and RFC 3986
     * 2、打包后，在tomcat中运行的时候，需要在tomcat中添加配置：relaxedPathChars 和 relaxedQueryChars
     * 如： <Connector port="8084" protocol="HTTP/1.1" relaxedPathChars="[]|" relaxedQueryChars="[]|{}^&#x5c;&#x60;&quot;&lt;&gt;" useBodyEncodingForURI="true" connectionTimeout="20000" redirectPort="8443" URIEncoding="UTF-8"/>
     *
     * @param
     * @return com.jiuxi.core.core.customizer.TopinfoTomcatConnectorCustomizer
     * @author 杨攀
     * @date 2021/11/4 13:01
     */
    @Bean
    public TopinfoTomcatConnectorCustomizer topinfoTomcatConnectorCustomizer() {
        return new TopinfoTomcatConnectorCustomizer();
    }


    /**
     * 1、限流缓存实现
     */
    @Bean
    public RateLimiterCacheService rateLimiterCacheService() {
        return new RateLimiterCacheServiceImpl();
    }

}

