<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-property>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="证书名称">
							{{formData.certName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="状态">
							<fb-tag v-if="formData.status == 1" type="success">已应用</fb-tag>
							<fb-tag v-else type="default">未应用</fb-tag>
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="使用状态">
							<fb-tag v-if="formData.isInUse == 1" type="success">使用中</fb-tag>
							<fb-tag v-else type="default">未使用</fb-tag>
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="过期状态">
							<fb-tag v-if="formData.isExpired == 1" type="danger">已过期</fb-tag>
							<fb-tag v-else type="success">正常</fb-tag>
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="证书描述">
							{{formData.certDesc || '-'}}
						</fb-property-item>
					</fb-col>
				</fb-row>

				<!-- 证书详细信息 -->
				<fb-divider orientation="left">证书信息</fb-divider>
				
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="公用名(CN)">
							{{formData.subjectCn || '-'}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="组织(O)">
							{{formData.subjectO || '-'}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="组织单位(OU)">
							{{formData.subjectOu || '-'}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="绑定域名">
							{{formData.domainNames || '-'}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="发证机构">
							{{formData.issuer || '-'}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="颁发日期">
							{{ formData.issueDate ? formatDateTime(formData.issueDate) : '-' }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="到期日期">
							<span :class="{'text-danger': isExpiringSoon(formData.expireDate)}">
								{{ formData.expireDate ? formatDateTime(formData.expireDate) : '-' }}
							</span>
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="应用时间">
							{{ formData.appliedTime ? formatDateTime(formData.appliedTime) : '-' }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="剩余天数">
							<span :class="getExpiryClass(formData.expireDate)">
								{{ getExpiryDays(formData.expireDate) }}
							</span>
						</fb-property-item>
					</fb-col>
				</fb-row>

				<!-- 操作信息 -->
				<fb-divider orientation="left">操作信息</fb-divider>
				
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="创建人">
							{{formData.createPersonName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="创建时间">
							{{ formData.createTime ? formatDateTime(formData.createTime) : '-' }}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="修改人">
							{{formData.updatePersonName || '-'}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="修改时间">
							{{ formData.updateTime ? formatDateTime(formData.updateTime) : '-' }}
						</fb-property-item>
					</fb-col>
				</fb-row>

				<!-- 证书文件信息 -->
				<fb-divider orientation="left">证书文件</fb-divider>
				
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="PEM证书文件">
							<fb-button v-if="formData.pemContent" size="s" @on-click="downloadPemFile">
								下载PEM文件
							</fb-button>
							<span v-else>-</span>
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="私钥文件">
							<fb-button v-if="formData.keyContent" size="s" @on-click="downloadKeyFile">
								下载Key文件
							</fb-button>
							<span v-else>-</span>
						</fb-property-item>
					</fb-col>
				</fb-row>

			 

			</fb-property>
		</div>

		<div class="tp-dialog-bottom">
			<fb-flex jc-center gap="8px">
			<fb-button v-if="formData.status == 0 && formData.isExpired != 1" 
									   type="primary" 
									   @on-click="handleApply">
								应用证书
							</fb-button>
							<fb-button v-if="formData.status == 1" 
									   type="warning" 
									   @on-click="handleRestart">
								重启Nginx
							</fb-button>
							<fb-button @on-click="handleEdit">
								修改证书
							</fb-button>
							<fb-button v-if="formData.isInUse != 1" 
									   type="danger" 
									   @on-click="handleDelete">
								删除证书
							</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button></fb-flex>
		</div>
	</div>
</template>

<script>
import dayjs from "dayjs";

export default {
	name: 'view',
	mixins: [],
	// 接收父组件的传参
	props: {
		param: {
			type: Object,
			default: () => ({})
		},
		parentPage: {
			type: Object,
			default: null
		}
	},

	// 初始化方法
	mounted() {
		this.init();
	},

	data() {
		return {
			formData: {}
		}
	},

	methods: {
		// 初始化
		init() {
			this.loadData();
		},

		// 加载数据
		loadData() {
			app.$svc.sys.serverCert.view({ certId: this.param.certId }).then((result) => {
				if (result.code == 1) {
					this.formData = result.data;
				} else {
					this.$message.error('加载数据失败: ' + result.message);
				}
			});
		},

		// 格式化日期时间
		formatDateTime(dateTime) {
			return dayjs(dateTime).format('YYYY-MM-DD HH:mm:ss');
		},

		// 检查是否即将过期
		isExpiringSoon(expireDate) {
			if (!expireDate) return false;
			const expire = dayjs(expireDate);
			const now = dayjs();
			const daysUntilExpire = expire.diff(now, 'day');
			return daysUntilExpire <= 30;
		},

		// 获取过期天数
		getExpiryDays(expireDate) {
			if (!expireDate) return '-';
			const expire = dayjs(expireDate);
			const now = dayjs();
			const days = expire.diff(now, 'day');
			
			if (days < 0) {
				return `已过期 ${Math.abs(days)} 天`;
			} else if (days === 0) {
				return '今天过期';
			} else {
				return `${days} 天`;
			}
		},

		// 获取过期状态样式类
		getExpiryClass(expireDate) {
			if (!expireDate) return '';
			const expire = dayjs(expireDate);
			const now = dayjs();
			const days = expire.diff(now, 'day');
			
			if (days < 0) {
				return 'text-danger'; // 已过期
			} else if (days <= 7) {
				return 'text-danger'; // 7天内过期
			} else if (days <= 30) {
				return 'text-warning'; // 30天内过期
			} else {
				return 'text-success'; // 正常
			}
		},

		// 下载PEM证书文件
		downloadPemFile() {
			if (!this.formData.pemContent) {
				this.$message.warning('证书文件不存在');
				return;
			}
			
			try {
				const content = atob(this.formData.pemContent);
				const blob = new Blob([content], { type: 'text/plain;charset=utf-8' });
				const url = window.URL.createObjectURL(blob);
				
				const link = document.createElement('a');
				link.href = url;
				link.download = `${this.formData.certName || 'certificate'}.pem`;
				document.body.appendChild(link);
				link.click();
				document.body.removeChild(link);
				window.URL.revokeObjectURL(url);
			} catch (error) {
				this.$message.error('下载失败: ' + error.message);
			}
		},

		// 下载私钥文件
		downloadKeyFile() {
			if (!this.formData.keyContent) {
				this.$message.warning('私钥文件不存在');
				return;
			}
			
			try {
				const content = atob(this.formData.keyContent);
				const blob = new Blob([content], { type: 'text/plain;charset=utf-8' });
				const url = window.URL.createObjectURL(blob);
				
				const link = document.createElement('a');
				link.href = url;
				link.download = `${this.formData.certName || 'private'}.key`;
				document.body.appendChild(link);
				link.click();
				document.body.removeChild(link);
				window.URL.revokeObjectURL(url);
			} catch (error) {
				this.$message.error('下载失败: ' + error.message);
			}
		},

		// 应用证书
		handleApply() {
			this.$confirm('确定要应用该证书吗？应用后将覆盖现有证书文件。', () => {
				app.$svc.sys.serverCert.apply({ certId: this.formData.certId }).then((result) => {
					if (result.code == 1) {
						this.$message.success('应用成功');
						this.loadData(); // 重新加载数据
					} else {
						this.$message.error('应用失败: ' + result.message);
					}
				});
			});
		},

		// 重启Nginx
		handleRestart() {
			this.$confirm('确定要重启Nginx服务吗？', () => {
				app.$svc.sys.serverCert.restart().then((result) => {
					if (result.code == 1) {
						this.$message.success('重启成功');
					} else {
						this.$message.error('重启失败: ' + result.message);
					}
				});
			});
		},

		// 修改证书
		handleEdit() {
			if (this.parentPage && this.parentPage.closeDialog && this.parentPage.handleEdit) {
				this.parentPage.closeDialog();
				// 触发父页面的编辑操作
				setTimeout(() => {
					this.parentPage.handleEdit(this.formData);
				}, 100);
			} else {
				// 如果没有parentPage，关闭当前弹窗并提示
				this.handleClose();
				this.$message.info('请从列表页面执行修改操作');
			}
		},

		// 删除证书
		handleDelete() {
			if (this.formData.isInUse == 1) {
				this.$message.warning('证书正在使用中，无法删除');
				return;
			}

			this.$confirm('确定要删除该证书吗？删除后将无法恢复！', () => {
				app.$svc.sys.serverCert.delete({ certId: this.formData.certId }).then((result) => {
					if (result.code == 1) {
						this.$message.success('删除成功');
						this.handleClose();
					} else {
						this.$message.error('删除失败: ' + result.message);
					}
				});
			});
		},

		// 关闭
		handleClose() {
		
            this.closeTpDialog()
		}
	}
}
</script>

<style scoped>
.text-danger {
	color: #ff4d4f;
	font-weight: bold;
}

.text-warning {
	color: #faad14;
	font-weight: bold;
}

.text-success {
	color: #52c41a;
}
</style>