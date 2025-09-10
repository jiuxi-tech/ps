import component from './src/MotionPointWave.vue'

component.install = function (adapter) {
	adapter.component(component.name, component)
}

export default component
