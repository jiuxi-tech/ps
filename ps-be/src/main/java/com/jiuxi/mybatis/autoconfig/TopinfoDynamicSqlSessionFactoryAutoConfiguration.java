package com.jiuxi.mybatis.autoconfig;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.jiuxi.shared.infrastructure.persistence.dynamic.DynamicDataSource;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @ClassName: TopinfoDynamicSqlSessionFactoryAutoConfiguration
 * @Description: 动态数据源的 SqlSessionFactory 重写默认的 SqlSessionFactory
 * @Author: Ypp
 * @Date: 2022/6/7 10:56
 * @Copyright: 2022 Hangzhou Jiuxi Inc. All rights reserved.
 */
@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(prefix = "topinfo.mybatis.dynamic", name = "enable", havingValue = "true",matchIfMissing = false)
public class TopinfoDynamicSqlSessionFactoryAutoConfiguration implements InitializingBean {

    private MybatisPlusProperties mybatisPlusProperties;
    private Interceptor[] interceptors;
    private TypeHandler[] typeHandlers;
    private LanguageDriver[] languageDrivers;
    private ResourceLoader resourceLoader;
    private DatabaseIdProvider databaseIdProvider;
    private List<ConfigurationCustomizer> configurationCustomizers;
    private List<MybatisPlusPropertiesCustomizer> mybatisPlusPropertiesCustomizers;
    private ApplicationContext applicationContext;

    public TopinfoDynamicSqlSessionFactoryAutoConfiguration(MybatisPlusProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider, ObjectProvider<TypeHandler[]> typeHandlersProvider, ObjectProvider<LanguageDriver[]> languageDriversProvider, ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider, ObjectProvider<List<MybatisPlusPropertiesCustomizer>> mybatisPlusPropertiesCustomizerProvider, ApplicationContext applicationContext) {
        this.mybatisPlusProperties = properties;
        this.interceptors = (Interceptor[])interceptorsProvider.getIfAvailable();
        this.typeHandlers = (TypeHandler[])typeHandlersProvider.getIfAvailable();
        this.languageDrivers = (LanguageDriver[])languageDriversProvider.getIfAvailable();
        this.resourceLoader = resourceLoader;
        this.databaseIdProvider = (DatabaseIdProvider)databaseIdProvider.getIfAvailable();
        this.configurationCustomizers = (List)configurationCustomizersProvider.getIfAvailable();
        this.mybatisPlusPropertiesCustomizers = (List)mybatisPlusPropertiesCustomizerProvider.getIfAvailable();
        this.applicationContext = applicationContext;
    }

    /**
     * 动态数据源 SqlSessionFactory
     * @author Ypp
     * @date 2022/2/22 16:15
     * @param dataSource
     * @return org.apache.ibatis.session.SqlSessionFactory
     */
    @Bean
    @ConditionalOnProperty(prefix = "topinfo.mybatis.dynamic", name = "enable", havingValue = "true",matchIfMissing = false)
    public SqlSessionFactory sessionFactory(DynamicDataSource dataSource) throws Exception{

        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(this.mybatisPlusProperties.getConfigLocation())) {
            factory.setConfigLocation(resourceLoader.getResource(this.mybatisPlusProperties.getConfigLocation()));
        }

        this.applyConfiguration(factory);
        if (this.mybatisPlusProperties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(this.mybatisPlusProperties.getConfigurationProperties());
        }

        if (!ObjectUtils.isEmpty(interceptors)) {
            factory.setPlugins(interceptors);
        }

        if (this.databaseIdProvider != null) {
            factory.setDatabaseIdProvider(this.databaseIdProvider);
        }

        if (StringUtils.hasLength(this.mybatisPlusProperties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(this.mybatisPlusProperties.getTypeAliasesPackage());
        }

        if (this.mybatisPlusProperties.getTypeAliasesSuperType() != null) {
            factory.setTypeAliasesSuperType(this.mybatisPlusProperties.getTypeAliasesSuperType());
        }

        if (StringUtils.hasLength(this.mybatisPlusProperties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(this.mybatisPlusProperties.getTypeHandlersPackage());
        }

        if (StringUtils.hasLength(this.mybatisPlusProperties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(this.mybatisPlusProperties.getTypeAliasesPackage());
        }

        if (this.mybatisPlusProperties.getTypeAliasesSuperType() != null) {
            factory.setTypeAliasesSuperType(this.mybatisPlusProperties.getTypeAliasesSuperType());
        }

        if (StringUtils.hasLength(this.mybatisPlusProperties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(this.mybatisPlusProperties.getTypeHandlersPackage());
        }

        if (!ObjectUtils.isEmpty(this.typeHandlers)) {
            factory.setTypeHandlers(this.typeHandlers);
        }

        if (!ObjectUtils.isEmpty(this.mybatisPlusProperties.resolveMapperLocations())) {
            factory.setMapperLocations(this.mybatisPlusProperties.resolveMapperLocations());
        }

        Class<? extends LanguageDriver> defaultLanguageDriver = this.mybatisPlusProperties.getDefaultScriptingLanguageDriver();
        if (!ObjectUtils.isEmpty(this.languageDrivers)) {
            factory.setScriptingLanguageDrivers(this.languageDrivers);
        }

        Optional.ofNullable(defaultLanguageDriver).ifPresent(factory::setDefaultScriptingLanguageDriver);
        if (StringUtils.hasLength(this.mybatisPlusProperties.getTypeEnumsPackage())) {
            factory.setTypeEnumsPackage(this.mybatisPlusProperties.getTypeEnumsPackage());
        }

        GlobalConfig globalConfig = this.mybatisPlusProperties.getGlobalConfig();
        this.getBeanThen(MetaObjectHandler.class, globalConfig::setMetaObjectHandler);
        /*this.getBeanThen(IKeyGenerator.class, (i) -> {
            globalConfig.getDbConfig().setKeyGenerators(i);
        });*/
        this.getBeanThen(ISqlInjector.class, globalConfig::setSqlInjector);
        this.getBeanThen(IdentifierGenerator.class, globalConfig::setIdentifierGenerator);
        factory.setGlobalConfig(globalConfig);
        return factory.getObject();
    }

    private void applyConfiguration(MybatisSqlSessionFactoryBean factory) {
        MybatisConfiguration configuration = this.mybatisPlusProperties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(this.mybatisPlusProperties.getConfigLocation())) {
            configuration = new MybatisConfiguration();
        }

        if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
            Iterator var3 = this.configurationCustomizers.iterator();

            while(var3.hasNext()) {
                ConfigurationCustomizer customizer = (ConfigurationCustomizer)var3.next();
                customizer.customize(configuration);
            }
        }

        factory.setConfiguration(configuration);
    }

    private <T> void getBeanThen(Class<T> clazz, Consumer<T> consumer) {
        if (this.applicationContext.getBeanNamesForType(clazz, false, false).length > 0) {
            consumer.accept(this.applicationContext.getBean(clazz));
        }

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (!CollectionUtils.isEmpty(this.mybatisPlusPropertiesCustomizers)) {
            this.mybatisPlusPropertiesCustomizers.forEach((i) -> {
                i.customize(this.mybatisPlusProperties);
            });
        }

        this.checkConfigFileExists();
    }

    private void checkConfigFileExists() {
        if (this.mybatisPlusProperties.isCheckConfigLocation() && StringUtils.hasText(this.mybatisPlusProperties.getConfigLocation())) {
            Resource resource = this.resourceLoader.getResource(this.mybatisPlusProperties.getConfigLocation());
            Assert.state(resource.exists(), "Cannot find config location: " + resource + " (please add config file or check your Mybatis configuration)");
        }

    }
}
