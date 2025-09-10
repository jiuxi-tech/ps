<template>
	<div>
		<fb-form :label-width="120" class="view-form">
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="应用名称">
						<span class="form-text">{{ formData.appName || '-' }}</span>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="应用编码">
						<span class="form-text">{{ formData.appCode || '-' }}</span>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="应用类型">
						<span class="form-text">{{ formData.appTypeName || '-' }}</span>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="应用状态">
						<fb-tag :type="getStatusType(formData.appStatus)">{{ formData.appStatusName || '-' }}</fb-tag>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="版本号">
						<span class="form-text">{{ formData.appVersion || '-' }}</span>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="排序号">
						<span class="form-text">{{ formData.sortOrder || 0 }}</span>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="24">
					<fb-form-item label="应用地址">
						<span class="form-text">
							<a v-if="formData.appUrl" :href="formData.appUrl" target="_blank" class="app-link">
								{{ formData.appUrl }}
								<fb-icon name="external-link" style="margin-left: 5px;"></fb-icon>
							</a>
							<span v-else>-</span>
						</span>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="24">
					<fb-form-item label="应用图标">
						<div class="app-icon-display">
							<img v-if="formData.appIcon && isImageUrl(formData.appIcon)" 
								 :src="formData.appIcon" 
								 alt="应用图标" 
								 class="app-icon-img">
							<fb-icon v-else-if="formData.appIcon" :name="formData.appIcon" size="32"></fb-icon>
							<span v-else class="form-text">-</span>
							<span class="icon-path">{{ formData.appIcon || '-' }}</span>
						</div>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="24">
					<fb-form-item label="应用描述">
						<div class="form-text-area">{{ formData.appDesc || '-' }}</div>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="24">
					<fb-form-item label="备注">
						<div class="form-text-area">{{ formData.remark || '-' }}</div>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="创建人">
						<span class="form-text">{{ formData.createUserName || '-' }}</span>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="创建时间">
						<span class="form-text">{{ formData.createDateStr || '-' }}</span>
					</fb-form-item>
				</fb-col>
			</fb-row>
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="修改人">
						<span class="form-text">{{ formData.updateUserName || '-' }}</span>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="修改时间">
						<span class="form-text">{{ formData.updateDateStr || '-' }}</span>
					</fb-form-item>
				</fb-col>
			</fb-row>
		</fb-form>
		
		<div class="form-actions">
			<fb-button @on-click="handleEdit" type="primary" v-permission="'SYS_APPMANAGE_APP_UPDATE'">
				<fb-icon name="edit"></fb-icon>
				编辑
			</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>
	export default {
		name: 'AppView',
		props: {
			id: {
				type: String,
				default: ''
			},
			passKey: {
				type: String,
				default: ''
			}
		},
		data() {
			return {
				loading: false,
				formData: {}
			}
		},
		mounted() {
			if (this.id) {
				this.loadData();
			}
		},
		methods: {
			// 加载数据
			loadData() {
				this.loading = true;
				app.$svc.sys.app.get({
					appId: this.id,
					passKey: this.passKey
				}).then(result => {
					this.loading = false;
					if (result.code === 1) {
						this.formData = result.data;
					} else {
						this.$message.error('加载数据失败: ' + result.message);
					}
				}).catch(error => {
					this.loading = false;
					this.$message.error('加载数据失败');
					console.error(error);
				});
			},
			// 获取状态类型
			getStatusType(status) {
				switch (status) {
					case '1':
						return 'success'; // 启用
					case '0':
						return 'danger'; // 禁用
					case '2':
						return 'warning'; // 维护中
					default:
						return 'default';
				}
			},
			// 判断是否为图片URL
			isImageUrl(url) {
				if (!url) return false;
				const imageExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp', '.svg', '.webp'];
				return imageExtensions.some(ext => url.toLowerCase().includes(ext)) || url.startsWith('http');
			},
			// 编辑
			handleEdit() {
				let param = {"id": this.id, "passKey": this.passKey};
				let options = {
					tabChangeConfirm: false,
					callBack: (result) => {
						this.loadData(); // 重新加载数据
						this.$emit('refresh');
					}
				}
				this.$refs.TpDialog?.show(import('./add.vue'), param, "修改应用", options);
			},
			// 关闭
			handleClose() {
				this.$emit('close');
			}
		}
	}
</script>

<style lang="less" scoped>
	.view-form {
		padding: 20px;
		
		.form-text {
			color: #333;
			font-size: 14px;
			line-height: 1.5;
		}
		
		.form-text-area {
			color: #333;
			font-size: 14px;
			line-height: 1.6;
			min-height: 20px;
			white-space: pre-wrap;
			word-break: break-all;
		}
		
		.app-link {
			color: #1890ff;
			text-decoration: none;
			
			&:hover {
				text-decoration: underline;
			}
		}
		
		.app-icon-display {
			display: flex;
			align-items: center;
			gap: 10px;
			
			.app-icon-img {
				width: 32px;
				height: 32px;
				object-fit: contain;
				border: 1px solid #d9d9d9;
				border-radius: 4px;
			}
			
			.icon-path {
				color: #666;
				font-size: 12px;
			}
		}
	}
	
	.form-actions {
		text-align: center;
		margin-top: 30px;
		padding-top: 20px;
		border-top: 1px solid #e8e8e8;
		
		.fb-button {
			margin: 0 10px;
		}
	}
</style>