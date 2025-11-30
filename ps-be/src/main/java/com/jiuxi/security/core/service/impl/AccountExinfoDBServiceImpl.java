package com.jiuxi.security.core.service.impl;

import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
import com.jiuxi.security.core.entity.vo.AccountExinfoVO;
import com.jiuxi.security.core.listener.LoginApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Description: 账号拓展表接口DB实现
 * @ClassName: AccountExinfoDBServiceImpl
 * @Author: pand
 * @Date: 2020-09-08 15:36
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class AccountExinfoDBServiceImpl implements LoginApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountExinfoDBServiceImpl.class);

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SecurityConfigurationProperties properties;

    private static final String findSql = "select err_count from tp_account_exinfo where account_id=? LIMIT 1";
    private static final String insertSql = "INSERT INTO tp_account_exinfo (account_id, err_count, last_err_time, last_login_time) VALUES (?, ?, ?, ?)";
    private static final String updateErrSql = "UPDATE tp_account_exinfo SET err_count=?, last_err_time=? WHERE account_id=?";
    private static final String updateLoginSql = "UPDATE tp_account_exinfo SET err_count=?, last_login_time=? WHERE account_id=?";
    private static final String lockSql = "UPDATE tp_account SET locked=1, update_time=? WHERE account_id=?";
    private static final String unlockSql = "UPDATE tp_account SET locked=0, update_time=? WHERE account_id=?";

    @Override
    public void exceute(boolean loginSuccess, String accountId) {
        if (jdbcTemplate == null) {
            throw new RuntimeException("在执行认证时，jdbcTemplate为null，请先在项目中引入了连接池的配置...");
        }
        String now = CommonDateUtil.now();

        List<AccountExinfoVO> list = jdbcTemplate.query(findSql, new Object[]{accountId}, new BeanPropertyRowMapper<>(AccountExinfoVO.class));
        if (loginSuccess) {
            if (list.isEmpty()) {
                // 新增一条
                jdbcTemplate.update(insertSql, new Object[]{accountId, 0, now, now});
            } else {
                Integer errCount = list.get(0).getErrCount();
                // 登陆成功，需要将失败次数归0
                this.resetErrCount(accountId, now, errCount);
            }
        } else {
            if (list.isEmpty()) {
                // 新增一条
                jdbcTemplate.update(insertSql, new Object[]{accountId, 1, now, ""});
            } else {
                Integer errCount = list.get(0).getErrCount();
                // 登陆失败，需要记录失败次数
                this.saveOrUpdate(accountId, now, errCount);
            }
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
    private void saveOrUpdate(String accountId, String now, Integer errCount) {

        // 更新最后一次登陆失败时间
        jdbcTemplate.update(updateErrSql, new Object[]{errCount + 1, now, accountId});
        
        // 使用新的配置获取最大错误次数
        int maxAttempts = properties.getAuthentication().getAccountLockout().getMaxAttempts();
        if (errCount >= maxAttempts - 1) {
            // 锁定账号
            jdbcTemplate.update(lockSql, new Object[]{now, accountId});
            LOGGER.info("账户登录失败次数达到上限，账户已锁定: accountId={}, failureCount={}", accountId, errCount + 1);
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
    private void resetErrCount(String accountId, String now, Integer errCount) {

        // 登陆错误次数归0, 由于需要更新最后一次登陆成功的时间，所以这里牺牲一点性能，每次都更新错误次数为0。
        jdbcTemplate.update(updateLoginSql, new Object[]{0, now, accountId});
        if (errCount != 0) {
            // 解锁账号，只要输入错误过一次，就更新账号表。弊端是可能这时候输入错误次数不超过5次，没有锁定，也会更新，浪费一点性能。
            jdbcTemplate.update(unlockSql, new Object[]{now, accountId});
        }
    }
}
