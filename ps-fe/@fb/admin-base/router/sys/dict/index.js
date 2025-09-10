/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/sys/dict/list',
		meta: {
			title: '字典管理',
		},
		component: () => import('../../../views/sys/dict/list.vue'),
	}

]
