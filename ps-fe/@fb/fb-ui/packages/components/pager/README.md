[comment]: <> (fb-docs: docsify/fb-ui/02/pager/README.md)

# 分页-Pager

用于切分过多数据的长页面，使其每次只加载一个页面。



## 基础用法

- 当加载/渲染所有数据需要花费很多时间时；
- 点击按钮切换页面，也可直接跳转到指定页面；

```html run {title:'示例演示'}
<template>
<div>

<fb-pager :current="pager.current" :size="pager.size" :pages="pager.pages" :total="pager.total"/>

</div>
</template>
<script>
  export default {
    data () {
      return {
      	pager:{
      		current:1,
      		size:10,
      		pages:100,
      		total:97,
      	}
      }
    },
  }
</script>
<style>
</style>
```

## 更多用法

### 锁定分页

```html run {title:'试一试'}
<template>
<div>

<fb-pager :current="pager.current" :size="pager.size" :pages="pager.pages" :total="pager.total" locking/>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	pager:{
      		current:1,
      		size:10,
      		pages:100,
      		total:97,
      	}
      }
    },
  }
</script>
<style>
</style>
```


### 设置对齐方式

```html run {title:'向右对齐'}
<template>
<div>
<fb-pager :current="pager.current" :size="pager.size" :pages="pager.pages" :total="pager.total" align="right"/>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	pager:{
      		current:1,
      		size:10,
      		pages:100,
      		total:97,
      	}
      }
    },
  }
</script>
<style>
</style>
```


```html run {title:'向左对齐'}
<template>
<div>
<fb-pager :current="pager.current" :size="pager.size" :pages="pager.pages" :total="pager.total" align="left"/>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	pager:{
      		current:1,
      		size:10,
      		pages:100,
      		total:97,
      	}
      }
    },
  }
</script>
<style>
</style>
```


### 快速跳转

```html run {title:'输入数字，按回车，进行跳转'}
<template>
<div>

<fb-pager :current="pager.current" :size="pager.size" :pages="pager.pages" :total="pager.total" show-quick-jumper/>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	pager:{
      		current:1,
      		size:10,
      		pages:100,
      		total:97,
      	}
      }
    },
  }
</script>
<style>
</style>
```

### 隐藏总数

```html run {title:'试一试'}
<template>
<div>

<fb-pager :current="pager.current" :size="pager.size" :pages="pager.pages" :total="pager.total" :show-total-info="false"/>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	pager:{
      		current:1,
      		size:10,
      		pages:100,
      		total:101
      	}
      }
    },
  }
</script>
<style>
</style>
```

### 简洁模式

```html run {title:'试一试'}
<template>
<div>

<fb-pager :current="pager.current" :size="pager.size" :pages="pager.pages" :total="pager.total" :simple="true"/>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	pager:{
      		current:1,
      		size:10,
      		pages:100,
      		total:97
      	}
      }
    },
  }
</script>
<style>
</style>
```

### 显示最大页数

```html run {title:'试一试'}
<template>
<div>

<fb-pager :current="pager.current" :size="pager.size" :pages="pager.pages" :total="pager.total" maxLength="4"/>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	pager:{
      		current:1,
      		size:10,
      		pages:100,
      		total:97
      	}
      }
    },
  }
</script>
<style>
</style>
```

## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-change | 当前页变化时触发，可以是方法名，或方法体 | (current,size,pages,total) => void

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| align | 对齐方式，可选值：left,center,right | String | center
| size | 单页数据记录数 | Number | 10
| current | 当前页，从1开始 | Number | 1
| pages | 总页数 | Number | 0
| total | 总记录数 | Number | 0
| locking | 是否锁定 | Boolean | false
| showSizeChanger | 是否显示分页数下拉 | Boolean | true
| sizeList | 分页数列表 | Array | [10, 20, 50, 100]
| showQuickJumper | 是否显示跳转框 | Boolean | false
| showTotalInfo | 是否显示总页数 | Boolean | true
| simple | 是否简洁模式 | Boolean | false
| maxLength | 显示最大页数 | Number | 6
