package com.jiuxi.security.core.service.impl;

import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
import com.jiuxi.security.core.listener.LoginApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description: 账号拓展表接口基于redis实现 demo
 * @ClassName: AccountExinfoRedisServiceImpl
 * @Author: pand
 * @Date: 2020-09-08 15:36
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class AccountExinfoRedisServiceImpl implements LoginApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountExinfoRedisServiceImpl.class);

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SecurityConfigurationProperties properties;

    /**
     * 将账号冻结
     */
    private static final String lockSql = "UPDATE tp_account SET locked=1, update_time=? WHERE account_id=?";

    @Override
    public void exceute(boolean loginSuccess, String accountId) {
        if (loginSuccess) {
            // 登陆成功，需要将失败次数归0
            this.resetErrCount(accountId);
        } else {
            // 登陆失败，需要记录失败次数
            this.saveOrUpdate(accountId);
        }
    }

    /**
     * 根据账号id记录账号登陆失败次数
     *
     * @param accountId: 账号id
     * @return void
     * @author pand
     * @date 2020-09-08 19:16
     */
    private void saveOrUpdate(String accountId) {
        if (jdbcTemplate == null) {
            throw new RuntimeException("在执行认证时，jdbcTemplate为null，请先在项目中引入了连接池的配置...");
        }

        String now = CommonDateUtil.now();
        // TODO 1。先查询失败的次数，次数为null，还没有失败过，不为null，将值加+1
        Integer errCount = 0;
        if (errCount < properties.getAuthentication().getErrCount()) {
            // TODO 2-1。判断+1之后的失败次数是否小于设置值，小于的话，不作处理，key不失效
        } else {
            //  TODO 2-2。+1之后的失败次数不小于设置值，锁定账号
            jdbcTemplate.update(lockSql, new Object[]{now, accountId});
        }
    }

    /**
     * 根据账号id将记录失败测试的字段重置归0
     *
     * @param accountId: 账号id
     * @return void
     * @author pand
     * @date 2020-09-08 19:17
     */
    private void resetErrCount(String accountId) {
        if (jdbcTemplate == null) {
            throw new RuntimeException("在执行认证时，jdbcTemplate为null，请先在项目中引入了连接池的配置...");
        }

        // TODO 将登陆失败次数归0，key 记录失效时间 30天

    }
}
