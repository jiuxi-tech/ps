/*!
* routerTool
* (c) 2020 lincong1987
*/

import {find, startsWith, each, isArray, isObject, mergeWith, unionBy, flattenDeep} from 'lodash-es'

/**
 * 智能路由
 * 1、路由的路径是多个“/”组成的字符串，使用“/”分割后得到不同的子项
 * 2、计算子项个数，用个数乘以4，计入得分
 * 3、判断子项是否是静态的，即不包含“：”、“*”等特殊字符串，若是计入3分。
 * 4、判断子项是否是动态的，即包含“：”特殊字符，若是计入2分。
 * 5、判断子项是否是模糊匹配，即包含“*”特殊字符，若是扣除1分。
 * 6、判断子项是否是根端，即只是“/”，若是计入1分。

 * @param {*} routes
 */
export const autoRouter = function (routes) {
	routes.forEach((item) => {
		if (!item.path) {
			item.path = '/router-wrong-path'
			console.log(`此路由配置错误，请检查 ${JSON.stringify(item)}`)
			return
		}
		const path = item.path
		let arr = path.split('/')
		if (arr[0] === '') {
			arr = arr.slice(1)
		}
		let count = 0
		arr.forEach((sonPath) => {
			count += 4
			if (sonPath.indexOf(':') !== -1) {
				count += 2
			} else if (sonPath.indexOf('*') !== -1) {
				count -= 1
			} else if (sonPath === '') {
				count += 1
			} else {
				count += 3
			}
		})
		item.count = count
		if (item.children && item.children.length) {
			autoRouter(item.children)
		}
	})
	routes = routes.sort((a, b) => b.count - a.count)
	return routes
}

/**
 * 注入
 * @param mainRouter
 * @param child
 */
export function injectRouter(mainRouter, child) {

	if (!child.path) {
		return
	}
	// 干掉 别名使用 path = alias 控制台警告
	// if (!child.alias) {
	// 	if (startsWith(child.path, '/')) {
	// 		child.alias = child.path
	// 	} else {
	// 		child.alias = `/${child.path}`
	// 	}
	//
	// }

	mainRouter.children.push(child)
}

export function handle(systemRouter, files) {
	if (!isArray(files)) {
		files = [files];
	}
	files = flattenDeep(files)
	let routes = systemRouter || []

	for (let i = 0; i < files.length; i++) {
		let mappedRouters = files[i];
		console.log("里有", mappedRouters.keys().length)

		let mainRouter = find(systemRouter, r => {
			return r.path === '/main1'
		})

		mainRouter.children = mainRouter.children || []

		mappedRouters.keys().forEach(key => {
			let mappedRouter = mappedRouters(key).default
			if (isArray(mappedRouter)) {
				// if (mappedRouter[0] && mappedRouter[0].name && mappedRouter[0].name === 'SystemApplicationRoot') return
				each(mappedRouter, (r) => {
					injectRouter(mainRouter, r)
				})
			} else {
				injectRouter(mainRouter, mappedRouter)
			}

		})
	}

	routes = autoRouter(routes)

	return routes
}

export function initRoutesWhite(indexPath, files) {
	if (!isArray(files)) {
		files = [files];
	}
	files = flattenDeep(files)
	let routes = []

	for (let i = 0; i < files.length; i++) {
		let mappedRouters = files[i];
		let routerKeys = mappedRouters.keys()

		for (let i = 0; i < routerKeys.length; i++) {
			let key = routerKeys[i]
			// 取出 主路由 配置项
			if (key.indexOf(indexPath) !== -1) {
				routes = mappedRouters(key).default
				routerKeys.splice(i, 1)
				break;
			}
		}
		console.log("里有", routerKeys)

		if (routes.length > 0) {
			filterRoutesWhiteList(mappedRouters, routes)
		}
	}

	// 过滤注入白名单
	function filterRoutesWhiteList(mappedRouters, arr) {
		let routerKeys = mappedRouters.keys()

		for (let i = 0; i < arr.length; i++) {
			let rItem = arr[i]
			if (rItem.meta && rItem.meta.whiteList) {
				let whiteList = rItem.meta.whiteList
				// 主路由 配置项是否追加 children
				rItem.children = rItem.children || []

				each(routerKeys, key => {
					let mappedRouter = mappedRouters(key).default
					if (isArray(mappedRouter) &&
						isArray(whiteList) &&
						arrIncludes(whiteList, key)
					) {
						each(mappedRouter, (r) => {
							if (rItem.path === r.path) {
								// 尽量不处理 文件自己 所在路径
								// 如果是文件自己不进行递归
								return
							}
							injectRouter(rItem, r)
							if (r.meta && r.meta.whiteList) {
								// 递归追加白名单
								filterRoutesWhiteList(mappedRouter)
							}
						})
					} else {
						// 为对象时

						if (rItem.path.indexOf(routerJsToIf(key)) > 0) {
							// 尽量不处理 文件自己 所在路径
							// 如果是文件自己不进行递归
							return
						}
						injectRouter(rItem, mappedRouter)
						if (mappedRouter.meta && mappedRouter.meta.whiteList) {
							// 递归追加白名单
							filterRoutesWhiteList(mappedRouter)
						}
					}

				})
			}
		}

	}

	// 路由js文件 转换为判断跳出递归条件
	function routerJsToIf(routerPath) {
		let path = routerPath.replace(/.\//, '')
		path = path.replace(/\/index.js/, '')
		return path
	}

	function arrIncludes(list, str) {
		for (let i = 0; i < list.length; i++) {
			if (startsWith(str, list[i])) {
				return true
			}
		}
	}

	// 路由器排序
	routes = autoRouter(routes)
	return routes
}

/**
 * 默认路由生成 initRoutes
 * @param constantRoutes 主路由对象
 * @param flatRoutes 平铺路由集合
 * @param mainPath 追加路由路径
 * */
export function initRoutes(constantRoutes, flatRoutes, mainPath) {

	/**
	 * 第 1 步
	 * 截取出 主路由 配置项
	 * */
	let routes = constantRoutes

	/**
	 * 第 2 步
	 * 向主路由配置文件下 mainPath 二级路由追加路由
	 * */
	if (routes.length > 0) {
		filterRoutesList(flatRoutes, routes)
	}

	// 过滤注入
	function filterRoutesList(flatRoutes, arr) {
		routesParentSon(flatRoutes)

		/**
		 * 向主文件 一级路由 追加 子路由
		 * 默认判断 mainPath 生成后台二级路由
		 * */
		for (let i = 0; i < arr.length; i++) {
			let firstRoute = arr[i]
			for (let key in flatRoutes) {
				let route = flatRoutes[key]
				if (
					route.meta && route.meta.parentPath && route.meta.parentPath === firstRoute.path ||
					firstRoute.path === mainPath && !route.meta ||
					firstRoute.path === mainPath && route.meta && !route.meta.parentPath
				) {
					// 主路由 配置项是否追加 children
					firstRoute.children = firstRoute.children || []
					if (firstRoute.path !== route.path) {
						injectRouter(firstRoute, route)
					}
				}
			}
		}

	}

	/**
	 * 生成约定好的父子关系路由
	 * meta.parentPath 作为媒介
	 * */
	function routesParentSon(mappedRoutes, route = {}) {
		for (let i in mappedRoutes) {
			let mappedRoute = mappedRoutes[i]

			// 二级路由互相追加
			if (route.meta && route.meta.parentPath && route.meta.parentPath === mappedRoute.path) {
				mappedRoute.children = mappedRoute.children || []
				mappedRoute.children.push(route)
				let index = mappedRoutes.indexOf(route)
				if (index >= 0) {
					mappedRoutes.splice(index, 1)
				}
				return
			}

			if (route.meta && route.meta.parentPath && route.meta.parentPath !== mappedRoute.path) {
				return
			}

			// 递归生成路由树
			if (mappedRoute.meta && mappedRoute.meta.parentPath) {
				routesParentSon(mappedRoutes, mappedRoute)
			}
		}
	}


	// 路由器排序
	routes = autoRouter(routes)
	return routes
}

/**
 * 平铺路由文件及获取主路由对象 flatFilesToRouteArr
 * @param indexPath 主路由对象文件路径
 * @param files 平铺路由集合
 * */
export function flatFilesToRouteArr(indexPath, files) {
	if (!isArray(files)) {
		files = [files];
	}
	files = flattenDeep(files)
	/**
	 * 第一步
	 * 合并多组文件 形成对象
	 * */
	let filesObj = {}
	for (let i in files) {
		// 文件集合器
		let file = files[i]
		let obj = {}
		let routerKeys = file.keys()
		for (let n in routerKeys) {
			let fileName = routerKeys[n]
			// 文件名 = 文件 default 对象
			obj[fileName] = file(fileName).default
		}

		mergeWith(filesObj, obj, customizer)
	}
	function customizer(objValue, srcValue) {
		if (isArray(objValue)) {
			return unionBy(srcValue, objValue, 'path')
		}
	}


	/**
	 * 第二步
	 * 截取出 主路由文件
	 * 保留 generator 的对象
	 * */
	let constantRoutes = []
	for (let key in filesObj) {
		let fileDefault = filesObj[key]
		if (key.indexOf(indexPath) !== -1) {
			constantRoutes = filesObj[key]
			delete filesObj[key]
			break;
		}
	}

	/**
	 * 第三步
	 * 平铺所有路由配置项
	 * */
	let flatRoutes = []
	for (let key in filesObj) {
		let mappedRouter = filesObj[key]
		if (isArray(mappedRouter)) {
			flatRoutes.push(...mappedRouter)
		} else if (isObject(mappedRouter)) { // 为对象时
			flatRoutes.push(mappedRouter)
		}
	}

	return {
		constantRoutes,
		flatRoutes
	}
}

// 旧版半自动
// export function initRoutes(indexPath, files, mainPath) {
// 	if (!isArray(files)) {
// 		files = [files];
// 	}
//
// 	let routes = []
//
// 	for (let i = 0; i < files.length; i++) {
// 		let mappedRouters = files[i];
// 		let routerKeys = mappedRouters.keys()
//
// 		for (let i = 0; i < routerKeys.length; i++) {
// 			let key = routerKeys[i]
// 			// 取出 主路由 配置项
// 			if (key.indexOf(indexPath) !== -1) {
// 				routes = mappedRouters(key).default
// 				routerKeys.splice(i, 1)
// 				break;
// 			}
// 		}
// 		console.log("里有", routerKeys)
//
// 		if (routes.length > 0) {
// 			filterRoutesBlackList(mappedRouters, routes)
// 		}
// 	}
//
// 	// 过滤注入
// 	function filterRoutesBlackList(mappedRouters, arr) {
// 		let routerKeys = mappedRouters.keys()
//
// 		for (let i = 0; i < arr.length; i++) {
// 			let rItem = arr[i]
// 			if (rItem.path === mainPath) {
// 				// 主路由 配置项是否追加 children
// 				rItem.children = rItem.children || []
//
// 				each(routerKeys, key => {
// 					let mappedRouter = mappedRouters(key).default
//
// 					if (rItem.meta && rItem.meta.backList) {
// 						if (arrIncludes(rItem.meta.backList, key)) {
// 							return
// 						}
// 					}
//
// 					if (isArray(mappedRouter)) {
// 						each(mappedRouter, (r) => {
// 							if (rItem.path === r.path) {
// 								// 尽量不处理 文件自己 所在路径
// 								// 如果是文件自己不进行递归
// 								return
// 							}
// 							injectRouter(rItem, r)
// 						})
// 					} else {
// 						// 为对象时
// 						if (rItem.path.indexOf(routerJsToIf(key)) > 0) {
// 							// 尽量不处理 文件自己 所在路径
// 							// 如果是文件自己不进行递归
// 							return
// 						}
// 						injectRouter(rItem, mappedRouter)
// 					}
//
// 				})
// 			}
// 		}
//
// 	}
//
// 	// 路由js文件 转换为判断跳出递归条件
// 	function routerJsToIf(routerPath) {
// 		let path = routerPath.replace(/.\//, '')
// 		path = path.replace(/\/index.js/, '')
// 		return path
// 	}
//
// 	function arrIncludes(list, str) {
// 		for (let i = 0; i < list.length; i++) {
// 			if (startsWith(str, list[i])) {
// 				return true
// 			}
// 		}
// 	}
//
// 	// 路由器排序
// 	routes = autoRouter(routes)
// 	return routes
// }


export default {
	handle,
	autoRouter,
	injectRouter,
	initRoutesWhite,
	initRoutes,
	flatFilesToRouteArr
}
