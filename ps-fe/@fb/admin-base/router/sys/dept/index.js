/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/sys/dept/org/tree',
		meta: {
			title: '部门管理',
		},
		component: () => import('../../../views/sys/dept/org/tree.vue'),
	},
	{
		path: '/sys/dept/ent/tree',
		meta: {
			title: '部门管理',
		},
		component: () => import('../../../views/sys/dept/ent/tree.vue'),
	}

]
