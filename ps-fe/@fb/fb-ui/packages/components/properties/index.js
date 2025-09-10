/*!
 * index
 * (c) 2020 lincong1987
 */

import FbProperties from './src/FbProperties'
import FbPropertiesItem from './src/FbPropertiesItem'
import FbPropertiesRow from './src/FbPropertiesRow'

FbProperties.install = function (adapter) {
	adapter.component(FbProperties.name, FbProperties)
}

FbPropertiesItem.install = function (adapter) {
	adapter.component(FbPropertiesItem.name, FbPropertiesItem)
}

FbPropertiesRow.install = function (adapter) {
	adapter.component(FbPropertiesRow.name, FbPropertiesRow)
}

export function install (adapter) {
	FbProperties.install(adapter)
	FbPropertiesItem.install(adapter)
	FbPropertiesRow.install(adapter)
}

export default {

	install,
	FbProperties,
	FbPropertiesItem,
	FbPropertiesRow,

}


