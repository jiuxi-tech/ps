/*!
 * search
 * (c) 2021 lincong1987
 */

let search = {}

export default {

	props: {
		show_buttons: true,
		show_table: true,
		show_query: true,
		show_actions: true,
		referType: '',
		referTypePattern: '',
	},

	query: {
		props: {
			layout: 'row-12',
			labelWidth: 120,
		},
		rows: [],
	},

	table: {
		props: {
			rownum: true,
			rownumTitle: '#',
			rownumWidth: 30,
			pk: 'rownum',
			columns: [],
			service: null,
			data: [],
			mockData: [],
			param: [],
			//formatters: {},
			cellAttrs: {},
			cellStyles: {},
			scroll: {
			},
			size: 'm',
			loadingText: '',
			caption: '',
			footer: '',
			noDataText: '没有数据',
			noEmpty: false,
			cellSpans: {},
			headerFormatters: {},
			headerAttrs: {},
			renders: {},
			sorters: {},
			showHeader: true,
			formatters: {},
			reader: [
				{
					label: 'pagerSize',
					value: 'size',
				},
				{
					label: 'pagerCurrent',
					value: 'current',
				},
				{
					label: 'pagerTotal',
					value: 'total',
				},
				{
					label: 'pagerPages',
					value: 'pages',
				},
				{
					label: 'sortByColumn',
					value: 'sortByColumn',
				},
				{
					label: 'sortByDirection',
					value: 'sortByDirection',
				},
			],
			pager: [
				{
					label: 'position',
					value: 'bottom',
				},
				{
					label: 'align',
					value: 'right',
				},
				{
					label: 'simple',
					value: false,
				},
				{
					label: 'current',
					value: 1,
				},
				{
					label: 'size',
					value: 10,
				},
				{
					label: 'pages',
					value: 0,
				},
				{
					label: 'showTotalInfo',
					value: true,
				},
			],
			showPager: true,
			noPager: false,
			keepSelected: false,
			autoSelect: false,
			autoQuery: false,
			showLoading: true,
			autoScroll: false,
			autoScrollSpeed: 100,
			autoScrollStep: 1,
			autoScrollDelay: 200,
		},

	},

	actions: {
		add: {
			label: '新增',
			show: true,
			code: 'ADD',
			icon: 'add-circle',
		},
		modify: {
			label: '修改',
			show: true,
			code: 'EDIT',
		},
		delete: {
			label: '删除',
			show: true,
			code: 'DELETE',
		},
		query: {
			label: '查询',
			show: true,
			code: 'QUERY',
			icon: 'search',
		},
		'export': {
			label: '导出',
			show: true,
			code: 'EXPORT',
			icon: 'export',
		},
	},

}
