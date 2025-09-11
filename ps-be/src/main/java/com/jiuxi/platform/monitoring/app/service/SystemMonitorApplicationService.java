package com.jiuxi.platform.monitoring.app.service;

import com.jiuxi.platform.monitoring.domain.service.SystemMonitorDomainService;
import com.jiuxi.platform.monitoring.domain.entity.SystemMetrics;
import com.jiuxi.platform.monitoring.domain.valueobject.MetricValue;
import com.jiuxi.platform.monitoring.domain.valueobject.HealthStatus;
import com.jiuxi.common.service.SystemMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统监控应用服务
 * 实现原有SystemMonitorService接口，集成新的DDD架构
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service("systemMonitorApplicationService")
public class SystemMonitorApplicationService implements SystemMonitorService {
    
    private final SystemMonitorDomainService systemMonitorDomainService;
    private final PerformanceMonitorService performanceMonitorService;
    private final HealthCheckService healthCheckService;
    
    // 兼容性配置存储
    private final Map<String, Object> monitorConfigs = new HashMap<>();
    private final List<Map<String, Object>> alertRules = new ArrayList<>();
    private final String instanceId;
    
    @Autowired
    public SystemMonitorApplicationService(
            SystemMonitorDomainService systemMonitorDomainService,
            PerformanceMonitorService performanceMonitorService,
            HealthCheckService healthCheckService) {
        this.systemMonitorDomainService = systemMonitorDomainService;
        this.performanceMonitorService = performanceMonitorService;
        this.healthCheckService = healthCheckService;
        this.instanceId = performanceMonitorService.getInstanceId();
        initDefaultConfigs();
    }
    
    private void initDefaultConfigs() {
        monitorConfigs.put("monitor.enabled", true);
        monitorConfigs.put("monitor.interval", 30);
        monitorConfigs.put("monitor.history.retention", 7);
        monitorConfigs.put("alert.enabled", true);
        monitorConfigs.put("alert.email.enabled", true);
        monitorConfigs.put("healthcheck.enabled", true);
        monitorConfigs.put("healthcheck.interval", 300);
    }
    
    // ==================== 系统信息监控 ====================
    
    @Override
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        
        // 基本系统信息
        systemInfo.put("osName", System.getProperty("os.name"));
        systemInfo.put("osVersion", System.getProperty("os.version"));
        systemInfo.put("osArch", System.getProperty("os.arch"));
        systemInfo.put("javaVersion", System.getProperty("java.version"));
        systemInfo.put("javaVendor", System.getProperty("java.vendor"));
        systemInfo.put("userTimezone", System.getProperty("user.timezone"));
        systemInfo.put("fileEncoding", System.getProperty("file.encoding"));
        systemInfo.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return systemInfo;
    }
    
    @Override
    public Map<String, Object> getJvmInfo() {
        SystemMetrics metrics = systemMonitorDomainService.getSystemMetrics(instanceId);
        if (metrics == null) {
            metrics = systemMonitorDomainService.collectSystemMetrics(instanceId);
        }
        
        Map<String, Object> jvmInfo = new HashMap<>();
        Map<String, MetricValue> currentMetrics = metrics.getAllCurrentMetrics();
        
        // JVM运行时信息
        addMetricValue(jvmInfo, "uptime", currentMetrics, "jvm.runtime.uptime");
        addMetricValue(jvmInfo, "startTime", currentMetrics, "jvm.runtime.start.time");
        
        // JVM内存信息
        addMetricValue(jvmInfo, "heapUsed", currentMetrics, "memory.heap.used");
        addMetricValue(jvmInfo, "heapMax", currentMetrics, "memory.heap.max");
        addMetricValue(jvmInfo, "heapCommitted", currentMetrics, "memory.heap.committed");
        addMetricValue(jvmInfo, "nonHeapUsed", currentMetrics, "memory.nonheap.used");
        
        // JVM线程信息
        addMetricValue(jvmInfo, "threadCount", currentMetrics, "jvm.threads.count");
        addMetricValue(jvmInfo, "daemonThreadCount", currentMetrics, "jvm.threads.daemon.count");
        addMetricValue(jvmInfo, "peakThreadCount", currentMetrics, "jvm.threads.peak.count");
        
        // JVM类加载信息
        addMetricValue(jvmInfo, "loadedClassCount", currentMetrics, "jvm.classes.loaded.count");
        addMetricValue(jvmInfo, "totalLoadedClassCount", currentMetrics, "jvm.classes.total.loaded.count");
        
        // GC信息
        addMetricValue(jvmInfo, "gcCount", currentMetrics, "jvm.gc.total.count");
        addMetricValue(jvmInfo, "gcTime", currentMetrics, "jvm.gc.total.time");
        
        jvmInfo.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return jvmInfo;
    }
    
    @Override
    public Map<String, Object> getServerInfo() {
        Map<String, Object> serverInfo = new HashMap<>();
        
        // 服务器基本信息
        serverInfo.put("serverName", "PS-BE-Server");
        serverInfo.put("instanceId", instanceId);
        serverInfo.put("startupTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        serverInfo.put("serverStatus", "Running");
        
        // 健康状态
        HealthStatus systemHealth = systemMonitorDomainService.checkSystemHealth(instanceId);
        serverInfo.put("healthStatus", systemHealth.getStatus().name());
        serverInfo.put("healthMessage", systemHealth.getMessage());
        
        return serverInfo;
    }
    
    @Override
    public Map<String, Object> getOsInfo() {
        Map<String, Object> osInfo = new HashMap<>();
        
        osInfo.put("name", System.getProperty("os.name"));
        osInfo.put("version", System.getProperty("os.version"));
        osInfo.put("arch", System.getProperty("os.arch"));
        osInfo.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        
        return osInfo;
    }
    
    @Override
    public Map<String, Object> getNetworkInfo() {
        Map<String, Object> networkInfo = new HashMap<>();
        
        try {
            java.net.InetAddress localhost = java.net.InetAddress.getLocalHost();
            networkInfo.put("hostName", localhost.getHostName());
            networkInfo.put("hostAddress", localhost.getHostAddress());
            networkInfo.put("canonicalHostName", localhost.getCanonicalHostName());
        } catch (Exception e) {
            networkInfo.put("error", "获取网络信息失败: " + e.getMessage());
        }
        
        return networkInfo;
    }
    
    @Override
    public Map<String, Object> getDiskInfo() {
        SystemMetrics metrics = systemMonitorDomainService.collectSystemMetrics(instanceId);
        Map<String, Object> diskInfo = new HashMap<>();
        Map<String, MetricValue> currentMetrics = metrics.getAllCurrentMetrics();
        
        // 总体磁盘信息
        addMetricValue(diskInfo, "totalSpace", currentMetrics, "disk.total");
        addMetricValue(diskInfo, "usedSpace", currentMetrics, "disk.used");
        addMetricValue(diskInfo, "freeSpace", currentMetrics, "disk.free");
        addMetricValue(diskInfo, "usagePercentage", currentMetrics, "disk.usage");
        
        return diskInfo;
    }
    
    // ==================== 性能监控 ====================
    
    @Override
    public Map<String, Object> getCpuUsage() {
        return performanceMonitorService.getRealTimePerformance();
    }
    
    @Override
    public Map<String, Object> getMemoryUsage() {
        return performanceMonitorService.getRealTimePerformance();
    }
    
    @Override
    public Map<String, Object> getDiskUsage() {
        return performanceMonitorService.getRealTimePerformance();
    }
    
    @Override
    public Map<String, Object> getNetworkTraffic() {
        Map<String, Object> networkTraffic = new HashMap<>();
        networkTraffic.put("message", "网络流量监控功能待实现");
        networkTraffic.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return networkTraffic;
    }
    
    @Override
    public Map<String, Object> getSystemLoad() {
        SystemMetrics metrics = systemMonitorDomainService.collectSystemMetrics(instanceId);
        Map<String, Object> systemLoad = new HashMap<>();
        Map<String, MetricValue> currentMetrics = metrics.getAllCurrentMetrics();
        
        addMetricValue(systemLoad, "loadAverage", currentMetrics, "cpu.load.average");
        addMetricValue(systemLoad, "loadPercentage", currentMetrics, "cpu.load.percentage");
        
        return systemLoad;
    }
    
    @Override
    public Map<String, Object> getThreadInfo() {
        return getJvmInfo(); // JVM信息已包含线程信息
    }
    
    @Override
    public Map<String, Object> getGcInfo() {
        return getJvmInfo(); // JVM信息已包含GC信息
    }
    
    // ==================== 应用监控 ====================
    
    @Override
    public Map<String, Object> getApplicationStatus() {
        Map<String, Object> appStatus = new HashMap<>();
        
        HealthStatus health = systemMonitorDomainService.checkSystemHealth(instanceId);
        appStatus.put("status", health.getStatus().name());
        appStatus.put("message", health.getMessage());
        appStatus.put("level", health.getLevel());
        
        SystemMonitorDomainService.MonitoringSummary summary = 
                systemMonitorDomainService.getMonitoringSummary();
        appStatus.put("healthPercentage", summary.getHealthPercentage());
        appStatus.put("collectorsCount", summary.getCollectorsCount());
        
        return appStatus;
    }
    
    @Override
    public Map<String, Object> getDatabasePoolStatus() {
        return healthCheckService.checkDatabaseHealth().equals(HealthStatus.healthy("database", "正常")) ? 
                Map.of("status", "healthy", "message", "数据库连接正常") :
                Map.of("status", "unhealthy", "message", "数据库连接异常");
    }
    
    @Override
    public Map<String, Object> getRedisStatus() {
        Map<String, Object> redisStatus = new HashMap<>();
        redisStatus.put("status", "unknown");
        redisStatus.put("message", "Redis状态检查功能待实现");
        return redisStatus;
    }
    
    @Override
    public Map<String, Object> getCacheStats() {
        Map<String, Object> cacheStats = new HashMap<>();
        cacheStats.put("message", "缓存统计功能待实现");
        return cacheStats;
    }
    
    @Override
    public Map<String, Object> getSessionStats() {
        Map<String, Object> sessionStats = new HashMap<>();
        sessionStats.put("message", "会话统计功能待实现");
        return sessionStats;
    }
    
    @Override
    public Map<String, Object> getApiStats() {
        Map<String, Object> apiStats = new HashMap<>();
        apiStats.put("message", "API统计功能待实现");
        return apiStats;
    }
    
    @Override
    public Map<String, Object> getErrorStats() {
        Map<String, Object> errorStats = new HashMap<>();
        errorStats.put("message", "错误统计功能待实现");
        return errorStats;
    }
    
    // ==================== 实时监控 ====================
    
    @Override
    public Map<String, Object> getRealTimePerformance(int duration) {
        return performanceMonitorService.getRealTimePerformance();
    }
    
    @Override
    public List<Map<String, Object>> getRealTimeCpuData(int duration) {
        return performanceMonitorService.getCpuUsageHistory(duration);
    }
    
    @Override
    public List<Map<String, Object>> getRealTimeMemoryData(int duration) {
        return performanceMonitorService.getMemoryUsageHistory(duration);
    }
    
    @Override
    public List<Map<String, Object>> getRealTimeNetworkData(int duration) {
        return new ArrayList<>(); // 待实现
    }
    
    @Override
    public List<Map<String, Object>> getRealTimeDiskIOData(int duration) {
        return new ArrayList<>(); // 待实现
    }
    
    // ==================== 历史数据 ====================
    
    @Override
    public Map<String, Object> getHistoryPerformance(String startTime, String endTime, int interval) {
        return performanceMonitorService.getPerformanceSummary();
    }
    
    @Override
    public List<Map<String, Object>> getHistoryCpuData(String startTime, String endTime, int interval) {
        return performanceMonitorService.getCpuUsageHistory(60); // 默认1小时
    }
    
    @Override
    public List<Map<String, Object>> getHistoryMemoryData(String startTime, String endTime, int interval) {
        return performanceMonitorService.getMemoryUsageHistory(60); // 默认1小时
    }
    
    @Override
    public List<Map<String, Object>> getHistoryNetworkData(String startTime, String endTime, int interval) {
        return new ArrayList<>(); // 待实现
    }
    
    @Override
    public List<Map<String, Object>> getHistoryDiskData(String startTime, String endTime, int interval) {
        return performanceMonitorService.getDiskUsageHistory(60); // 默认1小时
    }
    
    // ==================== 告警监控 ====================
    
    @Override
    public boolean setPerformanceAlert(String metric, double threshold, String operator, int level) {
        Map<String, Object> alert = new HashMap<>();
        alert.put("id", UUID.randomUUID().toString());
        alert.put("metric", metric);
        alert.put("threshold", threshold);
        alert.put("operator", operator);
        alert.put("level", level);
        alert.put("enabled", true);
        alert.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        alertRules.add(alert);
        return true;
    }
    
    @Override
    public List<Map<String, Object>> getPerformanceAlerts() {
        return new ArrayList<>(alertRules);
    }
    
    @Override
    public boolean deletePerformanceAlert(String alertId) {
        return alertRules.removeIf(alert -> alertId.equals(alert.get("id")));
    }
    
    @Override
    public List<Map<String, Object>> checkPerformanceAlerts() {
        Map<String, Object> thresholdCheck = performanceMonitorService.checkPerformanceThresholds();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> alerts = (List<Map<String, Object>>) thresholdCheck.get("alerts");
        return alerts != null ? alerts : new ArrayList<>();
    }
    
    // ==================== 健康检查 ====================
    
    @Override
    public Map<String, Object> performHealthCheck() {
        return healthCheckService.performFullHealthCheck();
    }
    
    @Override
    public Map<String, Object> checkDatabaseHealth() {
        HealthStatus dbHealth = healthCheckService.checkDatabaseHealth();
        Map<String, Object> result = new HashMap<>();
        result.put("status", dbHealth.getStatus().name());
        result.put("message", dbHealth.getMessage());
        result.put("details", dbHealth.getDetails());
        result.put("level", dbHealth.getLevel());
        return result;
    }
    
    @Override
    public Map<String, Object> checkRedisHealth() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "unknown");
        result.put("message", "Redis健康检查功能待实现");
        return result;
    }
    
    @Override
    public Map<String, Object> checkExternalServices() {
        HealthStatus networkHealth = healthCheckService.checkNetworkHealth();
        Map<String, Object> result = new HashMap<>();
        result.put("status", networkHealth.getStatus().name());
        result.put("message", networkHealth.getMessage());
        result.put("details", networkHealth.getDetails());
        return result;
    }
    
    @Override
    public Map<String, Object> checkDiskSpace() {
        HealthStatus diskHealth = healthCheckService.checkDiskSpaceHealth();
        Map<String, Object> result = new HashMap<>();
        result.put("status", diskHealth.getStatus().name());
        result.put("message", diskHealth.getMessage());
        result.put("details", diskHealth.getDetails());
        return result;
    }
    
    @Override
    public Map<String, Object> checkMemoryUsage() {
        HealthStatus memoryHealth = healthCheckService.checkMemoryHealth();
        Map<String, Object> result = new HashMap<>();
        result.put("status", memoryHealth.getStatus().name());
        result.put("message", memoryHealth.getMessage());
        result.put("details", memoryHealth.getDetails());
        return result;
    }
    
    // ==================== 监控配置 ====================
    
    @Override
    public boolean setMonitorConfig(String key, Object value) {
        monitorConfigs.put(key, value);
        return true;
    }
    
    @Override
    public Object getMonitorConfig(String key) {
        return monitorConfigs.get(key);
    }
    
    @Override
    public Map<String, Object> getAllMonitorConfigs() {
        return new HashMap<>(monitorConfigs);
    }
    
    @Override
    public boolean resetMonitorConfig() {
        monitorConfigs.clear();
        initDefaultConfigs();
        return true;
    }
    
    @Override
    public boolean setMonitorEnabled(boolean enabled) {
        return setMonitorConfig("monitor.enabled", enabled);
    }
    
    @Override
    public Map<String, Object> getMonitorStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("enabled", monitorConfigs.get("monitor.enabled"));
        status.put("instanceId", instanceId);
        status.put("collectorsCount", systemMonitorDomainService.getEnabledCollectors().size());
        
        SystemMonitorDomainService.MonitoringSummary summary = 
                systemMonitorDomainService.getMonitoringSummary();
        status.put("summary", summary);
        
        return status;
    }
    
    // ==================== 其他方法的简化实现 ====================
    
    @Override
    public Map<String, Object> getAlertHistory(int page, int size, Integer level, String startTime, String endTime) {
        return Map.of("message", "告警历史查询功能待实现");
    }
    
    @Override
    public boolean acknowledgeAlert(String alertId, String userId, String remark) {
        return true; // 待实现
    }
    
    @Override
    public Map<String, Object> getHealthCheckHistory(int page, int size, String status, String startTime, String endTime) {
        return Map.of("message", "健康检查历史查询功能待实现");
    }
    
    @Override
    public List<Map<String, Object>> getProcessList() {
        return new ArrayList<>(); // 待实现
    }
    
    @Override
    public Map<String, Object> getProcessDetail(long pid) {
        return Map.of("message", "进程详情查询功能待实现");
    }
    
    @Override
    public boolean killProcess(long pid) {
        return false; // 待实现
    }
    
    @Override
    public List<Map<String, Object>> getPortUsage() {
        return new ArrayList<>(); // 待实现
    }
    
    @Override
    public boolean isPortInUse(int port) {
        return false; // 待实现
    }
    
    @Override
    public Map<String, Object> getLogStats() {
        return Map.of("message", "日志统计功能待实现");
    }
    
    @Override
    public Map<String, Object> getErrorLogs(int page, int size, String level, String startTime, String endTime) {
        return Map.of("message", "错误日志查询功能待实现");
    }
    
    @Override
    public Map<String, Object> searchLogs(String keyword, int page, int size, String startTime, String endTime) {
        return Map.of("message", "日志搜索功能待实现");
    }
    
    @Override
    public List<Map<String, Object>> getLogFiles() {
        return new ArrayList<>(); // 待实现
    }
    
    @Override
    public String downloadLogFile(String fileName) {
        return null; // 待实现
    }
    
    @Override
    public Map<String, Object> generatePerformanceReport(String startTime, String endTime) {
        return performanceMonitorService.getPerformanceSummary();
    }
    
    @Override
    public Map<String, Object> getSlowQueryStats() {
        return Map.of("message", "慢查询统计功能待实现");
    }
    
    @Override
    public Map<String, Object> getApiPerformanceStats() {
        return Map.of("message", "API性能统计功能待实现");
    }
    
    @Override
    public Map<String, Object> getHotspotData() {
        return Map.of("message", "热点数据统计功能待实现");
    }
    
    @Override
    public Map<String, Object> analyzeBottlenecks() {
        return performanceMonitorService.getPerformanceSummary();
    }
    
    /**
     * 辅助方法：从指标中提取值到Map
     */
    private void addMetricValue(Map<String, Object> target, String key, 
            Map<String, MetricValue> metrics, String metricName) {
        MetricValue metric = metrics.get(metricName);
        if (metric != null) {
            target.put(key, metric.getValue());
            target.put(key + "Unit", metric.getUnit());
            target.put(key + "Timestamp", metric.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }
}