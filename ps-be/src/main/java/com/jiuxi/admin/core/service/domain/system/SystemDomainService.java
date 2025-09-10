package com.jiuxi.admin.core.service.domain.system;

import com.jiuxi.admin.core.bean.vo.TpDictionaryVO;
import com.jiuxi.admin.core.bean.vo.TpParameterConfigVO;
import com.jiuxi.admin.core.bean.vo.TpTraceVO;
import com.jiuxi.admin.core.bean.vo.TpTenantVO;
import com.jiuxi.admin.core.bean.query.TpDictionaryQuery;
import com.jiuxi.admin.core.bean.query.TpTraceQuery;
import com.jiuxi.common.bean.JsonResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 系统管理领域服务接口
 * 
 * 定义系统管理相关的核心业务逻辑，包括：
 * - 字典数据管理
 * - 系统参数配置
 * - 系统日志管理
 * - 租户管理
 * 
 * @author 系统重构
 * @since 2.2.2
 */
public interface SystemDomainService {

    // ==================== 字典管理 ====================
    
    /**
     * 创建字典
     * 
     * @param dictionaryVO 字典信息
     * @return 创建结果
     */
    JsonResponse createDictionary(TpDictionaryVO dictionaryVO);

    /**
     * 更新字典
     * 
     * @param dictionaryVO 字典信息
     * @return 更新结果
     */
    JsonResponse updateDictionary(TpDictionaryVO dictionaryVO);

    /**
     * 删除字典
     * 
     * @param dictionaryId 字典ID
     * @return 删除结果
     */
    JsonResponse deleteDictionary(String dictionaryId);

    /**
     * 分页查询字典
     * 
     * @param query 查询条件
     * @return 分页结果
     */
    Page<TpDictionaryVO> pageDictionaries(TpDictionaryQuery query);

    /**
     * 根据字典类型获取字典项
     * 
     * @param dictType 字典类型
     * @return 字典项列表
     */
    List<TpDictionaryVO> getDictionaryByType(String dictType);

    /**
     * 获取所有字典类型
     * 
     * @return 字典类型列表
     */
    List<String> getAllDictionaryTypes();

    // ==================== 参数配置管理 ====================
    
    /**
     * 创建参数配置
     * 
     * @param configVO 配置信息
     * @return 创建结果
     */
    JsonResponse createParameterConfig(TpParameterConfigVO configVO);

    /**
     * 更新参数配置
     * 
     * @param configVO 配置信息
     * @return 更新结果
     */
    JsonResponse updateParameterConfig(TpParameterConfigVO configVO);

    /**
     * 删除参数配置
     * 
     * @param configId 配置ID
     * @return 删除结果
     */
    JsonResponse deleteParameterConfig(String configId);

    /**
     * 获取参数配置值
     * 
     * @param configKey 配置键
     * @return 配置值
     */
    String getParameterValue(String configKey);

    /**
     * 批量获取参数配置
     * 
     * @param configKeys 配置键列表
     * @return 配置键值对
     */
    Map<String, String> getParameterValues(List<String> configKeys);

    /**
     * 获取所有参数配置
     * 
     * @return 参数配置列表
     */
    List<TpParameterConfigVO> getAllParameterConfigs();

    /**
     * 刷新参数配置缓存
     * 
     * @return 刷新结果
     */
    JsonResponse refreshParameterCache();

    // ==================== 系统日志管理 ====================
    
    /**
     * 记录系统日志
     * 
     * @param traceVO 日志信息
     * @return 记录结果
     */
    JsonResponse recordTrace(TpTraceVO traceVO);

    /**
     * 分页查询系统日志
     * 
     * @param query 查询条件
     * @return 分页结果
     */
    Page<TpTraceVO> pageTraces(TpTraceQuery query);

    /**
     * 清理过期日志
     * 
     * @param retentionDays 保留天数
     * @return 清理结果
     */
    JsonResponse cleanExpiredTraces(int retentionDays);

    /**
     * 获取系统统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getSystemStatistics();

    // ==================== 租户管理 ====================
    
    /**
     * 创建租户
     * 
     * @param tenantVO 租户信息
     * @return 创建结果
     */
    JsonResponse createTenant(TpTenantVO tenantVO);

    /**
     * 更新租户信息
     * 
     * @param tenantVO 租户信息
     * @return 更新结果
     */
    JsonResponse updateTenant(TpTenantVO tenantVO);

    /**
     * 删除租户
     * 
     * @param tenantId 租户ID
     * @return 删除结果
     */
    JsonResponse deleteTenant(String tenantId);

    /**
     * 获取所有租户
     * 
     * @return 租户列表
     */
    List<TpTenantVO> getAllTenants();

    /**
     * 验证租户是否有效
     * 
     * @param tenantId 租户ID
     * @return 是否有效
     */
    boolean isValidTenant(String tenantId);

    /**
     * 切换租户
     * 
     * @param tenantId 租户ID
     * @return 切换结果
     */
    JsonResponse switchTenant(String tenantId);
}