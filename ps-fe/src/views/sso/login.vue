<template>
	<div class="sso-login-container">
		<div class="login-card">
			<div class="loading-spinner" v-if="loading"></div>
			<div class="success-icon" v-if="success">✓</div>
			<div class="error-icon" v-if="error">✕</div>
			<h3 class="title">{{ statusTitle }}</h3>
			<p class="message" :class="{ 'success': success, 'error': error }">{{ statusMessage }}</p>
		</div>
	</div>
</template>

<script>




	export default {
		name: 'sso-login',
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
			return {
				loading: true,
				success: false,
				error: false,
				statusTitle: '正在验证登录凭证...',
				statusMessage: '请稍候'
			}
		},

		// 方法
		methods: {
			async init() {
				try {
					// 获取 URL 中的 token 参数
					const token = this.getQueryStr("token");
					
					if (!token) {
						this.showError('登录失败', '未能获取到有效的登录凭证，请重新尝试登录');
						setTimeout(() => {
							this.$router.replace('/login');
						}, 3000);
						return;
					}

					// 保存 token
					this.$datax.set('token', token);
					
					// 更新状态：正在获取用户信息
					this.statusTitle = '正在获取用户信息...';
					this.statusMessage = '即将进入系统';

					// 获取登录人用户信息
					const res = await this.$svc.platform.getUserInfo();
					this.$datax.set('userInfo', res.data);
					
					// 显示成功状态
					this.showSuccess('登录成功', '正在跳转到系统...');
					
					// 跳转到主页面
					setTimeout(() => {
						this.$router.replace('/main');
					}, 1500);
					
				} catch (e) {
					console.error('SSO登录失败:', e);
					this.showError('登录失败', '获取用户信息失败，请重新登录');
					
					setTimeout(() => {
						this.$router.replace('/login');
					}, 3000);
				}
			},

			getQueryStr(str) {
				let LocString = String(window.document.location.href);
				var rs = new RegExp("(^|)" + str + "=([^&]*)(&|$)", "gi").exec(LocString), tmp;
				if (tmp = rs) {
					return decodeURIComponent(tmp[2]);
				}
				return '';
			},
			
			showSuccess(title, message) {
				this.loading = false;
				this.success = true;
				this.error = false;
				this.statusTitle = title;
				this.statusMessage = message;
			},
			
			showError(title, message) {
				this.loading = false;
				this.success = false;
				this.error = true;
				this.statusTitle = title;
				this.statusMessage = message;
			}
		}
	}
</script>

<style lang="less" scoped>
.sso-login-container {
	min-height: 100vh;
	display: flex;
	align-items: center;
	justify-content: center;
	background: linear-gradient(180deg, #fde7d8, #d10000, #ffad99 35%, #fbe6d7 65%, #fdf4ed);
}

.login-card {
	background: white;
	border-radius: 12px;
	padding: 40px;
	box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
	text-align: center;
	min-width: 320px;
	
	.loading-spinner {
		display: inline-block;
		width: 40px;
		height: 40px;
		border: 4px solid #f3f3f3;
		border-top: 4px solid #c62828;
		border-radius: 50%;
		animation: spin 1s linear infinite;
	}
	
	.success-icon {
		display: inline-block;
		width: 60px;
		height: 60px;
		line-height: 60px;
		font-size: 36px;
		color: #67c23a;
		background: #f0f9ff;
		border-radius: 50%;
		animation: scaleIn 0.3s ease-out;
	}
	
	.error-icon {
		display: inline-block;
		width: 60px;
		height: 60px;
		line-height: 60px;
		font-size: 36px;
		color: #f56c6c;
		background: #fef0f0;
		border-radius: 50%;
		animation: scaleIn 0.3s ease-out;
	}
	
	.title {
		margin: 16px 0 8px 0;
		color: #333;
		font-size: 18px;
		font-weight: 500;
	}
	
	.message {
		margin: 0;
		color: #666;
		font-size: 14px;
		
		&.success {
			color: #67c23a;
		}
		
		&.error {
			color: #f56c6c;
		}
	}
}

@keyframes spin {
	0% { transform: rotate(0deg); }
	100% { transform: rotate(360deg); }
}

@keyframes scaleIn {
	0% { transform: scale(0); }
	50% { transform: scale(1.1); }
	100% { transform: scale(1); }
}
</style>
