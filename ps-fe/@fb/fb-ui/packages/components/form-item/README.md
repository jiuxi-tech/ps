[comment]: <> (fb-docs: docsify/fb-ui/03/validate/README.md)

# 表单校验-Validate
对表单域的数据进行校验。


## 基础用法

基于`fb-form-item`控件进行表单校验实现：

1. 使用 prop 属性，指定`校验器名称`，要求`唯一，不可重复`

```
<fb-form-item prop="valication-blur"/>
```
	
2、使用 :rule 属性，指定校验规则，可以是数组或对象

```
<fb-form-item prop="valication-blur" :rule='{required: true,trigger: "blur"}'/>
<fb-form-item prop="valication-blur" :rule='[{required:true},{type: "mobile"}]'/>
```

3、使用 label 属性，指定校验提示信息

```
<fb-form-item  prop="valication-blur" :rule='{required: true,trigger: "blur"}' label="用户名" />
```

4. 调用`validate()`方法执行校验

```
this.$refs[form].validate((result, error) => {
	if (result) {
		this.$message.success('校验通过')
	}
})
```
### 地址校验

```html run {title:'示例演示'}
<template>
	<fb-form ref="validAddressForm">
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="请求地址" :label-width="130"
							  prop="common-url" :rule='{type: "url"}'
				>
					<fb-input v-model="form.url"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "url"}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="IP地址" :label-width="130"
							  prop="common-ip" :rule='{type: "ip"}'
				>
					<fb-input v-model="form.ip"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "ip"}'
			</fb-col>
		</fb-row>

		<fb-row>
			<fb-col span="12">
				<fb-form-item label=" " :label-width="130">
					<fb-button type="primary" @on-click="doSubmit('validAddressForm')">提交</fb-button>
				</fb-form-item>
			</fb-col>
		</fb-row>

	</fb-form>
</template>

<script>
export default {
	data() {
		return {
			form: {
				url: '',
				ip: '',
			}
		}
	},
	methods: {
		doSubmit(form) {
			this.$refs[form].validate((result, error) => {
				if (result) {
					this.$message.success('校验通过')
				}
			})
		},
	}
}
</script>

```

### 通讯校验

```html run {title:'示例演示'}
<template>
<fb-form ref="validAddressForm">
	<fb-row>
		<fb-col span="12">
			<fb-form-item label="电子邮件" :label-width="130"
						  prop="common-email" :rule='{required:true, type: "email"}'
			>
				<fb-input v-model="form.email"/>
			</fb-form-item>
		</fb-col>
		<fb-col span="1">
		</fb-col>
		<fb-col span="11">
			:rule='{required:true, type: "email"}'
		</fb-col>
	</fb-row>
	<fb-row>
		<fb-col span="12">
			<fb-form-item label="邮政编码" :label-width="130"
						  prop="common-zipcode" :rule='[{required:true},{type: "zipcode"}]'
			>
				<fb-input v-model="form.zipcode"/>
			</fb-form-item>
		</fb-col>
		<fb-col span="1">
		</fb-col>
		<fb-col span="11">
			:rule='[{required:true},{type: "zipcode"}]'
		</fb-col>
	</fb-row>
	<fb-row>
		<fb-col span="12">
			<fb-form-item label="座机号码" :label-width="130"
						  prop="common-tel" :rule='[{required:true},{type: "tel"}]'
			>
				<fb-input v-model="form.tel"/>
			</fb-form-item>
		</fb-col>
		<fb-col span="1">
		</fb-col>
		<fb-col span="11">
			:rule='[{required:true},{type: "tel"}]'
		</fb-col>
	</fb-row>
	<fb-row>
		<fb-col span="12">
			<fb-form-item label="手机号码" :label-width="130"
						  prop="common-mobile" :rule='[{required:true},{type: "mobile"}]'
			>
				<fb-input v-model="form.mobile"/>
			</fb-form-item>
		</fb-col>
		<fb-col span="1">
		</fb-col>
		<fb-col span="11">
			:rule='[{required:true},{type: "mobile"}]'
		</fb-col>
	</fb-row>
	<fb-row>
		<fb-col span="12">
			<fb-form-item label="电话号码" :label-width="130"
						  prop="common-telmobile" :rule='[{required:true},{type: "telmobile"}]'
			>
				<fb-input v-model="form.telmobile"/>
			</fb-form-item>
		</fb-col>
		<fb-col span="1">
		</fb-col>
		<fb-col span="11">
			:rule='[{required:true},{type: "telmobile"}]'
		</fb-col>
	</fb-row>
	<fb-row>
		<fb-col span="12">
			<fb-form-item label=" " :label-width="130">
				<fb-button type="primary" @on-click="doSubmit('validAddressForm')">提交</fb-button>
			</fb-form-item>
		</fb-col>
	</fb-row>

</fb-form>
</template>

<script>
export default {
	data() {
		return {
			form: {
				email: '',
				zipcode: '',
				tel: '',
				mobile: '',
				telmobile: '',
			}
		}
	},
	methods: {
		doSubmit(form) {
			this.$refs[form].validate((result, error) => {
				if (result) {
					this.$message.success('校验通过')
				}
			})
		},
	}
}
</script>

```

### 身份校验

```html run {title:'示例演示'}
<template>
	<fb-form ref="validAddressForm">
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="身份证号" :label-width="130"
							  prop="common-idcard" :rule='[{required:true},{type: "idcard"}]'
				>
					<fb-input v-model="form.idcard"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='[{required:true},{type: "idcard"}]'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="工商注册号" :label-width="130"
							  prop="common-bizcode" :rule='[{required:true},{type: "bizcode"}]'
				>
					<fb-input v-model="form.bizcode"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='[{required:true},{type: "bizcode"}]'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="组织机构代码" :label-width="130"
							  prop="common-orgcode" :rule='[{required:true},{type: "orgcode"}]'
				>
					<fb-input v-model="form.orgcode"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='[{required:true},{type: "orgcode"}]'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="统一社会信用代码" :label-width="130"
							  prop="common-unicode" :rule='[{required:true},{type: "unicode"}]'
				>
					<fb-input v-model="form.unicode"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='[{required:true},{type: "unicode"}]'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="帐号校验" :label-width="130"
							  prop="common-username" :rule='[{required:true},{type: "username"}]'
				>
					<fb-input v-model="form.username"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='[{required:true},{type: "username"}]'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="密码校验" :label-width="130"
							  prop="common-password" :rule='[{required:true},{ type: "password"}]'
				>
					<fb-input v-model="form.password"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='[{required:true},{type: "password"}]'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label=" " :label-width="130">
					<fb-button type="primary" @on-click="doSubmit('validAddressForm')">提交</fb-button>
				</fb-form-item>
			</fb-col>
		</fb-row>

	</fb-form>
</template>

<script>
export default {
	data() {
		return {
			form: {
				idcard: '',
				bizcode: '',
				orgcode: '',
				unicode: '',
				username: '',
				password: '',
			}
		}
	},
	methods: {
		doSubmit(form) {
			this.$refs[form].validate((result, error) => {
				if (result) {
					this.$message.success('校验通过')
				}
			})
		},
	}
}
</script>

```

### 字符校验

```html run {title:'示例演示'}
<template>
	<fb-form ref="validAddressForm">
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="空白校验" prop="string-whitespace" :label-width="130"
							  :rule='{type: "string", whitespace:true,}'>
					<fb-textarea rows="4" size="s" v-model.trim="form.whitespace"></fb-textarea>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "string", whitespace:true,}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="字母校验" :label-width="130"
							  prop="common-letters" :rule='{type: "letters"}'
				>
					<fb-input v-model="form.letters"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "letters"}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="中文校验" :label-width="130"
							  prop="common-chinese" :rule='{type: "chinese"}'
				>
					<fb-input v-model="form.chinese"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "chinese"}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="长度校验" prop="string-len" :label-width="130"
							  :rule='{type: "string",len: 10}'>
					<fb-input v-model="form.len"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "string", len: 10}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="最小长度" prop="string-min" :label-width="130"
							  :rule='{type: "string",min: 2}'>
					<fb-input v-model="form.min"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "string", min: 2}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="最大长度" prop="string-max" :label-width="130"
							  :rule='{type: "string",max: 10}'>
					<fb-input v-model="form.max"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "string", max: 10}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="长度范围" prop="string-range" :label-width="130"
							  :rule='{type: "string",min: 2,max: 10}'>
					<fb-input v-model="form.range"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "string", min: 2, max: 10}'
			</fb-col>
		</fb-row>

		<fb-row>
			<fb-col span="12">
				<fb-form-item label=" " :label-width="130">
					<fb-button type="primary" @on-click="doSubmit('validAddressForm')">提交</fb-button>
				</fb-form-item>
			</fb-col>
		</fb-row>

	</fb-form>
</template>

<script>
export default {
	data() {
		return {
			form: {
				letters: '',
				chinese: '',
				whitespace: '',
				len: '',
				min: '',
				max: '',
				range: '',
			}
		}
	},
	methods: {
		doSubmit(form) {
			this.$refs[form].validate((result, error) => {
				if (result) {
					this.$message.success('校验通过')
				}
			})
		},
	}
}
</script>

```

### 数字校验

```html run {title:'示例演示'}
<template>
	<fb-form ref="validAddressForm">
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="数字校验" prop="number-number" :label-width="130"
							  :rule='{type: "number"}'>
					<fb-input v-model.number="form.number"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "number"}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="整数校验" prop="number-integer" :label-width="130"
							  :rule='{type: "integer"}'>
					<fb-input v-model="form.integer"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "integer"}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="浮点校验" prop="number-float" :label-width="130"
							  :rule='{type: "float",fixed: 2}'>
					<fb-input v-model="form.float"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "float"}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="相等校验" prop="number-len" :label-width="130"
							  :rule='{type: "number",len: 10}'>
					<fb-input v-model="form.len"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "number",len: 10}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="最小校验" prop="number-min" :label-width="130"
							  :rule='[{required:true},{type: "number", min: 0}]'>
					<fb-input v-model="form.min"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='[{required:true},{type: "number", min: 0}]'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="最大校验" prop="number-max" :label-width="130"
							  :rule='[{required:true},{type: "number",max: 0}]'>
					<fb-input v-model="form.max"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='[{required:true},{type: "number", max: 0}]'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="范围校验" prop="number-range" :label-width="130"
							  :rule='{type: "number",min: 2,max: 10}'>
					<fb-input v-model="form.range"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "number", min: 2, max: 10}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="金额校验" prop="number-money" :label-width="130"
							  :rule='{type: "money",fixed: 2}'
				>
					<fb-input v-model="form.money"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "money", fixed: 2}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label=" " :label-width="130">
					<fb-button type="primary" @on-click="doSubmit('validAddressForm')">提交</fb-button>
				</fb-form-item>
			</fb-col>
		</fb-row>

	</fb-form>
</template>

<script>
export default {
	data() {
		return {
			form: {
				number: '',
				integer: '',
				float: '',
				len: '',
				min: '',
				max: '',
				range: '',
				money: '',
			}
		}
	},
	methods: {
		doSubmit(form) {
			this.$refs[form].validate((result, error) => {
				if (result) {
					this.$message.success('校验通过')
				}
			})
		},
	}
}
</script>

```

### 单选校验

```html run {title:'示例演示'}
<template>
	<fb-form ref="validAddressForm">
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="必填校验" prop="radio-required" :label-width="130"
							  :rule='{required: true}'>
					<fb-radio-group v-model="form.required" :data="displays"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{required: true}'
			</fb-col>
		</fb-row>
		<fb-row v-if="form.required === 1">
			<fb-col span="12">
				<fb-form-item label="显示校验" prop="radio-display" :label-width="130"
							  :rule='{required: true}'>
					<fb-input v-model="form.display"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{required: true}'   通过 v-if 设置控件的显示与隐藏
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label=" " :label-width="130">
					<fb-button type="primary" @on-click="doSubmit('validAddressForm')">提交</fb-button>
				</fb-form-item>
			</fb-col>
		</fb-row>

	</fb-form>
</template>

<script>
export default {
	data() {
		return {
			form: {
				required: 0,
				display: '',
			},
			displays: [
				{
					value: 1,
					label: '显示',
				}, {
					value: 0,
					label: '隐藏',
				},
			],
		}
	},
	methods: {
		doSubmit(form) {
			this.$refs[form].validate((result, error) => {
				if (result) {
					this.$message.success('校验通过')
				}
			})
		},
	}
}
</script>

```

### 复选校验

```html run {title:'示例演示'}
<template>
	<fb-form ref="validAddressForm">
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="必填校验" prop="checkbox-required" :label-width="130"
							  :rule='{required: true}'>
					<fb-checkbox-group v-model="form.required" :data="nodes"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{required: true}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="个数校验" prop="checkbox-len" :label-width="130"
							  :rule='{type: "array",len: 3}'>
					<fb-checkbox-group v-model="form.len" :data="nodes"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "array",len: 3}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="最小校验" prop="checkbox-min" :label-width="130"
							  :rule='{type: "array", min: 2}'>
					<fb-checkbox-group v-model="form.min" :data="nodes"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "array", min: 2}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="最大校验" prop="checkbox-max" :label-width="130"
							  :rule='{type: "array",max: 5}'>
					<fb-checkbox-group v-model="form.max" :data="nodes"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "array",max: 5}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="范围校验" prop="checkbox-range" :label-width="130"
							  :rule='{type: "array",min: 2,max: 5}'>
					<fb-checkbox-group v-model="form.range" :data="nodes"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "array",min: 2,max: 5}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label=" " :label-width="130">
					<fb-button type="primary" @on-click="doSubmit('validAddressForm')">提交</fb-button>
				</fb-form-item>
			</fb-col>
		</fb-row>

	</fb-form>
</template>

<script>
export default {
	data() {
		return {
			form: {
				required: [],
				len: [],
				min: [],
				max: [],
				range: [],
			},
			nodes: [
				{value: 0, label: '部门0', children: [{value: 0, label: '人员0'}]}, 
				{value: 1, label: '部门1', children: [{value: 1, label: '人员1'}]}, 
				{value: 3, label: '部门3', children: [{value: 3, label: '人员3'}]}, 
				{value: 4, label: '部门4', children: [{value: 4, label: '人员4'}]}, 
				{value: 5, label: '部门5', children: [{value: 5, label: '人员5'}]}, 
			],
		}
	},
	methods: {
		doSubmit(form) {
			this.$refs[form].validate((result, error) => {
				if (result) {
					this.$message.success('校验通过')
				}
			})
		},
	}
}
</script>

```

### 下拉列表校验

```html run {title:'示例演示'}
<template>
	<fb-form ref="validAddressForm">
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="单选必填" prop="select-single" :label-width="130"
							  :rule='{required: true}'>
					<fb-select v-model="form.single" :data="nodes"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{required: true}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="多选必填" prop="select-multiple" :label-width="130"
							  :rule='{required: true}'>
					<fb-select v-model="form.multiple" :data="nodes" multiple/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{required: true}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="搜索必填" prop="select-search" :label-width="130"
							  :rule='{required: true}'>
					<fb-select v-model="form.search" :data="nodes"
							   :reader="{value:'value', label: 'label'}"
							   clearable
							   filterable/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{required: true}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="个数校验" prop="select-len" :label-width="130"
							  :rule='{type: "array",len: 3}'>
					<fb-select v-model="form.len" :data="nodes" multiple/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "array",len: 3}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="最小校验" prop="select-min" :label-width="130"
							  :rule='{type: "array", min: 2}'>
					<fb-select v-model="form.min" :data="nodes" multiple/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "array", min: 2}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="最大校验" prop="select-max" :label-width="130"
							  :rule='{type: "array",max: 5}'>
					<fb-select v-model="form.max" :data="nodes" multiple/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "array",max: 5}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="范围校验" prop="select-range" :label-width="130"
							  :rule='{type: "array",min: 2,max: 5}'>
					<fb-select v-model="form.range" :data="nodes" multiple/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "array",min: 2,max: 5}'
			</fb-col>
		</fb-row>

		<fb-row>
			<fb-col span="12">
				<fb-form-item label=" " :label-width="130">
					<fb-button type="primary" @on-click="doSubmit('validAddressForm')">提交</fb-button>
				</fb-form-item>
			</fb-col>
		</fb-row>

	</fb-form>
</template>

<script>
export default {
	data() {
		return {
			form: {
				single: '',
				multiple: '',
				search: '',
				len: [],
				min: [],
				max: [],
				range: [],
			},
			nodes: [
				{value: 0, label: '部门0', children: [{value: 0, label: '人员0'}]}, 
				{value: 1, label: '部门1', children: [{value: 1, label: '人员1'}]}, 
				{value: 3, label: '部门3', children: [{value: 3, label: '人员3'}]}, 
				{value: 4, label: '部门4', children: [{value: 4, label: '人员4'}]}, 
				{value: 5, label: '部门5', children: [{value: 5, label: '人员5'}]}, 
				{value: 6, label: '部门6', children: [{value: 6, label: '人员6'}]}, 
				{value: 7, label: '部门7', children: [{value: 7, label: '人员7'}]}, 
				{value: 8, label: '部门8', children: [{value: 8, label: '人员8'}]}, 
				{value: 9, label: '部门9', children: [{value: 9, label: '人员9'}]}, 
			],
		}
	},
	methods: {
		doSubmit(form) {
			this.$refs[form].validate((result, error) => {
				if (result) {
					this.$message.success('校验通过')
				}
			})
		},
	}
}
</script>

```

### 下拉树校验

```html run {title:'示例演示'}
<template>
	<fb-form ref="validAddressForm">
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="单选必填" prop="treeselect-single" :label-width="130"
							  :rule='{required: true}'>
					<fb-tree-select v-model="form.single" :data="nodes"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{required: true}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="多选必填" prop="treeselect-multiple" :label-width="130"
							  :rule='{required: true}'>
					<fb-tree-select v-model="form.multiple" :data="nodes" multiple/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{required: true}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="搜索必填" prop="treeselect-search" :label-width="130"
							  :rule='{required: true}'>
					<fb-tree-select v-model="form.search" :data="nodes"
									:reader="{value:'value', label: 'label'}"
									clearable
									filterable/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{required: true}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="个数校验" prop="treeselect-len" :label-width="130"
							  :rule='{type: "array",len: 3}'>
					<fb-tree-select v-model="form.len" :data="nodes" multiple/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "array",len: 3}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="最小校验" prop="treeselect-min" :label-width="130"
							  :rule='{type: "array", min: 2}'>
					<fb-tree-select v-model="form.min" :data="nodes" multiple/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "array", min: 2}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="最大校验" prop="treeselect-max" :label-width="130"
							  :rule='{type: "array",max: 5}'>
					<fb-tree-select v-model="form.max" :data="nodes" multiple/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "array",max: 5}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="范围校验" prop="treeselect-range" :label-width="130"
							  :rule='{type: "array",min: 2,max: 5}'>
					<fb-tree-select v-model="form.range" :data="nodes" multiple/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "array",min: 2,max: 5}'
			</fb-col>
		</fb-row>

		<fb-row>
			<fb-col span="12">
				<fb-form-item label=" " :label-width="130">
					<fb-button type="primary" @on-click="doSubmit('validAddressForm')">提交</fb-button>
				</fb-form-item>
			</fb-col>
		</fb-row>

	</fb-form>
</template>

<script>
export default {
	data() {
		return {
			form: {
				single: '',
				multiple: '',
				search: '',
				len: [],
				min: [],
				max: [],
				range: [],
			},
			nodes: [
				{value: 0, label: '部门0', children: [{value: 0, label: '人员0'}]}, 
				{value: 1, label: '部门1', children: [{value: 1, label: '人员1'}]}, 
				{value: 3, label: '部门3', children: [{value: 3, label: '人员3'}]}, 
				{value: 4, label: '部门4', children: [{value: 4, label: '人员4'}]}, 
				{value: 5, label: '部门5', children: [{value: 5, label: '人员5'}]}, 
				{value: 6, label: '部门6', children: [{value: 6, label: '人员6'}]}, 
				{value: 7, label: '部门7', children: [{value: 7, label: '人员7'}]}, 
				{value: 8, label: '部门8', children: [{value: 8, label: '人员8'}]}, 
				{value: 9, label: '部门9', children: [{value: 9, label: '人员9'}]}, 
			],
		}
	},
	methods: {
		doSubmit(form) {
			this.$refs[form].validate((result, error) => {
				if (result) {
					this.$message.success('校验通过')
				}
			})
		},
	}
}
</script>

```

### 时间校验

```html run {title:'示例演示'}
<template>
	<fb-form ref="validAddressForm">
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="日期校验" prop="datepicker-single" :label-width="130"
							  :rule='{required: true}'>
					<fb-datepicker v-model="form.single"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{required: true}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="多个日期" prop="datepicker-multiple" :label-width="130"
							  :rule='{type: "array", min: 2}'>
					<fb-datepicker v-model="form.multiple" mode="multiple"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "array", min: 2}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="范围必填" prop="datepicker-range" :label-width="130"
							  :rule='{required: true}'>
					<fb-datepicker v-model="form.range" :min-date="new Date(2020, 10, 1)"
								   :max-date="new Date(2021, 2, 15)" mode="range"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{required: true}'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="时间必填" prop="datepicker-time" :label-width="130"
							  :rule='{required: true}'>
					<fb-timepicker v-model="form.time"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{required: true}'
			</fb-col>
		</fb-row>

		<fb-row>
			<fb-col span="12">
				<fb-form-item label=" " :label-width="130">
					<fb-button type="primary" @on-click="doSubmit('validAddressForm')">提交</fb-button>
				</fb-form-item>
			</fb-col>
		</fb-row>

	</fb-form>
</template>

<script>
export default {
	data() {
		return {
			form: {
				single: '',
				multiple: '',
				range: '',
				time: '',
			}
		}
	},
	methods: {
		doSubmit(form) {
			this.$refs[form].validate((result, error) => {
				if (result) {
					this.$message.success('校验通过')
				}
			})
		},
	}
}
</script>

```

### 其他用法

```html run {title:'示例演示'}
<template>
	<fb-form ref="validAddressForm">
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="枚举校验" :label-width="130"
							  prop="common-enum" :rule='{type: "enum",enum: ["1","2","3"] }'
				>
					<fb-input v-model="form.enum"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				:rule='{type: "enum",enum: ["1","2","3"] }'
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="组合校验" prop="custom-array" :label-width="130"
							  :rule='[{type: "string",max: 10},{type: "url"}]'>
					<p style="margin-top: 5px;">通过数组，可以设置多个校验规则，需要同时满足</p>
					<fb-input v-model="form.array"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				<p style="margin-top: 39px;">
					:rule='[{type: "string",max: 10},{type: "url"}]'
				</p>
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label=" " :label-width="130">
					<fb-button type="primary" @on-click="doSubmit('validAddressForm')">提交</fb-button>
				</fb-form-item>
			</fb-col>
		</fb-row>

	</fb-form>
</template>

<script>
export default {
	data() {
		return {
			form: {
				enum: '',
				array: '',
			},
		}
	},
	methods: {
		doSubmit(form) {
			this.$refs[form].validate((result, error) => {
				if (result) {
					this.$message.success('校验通过')
				}
			})
		},
	}
}
</script>

```


## 更多校验

### 自定义校验

1、在 data() 上定义 `rules` 规则集合，定义校验器`custom-field2`，用于检验两次输入是否一致

```
export default {
	data() {
		// 1. 定义上下文，可在 校验方法 中获取上下文
		let _this = this;
	
		return {
			rules: {
				// 2. 定义校验器名称：（要求唯一）
				"custom-field2": {
					// 3. 定义校验方法，方法名与参数固定不变
					validator: (rule, value, callback, source, options) => {
						// 4. 编写校验规则： 比较两控件的值
						if (_this.form.field1 != _this.form.field2) {
							// 5.1 校验未通过，返回错误信息
							return callback('上下两控件值 必须一致')
						}
	
						// 5.2 校验通过，返回空参数
						return callback();
					}
				}
			}
		}
	}
}
```

2、在 `fb-form` 控件上，使用`:rule` 属性，绑定校验规则集合`rules`

```
<fb-form ref="validCustomForm" :rule="rules"/>
```

3、在 fb-form-item 控件上，使用 prop 属性，指定校验器名称

```
<fb-form-item  prop="custom-field2"/>
```

4. 调用`fb-form`控件的`validate()`方法执行校验

```
this.$refs[form].validate((result, error) => {
	if (result) {
		this.$message.success('校验通过')
	}
})
```

```html run {title:'示例演示'}
<template>
	<fb-form ref="validAddressForm" :rule="rules">
		<fb-row>
			<fb-col span="12">
				<fb-form-item label="定制校验" prop="custom-field1" :label-width="120"
							  :rule='{required: true}'>
					<p style="margin-top: 5px;">通过 validator 方法，自定义业务校验规则</p>
					<fb-input v-model="form.field1"/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				<p style="margin-top: 39px;">
					:rule='{required: true}'
				</p>
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label=" " prop="custom-field2" :label-width="130">
					<fb-input v-model="form.field2"
							  placeholder="请输入与上面控件一样的值"
					/>
				</fb-form-item>
			</fb-col>
			<fb-col span="1">
			</fb-col>
			<fb-col span="11">
				prop="custom-field2"
			</fb-col>
		</fb-row>
		<fb-row>
			<fb-col span="12">
				<fb-form-item label=" " :label-width="130">
					<fb-button type="primary" @on-click="doSubmit('validAddressForm')">提交</fb-button>
				</fb-form-item>
			</fb-col>
		</fb-row>

	</fb-form>
</template>

<script>
export default {
	data() {
		let _this = this;
		return {
			form: {
				field1: '',
				field2: '',
			},
			rules: {
				'custom-field2': {
					validator: (rule, value, callback, source, options) => {
						// 比较两控件的值
						if (_this.form.field1 != _this.form.field2) {
							// 校验未通过，返回错误信息
							return callback('上下两控件值 必须一致')
						}

						// 校验通过
						return callback()
					},
				},
			},
		}
	},
	methods: {
		doSubmit(form) {
			this.$refs[form].validate((result, error) => {
				if (result) {
					this.$message.success('校验通过')
				}
			})
		},
	}
}
</script>

```