package com.jiuxi.platform.monitoring.app.service;

import com.jiuxi.platform.monitoring.domain.entity.MetricsRecord;
import com.jiuxi.platform.monitoring.domain.repository.MetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 监控图表数据服务
 * 专门为图表展示提供格式化的数据接口
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class MetricsChartService {
    
    private final MetricsRepository metricsRepository;
    private final MetricsQueryService queryService;
    
    @Autowired
    public MetricsChartService(MetricsRepository metricsRepository, MetricsQueryService queryService) {
        this.metricsRepository = metricsRepository;
        this.queryService = queryService;
    }
    
    /**
     * 获取实时监控仪表盘数据
     */
    public Map<String, Object> getRealTimeDashboard(String instanceId) {
        Map<String, Object> dashboard = new HashMap<>();
        
        try {
            // 核心指标
            List<String> coreMetrics = Arrays.asList(
                "cpu.usage", "memory.heap.usage.percentage", "disk.usage", "jvm.threads.count"
            );
            
            Map<String, Object> realTimeData = queryService.queryRealTimeMetrics(instanceId, coreMetrics);
            dashboard.put("realTimeMetrics", realTimeData);
            
            // 最近1小时趋势数据
            LocalDateTime now = LocalDateTime.now();
            String startTime = now.minusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String endTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            Map<String, Object> trendCharts = new HashMap<>();
            for (String metric : coreMetrics) {
                Map<String, Object> chartData = createLineChart(instanceId, metric, startTime, endTime, "5m");
                trendCharts.put(metric, chartData);
            }
            dashboard.put("trendCharts", trendCharts);
            
            // 系统概览
            Map<String, Object> overview = queryService.queryInstanceOverview(instanceId, "1h");
            dashboard.put("systemOverview", overview);
            
            dashboard.put("instanceId", instanceId);
            dashboard.put("refreshTime", now);
            dashboard.put("status", "success");
            
        } catch (Exception e) {
            dashboard.put("status", "error");
            dashboard.put("error", "获取仪表盘数据失败: " + e.getMessage());
        }
        
        return dashboard;
    }
    
    /**
     * 创建折线图数据
     */
    public Map<String, Object> createLineChart(String instanceId, String metricName, 
                                              String startTime, String endTime, String interval) {
        
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            Map<String, Object> queryResult = queryService.queryHistoricalMetrics(
                instanceId, metricName, startTime, endTime, interval);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dataPoints = (List<Map<String, Object>>) queryResult.get("dataPoints");
            
            if (dataPoints != null && !dataPoints.isEmpty()) {
                // 提取时间和数值
                List<String> timeLabels = new ArrayList<>();
                List<Double> values = new ArrayList<>();
                
                for (Map<String, Object> point : dataPoints) {
                    LocalDateTime timestamp = (LocalDateTime) point.get("timestamp");
                    Double value = (Double) point.get("value");
                    
                    timeLabels.add(formatTimeLabel(timestamp));
                    values.add(value != null ? value : 0.0);
                }
                
                chartData.put("type", "line");
                chartData.put("labels", timeLabels);
                chartData.put("datasets", Arrays.asList(createDataset(metricName, values, getMetricColor(metricName))));
                chartData.put("options", createLineChartOptions(metricName));
                
                // 添加统计信息
                @SuppressWarnings("unchecked")
                Map<String, Object> statistics = (Map<String, Object>) queryResult.get("statistics");
                if (statistics != null) {
                    chartData.put("statistics", statistics);
                }
                
                // 数据单位
                String unit = dataPoints.get(0).get("unit").toString();
                chartData.put("unit", unit);
            } else {
                chartData.put("type", "line");
                chartData.put("labels", Arrays.asList());
                chartData.put("datasets", Arrays.asList());
                chartData.put("message", "无数据");
            }
            
            chartData.put("metricName", metricName);
            chartData.put("timeRange", startTime + " ~ " + endTime);
            
        } catch (Exception e) {
            chartData.put("error", "创建折线图失败: " + e.getMessage());
        }
        
        return chartData;
    }
    
    /**
     * 创建饼图数据（用于系统资源分布）
     */
    public Map<String, Object> createPieChart(String instanceId, String chartType, String timeRange) {
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            LocalDateTime end = LocalDateTime.now();
            LocalDateTime start = calculateStartTime(end, timeRange);
            
            List<String> labels = new ArrayList<>();
            List<Double> values = new ArrayList<>();
            List<String> colors = new ArrayList<>();
            
            switch (chartType) {
                case "memory_distribution":
                    createMemoryDistributionChart(instanceId, start, end, labels, values, colors);
                    break;
                case "disk_distribution":
                    createDiskDistributionChart(instanceId, start, end, labels, values, colors);
                    break;
                case "thread_status":
                    createThreadStatusChart(instanceId, start, end, labels, values, colors);
                    break;
                default:
                    throw new IllegalArgumentException("不支持的图表类型: " + chartType);
            }
            
            chartData.put("type", "pie");
            chartData.put("labels", labels);
            chartData.put("datasets", Arrays.asList(Map.of(
                "data", values,
                "backgroundColor", colors,
                "borderWidth", 1
            )));
            chartData.put("options", createPieChartOptions());
            
        } catch (Exception e) {
            chartData.put("error", "创建饼图失败: " + e.getMessage());
        }
        
        return chartData;
    }
    
    /**
     * 创建柱状图数据（用于指标对比）
     */
    public Map<String, Object> createBarChart(String instanceId, List<String> metricNames, 
                                             String timeRange, String aggregationType) {
        
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            LocalDateTime end = LocalDateTime.now();
            LocalDateTime start = calculateStartTime(end, timeRange);
            
            List<String> labels = metricNames;
            List<Double> values = new ArrayList<>();
            List<String> colors = new ArrayList<>();
            
            for (String metricName : metricNames) {
                Map<String, Object> stats = metricsRepository.getMetricStatistics(
                    instanceId, metricName, start, end);
                
                Double value = 0.0;
                if (stats.containsKey(aggregationType)) {
                    value = (Double) stats.get(aggregationType);
                }
                values.add(value);
                colors.add(getMetricColor(metricName));
            }
            
            chartData.put("type", "bar");
            chartData.put("labels", labels);
            chartData.put("datasets", Arrays.asList(Map.of(
                "label", aggregationType.toUpperCase() + " 值",
                "data", values,
                "backgroundColor", colors,
                "borderWidth", 1
            )));
            chartData.put("options", createBarChartOptions());
            
        } catch (Exception e) {
            chartData.put("error", "创建柱状图失败: " + e.getMessage());
        }
        
        return chartData;
    }
    
    /**
     * 创建热力图数据（用于时间分布分析）
     */
    public Map<String, Object> createHeatmapChart(String instanceId, String metricName, 
                                                 String startTime, String endTime) {
        
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            Map<String, Object> queryResult = queryService.queryHistoricalMetrics(
                instanceId, metricName, startTime, endTime, "raw");
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dataPoints = (List<Map<String, Object>>) queryResult.get("dataPoints");
            
            if (dataPoints != null && !dataPoints.isEmpty()) {
                // 按小时和星期几分组
                Map<String, Map<String, List<Double>>> heatmapData = new HashMap<>();
                
                for (Map<String, Object> point : dataPoints) {
                    LocalDateTime timestamp = (LocalDateTime) point.get("timestamp");
                    Double value = (Double) point.get("value");
                    
                    String dayOfWeek = timestamp.getDayOfWeek().toString();
                    String hour = String.format("%02d:00", timestamp.getHour());
                    
                    heatmapData.computeIfAbsent(dayOfWeek, k -> new HashMap<>())
                              .computeIfAbsent(hour, k -> new ArrayList<>())
                              .add(value != null ? value : 0.0);
                }
                
                // 转换为热力图格式
                List<Map<String, Object>> heatmapPoints = new ArrayList<>();
                for (Map.Entry<String, Map<String, List<Double>>> dayEntry : heatmapData.entrySet()) {
                    String day = dayEntry.getKey();
                    for (Map.Entry<String, List<Double>> hourEntry : dayEntry.getValue().entrySet()) {
                        String hour = hourEntry.getKey();
                        List<Double> values = hourEntry.getValue();
                        
                        double avgValue = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                        
                        Map<String, Object> point = new HashMap<>();
                        point.put("x", hour);
                        point.put("y", day);
                        point.put("value", avgValue);
                        heatmapPoints.add(point);
                    }
                }
                
                chartData.put("type", "heatmap");
                chartData.put("data", heatmapPoints);
                chartData.put("options", createHeatmapOptions());
            } else {
                chartData.put("message", "无数据");
            }
            
        } catch (Exception e) {
            chartData.put("error", "创建热力图失败: " + e.getMessage());
        }
        
        return chartData;
    }
    
    /**
     * 获取系统状态概览图表
     */
    public Map<String, Object> getSystemStatusOverview(String instanceId, String timeRange) {
        Map<String, Object> overview = new HashMap<>();
        
        try {
            // 健康状态饼图
            Map<String, Object> healthChart = createSystemHealthChart(instanceId, timeRange);
            overview.put("healthChart", healthChart);
            
            // 性能趋势图
            Map<String, Object> performanceTrend = createPerformanceTrendChart(instanceId, timeRange);
            overview.put("performanceTrend", performanceTrend);
            
            // 资源使用率仪表盘
            Map<String, Object> resourceGauges = createResourceGauges(instanceId);
            overview.put("resourceGauges", resourceGauges);
            
            // 关键指标卡片
            Map<String, Object> keyMetrics = createKeyMetricsCards(instanceId);
            overview.put("keyMetrics", keyMetrics);
            
        } catch (Exception e) {
            overview.put("error", "获取系统概览失败: " + e.getMessage());
        }
        
        return overview;
    }
    
    // 私有辅助方法
    
    private void createMemoryDistributionChart(String instanceId, LocalDateTime start, LocalDateTime end,
                                             List<String> labels, List<Double> values, List<String> colors) {
        
        Map<String, Object> heapStats = metricsRepository.getMetricStatistics(instanceId, "memory.heap.used", start, end);
        Map<String, Object> nonHeapStats = metricsRepository.getMetricStatistics(instanceId, "memory.nonheap.used", start, end);
        
        if (heapStats.containsKey("avg")) {
            labels.add("堆内存");
            values.add((Double) heapStats.get("avg"));
            colors.add("#FF6B6B");
        }
        
        if (nonHeapStats.containsKey("avg")) {
            labels.add("非堆内存");
            values.add((Double) nonHeapStats.get("avg"));
            colors.add("#4ECDC4");
        }
    }
    
    private void createDiskDistributionChart(String instanceId, LocalDateTime start, LocalDateTime end,
                                           List<String> labels, List<Double> values, List<String> colors) {
        
        Map<String, Object> usedStats = metricsRepository.getMetricStatistics(instanceId, "disk.used", start, end);
        Map<String, Object> freeStats = metricsRepository.getMetricStatistics(instanceId, "disk.free", start, end);
        
        if (usedStats.containsKey("avg")) {
            labels.add("已用空间");
            values.add((Double) usedStats.get("avg"));
            colors.add("#FF8A65");
        }
        
        if (freeStats.containsKey("avg")) {
            labels.add("可用空间");
            values.add((Double) freeStats.get("avg"));
            colors.add("#81C784");
        }
    }
    
    private void createThreadStatusChart(String instanceId, LocalDateTime start, LocalDateTime end,
                                       List<String> labels, List<Double> values, List<String> colors) {
        
        Map<String, Object> totalStats = metricsRepository.getMetricStatistics(instanceId, "jvm.threads.count", start, end);
        Map<String, Object> daemonStats = metricsRepository.getMetricStatistics(instanceId, "jvm.threads.daemon.count", start, end);
        
        if (totalStats.containsKey("avg") && daemonStats.containsKey("avg")) {
            double total = (Double) totalStats.get("avg");
            double daemon = (Double) daemonStats.get("avg");
            double nonDaemon = total - daemon;
            
            labels.addAll(Arrays.asList("用户线程", "守护线程"));
            values.addAll(Arrays.asList(nonDaemon, daemon));
            colors.addAll(Arrays.asList("#42A5F5", "#AB47BC"));
        }
    }
    
    private Map<String, Object> createSystemHealthChart(String instanceId, String timeRange) {
        Map<String, Object> healthData = new HashMap<>();
        
        // 模拟健康状态数据
        List<String> labels = Arrays.asList("健康", "警告", "异常");
        List<Double> values = Arrays.asList(80.0, 15.0, 5.0);
        List<String> colors = Arrays.asList("#4CAF50", "#FF9800", "#F44336");
        
        healthData.put("type", "doughnut");
        healthData.put("labels", labels);
        healthData.put("datasets", Arrays.asList(Map.of(
            "data", values,
            "backgroundColor", colors
        )));
        
        return healthData;
    }
    
    private Map<String, Object> createPerformanceTrendChart(String instanceId, String timeRange) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = calculateStartTime(end, timeRange);
        
        String startTime = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endTime = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        return createLineChart(instanceId, "cpu.usage", startTime, endTime, "5m");
    }
    
    private Map<String, Object> createResourceGauges(String instanceId) {
        Map<String, Object> gauges = new HashMap<>();
        
        List<String> metrics = Arrays.asList("cpu.usage", "memory.heap.usage.percentage", "disk.usage");
        
        for (String metric : metrics) {
            Optional<MetricsRecord> latest = metricsRepository.findLatestRecord(instanceId, metric);
            
            Map<String, Object> gauge = new HashMap<>();
            if (latest.isPresent()) {
                gauge.put("value", latest.get().getMetricValue());
                gauge.put("unit", latest.get().getUnit());
                gauge.put("max", 100.0);
                gauge.put("color", getGaugeColor(latest.get().getMetricValue()));
            } else {
                gauge.put("value", 0.0);
                gauge.put("unit", "%");
                gauge.put("max", 100.0);
                gauge.put("color", "#E0E0E0");
            }
            
            gauges.put(metric, gauge);
        }
        
        return gauges;
    }
    
    private Map<String, Object> createKeyMetricsCards(String instanceId) {
        Map<String, Object> cards = new HashMap<>();
        
        Map<String, String> metricLabels = Map.of(
            "cpu.usage", "CPU使用率",
            "memory.heap.usage.percentage", "内存使用率",
            "disk.usage", "磁盘使用率",
            "jvm.threads.count", "线程数"
        );
        
        for (Map.Entry<String, String> entry : metricLabels.entrySet()) {
            String metric = entry.getKey();
            String label = entry.getValue();
            
            Optional<MetricsRecord> latest = metricsRepository.findLatestRecord(instanceId, metric);
            
            Map<String, Object> card = new HashMap<>();
            card.put("label", label);
            
            if (latest.isPresent()) {
                MetricsRecord record = latest.get();
                card.put("value", record.getMetricValue());
                card.put("unit", record.getUnit());
                card.put("timestamp", record.getTimestamp());
                card.put("trend", calculateTrend(instanceId, metric));
            } else {
                card.put("value", "N/A");
                card.put("unit", "");
                card.put("trend", "stable");
            }
            
            cards.put(metric, card);
        }
        
        return cards;
    }
    
    private String calculateTrend(String instanceId, String metricName) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourAgo = now.minusHours(1);
        
        List<MetricsRecord> records = metricsRepository.findRawRecords(instanceId, metricName, oneHourAgo, now, 2);
        
        if (records.size() >= 2) {
            records.sort(Comparator.comparing(MetricsRecord::getTimestamp));
            double first = records.get(0).getMetricValue();
            double last = records.get(records.size() - 1).getMetricValue();
            
            if (last > first * 1.05) return "up";
            if (last < first * 0.95) return "down";
        }
        
        return "stable";
    }
    
    // 图表配置和样式方法
    
    private Map<String, Object> createDataset(String label, List<Double> data, String color) {
        Map<String, Object> dataset = new HashMap<>();
        dataset.put("label", formatMetricName(label));
        dataset.put("data", data);
        dataset.put("borderColor", color);
        dataset.put("backgroundColor", color + "20");
        dataset.put("borderWidth", 2);
        dataset.put("fill", false);
        dataset.put("tension", 0.1);
        return dataset;
    }
    
    private Map<String, Object> createLineChartOptions(String metricName) {
        Map<String, Object> options = new HashMap<>();
        options.put("responsive", true);
        options.put("maintainAspectRatio", false);
        
        Map<String, Object> scales = new HashMap<>();
        Map<String, Object> yAxis = new HashMap<>();
        yAxis.put("beginAtZero", true);
        yAxis.put("title", Map.of("display", true, "text", formatMetricName(metricName)));
        scales.put("y", yAxis);
        
        Map<String, Object> xAxis = new HashMap<>();
        xAxis.put("title", Map.of("display", true, "text", "时间"));
        scales.put("x", xAxis);
        
        options.put("scales", scales);
        
        return options;
    }
    
    private Map<String, Object> createPieChartOptions() {
        Map<String, Object> options = new HashMap<>();
        options.put("responsive", true);
        options.put("maintainAspectRatio", false);
        return options;
    }
    
    private Map<String, Object> createBarChartOptions() {
        Map<String, Object> options = new HashMap<>();
        options.put("responsive", true);
        options.put("maintainAspectRatio", false);
        
        Map<String, Object> scales = new HashMap<>();
        scales.put("y", Map.of("beginAtZero", true));
        options.put("scales", scales);
        
        return options;
    }
    
    private Map<String, Object> createHeatmapOptions() {
        Map<String, Object> options = new HashMap<>();
        options.put("responsive", true);
        options.put("maintainAspectRatio", false);
        return options;
    }
    
    private String getMetricColor(String metricName) {
        Map<String, String> colorMap = Map.of(
            "cpu.usage", "#FF6B6B",
            "memory.heap.usage.percentage", "#4ECDC4",
            "disk.usage", "#45B7D1",
            "jvm.threads.count", "#96CEB4",
            "jvm.gc.total.count", "#FFEAA7"
        );
        return colorMap.getOrDefault(metricName, "#95A5A6");
    }
    
    private String getGaugeColor(double value) {
        if (value < 60) return "#4CAF50";      // 绿色
        if (value < 80) return "#FF9800";      // 橙色
        return "#F44336";                      // 红色
    }
    
    private String formatMetricName(String metricName) {
        Map<String, String> nameMap = Map.of(
            "cpu.usage", "CPU使用率",
            "memory.heap.usage.percentage", "堆内存使用率",
            "disk.usage", "磁盘使用率",
            "jvm.threads.count", "JVM线程数",
            "jvm.gc.total.count", "GC总次数"
        );
        return nameMap.getOrDefault(metricName, metricName);
    }
    
    private String formatTimeLabel(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    
    private LocalDateTime calculateStartTime(LocalDateTime end, String timeRange) {
        switch (timeRange.toLowerCase()) {
            case "1h": return end.minusHours(1);
            case "6h": return end.minusHours(6);
            case "12h": return end.minusHours(12);
            case "1d": return end.minusDays(1);
            case "7d": return end.minusDays(7);
            case "30d": return end.minusDays(30);
            default: return end.minusHours(1);
        }
    }
}