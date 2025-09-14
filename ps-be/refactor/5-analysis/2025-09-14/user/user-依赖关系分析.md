# user模块依赖关系分析报告

## 1. 模块内部依赖关系

### 1.1 接口适配器层依赖
- **UserController** 依赖:
  - UserApplicationService (应用服务层)
  - PersonService (外部服务)
  - UserCreateDTO, UserQueryDTO, UserResponseDTO, UserUpdateDTO (应用层DTO)
  - PageResult (接口层DTO)
  - JsonResponse (公共组件)

- **其他Controller** 依赖:
  - 相应的应用服务接口
  - 对应的DTO类
  - JsonResponse (公共组件)

### 1.2 应用服务层依赖
- **UserApplicationService** 依赖:
  - UserRepository (领域层仓储接口)
  - UserDomainService (领域层服务)
  - UserAssembler (应用层装配器)
  - UserCreateDTO, UserQueryDTO, UserResponseDTO, UserUpdateDTO (应用层DTO)
  - User (领域层实体)
  - UserCreatedEvent, UserDeletedEvent, UserUpdatedEvent (领域层事件)
  - ApplicationEventPublisher (Spring事件发布器)

### 1.3 领域层依赖
- **User实体** 依赖:
  - UserProfile, UserAccount, ContactInfo, UserStatus, UserCategory (领域层值对象/实体)
  - UserCreatedEvent, UserAccountCreatedEvent, UserProfileUpdatedEvent, UserEvent (领域层事件)

- **UserDomainService** 依赖:
  - 相关的领域实体和值对象

- **UserRepository** (接口):
  - 无外部依赖，定义仓储接口

### 1.4 基础设施层依赖
- **UserRepositoryImpl** 依赖:
  - UserRepository (领域层接口)
  - UserPO, AccountPO (基础设施层实体)
  - UserMapper, AccountMapper, UserAccountMapper, UserPersonMapper (MyBatis映射器)
  - UserPOAssembler (基础设施层装配器)

## 2. 外部模块依赖

### 2.1 项目内依赖
- **com.jiuxi.common**: 
  - JsonResponse (响应封装)
  - Validation groups (AddGroup, UpdateGroup)
  - Annotations (Authorization, IgnoreAuthorization)

- **com.jiuxi.shared**: 
  - PersonVO (用户信息值对象)
  - PersonService (用户信息服务)

- **com.jiuxi.security**: 
  - PersonVO (安全相关用户信息)

### 2.2 第三方依赖
- **Spring Framework**:
  - Spring Web (RestController, RequestMapping等)
  - Spring Context (Autowired, Component, Service等)
  - Spring Transaction (Transactional)
  - Spring Event (ApplicationEventPublisher)

- **MyBatis**:
  - Mapper注解
  - Repository相关注解

- **Validation**:
  - javax.validation (Validated注解)
  - Validation groups

- **Logging**:
  - Slf4j (日志记录)

- **Lombok**:
  - Data, Getter, Setter等注解

## 3. 依赖关系图

```
接口适配器层 (intf/web)
    ↓ (依赖)
应用服务层 (app/service)
    ↓ (依赖)
领域层 (domain)
    ↓ (依赖)
基础设施层 (infra/persistence)
```

## 4. 循环依赖风险分析

### 4.1 当前存在的问题
1. **应用服务层与领域层之间**: UserApplicationService直接依赖User实体，符合DDD设计
2. **基础设施层与领域层之间**: UserRepositoryImpl实现UserRepository接口，符合依赖倒置原则
3. **接口层与应用层之间**: Controller直接依赖ApplicationService，符合六边形架构

### 4.2 潜在风险
1. **DTO跨层使用**: 当前DTO在接口层和应用层之间直接使用，重构时需要考虑转换
2. **事件发布依赖**: 领域事件通过Spring事件机制发布，存在对框架的依赖

## 5. 重构建议

### 5.1 依赖优化
1. 在接口层和应用层之间增加DTO转换器，避免直接使用应用层DTO
2. 领域事件发布考虑使用领域事件发布器而非直接依赖Spring机制
3. 领域层实体避免直接暴露给接口层，通过应用层进行隔离

### 5.2 分层规范
1. 严格遵循依赖方向: intf → app → domain → infra
2. 基础设施层只能被领域层通过接口依赖
3. 接口层只能依赖应用层，不能直接依赖领域层

## 6. 依赖关系验证
- [x] 接口层只依赖应用层
- [x] 应用层只依赖领域层
- [x] 领域层不依赖外层
- [x] 基础设施层正确实现领域层接口
- [x] 无循环依赖