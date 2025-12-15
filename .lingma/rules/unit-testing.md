---
trigger: specific_files
glob: "**/*test.java,**/test/**/*.java,**/*Test.java"
description: 单元测试编写规范，包括测试类结构、测试方法命名、断言使用等
---

# 单元测试编写规范

## 一、测试类结构

### 1. 测试类命名

```java
/**
 * 规则：被测试类名 + Test
 */

// 被测试类
public class UserService { }

// ✅ 正确的测试类
public class UserServiceTest { }

// ❌ 错误的测试类
public class TestUserService { }
public class UserServiceTestCase { }
```

### 2. 测试类注解

```java
/**
 * 用户服务测试类
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    
    @Autowired
    private IUserService userService;
    
    // 测试方法
    
}
```

### 3. 必须的注解

```java
/**
 * @SpringBootTest: 标识这是一个 Spring Boot 测试类
 * @RunWith: 指定测试运行器
 */
@SpringBootTest
@RunWith(SpringRunner.class)
```

## 二、测试方法规范

### 1. 测试方法命名

```java
/**
 * 规则：test + 被测试方法名 + 测试场景
 * 或：should + 预期结果 + when + 测试条件
 */

// ✅ 正确示例
@Test
public void testAddUser_Success() { }

@Test
public void testAddUser_WhenNameExists_ShouldThrowException() { }

@Test
public void testQueryById_WhenIdNotExists_ShouldReturnNull() { }

@Test
public void shouldReturnUserList_WhenQueryWithValidParams() { }

// ❌ 错误示例
@Test
public void test1() { }

@Test
public void addUser() { }
```

### 2. 测试方法结构（AAA 模式）

```java
/**
 * AAA 模式：
 * - Arrange: 准备测试数据
 * - Act: 执行被测试方法
 * - Assert: 断言结果
 */

@Test
public void testAddUser_Success() {
    // Arrange - 准备测试数据
    UserDTO dto = new UserDTO();
    dto.setUsername("testuser");
    dto.setRealname("测试用户");
    dto.setPassword("123456");
    
    // Act - 执行被测试方法
    userService.add(dto);
    
    // Assert - 断言结果
    UserEntity user = userService.getOne(
        new LambdaQueryWrapper<UserEntity>()
            .eq(UserEntity::getUsername, "testuser")
    );
    assertNotNull(user);
    assertEquals("testuser", user.getUsername());
    assertEquals("测试用户", user.getRealname());
}
```

## 三、断言使用

### 1. JUnit 断言

```java
import static org.junit.Assert.*;

/**
 * 相等断言
 */
assertEquals("期望值", "实际值");
assertEquals(100, result);

/**
 * 不相等断言
 */
assertNotEquals("期望值", "实际值");

/**
 * 为真断言
 */
assertTrue(condition);
assertTrue(result > 0);

/**
 * 为假断言
 */
assertFalse(condition);
assertFalse(result < 0);

/**
 * 非空断言
 */
assertNotNull(object);
assertNotNull(userService.queryById("123"));

/**
 * 为空断言
 */
assertNull(object);
assertNull(userService.queryById("notexist"));

/**
 * 数组相等断言
 */
assertArrayEquals(expectedArray, actualArray);
```

### 2. 断言消息

```java
/**
 * 断言失败时的提示消息
 */

// ✅ 推荐：添加失败消息
assertEquals("用户名应该相等", "expected", user.getUsername());
assertTrue("结果应该大于0", result > 0);
assertNotNull("用户对象不应该为空", user);

// ❌ 不推荐：没有失败消息
assertEquals("expected", user.getUsername());
assertTrue(result > 0);
```

## 四、异常测试

### 1. 测试异常抛出

```java
/**
 * 方式1：使用 @Test(expected)
 */
@Test(expected = BusinessException.class)
public void testAddUser_WhenNameExists_ShouldThrowException() {
    UserDTO dto = new UserDTO();
    dto.setUsername("admin");  // 假设 admin 已存在
    
    userService.add(dto);  // 应该抛出 BusinessException
}

/**
 * 方式2：使用 try-catch
 */
@Test
public void testAddUser_WhenNameExists_ShouldThrowException() {
    UserDTO dto = new UserDTO();
    dto.setUsername("admin");
    
    try {
        userService.add(dto);
        fail("应该抛出 BusinessException");
    } catch (BusinessException e) {
        assertEquals("名称已存在", e.getMessage());
    }
}

/**
 * 方式3：使用 assertThrows（JUnit 5）
 */
@Test
public void testAddUser_WhenNameExists_ShouldThrowException() {
    UserDTO dto = new UserDTO();
    dto.setUsername("admin");
    
    BusinessException exception = assertThrows(
        BusinessException.class,
        () -> userService.add(dto)
    );
    
    assertEquals("名称已存在", exception.getMessage());
}
```

## 五、Mock 使用

### 1. Mock 依赖对象

```java
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    
    /**
     * @InjectMocks: 创建被测试对象，并注入 Mock 对象
     * @Mock: 创建 Mock 对象
     */
    @InjectMocks
    private UserServiceImpl userService;
    
    @Mock
    private UserMapper userMapper;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
}
```

### 2. Mock 方法返回值

```java
/**
 * when().thenReturn(): 指定方法返回值
 */
@Test
public void testQueryById_Success() {
    // Arrange
    UserVO expectedUser = new UserVO();
    expectedUser.setId("123");
    expectedUser.setUsername("testuser");
    
    when(userMapper.queryById("123")).thenReturn(expectedUser);
    
    // Act
    UserVO actualUser = userService.queryById("123");
    
    // Assert
    assertNotNull(actualUser);
    assertEquals("123", actualUser.getId());
    assertEquals("testuser", actualUser.getUsername());
}
```

### 3. Mock 方法抛出异常

```java
/**
 * when().thenThrow(): 指定方法抛出异常
 */
@Test(expected = BusinessException.class)
public void testQueryById_WhenDatabaseError_ShouldThrowException() {
    // Arrange
    when(userMapper.queryById("123")).thenThrow(new RuntimeException("数据库错误"));
    
    // Act
    userService.queryById("123");  // 应该抛出异常
}
```

### 4. 验证方法调用

```java
/**
 * verify(): 验证方法是否被调用
 */
@Test
public void testDeleteUser_Success() {
    // Arrange
    String userId = "123";
    
    // Act
    userService.removeById(userId);
    
    // Assert
    verify(userMapper, times(1)).deleteById(userId);
}

/**
 * 验证方法未被调用
 */
@Test
public void testDeleteUser_WhenIdIsNull_ShouldNotCallMapper() {
    // Act
    try {
        userService.removeById(null);
    } catch (Exception e) {
        // 忽略异常
    }
    
    // Assert
    verify(userMapper, never()).deleteById(any());
}
```

## 六、测试数据准备

### 1. @Before 方法

```java
/**
 * @Before: 每个测试方法执行前都会执行
 */
@Before
public void setup() {
    // 准备通用测试数据
    testUser = new UserDTO();
    testUser.setUsername("testuser");
    testUser.setRealname("测试用户");
}

@Test
public void testAddUser() {
    userService.add(testUser);
    // ...
}
```

### 2. @BeforeClass 方法

```java
/**
 * @BeforeClass: 所有测试方法执行前执行一次
 * 必须是静态方法
 */
@BeforeClass
public static void init() {
    // 初始化配置
    System.setProperty("env", "test");
}
```

### 3. @After 方法

```java
/**
 * @After: 每个测试方法执行后都会执行
 */
@After
public void cleanup() {
    // 清理测试数据
    userService.remove(
        new LambdaQueryWrapper<UserEntity>()
            .eq(UserEntity::getUsername, "testuser")
    );
}
```

### 4. @AfterClass 方法

```java
/**
 * @AfterClass: 所有测试方法执行后执行一次
 * 必须是静态方法
 */
@AfterClass
public static void destroy() {
    // 清理资源
}
```

## 七、参数化测试

### 1. 使用 @ParameterizedTest

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * 测试多个输入值
 */
@ParameterizedTest
@ValueSource(strings = {"admin", "user", "guest"})
public void testQueryByUsername_WithMultipleUsernames(String username) {
    UserVO user = userService.queryByUsername(username);
    assertNotNull(user);
}

/**
 * 测试多个整数值
 */
@ParameterizedTest
@ValueSource(ints = {1, 2, 3, 4, 5})
public void testQueryByType_WithMultipleTypes(int type) {
    List<UserVO> list = userService.queryByType(type);
    assertTrue(list.size() > 0);
}
```

### 2. 使用 @CsvSource

```java
import org.junit.jupiter.params.provider.CsvSource;

/**
 * 测试多组参数
 */
@ParameterizedTest
@CsvSource({
    "admin, 管理员",
    "user, 普通用户",
    "guest, 访客"
})
public void testAddUser_WithMultipleUsers(String username, String realname) {
    UserDTO dto = new UserDTO();
    dto.setUsername(username);
    dto.setRealname(realname);
    
    userService.add(dto);
    
    UserEntity user = userService.getOne(
        new LambdaQueryWrapper<UserEntity>()
            .eq(UserEntity::getUsername, username)
    );
    assertNotNull(user);
    assertEquals(realname, user.getRealname());
}
```

## 八、测试覆盖率

### 1. 测试覆盖目标

- **类覆盖率**：≥ 80%
- **方法覆盖率**：≥ 80%
- **行覆盖率**：≥ 70%
- **分支覆盖率**：≥ 60%

### 2. 必须测试的方法

1. **Service 层的所有业务方法**
2. **Controller 层的所有接口方法**
3. **工具类的所有公共方法**
4. **复杂的业务逻辑**
5. **异常处理逻辑**

### 3. 可以不测试的内容

1. **getter/setter 方法**
2. **简单的构造方法**
3. **配置类**
4. **常量定义**

## 九、测试规范

### 1. 测试方法规范

```java
/**
 * 1. 每个测试方法只测试一个功能点
 * 2. 测试方法名要清楚描述测试内容
 * 3. 测试方法要独立，不依赖其他测试
 * 4. 测试方法要可重复执行
 */

// ✅ 正确示例：独立的测试方法
@Test
public void testAddUser_Success() { }

@Test
public void testAddUser_WhenNameExists() { }

@Test
public void testAddUser_WhenNameIsNull() { }

// ❌ 错误示例：一个方法测试多个功能
@Test
public void testAddUser() {
    // 测试正常添加
    userService.add(dto1);
    
    // 测试名称重复
    userService.add(dto2);
    
    // 测试名称为空
    userService.add(dto3);
}
```

### 2. 测试数据规范

```java
/**
 * 1. 使用有意义的测试数据
 * 2. 避免使用生产数据
 * 3. 测试后清理数据
 */

// ✅ 正确示例
@Test
public void testAddUser_Success() {
    UserDTO dto = new UserDTO();
    dto.setUsername("test_user_" + System.currentTimeMillis());
    dto.setRealname("测试用户");
    
    userService.add(dto);
    // ...
}

@After
public void cleanup() {
    // 清理测试数据
}

// ❌ 错误示例
@Test
public void testAddUser_Success() {
    UserDTO dto = new UserDTO();
    dto.setUsername("admin");  // 可能与生产数据冲突
    
    userService.add(dto);
    // 没有清理数据
}
```

## 十、常见错误和解决方案

### 1. ❌ 错误：测试方法没有 @Test 注解

```java
// ❌ 错误
public void testAddUser() { }

// ✅ 正确
@Test
public void testAddUser() { }
```

### 2. ❌ 错误：没有断言

```java
// ❌ 错误：只执行方法，没有验证结果
@Test
public void testAddUser() {
    userService.add(dto);
}

// ✅ 正确：有断言
@Test
public void testAddUser() {
    userService.add(dto);
    
    UserEntity user = userService.getOne(
        new LambdaQueryWrapper<UserEntity>()
            .eq(UserEntity::getUsername, dto.getUsername())
    );
    assertNotNull(user);
}
```

### 3. ❌ 错误：测试方法相互依赖

```java
// ❌ 错误：test2 依赖 test1 的执行结果
@Test
public void test1_AddUser() {
    userService.add(dto);
}

@Test
public void test2_QueryUser() {
    UserEntity user = userService.queryByUsername("testuser");
    assertNotNull(user);  // 依赖 test1 已经添加了数据
}

// ✅ 正确：每个测试独立
@Test
public void testAddUser() {
    userService.add(dto);
    // ...
}

@Test
public void testQueryUser() {
    // 自己准备数据
    userService.add(dto);
    
    UserEntity user = userService.queryByUsername("testuser");
    assertNotNull(user);
}
```

### 4. ❌ 错误：没有清理测试数据

```java
// ❌ 错误
@Test
public void testAddUser() {
    userService.add(dto);
    // 没有清理数据
}

// ✅ 正确
@Test
public void testAddUser() {
    userService.add(dto);
}

@After
public void cleanup() {
    userService.remove(
        new LambdaQueryWrapper<UserEntity>()
            .eq(UserEntity::getUsername, "testuser")
    );
}
```

## 十一、必须遵守的规则

1. **测试类名必须是被测试类名 + Test**
2. **测试类必须使用 @SpringBootTest 和 @RunWith 注解**
3. **测试方法必须使用 @Test 注解**
4. **测试方法必须有断言**
5. **测试方法必须独立，不依赖其他测试**
6. **测试数据必须在测试后清理**
7. **测试方法名必须清楚描述测试内容**
8. **每个测试方法只测试一个功能点**
9. **异常测试必须验证异常类型和消息**
10. **Mock 对象必须在 @Before 中初始化**
