# Common Package Mapping Guide

## 包映射关系 (Package Mapping)

### 响应类 (Response Classes)
- `com.jiuxi.common.bean.JsonResponse` → `com.jiuxi.shared.common.base.response.BaseResponse`
- `com.jiuxi.common.bean.ApiResponse` → `com.jiuxi.shared.common.base.response.BaseResponse`
- `com.jiuxi.common.bean.PageResponse` → `com.jiuxi.shared.common.base.response.PageResponse`

### 常量类 (Constants)
- `com.jiuxi.common.constant.TpConstant` → `com.jiuxi.shared.common.constants.SystemConstants`
- `com.jiuxi.common.constants.ApiConstants` → `com.jiuxi.shared.common.constants.ApiConstants`

### 异常类 (Exceptions)
- `com.jiuxi.common.exception.ExceptionUtils` → `com.jiuxi.shared.common.exception.BaseException`
- `com.jiuxi.common.exception.BusinessException` → `com.jiuxi.shared.common.exception.BusinessException`
- `com.jiuxi.common.exception.ValidationException` → `com.jiuxi.shared.common.exception.ValidationException`
- `com.jiuxi.common.exception.GlobalExceptionHandler` → `com.jiuxi.shared.common.handler.GlobalExceptionHandler`

### 工具类 (Utilities)
- `com.jiuxi.common.util.CommonDateUtil` → `com.jiuxi.shared.common.utils.DateUtils`
- `com.jiuxi.common.util.SnowflakeIdUtil` → 保留原位置 (工具类暂时不迁移)
- `com.jiuxi.common.util.DateUtils` → `com.jiuxi.shared.common.utils.DateUtils`
- `com.jiuxi.common.util.FileUtils` → `com.jiuxi.shared.common.utils.FileUtils`

### 验证注解 (Validation Annotations)
- `com.jiuxi.common.validation.annotations.Phone` → `com.jiuxi.shared.common.validation.annotations.Phone` (需要创建)
- `com.jiuxi.common.validation.annotations.Email` → `com.jiuxi.shared.common.validation.annotations.Email` (需要创建)
- `com.jiuxi.common.validation.annotations.IdCard` → `com.jiuxi.shared.common.validation.annotations.IdCard` (需要创建)

### 验证器 (Validators)
- `com.jiuxi.common.validation.validators.PhoneValidator` → `com.jiuxi.shared.common.validation.validators.PhoneValidator` (需要创建)
- `com.jiuxi.common.validation.validators.EmailValidator` → `com.jiuxi.shared.common.validation.validators.EmailValidator` (需要创建)
- `com.jiuxi.common.validation.validators.IdCardValidator` → `com.jiuxi.shared.common.validation.validators.IdCardValidator` (需要创建)

### 实体类 (Entities)
- `com.jiuxi.common.bean.TreeNode` → `com.jiuxi.shared.common.utils.TreeUtils` (方法迁移)

### 枚举类 (Enums)
- `com.jiuxi.common.enums.OperateType` → `com.jiuxi.shared.common.enums.OperationTypeEnum`

## 处理策略

1. **暂时保留的包**: 部分复杂工具类暂时保留在原位置，避免大范围影响
2. **需要补充的类**: shared/common中缺少的类需要从old common中复制或重新实现
3. **兼容性**: 优先保证系统功能正常，渐进式迁移