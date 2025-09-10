/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/logger/operate/list',
		meta: {
			title: '菜单管理',
		},
		component: () => import('../../../views/sys/logger/list.vue'),
	}

]
