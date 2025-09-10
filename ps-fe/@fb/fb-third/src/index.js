/*!
 * index
 * (c) 2021 lincong1987
 */
import pkg from '../package.json'
import { keyBy } from 'lodash-es'
// 引入 echarts 相关
import * as echarts from 'echarts'
import FbEcharts from '../packages/components/echarts/FbEcharts.vue'
import { initThemes } from '../packages/components/echarts'
// 引入 天地图 二次封装
import FbTmap from '../packages/components/tmap/src/FbTmap.vue'
// 引入第三方天地图
import tdt from 'vue-tianditu'
// import tdt from '../packages/components/tdt/index'
// 引入 tinymce 二次封装编辑器
import FbEditor from '../packages/components/editor/src/FbEditor.vue'
// 引入 swiper
import VueAwesomeSwiper from '../packages/components/swiper/index'
import 'swiper/swiper-bundle.css'

// 引入 lmap
import lmap from '../packages/components/lmap'

import FbQrcode from '../packages/components/qrcode'
import FbLottie from '../packages/components/lottie'

let componentList = [
	FbTmap,
	FbEditor,
	FbEcharts,
	FbQrcode,
	FbLottie,
	...lmap.components,
]

console.log(
	`%c fb-third v${pkg.version} %c docs: http://10.10.0.36/fb/fb-docs/#/fb-third/ `,
	`padding: 5px 0; background: #0284FE; color: #FFFFFF; border-radius: 4px 0 0 4px;`,
	`padding: 5px 0;  background: #FFFFFF; color: #0284FE; border-radius: 0 4px 4px 0;`)

/**
 * export *
 * 支持正常的 ES6 导出后 import {xxx} from 'fb-third' 使用模式
 * 重点：是支持编译器提示和文件索引
 * 而非 export default 文件默认导出对象的结构形式
 */
export * from '../packages/components/echarts/components'
export * from '../packages/components/echarts/utils/export'
export * from '../packages/components/motions/components'

export const install = function (Vue, opts = {}) {

	// 初始话 echarts 全局主题
	initThemes(echarts, opts)
	Vue.prototype.$echarts = echarts

	componentList.map(component => {
		// echarts 主题外引入初始配置
		if (component.name === 'FbEcharts' && opts.echarts &&
			opts.echarts.theme && opts.echarts.theme.defaultUse) {
			component.props.theme.default = opts.echarts.theme.defaultUse
		}

		component.componentName = component.name
		Vue.component(component.name, component)
		// console.log('[fb-third] installed ', component.name)
	})

	// 注意给docs使用 true

	let isDocs = false
	if (process.env.NODE_ENV === 'production') {
		isDocs = true
	}
	if (opts.tdt && opts.tdt.tk) {
		tdt.install(Vue, opts.tdt)
	}
	if (opts.swiper || isDocs) {
		VueAwesomeSwiper.install(Vue, opts.swiper)
		console.log('[fb-third] installed swiper@4.5.1')
	}
}

/* istanbul ignore if */
if (typeof window !== 'undefined' && window.Vue) {
	console.log('Auto install fb-third')
	install(window.Vue)
}

export const components = keyBy(componentList, 'name')

// es6 export default 语法 不支持 import 结构引入
export default {
	// ...components,
	// ...componentList,
	// ...tdt.components,
	install,
	version: pkg.version,
}


