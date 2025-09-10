/*!
 * FB-UI 配置文件
 * 定义组件库的全局配置、前缀和默认属性
 * (c) 2021 lincong1987
 */

// 全局配置对象，可用于存储运行时配置
let config = {}

// 组件前缀：用于CSS类名和组件命名的统一前缀
export const prefix = 'jpx'

// 组件默认属性配置：为各个组件定义默认的属性值
export const defaults = {

	// 简单表格组件默认配置
	FbSimpleTable: {
		showLoading: true, // 默认显示加载状态
		// pk: 'lalala', // 主键字段（已注释）
		// pager: { // 分页器配置（已注释）
		// 	showQuickJumper: true, // 显示快速跳转
		// }
	},

	// 对话框组件默认配置
	FbDialog: {
		zIndex: 1000 // 默认层级：确保对话框显示在其他元素之上
	},

	// 消息框包装器默认配置
	FbMessageBoxWrapper: {
		zIndex: 5000 // 默认层级：确保消息框显示在最顶层
	},

}

// 导出配置对象：包含全局配置、前缀和默认属性
export default {
	config,    // 全局配置
	prefix,    // 组件前缀
	defaults,  // 组件默认属性
}
