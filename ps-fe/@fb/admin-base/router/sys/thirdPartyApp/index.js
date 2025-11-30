/*!
* index
* (c) 2024
*/

var index = {}

export default [

	{
		path: '/sys/third-party-app/list',
		meta: {
			title: '第三方应用管理',
		},
		component: () => import('../../../views/sys/third-party-app/list.vue'),
	}
]
