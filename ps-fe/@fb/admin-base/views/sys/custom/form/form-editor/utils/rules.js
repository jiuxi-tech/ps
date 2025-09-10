/*!
 * input-rules
 * (c) 2021 lincong1987
 */

export default {

	'fb-select': [
		{
			label: '必填',
			value: 'required',
		},
	],

	'fb-input': [
		{
			label: '必填',
			value: 'required',
		},
		{
			label: '字母',
			value: 'letters',
		},
		{
			label: 'QQ号',
			value: 'qq',
		},
		{
			label: '身份证号码',
			value: 'idcard',
		},
		{
			label: '工商注册号',
			value: 'bizcode',
		},
		{
			label: '组织机构代码',
			value: 'orgcode',
		},
		{
			label: '统一社会信用代码',
			value: 'unicode',
		},
		{
			label: '电话号码',
			value: 'tel',
		},
		{
			label: '手机号码',
			value: 'mobile',
		},
		{
			label: '电话或手机号码',
			value: 'telmobile',
		},
		{
			label: '邮政编码',
			value: 'zipcode',
		},
		{
			label: '中文字符',
			value: 'chinese',
		},
		{
			label: '3-12位 数字、字母、下划线',
			value: 'username',
		},
		{
			label: '8位以上，包含数字，字母或特殊符号',
			value: 'password',
		},
		{
			label: 'IP 地址',
			value: 'ip',
		},
		{
			label: '金额',
			value: 'money',
		},

	],

	'fb-textarea': [
		{
			label: '必填',
			value: 'required',
		},
	],

}
