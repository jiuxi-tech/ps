/*!
* storeTool
* (c) 2020 lincong1987
*/


import {
	each, find, isArray, trimEnd, trimStart, filter, endsWith, flattenDeep,
} from 'lodash-es'


/**
 * 自动提升 index
 * @param modules
 * @returns {Array}
 */
export function fixModules(modules) {

	// 寻找 index.js 优先
	let result = filter(modules, function (module) {
		return endsWith(module.name, '/index.js')
	})
	// 追加非index.js文件
	each(modules, function (module) {
		if (!endsWith(module.name, '/index.js')) {
			result.push(module)
		}
	})

	return result

}

export const handle = function (store, files) {
	if (!isArray(files)) {
		files = [files];
	}
	files = flattenDeep(files)

	/**
	 * 第一步
	 * 合并多组文件(去重) 形成对象
	 * */
	let filesObj = {}
	for (let i = 0; i < files.length; i++) {
		let file = files[i];

		file.keys().forEach(key => {
			filesObj[key] = file(key).default
		})
	}

	/**
	 * 第二步
	 * 生成对应模块数组
	 * */
	let mappedModules = []
	for (let key in filesObj) {
		let fileDefault = filesObj[key]
		mappedModules.push({
			name: key,
			default: fileDefault,
		})
	}

	each(fixModules(mappedModules), (module) => {
		let mappedModule = module.default

		let mappedModulePath = trimEnd(trimStart(module.name, './'), '.js').split('/')

		// 如果是 index.js 则直接暴露 文件夹名称
		if (mappedModulePath[mappedModulePath.length - 1] == 'index') {
			mappedModulePath.pop()
		}
		//
		// if(!store.hasModule(mappedModulePath)){
		// 	store.registerModule(mappedModulePath, mappedModule)
		// }

		// todo: bug 如果某个文件夹中没有 index.js 时会报错，因为没有父模块
		if(mappedModulePath.length>0) {
			store.registerModule(mappedModulePath, mappedModule)
		}

	})
	return store
}

export default {handle, fixModules}
