package com.jiuxi.mybatis.autoconfig;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import com.jiuxi.mybatis.bean.DataSourceConfig;
import com.jiuxi.mybatis.bean.Dynamic;
import com.jiuxi.mybatis.core.dynamic.DynamicDataSource;
import com.jiuxi.mybatis.core.dynamic.DynamicDataSourceAspect;
import com.jiuxi.mybatis.core.idgenerator.CustomIdGenerator;
import com.jiuxi.mybatis.core.runner.MybatisCommandLineRunner;
import com.jiuxi.mybatis.service.LocalLicenceCacheService;
import com.jiuxi.mybatis.service.impl.LocalLicenceCacheServiceImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * ConditionalOnProperty： 检查配置文件中，是否有以topinfo.weixin.wxenterprise开头名称为active的配置，值与havingValue指定的值进行比较，如果一样则生效。
 *
 * @author pand
 * @date 2020-08-25 17:36
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(TopinfoMybatisAutoProperties.class)
public class TopinfoMybatisAutoConfiguration {


    private TopinfoMybatisAutoProperties properties;


    public TopinfoMybatisAutoConfiguration(TopinfoMybatisAutoProperties properties) {
        this.properties = properties;
    }

    /**
     * 数据源
     * @author 杨攀
     * @date 2020/1/9 15:09
     * @param
     * @return com.alibaba.druid.pool.DruidDataSource
     */
    @Bean
    @ConditionalOnProperty(prefix = "topinfo.mybatis.datasource-config", name = "url", matchIfMissing = false)
    @ConditionalOnMissingBean(DataSource.class)
    public DruidDataSource dataSource() {

        DataSourceConfig config = properties.getDatasourceConfig();
        DruidDataSource dataSource = new DruidDataSource();
        if(null != config.getDriverClass()){
            dataSource.setDriverClassName(config.getDriverClass());
        }

        // 手动指定 数据库类型
        if(null != config.getDbType()){
            dataSource.setDbType(config.getDbType());
        }

        dataSource.setUrl(config.getUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());

        // 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
        dataSource.setInitialSize(config.getInitialSize());
        // 最大连接池数量
        dataSource.setMaxActive(config.getMaxActive());
        // 最小连接池数量
        dataSource.setMinIdle(config.getMinIdle());
        // 最大连接池数量
        dataSource.setMaxWait(config.getMaxWait());
        // 用来检测连接是否有效的sql，要求是一个查询语句
        dataSource.setValidationQuery(config.getValidationQuery());
        // 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
        dataSource.setTestOnBorrow(config.isTestOnBorrow());
        // 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
        dataSource.setTestOnReturn(config.isTestOnReturn());
        // 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
        dataSource.setTestWhileIdle(config.isTestWhileIdle());
        // 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。
        dataSource.setPoolPreparedStatements(config.isPoolPreparedStatements());
        // 配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
        dataSource.setUseUnfairLock(config.isUseUnfairLock());

        //配置监控统计拦截的filters
        try {
            dataSource.setFilters(config.getFilters());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataSource;
    }

    /**
     * @param dataSource
     * @return org.springframework.transaction.PlatformTransactionManager
     * @description: 事务配置
     * @author 杨攀
     * @date 2020/1/9 15:09
     */
    @Bean
    @ConditionalOnProperty(prefix = "topinfo.mybatis.datasource-config", name = "url", matchIfMissing = false)
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }


    /**
     * 动态数据源
     * @author 杨攀
     * @date 2022/2/22 14:43
     * @param
     * @return com.jiuxi.mybatis.core.dynamic.DynamicDataSource
     */
    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "topinfo.mybatis.dynamic", name = "enable", havingValue = "true",matchIfMissing = false)
    public DynamicDataSource dynamicDataSource(){

        Dynamic dynamic = properties.getDynamic();

        Map<String, DataSourceConfig>  datasourceConfigs = dynamic.getDatasourceConfigs();

        // 目标数据源集合
        Map<Object, Object> targetDataSources = new HashMap<>(datasourceConfigs.size());

        datasourceConfigs.forEach((key, config) ->{

            DruidDataSource dataSource = new DruidDataSource();
            if(null != config.getDriverClass()){
                dataSource.setDriverClassName(config.getDriverClass());
            }

            // 手动指定 数据库类型
            if(null != config.getDbType()){
                dataSource.setDbType(config.getDbType());
            }

            dataSource.setUrl(config.getUrl());
            dataSource.setUsername(config.getUsername());
            dataSource.setPassword(config.getPassword());

            // 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
            dataSource.setInitialSize(config.getInitialSize());
            // 最大连接池数量
            dataSource.setMaxActive(config.getMaxActive());
            // 最小连接池数量
            dataSource.setMinIdle(config.getMinIdle());
            // 最大连接池数量
            dataSource.setMaxWait(config.getMaxWait());
            // 用来检测连接是否有效的sql，要求是一个查询语句
            dataSource.setValidationQuery(config.getValidationQuery());
            // 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
            dataSource.setTestOnBorrow(config.isTestOnBorrow());
            // 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
            dataSource.setTestOnReturn(config.isTestOnReturn());
            // 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
            dataSource.setTestWhileIdle(config.isTestWhileIdle());
            // 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。
            dataSource.setPoolPreparedStatements(config.isPoolPreparedStatements());
            // 配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
            dataSource.setUseUnfairLock(config.isUseUnfairLock());

            //配置监控统计拦截的filters
            try {
                dataSource.setFilters(config.getFilters());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            targetDataSources.put(key, dataSource);
        });


        // 默认数据源
        String defaultSourceName = dynamic.getDefaultSource();
        // 判断默认数据源的配置是否正确
        if(!targetDataSources.containsKey(defaultSourceName)){
            throw new TopinfoRuntimeException(-1, "默认数据源设置错误！请检查topinfo.mybatis.dynamic.default-source的配置");
        }

        DynamicDataSource dataSource = new DynamicDataSource();
        // 设置多数据源
        dataSource.setTargetDataSources(targetDataSources);
        // 设置默认数据源
        dataSource.setDefaultTargetDataSource(targetDataSources.get(defaultSourceName));

        return dataSource;
    }

    /**
     * 动态数据源 SqlSessionFactory
     * @author 杨攀
     * @date 2022/2/22 16:15
     * @param dataSource
     * @return org.apache.ibatis.session.SqlSessionFactory
     */
    /*
   @Bean
   @ConditionalOnProperty(prefix = "topinfo.mybatis.dynamic", name = "enable", havingValue = "true",matchIfMissing = false)
   public SqlSessionFactory sessionFactory(DynamicDataSource dataSource) throws Exception{

        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setMapperLocations(resolver.getResources(environment.getProperty("mybatis-plus.mapper-locations")));
        sessionFactoryBean.setTypeAliasesPackage(environment.getProperty("mybatis-plus.typeAliasesPackage"));
        return sessionFactoryBean.getObject();
    }*/

    /**
     * 多数据源事务
     * @author 杨攀
     * @date 2022/2/22 16:06
     * @param dataSource
     * @return org.springframework.transaction.PlatformTransactionManager
     */
    @Bean
    @ConditionalOnProperty(prefix = "topinfo.mybatis.dynamic", name = "enable", havingValue = "true",matchIfMissing = false)
    public PlatformTransactionManager txManager(DynamicDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 多数据源 dynamicDataSourceAspect
     * @author 杨攀
     * @date 2022/2/22 16:15
     * @param
     * @return com.jiuxi.mybatis.core.dynamic.DynamicDataSourceAspect
     */
    @Bean
    @ConditionalOnProperty(prefix = "topinfo.mybatis.dynamic", name = "enable", havingValue = "true",matchIfMissing = false)
    public DynamicDataSourceAspect dynamicDataSourceAspect(){
        return new DynamicDataSourceAspect();
    }



    /**
     * @Description: 配置Druid的监控，配置一个管理后台的Servlet
     * @param: @return
     * @return: ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> statViewServlet() {

        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<StatViewServlet>(new StatViewServlet(), "/druid/*");
        //添加初始化参数
        Map<String, String> initParams = new HashMap<>(3);
        //控制台管理用户
        initParams.put("loginUsername", "topinfo");
        initParams.put("loginPassword", "topinfo");
        // IP白名单
        initParams.put("allow", "127.0.0.1");
        // IP黑名单(共同存在时，deny优先于allow)
        // initParams.put("deny","192.168.15.21");

        bean.setInitParameters(initParams);
        return bean;
    }


    /**
     * @Description: 配置一个web监控的filter
     * @param: @return
     * @return: FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> webStatFilter() {

        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<WebStatFilter>();
        bean.setFilter(new WebStatFilter());
        //添加初始化参数
        Map<String, String> initParams = new HashMap<>(2);
        // 添加不需要忽略的格式信息
        initParams.put("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        // 关闭druid的session监控
        initParams.put("sessionStatEnable", "false");
        bean.setInitParameters(initParams);
        //添加过滤规则.
        bean.setUrlPatterns(Arrays.asList("/*"));

        return bean;
    }

    /**
     * @param
     * @return cn.hutool.core.lang.Snowflake
     * @description:
     * @author 杨攀
     * @date 2020/3/17 17:48
     */
    @Bean
    public MybatisCommandLineRunner mybatisCommandLineRunner() {
        return new MybatisCommandLineRunner();
    }

    @Bean
    @ConditionalOnMissingBean(IdentifierGenerator.class)
    public IdentifierGenerator idGenerator() {
        return new CustomIdGenerator();
    }


    /**
     * 分页插件， 因为MybatisPlus分页时，不识别TDengine， 而TDengine与mysql兼容，则这里指定为Mysql方言，
     *
     * @param
     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     * @author 杨攀
     * @date 2022/11/23 14:36
     */
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 设置最大单页限制数量，默认500， 改成3000
        paginationInnerInterceptor.setMaxLimit(3000L);
        // 设置方言
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    /**
     * @return com.github.pagehelper.PageInterceptor
     * @description: 分页插件 -- 旧版
     * @author 杨攀
     * @date 2020/1/9 15:14
     */
    /*
    @Bean
    @ConditionalOnMissingBean(PaginationInterceptor.class)
    public PaginationInterceptor pageInterceptor() {
        PaginationInterceptor pageInterceptor = new PaginationInterceptor();
        Properties props = new Properties();
        // 启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页，禁用合理化时，
        // 如果pageNum<1或pageNum>pages会返回空数据
        props.setProperty("reasonable", "true");
        // 支持通过 Mapper 接口参数来传递分页参数，默认值false，分页插件会从查询方法的参数值中，
        // 自动根据上面 params 配置的字段中取值，查找到合适的值时就会自动分页。
        props.setProperty("supportMethodsArguments", "true");
        pageInterceptor.setProperties(props);
        // 设置单页最大数量限制，默认500， 改成3000
        pageInterceptor.setLimit(3000);

        // 多租户属于 SQL 解析部分，依赖 MP 分页插件
        if("true".equals(properties.getTenant())){
            List<ISqlParser> sqlParserList = new ArrayList<>();

            TenantSqlParser tenantSqlParser = new TopinfoTenantParser();
            List<String> ignoreTenantTables = properties.getIgnoreTenantTables();

            // 添加租户处理器
            tenantSqlParser.setTenantHandler(new TopinfoTenantHandler(ignoreTenantTables));
            sqlParserList.add(tenantSqlParser);
            pageInterceptor.setSqlParserList(sqlParserList);
        }

        return pageInterceptor;
    }*/


    /**
     * 许可证
     *
     * @return com.jiuxi.security.core.service.TopinfoSecurityLogoutService
     * @date 2024/10/8 14:00
     */
    // @Bean
    public LocalLicenceCacheService localLicenceCacheService() {
        return new LocalLicenceCacheServiceImpl();
    }

}
