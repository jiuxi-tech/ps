/*!
 * FB-UI 组件库入口文件
 * 负责组件库的初始化、组件注册、主题配置和Vue插件安装
 * (c) 2020 lincong1987
 */

// 样式导入
// import 'vue-draggable-resizable/dist/VueDraggableResizable.css' // 由于webpack导入问题已注释
import '../packages/theme/pc3/src/index.less' // PC3主题样式文件

// 核心依赖导入
import pkg from '../package.json' // 包信息
import { keyBy, camelCase } from 'lodash-es' // 工具函数
import _propsUtil from '../packages/utils/props-util' // 属性工具

// 组件导入
import componentList from '../packages/components/components' // 所有组件列表
import loadingBar from '../packages/components/loading-bar' // 加载条组件
import message from '../packages/components/message' // 消息组件
import msgbox from '../packages/components/message-box' // 消息框组件
import loading from '../packages/components/spin' // 加载动画组件
import notification from '../packages/components/notification' // 通知组件

// 配置和主题导入
import { prefix, defaults } from './config' // 配置文件
import { colors as pc3 } from '../packages/theme/pc3/src/common/colors' // PC3主题颜色
import transitions from '../packages/transitions/transitions' // 过渡动画

// 图标字体处理：将字体图标转换为可用的图标映射
import { glyphs } from '../packages/theme/pc3/src/fonts/iconfont.json'
let icons = {} // 图标映射对象

// 构建图标映射：将字体类名转换为驼峰命名
glyphs.map(n => {
	icons[camelCase(n.font_class.toLowerCase())] = n.font_class
})

// 主题配置：支持多主题切换
const theme = {
	pc3, // PC3主题颜色配置
}

// 控制台输出：显示组件库版本和文档链接
//console.log(`use fb-ui ${pkg.version}`)
console.log(
	`%c fb-ui v${pkg.version}  %c docs: http://10.10.0.36/fb/fb-docs/#/fb-ui/ `,
	`padding: 5px 0; background: #0284FE; color: #fff; border-radius: 4px 0 0 4px;`,
	`padding: 5px 0; background: #fff; color: #0284FE; border-radius: 0 4px 4px 0;`)

/**
 * Vue插件安装函数
 * 负责将FB-UI组件库注册到Vue实例中
 * @param {Object} Vue - Vue构造函数
 * @param {Object} opts - 安装选项配置
 * @param {Object} opts.defaultProps - 组件默认属性覆盖
 * @param {String} opts.theme - 主题名称（默认pc3）
 * @param {String} opts.size - 全局组件尺寸
 * @param {Number} opts.zIndex - 全局层级起始值
 */
export const install = function (Vue, opts = {}) {

	// 按顺序执行安装步骤，支持自定义安装函数覆盖
	([
		mixinVue,        // 混入Vue全局方法和属性
		setDefaultProps, // 设置组件默认属性
		registerComponents, // 注册所有组件到Vue
		setPrototype,    // 设置Vue原型方法
	]).map((fn) => {
		// 如果用户提供了自定义安装函数，则使用自定义函数
		if (typeof opts[fn.name] !== 'undefined') {
			opts[fn.name](Vue, opts)
		} else {
			// 否则使用默认安装函数
			fn(Vue, opts)
		}
	})

	// 设置全局配置到Vue原型
	Vue.prototype.$FBUI = {
		size: opts.size || '', // 全局组件尺寸（small/medium/large）
		zIndex: opts.zIndex || 2000 // 全局层级起始值
	}

}

/**
 * 设置组件默认属性
 * 将用户传入的默认属性与内置默认属性进行合并，并挂载到Vue原型和全局变量上
 * @param {Object} Vue - Vue构造函数
 * @param {Object} opts - 安装选项配置
 */
export function setDefaultProps (Vue, opts) {
	// 合并默认属性并同时设置到全局变量和Vue原型上，便于组件访问
	window.FbUI_defaultProps = Vue.prototype.defaultProps =
		Object.assign({}, defaults, opts.defaultProps || {})
}

/**
 * 混入Vue全局数据
 * 为所有Vue组件提供主题、图标、过渡动画等全局数据
 * @param {Object} Vue - Vue构造函数
 * @param {Object} opts - 安装选项配置
 */
export function mixinVue (Vue, opts) {
	Vue.mixin({
		data () {
			return {
				icons,                              // 图标映射对象
				colors: theme[opts.theme || 'pc3'], // 主题颜色配置
				transitions,                        // 过渡动画配置
				prefix,                            // 组件前缀
			}
		},
	})
}

/**
 * 注册所有组件到Vue
 * 遍历组件列表并全局注册每个组件，同时为特定组件初始化默认属性
 * @param {Object} Vue - Vue构造函数
 */
export function registerComponents (Vue) {
	componentList.map(component => {
		// 设置组件名称标识
		component.componentName = component.name

		// 为特定组件初始化默认属性
		// 目前只对以下组件进行默认属性初始化，其他组件需要更详细的测试
		if (component.name === 'FbSimpleTable' ||
			component.name === 'FbDialog' ||
			component.name === 'FbNumber' ||
			component.name === 'FbFlop' ||
			component.name === 'FbTree' ||
			component.name === 'FbUpload' ||
			component.name === 'FbProperty' ||
			component.name === 'FbPropertyItem' ||
			component.name === 'FbUpload' ||
			component.name === 'FbDatepicker') {
			// 使用属性工具初始化组件的默认属性
			_propsUtil.initDefaultProps(component.props, window.FbUI_defaultProps[component.name] || {})
		}

		// 全局注册组件
		Vue.component(component.name, component)
	})
}

/**
 * 设置Vue原型方法
 * 将组件库的工具方法和组件挂载到Vue原型上，便于在组件中通过this.$xxx直接使用
 * @param {Object} Vue - Vue构造函数
 */
export function setPrototype (Vue) {
	// 加载相关组件
	Vue.prototype.$loadingBar = loadingBar     // 顶部加载进度条
	Vue.prototype.$loading = loading           // 加载动画遮罩
	
	// 消息提示相关组件
	Vue.prototype.$message = message           // 轻量级消息提示
	Vue.prototype.$msgbox = msgbox             // 消息对话框
	Vue.prototype.$alert = msgbox.alert        // 警告对话框
	Vue.prototype.$confirm = msgbox.confirm    // 确认对话框
	Vue.prototype.$notification = notification // 通知提醒
}

/* istanbul ignore if */
if (typeof window !== 'undefined' && window.Vue) {
	console.log('Auto install fb-ui')
	install(window.Vue)
}

// 按需导出：允许用户按需引入特定的组件和工具
export {
	loadingBar,    // 顶部加载进度条组件
	message,       // 消息提示组件
	msgbox,        // 消息对话框组件
	loading,       // 加载动画组件
	notification,  // 通知组件
	prefix,        // 组件前缀
	defaults,      // 默认配置
	icons,         // 图标映射
	transitions,   // 过渡动画
	theme,         // 主题配置
	_propsUtil as propsUtil, // 属性工具（重命名导出）
}

// 默认导出：完整的组件库对象，包含版本信息和安装方法
export default {
	version: pkg.version, // 组件库版本号
	install,             // Vue插件安装方法
	theme,               // 主题颜色配置
	...componentList,    // 展开所有组件
}

