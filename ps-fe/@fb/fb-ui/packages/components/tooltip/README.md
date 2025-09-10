[comment]: <> (fb-docs: docsify/fb-ui/04/tooltip/README.md)

# 文字提示-Tooltips
鼠标点击/移入元素时，演出气泡式的单纯文字提示；

## 基础用法

鼠标移入显示，移出消失，气泡浮层不承载复杂的文本和操作；
- 可以用来代替系统默认的“title”标签，为按钮、文字、操作做文案解释；

```html run {title:'示例演示'}
<template>
<div>

<div class="box">
	<div class="top">
		<fb-tooltip class="item" effect="dark" content="Top Left 提示文字" placement="top-start">
			<fb-button>上左</fb-button>
		</fb-tooltip>
		<fb-tooltip class="item" effect="dark" content="Top Center 提示文字" placement="top">
			<fb-button>上边</fb-button>
		</fb-tooltip>
		<fb-tooltip class="item" effect="dark" content="Top Right 提示文字" placement="top-end">
			<fb-button>上右</fb-button>
		</fb-tooltip>
	</div>
	<div class="left">
		<fb-tooltip class="item" effect="dark" content="Left Top 提示文字" placement="left-start">
			<fb-button>左上</fb-button>
		</fb-tooltip>
		<fb-tooltip class="item" effect="dark" content="Left Center 提示文字" placement="left">
			<fb-button>左边</fb-button>
		</fb-tooltip>
		<fb-tooltip class="item" effect="dark" content="Left Bottom 提示文字" placement="left-end">
			<fb-button>左下</fb-button>
		</fb-tooltip>
	</div>

	<div class="right">
		<fb-tooltip class="item" effect="dark" content="Right Top 提示文字" placement="right-start">
			<fb-button>右上</fb-button>
		</fb-tooltip>
		<fb-tooltip class="item" effect="dark" content="Right Center 提示文字" placement="right">
			<fb-button>右边</fb-button>
		</fb-tooltip>
		<fb-tooltip class="item" effect="dark" content="Right Bottom 提示文字" placement="right-end">
			<fb-button>右下</fb-button>
		</fb-tooltip>
	</div>
	<div class="bottom">
		<fb-tooltip class="item" effect="dark" content="Bottom Left 提示文字" placement="bottom-start">
			<fb-button>下左</fb-button>
		</fb-tooltip>
		<fb-tooltip class="item" effect="dark" content="Bottom Center 提示文字" placement="bottom">
			<fb-button>下边</fb-button>
		</fb-tooltip>
		<fb-tooltip class="item" effect="dark" content="Bottom Right 提示文字" placement="bottom-end">
			<fb-button>下右</fb-button>
		</fb-tooltip>
	</div>
</div>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
  }
</script>
<style>
.box {
    width: 400px;
}

.box .top {
    text-align: center;
}

.box .left {
    float: left;
    width: 60px;
}

.box .right {
    float: right;
    width: 60px;
}

.box .bottom {
    clear: both;
    text-align: center;
}

</style>
```

## 更多用法

### 消息自定义

```html run {title:'示例演示'}
<template>
<div>
	<fb-tooltip placement="right" effect="light" :tabindex="3">
		<template v-slot:content>
			<fb-icon name="notice"></fb-icon>
			我是白色
		</template>
		<fb-button >移到这里</fb-button>
	</fb-tooltip>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
  }
</script>
<style>
</style>
```

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| disabled | 禁用状态 | Boolean | false
| effect | 显示效果，可选值：light,dark | String | dark
| popperClass | 自定义样式 | String |
| content | 消息内容| String |
| transition | 过渡效果 | String | fade
| tabindex | 元素的 tab 键控制次序 | Number | 0
