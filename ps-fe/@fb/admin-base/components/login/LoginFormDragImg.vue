<!-- 拖拽滑块登录 form -->
<template>
	<fb-form :model="loginForm" ref="loginForm" autocomplete="off">

		<fb-form-item prop="userName" label="用户名" :showLabel="false"
					  :rule="[{required: true, whitespace: true}]">
			<fb-input v-model="loginForm.userName"
					  size="l"
					  prepend-icon="user"
					  clearable
					  placeholder="用户名"
					  autocomplete="off"
			/>
		</fb-form-item>

		<fb-form-item prop="userpwd" label="密码" :showLabel="false"
					  :rule="[{required: true }]">
			<fb-input v-model="loginForm.userpwd"
					  size="l"
					  type="password"
					  prepend-icon="password"
					  clearable
					  placeholder="密码"
					  autocomplete="off"
			>
			</fb-input>
		</fb-form-item>

		<fb-form-item prop="isVerify" :style="{paddingBottom: dragShow ? '20px' : '40px', transition: 'none'}">
			<fb-drag-verify-rotate-img
				v-if="captchaType === 'rotate'"
				ref="dragVerifyImg"
				v-show="!noDrag && dragShow"
				:action="`${baseURL}/platform/captcha/get-rotate-captcha`"
				v-model="dragImgStatus"
				@on-verify="dragImgVerify"
				img-box-show>
			</fb-drag-verify-rotate-img>

			<fb-drag-verify-img
				v-else
				:captchaType="captchaType"
				ref="dragVerifyImg"
				v-show="!noDrag && dragShow"
				:action="captchaType === 'slider' ? `${baseURL}/platform/captcha/get-slider-captcha` : `${baseURL}/sys/captcha/verification-image`"
				v-model="dragImgStatus"
				@on-verify="dragImgVerify"
				:xScale="captchaType === 'slider' ? xScale : 1"
				img-box-show>
			</fb-drag-verify-img>

		</fb-form-item>

		<fb-form-item>

			<fb-button
				type="primary"
				size="l"
				long
				:loading="inLoginProcess"
				@on-click="doLogin">
				登 录
			</fb-button>

		</fb-form-item>

	</fb-form>
</template>

<script>

/**
 * DefaultApplicationLogin
 * (c) 2020 lincong1987
 */

export default {

	props: {
		inLoginProcess: {
			type: Boolean,
			default: false
		},
		clearForm: {
			type: Boolean,
			default: false
		},
		captchaType: {
			type: String,
			default: 'rotate'
		},
		noDrag: {
			type: Boolean,
			default: false
		}
	},

	computed: {
		dragImgStatus: {
			get () {
				return this.$store.state.login.dragImgStatus
			},
			set (newVal) {
				this.$store.commit('login/setDragImgStatus', newVal)
			}
		}
	},

	data () {
		return {
			baseURL: app.$service.service.defaults.baseURL,
			dragShow: false,
			loginForm: {
				userName: '',
				userpwd: '',
				x: '',
				y: '',
				verification: ''
			},
			xScale: 312 / 590
		}
	},

	watch: {
		dragImgStatus: {
			handler(newVal) {
				if (newVal == 'fail') {
					this.dragShow = false
				}
			},
		}
	},

	mounted() {
		window.addEventListener('keyup', this.enterCall)
	},

	methods: {
		dragImgVerify (params) {
			this.loginForm.x = params.x
			this.loginForm.y = params.y
			// 修复：使用clientUuid而不是verification
			this.loginForm.verification = params.clientUuid

			// app.service({
			// 	method: 'get',
			// 	url: '/sys/captcha/check-verification',
			// 	params: {
			// 		x,
			// 		y
			// 	},
			// 	headers: {
			// 		"verification": verification
			// 	},
			// }).then(res => {
			// 	console.log(res)
			// 	let data = res
			// 	if (data.code >= 0) {
			// 		this.$store.commit('login/setDragImgStatus', 'success')
			// 		this.$message.success(data.message)
			// 	} else {
			// 		this.$store.commit('login/setDragImgStatus', 'fail')
			// 		this.$message.error(data.message)
			// 	}
			// }).catch(err => {
			// 	this.$store.commit('login/setDragImgStatus', 'fail')
			// })

			this.$emit('doLogin', {
				...this.loginForm,
				...params
			})

		},
		doLogin () {
			this.$refs.loginForm.validate((result, error) => {
				if (result) {
					this.dragShow = true
					this.$refs['dragVerifyImg'].reset();
					if (this.noDrag) {
						this.$emit('doLogin', {
							...this.loginForm,
							noDrag: true
						})
					}
					// if (this.dragImgStatus === 'success') {
					// 	this.$emit('doLogin', this.loginForm)
					// } else {
					// 	return this.$message.error('请拖拽滑块验证')
					// }
				} else {
					this.$message.warn('请填写正确的用户信息！')
				}
			})
		},
		destroy () {
			if (this.$refs['loginForm']) {
				this.$refs['loginForm'].resetFields();
			}
			setTimeout(() => {
				this.$store.commit('login/setDragImgStatus', 'end')
			},500)
			window.removeEventListener('keyup', this.enterCall);
		},
		enterCall (e) {
			let code = e.keyCode || e.which
			if (code === 13) {
				this.doLogin()
			}
		}
	},

	deactivated () {
		this.destroy()
	},

	destroyed() {
		this.destroy()
	}

}
</script>

<style lang="less" scoped>
@import "../../assets/styles/common.less";
.@{FbUiPrefix}-form-item {
	padding-bottom: 24px;
}
.@{FbUiPrefix}-drag-verify-content {
	z-index: 9;
}
</style>
