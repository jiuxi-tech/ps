package com.jiuxi.monitor.server.core.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.monitor.server.bean.ClientHeartbeatInfo;
import com.jiuxi.monitor.server.constant.MonitorServerConstant;
import com.jiuxi.monitor.server.core.bean.vo.AlarmSource;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorClientVO;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO;
import com.jiuxi.monitor.server.core.service.*;
import com.jiuxi.monitor.server.core.util.MonitorIpUtil;
import com.jiuxi.monitor.server.core.util.MonitorMD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName: MonitorServerServiceImpl
 * @Description: 监控服务端接口
 * @Author 杨占锐
 * @Date 2024/11/18 9:49
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service
public class MonitorServerServiceImpl implements MonitorServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorServerServiceImpl.class);

    @Autowired
    private MonitorCacheService monitorCacheService;
    @Autowired
    private MonitorAlarmService monitorAlarmService;
    @Autowired
    private TpMonitorClientService tpMonitorClientService;
    @Autowired
    private MonitorSendMailService monitorSendMailService;

    /**
     * 处理接收到的心跳信息
     * <per>
     * 1. 生成clientId
     * 2. 查询是否已经存在, 不存在则新增
     * 3. 添加心跳信息到缓存
     * 4. 计算报警的资源
     * 5. 发送邮件
     * </per>
     *
     * @param heartbeatInfo
     * @param request
     * @return void
     * @author 杨占锐
     * @date 2024/11/18 9:51
     */
    @Override
    public void handleHeartbeat(ClientHeartbeatInfo heartbeatInfo, HttpServletRequest request) {
        LOGGER.info("[监控服务端] 开始处理客户端心跳信息");
        LOGGER.debug("[监控服务端] 心跳信息: applicationId={}, applicationName={}", 
                heartbeatInfo.getApplicationId(), heartbeatInfo.getApplicationName());

        // 获取ip
        String ip = MonitorIpUtil.getIpAddr(request);
        LOGGER.debug("[监控服务端] 客户端IP地址: {}", ip);

        // 生成唯一id
        this.createClientId(heartbeatInfo);
        LOGGER.debug("[监控服务端] 生成客户端ID: {}", heartbeatInfo.getClientId());

        // 查询基本信息是否存在，不存在则新增信息
        TpMonitorClientVO clientVO = monitorCacheService.getClientBaseInfo(heartbeatInfo.getClientId());
        if (clientVO == null) {
            LOGGER.info("[监控服务端] 客户端不存在，新增客户端信息: {}", heartbeatInfo.getClientId());
            tpMonitorClientService.add(heartbeatInfo, ip);
        } else {
            LOGGER.debug("[监控服务端] 客户端已存在: {}", heartbeatInfo.getClientId());
        }

        // 添加心跳信息到缓存
        LOGGER.debug("[监控服务端] 将心跳信息添加到缓存");
        monitorCacheService.setHeartbeat(heartbeatInfo);

        if (null != clientVO) {
            // 计算报警的资源
            LOGGER.debug("[监控服务端] 开始计算报警事件");
            AlarmSource alarmSource = this.computeAlarmEvent(heartbeatInfo);
            // 发送邮件
            LOGGER.debug("[监控服务端] 检查是否需要发送报警邮件");
            this.sendMail(clientVO, alarmSource);
        }
        
        LOGGER.info("[监控服务端] 心跳处理完成: {}", heartbeatInfo.getClientId());
    }

    /**
     * 离线报警
     *
     * @param clientVO 客户端信息
     * @param configVO 配置信息
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 15:13
     */
    @Override
    public void offlineAlarm(TpMonitorClientVO clientVO, TpMonitorConfigVO configVO) {
        boolean isSendMail = monitorAlarmService.isSendMail(clientVO.getClientId(), MonitorServerConstant.OFF_LINE);
        if (isSendMail) {
            return;
        }
        AlarmSource alarmSource = new AlarmSource();
        alarmSource.setAlarmList(Arrays.asList(MonitorServerConstant.OFF_LINE));
        this.sendMail(clientVO, alarmSource);
    }

    /**
     * 发送邮件
     *
     * @param clientVO
     * @param alarmSource
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 13:55
     */
    private void sendMail(TpMonitorClientVO clientVO, AlarmSource alarmSource) {
        List<String> alarmEventList = alarmSource.getAlarmList();
        LOGGER.debug("[监控服务端] 检查报警事件列表，事件数量: {}", 
                CollectionUtil.isEmpty(alarmEventList) ? 0 : alarmEventList.size());
        
        if (CollectionUtil.isEmpty(alarmEventList)) {
            LOGGER.debug("[监控服务端] 无报警事件，跳过邮件发送");
            return;
        }
        
        LOGGER.info("[监控服务端] 发现报警事件，准备发送邮件: {}", alarmEventList);

        // 查询邮件配置
        TpMonitorConfigVO config = monitorCacheService.getConfig();
        if (null == config) {
            LOGGER.warn("[监控服务端] 邮件配置为空，不发送邮件，clientVO：{}, alarmEventList：{}"
                    , JSONObject.toJSONString(clientVO)
                    , JSONObject.toJSONString(alarmEventList));
            return;
        }
        
        LOGGER.debug("[监控服务端] 邮件配置获取成功，收件人: {}", config.getMailTo());

        // 组装邮件内容
        LOGGER.debug("[监控服务端] 开始组装邮件内容");
        String content = this.buildMailContent(clientVO, alarmSource);
        LOGGER.debug("[监控服务端] 邮件内容组装完成，长度: {}", content.length());

        // 发送邮件，可能发送给多个人
        LOGGER.info("[监控服务端] 开始发送报警邮件到: {}", config.getMailTo());
        boolean result = monitorSendMailService.sendMail(config, content);
        LOGGER.info("[监控服务端] 邮件发送结果: {}", result ? "成功" : "失败");

        // 记录已发送邮件到缓存
        LOGGER.debug("[监控服务端] 更新报警缓存状态");
        for (String source : alarmEventList) {
            if (result) {
                // 发送成功时，添加已发送邮件缓存，12小时内不再发送邮件。发送失败，则5分钟后会再次发送
                LOGGER.debug("[监控服务端] 添加已发送邮件缓存: clientId={}, source={}", clientVO.getClientId(), source);
                monitorAlarmService.addSendMailCache(clientVO.getClientId(), source);
            }
            // 无论发送成功或失败，都删除报警信息缓存。
            LOGGER.debug("[监控服务端] 删除报警信息缓存: clientId={}, source={}", clientVO.getClientId(), source);
            monitorAlarmService.deleteAlarmCache(clientVO.getClientId(), source);
        }

    }

    /**
     * 组装邮件内容
     *
     * @param clientVO
     * @param alarmSource
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/11/20 16:22
     */
    private String buildMailContent(TpMonitorClientVO clientVO, AlarmSource alarmSource) {

        List<String> alarmEventList = alarmSource.getAlarmList();
        Map<String, String> alarmMap = alarmSource.getAlarmMap();
        StringBuilder builder = new StringBuilder();
        builder.append("【")
                .append(clientVO.getApplicationName())
                .append("】")
                .append(clientVO.getSystemDesc())
                .append("：");
        if (StrUtil.isNotBlank(clientVO.getRemark())) {
            builder.append(clientVO.getRemark())
                    .append(",");
        }

        for (int i = 0; i < alarmEventList.size(); i++) {
            String source = alarmEventList.get(i);
            builder.append(MonitorServerConstant.SOURCE_MAP.get(source));
            String val = alarmMap.get(source);
            if (StrUtil.isNotBlank(val)) {
                builder.append("，当前值：")
                       .append(val)
                       .append("%");
            }
            if (i < alarmEventList.size() - 1) {
                builder.append("；");
            } else {
                builder.append("。");
            }
        }
        builder.append("请及时处理！");

        return builder.toString();
    }

    /**
     * 计算产生报警事件的资源
     *
     * @param heartbeatInfo 心跳信息
     * @return void
     * @author 杨占锐
     * @date 2024/11/18 14:01
     */
    private AlarmSource computeAlarmEvent(ClientHeartbeatInfo heartbeatInfo) {
        // 报警资源
        AlarmSource alarmBean = new AlarmSource();

        // 需要发送邮件的报警资源
        List<String> alarmList = new ArrayList<>();

        // 查询配置信息
        TpMonitorConfigVO configVO = monitorCacheService.getConfig();
        if (null == configVO) {
            return alarmBean;
        }

        // 超阈值(不健康)的资源
        Map<String, String> map = monitorAlarmService.getAlarmSource(heartbeatInfo);

        Set<String> alarmSource = new HashSet<>(map.keySet());
        if (null != heartbeatInfo.getNotHealthServer()) {
            alarmSource.addAll(heartbeatInfo.getNotHealthServer());
        }

        // 逐个计算是否达到报警算法条件
        for (String source : MonitorServerConstant.SOURCE_LIST) {
            boolean alarm = monitorAlarmService.computeAlarm(heartbeatInfo.getClientId(), source, alarmSource);
            if (alarm) {
                alarmList.add(source);
            }
        }

        alarmBean.setAlarmList(alarmList);
        alarmBean.setAlarmMap(map);
        return alarmBean;
    }

    /**
     * 生成唯一id
     * <pre>
     *     1. clientId 不为空时，直接使用
     *     2. clientId 为空时，绝对路径 + mac地址 + 系统名称，生成 md5
     * </pre>
     *
     * @param info
     * @return void
     * @author 杨占锐
     * @date 2024/11/18 10:08
     */
    private void createClientId(ClientHeartbeatInfo info) {

        String clientId = info.getClientId();
        if (StrUtil.isBlank(clientId)) {
            clientId = MonitorMD5Util.createMd5(info.getAbsolutePath() + info.getMacAddr() + info.getApplicationName());
            if (StrUtil.isBlank(clientId)) {
                // 兜底方案，应该不会执行到
                clientId = info.getMacAddr();
            }
        }
        if (clientId.length() > 36) {
            clientId = clientId.substring(0, 36);
        }
        info.setClientId(clientId);
    }
}
