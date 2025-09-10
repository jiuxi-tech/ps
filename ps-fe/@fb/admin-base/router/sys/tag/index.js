/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/sys/tag/list',
		meta: {
			title: '标签管理',
		},
		component: () => import('../../../views/sys/tag/list.vue'),
	}
]