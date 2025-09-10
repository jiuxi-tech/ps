[comment]: <> (fb-docs: docsify/fb-ui/03/upload/README.md)

# 上传-Upload

## 基础用法

```html run {title:'示例演示'}
<template>
<div>
<fb-upload
	:service="xxService"
	:file-list="fileList1"
	multiple
	readonly
>
</fb-upload>

</div>
</template>
<script>
  export default {
    data () {
      return {
		xxService: {
			download (file) {
				return `http://192.168.90.77:8080/jpx-admin/sys/file/download?id=${file.attachId}`
			},
			upload (file) {
				return new Promise((resolve, reject) => {
					setTimeout(() => {
						resolve({
							'code': 1,
							'data': {
								'attachId': '1356578926939340800',
								'attachName': 'a80f8b014a90f603912c67353b12b31bb151edb6.jpg',
								'attachSize': 32057.0,
								'createTime': '20210202202314',
								'referId': '',
								'referType': '',
								'savePath': '2021/2/2021_02_02_20_23_14856.jpg',
							},
							'message': '成功',
						})
					}, 1000)
				})
			},
		},
		fileList1: [
			{
				'attachId': '1352382906936328192',
				'attachName': 'a80f8b014a90f603912c67353b12b31bb151edb6.jpg',
				'attachSize': 32057.0,
				'createTime': '20210122062945',
				'referId': '',
				'referType': '',
				'savePath': '2021/1/2021_01_22_06_29_45730.jpg',
			},
			{
				'attachId': '1352384354373861376',
				'attachName': '11111.jpg',
				'attachSize': 53795.0,
				'createTime': '20210122063530',
				'referId': '',
				'referType': '',
				'savePath': '2021/1/2021_01_22_06_35_30826.jpg',
			},
		],
      }
    },
  }
</script>
<style>
</style>
```

## 图片查看器

```html run {title:'示例演示'}
<template>
<div style="height: 600px">
<fb-upload
    readonly
    view="image"
     :service="xxService"
    :file-list="fileList3"
>
    <fb-icon name="add-normal" size="30"></fb-icon>
</fb-upload>

</div>
</template>
<script>
  export default {
    data () {
      return {
		xxService: {
			download (file) {
				return `http://192.168.90.77:8080/jpx-admin/sys/file/download?id=${file.attachId}`
			},
			upload (file) {
				return new Promise((resolve, reject) => {
					setTimeout(() => {
						resolve({
							'code': 1,
							'data': {
								'attachId': '1356578926939340800',
								'attachName': 'a80f8b014a90f603912c67353b12b31bb151edb6.jpg',
								'attachSize': 32057.0,
								'createTime': '20210202202314',
								'referId': '',
								'referType': '',
								'savePath': '2021/2/2021_02_02_20_23_14856.jpg',
							},
							'message': '成功',
						})
					}, 1000)
				})
			},
		},
		fileList3: [
            {
                'attachId': '1545339861165146112',
                'attachTitle': '',
                'attachName': 'cjsh.jpeg',
                'referType': 'SYS1021',
                'referId': '1545340364880084992',
                'attachTypeCode': '',
                'savePath': '/D1/85/30/a1b1fef091cb4cfc8b96317c32cca8b2.jpg',
                'savePathBak1': '',
                'savePathBak2': '',
                'attachSize': 34846.0,
                'orderIndex': '',
                'extend01': '',
                'extend02': '',
                'extend03': '',
                'actived': '1',
                'creator': '1111111111111111111',
                'createTime': '20220708173148',
                'updator': '',
                'updateTime': '20220708173356',
            },
            {
                'attachId': '1545339887908028416',
                'attachTitle': '',
                'attachName': 'boy.jpeg',
                'referType': 'SYS1021',
                'referId': '1545340364880084992',
                'attachTypeCode': '',
                'savePath': '/D1/04/96/56dc49e952e146b9a920a41d788d1a60.jpg',
                'savePathBak1': '',
                'savePathBak2': '',
                'attachSize': 20509.0,
                'orderIndex': '',
                'extend01': '',
                'extend02': '',
                'extend03': '',
                'actived': '1',
                'creator': '1111111111111111111',
                'createTime': '20220708173154',
                'updator': '',
                'updateTime': '20220708173356',
            },
            {
                'attachId': '1545340169337438208',
                'attachTitle': '',
                'attachName': '8F33844783479FE044DAD8B413D_1E862A1A_8303.png',
                'referType': 'SYS1021',
                'referId': '1545340364880084992',
                'attachTypeCode': '',
                'savePath': '/D1/14/87/6a20ec7ffce9405692700aaa365c7216.jpg',
                'savePathBak1': '',
                'savePathBak2': '',
                'attachSize': 25639.0,
                'orderIndex': '',
                'extend01': '',
                'extend02': '',
                'extend03': '',
                'actived': '1',
                'creator': '1111111111111111111',
                'createTime': '20220708173302',
                'updator': '',
                'updateTime': '20220708173356',
            },
        ],
      }
    },
  }
</script>
<style>
</style>
```

## 图片查看器 + 黏贴上传

```html run {title:'示例演示'}
<template>
<div style="height: 600px">
<fb-upload
    view="image"
    drag
    paste
     :service="xxService"
    :file-list="fileList3"
>
    <fb-icon name="add-normal" size="30"></fb-icon>
</fb-upload>

</div>
</template>
<script>
  export default {
    data () {
      return {
		xxService: {
			download (file) {
				return `http://192.168.90.77:8080/jpx-admin/sys/file/download?id=${file.attachId}`
			},
			upload (file) {
				return new Promise((resolve, reject) => {
					setTimeout(() => {
						resolve({
							'code': 1,
							'data': {
								'attachId': '1356578926939340800',
								'attachName': 'a80f8b014a90f603912c67353b12b31bb151edb6.jpg',
								'attachSize': 32057.0,
								'createTime': '20210202202314',
								'referId': '',
								'referType': '',
								'savePath': '2021/2/2021_02_02_20_23_14856.jpg',
							},
							'message': '成功',
						})
					}, 1000)
				})
			},
		},
		fileList3: [
            {
                'attachId': '1545339861165146112',
                'attachTitle': '',
                'attachName': 'cjsh.jpeg',
                'referType': 'SYS1021',
                'referId': '1545340364880084992',
                'attachTypeCode': '',
                'savePath': '/D1/85/30/a1b1fef091cb4cfc8b96317c32cca8b2.jpg',
                'savePathBak1': '',
                'savePathBak2': '',
                'attachSize': 34846.0,
                'orderIndex': '',
                'extend01': '',
                'extend02': '',
                'extend03': '',
                'actived': '1',
                'creator': '1111111111111111111',
                'createTime': '20220708173148',
                'updator': '',
                'updateTime': '20220708173356',
            },
            {
                'attachId': '1545339887908028416',
                'attachTitle': '',
                'attachName': 'boy.jpeg',
                'referType': 'SYS1021',
                'referId': '1545340364880084992',
                'attachTypeCode': '',
                'savePath': '/D1/04/96/56dc49e952e146b9a920a41d788d1a60.jpg',
                'savePathBak1': '',
                'savePathBak2': '',
                'attachSize': 20509.0,
                'orderIndex': '',
                'extend01': '',
                'extend02': '',
                'extend03': '',
                'actived': '1',
                'creator': '1111111111111111111',
                'createTime': '20220708173154',
                'updator': '',
                'updateTime': '20220708173356',
            },
            {
                'attachId': '1545340169337438208',
                'attachTitle': '',
                'attachName': '8F33844783479FE044DAD8B413D_1E862A1A_8303.png',
                'referType': 'SYS1021',
                'referId': '1545340364880084992',
                'attachTypeCode': '',
                'savePath': '/D1/14/87/6a20ec7ffce9405692700aaa365c7216.jpg',
                'savePathBak1': '',
                'savePathBak2': '',
                'attachSize': 25639.0,
                'orderIndex': '',
                'extend01': '',
                'extend02': '',
                'extend03': '',
                'actived': '1',
                'creator': '1111111111111111111',
                'createTime': '20220708173302',
                'updator': '',
                'updateTime': '20220708173356',
            },
        ],
      }
    },
  }
</script>
<style>
</style>
```

## 更多用法

## 事件列表

| 事件名                | 说明                | 返回值 |
|:-------------------|:------------------|:-------|
| on-reject          |                   |
| on-update:fileList | 监听fileList发生变化时事件 |
| on-change          | 监听fileList发生变化时事件 |
| on-preview         | 附件查看事件（眼睛icon）    |

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| mode | 可选值：drag,select | String | select
| name |  | String | file
| fileList | 回显列表（后台查回来） | Array | []
| service | 绑定数据服务接口 | Object, Function | () => {}
| param | 绑定数据服务接口参数 | Object | {}
| directory | 目录 | Boolean | false
| multiple | 多选 | Boolean | false
| data |  |  |
| maxLength | 最大长度 | Number | 1
| accept | 可接受的[文件类型](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/Input/file) | String |
| beforeUpload |  | Function | () => {}
| afterSelect |  | Function | () => {}
| beforeRemove |  | Function | () => {}
| view | 可选值：list,image, avatar, grid | String | list
| disabled | 禁用 | Boolean | false
| withCredentials |  | Boolean | false
| fastSelect |  | Boolean | true
| height | 高度 | String, Number | 100px
| showUploadList |  | Boolean | true
| showUpload |  | Boolean | true
| compressTypes |  | String | image
| quality | 压缩质量 | Number | 0.7
| maxWidth |  | Number, Boolean | 2000
| maxHeight |  | Number, Boolean | 2000
| readonly | 只读 | Boolean | false
| avatarSize | 头像模式大小 | Number | 120
| avatarCircle | 头像圆圈展示 | Boolean | true
| buttonIcon | 上传按钮icon | String | null
| buttonText | 上传按钮文字 | String | null
| desc | 注解 | String | null
| showPreview | 是否显示预览小眼睛 | Boolean | true
| throwPreviewEvent | 抛出预览事件 | Boolean | false
| showDownload | 是否显示下载（目前阻止下载事件只查看文件名称） | Boolean | true
| showRemove | 是否显示删除 | Boolean | true
| drag | 拖住上传 | Boolean | false
| paste | 黏贴上传 | Boolean | false

