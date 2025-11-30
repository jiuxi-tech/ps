/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/sys/api-call-log/list',
		meta: {
			title: 'API调用日志',
		},
		component: () => import('../../../views/sys/api-call-log/list.vue'),
	}
]
