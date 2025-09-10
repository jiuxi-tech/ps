/*!
 * fb-input
 * (c) 2022 lincong1987
 */

import searchTypes from '../searchTypes'

export default {

	label: '文本框',
	value: 'fb-input',
	icon: 'text',
	index: 1,

	types: [
		{
			label: '文本',
			value: 'text',
		},
		{
			label: '密码',
			value: 'password',
		},
	],

	sizes: [
		{
			label: '小',
			value: 's',
		}, {
			label: '默认',
			value: 'm',
		},
		{
			label: '大',
			value: 'l',
		},
	],

	rules: [
		{
			label: '必填',
			value: 'required',
		},
		{
			label: '文本',
			value: 'string',
		},
		{
			label: '数字',
			value: 'number',
		},
		{
			label: '整数',
			value: 'integer',
		},
		{
			label: '浮点',
			value: 'float',
		},
		{
			label: '金额',
			value: 'money',
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
			label: '枚举',
			value: 'enum',
		},
	],

	searchTypes,

	defaults: {},

}
