<template>
	<div class="fb-admin-main" :style="getLayoutMainStyle">
		<!-- <fb-watermark :content="content" color="rgba(255, 0, 0, 1)"  
                                      :mask-opacity="1"  
                        > -->
		<transition name="admin-main-fade-transform" mode="out-in">
				<!--  确保 flatMenus 为后台页面组件按钮权限  -->
			<keep-alive :exclude="['refresh']"  >
				<router-view :key="key" v-if="flatMenus && flatMenus.length > 0"/>
			</keep-alive>
		</transition>
		<!-- </fb-watermark> -->
	</div>
</template>

<script>
	/**
	 * AdminMain
	 * (c) 2020 lincong1987
	 */
	import { mapGetters } from 'vuex'
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
			return {
				content: ['你的内容','15555555555']
			}
		},

		computed: {

			...mapGetters({
				flatMenus: 'menu/getFlatMenus'
			}),

			key () {
				return this.$route.fullPath
			},

			getLayoutMainStyle () {
				return {
					height: `${this.height - 64 - 40}px`,
					width: this.showMenu ? `${this.width - this.menuWidth}px` : `${this.width}px`,
					left: this.showMenu ? `${this.menuWidth}px` : `0px`,
				}
			},
		},
	}
</script>

<style lang="less" scoped>
@import "../../../assets/styles/layout/main.less";

</style>
