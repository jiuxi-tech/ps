# sys模块接口说明文档

> **生成时间**：2025-09-18  
> **模块名称**：sys（系统管理模块）  
> **文档版本**：v1.0

## 1. 接口概述

sys模块提供系统基础配置管理功能，包括字典管理、字典项管理和系统配置管理。所有接口都提供标准的RESTful API。

## 2. 字典管理接口

### 2.1 创建字典
- **接口路径**：`POST /system/dictionary`
- **接口作用**：创建新的数据字典
- **请求参数**：DictionaryCreateDTO
  - dictCode (String): 字典编码，必填
  - dictName (String): 字典名称，必填  
  - dictDesc (String): 字典描述，可选
  - dictType (String): 字典类型，可选
  - status (String): 状态，可选
  - orderIndex (Integer): 排序序号，可选
  - dictGroup (String): 字典分组，可选
  - parentDictId (String): 上级字典ID，可选
  - tenantId (String): 租户ID，可选
- **返回类型**：Map<String, Object>
  - code (Integer): 响应码，200-成功，500-失败
  - message (String): 响应消息
  - data (DictionaryResponseDTO): 创建后的字典数据，成功时返回

### 2.2 更新字典
- **接口路径**：`PUT /system/dictionary`
- **接口作用**：更新已存在的数据字典
- **请求参数**：DictionaryUpdateDTO
  - dictId (String): 字典ID，必填
  - dictCode (String): 字典编码，可选
  - dictName (String): 字典名称，可选
  - dictDesc (String): 字典描述，可选
  - dictType (String): 字典类型，可选
  - status (String): 状态，可选
  - orderIndex (Integer): 排序序号，可选
- **返回类型**：Map<String, Object>
  - code (Integer): 响应码，200-成功，500-失败
  - message (String): 响应消息
  - data (DictionaryResponseDTO): 更新后的字典数据，成功时返回

### 2.3 删除字典
- **接口路径**：`DELETE /system/dictionary/{dictId}`
- **接口作用**：根据字典ID删除字典
- **路径参数**：
  - dictId (String): 字典ID，必填
- **返回类型**：Map<String, Object>
  - code (Integer): 响应码，200-成功，500-失败
  - message (String): 响应消息

### 2.4 根据ID获取字典
- **接口路径**：`GET /system/dictionary/{dictId}`
- **接口作用**：根据字典ID查询字典详情
- **路径参数**：
  - dictId (String): 字典ID，必填
- **返回类型**：Map<String, Object>
  - code (Integer): 响应码，200-成功，404-不存在，500-失败
  - message (String): 响应消息
  - data (DictionaryResponseDTO): 字典数据，成功时返回

### 2.5 根据编码获取字典
- **接口路径**：`GET /system/dictionary/code/{dictCode}`
- **接口作用**：根据字典编码查询字典详情
- **路径参数**：
  - dictCode (String): 字典编码，必填
- **查询参数**：
  - tenantId (String): 租户ID，可选，默认值为"default"
- **返回类型**：Map<String, Object>
  - code (Integer): 响应码，200-成功，404-不存在，500-失败
  - message (String): 响应消息
  - data (DictionaryResponseDTO): 字典数据，成功时返回

## 3. 系统配置管理接口

### 3.1 分页查询配置列表
- **接口路径**：`POST /sys/config/list`
- **接口作用**：分页查询系统配置列表
- **请求参数**：TpSystemConfigQuery
  - 分页参数和查询条件
- **返回类型**：JsonResponse
  - data: IPage<TpSystemConfigVO> - 分页数据

### 3.2 批量删除配置
- **接口路径**：`POST /sys/config/delete`
- **接口作用**：批量删除系统配置
- **请求参数**：
  - configKeys (String): 配置键列表，用逗号分隔
- **返回类型**：JsonResponse
  - 标准成功响应

### 3.3 保存或更新配置
- **接口路径**：`POST /sys/config/save`
- **接口作用**：保存或更新系统配置参数
- **请求参数**：
  - configKey (String): 配置键，必填
  - configValue (String): 配置值，必填
  - description (String): 配置描述，可选
- **返回类型**：JsonResponse
  - 标准成功响应

### 3.4 查看配置详情
- **接口路径**：`POST /sys/config/view`
- **接口作用**：查看单个配置的详细信息
- **请求参数**：
  - configKey (String): 配置键，必填
- **返回类型**：JsonResponse
  - data: TpSystemConfig - 配置详情

### 3.5 根据键获取配置值（路径参数）
- **接口路径**：`POST /sys/config/value/{configKey}`
- **接口作用**：根据配置键获取配置值
- **路径参数**：
  - configKey (String): 配置键，必填
- **返回类型**：JsonResponse
  - data: String - 配置值

### 3.6 根据键获取配置值（查询参数）
- **接口路径**：`POST /sys/config/get`
- **接口作用**：根据配置键获取配置值
- **请求参数**：
  - configKey (String): 配置键，必填
- **返回类型**：JsonResponse
  - data: String - 配置值

### 3.7 获取所有配置
- **接口路径**：`POST /sys/config/all`
- **接口作用**：获取系统所有配置的键值对映射
- **请求参数**：无
- **返回类型**：JsonResponse
  - data: Map<String, String> - 所有配置的键值对

### 3.8 批量更新配置
- **接口路径**：`POST /sys/config/batch-update`
- **接口作用**：批量更新多个配置参数
- **请求参数**：Map<String, String>
  - 配置键值对映射
- **返回类型**：JsonResponse
  - 标准成功响应

### 3.9 测试配置接口
- **接口路径**：`POST /sys/config/test_config_management`
- **接口作用**：测试系统配置管理功能是否正常
- **请求参数**：无
- **返回类型**：JsonResponse
  - data: String - "系统配置管理功能正常"

## 4. 字典项管理接口

基于模块结构分析，存在DictionaryItemController，但具体接口实现需要进一步分析。

## 5. 响应数据结构

### 5.1 DictionaryResponseDTO
```json
{
  "dictId": "字典ID",
  "dictCode": "字典编码", 
  "dictName": "字典名称",
  "dictDesc": "字典描述",
  "dictType": "字典类型",
  "status": "状态",
  "dictGroup": "字典分组",
  "isSystemPreset": "是否系统预置",
  "parentDictId": "上级字典ID",
  "orderIndex": "排序序号",
  "creator": "创建人",
  "createTime": "创建时间",
  "updator": "更新人", 
  "updateTime": "更新时间",
  "tenantId": "租户ID"
}
```

### 5.2 JsonResponse
```json
{
  "code": "响应码",
  "message": "响应消息", 
  "data": "响应数据",
  "success": "是否成功"
}
```

## 6. 权限说明

- **字典管理接口**：无特殊权限要求
- **系统配置管理接口**：需要@Authorization注解验证权限

## 7. 注意事项

1. 所有接口都需要正确处理异常，避免500错误
2. 字典管理接口使用Map返回，系统配置接口使用JsonResponse返回
3. 字典支持多租户，通过tenantId区分
4. 系统配置支持热重载和缓存机制
5. 批量操作需要注意事务管理