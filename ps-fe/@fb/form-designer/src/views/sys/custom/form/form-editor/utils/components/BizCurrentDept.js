/*!
 * fb-textarea
 * (c) 2022 lincong1987
 */

export default {
	label: '当前部门',
	value: 'biz-current-dept',
	icon: 'contract-management',
	index: 1,
	defaults: {
		placeholder: '请输入部门',

		queryPlaceholder: '请输入部门',
		addPlaceholder: '请输入部门',
		editPlaceholder: '请输入部门',

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
