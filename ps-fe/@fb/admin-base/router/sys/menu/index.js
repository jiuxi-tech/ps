/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

	{
		path: '/sys/menu/tree',
		meta: {
			title: '菜单管理',
		},
		component: () => import('../../../views/sys/menu/tree.vue'),
	}

]
