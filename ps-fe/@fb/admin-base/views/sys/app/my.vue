<template>
	<div class="app-list-container">
		<div class="button-group"  >
			<fb-button type="primary" @on-click="getSsoUserStatus">获取SSO登录状态</fb-button>
			<fb-button type="primary" @on-click="getClients">获取客户端列表</fb-button>
			<fb-button type="primary" @on-click="getRoles">获取角色列表</fb-button>
			<fb-button type="primary" @on-click="getResources">获取资源列表</fb-button>
			<fb-button type="warning" @on-click="logoutSso">SSO 退出</fb-button>
		</div>
		<div class="result-clients">
			<div v-if="clientsLoading" class="loading-container">
				<div class="loading-spinner"></div>
				<p>正在加载客户端列表...</p>
			</div>
			<div v-else-if="clientsError" class="error-container">
				<div class="error-icon">⚠️</div>
				<h3>加载失败</h3>
				<p>{{ clientsError }}</p>
				<button class="retry-btn" @click="getClients">重试</button>
			</div>
			<div v-else-if="clients.length === 0" class="empty-state">
				<div class="empty-icon">📱</div>
				<h3>暂无客户端</h3>
				<p>点击上方按钮获取客户端列表</p>
			</div>
			<div v-else class="apps-grid">
				<div v-for="client in clients" :key="client.id || client.clientId" class="app-card"
					@click="openClient(client)">
					<div class="app-icon">
						<img v-if="client.attributes && client.attributes.logo_uri" :src="client.attributes.logo_uri"
							:alt="client.name || client.clientId" @error="handleImageError($event, client)" />
						<div v-else class="default-icon">
							{{ getClientIcon(client) }}
						</div>
					</div>
					<div class="app-info">
						<h4 class="app-name">{{ getClientDisplayName(client) }}</h4>
						<p class="app-description">{{ client.description || client.clientId }}</p>
						<span class="app-status" :class="{ enabled: client.enabled }">{{ client.enabled ? '启用' : '禁用'
						}}</span>
					</div>
				</div>
			</div>
		</div>
		<div class="result-roles">

		</div>
	</div>
</template>

<script>

export default {
	components: {
	},

	data() {
		return {
			clients: [],
			clientsLoading: false,
			clientsError: null
		}
	},

	mounted() {
		// 直接获取
		this.getClients();
	},
	methods: {
		getSsoUserStatus() {
			this.$svc.sys.sso.user.status().then(res => {
				console.log('获取SSO登录状态', res);
			}).catch(err => {
				console.error('获取SSO状态失败', err);
			});
		},
		logoutSso() {
			this.$svc.sys.sso.user.logout().then(res => {
				console.log('SSO退出响应', res);
			}).catch(err => {
				console.error('SSO退出失败', err);
			});
		},
		getClients() {
			this.clientsLoading = true;
			this.clientsError = null;

			this.$svc.sys.sso.user.clients().then(res => {
				console.log('获取客户端列表', res);
				if (res && res.code === 1 && res.data) {
					// 过滤掉系统内置客户端，只显示用户相关的客户端
					this.clients = res.data.filter(client => {
						// 排除系统内置客户端
						const systemClients = ['account', 'account-console', 'admin-cli', 'broker', 'realm-management', 'security-admin-console'];
						return !systemClients.includes(client.clientId);
					});
				} else {
					this.clientsError = (res && res.message) || '获取客户端列表失败';
				}
			}).catch(err => {
				console.error('获取客户端列表失败', err);
				this.clientsError =   '网络请求失败';
			}).finally(() => {
				this.clientsLoading = false;
			});
		},
		getRoles() {
			this.$svc.sys.sso.user.roles().then(res => {
				console.log('获取角色列表', res);
			}).catch(err => {
				console.error('获取角色列表失败', err);
			});
		},
		getResources() {
			this.$svc.sys.sso.user.resources().then(res => {
				console.log('获取资源列表', res);
			}).catch(err => {
				console.error('获取资源列表失败', err);
			});
		},

		// 获取客户端显示名称
		getClientDisplayName(client) {
			// 如果name是占位符格式（如${client_account}），则使用clientId
			if (client.name && !client.name.startsWith('${')) {
				return client.name;
			}
			return client.clientId || '未知客户端';
		},

		// 获取客户端图标
		getClientIcon(client) {
			// 如果有自定义名称，使用名称首字母
			if (client.name && !client.name.startsWith('${')) {
				return client.name.charAt(0).toUpperCase();
			}
			// 否则使用clientId首字母
			else if (client.clientId) {
				return client.clientId.charAt(0).toUpperCase();
			}
			return '📱';
		},

		// 处理图片加载错误
		handleImageError(event, client) {
			event.target.style.display = 'none';
			// 可以在这里添加默认图标显示逻辑
		},

		// 打开客户端
		openClient(client) {
			console.log('打开客户端:', client);

			// 记录用户点击应用的日志
			// this.$logger.send({
			// 	action: 'click_application',
			// 	module: 'sso_client_management',
			// 	details: {
			// 		clientId: client.clientId,
			// 		clientName: this.getClientDisplayName(client),
			// 		enabled: client.enabled
			// 	},
			// 	message: `用户点击了应用: ${this.getClientDisplayName(client)} (${client.clientId})`
			// });

			// 构建客户端访问URL
			let targetUrl = null;

			// 优先使用rootUrl
			if (client.rootUrl && !client.rootUrl.startsWith('${')) {
				targetUrl = client.rootUrl;
			}
			// 其次使用baseUrl
			else if (client.baseUrl && !client.baseUrl.startsWith('${')) {
				// 如果baseUrl是相对路径，需要拼接Keycloak服务器地址
				if (client.baseUrl.startsWith('/')) {
					targetUrl = 'http://localhost:8180' + client.baseUrl;
				} else {
					targetUrl = client.baseUrl;
				}
			}

			if (targetUrl) {
				// 记录成功打开应用的日志
				this.$logger.send({

					details: {
						clientId: client.clientId,
						clientName: this.getClientDisplayName(client),
						targetUrl: targetUrl
					},
					operterMsg: `成功打开应用: ${this.getClientDisplayName(client)}, URL: ${targetUrl}; clientId: ${client.clientId}`,
					appName: client.clientId,
					// 模块编码
					moduleCode: 'SSO_CLIENT_OPEN',
					// 模块名称
					moduleName: '打开SSO客户端',
					// 操作类型： login/logout/add/delete/edit/query/pass/unpass, 可以自己定义
					operateType: 'app',
					// 操作人id
					operterId: app.$datax.get('userInfo').personId,
				});
				window.open(targetUrl, '_blank');
			} else {
				// 记录无法打开应用的日志
				// this.$logger.send({
				// 	action: 'open_application_failed',
				// 	module: 'sso_client_management',
				// 	details: {
				// 		clientId: client.clientId,
				// 		clientName: this.getClientDisplayName(client),
				// 		reason: 'no_access_url'
				// 	},
				// 	message: `无法打开应用: ${this.getClientDisplayName(client)}, 原因: 暂无访问地址`
				// });
				this.$message.info(`客户端 ${this.getClientDisplayName(client)} 暂无访问地址`);
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

.button-group {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	margin-bottom: 24px;
	padding: 20px;
	background: white;
	border-radius: 8px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.result-display {
	padding: 20px;
	background: white;
	border-radius: 8px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

	h3 {
		margin: 0 0 16px 0;
		color: #333;
		font-size: 18px;
		font-weight: 600;
	}

	.result-content {
		padding: 16px;
		background: #f8f9fa;
		border-radius: 6px;
		border-left: 4px solid #1890ff;
		color: #666;
		line-height: 1.6;
	}
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
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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