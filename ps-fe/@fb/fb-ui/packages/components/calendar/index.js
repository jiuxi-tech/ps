/*!
* index
* (c) 2020 lincong1987
*/

import FbCalendar from './src/FbCalendar'
import FbCalendars from './src/FbCalendars'

FbCalendars.install = function (adapter) {
	adapter.component(FbCalendars.name, FbCalendars)
}

FbCalendar.install = function (adapter) {
	adapter.component(FbCalendar.name, FbCalendar)
}

const install = function (adapter) {
	FbCalendar.install(adapter)
	FbCalendars.install(adapter)
}

export default {
	FbCalendar,
	FbCalendars,
	install,
}


