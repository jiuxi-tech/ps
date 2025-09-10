package com.jiuxi.security.sso.controller;

import com.jiuxi.common.bean.JsonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试Controller
 * 
 * @author Test
 * @since 2.2.2
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    /**
     * 测试获取用户可访问的Keycloak客户端列表
     * 
     * @return 客户端列表
     */
    @GetMapping("/test_clients")
    public JsonResponse testGetUserClients() {
        try {
            // 直接返回模拟数据用于测试
            List<Map<String, Object>> clients = new ArrayList<>();
            
            // 模拟客户端数据
            Map<String, Object> client1 = new HashMap<>();
            client1.put("id", "ps-be");
            client1.put("clientId", "ps-be");
            client1.put("name", "PS Backend Application");
            client1.put("description", "后端应用客户端");
            client1.put("enabled", true);
            client1.put("publicClient", false);
            clients.add(client1);
            
            Map<String, Object> client2 = new HashMap<>();
            client2.put("id", "ps-fe");
            client2.put("clientId", "ps-fe");
            client2.put("name", "PS Frontend Application");
            client2.put("description", "前端应用客户端");
            client2.put("enabled", true);
            client2.put("publicClient", true);
            clients.add(client2);
            
            Map<String, Object> client3 = new HashMap<>();
            client3.put("id", "ps-mobile");
            client3.put("clientId", "ps-mobile");
            client3.put("name", "PS Mobile Application");
            client3.put("description", "移动端应用客户端");
            client3.put("enabled", true);
            client3.put("publicClient", true);
            clients.add(client3);
            
            return JsonResponse.buildSuccess(clients);
            
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取客户端列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 简单的健康检查接口
     * 
     * @return 健康状态
     */
    @GetMapping("/health")
    public JsonResponse health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", System.currentTimeMillis());
        return JsonResponse.buildSuccess(data);
    }
}