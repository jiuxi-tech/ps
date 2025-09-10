var index = {}

export default [

	{
		path: '/sys/monitor/list',
		meta: {
			title: 'tp_monitor_client',
		},
		component: () => import('@fb/monitor-ui/src/views/sys/monitor/list.vue'),
	}

]
