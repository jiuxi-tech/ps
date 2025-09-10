/*!
* index
* (c) 2020 lincong1987
*/

import FbTree from './src/FbTree'
import FbTreeNode from './src/FbTreeNode'
import FbTreeNodeText from './src/FbTreeNodeText'

FbTree.install = function (adapter) {
	adapter.component(FbTree.name, FbTree)
}
FbTreeNode.install = function (adapter) {
	adapter.component(FbTreeNode.name, FbTreeNode)
}
FbTreeNodeText.install = function (adapter) {
	adapter.component(FbTreeNodeText.name, FbTreeNodeText)
}

export function install (adapter) {
	FbTree.install(adapter)
	FbTreeNode.install(adapter)
	FbTreeNodeText.install(adapter)
}

export default {
	FbTreeNodeText,
	FbTreeNode,
	FbTree,
	install,
}


