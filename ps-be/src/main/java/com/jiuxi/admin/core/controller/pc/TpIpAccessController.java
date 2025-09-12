package com.jiuxi.admin.core.controller.pc;

import com.jiuxi.admin.core.service.TpSystemConfigService;
import com.jiuxi.admin.core.service.TpIpAccessLogService;
import com.jiuxi.admin.core.bean.entity.TpIpAccessLog;
import com.jiuxi.admin.core.bean.query.TpIpAccessLogQuery;
import com.jiuxi.admin.core.bean.vo.TpIpAccessLogVO;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.config.cache.IpAccessConfigCache;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IP访问控制管理Controller
 * 提供IP访问控制的配置管理和日志查询功能
 * 
 * @author system
 * @date 2025-01-20
 */
@RestController
@RequestMapping("/sys/ipaccess")
@Authorization
public class TpIpAccessController {

    @Autowired
    private TpSystemConfigService tpSystemConfigService;
    
    @Autowired
    private TpIpAccessLogService tpIpAccessLogService;
    
    @Autowired
    private IpAccessConfigCache ipAccessConfigCache;
    
    // 配置键常量
    private static final String IP_ENABLED_KEY = "security.ip.enabled";
    private static final String IP_WHITELIST_KEY = "security.ip.whitelist";
    private static final String IP_BLACKLIST_KEY = "security.ip.blacklist";
    private static final String IP_LOG_ENABLED_KEY = "security.ip.log.enabled";
    private static final String IP_DENY_MESSAGE_KEY = "security.ip.deny.message";

    /**
     * 获取IP访问控制配置
     */
    @GetMapping("/get")
    public JsonResponse getConfig() {
        Map<String, Object> config = new HashMap<>();
        
        // 获取配置值
        String enabled = tpSystemConfigService.getConfigValue(IP_ENABLED_KEY, "false");
        String whitelist = tpSystemConfigService.getConfigValue(IP_WHITELIST_KEY, "");
        String blacklist = tpSystemConfigService.getConfigValue(IP_BLACKLIST_KEY, "");
        String logEnabled = tpSystemConfigService.getConfigValue(IP_LOG_ENABLED_KEY, "true");
        String denyMessage = tpSystemConfigService.getConfigValue(IP_DENY_MESSAGE_KEY, "访问被拒绝，您的IP地址不在允许范围内");
        
        config.put("enabled", Boolean.parseBoolean(enabled));
        config.put("whitelist", whitelist);
        config.put("blacklist", blacklist);
        config.put("logEnabled", Boolean.parseBoolean(logEnabled));
        config.put("denyMessage", denyMessage);
        
        return JsonResponse.buildSuccess(config);
    }

    /**
     * 更新IP访问控制配置
     */
    @PostMapping("/update")
    public JsonResponse updateConfig(@RequestBody Map<String, Object> configMap) {
        try {
            // 更新配置
            if (configMap.containsKey("enabled")) {
                tpSystemConfigService.setConfigValue(IP_ENABLED_KEY, 
                    String.valueOf(configMap.get("enabled")), "IP访问控制开关");
            }
            
            if (configMap.containsKey("whitelist")) {
                tpSystemConfigService.setConfigValue(IP_WHITELIST_KEY, 
                    String.valueOf(configMap.get("whitelist")), "IP白名单");
            }
            
            if (configMap.containsKey("blacklist")) {
                tpSystemConfigService.setConfigValue(IP_BLACKLIST_KEY, 
                    String.valueOf(configMap.get("blacklist")), "IP黑名单");
            }
            
            if (configMap.containsKey("logEnabled")) {
                tpSystemConfigService.setConfigValue(IP_LOG_ENABLED_KEY, 
                    String.valueOf(configMap.get("logEnabled")), "IP访问日志开关");
            }
            
            if (configMap.containsKey("denyMessage")) {
                tpSystemConfigService.setConfigValue(IP_DENY_MESSAGE_KEY, 
                    String.valueOf(configMap.get("denyMessage")), "IP访问拒绝消息");
            }
            
            // 刷新缓存
            ipAccessConfigCache.refreshCache();
            
            return JsonResponse.buildSuccess("配置更新成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("配置更新失败: " + e.getMessage());
        }
    }

    /**
     * 测试IP地址
     */
    @PostMapping("/test")
    public JsonResponse testIp(@RequestBody Map<String, String> params) {
        String testIp = params.get("ip");
        
        if (testIp == null || testIp.trim().isEmpty()) {
            return JsonResponse.buildFailure("请输入要测试的IP地址");
        }
        
        try {
            // 获取IP匹配结果
            IpAccessConfigCache.IpMatchResult result = ipAccessConfigCache.getIpMatchResult(testIp);
            
            Map<String, Object> response = new HashMap<>();
            response.put("ip", testIp);
            response.put("allowed", result.isAllowed());
            response.put("reason", result.getReason());
            
            return JsonResponse.buildSuccess(response);
        } catch (Exception e) {
            return JsonResponse.buildFailure("IP测试失败: " + e.getMessage());
        }
    }

    /**
     * 获取IP访问日志
     */
    @GetMapping("/logs")
    public JsonResponse getLogs(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "20") int size,
                               @RequestParam(required = false) String ip,
                               @RequestParam(required = false) String result) {
        try {
            // 构建查询条件
            TpIpAccessLogQuery query = new TpIpAccessLogQuery();
            query.setCurrent(page);
            query.setSize(size);
            query.setClientIp(ip);
            query.setAccessResult(result);
            
            // 使用接口中定义的方法
            IPage<TpIpAccessLogVO> pageResult = tpIpAccessLogService.queryPage(query);
            return JsonResponse.buildSuccess(pageResult);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取日志失败: " + e.getMessage());
        }
    }

    /**
     * 获取日志详情
     */
    @GetMapping("/log-detail")
    public JsonResponse getLogDetail(@RequestParam String logId) {
        try {
            TpIpAccessLogVO log = tpIpAccessLogService.view(logId);
            return JsonResponse.buildSuccess(log);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取日志详情失败: " + e.getMessage());
        }
    }

    /**
     * 清理历史日志
     */
    @PostMapping("/clean-logs")
    public JsonResponse cleanLogs(@RequestBody Map<String, Object> params) {
        try {
            Integer days = (Integer) params.get("days");
            if (days == null || days <= 0) {
                return JsonResponse.buildFailure("请输入有效的天数");
            }
            
            // 清理指定天数之前的日志
            int cleanedCount = tpIpAccessLogService.cleanExpiredLogs(days);
            
            return JsonResponse.buildSuccess("成功清理 " + cleanedCount + " 条历史日志");
        } catch (Exception e) {
            return JsonResponse.buildFailure("清理日志失败: " + e.getMessage());
        }
    }

    /**
     * 刷新IP访问控制缓存
     */
    @PostMapping("/refresh-cache")
    public JsonResponse refreshCache() {
        try {
            ipAccessConfigCache.refreshCache();
            return JsonResponse.buildSuccess("缓存刷新成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("缓存刷新失败: " + e.getMessage());
        }
    }
}