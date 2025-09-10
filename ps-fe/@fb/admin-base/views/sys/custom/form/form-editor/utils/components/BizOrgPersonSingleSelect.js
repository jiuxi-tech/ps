/*!
 * biz-org-person-single-select
 * (c) 2022 lincong1987
 */


export default {

	label: '政府人员单选',
	value: 'biz-org-person-single-select',
	icon: 'user-police',
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
