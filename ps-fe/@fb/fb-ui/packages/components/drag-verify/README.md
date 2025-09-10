[comment]: <> (fb-docs: docsify/fb-ui/05/drag-verify/README.md)

# 拖拽验证 drag-verify

拖拽验证，一般用在登录验证和和需要用户手动验证的地方

## 基础用法

```html run {title:'示例演示'}
<template>
<div>
    <fb-card header="拼图验证" style="margin-top: 90px">
        <fb-drag-verify-img @on-verify="dragImgVerify" v-model="imgStatus" imgBoxShow></fb-drag-verify-img>
    </fb-card>

    <fb-card header="默认">
        <fb-drag-verify v-model="passing"></fb-drag-verify>
    </fb-card>

    <fb-card header="自定义">
        <fb-drag-verify
            text="你拽我啊"
            successText="哎呀"
            background="red"
            progressBarBg="yellow"
            completedBg="green"
            circle
            @on-callback="handleBack"
        ></fb-drag-verify>
    </fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
        passing: false,
		imgStatus: ""
      }
    },
    methods: {
        dragImgVerify ({x, y, verification}) {
            console.log(x, y, verification)
            if (x > 100) {
                this.imgStatus = 'success'
            } else {
                this.imgStatus = 'fail'
            }

            // axios({
            // 	method: 'post',
            // 	url: 'http://192.168.90.106:8081/jpx-admin/sys/captcha/check-verification',
            // 	params: {
            // 		x,
            // 		y
            // 	},
            // 	headers: {
            // 		"verification": verification
            // 	},
            // }).then(res => {
            // 	console.log(res)
            // 	let data = res.data
            // 	if (data.code >= 0) {
            // 		this.imgStatus = 'success'
            // 		this.$message.success(data.message)
            // 	} else {
            // 		this.imgStatus = 'fail'
            // 		this.$message.error(data.message)
            // 	}
            // }).catch(err => {
            // 	this.imgStatus = 'fail'
            // })
        },
        handleBack (val) {
            console.log(val)
            alert('回调' + val)
        }
    },
  }
</script>
```
## drag-verify事件列表
| 事件名 | 说明 | 返回值 |
|:-----|:----|:-----|
| on-callback | 回调 | (value) => value |

## drag-verify属性列表
| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | v-model 双向绑定值 | Boolean | false |
| width | 宽度 | Number | 312 |
| height | 高度  | Number | 36 |
| text | 默认文字  | String | 请拖拽滑块到右侧验证 |
| successText | 成功文字  | String | 验证通过 |
| background | 默认背景  | String | #ccc |
| progressBarBg | 进度背景颜色  | String | rgba(86, 209, 0, 0.7) |
| completedBg | 成功背景颜色   | String | rgba(86, 209, 0, 1) |
| completedColor | 成功文字颜色  | String | #fff |
| circle | 圆角  | Boolean | false |
| handlerIcon | 手柄 icon  | String | next |
| successIcon | 成功手柄 icon | String | selected |
| handlerBg | 手柄 背景 | String | #fff |
| handlerColor | 成功手柄 文字颜色 | String | #666 |
| messageColor | 文字说明 颜色 | String | #444 |
| textSize | 文字说明 大小 | String | 14 |

## drag-verify-img属性列表 与drag-verify一致
| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| action | 图片请求地址 | String | "" |
| disabled | 禁用 | Boolean | false |
| imgBoxShow | 图片背景显示 | Boolean | false |
| completedBorderColor | 成功 border color | String | rgba(86, 209, 0, 1) |
| failIcon | 失败icon | String | close |
| failBg | 失败 背景 | String | rgba(254, 242, 241, 1) |
| failBorderColor | 失败 border color | String | rgba(251, 84, 78, 1) |
| failDuration | 失败 延迟 | String | 400 |

## drag-verify-img事件列表
| 事件名 | 说明 | 返回值 |
|:-----|:----|:-----|
| on-success | 回调 | (status) => status |
| on-fail | 回调 | (status) => status |
| esc | window 事件关闭图片弹窗 | -- |


