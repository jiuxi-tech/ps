const path = require('path')
const fs = require('fs-extra');


let PROJECT_DIR = null;
let modules = null;
let FbAssets = null;

// 兼容 node.v15 下replaceAll
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}

function resolve(dir) {
	return path.join(PROJECT_DIR, dir)
}


function getPages(config) {
	return config.pages || {
		src: {
			template: 'public/index.html', // 在 dist/index.html 的输出
			filename: 'index.html', // 当使用 title 选项时，
		}
	};
}

function findModules(pkg, config) {
	if (modules == null) {
		modules = {};

		// 生产环境依赖
		getMoules(pkg.dependencies, pkg.modules, modules);
		// 开发环境依赖
		getMoules(pkg.devDependencies, pkg.modules, modules);
		// 本地环境依赖
		getMoules(pkg.linkDependencies, pkg.modules, modules);
	}

	function getMoules(dependencies, cfg, modules) {
		if (dependencies) {
			Object.keys(dependencies).forEach(name => {
				// 加载页面模块
				if (cfg) {
					cfg.forEach((n, i) => {
						if (!modules[name] && name.startsWith(n)) {
							modules[name] = dependencies[name];
						}
					})
				} else {
					if (!modules[name]) {
						modules[name] = dependencies[name];
					}
				}
			})
		}
	}

	return modules;
}


let alias = null;
const parseAlias = function (pkg, config) {
	if (alias == null) {
		alias = {};

		// 根据入口页面，定义不同的别名
		Object.keys(getPages(config)).forEach(page => {
			if (page === "src") {
				alias['@'] = resolve('src');
			} else {
				alias['@' + page] = resolve(page);

			}
		})

		// 组件包别名填充  方便组件包单独开发使用
		if (pkg.name && pkg.name.indexOf('@fb/') >= 0) {
			alias[`${pkg.name}`] = resolve('./')
			alias[`${pkg.name}/src`] = resolve('src')
		}

		console.info("alias:", JSON.stringify(alias,null,2));
	}
	return alias;
}


let proxy = null;
const parseProxy = function (config) {
	if (proxy == null) {
		proxy = {};
		(config.service || []).forEach((service) => {
			if (!service.target) return // 运行失败 容错

			let p = Object.assign({}, {
				changeOrigin: true,
				target: 'http://localhost:8080/',
				bypass: (req, res, proxyConfig) => {
					const host = req.headers && req.headers.host
					console.log(`${dayjs()} [${req.method}] ${res.statusCode} ${host}${req.url}`)
				},
			}, service)

			if (p.target.indexOf('ws://') != -1) {
				p.ws = true
			}

			//p.name = null
			p.method = null
			//p.timeout = null
			//p.withCredentials = null

			proxy[`${p.baseURL.indexOf('/') === 0 ? p.baseURL : '/' + p.baseURL}`] = p
		})

		console.log("proxy:", JSON.stringify(proxy,null,2))
	}
	return proxy
}

/**
 *
 * pages:{
 *
 * 	index: {
 * 		// page 的入口
 * 		entry: 'src/index.js', // 模板来源
 * 		template: 'node_modules/@fb/login/public/index.html', // 在 dist/index.html 的输出
 * 		filename: 'index.html', // 当使用 title 选项时，
 * 		// template 中的 title 标签需要是 <title><%= htmlWebpackPlugin.options.title %></title>
 * 		title: fbConfig.title + '-管理入口', // 在这个页面中包含的块，默认情况下会包含
 * 		// 提取出来的通用 chunk 和 vendor chunk。
 * 		// chunks: ['chunk-vendors', 'chunk-common', 'index'],
 * 	},
 * 	screen: {
 * 		// page 的入口
 * 		entry: 'screen/index.js', // 模板来源
 * 		template: 'public/screen.html', // 在 dist/index.html 的输出
 * 		filename: 'screen.html', // 当使用 title 选项时，
 * 		// template 中的 title 标签需要是 <title><%= htmlWebpackPlugin.options.title %></title>
 * 		title: fbConfig.title + '-大屏入口', // 在这个页面中包含的块，默认情况下会包含
 * 		// 提取出来的通用 chunk 和 vendor chunk。
 * 		// chunks: ['chunk-vendors', 'chunk-common', 'index'],
 * 	},
 * },
 * @param config
 * @returns {{}}
 */
let pages = null;

function parsePages(config) {
	if (pages == null) {
		// 页面入口
		let defaults = getPages(config);

		pages = {}
		Object.keys(defaults).forEach(page => {
			pages[page] = Object.assign({
				// page 的入口
				entry: page + '/index.js', // 模板来源
				filename: page + '.html', // 当使用 title 选项时，
				title: config.title, // 在这个页面中包含的块，默认情况下会包含
				// 提取出来的通用 chunk 和 vendor chunk。
				// chunks: ['chunk-vendors', 'chunk-common', 'index'],
			}, defaults[page])
		});
		console.info("pages:", JSON.stringify(pages,null,2));
	}
	return pages;
}

function parseCopy(pkg, config) {
	let m = findModules(pkg, config);

	let r = []
	Object.keys(m).forEach((n, i) => {
		// 判断路径是否存在
		let path = ''

		if (m[n] === 'local:') {
			path = n + '/public'
		} else {
			path = 'node_modules/' + n + '/public'
		}
		// 复制目录
		if (fs.pathExistsSync(path)) {
			r.push({from: path});
		}
	})

	// 从静态资源库中复制
	if (!FbAssets) return r;

	// 22-07-04 根据页面，自動加載靜態資源文件
	let assets = []
	Object.keys(pages).forEach(page => {
		assets = assets.concat(pages[page].config.assets || [])
	})

	if (assets.length > 0) {
		Object.keys(FbAssets).forEach(key => {
			// 识别加载的资源類型
			let asset = null
			assets.forEach(item => {
				if (typeof item === 'string' && item === key) {
					asset = FbAssets[key]
				} else if (typeof item === "object" && item.name === key) {
					asset = FbAssets[key]
				}
			})

			if (asset) {
				// 判断路径是否存在
				let from = '@fb/fb-assets/src/' + asset.path
				let to = asset.path

				// 复制目录
				if (fs.pathExistsSync(from)) {
					r.push({from: from, to: to});
				}
			}
		})
	}

	return r;
}


let plugins = null;

function getPlugin(pkg, config) {
	if (plugins == null) {
		plugins = [];
		// 多模块下使用
		if ((pkg.modules || []).length > 0) {
			// 复制模块下的静态文件
			const copys = parseCopy(pkg, config);
			console.info("copys:", JSON.stringify(copys,null,2));

			if (copys.length > 0) {
				try {
					const CopyWebpackPlugin = require('copy-webpack-plugin')
					plugins.push(new CopyWebpackPlugin({patterns:copys}))
				}catch (e){
					console.error(e)
					console.error('> copy-webpack-plugin 加载失败，请先安装: fb i --dev copy-webpack-plugin@6.4.1')
					return plugins;
				}
			}
		}
	}
	return plugins;
}

function hasMultipleCores() {
	try {
		return require('os').cpus().length > 1
	} catch (e) {
		return false
	}
}


const dayjs = require('dayjs')

var VueConfig = function (path, pkg, fbconfig, config = {}) {
	PROJECT_DIR = path
	try {
		FbAssets = require(resolve('@fb/fb-assets/src/index.js'))
	}catch (e){
	}

	return Object.assign({
		// 选项...

		// project deployment base
		publicPath: './',

		// where to output built files
		outputDir: 'dist',

		// where to put static assets (js/css/img/font/...)
		assetsDir: '',

		// filename for index.html (relative to outputDir)
		// indexPath: 'index.html',
		// indexPath: './dev/index.js',

		// whether filename will contain hash part
		filenameHashing: true,

		// boolean, use full build?
		runtimeCompiler: true,

		// deps to transpile
		transpileDependencies: [
			/* string or regex */
			"fb-core",
			"fb-ui",
			"fb-third",
		],

		// sourceMap for production build?
		productionSourceMap: false,// !process.env.VUE_CLI_TEST,

		// use thread-loader for babel & TS in production build
		// enabled by default if the machine has more than 1 cores
		parallel: hasMultipleCores(),

		// multi-page config
		pages: parsePages(fbconfig),
		integrity: false,

		css: {
			extract: {ignoreOrder: true},		// 关闭CSS加载顺序冲突告警
		},

		lintOnSave: 'default',

		devServer: Object.assign({}, {
			open: process.platform === 'darwin',
			host: 'localhost',
			port: 10801,
			https: false,
			hotOnly: false, // string | Object
			proxy: parseProxy(fbconfig),
			before: app => {
				console.log('before devServer start')
				//console.log(app)
			},
		}, fbconfig.server || {}),
		pluginOptions: {
			'style-resources-loader': {
				preProcessor: 'less',
				patterns: [],
			}
		},

		configureWebpack: {
			plugins: getPlugin(pkg, fbconfig),
			resolve: {
				alias: parseAlias(pkg, fbconfig)
			}
		},
		chainWebpack(config) {
			if (!FbAssets) return;

			// 22-07-04 根据页面，自動加載靜態資源文件
			Object.keys(pages).forEach(page => {
				config.plugin('html-' + page)
					.tap(args => {
						// 从 页面入口配置中，获取要加载的静态资源信息
						let assets = args[0].config.assets || []
						if (assets.length > 0) {
							let plugins_head = [];

							assets.forEach(key => {
								// 识别加载的资源類型
								let asset = null
								if (typeof key === 'string' && FbAssets[key]) {
									asset = FbAssets[key]
								} else if (typeof key === "object" && key.name && FbAssets[key.name]) {
									asset = FbAssets[key.name]
								}

								// 找到要加载的资源了
								if (asset) {
									// console.info("asset ", asset)
									if (asset.head) {
										if (asset.head.css) {
											asset.head.css.forEach(url => {
												if (!url.includes("//")) {
													if (url.startsWith("/")) url = url.substr(1);
													url = "{BASE_URL}" + url
												}
												plugins_head.push('<link href=\"' + url + '\" rel=\"stylesheet\"/>')
											})
										}
										if (asset.head.script) {
											asset.head.script.forEach(url => {
												if (!url.includes("//")) {
													if (url.startsWith("/")) url = url.substr(1);
													url = "{BASE_URL}" + url
												}
												plugins_head.push('<script src=\"' + url + '\" type=\"text/javascript\"><\\\/script>')
											})
										}
									}
								}
							})

							// 通過 document.write 進行輸出
							// {BASE_URL} 转换成 变量
							for (let i = 0; i < plugins_head.length; i++) {
								plugins_head[i] = "document.write(\'{SCRIPT}\')"
									.replaceAll(/{SCRIPT}/g, plugins_head[i].replaceAll(/{BASE_URL}/g, "\'+ BASE_URL +\'"))
							}

							// 外面包一层 <script></script>
							args[0].plugins_head = plugins_head.join('\n\t\t')
						}

						return args
					})
			})
		},
	}, config);
}

module.exports = {
	VueConfig
}

// export default VueConfig
