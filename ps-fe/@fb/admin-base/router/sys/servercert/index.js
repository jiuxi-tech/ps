/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/sys/server-cert/list',
		meta: {
			title: '服务器证书管理',
		},
		component: () => import('../../../views/sys/server-cert/list.vue'),
	}
]