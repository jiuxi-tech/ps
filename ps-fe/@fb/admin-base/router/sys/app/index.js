/*!
* app router
* (c) 2024
*/

var index = {}

export default [

	{
		path: '/sys/app/list',
		meta: {
			title: '应用管理',
		},
		component: () => import('../../../views/sys/app/my.vue'),
	},
 {
		path: '/sys/app/my',
		meta: {
			title: '应用管理',
		},
		component: () => import('../../../views/sys/app/my.vue'),
	},

]