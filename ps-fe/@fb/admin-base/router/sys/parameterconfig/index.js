var index = {}

export default [

	{
		path: '/sys/parameterconfig/list',
		meta: {
			title: 'tp_parameter_config',
		},
		component: () => import('../../../views/sys/parameterconfig/list.vue'),
	}

]
