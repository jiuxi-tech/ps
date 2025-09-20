<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="120">
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="配置键" prop="configKey" :rule="[{required: true}]">
							<fb-input 
								v-model="formData.configKey" 
								placeholder="请输入配置键，如：system.name"
								:readonly="mode === 'edit'"
								>
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="配置值" prop="configValue" :rule="[{required: true}]">
							<fb-textarea 
								v-model="formData.configValue" 
								rows="3"
								placeholder="请输入配置值"
								:maxlength="1000">
							</fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="配置描述" prop="description" :rule="[{required: true}]">
							<fb-textarea 
								v-model="formData.description" 
								rows="2"
								placeholder="请输入配置描述"
								:maxlength="200">
							</fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>
				
			</fb-form>
		</div>
		<div class="tp-dialog-bottom">
			<fb-flex gap="8px" jc-end	>
				<fb-button @on-click="handleSave" type="primary" :loading="loading">保存</fb-button>
				<fb-button @on-click="handleClose">取消</fb-button>
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
		// 执行界面初始化方法
		this.init(this.param);
	},
	data() {
		return {
			loading: false,
			mode: 'add', // add, edit
			id: null,
			formData: {
				configKey: '',
				configValue: '',
				description: ''
			}
		}
	},
	methods: {
		// 初始化
		init(param) {
			// 获取传入的参数
			this.configKey = param.configKey
 
			// 如果是编辑模式，加载数据
			if ( this.configKey) {
				this.loadData(this.configKey )
          this.mode = 'edit'
			}
		},
		// 加载数据
		loadData(configKey) {
			this.$svc.sys.config.get(configKey).then(data => {
				if (data.code == 1) {
					this.formData = {
						configKey: data.data.configKey || '',
						configValue: data.data.configValue || '',
						description: data.data.description || ''
					}
				} else {
					this.$message.error(data.msg || '加载数据失败')
				}
			}).catch(error => {
				this.$message.error('加载数据失败')
				console.error('加载数据失败:', error)
			})
		},
		// 保存
		handleSave() {
			this.$refs.fbform.validate((valid) => {
				if (valid) {
					this.loading = true
					
					const saveData = {
						configKey: this.formData.configKey,
						configValue: this.formData.configValue,
						description: this.formData.description,
						
					}

					let savePromise
					if (this.configKey) {
						saveData.configKey = this.configKey
						
						savePromise = this.$svc.sys.systemconfig.update(saveData)
					} else {
						savePromise = this.$svc.sys.systemconfig.add(saveData)
					}

					savePromise.then(data => {
						this.loading = false
						if (data.code == 1) {
							this.$message.success(this.configKey ? '修改成功' : '新增成功')
							this.handleClose()
						} else {
							this.$message.error(data.msg || '保存失败')
						}
					}).catch(error => {
						this.loading = false
						this.$message.error('保存失败')
						console.error('保存失败:', error)
					})
				} else {
					this.$message.error('请检查表单输入')
				}
			})
		},
		// 取消
      handleClose() {
          // 关闭，并传递参数
          this.closeTpDialog("xxxx");
      },
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