/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/sys/index/index',
		meta: {
			title: '字典管理',
		},
		name: 'index',
		component: () => import('../../../views/sys/index/index-old.vue'),
	}, {
		path: '/sys/index/index2',
		meta: {},
		name: 'index2',
		component: () => import('../../../views/sys/index/index2.vue'),
	}, {
		path: '/sys/index/index3',
		name: 'index3',
		meta: {},
		component: () => import('../../../views/sys/index/index3.vue'),
	}, {
		path: '/sys/index/index-m',
		name: 'index-m',
		meta: {},
		component: () => import('../../../views/sys/index/index-m.vue'),
	}


]
