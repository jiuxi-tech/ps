/*!
 * fb-textarea
 * (c) 2022 lincong1987
 */

import searchTypes from '../searchTypes'

export default {
	label: '文本域',
	value: 'fb-textarea',
	icon: 'textareaItem',
	index: 1,
	rules: [
		{
			label: '必填',
			value: 'required',
		},
	],

	sizes: [
		{
			label: '默认',
			value: 'm',
		},
		{
			label: '大',
			value: 'l',
		},
	],

	searchTypes,
	defaults: {
		rows: 3,
	},
}
