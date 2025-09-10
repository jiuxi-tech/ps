/*!
 * index
 * (c) 2020 lincong1987
 */

import FbRankList from './src/FbRankList.vue'
import FbRankListItem from './src/FbRankListItem.vue'

FbRankList.install = function (adapter) {
	adapter.component(FbRankList.name, FbRankList)
}

FbRankListItem.install = function (adapter) {
	adapter.component(FbRankListItem.name, FbRankListItem)
}

export function install (adapter) {
	FbRankList.install(adapter)
	FbRankListItem.install(adapter)
}

export default {

	install,
	FbRankList,
	FbRankListItem,

}
