/*!
 * fb-textarea
 * (c) 2022 lincong1987
 */

import searchTypes from '../searchTypes'

export default {
	label: '下拉树选择',
	value: 'fb-tree-select',
	icon: 'tree-expansion',
	index: 1,
	rules: [
		{
			label: '必填',
			value: 'required',
		},

		{
			label: '复选',
			value: 'array',
		},
	],

	sizes: [
		{
			label: '小',
			value: 's',
		}, {
			label: '默认',
			value: 'm',
		},
		{
			label: '大',
			value: 'l',
		},
	],

	searchTypes,

	fastBizProps: [
		{
			label: '字典树',
			value: 'biz-dic-select-tree',
		},
		{
			label: '字典下拉框',
			value: 'biz-dic-select',
		},
		{
			label: '政府机构树（完整）',
			value: 'biz-org-select-tree-full',
		},
		{
			label: '政府机构树（本级及下级）',
			value: 'biz-org-select-tree-ps',
		},
		{
			label: '企业部门树（完整）',
			value: 'biz-ent-select-tree-full',
		},
		{
			label: '企业部门树（本级及下级）',
			value: 'biz-ent-select-tree-ps',
		},
		{
			label: '行政区划（完整）',
			value: 'biz-city-select-tree-full',
		},
		{
			label: '行政区划（本级及下级）',
			value: 'biz-city-select-tree-ps',
		},
		{
			label: '行政区划（两级）',
			value: 'biz-city-select-tree-two-level',
		},
		{
			label: '是否启用',
			value: 'enabled',
		},
		{
			label: '是否激活',
			value: 'active',
		},
		{
			label: '性别',
			value: 'sex',
		},
		{
			label: '星期选择',
			value: 'week',
		},
	],

	defaults: {
		data: [],
		param: [],
		placeholder: '请选择',
		doCheck: 'ps',
		doUnCheck: 'ps',
		onlyLeaf: false,
		reader: [
			{
				'label': 'label',
				'value': '',
			},
			{
				'label': 'value',
				'value': '',
			},
		],
	},

}
