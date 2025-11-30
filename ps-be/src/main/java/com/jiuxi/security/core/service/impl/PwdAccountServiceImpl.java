package com.jiuxi.security.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.admin.core.service.TpSystemConfigService;
import com.jiuxi.admin.core.service.TpTimeRuleService;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SmUtils;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
import com.jiuxi.security.core.entity.vo.AccountExinfoVO;
import com.jiuxi.security.core.entity.vo.AccountVO;
import com.jiuxi.security.core.event.LoginApplicationEvent;
import com.jiuxi.security.core.listener.LoginApplicationEventCollection;
import com.jiuxi.security.core.listener.LoginApplicationService;
import com.jiuxi.security.core.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 账号/密码登陆实现类
 * @ClassName: PersonServiceImpl
 * @Author: pand
 * @Date: 2020-08-28 13:46
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class PwdAccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PwdAccountServiceImpl.class);

    @Autowired(required = false)
    protected JdbcTemplate jdbcTemplate; // JDBC模板，用于数据库操作

    @Autowired
    protected SecurityConfigurationProperties properties; // 安全配置属性

    @Autowired
    private LoginApplicationEventCollection loginApplicationEventCollection; // 登录事件集合

    @Autowired
    private LoginApplicationService accountExinfoDBService; // 账号扩展信息数据库服务

    @Autowired(required = false)
    private TpSystemConfigService tpSystemConfigService; // 系统配置服务

    @Autowired(required = false)
    private TpTimeRuleService tpTimeRuleService; // 时间规则服务

    @PostConstruct
    public void init() {
        loginApplicationEventCollection.addEvent(accountExinfoDBService);
    }

    /**
     * Spring应用上下文对象
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 账号冻结状态标识：1表示冻结，0表示未冻结
     */
    protected final String LOCKED = "1";

    private static final String loginSql = "SELECT account_id, tenant_id, person_id, userpwd, locked, EXTEND01 as restPwd, expired_time, last_password_change_time FROM tp_account WHERE username = ?  AND enabled = '1' AND actived = '1' LIMIT 1";

    private static final String personSql = "select CATEGORY from tp_person_basicinfo where PERSON_ID = ?";

    /**
     * 查询账号最后登陆时间和登陆错误次数
     */
    private static final String accountExinfoSql = "select account_id, err_count, last_err_time, last_login_time from tp_account_exinfo where account_id = ?";

    /**
     * 账号是否被禁用
     */
    private static final String isEnabledSql = "SELECT count(1) FROM tp_account WHERE   1=1 and username = ? AND enabled = '0'   LIMIT 1";

     /**
     * 账号是否被锁定
     */
    private static final String isLockedSql = "SELECT count(1) FROM tp_account WHERE 1=1 and username = ? AND locked = '1'   LIMIT 1";

    /**
     * <p>
     * 该方法仅根据用户名查询账号基本信息，不进行密码验证、账号锁定检查等登录相关的复杂逻辑。
     * 适用于SSO回调等场景，只需要获取用户基本信息的情况。
     * </p>
     *
     * @param userName 用户名，不能为空
     * @return AccountVO 账号信息对象，如果用户不存在则返回null
     * @throws TopinfoRuntimeException 当存在多个相同用户名的账号或查询用户信息失败时抛出
     * @throws RuntimeException 当jdbcTemplate为null时抛出
     * @author 系统生成
     * @since 1.0
     */
    public AccountVO queryAccountByUsername(String userName) {
        // 记录方法调用开始
        LOGGER.info("=== queryAccountByUsername 方法开始执行 ===");
        LOGGER.info("输入参数 - userName: {}", userName);
        
        // 检查jdbcTemplate是否为空
        if (jdbcTemplate == null) {
            LOGGER.error("jdbcTemplate为null，无法执行数据库查询");
            throw new RuntimeException("在执行认证时，jdbcTemplate为null，请先在项目中引入了连接池的配置...");
        }
        LOGGER.debug("jdbcTemplate检查通过，开始执行数据库查询");

        // 记录执行的SQL语句和参数
        LOGGER.debug("执行SQL查询 - SQL: {}", loginSql);
        LOGGER.debug("查询参数: [{}]", userName);
        
        // BeanPropertyRowMapper 字段转换时，注意数据库字段与实体属性的对应
        List<AccountVO> list = jdbcTemplate.query(loginSql, new Object[]{userName}, new BeanPropertyRowMapper<>(AccountVO.class));
        
        // 记录查询结果
        LOGGER.info("数据库查询完成，返回结果数量: {}", list != null ? list.size() : 0);
        
        // 检查查询结果是否为空
        if (list == null || list.isEmpty()) {
            LOGGER.error("查询失败，用户名不存在，当前查询用户名:{}", userName);
            LOGGER.info("=== queryAccountByUsername 方法执行结束，返回null ===");
            return null;
        }
        
        // 检查是否存在多个相同用户名的账号
        if (list.size() > 1) {
            LOGGER.error("查询失败，存在多个相同用户名的账号，当前查询用户名:{}, 查询到的账号数量:{}", userName, list.size());
            throw new TopinfoRuntimeException(-1, "查询失败，存在多个相同用户名的账号");
        }

        // 获取唯一的账号信息
        AccountVO accountVO = list.get(0);
        LOGGER.info("成功获取账号信息 - accountId: {}, tenantId: {}, personId: {}", 
                   accountVO.getAccountId(), accountVO.getTenantId(), accountVO.getPersonId());
        LOGGER.debug("账号详细信息 - locked: {}, expiredTime: {}", 
                    accountVO.getLocked(), accountVO.getExpiredTime());
        
        // 查询人员是政府人员还是企业人员的标示
        LOGGER.debug("开始查询人员类别信息 - personId: {}", accountVO.getPersonId());
        LOGGER.debug("执行人员类别查询SQL - SQL: {}", personSql);
        Integer category = jdbcTemplate.queryForObject(personSql, new Object[]{accountVO.getPersonId()}, Integer.class);
        if (null == category) {
            LOGGER.error("查询失败，用户信息有问题，当前查询用户名:{}, personId:{}", userName, accountVO.getPersonId());
            throw new TopinfoRuntimeException(-1, "查询失败，用户信息查询失败");
        }
        LOGGER.info("成功查询到人员类别 - category: {}", category);
        accountVO.setCategory(category);

        LOGGER.info("=== queryAccountByUsername 方法执行成功结束 ===");
        return accountVO;
    }

    /**
     * 用户名密码登陆，根据用户名查询用户/账号是否存在，存在继续校验账号是否可用
     *
     * @param vo: PersonVO的实体对象
     * @return com.jiuxi.security.core.entity.vo.SmsPersonVO
     * @author pand
     * @date 2020-08-27 14:48
     */
    @Override
    public AccountVO queryAccount(AccountVO vo) {

        String userName = vo.getUserName();
        String passWord = vo.getUserpwd();
        if (jdbcTemplate == null) {
            throw new RuntimeException("在执行认证时，jdbcTemplate为null，请先在项目中引入了连接池的配置...");
        }

        // 检查账号是否被禁用
        Integer disabledCount = jdbcTemplate.queryForObject(isEnabledSql, new Object[]{userName}, Integer.class);
        if (disabledCount != null && disabledCount > 0) {
            LOGGER.error("登录失败，账号已被禁用，当前登录用户名:{}", userName);
            throw new TopinfoRuntimeException(-1, "登录失败，账号已停用");
        }
        

        // 检查账号是否被锁定
        Integer lockedCount = jdbcTemplate.queryForObject(isLockedSql, new Object[]{userName}, Integer.class);
        if (lockedCount != null && lockedCount > 0) {
            LOGGER.error("登录失败，账号已被锁定，当前登录用户名:{}", userName);
            throw new TopinfoRuntimeException(-1, "登录失败，账号已被锁定");
        }


        // BeanPropertyRowMapper 字段转换时，注意数据库字段与实体属性的对应
        List<AccountVO> list = jdbcTemplate.query(loginSql, new Object[]{userName}, new BeanPropertyRowMapper<>(AccountVO.class));
        if (list != null && list.size() != 1) {
            LOGGER.error("登录失败，请检查用户名是否正确或用户是否有效，当前登录用户名:{}", userName);
            throw new TopinfoRuntimeException(-1, "登录失败，用户名或密码错误");
        }

        AccountVO accountVO = list.get(0);
        // 校验账号是否过期
        this.checkExpiredTime(accountVO);
        
        // 校验密码是否过期
        this.checkPasswordExpiry(accountVO);

        // 校验是否冻结
        if (LOCKED.equals(accountVO.getLocked())) {
            AccountExinfoVO accountExinfoVO = jdbcTemplate.queryForObject(accountExinfoSql, new BeanPropertyRowMapper<>(AccountExinfoVO.class), accountVO.getAccountId());
            if (null == accountExinfoVO) {
                LOGGER.error("登录失败，用户被冻结，当前登录用户名:{}", userName);
                throw new TopinfoRuntimeException(-1, "登录失败，用户被冻结");
            }

            // 1。半个小时以内再次登陆，直接抛账号被冻结异常
            int errCount = accountExinfoVO.getErrCount();
            String lastErrTimeStr = accountExinfoVO.getLastErrTime();
            LocalDateTime lastErrTime = LocalDateTime.parse(lastErrTimeStr, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            // 1.1 解锁时间 = 最后一次登陆错误时间 + 锁定时长（使用新配置）
            int lockoutDuration = properties.getAuthentication().getAccountLockout().getLockoutDuration();
            LocalDateTime delockingTime = lastErrTime.plusMinutes(lockoutDuration);
            LocalDateTime now = LocalDateTime.now();
            if (delockingTime.isAfter(now)) {
                LOGGER.error("账号被冻结 {} 分钟内登录失败，当前登录用户名:{}", lockoutDuration, userName);
                Duration between = Duration.between(now, delockingTime);
                String timeStr = between.toMinutes() > 0 ? between.toMinutes() + "分钟" : between.toMillis() / 1000 + "秒";
                throw new TopinfoRuntimeException(-1, "登录信息错误次数超限，请" + timeStr + "后再试！");
            }

            // 2。锁定时长外但是登陆错误次数大于最大累计次数，抛账号被冻结异常
            int maxTotalAttempts = properties.getAuthentication().getAccountLockout().getMaxTotalAttempts();
            if (errCount > maxTotalAttempts) {
                LOGGER.error("尝试登陆错误次数超过 {} 次登录失败，当前登录用户名:{}", maxTotalAttempts, userName);
                throw new TopinfoRuntimeException(-1, "登录信息错误次数超限，该账号已被冻结，请联系管理员解冻！");
            }

            // 3。锁定时长外且登陆错误次数小于最大累计次数，再次登陆
        }

        if (!SmUtils.match(passWord, accountVO.getUserpwd())) {
            // 密码不对，需要记录错误次数，达到五次，账号锁定
            if (null != loginApplicationEventCollection) {
                // 通过上下文对象发布监听
                applicationContext.publishEvent(new LoginApplicationEvent("登陆失败监听", loginApplicationEventCollection, false, accountVO.getAccountId()));
            }
            LOGGER.error("登录失败，密码错误，当前登录用户名:{}", userName);
            throw new TopinfoRuntimeException(-1, "登录失败，用户名或密码错误");
        }

        // 时间规则验证
        if (tpTimeRuleService != null) {
            // 直接从数据库查询用户角色ID列表
            String roleQuerySql = "SELECT ROLE_ID FROM tp_person_role WHERE PERSON_ID = ?";
            List<String> roleIdsList = jdbcTemplate.queryForList(roleQuerySql, String.class, accountVO.getPersonId());
            
            LOGGER.debug("用户 {} 的角色列表: {}", userName, roleIdsList);
            
            // 验证登录时间
            TpTimeRuleService.LoginTimeValidationResult validationResult = 
                tpTimeRuleService.validateLoginTimeWithReason(accountVO.getPersonId(), roleIdsList);
            
            if (!validationResult.isAllowed()) {
                LOGGER.error("登录失败，时间规则验证不通过，用户名:{}, 原因:{}", userName, validationResult.getReason());
                throw new TopinfoRuntimeException(-1, validationResult.getReason());
            }
        }

        // 查询人员是政府人员还是企业人员的标示
        Integer category = jdbcTemplate.queryForObject(personSql, new Object[]{accountVO.getPersonId()}, Integer.class);
        if (null == category) {
            LOGGER.error("登录失败，用户信息有问题，当前登录用户名:{}", userName);
            throw new TopinfoRuntimeException(-1, "登录失败，用户信息查询失败");
        }
        accountVO.setCategory(category);

        if (null != loginApplicationEventCollection) {
            // 认证通过，清零失败次数
            applicationContext.publishEvent(new LoginApplicationEvent("登陆成功监听", loginApplicationEventCollection, true, accountVO.getAccountId()));
        }

        return accountVO;
    }

    /**
     * 校验账号是否过期, 过期则抛出异常
     *
     * @param accountVO
     * @return void
     * @author 杨占锐
     * @date 2025/2/13 11:18
     */
    private void checkExpiredTime(AccountVO accountVO) {
        String expiredTime = accountVO.getExpiredTime();
        if (StrUtil.isBlank(expiredTime)) {
            return;
        }
        String date = CommonDateUtil.date();
        if (date.compareTo(expiredTime) > 0) {
            LOGGER.error("登录失败，用户已过期:accountVO：{}", JSONObject.toJSONString(accountVO));
            throw new TopinfoRuntimeException(-1, "登录失败，用户已过期");
        }
    }

    /**
     * 校验密码是否过期，如果过期则在AccountVO中设置提醒信息
     *
     * @param accountVO
     * @return void
     * @author AI Assistant
     * @date 2025/1/30
     */
    private void checkPasswordExpiry(AccountVO accountVO) {
        String lastPasswordChangeTime = accountVO.getLastPasswordChangeTime();
        if (StrUtil.isBlank(lastPasswordChangeTime)) {
            // 如果没有密码修改时间记录，认为密码已过期，直接锁定账户
            LOGGER.error("用户密码已过期，账户被锁定，用户名:{}", accountVO.getUserName());
            throw new TopinfoRuntimeException(-1, "账户已被锁定，请联系管理员重置密码");
        }

        try {
            // 获取密码有效期配置（月数）
            String validityMonthsStr = tpSystemConfigService.getConfigValue("password.validity.months");
            int validityMonths = StrUtil.isBlank(validityMonthsStr) ? 3 : Integer.parseInt(validityMonthsStr);

            // 获取密码过期提醒天数配置
            String reminderDaysStr = tpSystemConfigService.getConfigValue("password.expiry.reminder.days");
            int reminderDays = StrUtil.isBlank(reminderDaysStr) ? 7 : Integer.parseInt(reminderDaysStr);

            // 解析上次密码修改时间
            LocalDateTime lastChangeTime = LocalDateTime.parse(lastPasswordChangeTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiryTime = lastChangeTime.plusMonths(validityMonths);
            LocalDateTime reminderTime = expiryTime.minusDays(reminderDays);

            if (now.isAfter(expiryTime)) {
                // 1. 超过有效期，直接锁定账户
                LOGGER.error("用户密码已过期，账户被锁定，用户名:{}, 密码修改时间:{}", accountVO.getUserName(), lastPasswordChangeTime);
                throw new TopinfoRuntimeException(-1, "密码已过期，账户已被锁定，请联系管理员重置密码");
            } else if (now.isAfter(reminderTime)) {
                // 2. 在提醒期内，强制引导用户修改密码
                long daysUntilExpiry = Duration.between(now, expiryTime).toDays();
                accountVO.setRestPwd("3"); // 使用"3"表示强制修改状态
                LOGGER.info("用户密码即将过期，剩余{}天，强制引导修改密码，用户名:{}", daysUntilExpiry, accountVO.getUserName());
            } else {
                // 3. 在安全期内，正常登录，不设置任何提醒
                LOGGER.info("用户密码在安全期内，正常登录，用户名:{}", accountVO.getUserName());
                // 不设置restPwd，保持为null，表示正常状态
            }
        } catch (TopinfoRuntimeException e) {
            // 重新抛出业务异常
            throw e;
        } catch (Exception e) {
            LOGGER.error("检查密码有效期时发生异常，用户名:{}, 异常信息:{}", accountVO.getUserName(), e.getMessage(), e);
            // 发生异常时，为了安全起见，锁定账户
            throw new TopinfoRuntimeException(-1, "密码有效期检查异常，账户已被锁定，请联系管理员");
        }
    }
}
