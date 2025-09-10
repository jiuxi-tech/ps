/*!
 * index
 * (c) 2020 lincong1987
 */

import FbLink from './src/FbLink'
import FbLinkGroup from './src/FbLinkGroup'

FbLink.install = function (adapter) {
	adapter.component(FbLink.name, FbLink)
}
FbLinkGroup.install = function (adapter) {
	adapter.component(FbLinkGroup.name, FbLinkGroup)
}

export function install (adapter) {
	FbLink.install(adapter)
	FbLinkGroup.install(adapter)
}

export default {
	FbLinkGroup,
	FbLink,
	install,
}


