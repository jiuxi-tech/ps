/*!
 * index
 * (c) 2020 lincong1987
 */

import FbPageEdit from './src/FbPageEdit'
import FbPageSave from './src/FbPageSave'
import FbPageSearch from './src/FbPageSearch'
import FbPageTree from './src/FbPageTree'
import FbPageView from './src/FbPageView'
import FbPageTreeTabs from './src/FbPageTreeTabs'
import FbPageTreeTable from './src/FbPageTreeTable'
import FbPageTabs from './src/FbPageTabs'
import FbPageTreeEdit from './src/FbPageTreeEdit'
import FbDialogTreeSelect from './src/FbDialogTreeSelect'
import FbDialogTableSelect from './src/FbDialogTableSelect'
import FbDialogTable from './src/FbDialogTable'
import FbTabsPageSearch from './src/FbTabsPageSearch'

FbPageEdit.install = function (adapter) {
	adapter.component(FbPageEdit.name, FbPageEdit)
}
FbPageSave.install = function (adapter) {
	adapter.component(FbPageSave.name, FbPageSave)
}
FbPageSearch.install = function (adapter) {
	adapter.component(FbPageSearch.name, FbPageSearch)
}

FbPageTree.install = function (adapter) {
	adapter.component(FbPageTree.name, FbPageTree)
}

FbPageView.install = function (adapter) {
	adapter.component(FbPageView.name, FbPageView)
}

FbPageTreeTabs.install = function (adapter) {
	adapter.component(FbPageTreeTabs.name, FbPageTreeTabs)
}
FbPageTreeTable.install = function (adapter) {
	adapter.component(FbPageTreeTable.name, FbPageTreeTable)
}
FbPageTabs.install = function (adapter) {
	adapter.component(FbPageTabs.name, FbPageTabs)
}
FbPageTreeEdit.install = function (adapter) {
	adapter.component(FbPageTreeEdit.name, FbPageTreeEdit)
}

FbDialogTreeSelect.install = function (adapter) {
	adapter.component(FbDialogTreeSelect.name, FbDialogTreeSelect)
}
FbTabsPageSearch.install = function (adapter) {
	adapter.component(FbTabsPageSearch.name, FbTabsPageSearch)
}
FbDialogTable.install = function (adapter) {
	adapter.component(FbDialogTable.name, FbDialogTable)
}

export function install (adapter) {
	FbPageEdit.install(adapter)
	FbPageSave.install(adapter)
	FbPageSearch.install(adapter)
	FbPageTree.install(adapter)
	FbPageView.install(adapter)
	FbPageTreeTabs.install(adapter)
	FbPageTreeTable.install(adapter)
	FbPageTabs.install(adapter)
	FbPageTreeEdit.install(adapter)
	FbDialogTreeSelect.install(adapter)
	FbDialogTableSelect.install(adapter)
	FbTabsPageSearch.install(adapter)
	FbDialogTable.install(adapter)
}

export default {
	FbPageSearch,
	FbPageSave,
	FbPageEdit,
	FbPageTree,
	FbPageView,
	FbPageTreeTabs,
	FbPageTreeTable,
	FbPageTabs,
	FbPageTreeEdit,
	FbDialogTreeSelect,
	FbDialogTableSelect,
	FbTabsPageSearch,
	FbDialogTable,
	install,
}


