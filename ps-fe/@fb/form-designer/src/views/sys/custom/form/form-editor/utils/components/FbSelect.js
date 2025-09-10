/*!
 * fb-textarea
 * (c) 2022 lincong1987
 */

import searchTypes from '../searchTypes'

export default {
	label: '下拉选择',
	value: 'fb-select',
	icon: 'down-square',
	index: 1,
	rules: [
		{
			label: '必填',
			value: 'required',
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

	searchTypes,

	fastBizProps: [
		{
			label: '字典下拉框',
			value: 'biz-dic-select',
		},
		{
			label: '是否启用',
			value: 'enabled',
		},
		{
			label: '是否激活',
			value: 'active',
		},
		{
			label: '性别',
			value: 'sex',
		},
		{
			label: '星期选择',
			value: 'week',
		},

	],

	defaults: {
		data: [],
		param: [],
		placeholder: '请选择',
		reader: [
			{
				'label': 'label',
				'value': '',
			},
			{
				'label': 'value',
				'value': '',
			},
		],

	},
}
