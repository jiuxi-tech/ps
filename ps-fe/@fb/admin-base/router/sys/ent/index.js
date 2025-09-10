/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/sys/ent/list',
		meta: {
			title: '企业管理',
		},
		component: () => import('../../../views/sys/ent/list.vue'),
	},


	{
		path: '/sys/ent/ent-admin-list',
		meta: {
			title: '人员管理',
		},
		component: () => import('../../../views/sys/ent/account/list.vue'),
	}

]
