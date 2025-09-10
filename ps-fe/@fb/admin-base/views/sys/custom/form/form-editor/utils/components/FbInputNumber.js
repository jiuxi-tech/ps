/*!
 * FbInputNumber
 * (c) 2022 lincong1987
 */

import searchTypes from '../searchTypes'


export default {
	label: '数字输入框',
	value: 'fb-input-number',
	icon: 'verificationcode',
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
	],

	controlsPosition: [
		{
			label: '左右',
			value: '',
		},
		{
			label: '右',
			value: 'right',
		},
	],

	searchTypes: [],

	defaults: {
		//设置计数器允许的最小值
		min: -Infinity,
		//设置计数器允许的最大值
		max: Infinity,
		//计数器步长
		step: 1,
		//是否只能输入 step 的倍数
		stepStrictly: false,
		//数值精度
		precision: 0,
		//计数器尺寸
		size: 'm',
		//是否禁用
		disabled: false,
		//是否使用控制按钮
		controls: true,
		// 控制按钮位置 right
		controlsPosition: '',
		//输入框默认
		placeholder: '请输入',

	}
	,

}
