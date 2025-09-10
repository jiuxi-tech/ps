/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/sys/cron/list',
		meta: {
			title: '定时任务管理',
		},
		component: () => import('../../../views/sys/cron/list.vue'),
	}

]
