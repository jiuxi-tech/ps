/*!
 * index
 * (c) 2020 lincong1987
 */

import FbBreadcrumb from './src/FbBreadcrumb'
import FbBreadcrumbItem from './src/FbBreadcrumbItem'
import FbBreadcrumbSeparator from './src/FbBreadcrumbSeparator'

FbBreadcrumb.install = function (adapter) {
	adapter.component(FbBreadcrumb.name, FbBreadcrumb)
}
FbBreadcrumbItem.install = function (adapter) {
	adapter.component(FbBreadcrumbItem.name, FbBreadcrumbItem)
}
FbBreadcrumbSeparator.install = function (adapter) {
	adapter.component(FbBreadcrumbSeparator.name, FbBreadcrumbSeparator)
}

export function install (adapter) {
	FbBreadcrumb.install(adapter)
	FbBreadcrumbItem.install(adapter)
	FbBreadcrumbSeparator.install(adapter)
}

export default {
	FbBreadcrumbItem,
	FbBreadcrumb,
	FbBreadcrumbSeparator,
	install,
}


