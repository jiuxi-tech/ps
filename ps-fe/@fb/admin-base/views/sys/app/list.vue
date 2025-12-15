<template>
	<div class="app-list-container">
		<FbKeycloak ref="fbKeycloak" @authenticated="onAuthenticated" @error="onKeycloakError">
			<template #authenticated="{   }">
				<!-- 加载状态 -->
				<div v-if="loading" class="loading-container">
					<div class="loading-spinner"></div>
					<p>正在加载应用列表...</p>
				</div>

				<!-- 错误状态 -->
				<div v-else-if="error" class="error-container">
					<div class="error-icon">⚠️</div>
					<h3>加载失败</h3>
					<p>{{ error }}</p>
					<button @click="loadUserClients" class="retry-btn">重试</button>
				</div>

				<!-- 应用网格 -->
				<div v-else class="apps-grid">
					<div v-for="app in userClients" :key="app.id" class="app-card" @click="openApp(app)">
						<div class="app-icon">
							<img v-if="app.icon" :src="app.icon" :alt="app.name" />
							<div v-else class="default-icon">{{ getAppInitial(app.name) }}</div>
						</div>
						<div class="app-info">
							<h3 class="app-name">{{ app.name || app.clientId }}</h3>
							<p class="app-description">{{ app.description || '暂无描述' }}</p>
							<div class="app-status">
								<span :class="['status-badge', app.enabled ? 'enabled' : 'disabled']">
									{{ app.enabled ? '可用' : '不可用' }}
								</span>
							</div>
						</div>
					</div>

					<!-- 空状态 -->
					<div v-if="userClients.length === 0" class="empty-state">
						<div class="empty-icon">📱</div>
						<h3>暂无可用应用</h3>
						<p>您当前没有可访问的应用，请联系管理员分配权限</p>
					</div>
				</div>
			</template>
		</FbKeycloak>
	</div>
</template>

<script>
import FbKeycloak from '@fb/admin-base/components/FbKeycloak.vue'

export default {
	components: {
		FbKeycloak
	},

	data() {
		return {
			loading: false,
			error: null,
			userClients: [],
			currentUser: null,
			currentToken: null
		}
	},

	// 方法
	methods: {

		// FbKeycloak 认证成功回调
		onAuthenticated(data) {
			console.log('Keycloak认证成功:', data);
			this.currentUser = data.user;
			this.currentToken = data.token;
			// 认证成功后加载用户客户端列表
			this.loadUserClients();
		},

		// FbKeycloak 认证错误回调
		onKeycloakError(error) {
			console.error('Keycloak认证错误:', error);
			this.error = 'Keycloak认证服务初始化失败，请稍后重试';
		},

		async getAdminToken() {
			try {
				const response = await this.$axios.post("http://localhost:8180/realms/master/protocol/openid-connect/token",
					new URLSearchParams({
						client_id: "admin-cli",
						username: "admin",          // 管理员用户名
						password: "admin123", // 管理员密码
						grant_type: "password"
					}), {
					headers: { "Content-Type": "application/x-www-form-urlencoded" }
				}
				);

				window.adminToken = response.data.access_token;  // 保存到全局
				console.log("管理员 Token:", window.adminToken);
				return window.adminToken;
			} catch (error) {
				console.error('获取管理员Token失败:', error);
				throw error;
			}
		},

		// 获取用户可访问的客户端列表
		async loadUserClients() {
			try {
				this.loading = true;
				this.error = null;

				if (!this.currentToken) {
					throw new Error('无法获取用户认证token');
				}

				// 调用后端API获取用户可访问的客户端列表
				const response = await this.$axios.get('/api/keycloak/user-clients', {
					headers: {
						'Authorization': `Bearer ${this.currentToken}`
					}
				});

				if (response.data && response.data.code === 1) {
					this.userClients = this.formatClientData(response.data.data || []);
			} else {
				console.warn('后端API不可用');
			}
		} catch (error) {
			console.error('获取客户端列表失败:', error);
			} finally {
				this.loading = false;
			}
		},

		// 格式化客户端数据
		formatClientData(clients) {
			return clients.map(client => ({
				id: client.id || client.clientId,
				clientId: client.clientId,
				name: client.name || client.clientId,
				description: client.description || '',
				enabled: client.enabled !== false,
				baseUrl: client.baseUrl || client.rootUrl,
				icon: client.attributes && client.attributes.icon_url
			}));
		},



		// 获取应用名称首字母
		getAppInitial(name) {
			if (!name) return 'A';
			return name.charAt(0).toUpperCase();
		},

		// 打开应用
		openApp(app) {


			if (!app.enabled) {
				this.$message.warn('该应用当前不可用');
				return;
			}

			if (app.baseUrl) {
				// 在新窗口中打开应用
				window.open(app.baseUrl, '_blank');
			} else {
				this.$message.error('应用地址未配置');
			}
		},

		// 重试加载
		retryLoad() {
			this.loadUserClients();
		},

		// 登出功能（通过 FbKeycloak 组件）
		async logout() {
			try {
				if (this.$refs.fbKeycloak) {
					await this.$refs.fbKeycloak.logout();
				}
			} catch (error) {
				console.error('登出失败:', error);
				this.$message.error('登出失败，请稍后重试');
			}
		}
	}
}
</script>

<style lang="less" scoped>
.app-list-container {
	padding: 24px;
	height: 100vh;
	background-color: #f5f5f5;
	position: relative;
}

.page-header {
	margin-bottom: 32px;
	text-align: center;

	h2 {
		font-size: 28px;
		color: #333;
		margin-bottom: 8px;
		font-weight: 600;
	}

	p {
		font-size: 16px;
		color: #666;
		margin: 0;
	}
}

.loading-container {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 80px 20px;

	.loading-spinner {
		width: 40px;
		height: 40px;
		border: 4px solid #f3f3f3;
		border-top: 4px solid #1890ff;
		border-radius: 50%;
		animation: spin 1s linear infinite;
		margin-bottom: 16px;
	}

	p {
		font-size: 16px;
		color: #666;
		margin: 0;
	}
}

@keyframes spin {
	0% {
		transform: rotate(0deg);
	}

	100% {
		transform: rotate(360deg);
	}
}

.error-container {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 80px 20px;
	text-align: center;

	.error-icon {
		font-size: 48px;
		margin-bottom: 16px;
	}

	h3 {
		font-size: 20px;
		color: #333;
		margin-bottom: 8px;
	}

	p {
		font-size: 16px;
		color: #666;
		margin-bottom: 24px;
	}

	.retry-btn {
		padding: 10px 24px;
		background-color: #1890ff;
		color: white;
		border: none;
		border-radius: 6px;
		font-size: 14px;
		cursor: pointer;
		transition: background-color 0.3s;

		&:hover {
			background-color: #40a9ff;
		}
	}
}

.apps-grid {
	display: grid;
	grid-template-columns: repeat(auto-fill, minmax(100px, 120px));
	gap: 16px;
	max-width: 1200px;
	margin: 0 auto;
	justify-content: center;
	min-height: calc(100vh - 120px);
}

.app-card {
	background: white;
	border-radius: 16px;
	padding: 12px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
	transition: all 0.2s ease;
	cursor: pointer;
	border: 1px solid #f0f0f0;
	width: 100px;
	height: 120px;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;

	&:hover {
		transform: translateY(-2px);
		box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
		border-color: #1890ff;
		background: #fafafa;
	}

	.app-icon {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 56px;
		height: 56px;
		margin: 0 auto 8px;
		border-radius: 14px;
		background: linear-gradient(180deg,#fde7d8,#d10000,#ffad99 35%,#fbe6d7 65%,#fdf4ed);
		box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);

		img {
			width: 100%;
			height: 100%;
			object-fit: cover;
			border-radius: 14px;
		}

		.default-icon {
			font-size: 24px;
			font-weight: bold;
			color: white;
		}
	}

	.app-info {
		text-align: center;
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: center;

		.app-name {
			font-size: 12px;
			font-weight: 500;
			color: #333;
			margin: 0;
			line-height: 1.2;
			max-height: 24px;
			overflow: hidden;
			display: -webkit-box;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
			word-break: break-all;
		}

		.app-description {
			display: none;
		}

		.app-status {
			display: none;
		}
	}
}

.empty-state {
	position: absolute;
	top: 32vh;
	left: 50%;
	transform: translate(-50%, -50%);
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 80px 20px;
	text-align: center;
	width: 100%;
	max-width: 500px;

	.empty-icon {
		font-size: 64px;
		margin-bottom: 24px;
		opacity: 0.6;
	}

	h3 {
		font-size: 20px;
		color: #333;
		margin-bottom: 8px;
	}

	p {
		font-size: 16px;
		color: #666;
		margin: 0;
		max-width: 400px;
		line-height: 1.5;
	}
}

// 响应式设计
@media (max-width: 768px) {
	.apps-grid {
		grid-template-columns: repeat(auto-fill, minmax(90px, 100px));
		gap: 12px;
	}

	.app-card {
		width: 90px;
		height: 110px;
		padding: 10px;

		.app-icon {
			width: 48px;
			height: 48px;
			margin-bottom: 6px;

			.default-icon {
				font-size: 20px;
			}
		}

		.app-info {
			.app-name {
				font-size: 11px;
			}
		}
	}

	.page-header {
		margin-bottom: 24px;

		h2 {
			font-size: 24px;
		}

		p {
			font-size: 14px;
		}
	}
}

@media (max-width: 480px) {
	.app-list-container {
		padding: 16px;
	}

	.apps-grid {
		grid-template-columns: repeat(auto-fill, minmax(80px, 90px));
		gap: 10px;
	}

	.app-card {
		width: 80px;
		height: 100px;
		padding: 8px;

		.app-icon {
			width: 44px;
			height: 44px;
			margin-bottom: 4px;

			.default-icon {
				font-size: 18px;
			}
		}

		.app-info {
			.app-name {
				font-size: 10px;
			}
		}
	}
}
</style>