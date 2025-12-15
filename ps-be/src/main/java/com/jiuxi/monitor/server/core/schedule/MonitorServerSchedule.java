package com.jiuxi.monitor.server.core.schedule;

import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.monitor.server.bean.ClientHeartbeatInfo;
import com.jiuxi.monitor.server.constant.MonitorServerConstant;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorClientVO;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO;
import com.jiuxi.monitor.server.core.service.MonitorCacheService;
import com.jiuxi.monitor.server.core.service.MonitorServerService;
import com.jiuxi.monitor.server.core.service.TpMonitorClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: MonitorServerSchedule
 * @Description:
 * @Author jiuxx
 * @Date 2024/11/19 14:38
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Component
public class MonitorServerSchedule {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorServerSchedule.class);

    @Autowired
    private MonitorCacheService monitorCacheService;
    @Autowired
    private TpMonitorClientService tpMonitorClientService;
    @Autowired
    private MonitorServerService monitorServerService;

    @PostConstruct
    public void init() {
        LOGGER.info("[监控服务端] MonitorServerSchedule 定时任务调度器初始化完成");
        LOGGER.info("[监控服务端] 离线检测定时任务将在5分钟后开始执行，执行频率: 每5分钟一次");
    }

    /**
     * 定时发送系统离线消息
     *
     * <pre>
     *     1. 查询阈值配置，为空则结束
     *     2. 查询所有客户端
     *     3. 计算是否离线，离线则发送邮件
     * </pre>
     * @return void
     * @author jiuxx
     * @date 2024/11/19 14:39
     */
    @Scheduled(initialDelay = 5 * 60 * 1000, fixedDelay = 5 * 60 * 1000)
    public void offline() {
        try {
            LOGGER.debug("[监控服务端] 开始执行离线检测定时任务");

            // 1. 查询阈值配置，为空则结束
            TpMonitorConfigVO configVO = monitorCacheService.getConfig();
            if (null == configVO){
                LOGGER.info("[监控服务端] 未配置阈值信息，定时任务不再处理");
                return;
            }
            
            LOGGER.debug("[监控服务端] 监控配置获取成功，离线阈值: {}分钟", configVO.getOfflineThreshold());

            LOGGER.info("[监控服务端] 定时发送系统离线消息开始扫描... 频率5分钟一次");

            // 2. 查询所有客户端
            List<TpMonitorClientVO> clientVOS = tpMonitorClientService.listAll();
            LOGGER.debug("[监控服务端] 查询到客户端数量: {}", clientVOS.size());
            
            int offlineCount = 0;
            for (TpMonitorClientVO clientVO: clientVOS) {
                // 3. 计算是否离线，离线则发送邮件
                LOGGER.debug("[监控服务端] 检查客户端离线状态: clientId={}, applicationName={}", 
                        clientVO.getClientId(), clientVO.getApplicationName());
                boolean isOffline = this.computeOnOffLine(clientVO, configVO);
                if (isOffline) {
                    offlineCount++;
                }
            }
            
            LOGGER.info("[监控服务端] 离线检测完成，离线客户端数量: {}/{}", offlineCount, clientVOS.size());

        } catch (Exception e) {
            LOGGER.error("[监控服务端] 定时发送系统离线消息失败，错误：{}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 计算是否离线
     *
     * <per>
     *     1. 查询缓存中心跳信息
     *     2. 心跳信息与服务器时间相差5分钟则判定为离线
     *     3. 离线则发送邮件
     * </per>
     * @param clientVO
     * @param configVO
     * @return boolean 是否离线
     * @author jiuxx
     * @date 2024/11/19 14:57
     */
    private boolean computeOnOffLine(TpMonitorClientVO clientVO, TpMonitorConfigVO configVO) {
        try {

            // 1. 查询缓存中心跳信息
            ClientHeartbeatInfo heartbeatInfo = monitorCacheService.getHeartbeat(clientVO.getClientId());
            if (null == heartbeatInfo) {
                // 为空时，不再处理，因为缓存时间是30天，为空可能是手工删除了缓存
                LOGGER.debug("[监控服务端] 客户端心跳信息为空，可能已被手工删除: {}", clientVO.getClientId());
                return false;
            }
            
            LOGGER.debug("[监控服务端] 获取到心跳信息，最后更新时间: {}", heartbeatInfo.getLastUpdateTime());

            // 2. 心跳信息与服务器时间相差5分钟则判定为离线
            LocalDateTime lastUpdateTime = CommonDateUtil.parse(heartbeatInfo.getLastUpdateTime());
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime offlineThreshold = lastUpdateTime.plusMinutes(MonitorServerConstant.OFFLINE_MINUTE);
            
            LOGGER.debug("[监控服务端] 离线检测 - 当前时间: {}, 心跳时间: {}, 离线阈值时间: {}", 
                    now, lastUpdateTime, offlineThreshold);
            
            if (now.isAfter(offlineThreshold)) {
                // 3. 离线则发送邮件
                LOGGER.warn("[监控服务端] 检测到客户端离线: clientId={}, applicationName={}, 最后心跳: {}", 
                        clientVO.getClientId(), clientVO.getApplicationName(), heartbeatInfo.getLastUpdateTime());
                monitorServerService.offlineAlarm(clientVO, configVO);
                return true;
            } else {
                LOGGER.debug("[监控服务端] 客户端在线: clientId={}, applicationName={}", 
                        clientVO.getClientId(), clientVO.getApplicationName());
                return false;
            }

        } catch (Exception e) {
            LOGGER.error("[监控服务端] 定时发送系统离线消息失败，错误：{}", ExceptionUtils.getStackTrace(e));
            return false;
        }

    }
}
