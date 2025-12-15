# 日期时间格式规范

## 文档说明

本文档定义了PS-BMP系统中日期时间的统一处理规范，包括存储格式、传输格式、显示格式以及转换方法。

---

## 1. 格式定义

### 1.1 各层格式标准

| 层次 | 格式 | 示例 | 说明 |
|------|------|------|------|
| 数据库存储 | `VARCHAR(14)` 或 `BIGINT` | `20241201153045` | YYYYMMDDHHmmss字符串 |
| 后端传输 | `String` | `"20241201153045"` | YYYYMMDDHHmmss字符串 |
| 前端接收 | `string` | `"20241201153045"` | YYYYMMDDHHmmss字符串 |
| 前端显示 | `string` | `"2024-12-01 15:30:45"` | YYYY-MM-DD HH:mm:ss |
| 前端编辑 | `Date` | `new Date()` | JavaScript Date对象 |

### 1.2 标准格式

#### 完整日期时间
- **存储格式**: `YYYYMMDDHHmmss` （14位数字字符串）
- **显示格式**: `YYYY-MM-DD HH:mm:ss`
- **示例**: 
  - 存储: `20241201153045`
  - 显示: `2024-12-01 15:30:45`

#### 日期
- **存储格式**: `YYYYMMDD` （8位数字字符串）
- **显示格式**: `YYYY-MM-DD`
- **示例**:
  - 存储: `20241201`
  - 显示: `2024-12-01`

#### 时间
- **存储格式**: `HHmmss` （6位数字字符串）
- **显示格式**: `HH:mm:ss`
- **示例**:
  - 存储: `153045`
  - 显示: `15:30:45`

---

## 2. 后端处理

### 2.1 数据库字段定义

```sql
-- 推荐方式1: 使用VARCHAR存储
CREATE TABLE tp_example (
    id VARCHAR(32) PRIMARY KEY,
    create_time VARCHAR(14) COMMENT '创建时间 格式:YYYYMMDDHHmmss',
    update_time VARCHAR(14) COMMENT '更新时间 格式:YYYYMMDDHHmmss',
    expire_date VARCHAR(8) COMMENT '过期日期 格式:YYYYMMDD'
);

-- 方式2: 使用BIGINT存储（用于高性能场景）
CREATE TABLE tp_example (
    id VARCHAR(32) PRIMARY KEY,
    create_time BIGINT COMMENT '创建时间 格式:YYYYMMDDHHmmss',
    update_time BIGINT COMMENT '更新时间 格式:YYYYMMDDHHmmss'
);

-- 不推荐: 使用DATETIME（会有时区问题）
CREATE TABLE tp_example (
    id VARCHAR(32) PRIMARY KEY,
    create_time DATETIME COMMENT '创建时间',  -- 不推荐
    update_time DATETIME COMMENT '更新时间'   -- 不推荐
);
```

### 2.2 Java实体类定义

```java
public class Example {
    /**
     * 创建时间（格式：YYYYMMDDHHmmss）
     */
    private String createTime;
    
    /**
     * 更新时间（格式：YYYYMMDDHHmmss）
     */
    private String updateTime;
    
    /**
     * 过期日期（格式：YYYYMMDD）
     */
    private String expireDate;
    
    // Getter/Setter省略
}
```

### 2.3 时间工具类

```java
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间工具类
 */
public class DateTimeUtil {
    
    /**
     * 格式：YYYYMMDDHHmmss
     */
    public static final String FORMAT_FULL = "yyyyMMddHHmmss";
    
    /**
     * 格式：YYYYMMDD
     */
    public static final String FORMAT_DATE = "yyyyMMdd";
    
    /**
     * 格式：HHmmss
     */
    public static final String FORMAT_TIME = "HHmmss";
    
    /**
     * 格式：YYYY-MM-DD HH:mm:ss（用于显示）
     */
    public static final String FORMAT_DISPLAY = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 获取当前时间（YYYYMMDDHHmmss格式）
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_FULL);
        return sdf.format(new Date());
    }
    
    /**
     * 获取当前日期（YYYYMMDD格式）
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
        return sdf.format(new Date());
    }
    
    /**
     * 格式化时间为显示格式
     */
    public static String formatToDisplay(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return "";
        }
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(FORMAT_FULL);
            SimpleDateFormat outputFormat = new SimpleDateFormat(FORMAT_DISPLAY);
            Date date = inputFormat.parse(dateTime);
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateTime;
        }
    }
    
    /**
     * 显示格式转存储格式
     */
    public static String formatToStore(String displayTime) {
        if (displayTime == null || displayTime.isEmpty()) {
            return "";
        }
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(FORMAT_DISPLAY);
            SimpleDateFormat outputFormat = new SimpleDateFormat(FORMAT_FULL);
            Date date = inputFormat.parse(displayTime);
            return outputFormat.format(date);
        } catch (Exception e) {
            return displayTime;
        }
    }
}
```

### 2.4 自动填充时间字段

```java
/**
 * MyBatis-Plus自动填充配置
 */
@Component
public class MetaObjectHandler implements com.baomidou.mybatisplus.core.handlers.MetaObjectHandler {
    
    @Override
    public void insertFill(MetaObject metaObject) {
        // 新增时自动填充创建时间
        this.strictInsertFill(metaObject, "createTime", String.class, DateTimeUtil.getCurrentDateTime());
    }
    
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时自动填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", String.class, DateTimeUtil.getCurrentDateTime());
    }
}
```

---

## 3. 前端处理

### 3.1 前端日期库

项目使用 **dayjs** 作为日期处理库

```javascript
import dayjs from "dayjs";
```

### 3.2 格式化工具方法

```javascript
/**
 * 日期时间格式化工具
 */

// 格式化完整日期时间（用于显示）
export function formatDateTime(val) {
  if (!val || val === '' || val === null) {
    return '-';
  }
  try {
    return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
  } catch (e) {
    return '-';
  }
}

// 格式化日期（用于显示）
export function formatDate(val) {
  if (!val || val === '' || val === null) {
    return '-';
  }
  try {
    return dayjs(val, 'YYYYMMDD').format('YYYY-MM-DD');
  } catch (e) {
    return '-';
  }
}

// 格式化时间（用于显示）
export function formatTime(val) {
  if (!val || val === '' || val === null) {
    return '-';
  }
  try {
    return dayjs(val, 'HHmmss').format('HH:mm:ss');
  } catch (e) {
    return '-';
  }
}

// 转换为存储格式（提交时使用）
export function toStorageFormat(dateObj) {
  if (!dateObj) {
    return '';
  }
  return dayjs(dateObj).format('YYYYMMDDHHmmss');
}

// 转换为Date对象（编辑时使用）
export function toDateObject(storageStr) {
  if (!storageStr || storageStr === '' || storageStr === null) {
    return null;
  }
  try {
    return dayjs(storageStr, 'YYYYMMDDHHmmss').toDate();
  } catch (e) {
    return null;
  }
}

// 格式化过期时间（特殊处理）
export function formatExpireTime(val) {
  if (!val || val === '' || val === null) {
    return '永不过期';
  }
  try {
    return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
  } catch (e) {
    return '永不过期';
  }
}
```

### 3.3 表格formatters配置

```javascript
data() {
  return {
    formatters: {
      // 创建时间格式化
      createTime(val) {
        if (!val || val === '' || val === null) {
          return '-';
        }
        return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
      },
      
      // 更新时间格式化
      updateTime(val) {
        if (!val || val === '' || val === null) {
          return '-';
        }
        return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
      },
      
      // 过期时间格式化
      expireTime(val) {
        if (!val || val === '' || val === null) {
          return '永不过期';
        }
        try {
          return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
        } catch (e) {
          return '永不过期';
        }
      }
    }
  }
}
```

### 3.4 查看页面格式化

```vue
<template>
  <div class="tp-dialog">
    <div class="tp-dialog-top">
      <fb-property bordered label-width="140px">
        <fb-property-item label="创建时间">
          {{ formatTime(formData.createTime) }}
        </fb-property-item>
        <fb-property-item label="更新时间">
          {{ formatTime(formData.updateTime) }}
        </fb-property-item>
        <fb-property-item label="过期时间">
          {{ formatExpireTime(formData.expireTime) }}
        </fb-property-item>
      </fb-property>
    </div>
  </div>
</template>

<script>
import dayjs from "dayjs";

export default {
  methods: {
    formatTime(val) {
      if (!val || val === '' || val === null) {
        return '-';
      }
      try {
        return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
      } catch (e) {
        return '-';
      }
    },
    
    formatExpireTime(val) {
      if (!val || val === '' || val === null) {
        return '永不过期';
      }
      try {
        return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
      } catch (e) {
        return '永不过期';
      }
    }
  }
}
</script>
```

### 3.5 编辑页面日期处理

#### 日期选择器配置

```vue
<template>
  <fb-form ref="fbform">
    <fb-row>
      <fb-col span="12">
        <fb-form-item label="过期时间" prop="expireTime">
          <fb-datepicker 
            v-model="formData.expireTime" 
            placeholder="请选择过期时间（留空表示永不过期）"
            format="YYYY-MM-DD HH:mm:ss"
            clearable>
          </fb-datepicker>
        </fb-form-item>
      </fb-col>
    </fb-row>
  </fb-form>
</template>
```

#### 提交时转换

```javascript
save() {
  this.$refs.fbform.validate((result) => {
    if (result === true) {
      const submitData = { ...this.formData };
      
      // 日期转换为存储格式
      if (submitData.expireTime) {
        submitData.expireTime = dayjs(submitData.expireTime).format('YYYYMMDDHHmmss');
      } else {
        submitData.expireTime = ''
      }
      
      // 提交数据
      this.service.add(submitData).then((result) => {
        // 处理结果
      })
    }
  })
}
```

#### 回显时转换

```javascript
view(appId, passKey) {
  this.service.view({"appId": appId, "passKey": passKey}).then((result) => {
    if (result.code == 1) {
      this.formData = result.data;
      
      // 将时间字符串转换为Date对象
      if (this.formData.expireTime) {
        this.formData.expireTime = dayjs(this.formData.expireTime, 'YYYYMMDDHHmmss').toDate();
      }
      
      if (this.formData.createTime) {
        // 只读字段可以保持字符串格式，在显示时格式化
        // this.formData.createTime 保持 "20241201153045"
      }
    }
  })
}
```

---

## 4. 完整示例

### 4.1 新增页面完整流程

```vue
<template>
  <div class="tp-dialog">
    <div class="tp-dialog-top">
      <fb-form ref="fbform">
        <fb-row>
          <fb-col span="12">
            <fb-form-item label="应用名称" prop="appName" :rule="[{required: true}]">
              <fb-input v-model="formData.appName"></fb-input>
            </fb-form-item>
          </fb-col>
          <fb-col span="12">
            <fb-form-item label="过期时间" prop="expireTime">
              <fb-datepicker 
                v-model="formData.expireTime" 
                placeholder="留空表示永不过期"
                format="YYYY-MM-DD HH:mm:ss"
                clearable>
              </fb-datepicker>
            </fb-form-item>
          </fb-col>
        </fb-row>
      </fb-form>
    </div>
    
    <div class="tp-dialog-bottom">
      <fb-button type="primary" @on-click="save">保存</fb-button>
      <fb-button @on-click="handleClose">关闭</fb-button>
    </div>
  </div>
</template>

<script>
import dayjs from "dayjs";

export default {
  name: 'add',
  props: {
    param: { type: Object, require: false },
    meta: { type: Object, default: () => ({}) }
  },
  
  data() {
    return {
      service: this.$svc.sys.thirdPartyApp,
      formData: {
        appId: '',
        appName: '',
        expireTime: '',  // Date对象或空字符串
      }
    }
  },
  
  mounted() {
    this.init(this.param);
  },
  
  methods: {
    init(param) {
      if (param && param.id) {
        // 编辑模式：加载数据
        this.formData.appId = param.id;
        this.view(param.id, param.passKey);
      }
      // 新增模式：不需要加载数据
    },
    
    save() {
      this.$refs.fbform.validate((result) => {
        if (result === true) {
          const submitData = { ...this.formData };
          
          // 日期格式转换
          if (submitData.expireTime) {
            submitData.expireTime = dayjs(submitData.expireTime).format('YYYYMMDDHHmmss');
          } else {
            submitData.expireTime = ''
          }
          
          if (this.formData.appId) {
            // 修改
            submitData.passKey = this.param.passKey;
            this.service.update(submitData).then((result) => {
              if (result.code == 1) {
                this.$message.success('修改成功');
                const action = this.meta?.action || 'edit'
                this.closeTpDialog({ success: true, action });
              } else {
                this.$message.error('修改失败:' + result.message)
              }
            })
          } else {
            // 新增
            this.service.add(submitData).then((result) => {
              if (result.code == 1) {
                this.$message.success('新增成功');
                const action = this.meta?.action || 'add'
                this.closeTpDialog({ success: true, action });
              } else {
                this.$message.error('新增失败: ' + result.message)
              }
            })
          }
        }
      })
    },
    
    view(appId, passKey) {
      this.service.view({"appId": appId, "passKey": passKey}).then((result) => {
        if (result.code == 1) {
          this.formData = result.data;
          
          // 将时间字符串转换为Date对象（用于datepicker）
          if (this.formData.expireTime) {
            this.formData.expireTime = dayjs(this.formData.expireTime, 'YYYYMMDDHHmmss').toDate();
          }
        } else {
          this.$message.error('查询失败: ' + result.message)
        }
      }).catch((err) => {
        console.log(err);
      })
    },
    
    handleClose() {
      this.closeTpDialog()
    }
  }
}
</script>
```

### 4.2 查看页面完整流程

```vue
<template>
  <div class="tp-dialog">
    <div class="tp-dialog-top">
      <fb-property bordered label-width="140px">
        <fb-property-item label="应用名称">
          {{formData.appName}}
        </fb-property-item>
        <fb-property-item label="过期时间">
          {{ formatExpireTime(formData.expireTime) }}
        </fb-property-item>
        <fb-property-item label="创建时间">
          {{ formatTime(formData.createTime) }}
        </fb-property-item>
        <fb-property-item label="更新时间">
          {{ formatTime(formData.updateTime) }}
        </fb-property-item>
      </fb-property>
    </div>
    
    <div class="tp-dialog-bottom">
      <fb-button @on-click="handleClose">关闭</fb-button>
    </div>
  </div>
</template>

<script>
import dayjs from "dayjs";

export default {
  name: 'view',
  props: {
    param: { type: Object, require: false }
  },
  
  data() {
    return {
      service: this.$svc.sys.thirdPartyApp,
      formData: {
        appId: '',
        appName: '',
        expireTime: '',
        createTime: '',
        updateTime: '',
      }
    }
  },
  
  mounted() {
    this.init(this.param);
  },
  
  methods: {
    init(param) {
      if (param && param.id) {
        this.view(param.id, param.passKey);
      }
    },
    
    formatTime(val) {
      if (!val || val === '' || val === null) {
        return '-';
      }
      try {
        return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
      } catch (e) {
        return '-';
      }
    },
    
    formatExpireTime(val) {
      if (!val || val === '' || val === null) {
        return '永不过期';
      }
      try {
        return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
      } catch (e) {
        return '永不过期';
      }
    },
    
    view(appId, passKey) {
      this.service.view({"appId": appId, "passKey": passKey}).then((result) => {
        if (result.code == 1) {
          this.formData = result.data;
          // 查看页面不需要转换，保持字符串格式，在显示时格式化
        } else {
          this.$message.error('查询失败: ' + result.message)
        }
      })
    },
    
    handleClose() {
      this.closeTpDialog()
    }
  }
}
</script>
```

---

## 5. 常见场景处理

### 5.1 空值处理

```javascript
// 提交时
if (submitData.expireTime) {
  submitData.expireTime = dayjs(submitData.expireTime).format('YYYYMMDDHHmmss');
} else {
  submitData.expireTime = ''  // 空字符串，不是null
}

// 显示时
formatExpireTime(val) {
  if (!val || val === '' || val === null) {
    return '永不过期';  // 或 '-'
  }
  return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
}
```

### 5.2 当前时间获取

```javascript
// 前端获取当前时间（提交格式）
const now = dayjs().format('YYYYMMDDHHmmss');
// 结果: "20241201153045"

// 前端获取当前时间（显示格式）
const nowDisplay = dayjs().format('YYYY-MM-DD HH:mm:ss');
// 结果: "2024-12-01 15:30:45"
```

```java
// 后端获取当前时间
String now = DateTimeUtil.getCurrentDateTime();
// 结果: "20241201153045"
```

### 5.3 时间比较

```javascript
// 比较两个时间（字符串格式）
const time1 = "20241201153045";
const time2 = "20241201160000";

// 方式1: 直接字符串比较（推荐，因为格式统一）
if (time1 < time2) {
  console.log('time1早于time2');
}

// 方式2: 转换为Date对象比较
const date1 = dayjs(time1, 'YYYYMMDDHHmmss').toDate();
const date2 = dayjs(time2, 'YYYYMMDDHHmmss').toDate();
if (date1 < date2) {
  console.log('time1早于time2');
}
```

### 5.4 时间范围查询

```javascript
// 查询表单
formData: {
  startTime: '',  // 开始时间
  endTime: '',    // 结束时间
}

// 提交查询
handleQuery() {
  const queryData = { ...this.formData };
  
  // 转换日期范围
  if (queryData.startTime) {
    queryData.startTime = dayjs(queryData.startTime).format('YYYYMMDDHHmmss');
  }
  if (queryData.endTime) {
    queryData.endTime = dayjs(queryData.endTime).format('YYYYMMDDHHmmss');
  }
  
  // 发送请求
  this.$refs.table.doSearch();
}
```

---

## 6. 常见错误与解决方案

### 6.1 格式转换错误

❌ **错误**: 直接提交Date对象
```javascript
// 错误
formData.expireTime = new Date();  // 后端无法识别
this.service.add(formData);
```

✅ **正确**: 转换为字符串格式
```javascript
// 正确
formData.expireTime = dayjs(new Date()).format('YYYYMMDDHHmmss');
this.service.add(formData);
```

### 6.2 回显转换错误

❌ **错误**: 字符串直接绑定到datepicker
```javascript
// 错误
this.formData.expireTime = "20241201153045";  // datepicker无法识别
```

✅ **正确**: 转换为Date对象
```javascript
// 正确
this.formData.expireTime = dayjs("20241201153045", 'YYYYMMDDHHmmss').toDate();
```

### 6.3 空值处理错误

❌ **错误**: 使用null或undefined
```javascript
// 错误
submitData.expireTime = null;  // 可能导致后端异常
```

✅ **正确**: 使用空字符串
```javascript
// 正确
submitData.expireTime = '';  // 后端可以正确处理
```

---

## 7. 最佳实践

### 7.1 统一格式化方法

建议在项目中创建统一的日期格式化工具文件：

```javascript
// utils/dateFormat.js

import dayjs from 'dayjs';

/**
 * 日期时间格式化工具
 */
export default {
  /**
   * 格式化为显示格式 YYYY-MM-DD HH:mm:ss
   */
  formatDateTime(val) {
    if (!val) return '-';
    try {
      return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
    } catch (e) {
      return '-';
    }
  },
  
  /**
   * 格式化为存储格式 YYYYMMDDHHmmss
   */
  formatToStorage(dateObj) {
    if (!dateObj) return '';
    return dayjs(dateObj).format('YYYYMMDDHHmmss');
  },
  
  /**
   * 转换为Date对象
   */
  toDateObject(storageStr) {
    if (!storageStr) return null;
    try {
      return dayjs(storageStr, 'YYYYMMDDHHmmss').toDate();
    } catch (e) {
      return null;
    }
  },
  
  /**
   * 格式化过期时间
   */
  formatExpireTime(val) {
    if (!val) return '永不过期';
    try {
      return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
    } catch (e) {
      return '永不过期';
    }
  }
}
```

### 7.2 在组件中使用

```javascript
import dateFormat from '@/utils/dateFormat';

export default {
  methods: {
    formatTime(val) {
      return dateFormat.formatDateTime(val);
    },
    
    formatExpireTime(val) {
      return dateFormat.formatExpireTime(val);
    },
    
    save() {
      const submitData = { ...this.formData };
      submitData.expireTime = dateFormat.formatToStorage(submitData.expireTime);
      // 提交数据
    },
    
    view(id) {
      this.service.view({id}).then((result) => {
        this.formData = result.data;
        this.formData.expireTime = dateFormat.toDateObject(this.formData.expireTime);
      });
    }
  }
}
```

---

## 8. 检查清单

### 8.1 开发检查清单

**后端开发**:
- [ ] 数据库字段使用VARCHAR(14)存储日期时间
- [ ] 实体类使用String类型定义时间字段
- [ ] 新增时自动填充createTime
- [ ] 更新时自动填充updateTime
- [ ] 返回前端时保持YYYYMMDDHHmmss格式

**前端开发**:
- [ ] 导入dayjs库
- [ ] 显示时使用格式化方法转换
- [ ] 编辑时转换为Date对象
- [ ] 提交时转换为YYYYMMDDHHmmss格式
- [ ] 空值处理返回空字符串或默认文案
- [ ] 过期时间显示"永不过期"

---

## 版本信息

- **版本号**: v1.0.0
- **创建日期**: 2025年12月1日
- **最后更新**: 2025年12月1日

---

**重要提醒**: 严格遵循本规范可避免日期时间相关的所有常见问题！
