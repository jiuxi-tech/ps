/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/sys/server-cert/list',
		meta: {
			title: '标签管理',
		},
		component: () => import('../../../views/sys/server-cert/list.vue'),
	}
]