package com.jiuxi.common.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: DictService
 * @Description: 系统字典服务接口
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface DictService {
    
    // ==================== 字典类型管理 ====================
    
    /**
     * 创建字典类型
     *
     * @param dictType    字典类型编码
     * @param dictName    字典类型名称
     * @param description 描述
     * @param status      状态（1启用 0禁用）
     * @return 是否创建成功
     */
    boolean createDictType(String dictType, String dictName, String description, Integer status);
    
    /**
     * 更新字典类型
     *
     * @param dictType    字典类型编码
     * @param dictName    字典类型名称
     * @param description 描述
     * @param status      状态
     * @return 是否更新成功
     */
    boolean updateDictType(String dictType, String dictName, String description, Integer status);
    
    /**
     * 删除字典类型
     *
     * @param dictType 字典类型编码
     * @return 是否删除成功
     */
    boolean deleteDictType(String dictType);
    
    /**
     * 批量删除字典类型
     *
     * @param dictTypes 字典类型编码列表
     * @return 删除成功的数量
     */
    int deleteDictTypes(List<String> dictTypes);
    
    /**
     * 获取字典类型详情
     *
     * @param dictType 字典类型编码
     * @return 字典类型信息
     */
    Map<String, Object> getDictType(String dictType);
    
    /**
     * 获取所有字典类型
     *
     * @return 字典类型列表
     */
    List<Map<String, Object>> getAllDictTypes();
    
    /**
     * 分页查询字典类型
     *
     * @param page     页码
     * @param size     每页大小
     * @param dictName 字典名称（模糊查询）
     * @param status   状态过滤
     * @return 分页结果
     */
    Map<String, Object> getDictTypesPage(int page, int size, String dictName, Integer status);
    
    /**
     * 启用/禁用字典类型
     *
     * @param dictType 字典类型编码
     * @param status   状态（1启用 0禁用）
     * @return 是否操作成功
     */
    boolean updateDictTypeStatus(String dictType, Integer status);
    
    /**
     * 检查字典类型是否存在
     *
     * @param dictType 字典类型编码
     * @return 是否存在
     */
    boolean existsDictType(String dictType);
    
    // ==================== 字典数据管理 ====================
    
    /**
     * 创建字典数据
     *
     * @param dictType    字典类型编码
     * @param dictLabel   字典标签
     * @param dictValue   字典值
     * @param dictSort    排序
     * @param cssClass    CSS类名
     * @param listClass   列表样式类名
     * @param isDefault   是否默认
     * @param status      状态
     * @param remark      备注
     * @return 字典数据ID
     */
    String createDictData(String dictType, String dictLabel, String dictValue, Integer dictSort,
                         String cssClass, String listClass, Boolean isDefault, Integer status, String remark);
    
    /**
     * 更新字典数据
     *
     * @param dictId      字典数据ID
     * @param dictLabel   字典标签
     * @param dictValue   字典值
     * @param dictSort    排序
     * @param cssClass    CSS类名
     * @param listClass   列表样式类名
     * @param isDefault   是否默认
     * @param status      状态
     * @param remark      备注
     * @return 是否更新成功
     */
    boolean updateDictData(String dictId, String dictLabel, String dictValue, Integer dictSort,
                          String cssClass, String listClass, Boolean isDefault, Integer status, String remark);
    
    /**
     * 删除字典数据
     *
     * @param dictId 字典数据ID
     * @return 是否删除成功
     */
    boolean deleteDictData(String dictId);
    
    /**
     * 批量删除字典数据
     *
     * @param dictIds 字典数据ID列表
     * @return 删除成功的数量
     */
    int deleteDictDataList(List<String> dictIds);
    
    /**
     * 根据字典类型删除所有字典数据
     *
     * @param dictType 字典类型编码
     * @return 删除成功的数量
     */
    int deleteDictDataByType(String dictType);
    
    /**
     * 获取字典数据详情
     *
     * @param dictId 字典数据ID
     * @return 字典数据信息
     */
    Map<String, Object> getDictData(String dictId);
    
    /**
     * 根据字典类型获取字典数据列表
     *
     * @param dictType 字典类型编码
     * @return 字典数据列表
     */
    List<Map<String, Object>> getDictDataByType(String dictType);
    
    /**
     * 根据字典类型获取启用的字典数据列表
     *
     * @param dictType 字典类型编码
     * @return 字典数据列表
     */
    List<Map<String, Object>> getEnabledDictDataByType(String dictType);
    
    /**
     * 分页查询字典数据
     *
     * @param page      页码
     * @param size      每页大小
     * @param dictType  字典类型编码
     * @param dictLabel 字典标签（模糊查询）
     * @param status    状态过滤
     * @return 分页结果
     */
    Map<String, Object> getDictDataPage(int page, int size, String dictType, String dictLabel, Integer status);
    
    /**
     * 启用/禁用字典数据
     *
     * @param dictId 字典数据ID
     * @param status 状态（1启用 0禁用）
     * @return 是否操作成功
     */
    boolean updateDictDataStatus(String dictId, Integer status);
    
    /**
     * 设置默认字典数据
     *
     * @param dictType 字典类型编码
     * @param dictId   字典数据ID
     * @return 是否设置成功
     */
    boolean setDefaultDictData(String dictType, String dictId);
    
    /**
     * 获取默认字典数据
     *
     * @param dictType 字典类型编码
     * @return 默认字典数据
     */
    Map<String, Object> getDefaultDictData(String dictType);
    
    // ==================== 字典查询 ====================
    
    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型编码
     * @param dictValue 字典值
     * @return 字典标签
     */
    String getDictLabel(String dictType, String dictValue);
    
    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型编码
     * @param dictLabel 字典标签
     * @return 字典值
     */
    String getDictValue(String dictType, String dictLabel);
    
    /**
     * 批量获取字典标签
     *
     * @param dictType   字典类型编码
     * @param dictValues 字典值列表
     * @return 字典值-标签映射
     */
    Map<String, String> getDictLabels(String dictType, List<String> dictValues);
    
    /**
     * 批量获取字典值
     *
     * @param dictType   字典类型编码
     * @param dictLabels 字典标签列表
     * @return 字典标签-值映射
     */
    Map<String, String> getDictValues(String dictType, List<String> dictLabels);
    
    /**
     * 获取字典数据映射（值->标签）
     *
     * @param dictType 字典类型编码
     * @return 字典映射
     */
    Map<String, String> getDictMap(String dictType);
    
    /**
     * 获取字典数据反向映射（标签->值）
     *
     * @param dictType 字典类型编码
     * @return 字典反向映射
     */
    Map<String, String> getDictReverseMap(String dictType);
    
    /**
     * 搜索字典数据
     *
     * @param keyword 关键词
     * @return 搜索结果
     */
    List<Map<String, Object>> searchDictData(String keyword);
    
    /**
     * 根据字典类型搜索字典数据
     *
     * @param dictType 字典类型编码
     * @param keyword  关键词
     * @return 搜索结果
     */
    List<Map<String, Object>> searchDictDataByType(String dictType, String keyword);
    
    // ==================== 字典排序 ====================
    
    /**
     * 更新字典数据排序
     *
     * @param dictId   字典数据ID
     * @param dictSort 排序值
     * @return 是否更新成功
     */
    boolean updateDictDataSort(String dictId, Integer dictSort);
    
    /**
     * 批量更新字典数据排序
     *
     * @param sortData 排序数据（字典ID->排序值）
     * @return 更新成功的数量
     */
    int batchUpdateDictDataSort(Map<String, Integer> sortData);
    
    /**
     * 字典数据上移
     *
     * @param dictId 字典数据ID
     * @return 是否操作成功
     */
    boolean moveDictDataUp(String dictId);
    
    /**
     * 字典数据下移
     *
     * @param dictId 字典数据ID
     * @return 是否操作成功
     */
    boolean moveDictDataDown(String dictId);
    
    /**
     * 字典数据置顶
     *
     * @param dictId 字典数据ID
     * @return 是否操作成功
     */
    boolean moveDictDataToTop(String dictId);
    
    /**
     * 字典数据置底
     *
     * @param dictId 字典数据ID
     * @return 是否操作成功
     */
    boolean moveDictDataToBottom(String dictId);
    
    // ==================== 字典导入导出 ====================
    
    /**
     * 导出字典类型
     *
     * @param dictTypes 字典类型编码列表（为空则导出全部）
     * @param format    导出格式（excel/json/xml）
     * @return 导出文件路径
     */
    String exportDictTypes(List<String> dictTypes, String format);
    
    /**
     * 导出字典数据
     *
     * @param dictType 字典类型编码
     * @param format   导出格式（excel/json/xml）
     * @return 导出文件路径
     */
    String exportDictData(String dictType, String format);
    
    /**
     * 导入字典类型
     *
     * @param filePath 文件路径
     * @param override 是否覆盖现有数据
     * @return 导入结果
     */
    Map<String, Object> importDictTypes(String filePath, boolean override);
    
    /**
     * 导入字典数据
     *
     * @param dictType 字典类型编码
     * @param filePath 文件路径
     * @param override 是否覆盖现有数据
     * @return 导入结果
     */
    Map<String, Object> importDictData(String dictType, String filePath, boolean override);
    
    /**
     * 验证字典导入文件
     *
     * @param filePath 文件路径
     * @param type     验证类型（dictType/dictData）
     * @return 验证结果
     */
    Map<String, Object> validateDictImportFile(String filePath, String type);
    
    // ==================== 字典缓存管理 ====================
    
    /**
     * 刷新字典缓存
     */
    void refreshDictCache();
    
    /**
     * 刷新指定字典类型缓存
     *
     * @param dictType 字典类型编码
     */
    void refreshDictCache(String dictType);
    
    /**
     * 清空字典缓存
     */
    void clearDictCache();
    
    /**
     * 预热字典缓存
     */
    void warmupDictCache();
    
    /**
     * 获取字典缓存统计
     *
     * @return 缓存统计信息
     */
    Map<String, Object> getDictCacheStats();
    
    // ==================== 字典统计 ====================
    
    /**
     * 获取字典统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getDictStatistics();
    
    /**
     * 获取字典类型统计
     *
     * @param dictType 字典类型编码
     * @return 统计信息
     */
    Map<String, Object> getDictTypeStatistics(String dictType);
    
    /**
     * 获取字典使用情况
     *
     * @param dictType 字典类型编码
     * @return 使用情况
     */
    Map<String, Object> getDictUsage(String dictType);
    
    /**
     * 获取热门字典
     *
     * @param limit 数量限制
     * @return 热门字典列表
     */
    List<Map<String, Object>> getPopularDicts(int limit);
    
    // ==================== 字典验证 ====================
    
    /**
     * 验证字典值是否有效
     *
     * @param dictType  字典类型编码
     * @param dictValue 字典值
     * @return 是否有效
     */
    boolean isValidDictValue(String dictType, String dictValue);
    
    /**
     * 验证字典标签是否有效
     *
     * @param dictType  字典类型编码
     * @param dictLabel 字典标签
     * @return 是否有效
     */
    boolean isValidDictLabel(String dictType, String dictLabel);
    
    /**
     * 批量验证字典值
     *
     * @param dictType   字典类型编码
     * @param dictValues 字典值列表
     * @return 验证结果（值->是否有效）
     */
    Map<String, Boolean> validateDictValues(String dictType, List<String> dictValues);
    
    /**
     * 检查字典数据完整性
     *
     * @param dictType 字典类型编码
     * @return 检查结果
     */
    Map<String, Object> checkDictDataIntegrity(String dictType);
    
    /**
     * 修复字典数据
     *
     * @param dictType 字典类型编码
     * @return 修复结果
     */
    Map<String, Object> repairDictData(String dictType);
    
    // ==================== 字典树形结构 ====================
    
    /**
     * 获取树形字典数据
     *
     * @param dictType 字典类型编码
     * @return 树形结构数据
     */
    List<Map<String, Object>> getDictTree(String dictType);
    
    /**
     * 创建树形字典数据
     *
     * @param dictType   字典类型编码
     * @param parentId   父级ID
     * @param dictLabel  字典标签
     * @param dictValue  字典值
     * @param dictSort   排序
     * @param status     状态
     * @param remark     备注
     * @return 字典数据ID
     */
    String createTreeDictData(String dictType, String parentId, String dictLabel, String dictValue,
                             Integer dictSort, Integer status, String remark);
    
    /**
     * 移动树形字典数据
     *
     * @param dictId     字典数据ID
     * @param newParentId 新父级ID
     * @return 是否移动成功
     */
    boolean moveTreeDictData(String dictId, String newParentId);
    
    /**
     * 获取字典数据的所有子节点
     *
     * @param dictType 字典类型编码
     * @param parentId 父级ID
     * @return 子节点列表
     */
    List<Map<String, Object>> getDictChildren(String dictType, String parentId);
    
    /**
     * 获取字典数据的所有父节点路径
     *
     * @param dictId 字典数据ID
     * @return 父节点路径
     */
    List<Map<String, Object>> getDictParentPath(String dictId);
}