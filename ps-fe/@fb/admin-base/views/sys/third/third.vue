<template>
	<div style="width: 100%; height: 100%">
		<iframe :src="srcUrl" frameborder="0" style="width: 100%; height: 100%"></iframe>
	</div>
</template>
<script>


	export default {
		name: 'auth',
		mixins: [

		],
		// 组件
		components: {
			// 'component-a': ComponentA,

		},
		// 创建方法
		created() {
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化
			this.init(this.$route.query);
		},
		data() {
			return {
				// 表单数据
				srcUrl: '',
			}
		},

		// 方法
		methods: {

			// 设置标题
			init(param) {
				this.srcUrl = decodeURIComponent(param.path);
			},
		}
	}
</script>

<style lang="less" scoped>

</style>
