package com.jiuxi.admin.autoconfig;

import com.jiuxi.admin.core.listener.TpAccountListener;
import com.jiuxi.admin.core.listener.TpCityListener;
import com.jiuxi.admin.core.listener.TpDeptBasicinfoListener;
import com.jiuxi.admin.core.listener.TpDeptBasicinfoNewListener;
import com.jiuxi.admin.core.listener.TpEntBasicinfoEventCollection;
import com.jiuxi.admin.core.listener.TpEntBasicinfoListener;
import com.jiuxi.admin.core.listener.TpPersonBasicinfoListener;
import com.jiuxi.admin.core.runner.AdminCommanLineRunner;
import com.jiuxi.admin.core.service.AdminLicenceCacheService;
import com.jiuxi.admin.core.service.FileExpService;
import com.jiuxi.admin.core.service.PersonAccountService;
import com.jiuxi.admin.core.service.TpSmsSendService;
import com.jiuxi.admin.core.service.impl.*;
import com.jiuxi.admin.core.util.CommonDataPermissionsUtil;
// import com.jiuxi.easyexcel.service.DownloadDataService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @ClassName: AdminAutoConfiguration
 * @Description: 自动配置
 * @Author: 杨攀
 * @Date: 2020/11/18 14:25
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
@Configuration
@EnableConfigurationProperties({AdminConfigurationProperties.class})
@Import(CommonDataPermissionsUtil.class)
@MapperScan(basePackages = {"com.jiuxi.admin.core.mapper"})
@ComponentScan({"com.jiuxi.admin.core.controller", "com.jiuxi.admin.core.service"})
public class AdminAutoConfiguration {
    /**
     * @param
     * @return com.jiuxi.admin.core.service.TpSmsSendService
     * @description: 发送验证码，存到mysql的通用方法
     * @author 杨攀
     * @date 2020/5/26 10:37
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "topinfo.security.authorization", name = "authorizationMode", havingValue = "DB", matchIfMissing = false)
    public TpSmsSendService tpDBSmsSendService() {
        return new TpDbVerificationCodeServiceImpl();
    }


    /**
     * 各种加密密钥配置，初始化需要加载的加密工具类
     *
     * @return com.jiuxi.security.core.crypto.SecretKeyCommandLineRunner
     * @author pand
     * @date 2020-09-08 14:42
     */
    @Bean
    public AdminCommanLineRunner adminCommanLineRunner() {
        return new AdminCommanLineRunner();
    }

    @ConditionalOnMissingBean
    @Bean("personAccountService")
    public PersonAccountService PersonAccountService() {
        return new PersonAccountServiceImpl();
    }

    /**
     * excel默认导出实现
     *
     * @param
     * @return com.jiuxi.easyexcel.service.DownloadDataService
     * @author pand
     * @date   2022-05-24 11:27
     */
    // @ConditionalOnMissingBean
    // @Bean("downloadDataService")
    // public DownloadDataService downloadDataService() {
    //     return new DownloadDataServiceImpl();
    // }

    /**
     * 人员基本信息同步监听事件实例化
     *
     * @return com.jiuxi.admin.core.listener.TpPersonBasicinfoListener
     * @author pand
     * @date 2021-06-08 13:50
     */
    @Bean
    public TpPersonBasicinfoListener tpPersonBasicinfoListener() {
        return new TpPersonBasicinfoListener();
    }

    /**
     * 账号基本信息同步监听事件实例化
     *
     * @return com.jiuxi.admin.core.listener.TpPersonBasicinfoListener
     * @author pand
     * @date 2021-06-08 13:50
     */
    @Bean
    public TpAccountListener tpAccountListener() {
        return new TpAccountListener();
    }

    /**
     * 企业基本信息同步监听事件实例化
     *
     * @return com.jiuxi.admin.core.listener.TpEntBasicinfoListener
     * @author pand
     * @date 2021-06-08 13:50
     */
    @Bean
    public TpEntBasicinfoListener tpEntBasicinfoListener() {
        return new TpEntBasicinfoListener();
    }

    /**
     * 行政区划基本信息同步监听事件实例化
     *
     * @return com.jiuxi.admin.core.listener.TpCityListener
     * @author pand
     * @date 2021-06-08 13:50
     */
    @Bean
    public TpCityListener tpCityListener() {
        return new TpCityListener();
    }

    /**
     * 部门基本信息同步监听事件实例化
     *
     * @return com.jiuxi.admin.core.listener.TpCityListener
     * @author pand
     * @date 2021-06-08 13:50
     */
    @Bean
    @Deprecated
    public TpDeptBasicinfoListener tpDeptBasicinfoListener() {
        return new TpDeptBasicinfoListener();
    }

    /**
     * 部门基本信息同步监听事件实例化 (新)
     *
     * @return com.jiuxi.admin.core.listener.TpCityListener
     * @author pand
     * @date 2021-06-08 13:50
     */
    @Bean
    public TpDeptBasicinfoNewListener tpDeptBasicinfoNewListener() {
        return new TpDeptBasicinfoNewListener();
    }


    /**
     * 企业事件集合
     *
     * @return com.jiuxi.admin.core.listener.TpCityListener
     * @author pand
     * @date 2021-06-08 13:50
     */
    @Bean
    public TpEntBasicinfoEventCollection tpEntBasicinfoEventCollection() {
        return new TpEntBasicinfoEventCollection();
    }

    /**
     * 上传附件扩展服务
     *
     * @return com.jiuxi.admin.core.listener.TpCityListener
     * @author pand
     * @date 2021-06-08 13:50
     */
    @Bean
    @ConditionalOnMissingBean
    public FileExpService fileExpService() {
        return new FileExpServiceImpl();
    }

    /**
     * 许可证
     *
     * @return com.jiuxi.security.core.service.TopinfoSecurityLogoutService
     * @date 2024/10/8 14:00
     */
    @Bean
    public AdminLicenceCacheService adminLicenceCacheService() {
        return new AdminLicenceCacheServiceImpl();
    }

}
