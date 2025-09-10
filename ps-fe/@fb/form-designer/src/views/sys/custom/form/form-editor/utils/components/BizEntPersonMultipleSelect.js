/*!
 * biz-ent-person-multiple-select
 * (c) 2022 lincong1987
 */

export default {
	label: '企业人员多选',
	value: 'biz-ent-person-multiple-select',
	icon: 'user-groups',
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
