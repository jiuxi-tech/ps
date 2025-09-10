// rsbuild - https://rsbuild.dev/zh/index
import path from 'path'
import {defineConfig, loadEnv} from '@rsbuild/core';
import {pluginVue2} from '@rsbuild/plugin-vue2';
import {pluginLess} from "@rsbuild/plugin-less";
import {pluginBabel} from "@rsbuild/plugin-babel";
import vueLegacy from 'rsbuild-plugin-vue-legacy'

const {publicVars} = loadEnv({prefixes: ["VUE_APP_"]});

function parseAlias(pages, __dirname) {
	let alias = {};

	// 添加 @fb 别名，指向项目根目录下的 @fb 文件夹
	alias['@fb'] = path.resolve(__dirname, '@fb');

	// 根据环境添加 Vue 别名
	// 开发环境：使用完整版本（包含模板编译器）
	// 生产环境：使用运行时版本（体积更小）
	if (process.env.NODE_ENV === 'development') {
		alias['vue$'] = 'vue/dist/vue.esm.js';
	} else {
		alias['vue$'] = 'vue/dist/vue.runtime.esm.js';
	}

	// 根据入口页面，定义不同的别名
	Object.keys(pages).forEach(page => {
		if (page === "src") {
			alias['@'] = path.resolve(__dirname, 'src');
			alias['@src'] = path.resolve(__dirname, 'src');
		} else {
			alias['@' + page] = path.resolve(__dirname, page);

		}
	})

	console.info("alias:", JSON.stringify(alias, null, 2));
	return alias;
}

function parsePages(pages) {
	let obj = {
		entry: {},
		template: {},
		title: {}
	}
	Object.keys(pages).forEach(key => {
		let pageKey = key === 'src' ? 'index' : key
		obj.entry[pageKey] = pages[key].entry.indexOf('./') === 0 ? pages[key].entry : `./${pages[key].entry}`
		obj.template[pageKey] = pages[key].template.indexOf('./') === 0 ? pages[key].template : `./${pages[key].template}`
		obj.title[pageKey] = pages[key].title
	});
	return obj
}

function parseProxy(services) {
	let proxy = {};
	services.forEach((service) => {
		if (!service.target) return // 运行失败 容错

		let p = Object.assign({}, {
			changeOrigin: true,
			target: 'http://localhost:8080/',
			// 使用onProxyReq回调来动态设置请求头，确保后端能获取到真实客户端IP
			onProxyReq: (proxyReq, req, res) => {
				// 获取真实客户端IP
				const realIp = req.headers['x-forwarded-for'] || 
							   req.connection.remoteAddress || 
							   req.socket.remoteAddress ||
							   (req.connection.socket ? req.connection.socket.remoteAddress : null);
				
				// 设置转发头信息
				if (realIp) {
					proxyReq.setHeader('X-Forwarded-For', realIp);
					proxyReq.setHeader('X-Real-IP', realIp);
				}
				proxyReq.setHeader('X-Forwarded-Proto', req.protocol || 'http');
				proxyReq.setHeader('X-Forwarded-Host', req.headers.host || 'localhost:10801');
			}
		}, service)

		if (p.target.indexOf('ws://') != -1) {
			p.ws = true
		}
		p.method = null

		proxy[`${p.baseURL.indexOf('/') === 0 ? p.baseURL : '/' + p.baseURL}`] = p
	})

	console.log("proxy:", JSON.stringify(proxy, null, 2))
	return proxy
}


/**
 * RsDefineConfig 配置文件生产器
 * @param __dirname 根目录dir路径
 * @param pkg 根目录package.json
 * @param projectConfig 项目全局配置文件
 * @param output 输出配置
 * @param plugins 插件
 * @param option 配置解构覆盖
 * **/
export function RsDefineConfig(__dirname, pkg, projectConfig = {}, output = {}, plugins = [], option = []) {
	let pages = parsePages(projectConfig.pages)
	let alias = parseAlias(projectConfig.pages, __dirname)
	return defineConfig({
		output: Object.assign({
			assetPrefix: "./", // 打包后相对路径
			injectStyles: true,
		}, output),
		performance: {
			chunkSplit: {
				strategy: "split-by-experience",
			},
			removeConsole: true,
			removeMomentLocale: true,
		},
		plugins: [
			pluginVue2(),
			vueLegacy(), // 支持vue2.7一下的版本，rspack 默认支持2.7+
			pluginBabel({
				include: /\.(?:jsx|tsx)$/,
			}),
			pluginLess({
				lessLoaderOptions: {
					lessOptions: {
						javascriptEnabled: true,
						math: "always",
					},
				},
			}),
			...plugins,
		],
		html: {
			template({entryName}) {
				const templates = {
					...pages.template
				};
				return templates[entryName] || './public/index.html'
			},
			title({entryName}) {
				const templates = {
					...pages.title
				};
				return templates[entryName] || 'TX_Page'
			},
		},
		source: {
			// 指定入口文件
			entry: {
				...pages.entry
				// index: './src/index.js',
				// screen: {
				// 	import: './screen/index.js'
				// },
			},
			define: {
				...publicVars,
				'process.env.VUE_APP_URL': JSON.stringify(process.env.VUE_APP_URL),
				'process.env.VUE_APP_LOG_URL': JSON.stringify(process.env.VUE_APP_LOG_URL),
				'process.env.VUE_APP_REPORT_URL': JSON.stringify(process.env.VUE_APP_REPORT_URL),
				'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV),
			},
		},
		tools: {
			// bundlerChain === chainWebpack
			bundlerChain: (config, { isDev }) => {
				if (isDev) {
					config.devtool = 'eval-cheap-source-map';
				}
				return config;
			},
			rspack: {
				// 这边会影响打包
				// devtool: "source-map",
				resolve: {
					extensions: [".vue", ".js", ".jsx", ".tsx", ".ts", ".json"],
					alias: {
						...alias,
						// "@": path.resolve(__dirname, "src"),
					},
				},
			},
			// 编译 less 版本不匹配警告问题：临时方案
			lightningcssLoader: false,
		},
		server: {
			port: 10801,
			https: projectConfig.server.https,
			hmr: true,
			liveReload: true,
			proxy: {
				...parseProxy(projectConfig.service)
				// "/chemicalpark-manage": {
				// 	target: 'http://10.10.0.86:8080/',
				// 	ws: false,
				// 	changeOrigin: true,
				// 	// pathRewrite: {
				// 	// 	"^/api": "", // 需要rewrite的,
				// 	// },
				// },
			},
		},
		...option
	})
}

