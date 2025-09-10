<template>
	<div class="fb-admin-main" :style="getLayoutMainStyle">
		<keep-alive>
			<transition name="admin-main-fade-transform" mode="out-in">
				<keep-alive>
					<router-view :key="key"/>
				</keep-alive>
			</transition>
		</keep-alive>
	</div>
</template>

<script>
	/**
	 * AdminMain
	 * (c) 2020 lincong1987
	 */

	export default {
		name: 'AdminMain',

		props: {
			routerViewKey: [String, Number],
			height: [String, Number],
			width: [String, Number],
			menuWidth:[String, Number],
			showMenu: true,
		},

		data () {
			return {}
		},

		computed: {

			key () {
				return this.$route.path
			},

			getLayoutMainStyle () {
				return {
					height: `${this.height - 64 - 40}px`,
					width: this.showMenu ? `${this.width - this.menuWidth}px` : `${this.width}px`,
					left: this.showMenu ? `200px` : `0px`,
				}
			},
		},
	}
</script>

<style lang="less" scoped>

	.fb-admin-main {
		overflow: auto;
		position:   absolute;
		top:        104px;
		left:       0;

		background: #F0EFF5;
	}

	.admin-main-fade-transform-leave-active,
	.admin-main-fade-transform-enter-active {
		transition: all .5s;
	}

	.admin-main-fade-transform-enter {
		opacity:   0;
		transform: translateY(-30px);
	}

	.admin-main-fade-transform-leave-to {
		opacity:   0;
		transform: translateY(30px);
	}


</style>
