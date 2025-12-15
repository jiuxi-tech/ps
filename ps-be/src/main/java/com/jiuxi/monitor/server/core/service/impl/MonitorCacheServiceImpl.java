package com.jiuxi.monitor.server.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.monitor.server.bean.ClientHeartbeatInfo;
import com.jiuxi.monitor.server.constant.MonitorServerConstant;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorClientVO;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO;
import com.jiuxi.monitor.server.core.service.MonitorCacheService;
import com.jiuxi.monitor.server.core.service.MonitorRedisCacheService;
import com.jiuxi.monitor.server.core.service.TpMonitorClientService;
import com.jiuxi.monitor.server.core.service.TpMonitorConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @ClassName: MonitorCacheServiceImpl
 * @Description:
 * @Author jiuxx
 * @Date 2024/11/18 10:13
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service
public class MonitorCacheServiceImpl implements MonitorCacheService {

    @Autowired
    private MonitorRedisCacheService monitorRedisCacheService;
    @Autowired
    private TpMonitorConfigService tpMonitorConfigService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TpMonitorClientService tpMonitorClientService;

    /**
     * 查询客户端基本信息
     *
     * @param clientId
     * @return com.jiuxi.monitor.server.bean.ClientBaseInfo
     * @author jiuxx
     * @date 2024/11/19 13:28
     */
    @Override
    public TpMonitorClientVO getClientBaseInfo(String clientId) {

        String data = monitorRedisCacheService.getData(MonitorServerConstant.BASEINFO_KEY, clientId, MonitorServerConstant.CACHE_DAYS, () -> {
            TpMonitorClientVO clientVO = tpMonitorClientService.view(clientId);
            return clientVO;
        });

        if (null != data) {
            return JSONObject.parseObject(data, TpMonitorClientVO.class);
        }
        return null;
    }

    /**
     * 清空客户端基本信息缓存
     *
     * @param clientId
     * @return com.jiuxi.monitor.server.bean.ClientBaseInfo
     * @author jiuxx
     * @date 2024/11/19 13:28
     */
    @Override
    public void clearClientInfo(String clientId) {
        redisTemplate.delete(MonitorServerConstant.BASEINFO_KEY + clientId);
    }

    /**
     * 添加心跳信息到缓存中
     *
     * @param info
     * @return void
     * @author jiuxx
     * @date 2024/11/19 13:30
     */
    @Override
    public void setHeartbeat(ClientHeartbeatInfo info) {
        info.setLastUpdateTime(CommonDateUtil.now());
        redisTemplate.opsForValue().set(MonitorServerConstant.HEARTBEAT_KEY + info.getClientId(), JSONObject.toJSONString(info));
    }

    /**
     * 获取心跳信息
     *
     * @param clientId
     * @return com.jiuxi.monitor.server.bean.ClientHeartbeatInfo
     * @author jiuxx
     * @date 2024/11/19 15:51
     */
    @Override
    public ClientHeartbeatInfo getHeartbeat(String clientId) {
        String val = redisTemplate.opsForValue().get(MonitorServerConstant.HEARTBEAT_KEY + clientId);
        if (StrUtil.isBlank(val)) {
            return null;
        }
        return JSONObject.parseObject(val, ClientHeartbeatInfo.class);
    }

    /**
     * 查询服务资源报警配置信息
     *
     * @return void
     * @author jiuxx
     * @date 2024/11/19 13:30
     */
    @Override
    public TpMonitorConfigVO getConfig() {
        String data = monitorRedisCacheService.getData(MonitorServerConstant.ALARM_CONFIG_KEY, "", MonitorServerConstant.CACHE_DAYS, () -> {
            // 查询唯一一条的配置信息
            TpMonitorConfigVO vo = tpMonitorConfigService.getOne();
            return vo;
        });

        if (null != data) {
            TpMonitorConfigVO configVO = JSONObject.parseObject(data, TpMonitorConfigVO.class);
            if (null == configVO) {
                return null;
            }
            if (TpConstant.YES != configVO.getSendMail()) {
                // 未启用发送邮件
                return null;
            }
            if (StrUtil.isBlank(configVO.getEmail())) {
                // 未配置邮件地址
                return null;
            }
            if (StrUtil.isBlank(configVO.getPrincipal())) {
                // 未配置负责人
                return null;
            }
            return configVO;
        }
        return null;
    }

    /**
     * 删除服务资源报警配置信息
     *
     * @return void
     * @author jiuxx
     * @date 2024/11/19 13:30
     */
    @Override
    public void clearAlarmConfig() {
        redisTemplate.delete(MonitorServerConstant.ALARM_CONFIG_KEY);
    }
}
