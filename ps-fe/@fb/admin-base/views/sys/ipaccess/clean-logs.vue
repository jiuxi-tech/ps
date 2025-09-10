<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="cleanForm" :label-width="120">
				<fb-row>
					<fb-col span="24">
						<fb-alert type="warning" show-icon :closable="false">
							<template slot="desc">
								清理操作将永久删除指定天数之前的访问日志，此操作不可恢复，请谨慎操作！
							</template>
						</fb-alert>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="保留天数" prop="retainDays" :rule="[{required: true, message: '请输入保留天数'}, {type: 'number', min: 1, max: 365, message: '保留天数必须在1-365之间'}]">
							<fb-input-number 
								v-model="cleanData.retainDays" 
								:min="1" 
								:max="365"
								placeholder="请输入要保留的天数">
							</fb-input-number>
							<div class="form-tip">将删除 {{ cleanData.retainDays }} 天之前的所有访问日志</div>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="清理范围">
							<fb-radio-group v-model="cleanData.cleanType">
								<fb-radio value="all">全部日志</fb-radio>
								<fb-radio value="allowed">仅允许访问的日志</fb-radio>
								<fb-radio value="denied">仅拒绝访问的日志</fb-radio>
							</fb-radio-group>
							<div class="form-tip">选择要清理的日志类型</div>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="确认清理" prop="confirmClean" :rule="[{required: true, message: '请确认清理操作'}]">
							<fb-checkbox v-model="cleanData.confirmClean">
								我已了解清理操作的风险，确认执行清理
							</fb-checkbox>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<!-- 预计清理信息 -->
				<fb-row v-if="estimateInfo">
					<fb-col span="24">
						<fb-form-item label="预计清理">
							<fb-card>
								<div class="estimate-info">
									<div class="estimate-item">
										<strong>清理时间范围：</strong>{{ estimateInfo.timeRange }}
									</div>
									<div class="estimate-item">
										<strong>预计清理记录数：</strong>
										<span class="estimate-count">{{ estimateInfo.estimateCount }}</span> 条
									</div>
									<div class="estimate-item" v-if="estimateInfo.estimateSize">
										<strong>预计释放空间：</strong>{{ estimateInfo.estimateSize }}
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
				<fb-button @on-click="handleEstimate" :loading="estimating">预估清理量</fb-button>
				<fb-button @on-click="handleClean" type="danger" :loading="cleaning" :disabled="!cleanData.confirmClean">执行清理</fb-button>
				<fb-button @on-click="handleCancel">取消</fb-button>
			</fb-flex>
		</div>
	</div>
</template>

<script>
export default {
	name: 'CleanLogs',

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
			cleaning: false,
			estimating: false,
			cleanData: {
				retainDays: 30,
				cleanType: 'all',
				confirmClean: false
			},
			estimateInfo: null
		}
	},

	methods: {
		// 预估清理量
		handleEstimate() {
			this.$refs.cleanForm.validateField('retainDays', (valid) => {
				if (valid) {
					this.estimating = true
					const estimateData = {
						retainDays: this.cleanData.retainDays,
						cleanType: this.cleanData.cleanType
					}

					// 模拟预估接口调用
					setTimeout(() => {
						this.estimating = false
						const cutoffDate = new Date()
						cutoffDate.setDate(cutoffDate.getDate() - this.cleanData.retainDays)
						
						this.estimateInfo = {
							timeRange: `${cutoffDate.toLocaleDateString()} 之前`,
							estimateCount: Math.floor(Math.random() * 10000) + 1000,
							estimateSize: (Math.random() * 100 + 10).toFixed(2) + ' MB'
						}
					}, 1000)
				} else {
					this.$message.error('请先输入正确的保留天数')
				}
			})
		},

		// 执行清理
		handleClean() {
			this.$refs.cleanForm.validate((valid) => {
				if (valid) {
					this.$confirm(
						`确定要清理 ${this.cleanData.retainDays} 天之前的${this.getCleanTypeText()}吗？此操作不可恢复！`,
						() => {
							this.cleaning = true
							const cleanParams = {
								retainDays: this.cleanData.retainDays,
								cleanType: this.cleanData.cleanType
							}

							this.$svc.sys.ipaccess.cleanExpiredLogs(cleanParams).then(data => {
								this.cleaning = false
								if (data.code === 1) {
									this.$message.success(`清理完成，共清理了 ${data.data.cleanedCount || 0} 条记录`)
									this.$parent.close(true)
								} else {
									this.$message.error(data.msg || '清理失败')
								}
							}).catch(error => {
								this.cleaning = false
								this.$message.error('清理失败')
								console.error('清理失败:', error)
							})
						},
						{
							type: 'warning',
							title: '确认清理'
						}
					)
				} else {
					this.$message.error('请检查表单输入')
				}
			})
		},

		// 取消
		handleCancel() {
			this.$parent.close()
		},

		// 获取清理类型文本
		getCleanTypeText() {
			const typeMap = {
				'all': '全部日志',
				'allowed': '允许访问的日志',
				'denied': '拒绝访问的日志'
			}
			return typeMap[this.cleanData.cleanType] || '全部日志'
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

.form-tip {
	color: #999;
	font-size: 12px;
	margin-top: 4px;
	line-height: 1.4;
}

.estimate-info {
	padding: 10px;
}

.estimate-item {
	margin-bottom: 8px;
	display: flex;
	align-items: center;
	gap: 8px;
}

.estimate-item:last-child {
	margin-bottom: 0;
}

.estimate-count {
	color: #f56c6c;
	font-weight: bold;
}
</style>