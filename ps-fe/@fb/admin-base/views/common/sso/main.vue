<template>
	<div>

	</div>
</template>

<script>




	export default {
		name: 'sso-main',
		mixins: [

		],
		// 组件
		components: {
			// 'component-a': ComponentA,
		},
		// 创建方法
		created() {
			// 记录原来的默认值，用于表单重置
		},
		// 初始化方法
		mounted() {
			this.init();
		},
		data() {
			return {}
		},

		// 方法
		methods: {
			init() {
				// TODO 这里需要将token保存到cookie中，同时需要判断cookie中是否已经存在业务系统的token，如果已经存在，不用再次存放。

				let token = this.getQueryStr("jt");
				this.$datax.set('token', token)
				let sp = decodeURIComponent(this.getQueryStr("sp"));


				// 获取登陆人用户信息
				this.$svc.platform.getUserInfo().then(res => {
					this.$datax.set('userInfo', res.data)
					// 跳转页面
					this.$router.replace(sp)
				});
			},

			getQueryStr(str) {
				let LocString = String(window.document.location.href);

				var rs = new RegExp("(^|)" + str + "=([^&]*)(&|$)", "gi").exec(LocString), tmp;
				if (tmp = rs) {
					return tmp[2];
				}
			}
		}
	}
</script>

<style lang="less" scoped>

</style>
