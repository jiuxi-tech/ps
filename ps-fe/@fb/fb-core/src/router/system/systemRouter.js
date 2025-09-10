/*!
* systemRouter
* (c) 2020 lincong1987
*/

let systemRouter = [

	{
		path: '/',
		name: 'SystemApplicationRoot',
		component: () => import('../../views/SystemApplicationRoot'),
	},

	{
		path: '/login',
		name: 'login',
		meta: {
			title: '登录',
		},
		component: () => import('../../views/SystemApplicationLogin'),
	},
	{
		path: '/login1',
		name: 'login1',
		meta: {
			title: '登录1',
		},
		component: () => import('../../views/login/SystemApplicationLogin'),
	},

	{
		path: '/main1',
		name: 'main1',
		component: () => import('../../views/SystemApplicationLayout'),
		children: [
			{
				path: '404',
				name: 'notFound',
				alias: '/404',
				component: () => import('../../views/common/NotFound'),
			},

			{
				path: '403',
				name: 'notAllow',
				alias: '/403',
				component: () => import('../../views/common/NotAllow'),
			},

			{
				path: '/500',
				name: 'exception',
				alias: '/500',
				component: () => import('../../views/common/Exception'),
			},

		],
	},
	{
		path: '*',
		name: 'UnknowPage',
		component: () => import('../../views/common/UnknowPage'),
	},
]

export default systemRouter
