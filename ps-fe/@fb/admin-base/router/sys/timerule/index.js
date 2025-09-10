/*!
* timeRule router
* 时间段权限规则路由配置
*/

var index = {}

export default [

	{
		path: '/sys/timerule/list',
		meta: {
			title: '时间段权限规则',
		},
		component: () => import('../../../views/sys/timerule/list.vue'),
	},
	// {
	// 	path: '/sys/timeRule/form',
	// 	meta: {
	// 		title: '新增/编辑时间段权限规则',
	// 	},
	// 	component: () => import('../../../views/sys/timeRule/form.vue'),
	// }

]