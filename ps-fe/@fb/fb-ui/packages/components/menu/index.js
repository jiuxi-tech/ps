/*!
* index
* (c) 2020 lincong1987
*/

import component from './src/FbMenu'
import FbSubMenu from './src/FbSubMenu'
import FbMenuItem from './src/FbMenuItem'
import FbMenuItemGroup from './src/FbMenuItemGroup'

component.install = function (adapter) {
	adapter.component(component.name, component)
	adapter.component(FbSubMenu.name, FbSubMenu)
	adapter.component(FbMenuItem.name, FbMenuItem)
	adapter.component(FbMenuItemGroup.name, FbMenuItemGroup)
}

export default component


