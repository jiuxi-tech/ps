/*!
* systemRouter
* (c) 2020 lincong1987
*/

 
 

let index = [

	
	// SSO回调路由
	{
		path: '/sso/login',
		meta: {
			title: 'SSO登录回调',
		},
		component: () => {
			
			return import('@/views/sso/login.vue')
		},
	},
	 
	 
]

export default index
