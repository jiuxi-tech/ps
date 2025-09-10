var index = {}

export default [

	{
		path: '/sys/tpoperatelog/list',
		meta: {
			title: 'tp_operate_log',
		},
		component: () => import('../../../views/sys/tpoperatelog/list.vue'),
	}

]
