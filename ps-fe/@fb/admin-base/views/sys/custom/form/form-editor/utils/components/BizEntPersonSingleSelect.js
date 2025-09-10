/*!
 * biz-ent-person-single-select
 * (c) 2022 lincong1987
 */

export default {

	label: '企业人员单选',
	value: 'biz-ent-person-single-select',
	icon: 'user',
	index: 1,

	rules: [
		{
			label: '必填',
			value: 'required',
		},
	],

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

	defaults: {
		placeholder: '点击选择',
	},
}
