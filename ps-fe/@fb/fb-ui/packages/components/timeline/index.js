/*!
 * FbTimeline
 * (c) 2020 lincong1987
 */

import FbTimeline from './src/FbTimeline'
import FbTimelineItem from './src/FbTimelineItem'

FbTimeline.install = function (adapter) {
	adapter.component(FbTimeline.name, FbTimeline)
}

FbTimelineItem.install = function (adapter) {
	adapter.component(FbTimelineItem.name, FbTimelineItem)
}

export function install (adapter) {
	FbTimeline.install(adapter)
	FbTimelineItem.install(adapter)
}

export default {
	FbTimeline,
	FbTimelineItem,
	install,
}


