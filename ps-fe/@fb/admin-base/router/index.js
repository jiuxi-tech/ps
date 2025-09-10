/*!
* systemRouter
* (c) 2020 lincong1987
*/
import projectConfig from '../../../project.config';
let index = [

	{
		path: '/',
		name: 'SystemApplicationRoot',
		  redirect: '/main'
		 //component: () => import('../views/main/SystemApplicationRoot'),
	},

	{
		path: projectConfig.router.loginPath,// + "?a=a&:error_detail?",
		name: 'login',
		meta: {
			title: '登录',
			// 登录劫持
			// hijack: (userinfo) => {
			// 	console.log(userinfo, this)
			// 	app.$router.push('/sys/index/index')
			// }
		},
		component: () => import('../views/login/SystemLogin2.vue'),
		// 考虑多个登录页 以 /login/**的形式进行挂在
		children: [

			{
				path: 'register',
				meta: {
					title: '注册'
				},
				component: () => import('../views/login/LoginRegiter.vue'),
			},
			{
				path: 'forget-pass',
				meta: {
					title: '忘记密码'
				},
				component: () => import('../views/login/LoginForget.vue'),
			},
			// {
			// 	path: 'forget-pass',
			// 	meta: {
			// 		title: '忘记密码'
			// 	},
			// 	component: () => import('../views/login/LoginForget.vue'),
			// },
		],
	},

	{
		path: '/login2',
		meta: {
			title: '大屏登录',
			rootPath: '/login2',
			copyright: `<p>浙江省xxx</p><p>浙江省xxx</p>`,
			// 登录劫持
			hijack: (userinfo) => {
				console.log(userinfo, this)
				app.$router.push('/sys/index/index')
			}
		},
		component: () => import('../views/login/MidnightBlueLogin.vue'),
		children: [

			{
				path: 'register',
				meta: {
					title: '注册'
				},
				component: () => import('../views/login/LoginRegiter.vue'),
			},
			{
				path: 'forget-pass',
				meta: {
					title: '忘记密码'
				},
				component: () => import('../views/login/LoginForget.vue'),
			},
		],
	},

	{
		path: '/sso/main',
		meta: {
			title: '单点登陆缓冲页',
		},
		component: () => import('../views/common/sso/main.vue'),
	},

	{
		path: '/common/document/editor',
		meta: {
			title: '新tabbar',
		},
		component: () => import('../views/common/document/editor.vue'),
	},

	{
		path: projectConfig.router.mainPath,
		name: 'main',
		meta: {
			title: '后台1'
		},
		component: () => import('../views/main/SystemApplicationLayout'),
		children: [

			{
				path: '/refresh',
				name: 'refresh',
				component: () => import('../views/main/common/refresh.vue'),
			},

			{
				path: '404',
				name: 'notFound',
				alias: '/404',
				component: () => import('../views/main/common/NotFound'),
			},

			{
				path: '403',
				name: 'notAllow',
				alias: '/403',
				component: () => import('../views/main/common/NotAllow'),
			},

			{
				path: '/500',
				name: 'exception',
				// alias: '/500', // 如果 path 以斜杠开头, 添加别名会引起 警告
				component: () => import('../views/main/common/Exception'),
			},

		],
	},

	{
		path: '*',
		name: 'UnknowPage',
		component: () => import('../views/main/common/UnknowPage'),
	},
]

export default index
