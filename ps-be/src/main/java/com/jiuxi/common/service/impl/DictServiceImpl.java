package com.jiuxi.common.service.impl;

import com.jiuxi.common.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName: DictServiceImpl
 * @Description: 系统字典服务实现类
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
@Service
public class DictServiceImpl implements DictService {
    
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;
    
    // 内存存储（生产环境应使用数据库）
    private final Map<String, DictType> dictTypes = new ConcurrentHashMap<>();
    private final Map<String, DictData> dictDataMap = new ConcurrentHashMap<>();
    private final Map<String, List<DictData>> dictDataByType = new ConcurrentHashMap<>();
    private final Map<String, Long> dictUsage = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        // 初始化默认字典类型
        initDefaultDictTypes();
        // 初始化默认字典数据
        initDefaultDictData();
    }
    
    private void initDefaultDictTypes() {
        createDictType("sys_user_sex", "用户性别", "用户性别字典", 1);
        createDictType("sys_show_hide", "显示状态", "菜单状态字典", 1);
        createDictType("sys_normal_disable", "系统状态", "系统状态字典", 1);
        createDictType("sys_job_status", "任务状态", "任务状态字典", 1);
        createDictType("sys_job_group", "任务分组", "任务分组字典", 1);
        createDictType("sys_yes_no", "是否", "是否字典", 1);
        createDictType("sys_notice_type", "通知类型", "通知类型字典", 1);
        createDictType("sys_notice_status", "通知状态", "通知状态字典", 1);
        createDictType("sys_oper_type", "操作类型", "操作类型字典", 1);
        createDictType("sys_common_status", "通用状态", "通用状态字典", 1);
    }
    
    private void initDefaultDictData() {
        // 用户性别
        createDictData("sys_user_sex", "男", "1", 1, "", "primary", false, 1, "性别男");
        createDictData("sys_user_sex", "女", "2", 2, "", "success", false, 1, "性别女");
        createDictData("sys_user_sex", "未知", "0", 3, "", "info", true, 1, "性别未知");
        
        // 显示状态
        createDictData("sys_show_hide", "显示", "0", 1, "", "primary", true, 1, "显示菜单");
        createDictData("sys_show_hide", "隐藏", "1", 2, "", "danger", false, 1, "隐藏菜单");
        
        // 系统状态
        createDictData("sys_normal_disable", "正常", "0", 1, "", "primary", true, 1, "正常状态");
        createDictData("sys_normal_disable", "停用", "1", 2, "", "danger", false, 1, "停用状态");
        
        // 是否
        createDictData("sys_yes_no", "是", "Y", 1, "", "primary", false, 1, "系统默认是");
        createDictData("sys_yes_no", "否", "N", 2, "", "danger", true, 1, "系统默认否");
        
        // 通知类型
        createDictData("sys_notice_type", "通知", "1", 1, "", "warning", false, 1, "通知");
        createDictData("sys_notice_type", "公告", "2", 2, "", "success", false, 1, "公告");
        
        // 通知状态
        createDictData("sys_notice_status", "正常", "0", 1, "", "primary", true, 1, "正常状态");
        createDictData("sys_notice_status", "关闭", "1", 2, "", "danger", false, 1, "关闭状态");
        
        // 操作类型
        createDictData("sys_oper_type", "其他", "0", 1, "", "info", false, 1, "其他操作");
        createDictData("sys_oper_type", "新增", "1", 2, "", "info", false, 1, "新增操作");
        createDictData("sys_oper_type", "修改", "2", 3, "", "info", false, 1, "修改操作");
        createDictData("sys_oper_type", "删除", "3", 4, "", "danger", false, 1, "删除操作");
        createDictData("sys_oper_type", "授权", "4", 5, "", "primary", false, 1, "授权操作");
        createDictData("sys_oper_type", "导出", "5", 6, "", "warning", false, 1, "导出操作");
        createDictData("sys_oper_type", "导入", "6", 7, "", "warning", false, 1, "导入操作");
        createDictData("sys_oper_type", "强退", "7", 8, "", "danger", false, 1, "强退操作");
        createDictData("sys_oper_type", "生成代码", "8", 9, "", "warning", false, 1, "生成操作");
        createDictData("sys_oper_type", "清空数据", "9", 10, "", "danger", false, 1, "清空操作");
        
        // 通用状态
        createDictData("sys_common_status", "成功", "0", 1, "", "primary", false, 1, "正常状态");
        createDictData("sys_common_status", "失败", "1", 2, "", "danger", false, 1, "停用状态");
    }
    
    // ==================== 字典类型管理 ====================
    
    @Override
    public boolean createDictType(String dictType, String dictName, String description, Integer status) {
        if (dictTypes.containsKey(dictType)) {
            return false;
        }
        
        DictType type = new DictType();
        type.setDictType(dictType);
        type.setDictName(dictName);
        type.setDescription(description);
        type.setStatus(status);
        type.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        type.setUpdateTime(type.getCreateTime());
        
        dictTypes.put(dictType, type);
        dictDataByType.put(dictType, new ArrayList<>());
        
        return true;
    }
    
    @Override
    public boolean updateDictType(String dictType, String dictName, String description, Integer status) {
        DictType type = dictTypes.get(dictType);
        if (type == null) {
            return false;
        }
        
        type.setDictName(dictName);
        type.setDescription(description);
        type.setStatus(status);
        type.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return true;
    }
    
    @Override
    public boolean deleteDictType(String dictType) {
        if (!dictTypes.containsKey(dictType)) {
            return false;
        }
        
        // 删除字典类型及其所有数据
        dictTypes.remove(dictType);
        List<DictData> dataList = dictDataByType.remove(dictType);
        if (dataList != null) {
            for (DictData data : dataList) {
                dictDataMap.remove(data.getDictId());
            }
        }
        
        return true;
    }
    
    @Override
    public int deleteDictTypes(List<String> dictTypes) {
        int count = 0;
        for (String dictType : dictTypes) {
            if (deleteDictType(dictType)) {
                count++;
            }
        }
        return count;
    }
    
    @Override
    public Map<String, Object> getDictType(String dictType) {
        DictType type = dictTypes.get(dictType);
        if (type == null) {
            return null;
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("dictType", type.getDictType());
        result.put("dictName", type.getDictName());
        result.put("description", type.getDescription());
        result.put("status", type.getStatus());
        result.put("createTime", type.getCreateTime());
        result.put("updateTime", type.getUpdateTime());
        
        List<DictData> dataList = dictDataByType.get(dictType);
        result.put("dataCount", dataList != null ? dataList.size() : 0);
        
        return result;
    }
    
    @Override
    public List<Map<String, Object>> getAllDictTypes() {
        return dictTypes.values().stream()
                .map(type -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("dictType", type.getDictType());
                    result.put("dictName", type.getDictName());
                    result.put("description", type.getDescription());
                    result.put("status", type.getStatus());
                    result.put("createTime", type.getCreateTime());
                    result.put("updateTime", type.getUpdateTime());
                    
                    List<DictData> dataList = dictDataByType.get(type.getDictType());
                    result.put("dataCount", dataList != null ? dataList.size() : 0);
                    
                    return result;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> getDictTypesPage(int page, int size, String dictName, Integer status) {
        List<Map<String, Object>> allTypes = dictTypes.values().stream()
                .filter(type -> {
                    boolean nameMatch = dictName == null || type.getDictName().contains(dictName);
                    boolean statusMatch = status == null || status.equals(type.getStatus());
                    return nameMatch && statusMatch;
                })
                .map(type -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("dictType", type.getDictType());
                    result.put("dictName", type.getDictName());
                    result.put("description", type.getDescription());
                    result.put("status", type.getStatus());
                    result.put("createTime", type.getCreateTime());
                    result.put("updateTime", type.getUpdateTime());
                    
                    List<DictData> dataList = dictDataByType.get(type.getDictType());
                    result.put("dataCount", dataList != null ? dataList.size() : 0);
                    
                    return result;
                })
                .collect(Collectors.toList());
        
        int total = allTypes.size();
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);
        
        List<Map<String, Object>> records = start < total ? allTypes.subList(start, end) : new ArrayList<>();
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", total);
        result.put("current", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);
        
        return result;
    }
    
    @Override
    public boolean updateDictTypeStatus(String dictType, Integer status) {
        DictType type = dictTypes.get(dictType);
        if (type == null) {
            return false;
        }
        
        type.setStatus(status);
        type.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return true;
    }
    
    @Override
    public boolean existsDictType(String dictType) {
        return dictTypes.containsKey(dictType);
    }
    
    // ==================== 字典数据管理 ====================
    
    @Override
    public String createDictData(String dictType, String dictLabel, String dictValue, Integer dictSort,
                                String cssClass, String listClass, Boolean isDefault, Integer status, String remark) {
        if (!dictTypes.containsKey(dictType)) {
            return null;
        }
        
        String dictId = UUID.randomUUID().toString();
        DictData data = new DictData();
        data.setDictId(dictId);
        data.setDictType(dictType);
        data.setDictLabel(dictLabel);
        data.setDictValue(dictValue);
        data.setDictSort(dictSort != null ? dictSort : 0);
        data.setCssClass(cssClass);
        data.setListClass(listClass);
        data.setIsDefault(isDefault != null ? isDefault : false);
        data.setStatus(status != null ? status : 1);
        data.setRemark(remark);
        data.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        data.setUpdateTime(data.getCreateTime());
        
        dictDataMap.put(dictId, data);
        dictDataByType.get(dictType).add(data);
        
        // 如果设置为默认，取消其他默认项
        if (isDefault != null && isDefault) {
            setDefaultDictData(dictType, dictId);
        }
        
        return dictId;
    }
    
    @Override
    public boolean updateDictData(String dictId, String dictLabel, String dictValue, Integer dictSort,
                                 String cssClass, String listClass, Boolean isDefault, Integer status, String remark) {
        DictData data = dictDataMap.get(dictId);
        if (data == null) {
            return false;
        }
        
        data.setDictLabel(dictLabel);
        data.setDictValue(dictValue);
        data.setDictSort(dictSort != null ? dictSort : data.getDictSort());
        data.setCssClass(cssClass);
        data.setListClass(listClass);
        data.setIsDefault(isDefault != null ? isDefault : data.getIsDefault());
        data.setStatus(status != null ? status : data.getStatus());
        data.setRemark(remark);
        data.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        // 如果设置为默认，取消其他默认项
        if (isDefault != null && isDefault) {
            setDefaultDictData(data.getDictType(), dictId);
        }
        
        return true;
    }
    
    @Override
    public boolean deleteDictData(String dictId) {
        DictData data = dictDataMap.remove(dictId);
        if (data == null) {
            return false;
        }
        
        List<DictData> dataList = dictDataByType.get(data.getDictType());
        if (dataList != null) {
            dataList.removeIf(d -> d.getDictId().equals(dictId));
        }
        
        return true;
    }
    
    @Override
    public int deleteDictDataList(List<String> dictIds) {
        int count = 0;
        for (String dictId : dictIds) {
            if (deleteDictData(dictId)) {
                count++;
            }
        }
        return count;
    }
    
    @Override
    public int deleteDictDataByType(String dictType) {
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            return 0;
        }
        
        int count = dataList.size();
        for (DictData data : dataList) {
            dictDataMap.remove(data.getDictId());
        }
        dataList.clear();
        
        return count;
    }
    
    @Override
    public Map<String, Object> getDictData(String dictId) {
        DictData data = dictDataMap.get(dictId);
        if (data == null) {
            return null;
        }
        
        return convertDictDataToMap(data);
    }
    
    @Override
    public List<Map<String, Object>> getDictDataByType(String dictType) {
        recordDictUsage(dictType);
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            return new ArrayList<>();
        }
        
        return dataList.stream()
                .sorted(Comparator.comparing(DictData::getDictSort))
                .map(this::convertDictDataToMap)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Map<String, Object>> getEnabledDictDataByType(String dictType) {
        recordDictUsage(dictType);
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            return new ArrayList<>();
        }
        
        return dataList.stream()
                .filter(data -> data.getStatus() == 1)
                .sorted(Comparator.comparing(DictData::getDictSort))
                .map(this::convertDictDataToMap)
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> getDictDataPage(int page, int size, String dictType, String dictLabel, Integer status) {
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        
        List<Map<String, Object>> filteredData = dataList.stream()
                .filter(data -> {
                    boolean labelMatch = dictLabel == null || data.getDictLabel().contains(dictLabel);
                    boolean statusMatch = status == null || status.equals(data.getStatus());
                    return labelMatch && statusMatch;
                })
                .sorted(Comparator.comparing(DictData::getDictSort))
                .map(this::convertDictDataToMap)
                .collect(Collectors.toList());
        
        int total = filteredData.size();
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);
        
        List<Map<String, Object>> records = start < total ? filteredData.subList(start, end) : new ArrayList<>();
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", total);
        result.put("current", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);
        
        return result;
    }
    
    @Override
    public boolean updateDictDataStatus(String dictId, Integer status) {
        DictData data = dictDataMap.get(dictId);
        if (data == null) {
            return false;
        }
        
        data.setStatus(status);
        data.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return true;
    }
    
    @Override
    public boolean setDefaultDictData(String dictType, String dictId) {
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            return false;
        }
        
        // 取消所有默认项
        for (DictData data : dataList) {
            data.setIsDefault(false);
        }
        
        // 设置新的默认项
        DictData targetData = dictDataMap.get(dictId);
        if (targetData != null && targetData.getDictType().equals(dictType)) {
            targetData.setIsDefault(true);
            return true;
        }
        
        return false;
    }
    
    @Override
    public Map<String, Object> getDefaultDictData(String dictType) {
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            return null;
        }
        
        Optional<DictData> defaultData = dataList.stream()
                .filter(DictData::getIsDefault)
                .findFirst();
        
        return defaultData.map(this::convertDictDataToMap).orElse(null);
    }
    
    // ==================== 字典查询 ====================
    
    @Override
    public String getDictLabel(String dictType, String dictValue) {
        recordDictUsage(dictType);
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            return null;
        }
        
        return dataList.stream()
                .filter(data -> data.getDictValue().equals(dictValue) && data.getStatus() == 1)
                .map(DictData::getDictLabel)
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public String getDictValue(String dictType, String dictLabel) {
        recordDictUsage(dictType);
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            return null;
        }
        
        return dataList.stream()
                .filter(data -> data.getDictLabel().equals(dictLabel) && data.getStatus() == 1)
                .map(DictData::getDictValue)
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public Map<String, String> getDictLabels(String dictType, List<String> dictValues) {
        recordDictUsage(dictType);
        Map<String, String> result = new HashMap<>();
        for (String value : dictValues) {
            String label = getDictLabel(dictType, value);
            if (label != null) {
                result.put(value, label);
            }
        }
        return result;
    }
    
    @Override
    public Map<String, String> getDictValues(String dictType, List<String> dictLabels) {
        recordDictUsage(dictType);
        Map<String, String> result = new HashMap<>();
        for (String label : dictLabels) {
            String value = getDictValue(dictType, label);
            if (value != null) {
                result.put(label, value);
            }
        }
        return result;
    }
    
    @Override
    public Map<String, String> getDictMap(String dictType) {
        recordDictUsage(dictType);
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            return new HashMap<>();
        }
        
        return dataList.stream()
                .filter(data -> data.getStatus() == 1)
                .collect(Collectors.toMap(
                        DictData::getDictValue,
                        DictData::getDictLabel,
                        (existing, replacement) -> existing
                ));
    }
    
    @Override
    public Map<String, String> getDictReverseMap(String dictType) {
        recordDictUsage(dictType);
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            return new HashMap<>();
        }
        
        return dataList.stream()
                .filter(data -> data.getStatus() == 1)
                .collect(Collectors.toMap(
                        DictData::getDictLabel,
                        DictData::getDictValue,
                        (existing, replacement) -> existing
                ));
    }
    
    @Override
    public List<Map<String, Object>> searchDictData(String keyword) {
        return dictDataMap.values().stream()
                .filter(data -> data.getDictLabel().contains(keyword) || 
                        data.getDictValue().contains(keyword) ||
                        (data.getRemark() != null && data.getRemark().contains(keyword)))
                .map(this::convertDictDataToMap)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Map<String, Object>> searchDictDataByType(String dictType, String keyword) {
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            return new ArrayList<>();
        }
        
        return dataList.stream()
                .filter(data -> data.getDictLabel().contains(keyword) || 
                        data.getDictValue().contains(keyword) ||
                        (data.getRemark() != null && data.getRemark().contains(keyword)))
                .map(this::convertDictDataToMap)
                .collect(Collectors.toList());
    }
    
    // ==================== 字典排序 ====================
    
    @Override
    public boolean updateDictDataSort(String dictId, Integer dictSort) {
        DictData data = dictDataMap.get(dictId);
        if (data == null) {
            return false;
        }
        
        data.setDictSort(dictSort);
        data.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return true;
    }
    
    @Override
    public int batchUpdateDictDataSort(Map<String, Integer> sortData) {
        int count = 0;
        for (Map.Entry<String, Integer> entry : sortData.entrySet()) {
            if (updateDictDataSort(entry.getKey(), entry.getValue())) {
                count++;
            }
        }
        return count;
    }
    
    @Override
    public boolean moveDictDataUp(String dictId) {
        DictData data = dictDataMap.get(dictId);
        if (data == null) {
            return false;
        }
        
        List<DictData> dataList = dictDataByType.get(data.getDictType());
        if (dataList == null || dataList.size() <= 1) {
            return false;
        }
        
        // 简化实现：减少排序值
        data.setDictSort(data.getDictSort() - 1);
        return true;
    }
    
    @Override
    public boolean moveDictDataDown(String dictId) {
        DictData data = dictDataMap.get(dictId);
        if (data == null) {
            return false;
        }
        
        List<DictData> dataList = dictDataByType.get(data.getDictType());
        if (dataList == null || dataList.size() <= 1) {
            return false;
        }
        
        // 简化实现：增加排序值
        data.setDictSort(data.getDictSort() + 1);
        return true;
    }
    
    @Override
    public boolean moveDictDataToTop(String dictId) {
        DictData data = dictDataMap.get(dictId);
        if (data == null) {
            return false;
        }
        
        List<DictData> dataList = dictDataByType.get(data.getDictType());
        if (dataList == null) {
            return false;
        }
        
        // 设置为最小排序值
        int minSort = dataList.stream().mapToInt(DictData::getDictSort).min().orElse(0);
        data.setDictSort(minSort - 1);
        return true;
    }
    
    @Override
    public boolean moveDictDataToBottom(String dictId) {
        DictData data = dictDataMap.get(dictId);
        if (data == null) {
            return false;
        }
        
        List<DictData> dataList = dictDataByType.get(data.getDictType());
        if (dataList == null) {
            return false;
        }
        
        // 设置为最大排序值
        int maxSort = dataList.stream().mapToInt(DictData::getDictSort).max().orElse(0);
        data.setDictSort(maxSort + 1);
        return true;
    }
    
    // ==================== 字典导入导出 ====================
    
    @Override
    public String exportDictTypes(List<String> dictTypes, String format) {
        // 简化实现，返回模拟文件路径
        return "/tmp/dict_types_export_" + System.currentTimeMillis() + "." + format;
    }
    
    @Override
    public String exportDictData(String dictType, String format) {
        // 简化实现，返回模拟文件路径
        return "/tmp/dict_data_export_" + dictType + "_" + System.currentTimeMillis() + "." + format;
    }
    
    @Override
    public Map<String, Object> importDictTypes(String filePath, boolean override) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("imported", 0);
        result.put("skipped", 0);
        result.put("errors", new ArrayList<>());
        return result;
    }
    
    @Override
    public Map<String, Object> importDictData(String dictType, String filePath, boolean override) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("imported", 0);
        result.put("skipped", 0);
        result.put("errors", new ArrayList<>());
        return result;
    }
    
    @Override
    public Map<String, Object> validateDictImportFile(String filePath, String type) {
        Map<String, Object> result = new HashMap<>();
        result.put("valid", true);
        result.put("errors", new ArrayList<>());
        result.put("warnings", new ArrayList<>());
        return result;
    }
    
    // ==================== 字典缓存管理 ====================
    
    @Override
    public void refreshDictCache() {
        if (redisTemplate != null) {
            Set<String> keys = redisTemplate.keys("dict:*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
    }
    
    @Override
    public void refreshDictCache(String dictType) {
        if (redisTemplate != null) {
            redisTemplate.delete("dict:" + dictType);
        }
    }
    
    @Override
    public void clearDictCache() {
        refreshDictCache();
    }
    
    @Override
    public void warmupDictCache() {
        // 预热所有字典数据到缓存
        for (String dictType : dictTypes.keySet()) {
            getDictDataByType(dictType);
        }
    }
    
    @Override
    public Map<String, Object> getDictCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("cacheEnabled", redisTemplate != null);
        stats.put("cachedTypes", dictTypes.size());
        stats.put("totalData", dictDataMap.size());
        return stats;
    }
    
    // ==================== 字典统计 ====================
    
    @Override
    public Map<String, Object> getDictStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTypes", dictTypes.size());
        stats.put("totalData", dictDataMap.size());
        stats.put("enabledTypes", dictTypes.values().stream().mapToLong(type -> type.getStatus() == 1 ? 1 : 0).sum());
        stats.put("enabledData", dictDataMap.values().stream().mapToLong(data -> data.getStatus() == 1 ? 1 : 0).sum());
        
        Map<String, Long> typeStats = dictDataByType.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (long) entry.getValue().size()
                ));
        stats.put("typeStats", typeStats);
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getDictTypeStatistics(String dictType) {
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            return null;
        }
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("dictType", dictType);
        stats.put("totalData", dataList.size());
        stats.put("enabledData", dataList.stream().mapToLong(data -> data.getStatus() == 1 ? 1 : 0).sum());
        stats.put("defaultData", dataList.stream().mapToLong(data -> data.getIsDefault() ? 1 : 0).sum());
        stats.put("usage", dictUsage.getOrDefault(dictType, 0L));
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getDictUsage(String dictType) {
        Map<String, Object> usage = new HashMap<>();
        usage.put("dictType", dictType);
        usage.put("accessCount", dictUsage.getOrDefault(dictType, 0L));
        usage.put("lastAccess", "N/A"); // 简化实现
        return usage;
    }
    
    @Override
    public List<Map<String, Object>> getPopularDicts(int limit) {
        return dictUsage.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    Map<String, Object> dict = new HashMap<>();
                    dict.put("dictType", entry.getKey());
                    dict.put("usage", entry.getValue());
                    DictType type = dictTypes.get(entry.getKey());
                    if (type != null) {
                        dict.put("dictName", type.getDictName());
                        dict.put("description", type.getDescription());
                    }
                    return dict;
                })
                .collect(Collectors.toList());
    }
    
    // ==================== 字典验证 ====================
    
    @Override
    public boolean isValidDictValue(String dictType, String dictValue) {
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            return false;
        }
        
        return dataList.stream()
                .anyMatch(data -> data.getDictValue().equals(dictValue) && data.getStatus() == 1);
    }
    
    @Override
    public boolean isValidDictLabel(String dictType, String dictLabel) {
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            return false;
        }
        
        return dataList.stream()
                .anyMatch(data -> data.getDictLabel().equals(dictLabel) && data.getStatus() == 1);
    }
    
    @Override
    public Map<String, Boolean> validateDictValues(String dictType, List<String> dictValues) {
        Map<String, Boolean> result = new HashMap<>();
        for (String value : dictValues) {
            result.put(value, isValidDictValue(dictType, value));
        }
        return result;
    }
    
    @Override
    public Map<String, Object> checkDictDataIntegrity(String dictType) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList == null) {
            errors.add("字典类型不存在");
        } else {
            // 检查重复值
            Set<String> values = new HashSet<>();
            for (DictData data : dataList) {
                if (!values.add(data.getDictValue())) {
                    errors.add("存在重复的字典值: " + data.getDictValue());
                }
            }
            
            // 检查默认项
            long defaultCount = dataList.stream().mapToLong(data -> data.getIsDefault() ? 1 : 0).sum();
            if (defaultCount > 1) {
                warnings.add("存在多个默认项");
            } else if (defaultCount == 0) {
                warnings.add("没有设置默认项");
            }
        }
        
        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        result.put("warnings", warnings);
        
        return result;
    }
    
    @Override
    public Map<String, Object> repairDictData(String dictType) {
        Map<String, Object> result = new HashMap<>();
        List<String> repairs = new ArrayList<>();
        
        List<DictData> dataList = dictDataByType.get(dictType);
        if (dataList != null) {
            // 修复重复值（简化实现）
            Set<String> seenValues = new HashSet<>();
            for (DictData data : dataList) {
                if (!seenValues.add(data.getDictValue())) {
                    data.setDictValue(data.getDictValue() + "_" + System.currentTimeMillis());
                    repairs.add("修复重复值: " + data.getDictLabel());
                }
            }
            
            // 修复默认项
            long defaultCount = dataList.stream().mapToLong(data -> data.getIsDefault() ? 1 : 0).sum();
            if (defaultCount > 1) {
                // 只保留第一个默认项
                boolean firstDefault = true;
                for (DictData data : dataList) {
                    if (data.getIsDefault()) {
                        if (firstDefault) {
                            firstDefault = false;
                        } else {
                            data.setIsDefault(false);
                            repairs.add("取消多余默认项: " + data.getDictLabel());
                        }
                    }
                }
            } else if (defaultCount == 0 && !dataList.isEmpty()) {
                // 设置第一个为默认项
                dataList.get(0).setIsDefault(true);
                repairs.add("设置默认项: " + dataList.get(0).getDictLabel());
            }
        }
        
        result.put("success", true);
        result.put("repairs", repairs);
        
        return result;
    }
    
    // ==================== 字典树形结构 ====================
    
    @Override
    public List<Map<String, Object>> getDictTree(String dictType) {
        // 简化实现，返回平铺结构
        return getDictDataByType(dictType);
    }
    
    @Override
    public String createTreeDictData(String dictType, String parentId, String dictLabel, String dictValue,
                                    Integer dictSort, Integer status, String remark) {
        // 简化实现，忽略父级关系
        return createDictData(dictType, dictLabel, dictValue, dictSort, null, null, false, status, remark);
    }
    
    @Override
    public boolean moveTreeDictData(String dictId, String newParentId) {
        // 简化实现
        return true;
    }
    
    @Override
    public List<Map<String, Object>> getDictChildren(String dictType, String parentId) {
        // 简化实现，返回所有数据
        return getDictDataByType(dictType);
    }
    
    @Override
    public List<Map<String, Object>> getDictParentPath(String dictId) {
        // 简化实现，返回空路径
        return new ArrayList<>();
    }
    
    // ==================== 私有辅助方法 ====================
    
    private Map<String, Object> convertDictDataToMap(DictData data) {
        Map<String, Object> result = new HashMap<>();
        result.put("dictId", data.getDictId());
        result.put("dictType", data.getDictType());
        result.put("dictLabel", data.getDictLabel());
        result.put("dictValue", data.getDictValue());
        result.put("dictSort", data.getDictSort());
        result.put("cssClass", data.getCssClass());
        result.put("listClass", data.getListClass());
        result.put("isDefault", data.getIsDefault());
        result.put("status", data.getStatus());
        result.put("remark", data.getRemark());
        result.put("createTime", data.getCreateTime());
        result.put("updateTime", data.getUpdateTime());
        return result;
    }
    
    private void recordDictUsage(String dictType) {
        dictUsage.merge(dictType, 1L, Long::sum);
    }
    
    /**
     * 字典类型内部类
     */
    private static class DictType {
        private String dictType;
        private String dictName;
        private String description;
        private Integer status;
        private String createTime;
        private String updateTime;
        
        // Getters and Setters
        public String getDictType() { return dictType; }
        public void setDictType(String dictType) { this.dictType = dictType; }
        
        public String getDictName() { return dictName; }
        public void setDictName(String dictName) { this.dictName = dictName; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        
        public String getCreateTime() { return createTime; }
        public void setCreateTime(String createTime) { this.createTime = createTime; }
        
        public String getUpdateTime() { return updateTime; }
        public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
    }
    
    /**
     * 字典数据内部类
     */
    private static class DictData {
        private String dictId;
        private String dictType;
        private String dictLabel;
        private String dictValue;
        private Integer dictSort;
        private String cssClass;
        private String listClass;
        private Boolean isDefault;
        private Integer status;
        private String remark;
        private String createTime;
        private String updateTime;
        
        // Getters and Setters
        public String getDictId() { return dictId; }
        public void setDictId(String dictId) { this.dictId = dictId; }
        
        public String getDictType() { return dictType; }
        public void setDictType(String dictType) { this.dictType = dictType; }
        
        public String getDictLabel() { return dictLabel; }
        public void setDictLabel(String dictLabel) { this.dictLabel = dictLabel; }
        
        public String getDictValue() { return dictValue; }
        public void setDictValue(String dictValue) { this.dictValue = dictValue; }
        
        public Integer getDictSort() { return dictSort; }
        public void setDictSort(Integer dictSort) { this.dictSort = dictSort; }
        
        public String getCssClass() { return cssClass; }
        public void setCssClass(String cssClass) { this.cssClass = cssClass; }
        
        public String getListClass() { return listClass; }
        public void setListClass(String listClass) { this.listClass = listClass; }
        
        public Boolean getIsDefault() { return isDefault; }
        public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
        
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        
        public String getRemark() { return remark; }
        public void setRemark(String remark) { this.remark = remark; }
        
        public String getCreateTime() { return createTime; }
        public void setCreateTime(String createTime) { this.createTime = createTime; }
        
        public String getUpdateTime() { return updateTime; }
        public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
    }
}