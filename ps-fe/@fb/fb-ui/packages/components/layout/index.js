/*!
 * index
 * (c) 2020 lincong1987
 */

import FbLayout from './src/FbLayout'
import FbLayoutHeader from './src/FbLayoutHeader'
import FbLayoutBody from './src/FbLayoutBody'
import FbLayoutFooter from './src/FbLayoutFooter'
import FbLayoutSider from './src/FbLayoutSider'

FbLayout.install = function (adapter) {
	adapter.component(FbLayout.name, FbLayout)
}
FbLayoutHeader.install = function (adapter) {
	adapter.component(FbLayoutHeader.name, FbLayoutHeader)
}
FbLayoutBody.install = function (adapter) {
	adapter.component(FbLayoutBody.name, FbLayoutBody)
}
FbLayoutFooter.install = function (adapter) {
	adapter.component(FbLayoutFooter.name, FbLayoutFooter)
}
FbLayoutSider.install = function (adapter) {
	adapter.component(FbLayoutSider.name, FbLayoutSider)
}

export function install (adapter) {

	FbLayout.install(adapter)
	FbLayoutHeader.install(adapter)
	FbLayoutBody.install(adapter)
	FbLayoutFooter.install(adapter)
	FbLayoutSider.install(adapter)

}

export default {
	FbLayout,
	FbLayoutHeader,
	FbLayoutBody,
	FbLayoutFooter,
	FbLayoutSider,
	install,
}


