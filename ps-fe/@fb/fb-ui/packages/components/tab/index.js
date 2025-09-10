/*!
 * index
 * (c) 2020 lincong1987
 */

import FbTabs from './src/FbTabs'
import FbTab from './src/FbTab'

FbTab.install = function (adapter) {
	adapter.component(FbTab.name, FbTab)
}
FbTabs.install = function (adapter) {
	adapter.component(FbTabs.name, FbTabs)
}

export const install = function (adapter) {
	FbTab.install(adapter)
	FbTabs.install(adapter)
}

export default {
	install,
	FbTabs,
	FbTab,
}


