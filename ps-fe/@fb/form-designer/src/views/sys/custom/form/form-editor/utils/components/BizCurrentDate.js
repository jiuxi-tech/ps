/*!
 * fb-textarea
 * (c) 2022 lincong1987
 */

export default {
	label: '当前日期',
	value: 'biz-current-date',
	icon: 'contract-management',
	index: 1,
	defaults: {
		placeholder: '当前日期',
	},

	searchTypes: [
		{
			label: '模糊',
			value: 'like',
		},
		{
			label: '等于',
			value: '=',
		},
	],
}
