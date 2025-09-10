<template>
	<div class="fb-admin-tabbar" :style="getLayoutTabbarStyle">
		<fb-tabs v-model="current" closable
				 ref="tabbar"
				 :before-remove="handleBeforeRemove"
				 @on-tab-remove="handleTabRemove"
				 @on-tab-choose="handleTabChoose"
				 @on-tab-change="handleTabChange"
		>
			<fb-tab v-for="(tab, index) in  $store.state.tabbar.tabs" :label="tab.text" :name="tab.id"
					:key="tab.id"></fb-tab>
		</fb-tabs>


	</div>
</template>

<script>
	/**
	 * AdminTabbar
	 * (c) 2020 lincong1987
	 */
	import { mapState, mapMutations } from 'vuex'
	import { findIndex } from 'lodash-es'

	export default {
		name: 'AdminTabbar',

		props: {
			routerViewKey: [String, Number],
			height: [String, Number],
			width: [String, Number],
			menuWidth: [String, Number],
			showMenu: true,

		},

		data () {
			return {
				scroll: false,

				current: 1,

				tabs: [],
			}
		},

		computed: {
			...mapState(['menu']),

			getLayoutTabbarStyle () {
				return {
					// height: this.height,
					left: this.showMenu ? `200px` : `0px`,
					width: this.showMenu ? `${this.width - this.menuWidth}px` : `${this.width}px`,
				}
			},

			getContainerClass () {

				var arr = ['fb-admin-tabbar__container']

				if (this.scroll) {
					arr.push('fb-admin-tabbar__container--scroll')
				}

				return arr

			},

			getTabbarStyle () {
				let arr = []
				return arr

			},

		},

		methods: {

			handleBeforeRemove (id, index) {
				return new Promise((resolve, reject) => {
					if (this.$store.state.tabbar.tabs.length<=1) {
						this.$message.warn('不能关闭所有的页面')
						resolve(false)
					} else {
						resolve(true)
					}
				})
			},

			handleTabRemove (id, index) {
				console.log('删除了', index)

				console.log(this.$store.state.tabbar.tabs[index].path)
				if (index != -1) {
					this.$store.state.tabbar.tabs.splice(index, 1)
				}
			},
			handleTabChoose (tab, index) {
				console.log('选择了', index)
				if (this.$route.path == tab.path) {
					return
				}
				if (this.$store.state.tabbar.tabs[index] && this.$store.state.tabbar.tabs[index].path) {
					console.log(this.$store.state.tabbar.tabs[index].path)

					this.$router.push(this.$store.state.tabbar.tabs[index].path)
				}

//				this.$router.push()
			},

			handleTabChange () {
				this.$refs.tabbar.scrollNext()
			},

			refresh (tab, i) {
				this.$router.push(tab.path + '?_t=' + new Date().getTime())
			},
		},

	}
</script>

<style lang="less">

	.fb-admin-tabbar {
		position: absolute;
		top:      64px;
		left:     0;
		height:   40px;

		.fb-tabs {
			.fb-tabs-header {
				.fb-tabs-header-container {
					.fb-tabs-header-item {

						min-width:   80px;
						padding:     0;
						user-select: none;

						.fb-tabs-header-label {
							height:      40px;
							line-height: 40px;
							user-select: none;
						}

						.button-close {
							transform: scale(0.8);
							top:       14px;
							right:     8px;
							color:     rgba(61, 61, 61, 1);

							&:hover {
								color:     rgba(2, 132, 254, 1);
								transform: scale(0.9);
							}
						}

						&:after {
							content:    "";
							position:   absolute;
							width:      1px;
							height:     24px;
							background: #E8E8E8;
							top:        8px;
							right:      0;
						}


						&:hover {

							.fb-tabs-header-label {
								&:after {
									background: rgba(2, 132, 254, 0.8);
									width: 100%;
								}
							}

						}
					}
				}

			}
		}


		.fb-admin-tabbar__wrap {
			white-space: nowrap;
			float:       left;
			transition:  .5s cubic-bezier(.645, .045, .355, 1);

			.fb-admin-tabbar__item {
				display:     table;
				height:      40px;
				line-height: 40px;
				position:    relative;
				padding:     0 12px 0 16px;
				cursor:      pointer;
				user-select: none;

				&:before {
					position:   absolute;
					height:     3px;
					content:    "";
					width:      100%;
					left:       0;
					bottom:     -3px;
					background: #0284FE;
					opacity:    0;
					transform:  scale(0);
					transition: all .4s;
				}

				.fb-admin-tabbar__item__label {
					display: table-cell;
					color:   #0284FE;

					.fb-admin-tabbar__item__label__icon {

					}
				}

				.fb-admin-tabbar__item__close {
					display:    table-cell;
					cursor:     pointer;
					text-align: right;
					width:      20px;
				}


				&.active {
					&:before {
						transform: scale(1);
						opacity:   1;
						bottom:    0;
					}
				}

			}

		}
	}

</style>
