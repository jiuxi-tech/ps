/*!
* 模块路由
* (c) 2020 lincong1987
*/
export default [
	{
		path: '/',
		redirect: '/test'
	},
	{
		// 如果我被组件包话
		// 请以 @fb/xxx/src/views/test 引用
		path: '/test',
		component: () => import('../views/test.vue'),
		children: [
			{
				path: '/test/tp-dialog-test',
				component: () => import('../views/test/tp-dialog-test.vue'),
			}
		]
	},
]
