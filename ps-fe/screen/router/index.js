/*!
 * index
 * (c) 2021 lincong1987
 */
export default [

	{
		path: '/',
		redirect: '/temp1',
	},

	{
		path: '/index',
		meta: {
			title: '浙江省概况',
		},
		component: (a) => {
			return import('../views/index.vue')
		},
	},

	{
		path: '/temp1',
		meta: {
			title: '浙江省概况',
		},
		component: (a) => {
			return import('../views/index.vue')
		},
	},

	{
		path: '*',
		meta: {
			title: '404',
		},
		component: {
			template: `
				<div>页面不存在</div>
			`,
		},
	},

]
