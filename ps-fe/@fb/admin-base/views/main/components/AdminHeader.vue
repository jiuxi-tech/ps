<template>
	<div class="fb-admin-header" :style="getLayoutHeaderStyle">
		<!-- 配置加载状态 -->
		<div v-if="configLoading" class="fb-admin-header__logo config-loading">
			<fb-icon name="loading" size="16"></fb-icon> 加载中...
		</div>
		<!-- 配置错误状态 -->
		<div v-else-if="configError" class="fb-admin-header__logo config-error">
			<fb-icon name="warning" size="16"></fb-icon> 配置加载失败
			<fb-button size="xs" type="link" @on-click="loadSystemConfig">重试</fb-button>
		</div>
		<!-- 正常显示 -->
		<div v-else class="fb-admin-header__logo">
			<img v-if="logoUrl" :src="logoUrl" alt="Logo" class="logo-image" />
			{{ logoTitle }}
		</div>
		<nav class="fb-admin-header__nav fb-admin-header__nav" :style="getNavStyle"

			 @mouseenter="showFullMenus = true"
			 @mouseleave="showFullMenus = false"
		>

			<div class="fb-admin-header__nav_list">
				<!--                <div data-v-82dce4e2="" class="fb-admin-header__nav__item">-->
				<!--                    <div data-v-82dce4e2="" class="jpx-tooltip item" aria-describedby="jpx-tooltip-3433" tabindex="0">-->
				<!--                        <div data-v-82dce4e2="" class="fb-admin-header__nav__item__icon">-->
				<!--                            <i data-v-3051a704="" data-v-82dce4e2="" class="jpx-icon jpx-icon-home jpx-icon-xl"-->
				<!--                               style="font-size: 20px;"></i>-->
				<!--                        </div>-->
				<!--                        &lt;!&ndash; <div data-v-82dce4e2="" class="fb-admin-header__nav__item__name">首页</div> &ndash;&gt;-->
				<!--                    </div>-->
				<!--                </div>-->
				<div v-for="menu in filteredMenus" :key="menu.id || menu.code"
					 @click="showSubMenus(menu)"
					 class="fb-admin-header__nav__item" :class="getNavItemClass(menu)">


					<!--                    <fb-tooltip class="item" effect="dark" :content="menu.text" placement="bottom">-->
					<!--                        -->
					<!--                    </fb-tooltip>-->

					<div :title="menu.text">
						<div class="fb-admin-header__nav__item__icon">
							<fb-icon :name="menu.icon||'module'" size="xl"></fb-icon>
						</div>
						<div v-if="menu.text.length >= 5" class="fb-admin-header__nav__item__name">
							{{ menu.text.substring(0, 5) }}
						</div>
						<div v-else class="fb-admin-header__nav__item__name">{{ menu.text }}</div>
					</div>
				</div>
			</div>

			<!-- <div class="fb-admin-header__nav__toggle"

			>
				<fb-icon size="12" :name="showFullMenus ? 'up' : 'down'"></fb-icon>
			</div> -->
		</nav>
		<div class="fb-admin-header__actions">
			<!-- <div class="fb-admin-header__actions-home">
				<fb-button v-permission="'BIG_SCREEN'" @on-click="backScreen" icon="chart-gauge-fill" round size="l"
						   style="background: linear-gradient(90deg, #5AAF52, #3E8E58);border: none;color: #fff">驾驶舱
				</fb-button>
			</div> -->
			<div class="fb-admin-header__actions-item" v-if="false">
				<div @click="openHelp" class="c-box">
					<fb-icon :name="'map'" size="16"></fb-icon>
				</div>
			</div>

			<div  v-if="false" class="fb-admin-header__actions-notice fb-admin-header__actions-item">
				<div @click="noticePopShow = !noticePopShow" class="c-box">
					<fb-badge :count="notices.noticeNum" countSize="s">
						<fb-icon :name="'bell'" size="16"></fb-icon>
					</fb-badge>
				</div>
				<fb-popup-picker v-model="noticePopShow" :position="'bottomRight'">
					<fb-card>
						<div class="card-header">
							<fb-tabs v-model="notices.currentTab">
								<fb-tab :label="`重要信息(${notices.noticeNum})`" name="noticeList"></fb-tab>
							</fb-tabs>
						</div>
						<div class="card-body">
							<fb-list-roll v-if="notices.currentTab == 'noticeList' && notices.noticeList.length > 0"
										  bodyClass="card-body-notice" mode="y">
								<div class="card-body-item" v-for="(item, idx) in notices.noticeList" :key="idx">
									<div>{{ item.text }}</div>
									<p>{{ item.time }}</p>
									<fb-container absolute right="8px" top="8px">
										<fb-button type="primary" @on-click="replayNotice(item)">回复</fb-button>
									</fb-container>
								</div>
							</fb-list-roll>
							<div class="card-body-nodata" v-if="notices[notices.currentTab].length == 0">
								<fb-empty v-if="notices.currentTab == 'noticeList'" type="notice"
										  text="还没有通知"></fb-empty>
								<!--								<fb-empty v-if="notices.currentTab == 'agencyList'" type="todo"
																		  text="已完成所有待办"></fb-empty>-->
							</div>
						</div>
						<div class="card-footer clearfix">
							<!--							<span v-show="notices.currentTab == 'noticeList'">标记已读</span>-->
							<!--							<span>  </span>-->
							<fb-link v-if="notices.noticeNum != 0" label="查看全部" :click="handleClick"></fb-link>
						</div>
					</fb-card>
				</fb-popup-picker>
			</div>

			<div class="fb-admin-header__actions-avatar fb-admin-header__actions-item">
				<div @click="adminPopShow = !adminPopShow" class="c-box">
					<fb-avatar circle size="24" icon="user" background-color="#C5D0FC"></fb-avatar>
					{{ userName }}
				</div>
				<fb-popup-picker v-model="adminPopShow" :position="'bottomRight'">
					<fb-card>
						<div class="card-header">
							<fb-avatar circle size="40" icon="user"
									   background-color="#C5D0FC"></fb-avatar>
							<div>
								<p>{{ userId }}</p>
								<p>{{ userName }}</p>
							</div>
							<!-- <p>{{ userDepts }}</p> -->
						</div>

						<div class="card-body">
							<!--							<div class="card-item" @click="updateUserInfo">-->
							<!--								<fb-icon :name="'user-edit'"></fb-icon>-->
							<!--								修改个人信息-->
							<!--							</div>-->
							<div class="card-item" @click="updatePwd">
								<fb-icon :name="'password'"></fb-icon>
								修改密码
							</div>
							<!--							<div class="card-item" @click="$refs.styleForm.show()">-->
							<!--								<fb-icon :name="'skin'"></fb-icon>-->
							<!--								皮肤样式-->
							<!--							</div>-->
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
		<fb-drawer ref="styleForm" title="皮肤样式" position="right" width="500" :esc="false">
			<fb-button slot="actions" @on-click="$refs.styleForm.hide()" round size="m" type="link"
					   icon="close"></fb-button>
			<fb-form label-position="top">

				<fb-fieldset label="外观"></fb-fieldset>

				<fb-row flex justify="start">
					<fb-col span="6">
						<img src="../../../assets/styles/theme/classic_blue/thumbnail.png"
							 style="cursor: pointer" @click="updateStyle('classic_blue')"/>
						<fb-text align="center" width="128px" ellipsis>经典蓝</fb-text>
						<img src="../../../assets/styles/theme/current.png" class="current"
							 v-if='this.theme === "classic_blue"'
						/>
					</fb-col>
					<fb-col span="6" offset="2">
						<img src="../../../assets/styles/theme/sky_blue/thumbnail.png"
							 style="cursor: pointer" @click="updateStyle('sky_blue')"/>
						<fb-text align="center" width="128px" ellipsis>天空蓝</fb-text>
						<img src="../../../assets/styles/theme/current.png" class="current"
							 v-if='this.theme === "sky_blue"'
						/>
					</fb-col>
					<fb-col span="6" offset="2">
						<img src="../../../assets/styles/theme/sea_blue/thumbnail.png"
							 style="cursor: pointer" @click="updateStyle('sea_blue')"/>
						<fb-text align="center" width="128px" ellipsis>深海蓝</fb-text>
						<img src="../../../assets/styles/theme/current.png" class="current"
							 v-if='this.theme === "sea_blue"'
						/>
					</fb-col>
				</fb-row>
				<fb-row flex justify="start">
					<fb-col span="6">
					</fb-col>
					<fb-col span="6" offset="2">
					</fb-col>
					<fb-col span="6" offset="2">
					</fb-col>
				</fb-row>
				<fb-row flex justify="start">
					<fb-col span="6">
					</fb-col>
					<fb-col span="6" offset="2">
					</fb-col>
					<fb-col span="6" offset="2">
					</fb-col>
				</fb-row>
			</fb-form>
		</fb-drawer>
		<tp-dialog ref="TpDialog"></tp-dialog>
		<tp-dialog ref="TpDialog1" @closeTpDialog="closeDialog"></tp-dialog>
		<tp-dialog ref="TpDialog2" @closeTpDialog="closeDialog"></tp-dialog>
	</div>
</template>

<script>

import Page from '@fb/tp-components/src/util/Page'
// import TpDialog from '@fb/tp-components/src/components/tp-dialog/index'
import clickoutside from '@fb/fb-ui/packages/directives/clickoutside'

/**
 * AdminHeader
 * (c) 2020 lincong1987
 */

export default {
	name: 'AdminHeader',

	mixins: [
		Page,
	],

	directives: {
		// 'directive-a': DirectiveA,
		clickoutside: clickoutside,
	},

	props: {
		routerViewKey: [String, Number],
		height: [String, Number],
		width: [String, Number],
		menuWidth: [String, Number],
		showMenu: true,
	},

	// 组件
	components: {
		// 'component-a': ComponentA,
		// TpDialog,
	},

	data() {
		let logoTitle = app.projectConfig.logoTitle || '系统管理'

		if(window._publicConfig && window._publicConfig.logoTitle) {
			logoTitle = window._publicConfig.logoTitle
		}

		return {
			logoTitle,
			logoUrl: '',
			configLoading: false,
			configError: false,
			showFullMenus: false,
			noticePopShow: false,
			adminPopShow: false,
			userId: '',
			ascnId: '',
			userName: '用户名',
			userDepts: '所在部门',
			notices: {
				noticeNum: 0,
				currentTab: 'noticeList',
				noticeList: [],
			},
			theme: 'sea_blue',
			badgeList: [
				// {
				// 	name: 'GKCSLS',
				// 	count: 0,
				// 	title: '管控措施落实',
				// 	valName: 'GKCSLS',
				// }
				// ,
				// {
				// 	name: 'QYZDTB',
				// 	count: 0,
				// 	title: '企业诊断填报',
				// 	valName: 'QYZDTB',
				// }
				// , {
				// 	name: 'ZS_ENT_YQYD',
				// 	count: 0,
				// 	title: '企业信息',
				// 	valName: 'ZS_ENT_YQYD',
				// }
				// ,
				// {
				// 	name: 'ZXZZ_S_SR_SPECIALCHECK_ENT',
				// 	count: 0,
				// 	title: '专项整治检查',
				// 	valName: 'ZXZZ_S_SR_SPECIALCHECK_ENT',
				// }
				// ,
				// {
				// 	name: 'CUSTOM_S_HD_HIDDENDANGERINFO_LIST_NEATEN',
				// 	count: 0,
				// 	title: '隐患整改',
				// 	valName: 'CUSTOM_S_HD_HIDDENDANGERINFO_LIST_NEATEN',
				// }
			],
		}
	},

	computed: {
		filteredMenus() {
			return this.$store.state.menu.menus.filter(menu => menu.code !== 'BIG_SCREEN')
		},
		getLayoutHeaderStyle() {
			return {
				// height: this.height,
				width: `${this.width}px`,
			}
		},
		getNavStyle() {
			//:style="{overflow: showFullMenus ? 'visiable': 'hidden'}"
			var arr = [
				{
					overflow: this.showFullMenus ? 'visible' : 'hidden',
				},
			]
			return arr
		},
	},
	watch: {
		'$route': {
			handler() {
				this.initTopMenuChange()
			},
		}
	},
	methods: {
		init() {
			// 用户信息
			let userinfo = this.$datax.get('userInfo')
			this.userName = userinfo.personName
			this.userId = userinfo.userName
			this.ascnId = userinfo.ascnId


		 

			//this.messageRemindlist();
			//用户所在单位、安委办、安委会
			//this.getUserDeptList()
			// 通知公告

			//企业
			// if(userinfo.category === 1) {
			// 	this.handleEntComplete()
			// }
			// 实现轮询
			/*this.heartbeat()*/
//			window.setInterval(() => {
//
//			}, 300000)
	this.heartbeat()
			this.heartbeatTimer= setTimeout(() => {
				this.heartbeat()
			}, 300000)
		},
		// 心跳 5 分钟一次
		heartbeat() {
			let token = app.$datax.get('token')
			if (!token) {
				console.warn('[心跳] TOKEN不存在，跳过本次心跳')
				clearTimeout(this.heartbeatTimer)
				this.heartbeatTimer = setTimeout(() => {
					this.heartbeat()
				}, 300000)
				return
			}
			
			// 改用axios请求以支持TOKEN自动刷新
			app.service.get('/platform/stationline/heartbeat', {
				params: { jt: token },
				loading: false  // 关键：不显示loading，避免影响用户体验
			}).then(response => {
				// 响应拦截器会自动处理newToken更新
				if (response && response.data && response.data.newToken) {
					console.log('[心跳] TOKEN已自动刷新')
				}
			}).catch(error => {
				// 心跳失败不影响用户操作，仅记录日志
				console.error('[心跳] 心跳请求失败', error)
			})
			
			clearTimeout(this.heartbeatTimer)
			this.heartbeatTimer = setTimeout(() => {
				this.heartbeat()
			}, 300000)
		},
		updateUserInfo() {
			let that = this
			//	修改密码
			let param = {}
			let options = {
				'height': 300, // 弹出框高度
				'top': '15vh', // 控制弹出框上边 距离屏幕的的百分比
				callBack: function(result) {
				},
			}
			this.$refs.TpDialog.show('/main/components/UpdateUserInfo.vue', param, '个人信息修改', options)
		},
		updatePwd() {
			let that = this
			//	修改密码
			let param = {}
			let options = {
				'height': 300, // 弹出框高度
				'top': '15vh', // 控制弹出框上边 距离屏幕的的百分比
				callBack: function(result) {
				},
			}
			this.$refs.TpDialog.show(import('./UpdatePwd.vue'), param, '修改密码', options)
		},
		handleClick() {
			let that = this;
			let param = {
				ascnId: this.ascnId
			}
			let options = {
				"top": '15vh', // 控制弹出框上边 距离屏幕的的百分比
				callBack: function(result) {
					that.messageRemindlist()
				}
			}
			this.$refs.TpDialog.show('/sys/ssysnoticereceive/list-dialog.vue', param, '信息回复', options)
		},
		updateStyle(color) {
			this.theme = color
			this.$datax.set('theme', color)

			this.$emit('change-theme', color)
		},
		getNavItemClass(menu) {
			var arr = []
			let subMenu = this.$store.state.menu.subMenu

			if(subMenu && subMenu.code && subMenu.code === menu.code) {
				arr.push('active')
			}

			return arr
		},

		logout() {
			let removeDatax = ['login', 'token', 'TOKEN_REAL', 'userInfo']
			let removeStore = ['admin/removeToken', 'menu/clear', 'tabbar/clear']
			this.$msgbox.confirm('确定要退出吗？', () => {
				this.logoutLog();
				app.trigger('monitor-login-out', {
					removeDatax,
					removeStore,
				})
				removeDatax.forEach((key) => {
					this.$datax.remove(key)
				})
				removeStore.forEach((key) => {
					this.$store.dispatch(key)
				})

				// 检查Keycloak登录状态，如果已登录则先退出
				if (this.$kc && this.$kc().authenticated) {
					this.$kc().clearToken()
					this.$kc().logout(`${window.location.origin}${this.$datax.get('GLOBAL_CONFIG').loginPath}`).then(() => {
						this.$router.replace(this.$datax.get('GLOBAL_CONFIG').loginPath)
					}).catch((error) => {
						console.error('Keycloak logout failed:', error)
						// 即使Keycloak退出失败，也继续跳转到登录页
						this.$router.replace(this.$datax.get('GLOBAL_CONFIG').loginPath)
					})
				} else {
					this.$router.replace(this.$datax.get('GLOBAL_CONFIG').loginPath)
				}
			})

		},
		logoutLog() {
			// 日志埋点
			let data = {
				// 模块编码
				moduleCode: 'login',
				// 操作类型： login/logout/add/delete/edit/query/pass/unpass, 可以自己定义
				operterType: 'logout',
			}

			// 日志埋点
			this.$logger.send(data)
		},

		 
		showSubMenus(menu) {
			this.$store.dispatch('menu/subMenu', menu)

			if(menu.value == '1874733105751785472'
				|| menu.value == '1874733191533690880'
				|| menu.value == '1874733361285562368'
				|| menu.value == '1876514437976817664'
				|| menu.value == '1879082348423675904'
			) {
				//this.getWorkCount()
			}
		},
		replayNotice(item) {
			let that = this;
			let param = {
				noticeReceiveId: item.id,
				noticeId: item.pid,
			}
			let options = {
				"top": '15vh', // 控制弹出框上边 距离屏幕的的百分比
				callBack: function(result) {
					that.messageRemindlist()
				}
			}
			this.$refs.TpDialog.show('/sys/ssysnoticereceive/replies.vue', param, '信息回复', options)
		},
		// 通知公告提醒查询
		messageRemindlist() {
			let that = this;
			this.$svc.sys.ssysnoticereceive.searchRemind().then(res => {
				if(res.code === 1) {

					let list = [];
					res.data.forEach(val => {
						list.push({
							'id': val.noticeReceiveId,
							'pid': val.noticeId,
							'text': val.noticeTitle,
							'time': that.formatDate(val.issueTime, "YYYY-MM-DD")
						});
					})
					that.notices.noticeList = list;
					that.notices.noticeNum = list.length;
				}
			})
		},
		// 动态加载系统配置
		async loadSystemConfig() {
			this.configLoading = true
			this.configError = false
			
			try {
				// 创建超时Promise
				const timeoutPromise = new Promise((_, reject) => {
					setTimeout(() => reject(new Error('请求超时')), 10000)
				})
				
				// 并行获取配置
				const configPromises = [
					this.$svc.sys.config.getConfigValue('admin.title'),
					this.$svc.sys.config.getConfigValue('admin.logo')
				]
				
				// 使用Promise.race实现超时控制
				const results = await Promise.race([
					Promise.all(configPromises),
					timeoutPromise
				])
				
				const [titleResult, logoResult] = results
				
				// 更新配置数据
				if (titleResult && titleResult.data) {
					this.logoTitle = titleResult.data
				}
				
				if (logoResult && logoResult.data) {
					this.logoUrl = logoResult.data
				}
				
			} catch (error) {
				console.warn('加载系统配置失败，使用默认配置:', error)
				this.configError = true
			} finally {
				this.configLoading = false
			}
		},
		getUserDeptList() {
			// this.$svc.common.list().then((result) => {
			// 	if (result.code === 1 && result.data) {
			// 		this.userDepts = result.data.map(item => item.deptFullName).join('、')
			// 	}
			// })
		},

		//部署落实- 任务审核，任务办结 小红数字接口
		getWorkCount() {
			//判断有无待办提示
			this.$svc.sys.ssysnotice.getWorkCount().then(result => {
				if(result.code == 1) {
					this.badgeList.forEach(item => {
						if(result.data[item.valName] && item.name === 'GKCSLS') {
							item.count = result.data[item.valName]
						}
						if(result.data[item.valName] && item.name === 'ZXZZ_S_SR_SPECIALCHECK_ENT') {
							item.count = result.data[item.valName]
						}
						if(result.data[item.valName] && item.name === 'QYZDTB') {
							item.count = result.data[item.valName]
						}
						if(result.data[item.valName] && item.name === 'CUSTOM_S_HD_HIDDENDANGERINFO_LIST_NEATEN') {
							item.count = result.data[item.valName]
						}
						if(result.data[item.valName] && item.name === 'ZS_ENT_YQYD') {
							item.count = result.data[item.valName]
						}
					})
					app.trigger('on-setBadgeList', this.badgeList)
				}
			})
		},
		//部署落实- 任务签收，任务反馈 小红数字接口
		mainFeedCount() {

			//判断有无待办提示
			this.$svc.manage.work.mainFeedCount().then((result) => {

				// console.log("部署落实处理状态统计值workCount：" + result.toString())
				// result = {
				//   "msg": "操作成功",
				//   "code": 200,
				//   "data": {
				//
				//     "feedStatusSigning": 3,
				//     "feedStatusFeeding": 4,
				//   },
				//   "success": true
				// }
				if(result.code === 200 && result.data) {
					this.badgeList.forEach(item => {
						if(result.data[item.valName] && item.name === 'BSLS_RWQS') {
							item.count = result.data[item.valName]
						}
						if(result.data[item.valName] && item.name === 'BSLS_JDFK') {
							item.count = result.data[item.valName]
						}

					})
					app.trigger('on-setBadgeList', this.badgeList)
				}
			})
		},
		//部署落实- 派单签收，派单反馈 小红数字接口
		noticeFeedCount() {
			//判断有无待办提示
			this.$svc.manage.work.noticeFeedCount().then((result) => {

				if(result.code === 200 && result.data) {
					this.badgeList.forEach(item => {
						if(result.data[item.valName] && item.name === 'BSLS_PDGL_QS') {
							item.count = result.data[item.valName]
						}
						if(result.data[item.valName] && item.name === 'BSLS_PDGL_PDFK') {
							item.count = result.data[item.valName]
						}

					})
					app.trigger('on-setBadgeList', this.badgeList)
				}
			})
		},
		handlePointOut() {
			let param = {}
			//判断有无待办提示
			this.$svc.sys.dept.pointOut().then((result) => {
				if(result.code === 1) {
					if(result.data) {
						let options = {
							width: 600,
							height: 250,
							top: '15vh', // 控制弹出框上边 距离屏幕的的百分比
						}
						param = {
							data: result.data,
						}
						if(result.data.notFeedbackCount > 0 || result.data.notBranchHandleCount > 0 ||
							result.data.notCcInfoCount > 0) {
							// 有待办提示的弹出
							setTimeout(() => {
								this.$refs.TpDialog2.show('/main/components/PointOut.vue', param, '待办提示', options)
							}, 500)

						}

					}
				} else {
					// 服务器返回失败
					//this.$message.error('错误提示:' + result.message)
				}
			})

		},
		handleEntComplete() {
			let param = {}
			let options = {
				showCloseBtn: false,
			}
			//  判断企业信息是否完善，未完成，弹出完善信息页面
			this.$svc.yqyd.enterprise.view({'entId': this.$datax.get('userInfo').ascnId}).then((result) => {
				if(result.code === 1) {
					if(result.data.extend01 === '00') {
						// 企业注册后需完善信息
						this.$refs.TpDialog1.show('/yqyd/enterprise/ent-complete.vue', param, '企业信息完善', options)
					}
				} else {
					// 服务器返回失败
					this.$message.error('错误提示:' + result.message)
				}
			})
		},

		closeDialog(param) {
			console.log(param)
		},
		backScreen() {
			window.open(app.projectConfig.router.screenPath)
		},
		openHelp() {
			let param = {}
			this.$refs.TpDialog.show('/main/common/SystemHelpInfo.vue', param, '工贸行业分类分级说明')
		},
		initTopMenuChange() {
			const flatMenus = this.$store.state.menu.flatMenus
			flatMenus.forEach(menu => {
				if(menu.path === this.$route.path) {

					let topMenu = this.findTopMenu(flatMenus, menu.pid)
					if(topMenu) {
						this.showSubMenus(topMenu)
					}
					return
				}
			})
		},
		findTopMenu(flatMenus, pid) {
			for(let i in flatMenus) {
				let menu = flatMenus[i]
				if(menu.id === pid && menu.pid === '-1' && menu.code !== 'SCREEN_MENU') {
					return menu
				}
				if(menu.id === pid) {
					return this.findTopMenu(flatMenus, menu.pid)
				}
			}
		},
	},
	mounted() {
		this.initTopMenuChange()
		this.init()
		this.updateStyle(this.$datax.get('theme') || this.theme)
		this.loadSystemConfig()
		// setTimeout(() => {
		// 	//this.getWorkCount()
		// }, 100)
	},
	activated() {
		this.init()
	},

}
</script>

<style lang="less">
@import "../../../assets/styles/common.less";

.fb-admin-header {
	position: absolute;
	top: 0;
	left: 0;
	height: 64px;
	//background: url("../../../assets/img/main/header_right_bg.png") right top no-repeat #183FBF;
	display: flex;


	.fb-admin-header__logo {

		height: 100%;
		font-size: clamp(16px, 2.5vw, 22px);
		font-weight: 700;
		color: #FFFFFF;
		line-height: 24px;
		text-shadow: 0px 0px 6px rgba(0, 21, 41, 0.1);
		padding-top: 0px;
		//padding-left: 72px;
		padding-left: clamp(12px, 1.5vw, 16px);
		padding-right: clamp(16px, 2vw, 22px);
		position: relative;
		width: clamp(360px, 30vw, 480px);
		display: flex;
		align-items: center;
		gap: clamp(12px, 1.5vw, 16px);

		img {
			height: clamp(24px, 3vw, 32px);
			width: auto;
		}

		//&:before {
		//    content:         "";
		//    display:         block;
		//    width:           44px;
		//    height:          40px;
		//    position:        absolute;
		//    top:             13px;
		//    left:            16px;
		//    background:      url("../../../assets/img/login/logo-icon.png") center no-repeat;
		//    background-size: 100% 100%;
		//}
	}

	.fb-admin-header__nav {
		position: relative;
		//left:       484px;
		top: 0;
		height: 64px;
		transition: all 0.4s;
		flex: 1;

	}

	.fb-admin-header__actions {
		position: relative;
		width: unset;
	}
}


.fb-admin-header__nav_list {
	width: unset;
	clear: both;
	overflow: hidden;
	//mix-blend-mode: lighten;
	// background: linear-gradient(#183fbf 0px, #183fbf 64px, #13297e);
	//background: linear-gradient(#0284FE 0px, #0284FE 64px, #3858d1);
	border-radius: 0 0 8px 8px;

	.fb-admin-header__nav__item {
		float: left;
		width: 72px;
		height: 64px;
		cursor: pointer;
		transition: all 0.6s;
		user-select: none;

		&.active,
		&:hover {
			background: rgba(255, 255, 255, 0.2);
		}

		.fb-admin-header__nav__item__icon {
			text-align: center;
			font-size: 20px;
			color: #fff;
			padding-top: 8px;
			transition: all 0.6s;
		}

		.fb-admin-header__nav__item__name {
			text-align: center;
			color: #fff;
			height: 16px;
			line-height: 16px;
			font-size: 14px;
			padding-top: 6px;
			transition: all 0.6s;

		}

	}
}

.fb-admin-header__nav__toggle {
	position: absolute;
	right: 0px;
	top: 18px;
	width: 14px;
	height: 28px;
	background: rgba(0, 0, 0, 0.2);
	border-radius: 2px;
	cursor: pointer;
	color: #fff;
	transition: all 0.6s;
	display: flex;
	align-items: center;
	justify-content: center;

	&:hover {
		background: rgba(255, 255, 255, 0.2);
	}
}


.fb-admin-header__actions {
	position: absolute;
	right: 0;
	top: 0;
	padding: 0 0 0 16px;
	height: 64px;
	line-height: 64px;
	display: flex;

	.fb-admin-header__actions-item {
		height: 100%;
		width: 44px;
		color: #fff;
		text-align: center;
		position: relative;

		.c-box {
			position: relative;

			&:hover {
				cursor: pointer;
				background: rgba(255, 255, 255, 0.2);
			}
		}
	}

	.fb-admin-header__actions-notice {
		.c-box {
			.notice-tip-num {
				position: absolute;
				top: 14px;
				right: 3px;
				width: 16px;
				height: 16px;
				line-height: 16px;
				border-radius: 50%;
				background: red;
				font-size: 12px;
			}
		}

		.@{FbUiPrefix}-popup-picker {
			line-height: normal;
			width: 300px;

			.@{FbUiPrefix}-card {
				.@{FbUiPrefix}-card__body {
					padding: 0;
					color: #666666;
					text-align: left;

					.card-header {
						border-bottom: 1px solid #E8E8E8;

						.@{FbUiPrefix}-tabs-header-label {
							width: 126px;
							text-align: center;
						}

						.@{FbUiPrefix}-tabs-buttons {
							display: none;
						}
					}

					.card-body {
						height: 315px;
						overflow-y: auto;

						.card-body-item {
							padding: 8px 16px;
							border-bottom: 1px solid #E8E8E8;
							cursor: pointer;
							transition: all .3s;
							position: relative;

							&:hover, &.active {
								background: rgba(230, 247, 255, 1);
							}
						}

						.card-body-notice {
							.card-body-item {
								> div:first-child {
									display: -webkit-box;
									-webkit-box-orient: vertical;
									line-clamp: 2;
									overflow: hidden;
									color: #313C47;
									font-size: 13px;
									line-height: 22px;

									&:after {
										content: "";
										display: inline-block;
										width: 8px;
										height: 8px;
										border-radius: 50%;
										background: #FB544E;
										margin-left: 8px;
									}
								}

								> p {
									margin-top: 4px;
									font-size: 12px;
									color: #666666;
									line-height: 20px;
								}
							}
						}

						.card-body-agency {
							.card-body-item {
								> div {
									display: flex;
									justify-content: space-between;
									color: #313C47;
									font-size: 13px;
									line-height: 22px;

									.state {
										font-size: 12px;
										padding: 0px 8px;
										border: 1px solid #ccc;
										border-radius: 4px;

										&.red {
											color: #FB544E;
											border-color: #FB544E;
										}

										&.yellow {
											color: #FFB500;
											border-color: #FFB500;
										}
									}
								}

								> p {
									margin-top: 4px;
									font-size: 12px;
									color: #666666;
									line-height: 20px;
								}
							}
						}

						.card-body-nodata {
							.@{FbUiPrefix}-empty {
								margin-top: 110px;
							}
						}
					}

					.card-footer {
						padding: 9px 16px;
						border-top: 1px solid #E8E8E8;
						color: #313C47;

						span {
							transition: all .3s;
							float: left;

							&:hover {
								color: #0284FE;
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
		min-width: 130px;

		.c-box {
			padding: 0 16px;

			.@{FbUiPrefix}-avatar {
				margin-right: 4px;
				margin-top: -2px;
			}
		}

		.@{FbUiPrefix}-popup-picker {
			width: 160px;

			.@{FbUiPrefix}-card {
				.@{FbUiPrefix}-card__body {
					padding: 0;
					color: #666666;
					text-align: left;

					.card-header {
						padding: 10px;
						border-bottom: 1px solid #E8E8E8;
						display: flex;
						flex-wrap: wrap;

						.@{FbUiPrefix}-avatar {
							margin-right: 10px;
						}

						p {
							margin: 0;
						}
					}

					.card-item {
						padding: 0 16px;
						height: 32px;
						line-height: 32px;

						.@{FbUiPrefix}-icon {
							margin-right: 6px;
						}

						&:hover {
							cursor: pointer;
							background: #E6F7FF;
						}
					}

					.card-footer {
						padding: 5px 0;
						border-top: 1px solid #E8E8E8;
					}
				}
			}
		}
	}
}

.current {
	position: absolute;
	bottom: 42px;
	right: 4px;
}

.jpx-button.jpx-button--link {
	color: #666666
}

.jpx-card--border {
	border: none;
		// 配置加载和错误状态样式
		.config-loading {
			color: #999 !important;
			display: flex;
			align-items: center;
			gap: 8px;
		}
		
		.config-error {
			color: #ff6b6b !important;
			display: flex;
			align-items: center;
			gap: 8px;
		}
		
		// LOGO图片样式
		.logo-image {
			height: 32px;
			width: auto;
			margin-right: 12px;
			vertical-align: middle;
			object-fit: contain;
		}
}
</style>
