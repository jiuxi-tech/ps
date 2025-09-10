/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/sys/person/org/list',
		meta: {
			title: '人员管理',
		},
		component: () => import('../../../views/sys/person/org/list.vue'),
	},{
		path: '/sys/person/ent/list',
		meta: {
			title: '人员管理',
		},
		component: () => import('../../../views/sys/person/ent/list.vue'),
	}

]
