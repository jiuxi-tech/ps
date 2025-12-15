package com.jiuxi.shared.config.database;

/**
 * @ClassName: DataSourceConfiguration
 * @Description: 数据源配置实体
 * @Author: Ypp
 * @Date: 2020/3/17 17:32
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class DataSourceConfiguration {

    /** 数据库url */
    private String url;

    /** 用户名 */
    private String username;

    /*** 密码 */
    private String password;

    /** 驱动类 */
    private String driverClass;

    /** 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时 */
    private int initialSize;

    /** 最大连接池数量 */
    private int maxActive = 500;

    /** 最小连接池数量 */
    private int minIdle = 5;

    /** 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。 */
    private int maxWait = -1;

    /** 用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会其作用。 */
    private String validationQuery = "SELECT 1";

    /** 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
    private boolean testOnBorrow = false;

    /** 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能 */
    private boolean testOnReturn = false;

    /** 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。 */
    private boolean testWhileIdle = true;

    /** 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。 */
    private boolean poolPreparedStatements = false;

    /** 多个逗号分隔开，配置监控统计拦截的filters：
     *  stat:监控统计
     *  wall：防御sql注入
     *  log4j：日志记录
     */
    private String filters = "stat";

    /** 配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。 */
    private boolean useUnfairLock = false;

    /** 数据库类型，比如：mysql等 */
    private String dbType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public boolean isUseUnfairLock() {
        return useUnfairLock;
    }

    public void setUseUnfairLock(boolean useUnfairLock) {
        this.useUnfairLock = useUnfairLock;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}