/*!
 * FbInputNumber
 * (c) 2022 lincong1987
 */

import searchTypes from '../searchTypes'

let FbInputNumber = {}

export default {

	label: '颜色选择器',
	value: 'fb-color-picker',
	icon: 'down-square',
	index: 1,

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
	],

	searchTypes: [],

	defaults: {},

}
