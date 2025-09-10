[comment]: <> (fb-docs: docsify/fb-ui/03/datepicker/README.md)

# 日期选择-DatePicker

## 基础用法

```html run {title:'示例演示'}
<template>
<div>
日期选择：
<br/><br/>
<fb-datepicker placeholder="选择 YYYY-MM-DD"/>
<br/><br/>
<fb-datepicker format="YYYY" :show-foot-left-btns="[]" placeholder="选择 YYYY"/>
<br/><br/>
<fb-datepicker format="YYYY-MM" :show-foot-left-btns="[]" placeholder="选择 YYYY-MM"/>
<br/><br/>
<fb-datepicker format="YYYYMMDD" :show-foot-left-btns="['today']" placeholder="选择 YYYYMMDD"/>
<br/><br/>
<fb-datepicker format="YYYY-MM-DD HH:mm" placeholder="选择 YYYY-MM-DD HH:mm"/>
<br/><br/>
<fb-datepicker mode="multiple" placeholder="选择多个"/>
<br/><br/>
<fb-datepicker mode="range" placeholder="范围选择"/>
<br/><br/>
<fb-datepicker :showConfirm="true" placeholder="确认选择"/>
<br/><br/>
<fb-datepicker clearable placeholder="带内置“清除”按钮"/>


<br/><br/>
控件样式：

<br/><br/>
<fb-datepicker placeholder="自定义宽度：30%" style="width: 30%"></fb-datepicker>
<fb-datepicker placeholder="自定义宽度：150px" style="width: 150px"></fb-datepicker>
<br/><br/>
<fb-datepicker placeholder="只读" width="30%" readonly></fb-datepicker>
<br/><br/>
<fb-datepicker placeholder="禁用" width="30%" disabled></fb-datepicker>

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

### 时间段查询



```html run {title:'示例演示'}
<template>
<div style="height:420px">
<fb-page-search>
	<template slot="query">
		<br/>
		<fb-form>
			<fb-row>
				<fb-col span="11" offset="1">
					<fb-form-item label="双格分布" :content-style="{ }" style="display: inline-block; width: 56%">
						<fb-datepicker  v-model="queryForm.time1_1" :maxDate="queryForm.time1_2"></fb-datepicker>
					</fb-form-item>
					<fb-form-item  label="-" :content-style="{marginLeft: '30px'}" :label-style="{width:'30px', textAlign: 'center', paddingLeft: '12px'}"  style="display: inline-block; width: 43%">
						<fb-datepicker  v-model="queryForm.time1_2" :minDate="queryForm.time1_1"></fb-datepicker>
					</fb-form-item>
				</fb-col>
				<fb-col span="11" offset="1">
					<fb-form-item label="地址">
						<fb-input  v-model="queryForm.address"></fb-input>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="11" offset="1">
					<fb-form-item label="单格分布" :content-style="{textAlign: 'center'}" >
						<fb-datepicker  v-model="queryForm.time2_1" :maxDate="queryForm.time2_2" style="width:47%; float: left"></fb-datepicker>
						<span style="vertical-align: -webkit-baseline-middle">-</span>
						<fb-datepicker  v-model="queryForm.time2_2" :minDate="queryForm.time2_1" style="width:47%; float: right"></fb-datepicker>
					</fb-form-item>
				</fb-col>
				<fb-col span="11" offset="1">
					<fb-form-item label="编号">
						<fb-input v-model="queryForm.code"></fb-input>
					</fb-form-item>
				</fb-col>
			</fb-row>
		</fb-form>
		<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
	</template>
	<template slot="buttons">
		{{queryForm}}
	</template>
</fb-page-search>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	queryForm:{
			code:'',
			address:'',
			time1_1:"",
			time1_2:'',
			time2_1:'',
			time2_2:'',}
      }
    },
    methods: {
        handleQuery() {
        },
	}
  }
</script>
<style>
</style>
```

## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-enter | 当键盘按下回车键时触发，可以是方法名，或方法体 | (event) => void
| on-clear |  |
| on-blur | 当输入框失去焦点时触发，可以是方法名，或方法体 | (event) => void
| on-input | 当输入框内容变化时触发，可以是方法名，或方法体 | (val) => void
| on-change | 当输入框内容变化时触发，可以是方法名，或方法体 | (val) => void
| on-input-change |  |



## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 当前值 | Date, Array, Number, String |
| disabled | 禁用状态 | Boolean  | false
| readonly | 只读状态 | Boolean  | false
| mode | 显示模式，可选值是：single, multiple, range | String | single
| showConfirm | 是否显示确认按钮, 要点击确认才会关掉面板 | Boolean | false
| rangeSeparator | 区间选择时显示的分隔字符 | String | ~
| clearable | 是否显示清除图标 | Boolean | false
| placeholder | 占位符 | String | 请选择日期
| format | 格式化 | String | YYYY-MM-DD
| enableTime | 是否启用时间面板 | Boolean | false
| enableSeconds | 是否启用秒 | Boolean | false
| onlyMonth | 是否只显示月份 | Boolean | false
| minDate | 能选的最小日期 | Date |
| maxDate | 能选的最小日期 | Date |
| maxRange | 以当前日期为中心,向左右延伸可选的日期，如：7D 七天， 1M 一个月， 2Y 两年 | String |
| disable | 禁用的时间 | Array | []
| enable | 允许的时间 | Array | []
| disabledDate | 禁用的日期 | Function | () => false
| showFootLeftBtns | 底部左侧默认按钮组 [] 空数组不显示 | Array | ['today', 'yesterday', 'oneweekago']
