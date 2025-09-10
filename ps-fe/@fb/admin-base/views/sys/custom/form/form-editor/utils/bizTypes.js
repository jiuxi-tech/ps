/*!
 * bizTypes
 * (c) 2021 lincong1987
 */

export const bizTypes = {

	// 字典树
	'biz-dic-select-tree': {
		service: 'sys.dict.tree',
		param: [
			{
				label: 'dicCode',
				value: 'E10',
			},
		],
		reader: [
			{
				'label': 'label',
				'value': 'text',
			},
			{
				'label': 'value',
				'value': 'id',
			},
		],
	},

	// 字典下拉框
	'biz-dic-select': {
		service: 'sys.dict.select',
		param: [
			{
				label: 'pdicCode',
				value: 'E09',
			},
		],
		reader: [
			{
				'label': 'label',
				'value': 'text',
			},
			{
				'label': 'value',
				'value': 'id',
			},
		],
	},

	// 政府机构树（完整）
	'biz-org-select-tree-full': {
		service: 'sys.org.allTree',
		reader: [
			{
				'label': 'label',
				'value': 'text',
			},
			{
				'label': 'value',
				'value': 'id',
			},
		],
	},

	// 政府机构树（本级及下级）
	'biz-org-select-tree-ps': {
		service: 'sys.org.tree',
		reader: [
			{
				'label': 'label',
				'value': 'text',
			},
			{
				'label': 'value',
				'value': 'id',
			},
		],
	},

	// 企业部门树（完整）
	'biz-ent-select-tree-full': {
		service: 'sys.ent.allTree',
		reader: [
			{
				'label': 'label',
				'value': 'text',
			},
			{
				'label': 'value',
				'value': 'id',
			},
		],
	},

	// 企业部门树（本级及下级）
	'biz-ent-select-tree-ps': {
		service: 'sys.ent.tree',
		reader: [
			{
				'label': 'label',
				'value': 'text',
			},
			{
				'label': 'value',
				'value': 'id',
			},
		],
	},

	// 行政区划（完整）
	'biz-city-select-tree-full': {
		service: 'sys.city.tree',
		param: [
			{
				label: 'sync',
				value: 1,
			},
			{
				label: 'expand',
				value: false,
			},
			{
				label: 'cityId',
				value: '-1',
			},
		],
		reader: [
			{
				'label': 'label',
				'value': 'text',
			},
			{
				'label': 'value',
				'value': 'extend02',
			},
		],
	},

	// 行政区划（本级及下级）
	'biz-city-select-tree-ps': {
		service: 'sys.dept.deptCityTree',
		reader: [
			{
				'label': 'label',
				'value': 'text',
			},
			{
				'label': 'value',
				'value': 'id',
			},
		],
	},

	// 行政区划（两级）
	'biz-city-select-tree-two-level': {
		service: 'sys.city.selectTwoLevel',
		param: [
			{
				label: 'sync',
				value: 1,
			},
			{
				label: 'expand',
				value: false,
			},
			{
				label: 'cityId',
				value: '-1',
			},
		],
		reader: [
			{
				'label': 'label',
				'value': 'text',
			},
			{
				'label': 'value',
				'value': 'extend02',
			},
		],
	},

	/**************************************************************************/
	/* 证件类型 */
	/**************************************************************************/
//	'biz-Y24': {
//		placeholder: '请选择证件类型',
//		service: 'sys.dict.select',
//		param: [
//			{
//				label: 'pdicCode',
//				value: 'Y24',
//			},
//		],
//	},
//
//	'biz-S24': {
//		placeholder: '请选择证件类型',
//		service: 'sys.dict.select',
//		param: [
//			{
//				label: 'pdicCode',
//				value: 'S24',
//			},
//		],
//	},

	/**************************************************************************/
	/**************************************************************************/
	/**************************************************************************/
	/**************************************************************************/
	/**************************************************************************/
	/**************************************************************************/
	/**************************************************************************/



//			{label: '行政区划（完整）', value: ''},
//			{label: '', value: ''},
//			{label: '', value: ''},

	'enabled': {
		data: [
			{
				'label': '启用',
				'value': 1,
			},
			{
				'label': '禁用',
				'value': 0,
			},
		],
	},

	'active': {
		data: [
			{
				'label': '是',
				'value': 1,
			},
			{
				'label': '否',
				'value': 0,
			},
		],
	},

	'yesorno': {
		data: [
			{
				'label': '是',
				'value': 1,
			},
			{
				'label': '否',
				'value': 0,
			},
		],
	},

	'sex': {
		data: [
			{
				'label': '保密',
				'value': 0,
			},
			{
				'label': '男',
				'value': 1,
			},
			{
				'label': '女',
				'value': 2,
			},

		],
	},

	'week': {
		data: [
			{
				'label': '星期一',
				'value': 1,
			},
			{
				'label': '星期二',
				'value': 2,
			},
			{
				'label': '星期三',
				'value': 3,
			},
			{
				'label': '星期四',
				'value': 4,
			},
			{
				'label': '星期五',
				'value': 5,
			},
			{
				'label': '星期六',
				'value': 6,
			},
			{
				'label': '星期日',
				'value': 7,
			},
		],
	},

	'quarter': {
		data: [
			{
				'label': '第一季度',
				'value': 1,
			},
			{
				'label': '第二季度',
				'value': 2,
			},
			{
				'label': '第三季度',
				'value': 3,
			},
			{
				'label': '第四季度',
				'value': 4,
			},
		],
	},

	'date_format_YYYYMMDDHHmmss': {
		format: 'YYYY-MM-DD HH:mm:ss',
		valueFormat: 'YYYYMMDDHHmmss',
	},
	'date_format_YYYYMMDD': {
		format: 'YYYY-MM-DD',
		valueFormat: 'YYYYMMDD',
	},
	'date_format_YYYY': {
		format: 'YYYY',
		valueFormat: 'YYYY',
	},
	'date_format_YYYYMM': {
		format: 'YYYY-MM',
		valueFormat: 'YYYYMM',
	},
	'date_format_HHmmss': {
		format: 'HH:mm:ss',
		valueFormat: 'HHmmss',
	},
	'date_format_HHmm': {
		format: 'HH:mm',
		valueFormat: 'HHmm',
	},
	'date_format_HH': {
		format: 'HH',
		valueFormat: 'HH',
	},
	'date_format_mmss': {
		format: 'mmss',
		valueFormat: 'mm:ss',
	},

}

export default {bizTypes}
