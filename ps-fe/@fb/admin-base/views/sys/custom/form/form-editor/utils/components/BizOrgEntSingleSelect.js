/*!
 * biz-org-ent-single-select
 * (c) 2022 lincong1987
 */

export default {
	label: '政府企业单选',
	value: 'biz-org-ent-single-select',
	icon: 'enterprise',
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
