# FbFlex 弹性布局组件

## 概述

FbFlex 是一个基于 CSS Flexbox 的 Vue.js 布局组件，提供了丰富的样式属性和布局控制功能。该组件支持弹性布局、网格布局、定位、间距、颜色、边框等多种样式配置，是构建现代化用户界面的强大工具。

## 特性

- 🎯 **弹性布局**: 基于 CSS Flexbox，支持各种弹性布局配置
- 🎨 **丰富样式**: 支持颜色、背景、边框、阴影、滤镜等样式属性
- 📐 **布局控制**: 支持定位、间距、尺寸等布局属性
- 🔤 **文本处理**: 支持文本对齐、省略号、渐变文本等
- 🎛️ **网格支持**: 支持 CSS Grid 布局
- 🎪 **高度可定制**: 通过 props 灵活配置各种样式
- 🚀 **性能优化**: 基于计算属性的样式生成，高效渲染

## 安装使用

```javascript
import FbFlex from '@fb/fb-ui/packages/components/flex'

// 全局注册
Vue.component('FbFlex', FbFlex)

// 或局部注册
export default {
  components: {
    FbFlex
  }
}
```

## 基础用法

```vue
<template>
  <!-- 基础弹性布局 -->
  <fb-flex>
    <div>项目1</div>
    <div>项目2</div>
    <div>项目3</div>
  </fb-flex>

  <!-- 水平居中 -->
  <fb-flex justify-content="center">
    <div>居中内容</div>
  </fb-flex>

  <!-- 垂直居中 -->
  <fb-flex align-items="center" height="200px">
    <div>垂直居中</div>
  </fb-flex>

  <!-- 完全居中 -->
  <fb-flex justify-content="center" align-items="center" height="200px">
    <div>完全居中</div>
  </fb-flex>
</template>
```

## 主要属性

### 弹性布局属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `flex-direction` | String | - | 弹性容器的主轴方向 (row, column, row-reverse, column-reverse) |
| `flex-wrap` | String | - | 弹性项目换行方式 (nowrap, wrap, wrap-reverse) |
| `flex-flow` | String | - | flex-direction 和 flex-wrap 的简写 |
| `justify-content` | String | - | 主轴对齐方式 (flex-start, center, flex-end, space-between, space-around, space-evenly) |
| `align-items` | String | - | 交叉轴对齐方式 (flex-start, center, flex-end, stretch, baseline) |
| `align-content` | String | - | 多行对齐方式 |
| `place-content` | String | - | align-content 和 justify-content 的简写 |
| `justify-self` | String | - | 单个项目在主轴上的对齐方式 |
| `gap` | String/Number | - | 项目间距 |
| `flex` | String/Number | - | flex 简写属性 |
| `flex-grow` | Number | - | 弹性增长因子 |
| `flex-shrink` | Number | - | 弹性收缩因子 |
| `flex-basis` | String | - | 弹性基准值 |
| `order` | Number | - | 项目排序 |

### 网格布局属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `grid` | Boolean | false | 启用网格布局 |
| `grid-template-columns` | String | - | 网格列模板 |
| `grid-template-rows` | String | - | 网格行模板 |
| `grid-template-areas` | String | - | 网格区域模板 |
| `grid-area` | String | - | 网格区域 |

### 显示属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `display` | String | 'flex' | 显示方式 |
| `inline` | Boolean | false | 行内显示 |
| `block` | Boolean | false | 块级显示 |
| `overflow` | String | - | 溢出处理 |

### 定位属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `position` | String | - | 定位方式 (static, relative, absolute, fixed, sticky) |
| `top` | String/Number | - | 顶部偏移 |
| `right` | String/Number | - | 右侧偏移 |
| `bottom` | String/Number | - | 底部偏移 |
| `left` | String/Number | - | 左侧偏移 |
| `z-index` | Number | - | 层级 |

### 尺寸属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `width` | String/Number | - | 宽度 |
| `height` | String/Number | - | 高度 |
| `min-width` | String/Number | - | 最小宽度 |
| `min-height` | String/Number | - | 最小高度 |
| `max-width` | String/Number | - | 最大宽度 |
| `max-height` | String/Number | - | 最大高度 |

### 间距属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `margin` | String/Number | - | 外边距 |
| `margin-top` | String/Number | - | 上外边距 |
| `margin-right` | String/Number | - | 右外边距 |
| `margin-bottom` | String/Number | - | 下外边距 |
| `margin-left` | String/Number | - | 左外边距 |
| `padding` | String/Number | - | 内边距 |
| `padding-top` | String/Number | - | 上内边距 |
| `padding-right` | String/Number | - | 右内边距 |
| `padding-bottom` | String/Number | - | 下内边距 |
| `padding-left` | String/Number | - | 左内边距 |

### 样式属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `color` | String | - | 文字颜色 |
| `background` | String | - | 背景 |
| `border` | String | - | 边框 |
| `border-radius` | String/Number | - | 圆角 |
| `box-shadow` | String | - | 阴影 |
| `box-sizing` | String | - | 盒模型 |
| `outline` | String | - | 轮廓 |
| `filter` | String | - | 滤镜效果 |
| `backdrop-filter` | String | - | 背景滤镜 |
| `mask` | String | - | 遮罩 |
| `cursor` | String | - | 鼠标样式 |

### 字体属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `font-size` | String/Number | - | 字体大小 |
| `font-weight` | String/Number | - | 字体粗细 |
| `font-style` | String | - | 字体样式 |
| `font-family` | String | - | 字体族 |
| `line-height` | String/Number | - | 行高 |
| `vertical-align` | String | - | 垂直对齐 |
| `size` | String/Number | - | 文本大小 (自定义属性) |

### 文本属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `text-left` | Boolean | false | 左对齐 |
| `text-center` | Boolean | false | 居中对齐 |
| `text-right` | Boolean | false | 右对齐 |
| `text-justify` | Boolean | false | 两端对齐 |
| `ellipsis` | Boolean | false | 文本省略号 |
| `text-gradient` | String/Array | - | 文本渐变色 |
| `valign` | String | - | 垂直对齐方式 |

## 使用示例

### 1. 基础弹性布局

```vue
<template>
  <!-- 水平排列 -->
  <fb-flex gap="16px">
    <div>项目1</div>
    <div>项目2</div>
    <div>项目3</div>
  </fb-flex>

  <!-- 垂直排列 -->
  <fb-flex flex-direction="column" gap="8px">
    <div>项目1</div>
    <div>项目2</div>
    <div>项目3</div>
  </fb-flex>
</template>
```

### 2. 对齐方式

```vue
<template>
  <!-- 主轴居中 -->
  <fb-flex justify-content="center">
    <div>居中内容</div>
  </fb-flex>

  <!-- 交叉轴居中 -->
  <fb-flex align-items="center" height="100px">
    <div>垂直居中</div>
  </fb-flex>

  <!-- 两端对齐 -->
  <fb-flex justify-content="space-between">
    <div>左侧</div>
    <div>右侧</div>
  </fb-flex>

  <!-- 完全居中 -->
  <fb-flex justify-content="center" align-items="center" height="200px">
    <div>完全居中</div>
  </fb-flex>
</template>
```

### 3. 弹性项目

```vue
<template>
  <fb-flex>
    <fb-flex flex="1" background="#f0f0f0" padding="10px">弹性项目1</fb-flex>
    <fb-flex flex="2" background="#e0e0e0" padding="10px">弹性项目2 (2倍宽度)</fb-flex>
    <fb-flex flex="1" background="#d0d0d0" padding="10px">弹性项目3</fb-flex>
  </fb-flex>
</template>
```

### 4. 样式配置

```vue
<template>
  <fb-flex
    padding="20px"
    margin="10px"
    background="linear-gradient(45deg, #ff6b6b, #4ecdc4)"
    border-radius="8px"
    box-shadow="0 4px 8px rgba(0,0,0,0.1)"
    color="white"
    font-weight="bold"
  >
    样式化的容器
  </fb-flex>
</template>
```

### 5. 网格布局

```vue
<template>
  <fb-flex
    grid
    grid-template-columns="repeat(3, 1fr)"
    gap="16px"
    padding="20px"
  >
    <div>网格项目1</div>
    <div>网格项目2</div>
    <div>网格项目3</div>
    <div>网格项目4</div>
    <div>网格项目5</div>
    <div>网格项目6</div>
  </fb-flex>
</template>
```

### 6. 文本处理

```vue
<template>
  <!-- 文本省略号 -->
  <fb-flex width="200px" ellipsis>
    这是一段很长的文本，会被截断并显示省略号
  </fb-flex>

  <!-- 渐变文本 -->
  <fb-flex text-gradient="linear-gradient(45deg, #ff6b6b, #4ecdc4)" font-size="24px">
    渐变色文本
  </fb-flex>

  <!-- 文本对齐 -->
  <fb-flex text-center padding="10px" background="#f5f5f5">
    居中文本
  </fb-flex>

  <!-- 垂直居中文本 -->
  <fb-flex valign="center" height="60px" background="#f0f0f0">
    垂直居中的文本
  </fb-flex>
</template>
```

### 7. 复杂布局示例

```vue
<template>
  <!-- 卡片布局 -->
  <fb-flex
    flex-direction="column"
    width="300px"
    background="white"
    border-radius="8px"
    box-shadow="0 2px 8px rgba(0,0,0,0.1)"
    overflow="hidden"
  >
    <!-- 头部 -->
    <fb-flex
      padding="16px"
      background="linear-gradient(135deg, #667eea 0%, #764ba2 100%)"
      color="white"
    >
      <fb-flex flex="1">
        <h3>卡片标题</h3>
      </fb-flex>
      <fb-flex>
        <button>操作</button>
      </fb-flex>
    </fb-flex>
    
    <!-- 内容 -->
    <fb-flex flex="1" padding="16px" flex-direction="column" gap="12px">
      <p>这是卡片的内容区域</p>
      <fb-flex justify-content="space-between">
        <span>标签</span>
        <span>值</span>
      </fb-flex>
    </fb-flex>
    
    <!-- 底部 -->
    <fb-flex padding="16px" border-top="1px solid #eee" justify-content="flex-end" gap="8px">
      <button>取消</button>
      <button>确定</button>
    </fb-flex>
  </fb-flex>
</template>
```

### 8. 响应式布局

```vue
<template>
  <!-- 响应式网格 -->
  <fb-flex
    grid
    grid-template-columns="repeat(auto-fit, minmax(250px, 1fr))"
    gap="20px"
    padding="20px"
  >
    <fb-flex v-for="item in items" :key="item.id" 
      padding="16px" 
      background="white" 
      border-radius="8px" 
      box-shadow="0 2px 4px rgba(0,0,0,0.1)"
    >
      {{ item.content }}
    </fb-flex>
  </fb-flex>
</template>
```

## 事件

| 事件名 | 说明 | 参数 |
|--------|------|------|
| `on-click` | 点击事件 | event |

```vue
<template>
  <fb-flex @on-click="handleClick">
    点击我
  </fb-flex>
</template>

<script>
export default {
  methods: {
    handleClick(event) {
      console.log('FbFlex clicked:', event)
    }
  }
}
</script>
```

## 插槽

| 插槽名 | 说明 |
|--------|------|
| `default` | 默认插槽，用于放置子元素 |

## CSS 类名

组件会自动添加以下 CSS 类名：

- `{prefix}-flex`: 基础类名
- `{prefix}-flex--ellipsis`: 当启用 ellipsis 属性时添加

## 注意事项

1. **单位处理**: 数值类型的属性会自动添加 `px` 单位，字符串类型保持原样
2. **显示模式**: 默认为 `flex` 显示，可通过 `display`、`inline`、`block`、`grid` 等属性控制
3. **优先级**: 某些属性可能会相互覆盖，请注意属性的优先级
4. **浏览器兼容性**: 组件使用了现代 CSS 特性，请确保目标浏览器支持
5. **性能考虑**: 避免在大量元素上使用复杂的样式属性
6. **文本渐变**: 使用 `text-gradient` 时会设置 `color: transparent`，请注意文本可访问性

## 最佳实践

1. **语义化使用**: 根据布局需求选择合适的属性组合
2. **性能优化**: 避免不必要的样式计算，合理使用缓存
3. **可维护性**: 使用有意义的属性名称，保持代码清晰
4. **响应式设计**: 结合媒体查询和网格布局实现响应式设计
5. **组合使用**: 可以嵌套使用 FbFlex 组件创建复杂布局
6. **样式隔离**: 使用 scoped 样式避免样式污染

## 相关组件

- `FbRow`: 栅格行组件
- `FbCol`: 栅格列组件
- `FbContainer`: 容器组件
- `FbGrid`: 网格组件

## 技术实现

组件基于以下技术实现：

- **Vue.js**: 组件框架
- **CSS Flexbox**: 弹性布局
- **CSS Grid**: 网格布局
- **计算属性**: 动态样式生成
- **样式工具**: 模块化样式属性管理

## 更新日志

### v1.0.0
- 初始版本发布
- 支持基础弹性布局功能
- 支持样式属性配置
- 支持网格布局
- 支持文本处理功能
- 支持事件监听
- 支持插槽内容