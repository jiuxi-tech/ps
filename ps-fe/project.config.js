/*!
 * project
 * (c) 2020 lincong1987
 */
module.exports = {

	// 系统名称
	name: 'ps-bmp-admin',

	// 主题
	theme: 'pc3',
	TX_THEME: "sea_blue",

	//logoTitle: "省委党校业务中台管理系统",
	logoTitle: "JPX3.0",

	// 登录页相关配置 -- 后期可能成为组件包的配置
	login: {
		title: '',
		copyright: 'Copyright © 2001-2025 JPX3.0 All Rights Reserved.',
		//copyright: 'Copyright © 2001-2025 省委党校业务中台管理系统 All Rights Reserved.'
	},
	// 浏览器title
	title: "JPX3.0",

	// token or cookies
	auth: 'token',

	// token 配置
	token: {
		// 类型 jwt common
		type: 'common',
		time: 1000,
		path: '/getToken',
	},

	server: {
		host: '0.0.0.0',
		port: 10801,
		https: false,
		hmr: true
	},

	// 将会自动创建 vueInstance.[name] 服务， 全局 app.$svc.[name]
	service: [
		{
			name: 'service',
			target: process.env.VUE_APP_URL,
			baseURL: '/ps-be',
			mock: 'http://192.168.0.46:3000/api/open/plugin/export-full?type=json&pid=11&status=all&token=c305cf2dbd1f7fb9deb6769886ea5446862e875ced1a3829b0cbb5580c2e4873',
			method: 'post',
			timeout: 10000,
			withCredentials: true,
		},
		 
		// 日志服务
		// {
		// 	name: 'logService',
		// 	target: process.env.VUE_APP_LOG_URL,
		// 	baseURL: '/log-center',
		// 	mock: '',
		// 	method: 'post',
		// 	timeout: 10000,
		// 	withCredentials: true,
		// },
		// 报表服务
		// {
		// 	name: 'thirdService',
		// 	target: process.env.VUE_APP_REPORT_URL,
		// 	baseURL: '/report',
		// 	mock: '',
		// 	method: 'get',
		// 	timeout: 10000,
		// 	withCredentials: true,
		// },
	],

	sysconfig: {
		// 登陆账号是否加密
		passwordEncryption: true,
		// 非对称加密公钥
		publicKey: '04be3415fd3a7231fa23e4cfdf6f857b0f3137e75692f7b5011d459afc0cdd7741676dca32ca6489cfe0f0fd43b5e9f9f0f77c7997630ba1142c725178a9181558',
		// 认证类型 token / cookie
		authType: "token",
		// 系统的fb-row的默认间距
		gutter: 8,
		// 日志收集开关
		loggerSwitch: true,
		// 帆软报表开关
		reportSwitch: false,
	},

	// multi-page config
	pages: {
		src: {
			// page 的入口
			entry: 'src/main.js',
			// 模板来源
			template: 'public/index.html',
			// 在 dist/index.html 的输出
			filename: 'index.html',
			// 当使用 title 选项时，
			// template 中的 title 标签需要是 <title><%= htmlWebpackPlugin.options.title %></title>
			//title: "省委党校业务中台管理系统",
			title: "JPX3.0",
			// 在这个页面中包含的块，默认情况下会包含
			// 提取出来的通用 chunk 和 vendor chunk。
			// chunks: ['chunk-vendors', 'chunk-common', 'index'],
			config: {
				theme: 'classic_blue',
				"assets": [
					"pdf",
					"leaflet",
					"loading"
				]
			}
		},
		// screen: {
		// 	// page 的入口
		// 	entry: 'screen/main.js',
		// 	// 模板来源
		// 	template: 'public/screen.html',
		// 	// 在 dist/index.html 的输出
		// 	filename: 'screen.html',
		// 	// 当使用 title 选项时，
		// 	// template 中的 title 标签需要是 <title><%= htmlWebpackPlugin.options.title %></title>
		// 	title: "数字大屏", // 在这个页面中包含的块，默认情况下会包含
		// 	chunks: ['chunk-vendors', 'chunk-common', 'screen'],
		// 	config: {
		// 		container: '#app',
		// 		width: 1920,
		// 		height: 1080,
		// 		theme: 'royal-blue',
		// 		"assets": [
		// 			"leaflet",
		// 			"loading"
		// 		]
		// 	}
		// }
	},
	//地图中心点位
	centerPoint: [120.20249, 30.23999],

	// 路由配置
	router: {

		// 启用进度条
		progress: true,
		// 无 token 跳转路由配置白名单, 支持*匹配 '/sys/*'
		whiteList: ['/login/*', '/register', '/sso/main','/sso/login'],
		/*!
		 * 启动模式 startMode
		 * 手动 manual 需要 indexPath
		 * 半自动白名单 semiAutoWhite 需要 indexPath
		 * 半自动 semiAuto 需要 indexPath
		 * 框架自带 **
		 */
		startMode: 'semiAuto',
		// 主路由配置 router文件夹下 相对全路径
		indexPath: './index.js',
		// 一级路由 path 路径，判断 children 追加到那个路由下
		mainPath: '/main',
		// 登录页面
		loginPath: '/login',
		// 后台页面路径使用 window.open 方式跳转
		adminPath: 'index.html'
	},
	// console 日志
	logger: {
		enabled: true,
		name: 'main',
		pattern: '%d{yyyy-MM-dd HH:mm:ss,SSS} [%c] %-5p - %m{1}%n',
		show: false,
	},
}
