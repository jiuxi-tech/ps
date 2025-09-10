/*!
 * biz-org-ent-multiple-select
 * (c) 2022 lincong1987
 */

export default {
	label: '政府企业多选',
	value: 'biz-org-ent-multiple-select',
	icon: 'enterprise-search',
	index: 1,
	rules: [
		{
			label: '必填',
			value: 'required',
		},
	],
	defaults: {
		placeholder: '点击选择',
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
