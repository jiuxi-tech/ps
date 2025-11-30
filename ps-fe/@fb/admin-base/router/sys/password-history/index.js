/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/sys/password-history/list',
		meta: {
			title: '密码历史',
		},
		component: () => import('../../../views/sys/password-history/list.vue'),
	}
]