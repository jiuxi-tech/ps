package com.jiuxi.monitor.server.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.common.server.bean.Cpu;
import com.jiuxi.common.server.bean.Jvm;
import com.jiuxi.common.server.bean.Memory;
import com.jiuxi.common.server.bean.SysFile;
import com.jiuxi.common.util.CommonArithmeticUtil;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.monitor.server.bean.ClientHeartbeatInfo;
import com.jiuxi.monitor.server.constant.MonitorServerConstant;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO;
import com.jiuxi.monitor.server.core.service.MonitorAlarmService;
import com.jiuxi.monitor.server.core.service.MonitorCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: MonitorAlarmServiceImpl
 * @Description:
 * @Author jiuxx
 * @Date 2024/11/18 14:10
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service
public class MonitorAlarmServiceImpl implements MonitorAlarmService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MonitorCacheService monitorCacheService;

    /**
     * 报警次数 key
     */
    private static final String ALARM_KEY = "alarm";

    /**
     * 首次报警时间 key
     */
    private static final String FIRST_ALARM_KEY = "first_alarm";

    /**
     * 不报警次数 key
     */
    private static final String NOT_ALARM_KEY = "not_alarm";

    /**
     * 判断是否已经发送了邮件
     *
     * @param clientId
     * @param source
     * @return boolean
     * @author jiuxx
     * @date 2024/11/19 15:16
     */
    @Override
    public boolean isSendMail(String clientId, String source) {
        String val = redisTemplate.opsForValue().get(getSendMailCacheKey(clientId, source));
        return StrUtil.isNotBlank(val);
    }

    /**
     * 添加已经发送邮件的缓存
     *
     * @param clientId
     * @param source
     * @return void
     * @author jiuxx
     * @date 2024/11/19 15:30
     */
    @Override
    public void addSendMailCache(String clientId, String source) {
        redisTemplate.opsForValue().set(getSendMailCacheKey(clientId, source), CommonDateUtil.now());
        redisTemplate.expire(getSendMailCacheKey(clientId, source), MonitorServerConstant.CACHE_HOURS, TimeUnit.HOURS);
    }

    /**
     * 计算是否达到报警条件
     *
     * <pre>
     *     持续5分钟故障频率大于90%报警算法
     *     1. 判断是否超过阈值，超则超过的次数+1，返回1时，存入首次报警时间
     *     2. 未超阈值时，查询是否有报警，有则存入不报警缓存，次数加1, 无则结束处理
     *     3. 如果首次报警时间距离当前时间大于10分钟，则取出报警次数和不报警次数，故障频率大于90%，则返回true
     * </pre>
     *
     * @param clientId    客户端id
     * @param source      资源名称
     * @param alarmSource 超阈值的资源
     * @return boolean   报警返回true
     * @author jiuxx
     * @date 2024/11/18 14:14
     */
    @Override
    public boolean computeAlarm(String clientId, String source, Set<String> alarmSource) {

        // 已经发送邮件，返回false
        if (isSendMail(clientId, source)) {
            return false;
        }

        // 心跳报警信息缓存key
        String key = getAlarmInfoCacheKey(clientId, source);

        // 超阈值时
        if (alarmSource.contains(source)) {
            // 取出报警次数
            Long alarmTimes = redisTemplate.opsForHash().increment(key, ALARM_KEY, 1);
            if (alarmTimes.intValue() <= 1) {
                // 报警次数小于等于1时，存入报警时间
                redisTemplate.opsForHash().put(key, FIRST_ALARM_KEY, CommonDateUtil.now());
            } else {
                // 报警次数大于1时，计算是否达到报警条件
                Object firstTime = redisTemplate.opsForHash().get(key, FIRST_ALARM_KEY);
                if (null != firstTime) {
                    LocalDateTime first = CommonDateUtil.parse(String.valueOf(firstTime));
                    // 当前时间已经超过第一次报警时间10分钟
                    if (LocalDateTime.now().isAfter(first.plusMinutes(MonitorServerConstant.CONTINUE_MINUTE))) {
                        // 取出不报警次数
                        Object notAlarmTimes = redisTemplate.opsForHash().get(key, NOT_ALARM_KEY);
                        // 不报警次数为空，直接返回true
                        if (null == notAlarmTimes) {
                            return true;
                        }
                        // 不报警次数
                        long notTimes = Long.parseLong(String.valueOf(notAlarmTimes));
                        // 报警次数 / （报警次数 + 不报警次数）
                        BigDecimal divide = CommonArithmeticUtil.div(String.valueOf(alarmTimes), String.valueOf(notTimes + alarmTimes), 2);
                        if (divide.doubleValue() > 0.9) {
                            return true;
                        }
                    }
                }
            }
        } else {
            Object alarmVal = redisTemplate.opsForHash().get(key, ALARM_KEY);
            if (null != alarmVal) {
                redisTemplate.opsForHash().increment(key, NOT_ALARM_KEY, 1);
            }
        }

        return false;
    }

    /**
     * 获取报警(超阈值)的资源信息
     *
     * @param heartbeatInfo
     * @return java.lang.String
     * @author jiuxx
     * @date 2024/11/19 18:08
     */
    @Override
    public Map<String, String> getAlarmSource(ClientHeartbeatInfo heartbeatInfo) {

        // 存储所有报警的资源
        Map<String, String> map = new HashMap<>();

        // 查询配置信息
        TpMonitorConfigVO configVO = monitorCacheService.getConfig();
        if (null == configVO) {
            return map;
        }

        // cpu
        BigDecimal cpuThreshold = configVO.getCpuThreshold();
        if (null != cpuThreshold) {
            Cpu cpu = heartbeatInfo.getCpu();
            if (cpu.getSys() > cpuThreshold.doubleValue()) {
                map.put(MonitorServerConstant.CPU, String.valueOf(cpu.getSys()));
            }
        }

        // 内存
        BigDecimal memoryThreshold = configVO.getMemoryThreshold();
        if (null != memoryThreshold) {
            Memory mem = heartbeatInfo.getMem();
            Jvm jvm = heartbeatInfo.getJvm();
            double usage = Math.max(mem.getUsage(), jvm.getUsage());
            if (usage > memoryThreshold.doubleValue()) {
                map.put(MonitorServerConstant.MEMORY, String.valueOf(usage));
            }
        }

        // 硬盘
        BigDecimal diskThreshold = configVO.getDiskThreshold();
        if (null != diskThreshold) {
            List<SysFile> sysFiles = heartbeatInfo.getSysFiles();
            double usage = 0.0;
            for (SysFile sysFile : sysFiles) {
                usage = Math.max(usage, sysFile.getUsage());
            }
            if (usage > diskThreshold.doubleValue()) {
                map.put(MonitorServerConstant.DISK, String.valueOf(usage));
            }
        }

        return map;
    }

    /**
     * 删除报警信息缓存
     *
     * @param clientId 客户端id
     * @param source   报警资源 如：cpu
     * @return void
     * @author jiuxx
     * @date 2024/11/27 16:28
     */
    @Override
    public void deleteAlarmCache(String clientId, String source) {
        String alarmInfoCacheKey = getAlarmInfoCacheKey(clientId, source);
        redisTemplate.delete(alarmInfoCacheKey);
    }

    /**
     * 获取报警信息缓存key
     *
     * @param clientId
     * @return java.lang.String
     * @author jiuxx
     * @date 2024/11/19 15:32
     */
    private String getAlarmInfoCacheKey(String clientId, String source) {
        return MonitorServerConstant.HEARTBEAT_ALARM_KEY + clientId + ":" + source;
    }


    /**
     * 获取发送邮件的缓存key
     *
     * @param clientId
     * @param source
     * @return java.lang.String
     * @author jiuxx
     * @date 2024/11/19 15:32
     */
    private String getSendMailCacheKey(String clientId, String source) {
        return MonitorServerConstant.SEND_MAIL_KEY + clientId + ":" + source;
    }


}
