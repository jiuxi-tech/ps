var index = {}

export default [

	{
		path: '/sys/tpoperatelog/list',
		meta: {
			title: 'tp_operate_log',
		},
		component: () => import('@fb/log-center-ui/src/views/sys/tpoperatelog/list.vue'),
	}

]
