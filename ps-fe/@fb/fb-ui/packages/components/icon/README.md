[comment]: <> (fb-docs: docsify/fb-ui/01/icon/README.md)

# 图标-Icon

## 基础用法

```html run {title:'示例演示'}
<template>
<div>
图标类型：
	<fb-icon name="folder-expansion-fill"></fb-icon>
	<br/>
	<br/>

图标大小：
	<fb-icon name="folder-expansion-fill" size="xs"></fb-icon>
	<fb-icon name="folder-expansion-fill" size="s"></fb-icon>
	<fb-icon name="folder-expansion-fill" size="m"></fb-icon>
	<fb-icon name="folder-expansion-fill" size="l"></fb-icon>
	<fb-icon name="folder-expansion-fill" size="xl"></fb-icon>
	<fb-icon name="folder-expansion-fill" size="xxl"></fb-icon>
	<fb-icon name="folder-expansion-fill" size="xxxl"></fb-icon>
	<fb-icon name="folder-expansion-fill" size="50"></fb-icon>
	<fb-icon name="folder-expansion-fill" size="50px"></fb-icon>
	<br/>
	<br/>
图标颜色：
	<fb-icon name="folder-expansion-fill" color="red"></fb-icon>
	<br/>
	<br/>
图标颜色：
	<fb-icon name="folder-expansion-fill" rotating></fb-icon>
	<br/>
	<br/>

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

## 更多用法

### 控件中设置图标
如果控件具有icon属性，则可以设置该控件的图标，具体以控件为准

```html run {title:'试一试'}
<template>
<div>
	<fb-button type="primary" icon="folder-expansion-fill" >按钮控件</fb-button>
	<fb-link label="链接控件" icon="folder-expansion-fill"></fb-link>
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

## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-click | 当鼠标指针点击图标时触发，可以是方法名，或方法体 | (event) => void
| on-mouseenter | 当鼠标指针进入（穿过）图标时触发，可以是方法名，或方法体 | (event) => void
| on-mouseleave | 当鼠标指针离开图标时触发，可以是方法名，或方法体 | (event) => void

## 属性列表

| 属性       | 说明                                             | 类型 | 默认值 |
|:---------|:-----------------------------------------------|:-----|:-------|
| name     | 图标的名称                                          | String |  |
| size     | 链接的尺寸 可选值是 xs、s、m、l、xl、xxl、xxxl，或自定义大小，支持 px,% | m||
| color    | 图标的颜色                                          | String ||
| rotating | 图标是否旋转                                         | Boolean | false|
| bold     | 加粗                                             | Boolean | false |
| weight   | 字重                                             | String, Number | |
| mr       | marginRight 右边距                                | String, Number | |
| ml       | marginLeft 左边距                                 | String, Number | |
| valign   | 垂直居中，同verticalAlign                                         | String, Number | |

## 图标列表

```html run {title:'图标列表'}
<template>
	<div>
       共：{{ length }}
		<fb-card v-for="(icons, name) in types" :key="name">
			<div slot="title">
				{{name}} ({{icons.length}})
			</div>
			<div slot="actions">
				<div style="padding-top:11px;">{{opText}}</div>
			</div>
			<ul>
				<li v-for="(icon, j) in icons" @click="selectIcon(icon)" v-clipboard:copy="copyTxt" v-clipboard:success="onCopy"
					:class="{active: icon.font_class == selectedIcon.font_class }">
					<fb-icon :name="icon.font_class"
							 :rotating="rotating"
							 @click="selectIcons"
							 size="32"
					>
					</fb-icon>
					<fb-text class="name">{{icon.name}}</fb-text>
					<fb-text class="cls">{{icon.font_class}}</fb-text>
				</li>
			</ul>
		</fb-card>
	</div>
</template>

<script>
	export default {
		data () {
			return {
				rotating: false,
				length: 0,
				selectedIcon: {
					icon_id: '',
					name: '',
					font_class: '',
					unicode: '',
					unicode_decimal: '',
				},
				opText:"点击图标，复制样式名",
				types: [],
			}
		},
		computed: {
			copyTxt () {
				return this.selectedIcon.font_class
			},
		},
		created () {
			this.selectIcons().then(icons=>{
				let types = {}
				var count = 0
				for (var i in icons.glyphs) {
					count++

					let icon = icons.glyphs[i]
					let type = icon.name.split('-')[0]
                    if (!types[type]) {
                        types[type] = []
                    }
					types[type].push(icon)
				}

				this.types = types
				this.length = count
			})
		},
		methods: {
			selectIcons () {
				return new Promise((resolve, reject) => {
					axios({
					  method: 'get',
					  url: 'fb-ui/01/icon/iconfont.json',
					  dataType: "json",
					  crossDomain: true,
					  cache: false
					}).then(res => {
					  resolve(res.data)
					})
				})
			},
			selectIcon (icon) {
				this.selectedIcon = icon
			},
			onCopy(e){
				this.opText = '已复制 '+ e.text + ' 到剪贴板'
				this.$message.success(this.opText,{align:'top'})
			}
		},
	}
</script>

<style>

.detail {
  position: fixed;
  margin-left: 16px;
  top: 72px;
  width: 208px;
  z-index: 1;
}
.jpx-card {
  margin-bottom: 12px;
}
.jpx-card ul {
  overflow: hidden;
  clear: both;
  margin: 0;
  padding: 0;
}
.jpx-card ul li {
  margin: 4px;
  float: left;
  text-align: center;
  list-style: none;
  cursor: pointer;
  color: #666;
  position: relative;
  overflow: hidden;
  width: 218px;
  height: 80px;
  background: #FFFFFF;
  border-radius: 4px;
  border: 1px solid #E8E8E8;
  transition: all 0.2s;
}
.jpx-card ul li:hover {
  background: #E6F7FF;
}
.jpx-card ul li:active,
.jpx-card ul li.active {
  background: #0284FE;
}
.jpx-card ul li:active .fb-icon,
.jpx-card ul li.active .fb-icon,
.jpx-card ul li:active .name,
.jpx-card ul li.active .name,
.jpx-card ul li:active .cls,
.jpx-card ul li.active .cls {
  color: #fff;
}
.jpx-card ul li .jpx-icon {
  display: block;
  color: #666;
  position: absolute;
  top: 24px;
  left: 16px;
}
.jpx-card ul li .name {
  color: #313C47;
  display: block;
  position: absolute;
  top: 16px;
  left: 64px;
}
.jpx-card ul li .cls {
  color: #666666;
  display: block;
  position: absolute;
  top: 42px;
  left: 64px;
}
</style>
```
