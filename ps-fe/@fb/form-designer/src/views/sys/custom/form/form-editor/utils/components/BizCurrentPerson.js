/*!
 * fb-textarea
 * (c) 2022 lincong1987
 */

export default {
	label: '当前人员',
	value: 'biz-current-person',
	icon: 'contract-management',
	index: 1,
	defaults: {
		placeholder: '请输入人员名称',
		queryPlaceholder: '请输入人员名称',
		addPlaceholder: '请输入人员名称',
		editPlaceholder: '请输入人员名称',
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
