var index = {}

export default [

	{
		path: '/sys/account-third/list',
		meta: {
			title: '合作方管理',
		},
		component: () => import('../../../views/sys/accountthird/list.vue'),
	}

]
