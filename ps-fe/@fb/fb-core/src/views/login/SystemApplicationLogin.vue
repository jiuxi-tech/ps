<template>
	<div class="login-wrapper">

		<div class="login-logo-icon"></div>

		<div class="login-logo">JPX PC视觉规范V3.0</div>


		<transition name="slide-to-down">
			<div v-show="!seletDepartment" class="login-dialog">

				<div class="login-form">

					<div class="login-form__caption">
						欢迎登陆
					</div>


					<fb-form ref="loginForm" autocomplete="off">

						<fb-form-item prop="用户名"
									  :rule="[{required: true, whitespace: true}, {type: 'username'}]">
							<fb-input v-model="loginForm.username"
									  size="l"
									  prepend-icon="user"
									  clearable
									  placeholder="用户名"
									  autocomplete="off"
							/>
						</fb-form-item>

						<fb-form-item prop="密码"
									  :rule="[{required: true }]">
							<fb-input v-model="loginForm.password"
									  size="l"
									  type="password"
									  prepend-icon="password"
									  clearable
									  placeholder="密码"
									  autocomplete="off"
							>
							</fb-input>
						</fb-form-item>

						<!--					<fb-form-item prop="loginForm.capture"-->
						<!--					              :rule="[]"-->
						<!--					              style="padding-bottom: 40px">-->
						<!--						<fb-input v-model="loginForm.capture"-->
						<!--						          size="l"-->
						<!--						          type="capture"-->
						<!--						          prepend-icon="qrcode"-->
						<!--						          clearable-->
						<!--						          placeholder="验证码"-->
						<!--						          autocomplete="off"-->
						<!--						          style="width: 214px;"-->
						<!--						>-->
						<!--						</fb-input>-->

						<!--						<system-capture :api="$datax.capturePath"/>-->

						<!--					</fb-form-item>-->

						<fb-form-item style="padding-bottom: 40px">


							<fb-drag-verify v-model="loginForm.isVerify"></fb-drag-verify>

						</fb-form-item>

						<fb-form-item>

							<fb-button
								type="primary"
								size="l"
								long
								:loading="inLoginProcess"
								:disabled="!loginForm.isVerify"
								@on-click="doLogin">
								登 录
							</fb-button>

						</fb-form-item>

					</fb-form>

					<div>
						<fb-button type="link">忘记密码？</fb-button>
						<fb-button type="link" class="register-btn">点击注册</fb-button>
					</div>

				</div>
			</div>
		</transition>

		<transition name="slide-to-down">
			<div v-show="seletDepartment" class="login-dialog">
				<div class="department-form">
					<div class="login-form__caption">
						选择部门
					</div>
					<fb-list-roll fatherClass="department-form-body" mode="y">
						<div class="department-form-body_item" @click="item.isSelect = !item.isSelect" v-for="(item, idx) in departmentList" :key="idx">
							<fb-icon name="node" color="#999"></fb-icon>
							{{item.department}}
							<fb-icon name="selected" size="12" v-show="item.isSelect"></fb-icon>
						</div>
					</fb-list-roll>
					<fb-button
						type="primary"
						size="l"
						long
						@on-click="confirmDepartment">
						确 定
					</fb-button>
				</div>
			</div>
		</transition>

		<div class="login-footer">
			<p>建设单位：浙江省应急管理厅</p>
			<p>技术支持：JPX</p>
		</div>

	</div>
</template>

<script>
	/**
	 * DefaultApplicationLogin
	 * (c) 2020 lincong1987
	 */

	import SystemCapture from '../components/SystemCapture'

	export default {

		components: {
			SystemCapture,
		},

		data () {
			return {

				loginForm: {
					username: 'admin',
					password: '12345678',
					capture: '',
					isVerify: false
				},

				inLoginProcess: false,
				// 选择部门
				seletDepartment: false,
				departmentList: [
					{ department: '食药部门', isSelect: false, },
					{ department: '司法部门', isSelect: false, },
					{ department: '应急部门', isSelect: false, },
					{ department: '应急部门', isSelect: false, },
					{ department: '应急部门', isSelect: false, },
				]
			}
		},

		mounted () {

			this.$datax.set('capturePath', 'http://admin.dlszywz.cn/include/captcha/captcha.php?')

			// this.$systemService.get('/test', {
			// 	a: 'aaa',
			// 	name: 'asd',
			// }).then(res => res.data).then(json => {
			// 	console.log(json)
			// }).catch(e => {
			// 	console.log(e)
			// })

			this.$nextTick(() => {

			})

		},

		methods: {
			doLogin () {
				if (!this.loginForm.isVerify) {
					this.$message.error('请验证')
					return
				}

				this.inLoginProcess = true

				// 校验
				this.$refs.loginForm.validate((result, errors) => {

					console.log('登陆表单校验', result, errors)
					// 切换部门
					this.seletDepartment = true

					if (result) {

						this.$svc.platform.login(this.loginForm.username, this.loginForm.password).then(json => {
							this.$datax.set('token', json.data.token)
							this.$datax.set('user', json.data)
							this.$store.dispatch('admin/setToken', json.data.token)
							// this.$router.replace(this.$datax.get('GLOBAL_CONFIG').mainPath)

							if (this.$store.state.menu.loadingStauts === false) {
								console.log("load menu from login")
								this.$store.state.menu.loadingStauts = 'loading'
								this.$svc.sys.menu.getMenus().then(data => {
									this.$store.dispatch('menu/load', data)
									this.$store.state.menu.loadingStauts = true
								})
							}

						})
					}

					this.inLoginProcess = false
				})

			},
			confirmDepartment () {
				for (let i = 0; i < this.departmentList.length; i++) {
					let item = this.departmentList[i]
					if (item.isSelect) {
						this.$router.replace(this.$datax.get('GLOBAL_CONFIG').mainPath)
						this.loginForm.isVerify = false
						this.seletDepartment = false
						return
					}
				}
				this.$message.warn('请点选部门登录')
			}
		},

	}
</script>

<style lang="less" scoped>

	.login-wrapper {
		position: absolute;
		top:      0;
		bottom:   0;
		left:     0;
		right:    0;
		width:    100%;
		height:   100%;
		background:          linear-gradient(180deg, #65BDFF 0%, #025EFE 100%);
		overflow: hidden;
		z-index:  1;

		&::after {
			content:             "";
			position:            absolute;
			height:              100%;
			width:               100%;
			background-image:    url("../../public/img/login/bg.png");
			background-size:     cover;
			background-position: 50% 50%;
		}

	}

	.login-logo-icon {
		position:    absolute;
		width:       96px;
		height:      96px;
		left:        50%;
		top:         16%;
		margin-top:  -(96px * 0.8);
		margin-left: -(96px/2);
		background-image:    url("../../public/img/login/logo-icon.png");
		z-index:     5;
	}

	.login-logo {
		position:    absolute;
		width:       500px;
		height:      44px;
		left:        50%;
		top:         24%;
		margin-top:  -(44px * 0.8);
		margin-left: -(500px/2);
		font-size:   36px;
		color:       #ffffff;
		text-align:  center;
		font-weight: bold;
		z-index:     5;
	}

	.login-dialog {
		position:      absolute;
		width:         360px;
		height:        422px;
		top:           48.2%;
		left:          50%;
		margin-top:    -(422px * 0.382);
		margin-left:   -(360px/2);
		background:    #FFFFFF;
		box-shadow:    0px 2px 8px 0px rgba(0, 0, 0, 0.15);
		border-radius: 4px;
		z-index:       100;
		.login-form__caption {
			margin-bottom: 16px;
		}

		.login-form, .department-form {

			padding: 0 24px;

			&__caption {
				height:      32px;
				font-size:   24px;
				color:       #313C47;
				line-height: 32px;
				padding:     32px 0;
				text-align:  center;
			}
		}

		.department-form {
			.department-form-body {
				height: 240px;
				margin-bottom: 24px;

				.department-form-body_item {
					color: #313C47;
					height: 36px;
					line-height: 36px;
					background: #F0EFF5;
					border-radius: 4px;
					padding: 0 32px;
					position: relative;
					margin-bottom: 24px;

					&:hover {
						opacity: .8;
						cursor: pointer;
					}

					.fb-icon {
						position: absolute;
						top: 10px;
						left: 9px;

						&:last-child {
							left: unset;
							right: 9px;
							background: rgb(86, 209, 0);
							color: rgb(255, 255, 255);
							padding: 3px;
							border-radius: 50%;
							top: 9px;
						}
					}
				}
			}
		}

	}

	.register-btn {
		float: right;
	}


	@media screen and (max-height: 720px) {

		.login-dialog {
			top:        50%;
			margin-top: -(422px * 0.5);
		}

		.login-logo {
			top:        10%;
			margin-top: -(44px * 0.8);
		}
	}


	.fb-form-item {
		padding-bottom: 24px;
	}

	.login-footer {
		position:    absolute;
		width:       360px;
		height:      44px;
		bottom:      30px;
		left:        50%;
		margin-left: -(360px/2);
		z-index:     10;

		p {
			text-align: center;
			color:      #fff;
			margin:  0;
		}
	}

</style>
