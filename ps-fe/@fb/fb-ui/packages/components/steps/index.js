/*!
 * index
 * (c) 2020 lincong1987
 */

import FbSteps from './src/FbSteps'
import FbStep from './src/FbStep'

FbSteps.install = function (adapter) {
	adapter.component(FbSteps.name, FbSteps)
}

FbStep.install = function (adapter) {
	adapter.component(FbStep.name, FbStep)
}

export function install (adapter) {
	FbSteps.install(adapter)
	FbStep.install(adapter)
}

export default {
	FbSteps,
	FbStep,
	install,
}


