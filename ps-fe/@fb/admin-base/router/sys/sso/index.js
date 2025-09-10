/*!
* sso router
* (c) 2024
*/

var index = {}

export default [

	{
		path: '/sys/sso/session/list',
		meta: {
			title: '全局会话管理',
		},
		component: () => import('../../../views/sys/sso/session/list.vue'),
	},
	{
		path: '/sys/sso/client/list',
		meta: {
			title: '客户端管理',
		},
		component: () => import('../../../views/sys/sso/client/list.vue'),
	},
	{
		path: '/sys/sso/log/list',
		meta: {
			title: '应用日志管理',
		},
		component: () => import('../../../views/sys/sso/log/index.vue'),
	},
]