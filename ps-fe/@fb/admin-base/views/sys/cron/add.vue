<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="180">
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="说明:">
							任务名称 = 目标类_目标方法；修改目标类和目标方法不会修改已经在运行的定时任务所调用的类和方法，只是任务名称改变。
							如果目标方法有参数，如：methodName(aa,bb)写即可，参数间不要空格。
						</fb-form-item>
					</fb-col>
				</fb-row>
				<br/>
				<fb-row>
					<fb-col span="22">
						<fb-form-item label="任务组" prop="jobGroupName">
							<fb-input v-model="formData.jobGroupName" placeholder="请输入任务组"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="22">
						<fb-form-item label="任务名称">
							<fb-input v-model="formData.jobName" readonly></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="22">
						<fb-form-item label="目标类" prop="beanName" :rule="{required: true}">
							<fb-input v-model="formData.beanName" placeholder="请输入目标类"
									  @on-blur="jobNameFormat"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="22">
						<fb-form-item label="目标方法" prop="methodName" :rule='{required:true}'>
							<fb-input v-model="formData.methodName" placeholder="请输入目标方法"
									  @on-blur="jobNameFormat"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>


				<fb-row>
					<fb-col span="22">
						<fb-form-item label="cron表达式" prop="cronExpression" :rule='{required:true}'>
							<fb-input v-model="formData.cronExpression" placeholder="如：0 5 * * * ?"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>


				<fb-row>
					<fb-col span="22">
						<fb-form-item :rule='{required:true}' label="串发执行" prop="concurrent">
							<fb-radio-group :data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}"
											v-model="formData.concurrent"></fb-radio-group>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="22">
						<fb-form-item label="任务描述">
							<fb-textarea rows="5" v-model="formData.description"
										 placeholder="请输入任务描述"
										 maxlength="200">
							</fb-textarea>
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
		name: 'add',
		mixins: [

		],
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
			this.init(this.param);
		},
		data() {
			return {
				// 请求的 service
				service: app.$svc.sys.cron,

				// 表单数据
				formData: {
					jobName: '',
					oldJobName: '',
					beanName: '',
					methodName: '',
					jobGroupName: '',
					cronExpression: '',
					concurrent: 0,
					description: '',
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
			init(param) {
				// 有值表示是修改方法
				if (param.jobName && param.jobGroupName) { // 传ID表示修改
					this.formData.jobName = param.jobName;
					this.formData.jobGroupName = param.jobGroupName;
					this.view()
				}
			},

			// 取消
			handleClose() {
				let param = {};
				this.closeTpDialog(param);
			},


			add() {
				let that = this;
				// 图片处理，后端list接收
				that.formData.file = that.file;
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						// 修改
						this.service.addOrUpdate(that.formData).then((result) => {
							// 判断code
							if (result.code == 1) {
								this.$message.success('成功');
								this.closeTpDialog();
							} else {
								// 服务器返回失败
								this.$message.error('失败:' + result.data.message)
							}
						})
					}
				})
			},
			// 查询信息
			view() {
				// 调用新增service方法
				this.service.view({
					"jobName": this.formData.jobName,
					"jobGroupName": this.formData.jobGroupName
				}).then((result) => {
					// 判断code
					if (result.code == 1) {
						if (result.data.jobName) {
							let jobName = result.data.jobName;
							result.data.beanName = jobName.split("_")[0];
							result.data.methodName = jobName.split("_")[1];
						}
						this.formData = result.data
					} else {
						// 服务器返回失败
						this.$message.error('错误提示:' + result.message)
					}
				})
			},

			jobNameFormat() {
				if (this.formData.beanName || this.formData.methodName) {
					this.formData.jobName = this.formData.beanName + "_" + this.formData.methodName;
				}
			},

		}
	}
</script>

<style lang="less" scoped>

</style>
