package com.jiuxi.platform.monitoring.app.service;

import com.jiuxi.platform.monitoring.domain.valueobject.HealthStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 健康检查服务
 * 负责系统各组件的健康状态检查
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class HealthCheckService {
    
    private final DataSource dataSource;
    private final Map<String, HealthStatus> lastHealthResults;
    private final Map<String, LocalDateTime> lastCheckTimes;
    
    @Autowired
    public HealthCheckService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.lastHealthResults = new ConcurrentHashMap<>();
        this.lastCheckTimes = new ConcurrentHashMap<>();
    }
    
    /**
     * 执行完整的系统健康检查
     */
    public Map<String, Object> performFullHealthCheck() {
        Map<String, Object> result = new HashMap<>();
        List<HealthStatus> componentHealths = new ArrayList<>();
        
        // 数据库健康检查
        HealthStatus dbHealth = checkDatabaseHealth();
        componentHealths.add(dbHealth);
        
        // 磁盘空间检查
        HealthStatus diskHealth = checkDiskSpaceHealth();
        componentHealths.add(diskHealth);
        
        // 内存使用检查
        HealthStatus memoryHealth = checkMemoryHealth();
        componentHealths.add(memoryHealth);
        
        // JVM健康检查
        HealthStatus jvmHealth = checkJvmHealth();
        componentHealths.add(jvmHealth);
        
        // 网络连通性检查
        HealthStatus networkHealth = checkNetworkHealth();
        componentHealths.add(networkHealth);
        
        // 计算整体健康状态
        HealthStatus overallHealth = calculateOverallHealth(componentHealths);
        
        // 构建结果
        result.put("overallStatus", overallHealth.getStatus().name());
        result.put("overallMessage", overallHealth.getMessage());
        result.put("overallLevel", overallHealth.getLevel());
        result.put("checkTime", LocalDateTime.now().toString());
        
        List<Map<String, Object>> components = new ArrayList<>();
        for (HealthStatus health : componentHealths) {
            Map<String, Object> component = new HashMap<>();
            component.put("component", health.getComponent());
            component.put("status", health.getStatus().name());
            component.put("message", health.getMessage());
            component.put("details", health.getDetails());
            component.put("level", health.getLevel());
            components.add(component);
        }
        result.put("components", components);
        
        // 健康统计
        long healthyCount = componentHealths.stream().mapToLong(h -> h.isHealthy() ? 1 : 0).sum();
        long warningCount = componentHealths.stream().mapToLong(h -> h.needsWarning() ? 1 : 0).sum();
        long unhealthyCount = componentHealths.stream().mapToLong(h -> h.isUnhealthy() ? 1 : 0).sum();
        
        result.put("healthyComponents", healthyCount);
        result.put("warningComponents", warningCount);
        result.put("unhealthyComponents", unhealthyCount);
        result.put("totalComponents", componentHealths.size());
        
        return result;
    }
    
    /**
     * 检查数据库健康状态
     */
    public HealthStatus checkDatabaseHealth() {
        String component = "database";
        
        try {
            if (dataSource == null) {
                return HealthStatus.critical(component, "数据源配置缺失", "无法获取数据源实例");
            }
            
            long startTime = System.currentTimeMillis();
            
            try (Connection connection = dataSource.getConnection()) {
                if (connection == null || connection.isClosed()) {
                    return HealthStatus.critical(component, "数据库连接失败", "无法建立数据库连接");
                }
                
                // 执行简单查询测试连接
                try (PreparedStatement stmt = connection.prepareStatement("SELECT 1");
                     ResultSet rs = stmt.executeQuery()) {
                    
                    if (rs.next()) {
                        long responseTime = System.currentTimeMillis() - startTime;
                        String details = String.format("连接成功，响应时间: %dms", responseTime);
                        
                        if (responseTime > 5000) { // 5秒
                            return HealthStatus.warning(component, "数据库响应缓慢", details);
                        } else if (responseTime > 1000) { // 1秒
                            return HealthStatus.warning(component, "数据库响应较慢", details);
                        } else {
                            return HealthStatus.healthy(component, "数据库连接正常");
                        }
                    } else {
                        return HealthStatus.unhealthy(component, "数据库查询异常", "查询未返回结果");
                    }
                }
            }
            
        } catch (Exception e) {
            return HealthStatus.critical(component, "数据库健康检查失败", 
                    "异常信息: " + e.getMessage());
        } finally {
            updateHealthResult(component);
        }
    }
    
    /**
     * 检查磁盘空间健康状态
     */
    public HealthStatus checkDiskSpaceHealth() {
        String component = "diskspace";
        
        try {
            java.io.File[] roots = java.io.File.listRoots();
            double minFreeSpacePercentage = 100.0;
            String criticalDisk = null;
            
            for (java.io.File root : roots) {
                long totalSpace = root.getTotalSpace();
                long freeSpace = root.getFreeSpace();
                
                if (totalSpace > 0) {
                    double freePercentage = (double) freeSpace / totalSpace * 100;
                    if (freePercentage < minFreeSpacePercentage) {
                        minFreeSpacePercentage = freePercentage;
                        criticalDisk = root.getAbsolutePath();
                    }
                }
            }
            
            String details = String.format("最低可用空间: %.2f%% (%s)", minFreeSpacePercentage, criticalDisk);
            
            if (minFreeSpacePercentage < 5.0) {
                return HealthStatus.critical(component, "磁盘空间严重不足", details);
            } else if (minFreeSpacePercentage < 10.0) {
                return HealthStatus.unhealthy(component, "磁盘空间不足", details);
            } else if (minFreeSpacePercentage < 20.0) {
                return HealthStatus.warning(component, "磁盘空间偏低", details);
            } else {
                return HealthStatus.healthy(component, "磁盘空间充足");
            }
            
        } catch (Exception e) {
            return HealthStatus.critical(component, "磁盘空间检查失败", 
                    "异常信息: " + e.getMessage());
        } finally {
            updateHealthResult(component);
        }
    }
    
    /**
     * 检查内存健康状态
     */
    public HealthStatus checkMemoryHealth() {
        String component = "memory";
        
        try {
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long maxMemory = runtime.maxMemory();
            long usedMemory = totalMemory - freeMemory;
            
            double memoryUsagePercentage = (double) usedMemory / maxMemory * 100;
            
            String details = String.format("内存使用率: %.2f%% (%dMB/%dMB)", 
                    memoryUsagePercentage, 
                    usedMemory / (1024 * 1024),
                    maxMemory / (1024 * 1024));
            
            if (memoryUsagePercentage > 95.0) {
                return HealthStatus.critical(component, "内存使用率过高", details);
            } else if (memoryUsagePercentage > 85.0) {
                return HealthStatus.unhealthy(component, "内存使用率偏高", details);
            } else if (memoryUsagePercentage > 75.0) {
                return HealthStatus.warning(component, "内存使用率较高", details);
            } else {
                return HealthStatus.healthy(component, "内存使用正常");
            }
            
        } catch (Exception e) {
            return HealthStatus.critical(component, "内存检查失败", 
                    "异常信息: " + e.getMessage());
        } finally {
            updateHealthResult(component);
        }
    }
    
    /**
     * 检查JVM健康状态
     */
    public HealthStatus checkJvmHealth() {
        String component = "jvm";
        
        try {
            java.lang.management.ThreadMXBean threadBean = 
                    java.lang.management.ManagementFactory.getThreadMXBean();
            
            int threadCount = threadBean.getThreadCount();
            long[] deadlockedThreads = threadBean.findDeadlockedThreads();
            int deadlockCount = deadlockedThreads != null ? deadlockedThreads.length : 0;
            
            String details = String.format("线程数: %d, 死锁线程: %d", threadCount, deadlockCount);
            
            if (deadlockCount > 0) {
                return HealthStatus.critical(component, "检测到线程死锁", details);
            } else if (threadCount > 1000) {
                return HealthStatus.warning(component, "线程数量过多", details);
            } else if (threadCount > 500) {
                return HealthStatus.warning(component, "线程数量较多", details);
            } else {
                return HealthStatus.healthy(component, "JVM状态正常");
            }
            
        } catch (Exception e) {
            return HealthStatus.critical(component, "JVM健康检查失败", 
                    "异常信息: " + e.getMessage());
        } finally {
            updateHealthResult(component);
        }
    }
    
    /**
     * 检查网络健康状态
     */
    public HealthStatus checkNetworkHealth() {
        String component = "network";
        
        try {
            List<String> testHosts = List.of("127.0.0.1", "localhost");
            List<Integer> testPorts = List.of(8080, 3306, 6379); // 常用服务端口
            
            int successfulConnections = 0;
            int totalTests = 0;
            StringBuilder details = new StringBuilder();
            
            for (String host : testHosts) {
                for (Integer port : testPorts) {
                    totalTests++;
                    try (Socket socket = new Socket()) {
                        socket.connect(new java.net.InetSocketAddress(host, port), 3000); // 3秒超时
                        successfulConnections++;
                        details.append(String.format("%s:%d[OK] ", host, port));
                    } catch (Exception e) {
                        details.append(String.format("%s:%d[FAIL] ", host, port));
                    }
                }
            }
            
            double successRate = (double) successfulConnections / totalTests * 100;
            String resultDetails = String.format("连接成功率: %.1f%% (%d/%d) - %s", 
                    successRate, successfulConnections, totalTests, details.toString().trim());
            
            if (successfulConnections == 0) {
                return HealthStatus.critical(component, "网络连接全部失败", resultDetails);
            } else if (successRate < 50.0) {
                return HealthStatus.unhealthy(component, "网络连接大部分失败", resultDetails);
            } else if (successRate < 100.0) {
                return HealthStatus.warning(component, "部分网络连接失败", resultDetails);
            } else {
                return HealthStatus.healthy(component, "网络连接正常");
            }
            
        } catch (Exception e) {
            return HealthStatus.critical(component, "网络健康检查失败", 
                    "异常信息: " + e.getMessage());
        } finally {
            updateHealthResult(component);
        }
    }
    
    /**
     * 获取组件健康历史
     */
    public Map<String, Object> getHealthHistory(String component) {
        Map<String, Object> result = new HashMap<>();
        
        HealthStatus lastResult = lastHealthResults.get(component);
        LocalDateTime lastCheckTime = lastCheckTimes.get(component);
        
        result.put("component", component);
        result.put("hasHistory", lastResult != null);
        
        if (lastResult != null) {
            result.put("lastStatus", lastResult.getStatus().name());
            result.put("lastMessage", lastResult.getMessage());
            result.put("lastDetails", lastResult.getDetails());
            result.put("lastLevel", lastResult.getLevel());
        }
        
        if (lastCheckTime != null) {
            result.put("lastCheckTime", lastCheckTime.toString());
        }
        
        return result;
    }
    
    /**
     * 定时健康检查
     */
    @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    @Async
    public void scheduleHealthCheck() {
        try {
            CompletableFuture.runAsync(this::performFullHealthCheck);
        } catch (Exception e) {
            System.err.println("定时健康检查失败: " + e.getMessage());
        }
    }
    
    /**
     * 计算整体健康状态
     */
    private HealthStatus calculateOverallHealth(List<HealthStatus> componentHealths) {
        if (componentHealths.isEmpty()) {
            return HealthStatus.unknown("system", "无组件健康数据");
        }
        
        // 查找最差的健康状态
        HealthStatus worstHealth = componentHealths.get(0);
        for (HealthStatus health : componentHealths) {
            if (health.compareLevel(worstHealth) > 0) {
                worstHealth = health;
            }
        }
        
        // 统计各状态数量
        long criticalCount = componentHealths.stream()
                .mapToLong(h -> h.isCritical() ? 1 : 0).sum();
        long unhealthyCount = componentHealths.stream()
                .mapToLong(h -> h.isUnhealthy() ? 1 : 0).sum();
        long warningCount = componentHealths.stream()
                .mapToLong(h -> h.needsWarning() ? 1 : 0).sum();
        long healthyCount = componentHealths.stream()
                .mapToLong(h -> h.isHealthy() ? 1 : 0).sum();
        
        String details = String.format("组件状态统计 - 健康:%d, 警告:%d, 不健康:%d, 严重:%d", 
                healthyCount, warningCount, unhealthyCount, criticalCount);
        
        if (criticalCount > 0) {
            return HealthStatus.critical("system", 
                    String.format("系统存在%d个严重问题", criticalCount), details);
        } else if (unhealthyCount > 0) {
            return HealthStatus.unhealthy("system", 
                    String.format("系统存在%d个健康问题", unhealthyCount), details);
        } else if (warningCount > 0) {
            return HealthStatus.warning("system", 
                    String.format("系统存在%d个警告", warningCount), details);
        } else {
            return HealthStatus.healthy("system", "系统整体健康");
        }
    }
    
    /**
     * 更新健康检查结果
     */
    private void updateHealthResult(String component) {
        lastCheckTimes.put(component, LocalDateTime.now());
    }
}