<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="testForm" :label-width="120">
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="测试IP地址" prop="testIp" :rule="[{required: true, message: '请输入要测试的IP地址'}]">
							<fb-input 
								v-model="testData.testIp" 
								placeholder="请输入IP地址，如：192.168.1.100"
								clearable>
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="测试结果">
							<div v-if="!testResult" class="test-placeholder">
								点击"开始测试"按钮查看IP访问权限
							</div>
							<div v-else class="test-result">
								<fb-alert 
									:type="testResult.allowed ? 'success' : 'error'"
									show-icon
									:closable="false">
									<template slot="desc">
										<div><strong>访问结果：</strong>{{ testResult.allowed ? '允许访问' : '拒绝访问' }}</div>
										<div v-if="testResult.matchedRule"><strong>匹配规则：</strong>{{ testResult.matchedRule }}</div>
										<div v-if="testResult.ruleType"><strong>规则类型：</strong>{{ getRuleTypeText(testResult.ruleType) }}</div>
										<div v-if="testResult.reason"><strong>拒绝原因：</strong>{{ testResult.reason }}</div>
									</template>
								</fb-alert>
							</div>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<!-- 当前配置预览 -->
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="当前配置">
							<fb-card>
								<div class="config-preview">
									<div class="config-item">
										<strong>IP访问控制：</strong>
										<fb-tag :color="configData.enabled ? 'green' : 'red'">
											{{ configData.enabled ? '已启用' : '已禁用' }}
										</fb-tag>
									</div>
									<div class="config-item">
										<strong>白名单规则数：</strong>{{ getWhitelistCount() }}
									</div>
									<div class="config-item">
										<strong>黑名单规则数：</strong>{{ getBlacklistCount() }}
									</div>
								</div>
							</fb-card>
						</fb-form-item>
					</fb-col>
				</fb-row>
			</fb-form>
		</div>
		<div class="tp-dialog-bottom">
			<fb-flex gap="8px" jc-end>
				<fb-button @on-click="handleTest" type="primary" :loading="testing">开始测试</fb-button>
				<fb-button @on-click="handleReset">重置</fb-button>
				<fb-button @on-click="handleCancel">关闭</fb-button>
			</fb-flex>
		</div>
	</div>
</template>

<script>
export default {
	name: 'IpAccessTest',

	props: {
		param: {
			type: Object,
			require: false
		},
		parentPage: {
			type: Object,
			default: null
		}
	},

	data() {
		return {
			testing: false,
			testData: {
				testIp: ''
			},
			testResult: null,
			configData: {
				enabled: false,
				whitelist: '',
				blacklist: ''
			}
		}
	},

	mounted() {
		if (this.param && this.param.configData) {
			this.configData = this.param.configData
		}
	},

	methods: {
		// 执行测试
		handleTest() {
			this.$refs.testForm.validate((valid) => {
				if (valid) {
					this.testing = true
					const testData = {
						testIp: this.testData.testIp,
						config: this.configData
					}

					this.$svc.sys.ipaccess.testIpRule(testData).then(data => {
						this.testing = false
						if (data.code === 1) {
							this.testResult = data.data
						} else {
							this.$message.error(data.msg || '测试失败')
						}
					}).catch(error => {
						this.testing = false
						this.$message.error('测试失败')
						console.error('测试失败:', error)
					})
				} else {
					this.$message.error('请检查输入')
				}
			})
		},

		// 重置
		handleReset() {
			this.testData.testIp = ''
			this.testResult = null
		},

		// 关闭
		handleCancel() {
			this.$parent.close()
		},

		// 获取规则类型文本
		getRuleTypeText(ruleType) {
			const typeMap = {
				'WHITELIST': '白名单',
				'BLACKLIST': '黑名单',
				'EXACT': '精确匹配',
				'WILDCARD': '通配符',
				'CIDR': 'CIDR网段',
				'RANGE': 'IP范围'
			}
			return typeMap[ruleType] || ruleType
		},

		// 获取白名单规则数量
		getWhitelistCount() {
			if (!this.configData.whitelist) return 0
			return this.configData.whitelist.split('\n').filter(line => line.trim()).length
		},

		// 获取黑名单规则数量
		getBlacklistCount() {
			if (!this.configData.blacklist) return 0
			return this.configData.blacklist.split('\n').filter(line => line.trim()).length
		}
	}
}
</script>

<style scoped>
.tp-dialog {
	display: flex;
	flex-direction: column;
	height: 100%;
}

.tp-dialog-top {
	flex: 1;
	padding: 20px;
	overflow-y: auto;
}

.tp-dialog-bottom {
	padding: 15px 20px;
	border-top: 1px solid #e8eaec;
	text-align: right;
	background: #fafafa;
}

.test-placeholder {
	color: #999;
	padding: 20px;
	text-align: center;
	border: 1px dashed #ddd;
	border-radius: 4px;
}

.test-result {
	margin-top: 10px;
}

.config-preview {
	padding: 10px;
}

.config-item {
	margin-bottom: 8px;
	display: flex;
	align-items: center;
	gap: 8px;
}

.config-item:last-child {
	margin-bottom: 0;
}
</style>