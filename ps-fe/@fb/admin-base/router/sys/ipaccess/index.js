/*!
* ipaccess router
* IP访问控制路由配置
*/

export default [

	{
		path: '/sys/ipaccess/config',
		meta: {
			title: 'IP访问控制配置',
		},
		component: () => import('../../../views/sys/ipaccess/config.vue'),
	},

	{
		path: '/sys/ipaccess/test',
		meta: {
			title: 'IP规则测试',
		},
		component: () => import('../../../views/sys/ipaccess/test.vue'),
	},

	// {
	// 	path: '/sys/ipaccess/logs',
	// 	meta: {
	// 		title: 'IP访问日志',
	// 	},
	// 	component: () => import('../../../views/sys/ipaccess/logs.vue'),
	// },

	// {
	// 	path: '/sys/ipaccess/log-detail',
	// 	meta: {
	// 		title: '访问日志详情',
	// 	},
	// 	component: () => import('../../../views/sys/ipaccess/log-detail.vue'),
	// },

	// {
	// 	path: '/sys/ipaccess/clean-logs',
	// 	meta: {
	// 		title: '清理访问日志',
	// 	},
	// 	component: () => import('../../../views/sys/ipaccess/clean-logs.vue'),
	// }

]