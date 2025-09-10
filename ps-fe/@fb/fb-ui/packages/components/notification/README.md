[comment]: <> (fb-docs: docsify/fb-ui/05/notification/README.md)

# 全局提示-Notification
全局展示的操作反馈信息
## 基础用法
- 提供五种全局操作反馈信息，“成功信息”、“提示信息”、“警告信息”、“错误信息”、“加载信息”；
- 页面四个角的信息提醒

```html run {title:'示例演示'}
<template>
<div>
<fb-card header="一般">
			<fb-button @on-click="open">打开</fb-button>
			<fb-button @on-click="openAndClose">打开一个关闭上一个</fb-button>
		</fb-card>

 <div style="height: 300px;"></div>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
    methods: {
        open () {
            this.$notification({
                title: `通知提醒框`,
                message: '这里是带图标的通知提醒框，图标可以配置，默认信息图标；系统默认右下角弹出，展示5秒后自动消失。多条提醒框以时间先后从上到下呈现；',
            })
        },
        openAndClose () {
            console.log(this.myNotification)
            if (this.myNotification) {
                this.myNotification()
            }
            this.myNotification = this.$notification({
                type: 'warning',
                iconShow: false,
                title: `通知提醒框`,
                message: '这里是带图标的通知提醒框，图标可以配置，默认信息图标；系统默认右下角弹出，展示5秒后自动消失。多条提醒框以时间先后从上到下呈现；',
                showOkButton: true,
                okButtonText: '去处理',
                callback: () => {
                    console.log(123123)
                }
            })
        },
    },
  }
</script>
<style>
</style>
```

## 更多用法
Fb 为 Vue.prototype 添加了全局方法 $notification。因此在 vue instance 中可以采用本页面中的方式调用 Notification。

### 位置
```html run {title:'示例演示'}
<template>
<div>
<fb-card header="placement">
			<fb-button @on-click="openTopLeft">TopLeft</fb-button>
			<fb-button @on-click="openTopRight">TopRight</fb-button>
			<fb-button @on-click="openBottomLeft">BottomLeft</fb-button>
			<fb-button @on-click="openBottomRight">BottomRight</fb-button>
		</fb-card>

 <div style="height: 300px;"></div>
</div>
</template>
<script>
  export default {
    data () {
      return {
            index: 1,
      }
    },
    methods: {
        openTopLeft () {
            this.$notification({
                placement: 'top-left',
                title: `通知提醒框 ${this.index}`,
                message: '这里是带图标的通知提醒框，图标可以配置，默认信息图标；系统默认右下角弹出，展示5秒后自动消失。多条提醒框以时间先后从上到下呈现；',
                onClick: () => {
                    console.log('Notification Clicked!')
                },
            })
        },
        openTopRight () {
            this.$notification({
                placement: 'top-right',
                title: `通知提醒框 ${this.index}`,
                message: '这里是带图标的通知提醒框，图标可以配置，默认信息图标；系统默认右下角弹出，展示5秒后自动消失。多条提醒框以时间先后从上到下呈现；',
                onClick: () => {
                    console.log('Notification Clicked!')
                },
            })
        },
        openBottomLeft () {
            this.$notification({
                placement: 'bottom-left',
                title: `通知提醒框 ${this.index}`,
                message: '这里是带图标的通知提醒框，图标可以配置，默认信息图标；系统默认右下角弹出，展示5秒后自动消失。多条提醒框以时间先后从上到下呈现；',
                onClick: () => {
                    console.log('Notification Clicked!')
                },
            })
        },
        openBottomRight () {
            this.$notification({
                placement: 'bottom-right',
                title: `通知提醒框 ${this.index}`,
                message: '这里是带图标的通知提醒框，图标可以配置，默认信息图标；系统默认右下角弹出，展示5秒后自动消失。多条提醒框以时间先后从上到下呈现；',
                onClick: () => {
                    console.log('Notification Clicked!')
                },
            })
        },
    },
  }
</script>
<style>
</style>
```

### 类型
```html run {title:'示例演示'}
<template>
<div>
<fb-card header="type">
			<fb-button @on-click="openSuccess">Success</fb-button>
			<fb-button @on-click="openWarning">Warning</fb-button>
			<fb-button @on-click="openInfo">Info</fb-button>
			<fb-button @on-click="openDanger">Danger</fb-button>
		</fb-card>

 <div style="height: 300px;"></div>
</div>
</template>
<script>
  export default {
    data () {
      return {
            index: 1,
      }
    },
    methods: {
        openSuccess () {
            this.$notification({
                type: 'success',
                title: '类型',
                message: 'success',
            })
        },
        openWarning () {
            this.$notification({
                type: 'warning',
                title: '类型',
                message: 'warning',
            })
        },
        openInfo () {
            this.$notification({
                type: 'info',
                title: '类型',
                message: 'info',
            })
        },
        openDanger () {
            this.$notification({
                type: 'danger',
                title: '类型',
                message: 'danger',
            })
        },
    },
  }
</script>
<style>
</style>
```

### 自定义
```html run {title:'示例演示'}
<template>
<div>
<fb-card header="自定义 icon">
			<fb-button @on-click="openIcon">自定义 icon</fb-button>
		</fb-card>

		<fb-card header="延时关闭 duration">
			<fb-button @on-click="openDuration">duration: 10000ms</fb-button>
		</fb-card>

 <div style="height: 300px;"></div>
</div>
</template>
<script>
  export default {
    data () {
      return {
            index: 1,
      }
    },
    methods: {
       openIcon () {
            this.$notification({
                title: '自定义 icon',
                message: '自定义 icon',
                icon: 'qrcode',
                iconColor: 'black',
            })
        },
   
        openDuration () {
   
            this.$notification({
                type: 'danger',
                title: '类型',
                message: '10s后关闭',
                duration: 10 * 1000,
            })
   
        },
    },
  }
</script>
<style>
</style>
```

## 属性列表

| 属性 | 说明 | 类型 | 可选值 | 默认值 |
|:-----|:----|:-----|:-------|:-------|
| title | 标题 | String, Number, Error |  |  |
| message | 消息文字 | String, Number, Error |  |  |
| type | 类型 | String | success/warn/info/error/loading | info |
| duration | 显示时间, 毫秒 | Number |  | 3000 |
| showOkButton | 显示确定按钮 | Boolean |  | false |
| okButtonText | 确定按钮文字 | String |  | 确定 |
| callback | 确定 回调 | Function |  |  |
| icon | icon名称 | String |  |  |
| iconColor | icon杨色 | String |  |  |
| iconShow | icon显示 | Boolean |  | true |
