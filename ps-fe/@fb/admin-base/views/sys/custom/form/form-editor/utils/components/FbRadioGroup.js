/*!
 * fb-textarea
 * (c) 2022 lincong1987
 */

import searchTypes from '../searchTypes'

export default {

	label: '单选框组',
	value: 'fb-radio-group',
	icon: 'radio',
	index: 1,

	rules: [
		{
			label: '必填',
			value: 'required',
		},
	],

	searchTypes,

	fastBizProps: [
		{
			label: '字典下拉框',
			value: 'biz-dic-select',
		},
		{
			label: '是否',
			value: 'yesorno',
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
		radioSpace: 16,
		data: [],
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
