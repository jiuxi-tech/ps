<template>
	<fb-container class="screen-panel-right">
		<transition :name="transitionName">
			<div v-show="divShow" :style="{'animationDelay': firstEntry ? `${delay}ms` : ''}">
				<slot></slot>
			</div>
		</transition>
	</fb-container>
</template>

<script>
/***
 * 右侧面板收集器
 * 大屏动画二级进场
 * */
export default {
	name: 'ScreenPanelRight',
	props: {
		// 毫秒时
		delay: {
			type: Number,
			default: 350
		},
		transitionName: {
			type: String,
			default: 'slide-in-right'
		},
		panelShow: {
			type: [Boolean, Number],
			default: true
		}
	},
	data() {
		return {
			divShow: false,
			// 判断首次进去屏幕（保障子元素插件等样式正常），动画delay延迟顺序。
			firstEntry: true,
		}
	},
	watch: {
		panelShow(val) {
			this.changeDivShow(val)
		}
	},
	mounted() {
		this.divShow = this.panelShow

		setTimeout(() => {
			this.firstEntry = false
		}, this.delay + 500)
	},
	methods: {
		changeDivShow(visibility) {
			this.divShow = visibility
		}
	}
}
</script>

<style lang="less" scoped>
@import "../assets/styles/components/screen-panel.less";
</style>
