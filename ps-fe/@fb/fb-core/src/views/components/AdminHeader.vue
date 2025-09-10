<template>
	<div class="fb-admin-header" :style="getLayoutHeaderStyle">

		<div class="fb-admin-header__logo">
			JPX3.0
		</div>


		<nav class="fb-admin-header__nav fb-admin-header__nav" :style="getNavStyle">

			<div class="fb-admin-header__nav_list">
				<div v-for="(menu, i) in $store.state.menu.menus"
					 @click="showSubMenus(menu)"
					 class="fb-admin-header__nav__item" :class="getNavItemClass(menu)">
					<div class="fb-admin-header__nav__item__icon">
						<fb-icon :name="menu.icon||'module'" size="xl"></fb-icon>
					</div>
					<div class="fb-admin-header__nav__item__name">{{menu.text}}</div>
				</div>
			</div>

			<div class="fb-admin-header__nav__toggle"
				 v-if="$store.state.menu.menus.length > 8"
				 @click="showFullMenus = !showFullMenus"
			>
				<fb-icon :name="showFullMenus ? 'up' : 'down'"></fb-icon>
			</div>
		</nav>



		<div class="fb-admin-header__actions">
			<div class="fb-admin-header__actions-home fb-admin-header__actions-item">
				<div class="c-box">
					<fb-icon :name="'home'"></fb-icon>
				</div>
			</div>
			<div class="fb-admin-header__actions-help fb-admin-header__actions-item">
				<div class="c-box">
					<fb-icon :name="'help'"></fb-icon>
				</div>
			</div>

			<div class="fb-admin-header__actions-notice fb-admin-header__actions-item">
				<div @click="noticePopShow = !noticePopShow" class="c-box">
					<div class="notice-tip-num" v-if="notices.num">{{notices.num}}</div>
					<fb-icon :name="'notice'"></fb-icon>
				</div>
				<fb-popup-picker v-model="noticePopShow" :position="'bottomCenter'">
					<fb-card>
						<div class="card-header">
							<fb-tabs v-model="notices.currentTab">
								<fb-tab :label="`通知公告(${notices.noticeNum})`" name="noticeList"></fb-tab>
								<fb-tab :label="`待办业务(${notices.agencyNum})`" name="agencyList"></fb-tab>
							</fb-tabs>
						</div>
						<div class="card-body">
							<fb-list-roll v-if="notices.currentTab == 'noticeList' && notices.noticeList.length > 0"
										  bodyClass="card-body-notice" mode="y">
								<div class="card-body-item" v-for="(item, idx) in notices.noticeList" :key="idx">
									<div>{{item.text}}</div>
									<p>{{item.time}}</p>
								</div>
							</fb-list-roll>
							<fb-list-roll v-if="notices.currentTab == 'agencyList' && notices.agencyList.length > 0"
										  bodyClass="card-body-agency" mode="y">
								<div class="card-body-item" v-for="(item, idx) in notices.agencyList" :key="idx">
									<div>
										<span class="name">{{item.name}}</span>
										<span class="state" :class="[`${item.state == '待复审' ? 'red' : 'yellow'}`]">{{item.state}}</span>
									</div>
									<p>{{item.text}}</p>
								</div>
							</fb-list-roll>
							<div class="card-body-nodata" v-if="notices[notices.currentTab].length == 0">
								<fb-empty v-if="notices.currentTab == 'noticeList'" type="notice"
										  text="还没有通知"></fb-empty>
								<fb-empty v-if="notices.currentTab == 'agencyList'" type="todo"
										  text="已完成所有待办"></fb-empty>
							</div>
						</div>
						<div class="card-footer clearfix">
							<span v-show="notices.currentTab == 'noticeList'">标记已读</span>
							<span>查看全部</span>
						</div>
					</fb-card>
				</fb-popup-picker>
			</div>

			<div class="fb-admin-header__actions-avatar fb-admin-header__actions-item">
				<div @click="adminPopShow = !adminPopShow" class="c-box">
					<fb-avatar circle size="24" icon="user" background-color="RGBA(92, 175, 254, 1)"></fb-avatar>
					{{userName}}
				</div>
				<fb-popup-picker v-model="adminPopShow" :position="'bottomRight'">
					<fb-card>
						<div class="card-header">
							<fb-avatar circle size="40" icon="user"
									   background-color="RGBA(92, 175, 254, 1)"></fb-avatar>
							<div>
								<p>admin</p>
								<p>系统管理员</p>
							</div>
						</div>
						<div class="card-body">
							<div class="card-item">
								<fb-icon :name="'password'"></fb-icon>
								修改密码
							</div>
							<div class="card-item">
								<fb-icon :name="'skin'"></fb-icon>
								皮肤样式
							</div>
						</div>
						<div class="card-footer">
							<div @click="logout" class="card-item">
								<fb-icon :name="'exit'"></fb-icon>
								退出登录
							</div>
						</div>
					</fb-card>
				</fb-popup-picker>
			</div>

		</div>
	</div>
</template>

<script>
	/**
	 * AdminHeader
	 * (c) 2020 lincong1987
	 */

	export default {
		name: 'AdminHeader',

		props: {
			routerViewKey: [String, Number],
			height: [String, Number],
			width: [String, Number],
			menuWidth: [String, Number],
			showMenu: true,
		},

		data () {
			return {
				showFullMenus: false,
				noticePopShow: false,
				adminPopShow: false,
				userName: '系统管理员',
				notices: {
					num: 3,
					noticeNum: 3,
					agencyNum: 4,
					currentTab: 'noticeList',
					noticeList: [
						{
							text: '这里有底色表示鼠标浮动状态这里有底色表示鼠标浮动状态这里有底色表示鼠标浮动状态这里有底色表示鼠标浮动状态',
							time: '2018-10-11',
						},
						{
							text: '这里有底色表示鼠标浮动状',
							time: '2018-10-11',
						},
					],
					agencyList: [
						// {name:'撤销缓刑审批', state: '待复审', text: '请于2019-02-24前完成李四(小银司法所)的撤销缓刑审批'},
						// {name:'撤销缓刑审批', state: '待审批', text: '请于2019-02-24前完成李四(小银司法所)的撤销缓刑审批'},
					],
				},
			}
		},

		computed: {

			getLayoutHeaderStyle () {
				return {
					// height: this.height,
					width: `${this.width}px`,
				}
			},

			getNavStyle () {

				//:style="{overflow: showFullMenus ? 'visiable': 'hidden'}"

				var arr = [
					{
						overflow: this.showFullMenus ? 'visible' : 'hidden',
					},
				]
				return arr
			},
		},

		methods: {

			getNavItemClass (menu) {
				var arr = []
				let subMenu = this.$store.state.menu.subMenu

				if (subMenu && subMenu.code && subMenu.code == menu.code) {
					arr.push('active')
				}

				return arr
			},

			logout () {

				this.$msgbox.confirm('确定要退出吗？', () => {
					this.$datax.remove('login')
					this.$datax.remove('token')
					this.$store.dispatch('admin/removeToken')
					this.$store.dispatch('menu/clear')
					this.$router.replace(this.$datax.get('GLOBAL_CONFIG').loginPath)
				})

			},

			showSubMenus (menu) {
				this.$store.dispatch('menu/subMenu', menu)
			},

		},
		mounted () {

			// this.$store.dispatch("menu/fetch")

		},
	}
</script>

<style lang="less" scoped>

	.fb-admin-header {
		position:   absolute;
		top:        0;
		left:       0;
		height:     64px;
		background: #0284FE;

		.fb-admin-header__logo {
			color:     #fff;
			font-size: 40px;
			padding:   0 20px;
		}

		.fb-admin-header__nav {
			position:   absolute;
			left:       484px;
			top:        0;
			height:     64px;
			width:      540px;
			transition: all, 0.4s;
		}
	}


	.fb-admin-header__nav_list {
		width:      540px;
		clear:      both;
		background: #0284FE;
		overflow:   hidden;

		.fb-admin-header__nav__item {
			float:      left;
			width:      64px;
			height:     64px;
			cursor:     pointer;
			transition: all, 0.6s;

			&.active,
			&:hover {
				background: rgba(255, 255, 255, 0.2);
				transform:  scale(1.05);
			}

			.fb-admin-header__nav__item__icon {
				text-align:  center;
				font-size:   20px;
				color:       #fff;
				padding-top: 8px;
			}

			.fb-admin-header__nav__item__name {
				text-align:  center;
				color:       #fff;
				height:      16px;
				line-height: 16px;
				font-size:   12px;
				padding-top: 6px;
			}

		}
	}

	.fb-admin-header__nav__toggle {
		position:      absolute;
		right:         10px;
		top:           18px;
		width:         14px;
		height:        28px;
		line-height:   28px;
		text-align:    center;
		background:    rgba(0, 0, 0, 0.2);
		border-radius: 2px;
		cursor:        pointer;
		color:         #fff;
		transition:    all, 0.6s;

		&:hover {
			background: rgba(255, 255, 255, 0.2);
		}
	}


	.fb-admin-header__actions {
		position:    absolute;
		right:       0;
		top:         0;
		padding:     0 16px;
		height:      64px;
		line-height: 64px;
		display:     flex;

		.fb-admin-header__actions-item {
			height:     100%;
			width:      44px;
			color:      #fff;
			text-align: center;
			position:   relative;

			.c-box {
				position: relative;

				&:hover {
					cursor:     pointer;
					background: rgba(255, 255, 255, 0.2);
				}
			}
		}

		.fb-admin-header__actions-notice {
			.c-box {
				.notice-tip-num {
					position:      absolute;
					top:           14px;
					right:         3px;
					width:         16px;
					height:        16px;
					line-height:   16px;
					border-radius: 50%;
					background:    red;
					font-size:     12px;
				}
			}

			.fb-popup-picker {
				line-height: initial;
				width:       300px;

				.fb-card {
					/deep/ .fb-card__body {
						padding:    0;
						color:      #666666;
						text-align: left;

						.card-header {
							border-bottom: 1px solid #E8E8E8;

							.fb-tabs-header-label {
								width:      126px;
								text-align: center;
							}

							.fb-tabs-buttons {
								display: none;
							}
						}

						.card-body {
							height:     315px;
							overflow-y: auto;

							.card-body-item {
								padding:       8px 16px;
								border-bottom: 1px solid #E8E8E8;
								cursor:        pointer;
								transition:    all .3s;

								&:hover, &.active {
									background: rgba(230, 247, 255, 1);
								}
							}

							.card-body-notice {
								.card-body-item {
									> div {
										display:            -webkit-box;
										-webkit-box-orient: vertical;
										-webkit-line-clamp: 2;
										overflow:           hidden;
										color:              #313C47;
										font-size:          13px;
										line-height:        22px;

										&:after {
											content:       '';
											display:       inline-block;
											width:         8px;
											height:        8px;
											border-radius: 50%;
											background:    #FB544E;
											margin-left:   8px;
										}
									}

									> p {
										margin-top:  4px;
										font-size:   12px;
										color:       #666666;
										line-height: 20px;
									}
								}
							}

							.card-body-agency {
								.card-body-item {
									> div {
										display:         flex;
										justify-content: space-between;
										color:           #313C47;
										font-size:       13px;
										line-height:     22px;

										.state {
											font-size:     12px;
											padding:       0px 8px;
											border:        1px solid #ccc;
											border-radius: 4px;

											&.red {
												color:        #FB544E;
												border-color: #FB544E;
											}

											&.yellow {
												color:        #FFB500;
												border-color: #FFB500;
											}
										}
									}

									> p {
										margin-top:  4px;
										font-size:   12px;
										color:       #666666;
										line-height: 20px;
									}
								}
							}

							.card-body-nodata {
								.fb-empty {
									margin-top: 110px;
								}
							}
						}

						.card-footer {
							padding:    9px 16px;
							border-top: 1px solid #E8E8E8;
							color:      #313C47;

							span {
								transition: all .3s;
								float:      left;

								&:hover {
									color:  #0284FE;
									cursor: pointer;
								}

								&:last-child {
									float: right;
								}
							}
						}
					}
				}
			}
		}

		.fb-admin-header__actions-avatar {
			width: auto;

			.c-box {
				padding: 0 16px;

				.fb-avatar {
					margin-right: 4px;
					margin-top:   -2px;
				}
			}

			.fb-popup-picker {
				width: 160px;

				.fb-card {
					/deep/ .fb-card__body {
						padding:    0;
						color:      #666666;
						text-align: left;

						.card-header {
							padding:       10px;
							border-bottom: 1px solid #E8E8E8;
							display:       flex;

							.fb-avatar {
								margin-right: 10px;
							}

							p {
								margin: 0;
							}
						}

						.card-item {
							padding:     0 16px;
							height:      32px;
							line-height: 32px;

							.fb-icon {
								margin-right: 6px;
							}

							&:hover {
								cursor:     pointer;
								background: #E6F7FF;
							}
						}

						.card-footer {
							padding:    5px 0;
							border-top: 1px solid #E8E8E8;
						}
					}
				}
			}
		}

	}
</style>
