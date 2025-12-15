package com.jiuxi.common.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DataImportExportService
 * @Description: 数据导入导出服务接口
 * @Author: Qdd
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface DataImportExportService {
    
    // ==================== 数据导出 ====================
    
    /**
     * 导出Excel文件
     *
     * @param data 数据列表
     * @param headers 表头映射
     * @param sheetName 工作表名称
     * @return 文件路径
     */
    String exportToExcel(List<Map<String, Object>> data, Map<String, String> headers, String sheetName);
    
    /**
     * 导出CSV文件
     *
     * @param data 数据列表
     * @param headers 表头映射
     * @return 文件路径
     */
    String exportToCsv(List<Map<String, Object>> data, Map<String, String> headers);
    
    /**
     * 导出JSON文件
     *
     * @param data 数据对象
     * @return 文件路径
     */
    String exportToJson(Object data);
    
    /**
     * 导出XML文件
     *
     * @param data 数据对象
     * @param rootElement 根元素名称
     * @return 文件路径
     */
    String exportToXml(Object data, String rootElement);
    
    /**
     * 导出PDF文件
     *
     * @param data 数据列表
     * @param headers 表头映射
     * @param title 文档标题
     * @return 文件路径
     */
    String exportToPdf(List<Map<String, Object>> data, Map<String, String> headers, String title);
    
    /**
     * 导出SQL文件
     *
     * @param tableName 表名
     * @param data 数据列表
     * @return 文件路径
     */
    String exportToSql(String tableName, List<Map<String, Object>> data);
    
    /**
     * 批量导出多个表
     *
     * @param tableDataMap 表名和数据的映射
     * @param format 导出格式
     * @return 压缩文件路径
     */
    String batchExport(Map<String, List<Map<String, Object>>> tableDataMap, String format);
    
    /**
     * 导出模板文件
     *
     * @param templateType 模板类型
     * @param headers 表头映射
     * @return 文件路径
     */
    String exportTemplate(String templateType, Map<String, String> headers);
    
    /**
     * 自定义导出
     *
     * @param exportConfig 导出配置
     * @return 文件路径
     */
    String customExport(Map<String, Object> exportConfig);
    
    // ==================== 数据导入 ====================
    
    /**
     * 从Excel导入数据
     *
     * @param inputStream 文件输入流
     * @param sheetIndex 工作表索引
     * @param headerRow 表头行号
     * @return 导入结果
     */
    Map<String, Object> importFromExcel(InputStream inputStream, int sheetIndex, int headerRow);
    
    /**
     * 从CSV导入数据
     *
     * @param inputStream 文件输入流
     * @param delimiter 分隔符
     * @param hasHeader 是否包含表头
     * @return 导入结果
     */
    Map<String, Object> importFromCsv(InputStream inputStream, String delimiter, boolean hasHeader);
    
    /**
     * 从JSON导入数据
     *
     * @param inputStream 文件输入流
     * @return 导入结果
     */
    Map<String, Object> importFromJson(InputStream inputStream);
    
    /**
     * 从XML导入数据
     *
     * @param inputStream 文件输入流
     * @param rootElement 根元素名称
     * @return 导入结果
     */
    Map<String, Object> importFromXml(InputStream inputStream, String rootElement);
    
    /**
     * 从SQL导入数据
     *
     * @param inputStream 文件输入流
     * @return 导入结果
     */
    Map<String, Object> importFromSql(InputStream inputStream);
    
    /**
     * 批量导入
     *
     * @param inputStream 压缩文件输入流
     * @param importConfig 导入配置
     * @return 导入结果
     */
    Map<String, Object> batchImport(InputStream inputStream, Map<String, Object> importConfig);
    
    /**
     * 验证导入数据
     *
     * @param data 数据列表
     * @param validationRules 验证规则
     * @return 验证结果
     */
    Map<String, Object> validateImportData(List<Map<String, Object>> data, Map<String, Object> validationRules);
    
    /**
     * 预览导入数据
     *
     * @param inputStream 文件输入流
     * @param fileType 文件类型
     * @param previewRows 预览行数
     * @return 预览数据
     */
    Map<String, Object> previewImportData(InputStream inputStream, String fileType, int previewRows);
    
    // ==================== 数据转换 ====================
    
    /**
     * 数据格式转换
     *
     * @param inputStream 输入文件流
     * @param sourceFormat 源格式
     * @param targetFormat 目标格式
     * @return 转换后的文件路径
     */
    String convertDataFormat(InputStream inputStream, String sourceFormat, String targetFormat);
    
    /**
     * 数据清洗
     *
     * @param data 原始数据
     * @param cleanRules 清洗规则
     * @return 清洗后的数据
     */
    List<Map<String, Object>> cleanData(List<Map<String, Object>> data, Map<String, Object> cleanRules);
    
    /**
     * 数据映射
     *
     * @param data 原始数据
     * @param mappingRules 映射规则
     * @return 映射后的数据
     */
    List<Map<String, Object>> mapData(List<Map<String, Object>> data, Map<String, String> mappingRules);
    
    /**
     * 数据合并
     *
     * @param dataList 数据列表
     * @param mergeKey 合并键
     * @return 合并后的数据
     */
    List<Map<String, Object>> mergeData(List<List<Map<String, Object>>> dataList, String mergeKey);
    
    /**
     * 数据分割
     *
     * @param data 原始数据
     * @param splitRules 分割规则
     * @return 分割后的数据组
     */
    Map<String, List<Map<String, Object>>> splitData(List<Map<String, Object>> data, Map<String, Object> splitRules);
    
    // ==================== 任务管理 ====================
    
    /**
     * 创建导入任务
     *
     * @param taskConfig 任务配置
     * @return 任务ID
     */
    String createImportTask(Map<String, Object> taskConfig);
    
    /**
     * 创建导出任务
     *
     * @param taskConfig 任务配置
     * @return 任务ID
     */
    String createExportTask(Map<String, Object> taskConfig);
    
    /**
     * 获取任务状态
     *
     * @param taskId 任务ID
     * @return 任务状态
     */
    Map<String, Object> getTaskStatus(String taskId);
    
    /**
     * 取消任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean cancelTask(String taskId);
    
    /**
     * 获取任务列表
     *
     * @param page 页码
     * @param size 每页大小
     * @param status 任务状态
     * @param taskType 任务类型
     * @return 任务列表
     */
    Map<String, Object> getTaskList(int page, int size, String status, String taskType);
    
    /**
     * 删除任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean deleteTask(String taskId);
    
    /**
     * 重试失败任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean retryTask(String taskId);
    
    /**
     * 获取任务日志
     *
     * @param taskId 任务ID
     * @return 任务日志
     */
    List<Map<String, Object>> getTaskLogs(String taskId);
    
    // ==================== 模板管理 ====================
    
    /**
     * 创建导入模板
     *
     * @param templateName 模板名称
     * @param templateConfig 模板配置
     * @return 模板ID
     */
    String createImportTemplate(String templateName, Map<String, Object> templateConfig);
    
    /**
     * 创建导出模板
     *
     * @param templateName 模板名称
     * @param templateConfig 模板配置
     * @return 模板ID
     */
    String createExportTemplate(String templateName, Map<String, Object> templateConfig);
    
    /**
     * 更新模板
     *
     * @param templateId 模板ID
     * @param templateConfig 模板配置
     * @return 是否成功
     */
    boolean updateTemplate(String templateId, Map<String, Object> templateConfig);
    
    /**
     * 删除模板
     *
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean deleteTemplate(String templateId);
    
    /**
     * 获取模板列表
     *
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<Map<String, Object>> getTemplateList(String templateType);
    
    /**
     * 获取模板详情
     *
     * @param templateId 模板ID
     * @return 模板详情
     */
    Map<String, Object> getTemplateDetail(String templateId);
    
    /**
     * 应用模板
     *
     * @param templateId 模板ID
     * @param data 数据
     * @return 处理结果
     */
    Map<String, Object> applyTemplate(String templateId, Object data);
    
    // ==================== 数据源管理 ====================
    
    /**
     * 添加数据源
     *
     * @param dataSourceConfig 数据源配置
     * @return 数据源ID
     */
    String addDataSource(Map<String, Object> dataSourceConfig);
    
    /**
     * 更新数据源
     *
     * @param dataSourceId 数据源ID
     * @param dataSourceConfig 数据源配置
     * @return 是否成功
     */
    boolean updateDataSource(String dataSourceId, Map<String, Object> dataSourceConfig);
    
    /**
     * 删除数据源
     *
     * @param dataSourceId 数据源ID
     * @return 是否成功
     */
    boolean deleteDataSource(String dataSourceId);
    
    /**
     * 测试数据源连接
     *
     * @param dataSourceId 数据源ID
     * @return 测试结果
     */
    Map<String, Object> testDataSourceConnection(String dataSourceId);
    
    /**
     * 获取数据源列表
     *
     * @return 数据源列表
     */
    List<Map<String, Object>> getDataSourceList();
    
    /**
     * 从数据源导出数据
     *
     * @param dataSourceId 数据源ID
     * @param query 查询条件
     * @param format 导出格式
     * @return 文件路径
     */
    String exportFromDataSource(String dataSourceId, String query, String format);
    
    /**
     * 向数据源导入数据
     *
     * @param dataSourceId 数据源ID
     * @param tableName 表名
     * @param data 数据
     * @return 导入结果
     */
    Map<String, Object> importToDataSource(String dataSourceId, String tableName, List<Map<String, Object>> data);
    
    // ==================== 统计分析 ====================
    
    /**
     * 获取导入导出统计
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计数据
     */
    Map<String, Object> getImportExportStats(String startTime, String endTime);
    
    /**
     * 获取文件格式统计
     *
     * @return 格式统计
     */
    Map<String, Object> getFileFormatStats();
    
    /**
     * 获取任务执行统计
     *
     * @return 执行统计
     */
    Map<String, Object> getTaskExecutionStats();
    
    /**
     * 获取错误统计
     *
     * @return 错误统计
     */
    Map<String, Object> getErrorStats();
    
    /**
     * 生成数据质量报告
     *
     * @param data 数据
     * @return 质量报告
     */
    Map<String, Object> generateDataQualityReport(List<Map<String, Object>> data);
    
    // ==================== 配置管理 ====================
    
    /**
     * 设置导入导出配置
     *
     * @param key 配置键
     * @param value 配置值
     * @return 是否成功
     */
    boolean setImportExportConfig(String key, Object value);
    
    /**
     * 获取导入导出配置
     *
     * @param key 配置键
     * @return 配置值
     */
    Object getImportExportConfig(String key);
    
    /**
     * 获取所有配置
     *
     * @return 所有配置
     */
    Map<String, Object> getAllImportExportConfigs();
    
    /**
     * 重置配置
     *
     * @return 是否成功
     */
    boolean resetImportExportConfig();
    
    /**
     * 获取支持的文件格式
     *
     * @return 支持的格式列表
     */
    List<String> getSupportedFormats();
    
    /**
     * 获取格式配置
     *
     * @param format 格式名称
     * @return 格式配置
     */
    Map<String, Object> getFormatConfig(String format);
}