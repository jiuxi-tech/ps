<template>
	<div class="refresh-container">
		<div class="refresh-loading" v-if="isRefreshing">
			<fb-spin size="large">
				<div class="refresh-text">页面刷新中...</div>
			</fb-spin>
		</div>
	</div>
</template>

<script>
	export default {
		name: 'refresh',
		data() {
			return {
				isRefreshing: true,
				refreshTimer: null
			}
		},
		beforeRouteEnter (to, from, next) {
			// 添加延迟以显示加载状态，提升用户体验
			next(vm => {
				// 设置最小加载时间，避免闪烁
				vm.refreshTimer = setTimeout(() => {
					try {
						// 优先使用路由名称进行跳转，保持路由状态
						if (from.name && Object.keys(to.params).length) {
							vm.$router.replace({
								name: from.name,
								params: to.params,
								query: from.query || {} // 保持原有查询参数
							})
						} else if (from.path) {
							vm.$router.replace({
								path: from.path,
								query: to.query || from.query || {}
							})
						} else {
							// 兜底处理：如果没有来源路由，跳转到首页
							console.warn('刷新页面时未找到来源路由，跳转到首页')
							vm.$router.replace('/')
						}
					} catch (error) {
						console.error('页面刷新时发生错误:', error)
						// 错误处理：跳转到首页
						vm.$router.replace('/')
					} finally {
						vm.isRefreshing = false
					}
				}, 300) // 300ms的最小加载时间
			})
		},
		beforeDestroy() {
			// 清理定时器
			if (this.refreshTimer) {
				clearTimeout(this.refreshTimer)
			}
		}
	}
</script>

<style lang="less" scoped>
.refresh-container {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background-color: rgba(255, 255, 255, 0.8);
	display: flex;
	align-items: center;
	justify-content: center;
	z-index: 9999;

	.refresh-loading {
		text-align: center;

		.refresh-text {
			margin-top: 16px;
			font-size: 14px;
			color: #666;
		}
	}
}
</style>
