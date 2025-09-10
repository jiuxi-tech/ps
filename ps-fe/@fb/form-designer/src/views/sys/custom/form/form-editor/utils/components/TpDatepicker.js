/*!
 * fb-textarea
 * (c) 2022 lincong1987
 */

import searchTypes from '../searchTypes'

export default {

	label: '日期选择',
	value: 'tp-datepicker',
	icon: 'calendar',
	index: 1,

	rules: [
		{
			label: '必填',
			value: 'required',
		},
	],

	searchTypes,

	fastBizProps: [
		{
			label: '精确到秒',
			value: 'date_format_YYYYMMDDHHmmss',
		},
		{
			label: '年月日',
			value: 'date_format_YYYYMMDD',
		},
		{
			label: '年',
			value: 'date_format_YYYY',
		},
		{
			label: '年月',
			value: 'date_format_YYYYMM',
		},
//							{label: '时分秒', value: 'date_format_HHmmss'},
//							{label: '时分', value: 'date_format_HHmm'},
//							{label: '小时', value: 'date_format_HH'},
//							{label: '分秒', value: 'date_format_mmss'},
	],

	defaults: {
		format: 'YYYY-MM-DD',
		valueFormat: 'YYYYMMDD',
	},
}
