/*!
 * fb-textarea
 * (c) 2022 lincong1987
 */

import searchTypes from '../searchTypes'

export default {


	label: '复选框组',
	value: 'fb-checkbox-group',
	icon: 'selected-square',
	index: 1,

	rules: [
		{
			label: '必填',
			value: 'required',
		},
		{
			label: '复选',
			value: 'array',
		},
	],

	searchTypes,

	fastBizProps: [
		{
			label: '星期选择',
			value: 'week',
		},
		{
			label: '季度选择',
			value: 'quarter',
		},
	],

	defaults: {
		defaultValue: [],
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
