/*!
 * fb-textarea
 * (c) 2022 lincong1987
 */

export default {
	label: '当前机构',
	value: 'biz-current-org',
	icon: 'contract-management',
	index: 1,
	defaults: {
		placeholder: '请输入机构',

		queryPlaceholder: '请输入机构',
		addPlaceholder: '请输入机构',
		editPlaceholder: '请输入机构',

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
