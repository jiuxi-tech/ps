/*!
 * index
 * (c) 2020 lincong1987
 */
//import FbTable from './src/FbTable'
import FbSimpleTable from './src/FbSimpleTable'
//import FbSimpleTableColumn from './src/FbSimpleTableColumn'


//FbTable.install = function (adapter) {
//	adapter.component(FbTable.name, FbTable)
//}

FbSimpleTable.install = function (adapter) {
	adapter.component(FbSimpleTable.name, FbSimpleTable)
}
//FbSimpleTableColumn.install = function (adapter) {
//	adapter.component(FbSimpleTableColumn.name, FbSimpleTableColumn)
//}

export const install = function (adapter) {
//	FbTable.install(adapter)
	FbSimpleTable.install(adapter)
//	FbSimpleTableColumn.install(adapter)
}

export default {
	install,
//	FbTable,
	FbSimpleTable,
//	FbSimpleTableColumn,
}


