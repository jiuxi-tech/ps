/*!
 * index
 * (c) 2020 lincong1987
 */

import FbList from './src/FbList'

import FbListItem from './src/FbListItem'

import FbListItemMeta from './src/FbListItemMeta'

FbList.install = function (adapter) {
	adapter.component(FbList.name, FbList)
}

FbListItem.install = function (adapter) {
	adapter.component(FbListItem.name, FbListItem)
}

FbListItemMeta.install = function (adapter) {
	adapter.component(FbListItemMeta.name, FbListItemMeta)
}

export function install (adapter) {
	FbList.install(adapter)
	FbListItem.install(adapter)
	FbListItemMeta.install(adapter)
}

export default {
	install,
	FbList,
	FbListItem,
	FbListItemMeta,
}


