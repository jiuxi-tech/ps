<template>
	<div>
		<fb-page-search>

			
			<!-- <template slot="buttons">
				<fb-button @on-click="handleTest" icon="experiment">测试规则</fb-button>
				<fb-button @on-click="handleViewLogs" icon="file-text">访问日志</fb-button>
			</template> -->

			<template slot="actions">
			 <fb-flex ai-center>
				<fb-text>IP访问控制配置，支持通配符(*)、CIDR表示法(192.168.1.0/24)和IP范围(192.168.1.1-192.168.1.100)。黑名单优先级高于白名单。</fb-text>
				<fb-button type="primary" icon="save" @on-click="handleSave" :loading="saving">保存配置</fb-button>
		 
			</fb-flex>
				</template>

			<template slot="table">
					<fb-form ref="config-form" :label-width="140">
						<!-- 基础配置 -->
						<fb-row>
							<fb-col span="24">
								<fb-form-item label="启用IP访问控制" prop="enabled">
									<fb-switch v-model="configData.enabled"></fb-switch>
									<div class="form-tip">开启后将对所有访问进行IP检查</div>
								</fb-form-item>
							</fb-col>
						</fb-row>

						<!-- <fb-row>
							<fb-col span="24">
								<fb-form-item label="启用访问日志" prop="logEnabled">
									<fb-switch v-model="configData.logEnabled"></fb-switch>
									<div class="form-tip">记录IP访问日志，包括允许和拒绝的访问</div>
								</fb-form-item>
							</fb-col>
						</fb-row> -->

						<fb-row>
							<fb-col span="24">
								<fb-form-item label="拒绝访问提示" prop="denyMessage">
									<fb-input v-model="configData.denyMessage" placeholder="请输入拒绝访问时的提示信息"></fb-input>
									<div class="form-tip">当IP被拒绝访问时显示的提示信息</div>
								</fb-form-item>
							</fb-col>
						</fb-row>

						<!-- IP白名单配置 -->
						<fb-row>
							<fb-col span="24">
								<fb-form-item label="IP白名单" prop="whitelist">
									<fb-textarea 
										v-model="configData.whitelist" 
										rows="6"
										placeholder="请输入允许访问的IP地址，每行一个\n支持格式：\n192.168.1.100 (单个IP)\n192.168.1.* (通配符)\n192.168.1.0/24 (CIDR)\n192.168.1.1-192.168.1.100 (IP范围)"
										:maxlength="2000">
									</fb-textarea>
									<div class="form-tip">白名单中的IP地址将被允许访问，每行一个规则</div>
								</fb-form-item>
							</fb-col>
						</fb-row>

						<!-- IP黑名单配置 -->
						<fb-row>
							<fb-col span="24">
								<fb-form-item label="IP黑名单" prop="blacklist">
									<fb-textarea 
										v-model="configData.blacklist" 
										rows="6"
										placeholder="请输入禁止访问的IP地址，每行一个\n支持格式：\n192.168.1.100 (单个IP)\n192.168.1.* (通配符)\n192.168.1.0/24 (CIDR)\n192.168.1.1-192.168.1.100 (IP范围)"
										:maxlength="2000">
									</fb-textarea>
									<div class="form-tip">黑名单中的IP地址将被拒绝访问，优先级高于白名单</div>
								</fb-form-item>
							</fb-col>
						</fb-row>
					</fb-form>
			</template>
		</fb-page-search>

		<!-- 测试规则对话框 -->
		<tp-dialog ref="testDialog"></tp-dialog>
		<!-- 访问日志对话框 -->
		<tp-dialog ref="logsDialog"></tp-dialog>
	</div>
</template>

<script>
export default {
	name: 'IpAccessConfig',

	data() {
		return {
			loading: false,
			saving: false,
			configData: {
				enabled: false,
				logEnabled: true,
				denyMessage: '您的IP地址无权访问此系统',
				whitelist: '',
				blacklist: ''
			}
		}
	},

	mounted() {
		this.loadConfig()
	},

	methods: {
		// 加载配置
		loadConfig() {
			this.loading = true
			this.$svc.sys.ipaccess.getConfig().then(data => {
				this.loading = false
				if (data.code === 1) {
					this.configData = {
						enabled: data.data.enabled || false,
						logEnabled: data.data.logEnabled !== false,
						denyMessage: data.data.denyMessage || '您的IP地址无权访问此系统',
						whitelist: data.data.whitelist || '',
						blacklist: data.data.blacklist || ''
					}
				} else {
					this.$message.error(data.msg || '加载配置失败')
				}
			}).catch(error => {
				this.loading = false
				this.$message.error('加载配置失败')
				console.error('加载配置失败:', error)
			})
		},

		// 保存配置
		handleSave() {
			this.saving = true
			this.$svc.sys.ipaccess.updateConfig(this.configData).then(data => {
				this.saving = false
				if (data.code === 1) {
					this.$message.success('保存成功')
				} else {
					this.$message.error(data.msg || '保存失败')
				}
			}).catch(error => {
				this.saving = false
				this.$message.error('保存失败')
				console.error('保存失败:', error)
			})
		},

		// // 测试规则
		// handleTest() {
		// 	let param = { configData: this.configData }
		// 	let options = { width: 600, height: 400 }
		// 	this.$refs.testDialog.show(import('./test.vue'), param, 'IP规则测试', options)
		// },

		// // 查看访问日志
		// handleViewLogs() {
		// 	let param = {}
		// 	let options = { width: 1200, height: 600 }
		// 	this.$refs.logsDialog.show(import('./logs.vue'), param, 'IP访问日志', options)
		// }
	}
}
</script>

<style scoped>
.form-tip {
	color: #999;
	font-size: 12px;
	margin-top: 4px;
	line-height: 1.4;
}

.fb-card {
	padding: 20px;
}
</style>