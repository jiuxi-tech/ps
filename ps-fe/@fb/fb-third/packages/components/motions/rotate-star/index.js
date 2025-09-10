import component from './src/MotionRotateStar.vue'

component.install = function (adapter) {
	adapter.component(component.name, component)
}

export default component
