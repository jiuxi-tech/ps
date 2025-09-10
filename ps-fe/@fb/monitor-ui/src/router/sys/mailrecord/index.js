var index = {}

export default [

	{
		path: '/sys/mail-record/list',
		meta: {
			title: 'tp_mailrecord_client',
		},
		component: () => import('@fb/monitor-ui/src/views/sys/mailrecord/list.vue'),
	}

]
