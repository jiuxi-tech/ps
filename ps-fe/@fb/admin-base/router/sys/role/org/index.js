/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/sys/role/org/list',
		meta: {
			title: '部门管理',
		},
		component: () => import('../../../../views/sys/role/org/list.vue'),
	}
]
