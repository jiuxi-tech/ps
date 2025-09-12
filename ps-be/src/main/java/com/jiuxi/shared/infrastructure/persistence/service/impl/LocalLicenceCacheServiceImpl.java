package com.jiuxi.shared.infrastructure.persistence.service.impl;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.SmUtils;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.mybatis.bean.SecurityLicenceVO;
import com.jiuxi.shared.infrastructure.persistence.service.LocalLicenceCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author pand
 * @Description 许可证
 * @ClassName LocalLicenceCacheServiceImpl
 * packageName com.jiuxi.shared.infrastructure.persistence.service.impl
 * @Date 2025/1/10 14:46
 * @Copyright: 2025 www.tuxun.net Inc. All rights reserved.
 */
public class LocalLicenceCacheServiceImpl implements LocalLicenceCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalLicenceCacheServiceImpl.class);

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuuMMdd");

    /**
     * 根据服务器mac地址生成的的序列号本地缓存，项目启动生成缓存起来
     */
    private static final String SECURITY_SERIAL_NUMBER_CACHE_KEY = "mybatis-serial-number-key";
    /**
     * 系统序列号本地缓存
     */
    private Cache<String, String> mybatisSerialNumberCache;

    /**
     * 根据系统序列号查询许可证信息
     */
    private static String VIEW_LICENCE_BY_SYSTEM_SERIAL_NUMBER = "select LICENCE_ID as licenceId, SYSTEM_NAME as systemName, HASH_CODE as hashCode, EXPIRING_DATE as expiringDate, extend01 as systemSerialNumber from tp_licence where ACTIVED = 1 and extend01 = ?";

    /**
     * jdbc模版
     */
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        // 许可证序列号本地缓存,不需要过期
        mybatisSerialNumberCache = CacheUtil.newFIFOCache(1);

        // 校验
        this.syncCheck();
    }

    /**
     * 根据系统序列号查询许可证信息
     *
     * @return
     */
    private SecurityLicenceVO viewBySystemSerialNumber() {

        String systemSerialNumber = this.generatorSystemSerialNumber();
        List<SecurityLicenceVO> list;
        try {
            list = jdbcTemplate.query(VIEW_LICENCE_BY_SYSTEM_SERIAL_NUMBER, new Object[]{systemSerialNumber}, new BeanPropertyRowMapper<>(SecurityLicenceVO.class));
        } catch (Exception e) {
            LOGGER.error("查看失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }

        SecurityLicenceVO vo = null;
        if (null != list && !list.isEmpty()) {
            if (list.size() == 1) {
                vo = list.get(0);
            } else {
                throw new TopinfoRuntimeException(-1, "存在多条许可证信息！");
            }
        }

        return vo;
    }

    /**
     * 校验
     */
    private void syncCheck() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("Ipc-SyncBasicInfo-Pool-").build();

        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, threadFactory);

        /**
         *  scheduleWithFixedDelay:
         *  首次执行: 在 initialDelay 时间之后，任务首次被执行。
         *  后续执行: 每当任务执行结束后，scheduleWithFixedDelay 会等待 delay 时间间隔，然后再次执行任务。
         *  这意味着，如果任务的执行时间超过了 delay，下一次执行将在当前任务结束之后立即开始计算新的 delay，而非严格按照固定时间间隔执行。
         */
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {

            @Override
            public void run() {
                try {
                    // 执行同步
                    jcBindMacxkz();

                } catch (Exception e) {
                    LOGGER.error("【定时同步】启动定时同步错误，错误:{}", ExceptionUtils.getStackTrace(e));
                }
            }
            // 首次任务在1分钟之后执行，之后在每次任务执行完12小时后执行
        }, 10, 43200, TimeUnit.SECONDS);
    }

    /**
     * 检查绑定mac地址的许可证是否正常
     *
     * @return true：正常， false：不可用
     */
    private boolean jcBindMacxkz() {

        LOGGER.info("------------------------关闭许可证校验------------------------");


        return true;
/*
        // 先查询缓存中 序列号 对应的 许可证信息
        // 获取本地mac地址
        String systemSerialNumber = this.generatorSystemSerialNumber();

        // 查询数据库中 序列号 对应的 许可证信息
        SecurityLicenceVO vo = this.viewBySystemSerialNumber();
        if (null == vo) {
            // 许可证信息，集群部署会有多条，查询不到缓存null
            LOGGER.error("序列号systemSerialNumber: {}没有对应的许可证信息！", systemSerialNumber);
            TpConstant.securityLicenceMap.put(systemSerialNumber, false);
            return false;
        }

        // 校验许可证是否合法有效
        String hashCode = vo.getHashCode();
        String expiringDate = vo.getExpiringDate();
        if (StrUtil.isBlank(expiringDate)) {
            // 过期日期为空，没有上传合法许可证
            LOGGER.error("序列号systemSerialNumber: {}没有上传合法许可证！", systemSerialNumber);
            TpConstant.securityLicenceMap.put(systemSerialNumber, false);
            return false;
        }

        // 许可证hashCode值
        String thisHashCode = SmUtils.digestHexSM3(vo.getSystemSerialNumber() + vo.getExpiringDate());
        if (!StrUtil.equals(hashCode, thisHashCode)) {
            // hashCode不一致，许可证被篡改
            LOGGER.error("许可证被篡改，无效！");
            TpConstant.securityLicenceMap.put(systemSerialNumber, false);
            return false;
        }

        // 通过过期日期,判断许可证是否过期,如果已过期,刷新缓存为"null"
        boolean flag = this.isExpire(expiringDate);
        if (flag) {
            // 过期日期不在当前日期后面，已经过期。即 exDate<=now 就说明已经过期
            LOGGER.error("许可证过期，无效！");
            TpConstant.securityLicenceMap.put(systemSerialNumber, false);
            return false;
        }

        TpConstant.securityLicenceMap.put(systemSerialNumber, true);
 
        return true; */
    }

    /**
     * 获取当前服务器的mac地址生成系统序列号
     *
     * @return
     */
    @Override
    public String generatorSystemSerialNumber() {

        String systemSerialNumber = mybatisSerialNumberCache.get(SECURITY_SERIAL_NUMBER_CACHE_KEY);
        if (StrUtil.isBlank(systemSerialNumber)) {
            // 获取本地mac地址
            String macAddress = NetUtil.getLocalMacAddress();

            // sm3生成序列号
            systemSerialNumber = SmUtils.digestHexSM3(macAddress);
            mybatisSerialNumberCache.put(SECURITY_SERIAL_NUMBER_CACHE_KEY, systemSerialNumber);
        }

        return systemSerialNumber;
    }

    /**
     * 判断是否过期
     * <pre>
     *     1. 到期日期小于等于当前日期，都表示过期
     * </pre>
     *
     * @param expiringDate 到期日期
     * @return boolean 过期返回：true
     * @author 杨占锐
     * @date 2023/12/6 9:38
     */
    private boolean isExpire(String expiringDate) {
        LocalDate exDate = LocalDate.parse(expiringDate, dateTimeFormatter);
        LocalDate now = LocalDate.now();
        // exDate > now 说明没有过期
        boolean flag = exDate.isAfter(now);
        // 返回结果取反
        return !flag;
    }

}