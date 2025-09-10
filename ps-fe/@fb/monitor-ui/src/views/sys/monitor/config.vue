<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="160">
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="cpu报警阈值" prop="cpuThreshold" :rule="[{required: true}, {type: 'number', min: 1, max: 100}]">
							<fb-input v-model="formData.cpuThreshold" placeholder="cpu报警阈值"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="内存报警阈值" prop="memoryThreshold" :rule="[{required: true}, {type: 'number', min: 1, max: 100}]">
							<fb-input v-model="formData.memoryThreshold" placeholder="内存报警阈值"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="磁盘报警阈值" prop="diskThreshold" :rule="[{required: true}, {type: 'number', min: 1, max: 100}]">
							<fb-input v-model="formData.diskThreshold" placeholder="磁盘报警阈值"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="是否发送邮件" prop="email" :rule="[{required: true}]">
							<fb-select :data="[{value: 1, label: '是'}
                                                   ,{value: 0, label: '否'}
                                                   ]"
									   placeholder="请选择"
									   v-model="formData.sendMail">
							</fb-select>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="系统负责人" prop="principal" :rule="[{required: (formData.sendMail === 1)}]">
							<fb-input v-model="formData.principal" placeholder="系统负责人,多个使用逗号分隔"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="负责人邮箱" prop="email" :rule="[{required: (formData.sendMail === 1)}]">
							<fb-input v-model="formData.email" placeholder="负责人邮箱,多个使用逗号分隔"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="负责人手机号" prop="mobile" :rule="[{required: (formData.sendMail === 1)}]">
							<fb-input v-model="formData.mobile" placeholder="负责人手机号,多个使用逗号分隔"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
			</fb-form>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="add">保存</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>


<script>

	export default {
		name: 'add-basicinfo',

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
			// 执行界面初始化
			this.init();
		},
		data() {
			return {
				// 请求的 service
				service: app.$svc.sys.monitorconfig,
				// 表单数据
				formData: {
					configId: '',
					cpuThreshold: '',
					memoryThreshold: '',
					diskThreshold: '',
					sendMail: 1,
					principal: '',
					mobile: '',
					email: '',
				},
			}
		},

		// 方法
		methods: {

			/**
			 * 显示窗口
			 * param 参数
			 * title 标题
			 */
			init() {
				this.view()
			},

			// 取消
			handleClose() {
				// 隐藏
				this.closeTpDialog();
			},

			add() {
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						// 调用修改service方法
						this.service.update(this.formData).then((result) => {
							// 判断code
							if (result && result.code == 1) {
								this.$message.success('修改成功');
								this.handleClose();
							} else {
								// 服务器返回失败
								this.$message.error('修改失败:' + result.message)
							};
						})
					}
				})
			},

			// 查询信息
			view() {
				// 调用新增service方法
				this.service.view({}).then((result) => {
					// 判断code
					if (result && result.code == 1) {
						// 如果返回数据不为空，则更新表单数据
						if (result.data) {
							this.formData = result.data
						}
						// 如果返回数据为空，保持默认的formData结构
					} else {
						// 服务器返回失败
						this.$message.error('错误提示:' + result.message)
					}
				})
			},
		}
	}
</script>

<style lang="less" scoped>

</style>
