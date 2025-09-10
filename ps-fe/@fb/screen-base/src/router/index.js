/**
 * index
 * (c) 2021 lincong1987
 */

export default [
	{
		path: '/',
		redirect: '/index'
	},
    {
        path: '*',
        meta: {
            title: '404',
        },
        component: {
            template: `<div>页面不存在</div>`,
        },
    },
	{
		path: '/index',
		component: () => import('../views/index.vue'),
	},
	{
		path: '/test',
		component: () => import('../views/test.vue'),
	},

	// 温岭留存
	{
		path: '/monitor-warn/wenling/zhfxts',
		component: () => import('../views/monitor-warn/wenling/zhfxts.vue'),
	},
	{
		path: '/monitor-warn/wenling/znjcyj',
		component: () => import('../views/monitor-warn/wenling/znjcyj.vue'),
	},
	{
		path: '/monitor-warn/wenling/znjcyj-ent',
		component: () => import('../views/monitor-warn/wenling/znjcyj-ent.vue'),
	},

	// 主题测试页
	{
		path: '/theme/midnight-blue',
		component: () => import('../views/theme/midnight-blue.vue'),
	},
	{
		path: '/theme/dark-blue',
		component: () => import('../views/theme/DarkBlue.vue'),
	},
	{
		path: '/theme/sky-blue',
		component: () => import('../views/theme/SkyBlue.vue'),
	},
	{
		path: '/theme/royal-blue',
		component: () => import('../views/theme/RoyalBlue.vue'),
	},
	{
		path: '/theme/navy-blue',
		component: () => import('../views/theme/NavyBlue.vue'),
	},
	{
		path: '/theme/cornflower-blue',
		component: () => import('../views/theme/CornflowerBlue.vue'),
	},

	{
		path: '/test1',
		component: () => import('../views/text1.vue'),
	}

    // ...initRouter()
]
