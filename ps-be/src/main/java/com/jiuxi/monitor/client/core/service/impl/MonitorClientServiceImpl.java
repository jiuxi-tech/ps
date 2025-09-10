package com.jiuxi.monitor.client.core.service.impl;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonServerInfoUtil;
import com.jiuxi.monitor.client.autoconfig.MonitorClientAutoConfigurationProperties;
import com.jiuxi.monitor.client.constant.MonitorClientConstant;
import com.jiuxi.monitor.client.core.service.MonitorClientService;
import com.jiuxi.monitor.client.core.util.MacAddressUtil;
import com.jiuxi.monitor.common.bean.ClientHeartbeatInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;


/**
 * @ClassName: MonitorClientServiceImpl
 * @Description:
 * @Author 杨占锐
 * @Date 2024/11/14 17:44
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service
@ConditionalOnProperty(prefix = "jiuxi.platform.plugin.monitor", name = "server-url")
public class MonitorClientServiceImpl implements MonitorClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorClientServiceImpl.class);

    @Autowired(required = false)
    private MonitorClientAutoConfigurationProperties properties;
    @Autowired(required = false)
    private MonitorHealthServiceComposite healthServiceComposite;

    @Value("${spring.application.id:}")
    private String applicationId;
    @Value("${spring.application.name:}")
    private String applicationName;

    /**
     * 发送心跳
     *
     * @param
     * @return void
     * @author 杨占锐
     * @date 2024/11/14 18:24
     */
    @Override
    public void heartbeat() {
        LOGGER.info("[监控客户端] 开始执行心跳发送...");
        
        // 检查配置
        if (properties == null) {
            LOGGER.error("[监控客户端] 监控配置为空，无法发送心跳");
            return;
        }
        
        LOGGER.debug("[监控客户端] 监控服务器地址: {}", properties.getServerUrl());
        LOGGER.debug("[监控客户端] 客户端ID: {}", properties.getClientId());
        
        // 构建心跳
        ClientHeartbeatInfo serverInfo = buildServerInfo();

        // 发送心跳
        if (null != serverInfo) {
            LOGGER.info("[监控客户端] 心跳信息构建成功，准备发送心跳");
            sendHeartbeat(serverInfo);
        } else {
            LOGGER.error("[监控客户端] 心跳信息构建失败，无法发送心跳");
        }
    }

    /**
     * 发送心跳
     *
     * @param serverInfo
     * @return void
     * @author 杨占锐
     * @date 2024/11/14 18:24
     */
    private void sendHeartbeat(ClientHeartbeatInfo serverInfo) {
        // 构建完整的心跳接口URL
        String heartbeatUrl = properties.getServerUrl() + "/sys/monitor/heartbeat";
        LOGGER.info("[监控客户端] 准备发送心跳到服务器: {}", heartbeatUrl);
        LOGGER.debug("[监控客户端] 心跳数据: {}", JSONObject.toJSONString(serverInfo));
        
        try {
            // 发送心跳
            LOGGER.debug("[监控客户端] 连接超时: {}ms, 读取超时: {}ms", 
                properties.getConnectionTimeout(), properties.getReadTimeout());
                
            String response = HttpUtil.createPost(heartbeatUrl)
                    .header("token", MonitorClientConstant.TOKEN)
                    .body(JSONObject.toJSONString(serverInfo))
                    .setConnectionTimeout(properties.getConnectionTimeout())
                    .setReadTimeout(properties.getReadTimeout())
                    .execute()
                    .body();
                    
            if (StrUtil.isNotBlank(response)) {
                LOGGER.info("[监控客户端] HTTP发送心跳成功, response: {}", response);
            } else {
                LOGGER.error("[监控客户端] HTTP发送心跳失败, response为空");
            }
        } catch (Exception e) {
            LOGGER.error("[监控客户端] HTTP发送心跳失败，错误：{}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 构建心跳信息
     *
     * @return com.jiuxi.monitor.client.core.bean.ClientServerInfo
     * @author 杨占锐
     * @date 2024/11/14 18:24
     */
    public ClientHeartbeatInfo buildServerInfo() {
        LOGGER.debug("[监控客户端] 开始构建心跳信息...");
        
        try {
            ClientHeartbeatInfo serverInfo = new ClientHeartbeatInfo();
            
            // 服务器信息
            LOGGER.debug("[监控客户端] 构建服务器基础信息...");
            CommonServerInfoUtil.buildServerInfo(serverInfo);
            
            // mac地址
            String macAddr = MacAddressUtil.getMacAddr();
            serverInfo.setMacAddr(macAddr);
            LOGGER.debug("[监控客户端] MAC地址: {}", macAddr);
            
            // 部署路径
            String absolutePath = this.getAbsolutePath();
            serverInfo.setAbsolutePath(absolutePath);
            LOGGER.debug("[监控客户端] 部署路径: {}", absolutePath);
            
            // 客户端id
            serverInfo.setClientId(properties.getClientId());
            LOGGER.debug("[监控客户端] 客户端ID: {}", properties.getClientId());
            
            // 系统描述
            serverInfo.setSystemDesc(properties.getSystemDesc());
            LOGGER.debug("[监控客户端] 系统描述: {}", properties.getSystemDesc());
            
            // applicationId
            serverInfo.setApplicationId(applicationId);
            LOGGER.debug("[监控客户端] 应用ID: {}", applicationId);
            
            // applicationName
            serverInfo.setApplicationName(applicationName);
            LOGGER.debug("[监控客户端] 应用名称: {}", applicationName);
            
            // 不健康的服务
            // 检查healthServiceComposite是否为null，避免循环依赖导致的空指针异常
            if (healthServiceComposite != null) {
                List<String> notHealthServer = healthServiceComposite.getNotHealthServer();
                serverInfo.setNotHealthServer(notHealthServer);
                LOGGER.debug("[监控客户端] 不健康服务: {}", notHealthServer);
            } else {
                LOGGER.debug("[监控客户端] 健康检查服务未初始化");
            }
            
            LOGGER.info("[监控客户端] 心跳信息构建完成");
            return serverInfo;
        } catch (Exception e) {
            LOGGER.error("[监控客户端] 构建心跳信息失败，错误：{}，properties：{}", ExceptionUtils.getStackTrace(e), JSONObject.toJSONString(properties));
        }

        return null;
    }

    /**
     * 获取项目路径
     *
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/12/18 14:37
     */
    private String getAbsolutePath() {
        URL resource = ClassLoaderUtil.getClassLoader().getResource("");
        if (null == resource) {
            return System.getProperty("user.dir");
        }
        String path = resource.getPath();
        String webInfo = "WEB-INF";
        if (path.contains(webInfo)) {
            path = path.substring(0, path.indexOf(webInfo));
        }
        String classes = "classes";
        if (path.contains(classes)) {
            path = path.substring(0, path.indexOf(classes));
        }
        return path;
    }

}
