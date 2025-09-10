/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [
	{
		path: '/sys/role/ent/list',
		meta: {
			title: '部门管理',
		},
		component: () => import('../../../../views/sys/role/ent/list.vue'),
	}

]
