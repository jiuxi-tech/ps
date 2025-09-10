/*!
 * index
 * (c) 2020 lincong1987
 */

var index = {}

export default [

	{
		path: '/sys/custom/module/list',
		meta: {
			title: '自定义模块',
		},
		component: () => import('../../../../src/views/sys/custom/module/list.vue'),
	},
	{
		path: '/sys/custom/form/list',
		meta: {
			title: '自定义模块',
		},
		component: () => import('../../../../src/views/sys/custom/form/list.vue'),
	},
	{
		path: '/sys/custom/form/design',
		meta: {
			title: '设计',
		},
		component: () => import('../../../../src/views/sys/custom/form/design.vue'),
	},

	{
		path: '/sys/custom/form/runtime-test/:mid',
		meta: {
			title: '测试',
		},
		component: () => import('../../../../src/views/sys/custom/form/runtime-test'),
	},

	{
		path: '/sys/custom/form/runtime-list/:mcode',
		meta: {
			title: '运行',
		},
		component: () => import('../../../../src/views/sys/custom/form/runtime-list.vue'),
	},

	/*{
	 path: '/sys/custom-module/list',
	 meta: {
	 title: '自定义模块',
	 },
	 component: () => import('../../../../src/views/sys/custom/module/list.vue'),
	 }, {
	 path: '/sys/custom-form/list',
	 meta: {
	 title: '设计表单',
	 },
	 component: () => import('../../../../src/views/sys/custom/form/list.vue'),
	 }, */{
		path: '/sys/c-json/:mcode',
		meta: {
			title: '列表',
		},
		component: () => import('../../../../src/views/sys/custom/cjson/list.vue'),
	},
]
