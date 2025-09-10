/*!
 * index
 * (c) 2020 lincong1987
 */

import FbProperty from './src/FbProperty'
import FbPropertyItem from './src/FbPropertyItem'
import FbPropertyRow from './src/FbPropertyRow'

FbProperty.install = function (adapter) {
	adapter.component(FbProperty.name, FbProperty)
}

FbPropertyItem.install = function (adapter) {
	adapter.component(FbPropertyItem.name, FbPropertyItem)
}

FbPropertyRow.install = function (adapter) {
	adapter.component(FbPropertyRow.name, FbPropertyRow)
}

export function install (adapter) {
	FbProperty.install(adapter)
	FbPropertyItem.install(adapter)
	FbPropertyRow.install(adapter)
}

export default {

	install,
	FbProperty,
	FbPropertyItem,
	FbPropertyRow,

}


