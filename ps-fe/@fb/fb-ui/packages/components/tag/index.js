/*!
 * index
 * (c) 2020 lincong1987
 */

import FbTag from './src/FbTag'
import FbTags from './src/FbTags'

FbTag.install = function (adapter) {
	adapter.component(FbTag.name, FbTag)
}

FbTags.install = function (adapter) {
	adapter.component(FbTags.name, FbTags)
}

export function install (adapter) {
	FbTag.install(adapter)
	FbTags.install(adapter)
}

export default {
	FbTags,
	FbTag,
	install,
}


