/*!
 * index
 * (c) 2020 lincong1987
 */

import FbCollapse from './src/FbCollapse'

FbCollapse.install = function (adapter) {
	adapter.component(FbCollapse.name, FbCollapse)
}

export function install (adapter) {
	FbCollapse.install(adapter)
}

export default {

	install,
	FbCollapse,

}


