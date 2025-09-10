/*!
* index
* (c) 2020 lincong1987
*/

import axios, {
	AxiosAdapter,
	AxiosBasicCredentials,
	AxiosProxyConfig,
	AxiosTransformer,
	CancelToken,
	Method,
	ResponseType,
} from 'axios'
import { assignIn, each, isArray } from 'lodash-es'

const services = {}

export const createService = function (config, app) {

	// 开启使用 location.pathname 后台虚拟目录
	// 与路由 history 不通用
	if (config.usePathName && location.pathname !== '/') {
		let pathname = location.pathname
		config.baseURL = pathname.substr(0,pathname.length - 1)
	}

	const instance = axios.create(config)

	// 请求拦截
	instance.setRequestInterceptor = function (before, error) {
		if (isArray(before)) {
			return instance.interceptors.request.use(...before)
		}
		return instance.interceptors.request.use(before, error)

	}

	// 删除请求拦截
	instance.ejectRequestInterceptor = function (interceptor) {
		return instance.interceptors.request.eject(interceptor)
	}

	// 响应拦截
	instance.setResponseInterceptor = function (after, error) {
		if (isArray(after)) {
			return instance.interceptors.response.use(...after)
		}
		return instance.interceptors.response.use(after, error)
	}

	//
	// 删除响应拦截
	instance.ejectResponseInterceptor = function (interceptor) {
		return instance.interceptors.response.eject(interceptor)
	}

	instance.setHeaders = function (headers) {
		each(headers, (value, key) => {
			if(['delete', 'get', 'head', 'post', 'put', 'patch', 'common'].includes(key)) {
				assignIn(instance.defaults.headers[key], value)
			}   else{
				instance.defaults.headers[key] = value
			}
		})

	}

	instance.removeHeaders = function (headers) {
		each(headers, (value, key) => {
			if(instance.defaults.headers[key]){
				delete instance.defaults.headers[key]
			}
		})
	}


	// if (config.headers) {
	// 	instance.defaults.headers = config.headers
	// }

	if (app.Vue) {
		app.Vue.prototype[config.name] = instance
	}

	app[config.name] = instance

	return instance
}

export const installService = function (app, serviceConfig = []) {

	let services = {}

	each(serviceConfig, (config) => {

		if (!config.name) {
			console.log('[ERROR] serviceConfig 必须要有 name')
		} else {
			let name = config.name
			services[name] = createService(config, app)
			console.log(`[INFO] createService ${name}`)
		}

	})
	return services

}

export default {
	axios,
	installService,
	createService,
}
