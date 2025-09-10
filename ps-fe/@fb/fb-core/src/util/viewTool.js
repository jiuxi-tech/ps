/*!
 * viewTool
 * (c) 2020 lincong1987
 */

import {sortBy, trimEnd, trimStart, each, cloneDeep, isArray, startsWith} from 'lodash-es'
import {autoRouter, injectRouter} from './routerTool'

//   views
//  ├── index.vue         # 根路由页面 路径 /
//  ├── *.vue             # 模糊匹配 路径 *
//  ├── a.vue             # 路径 /a
//  ├── b
//  │   ├── index.vue     # 路径 /b
//  │   ├── @id.vue       # 动态路由 /b/:id
//  │   └── c.vue         # 路径 /b/c
//  └── layout.vue        # 根路由下所有view共用的外层

export function buildRouter(rotuer, context, filepath, component) {
//	                  debugger
//	var c = context('./', true, /\.vue$/)
//
//
//	console.log(c.keys())

	// let path = trimStart(trimEnd(key, '.js'), './').split('/')
	// if (path.length > 0 && path[path.length - 1] == 'index') {
	// 	path.pop()
	// }
}

const getRouteName = function (parentRoutePath, fileName) {
	const routeName = Path.posix.join(parentRoutePath, fileName)
	return routeName.slice(1).replace(/\//g, '_').replace(/@/g, '_').replace(/\*/g, 'FUZZYMATCH')
}

const getRoutePath = function (parentRoutePath, fileName) {
	// /index.vue -> /
	if (fileName === 'index') {
		fileName = ''
	}
	let routePath = Path.posix.join(parentRoutePath, fileName)
	// /@id.vue -> /:id
	routePath = routePath.replace(/@/g, ':')
	// /*.vue -> *
	if (routePath === '/*') {
		routePath = '*'
	}
	return routePath
}

export function handle(rotuer, files) {
	if (!isArray(files)) {
		files = [files];
	}

	//context('./', true, /\.vue$/)

	let views = []

	for (let i = 0; i < files.length; i++) {
		let mappedViews = files[i];
		mappedViews.keys().forEach((key) => {
			views.push({
				path: key,
				component: mappedViews(key).default,
			})

		})
	}

	let routers = []

	if (views.length > 0) {

		views = sortBy(views, "path")
		buildRouters(views, rotuer, routers)
	}

}

export function auto() {

}

export function buildRouters(views, $rotuer, routers) {
	each(sortBy(views, 'name'), (view) => {
		let route = {
			path: '',
			name: '',
			meta: {},
			component: {},
			children: []
		}
	})
}


export function handleAuto(files) {
	if (!isArray(files)) {
		files = [files];
	}

	let routes = []

	for (let i = 0; i < files.length; i++) {
		let mappedViews = files[i];

		let mappedViewsKeys = mappedViews.keys()
		let rankOneArr = []

		mappedViews.keys().forEach((key, i) => {
			// 初始化一级路由数组
			let comView = mappedViews(key).default
			if (comView.routerConfig && comView.routerConfig.meta) {

				// 深克隆出 router 对象
				let routerObj = cloneDeep(comView.routerConfig)
				let meta = routerObj.meta || {}
				initRouterObj(routerObj, comView, key)

				// 等级为 1 的时候为 routes 一级对象
				if (meta.rank && meta.rank == '1') {
					routes.push(routerObj)
					rankOneArr.push(i)
				}
			}
		})

		// 切割掉 rank = 1 的路由对象
		each(rankOneArr, (key, i) => {
			mappedViewsKeys.splice(key - i, 1)
		})

		if (routes.length > 0) {
			filterRoutesWhiteList(routes)
		}
	}

	// 初始化路由对象
	function initRouterObj(routerObj, comView, viewPath) {
		if (!routerObj.path) {
			routerObj.path = viewPathToRouterPath(viewPath)
		}

		if (!routerObj.name && comView.name) {
			routerObj.name = comView.name
		}

		if (routerObj.path.length > 0) {
			routerObj.component = comView
		}
	}

	// 文件路径转路由路径
	function viewPathToRouterPath(viewPath) {
		let path = viewPath.replace(/.\//, '/')
		path = path.replace(/.vue/, '')
		return path
	}

	// 过滤注入白名单
	function filterRoutesWhiteList(arr) {
		for (let i = 0; i < arr.length; i++) {
			let rItem = arr[i]
			if (rItem.meta && rItem.meta.whiteList) {
				let whiteList = rItem.meta.whiteList
				// 主路由 配置项是否追加 children
				rItem.children = rItem.children || []

				each(mappedViewsKeys, key => {
					let comView = mappedViews(key).default
					let routerObj = comView.routerConfig ? cloneDeep(comView.routerConfig) : {}
					initRouterObj(routerObj, comView, key)

					if (isArray(whiteList) &&
						arrIncludes(whiteList, key)
					) {
						if (rItem.path === viewPathToRouterPath(key)) {
							// 尽量不处理 文件自己 所在路径
							// 如果是文件自己不进行递归
							return
						}
						injectRouter(rItem, routerObj)
						if (routerObj.meta && routerObj.meta.whiteList) {
							// 递归追加白名单
							filterRoutesWhiteList([routerObj])
						}
					}
				})
			}
		}

	}

	// 数组判断
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

export default {
	handle,
	handleAuto
}
