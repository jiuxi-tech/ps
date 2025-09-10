/*!
 * index
 * (c) 2020 lincong1987
 */

import FbPager from './src/FbPager'
import FbPagerModel from './src/FbPagerModel'

FbPager.install = function (adapter) {
	adapter.component(FbPager.name, FbPager)
}
FbPagerModel.install = function (adapter) {
	adapter.component(FbPagerModel.name, FbPagerModel)
}
export default {
	FbPager,
	FbPagerModel,
}


