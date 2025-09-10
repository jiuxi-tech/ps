<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="120">
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="配置键">
							<fb-input v-model="formData.configKey" readonly></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="配置值">
							<fb-textarea 
								v-model="formData.configValue" 
								rows="3"
								readonly>
							</fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="配置描述">
							<fb-textarea 
								v-model="formData.description" 
								rows="2"
								readonly>
							</fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="创建时间">
							<fb-input v-model="formData.createTime" readonly></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="更新时间">
							<fb-input v-model="formData.updateTime" readonly></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
			</fb-form>
		</div>
		<div class="tp-dialog-bottom">
			<fb-flex gap="8px" jc-end>
				<fb-button @on-click="handleClose">关闭</fb-button>
			</fb-flex>
		</div>
	</div>
</template>

<script>

export default {

	// 组件
	components: {
		// 'component-a': ComponentA,
	},

		// 接收父组件的传参
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
	// 初始化方法
	mounted() {
		this.init(this.param);
	},
	data() {
		return {
			id: null,
			formData: {
				configKey: '',
				configValue: '',
				description: '',
				createTime: '',
				updateTime: ''
			}
		}
	},
	methods: {
		// 初始化
		init() {
			// 获取传入的参数
			const param = this.param || {}
			
			// 如果传入的是configKey，直接使用
			if (param.configKey) {
				this.loadData(param.configKey)
			} else if (param.id) {
				// 如果传入的是id，需要先获取configKey
				// 这里假设id就是configKey，如果不是需要调整
				this.loadData(param.id)
			}
		},
		
		// 加载数据
		loadData(configKey) {
			this.$svc.sys.config.get(configKey).then(data => {
				 
				if (data.code == 1) {
					this.formData = {
						configKey: data.data.configKey || '',
						configValue: data.data.configValue || '',
						description: data.data.description || '',
						createTime: data.data.createTime || '',
						updateTime: data.data.updateTime || ''
					}
				} else {
					this.$message.error(data.msg || '加载数据失败')
				}
			}).catch(error => {
				this.$message.error('加载数据失败')
				console.error('加载数据失败:', error)
			})
		},

		// 关闭
		handleClose() {
			this.$parent.close()
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
</style>