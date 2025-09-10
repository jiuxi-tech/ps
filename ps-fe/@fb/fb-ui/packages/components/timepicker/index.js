/*!
* index
* (c) 2020 lincong1987
*/

import FbTimepicker from './src/FbTimepicker'
import FbTimeSelect from './src/FbTimeSelect'
import FbTimeItem from './src/FbTimeItem'

FbTimepicker.install = function (adapter) {
	adapter.component(FbTimepicker.name, FbTimepicker)
}
FbTimeSelect.install = function (adapter) {
	adapter.component(FbTimeSelect.name, FbTimeSelect)
}
FbTimeItem.install = function (adapter) {
	adapter.component(FbTimeItem.name, FbTimeItem)
}

const install = function (adapter) {
	FbTimepicker.install(adapter)
	FbTimeSelect.install(adapter)
	FbTimeItem.install(adapter)
}

export default {
	FbTimepicker,
	FbTimeSelect,
	FbTimeItem,
	install,
}
