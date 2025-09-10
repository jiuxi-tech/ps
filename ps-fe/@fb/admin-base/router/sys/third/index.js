/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [
	{
		path: '/sys/third/third',
		meta: {
			title: '外部界面',
		},
		component: () => import('../../../views/sys/third/third.vue'),
	},
	{
		path: '/sys/third/iframe',
		meta: {
			title: '外部界面',
		},
		component: () => import('../../../views/sys/third/third.vue'),
	}

]
