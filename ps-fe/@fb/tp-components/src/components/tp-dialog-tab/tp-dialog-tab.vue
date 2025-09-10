<template>
	<div class="tp-dialog-tab-content">

		<fb-dialog
			ref="tp-dialog-tab"
			no-padding
			:body-style="{padding: 0, 'padding-bottom': '200px'}"
			v-if="dialog.create"
			v-show="dialog.visible"
			:title="dialog.title"
			:height="dialog.height"
			:width="dialog.width"
			:top="dialog.top"
			:overflow="dialog.overflow"
			:can-full-screen="dialog.canFullScreen"
			:disable-esc="true"
			:close-on-click-shadow="false"
			@on-close="closeTpDialog">

			<fb-page-tabs style="height: 100%">
				<fb-tabs v-model="currentTab" style="height: 100%" type="card" :before-change="beforeChange">
					<fb-tab v-for="(item, index) in tabSteps" :key="index" :label="item.label">
						<component ref="componentPage" :is="item.component" :param="dialog.param"
								   :parentPage="currentPage"></component>
					</fb-tab>
				</fb-tabs>
			</fb-page-tabs>

		</fb-dialog>
	</div>

</template>

<script>

	import {isString} from "lodash-es";

	export default {
		name: 'TpDialogTab',
		mixins: [],
		// 接收父组件的传参
		props: {},
		// 组件
		components: {
			// 'component-a': ComponentA,
		},
		// 创建方法
		created() {
			// 创建方法
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化
		},
		data() {
			return {
				// 当前界面，用于跨组件传递
				currentPage: this,
				// 默认的选中Tab
				currentTab: 1,
				tabSteps: [],
				// Dialog 是否显示
				dialog: {
					title: '',
					param: {},
					canFullScreen: false,
					create: false,
					visible: false,
					height: 600,
					width: 800,
					top: '15vh',
					overflow: 'hidden',
					tabChangeConfirm: true, // tab切换时，修改数据后的提示框
					callBack: this.closeTpDialogCallBack,
				},
				pageParam: {},
			}
		},
		// 方法
		methods: {
			// 设置标题
			/**
			 [{
				url: '/sys/person/vvvv.vue',
				label: '人员基本信息',
				icon: "chart-line"
			},
			 {
				url: '/sys/person/xxxx.vue',
				label: '人员扩展信息',
				icon: "progressbar"
			}]
			 * @param tabArry
			 * @param param
			 * @param title
			 * @param options
			 */
			show(tabArry, param, title, options) {

				// 屏幕可用工作区宽度
				// let currentWidth = window.screen.width
				// 网页分辨率宽
				let currentWidth = window.innerWidth
				if (currentWidth < 1440) {
					// 小分辨率，使用默认值
					this.dialog.width = 800;
					this.dialog.height = 556;
				} else if (1440 <= currentWidth && currentWidth < 1600) {
					this.dialog.width = 864;
					this.dialog.height = 588;
				} else if (1600 <= currentWidth && currentWidth < 1920) {
					this.dialog.width = 960;
					this.dialog.height = 656;
				} else if (1920 <= currentWidth) {
					this.dialog.width = 1152;
					this.dialog.height = 796;
				}
				console.log("页面可见区域宽"+ currentWidth + ',' + this.dialog.width + ',' + this.dialog.height);

				let componentArry = []
				tabArry.forEach(function (item, index) {
					// let component = () => import('@/views' + item.url)
					const url = item.url
					if (isString(url)) {
						console.warn('请以 import 方式传入url，string形式预计在2x废弃！！！')
					}
					let component = () => ({
						// 需要加载的组件 (应该是一个 `Promise` 对象)
						component: isString(url) ? import('@/views' + url) : url,
						// 异步组件加载时使用的组件
						loading: {template: '<div><fb-spin></fb-spin></div>'},
						// 加载失败时使用的组件
						error: {template: '<div>加载组件失败</div>'},
						// 展示加载时组件的延时时间。默认值是 200 (毫秒)
						delay: 200,
						// 如果提供了超时时间且组件加载也超时了，
						// 则使用加载失败时使用的组件。默认值是：`Infinity`
						timeout: 3000
					});

					item.component = component
					componentArry.push(item)
				})

				this.tabSteps = componentArry

				this.dialog.title = title
				options = options || {};
				this.dialog.height = options.height || this.dialog.height;
				this.dialog.width = options.width || this.dialog.width;
				if (typeof (options.canFullScreen) != 'undefined' && !options.canFullScreen) {
					this.dialog.canFullScreen = options.canFullScreen
				}
				this.dialog.top = options.top || this.dialog.top;
				this.dialog.overflow = options.overflow || this.dialog.overflow;

				if (typeof (options.tabChangeConfirm) != 'undefined' && !options.tabChangeConfirm) {
					this.dialog.tabChangeConfirm = options.tabChangeConfirm
				}
				this.dialog.callBack = options.callBack || this.closeTpDialogCallBack;

				// 设置参数
				this.dialog.param = param

				// 第一次，创建
				this.dialog.create = true
				// 显示
				this.dialog.visible = true
				// 默认显示第一个tab
				this.currentTab = options.currentTab || this.currentTab;

				this.$nextTick(() => {
					this.dialog.create && this.$refs['tp-dialog-tab'].show()
				})
				// 每次打开清空参数
				this.pageParam = {}
			},
			// 取消
			closeTpDialog(param) {

				if (param) {
					// 子组件调用父组件方法，并传递参数
					this.$emit('closeTpDialog', param)
					// 另一种回调
					this.dialog.callBack(param)
				} else {
					// 子组件调用父组件方法
					this.$emit('closeTpDialog')
					// 另一种回调
					this.dialog.callBack()
				}
				// 隐藏
				this.dialog.visible = false
			},
			// 监听tab切换(目标tab, 目标tab的下标)
			beforeChange(tab, index) {

				// 默认tab切换时，提示confirm
				if (!this.dialog.tabChangeConfirm) {
					return true
				}

				let that = this
				let i = this.currentTab - 1;
				// 获取当前界面是否更新
				let updateCount = that.$refs.componentPage[i].$data.updateCount
				// let updateCount = tab.$children[0].$data.updateCount;

				// 等于 0  则跳过
				if (updateCount <= 1) {
					return true
				}

				//更新次数大于等于1 说明用户修改过当前页数据 因为获取详情时会更新一次
				return new Promise((resolve, reject) => {
					this.$confirm('您填写的内容未保存，确定离开吗？', () => {
						// 重置修改计算
						that.$refs.componentPage[i].$data.updateCount = 0;
						resolve(true)
					}, () => {
						resolve(false)
					})
				});
			},
			// 设置参数，用于界面传参
			setPageParam(e) {
				this.pageParam = e
			},
			// 获取参数，用于界面传参
			getPageParam() {
				return this.pageParam
			},
			// 默认的弹出窗回调
			closeTpDialogCallBack(param) {
				// 默认不需要做任何事情
			}

		},
	}
</script>

<style lang="less" scoped>
	@import "../../assets/styles/common.less";

	.tp-dialog-tab-content {
		/deep/ .@{FbUiPrefix}-page-tabs {
			height: 100%;

			.@{FbUiPrefix}-tabs {
				height: 100%;
			}
		}
	}

</style>
