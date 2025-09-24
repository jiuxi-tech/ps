/*!
* sys router
* (c) 2024
*/

export default [
	{
		path: '/sys/database-backup/list',
		meta: {
			title: '数据库备份管理',
		},
		component: () => import('../../../views/sys/database-backup/list.vue'),
	},
 
 
]