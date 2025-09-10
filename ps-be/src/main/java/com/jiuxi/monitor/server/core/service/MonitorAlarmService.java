package com.jiuxi.monitor.server.core.service;

import com.jiuxi.monitor.server.bean.ClientHeartbeatInfo;

import java.util.Map;
import java.util.Set;

/**
 * @ClassName: MonitorAlarmService
 * @Description: 计算报警服务
 * @Author 杨占锐
 * @Date 2024/11/18 14:09
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface MonitorAlarmService {

    /**
     * 判断是否已经发送了邮件
     *
     * @param clientId
     * @param source
     * @return boolean
     * @author 杨占锐
     * @date 2024/11/19 15:16
     */
    boolean isSendMail(String clientId, String source);

    /**
     * 添加已经发送邮件的缓存
     *
     * @param clientId
     * @param source
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 15:30
     */
    void addSendMailCache(String clientId, String source);

    /**
     * 计算是否达到报警条件
     *
     * @param clientId    客户端id
     * @param source      资源名称
     * @param alarmSource 超阈值的资源
     * @return boolean    报警返回true
     * @author 杨占锐
     * @date 2024/11/18 14:14
     */
    boolean computeAlarm(String clientId, String source, Set<String> alarmSource);

    /**
     * 获取报警(超阈值)的资源信息
     *
     * @param heartbeat
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/11/19 18:08
     */
    Map<String, String> getAlarmSource(ClientHeartbeatInfo heartbeat);

    /**
     * 删除报警信息缓存
     *
     * @param clientId 客户端id
     * @param source   报警资源 如：cpu
     * @return void
     * @author 杨占锐
     * @date 2024/11/27 16:28
     */
    void deleteAlarmCache(String clientId, String source);
}
