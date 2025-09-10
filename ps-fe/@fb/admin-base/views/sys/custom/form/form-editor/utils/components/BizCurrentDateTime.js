/*!
 * fb-textarea
 * (c) 2022 lincong1987
 */

export default {
	label: '当前日期时间',
	value: 'biz-current-date-time',
	icon: 'contract-management',
	index: 1,
	defaults: {
		placeholder: '请选择日期时间',

		queryPlaceholder: '请选择日期时间',
		addPlaceholder: '请选择日期时间',
		editPlaceholder: '请选择日期时间',

		format: 'YYYY-MM-DD HH:mm:ss',
		modifiable: false,
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
