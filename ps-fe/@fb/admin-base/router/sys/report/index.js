/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [
	{
		path: '/fine/report/tree',
		meta: {
			title: '帆软报表文件上传',
		},
		component: () => import('../../../views/sys/report/tree.vue'),
	}

]
