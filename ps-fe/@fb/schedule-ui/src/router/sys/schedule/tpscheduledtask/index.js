var index = {}

export default [

	{
		path: '/schedule/tpscheduledtask/list',
		meta: {
			title: 'tp_scheduled_task',
		},
		component: () => import('@fb/schedule-ui/src/views/sys/schedule/tpscheduledtask/list.vue'),
	}

]
