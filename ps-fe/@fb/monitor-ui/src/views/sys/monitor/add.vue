<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="160">

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="客户端id" prop="clientId">
							<fb-input v-model="formData.clientId" disabled placeholder="请输入客户端id"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="系统名称" prop="applicationName" :rule="[{required: true}]">
							<fb-input v-model="formData.applicationName" placeholder="请输入系统名称"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="系统描述" prop="systemDesc" :rule="[{required: true}]">
							<fb-input v-model="formData.systemDesc" placeholder="请输入系统描述"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="mac地址" prop="macAddr" :rule="[{required: false}]">
							<fb-input v-model="formData.macAddr" disabled placeholder="请输入mac地址"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="系统部署的绝对路径" prop="absolutePath" :rule="[{required: false}]">
							<fb-input v-model="formData.absolutePath" disabled placeholder="请输入系统部署的绝对路径"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="ip链路" prop="ip" :rule="[{required: false}]">
							<fb-input v-model="formData.ip" disabled placeholder="请输入ip链路"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="系统访问地址" prop="systemUrl" :rule="[{required: false}]">
							<fb-input v-model="formData.systemUrl" placeholder="请输入系统访问地址"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="备注" prop="remark" :rule="[{required: false}]">
							<fb-textarea v-model="formData.remark" placeholder="请输入备注"></fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="扩展字段01" prop="systemUrl" :rule="[{required: false}]">
							<fb-input v-model="formData.extend01" ></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="扩展字段02" prop="systemUrl" :rule="[{required: false}]">
							<fb-input v-model="formData.extend02" ></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="扩展字段03" prop="systemUrl" :rule="[{required: false}]">
							<fb-input v-model="formData.extend03" ></fb-input>
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
				service: app.$svc.sys.monitor,
				// 表单数据
				formData: {
					clientId: '',
					applicationName: '',
					macAddr: '',
					absolutePath: '',
					systemDesc: '',
					ip: '',
					remark: '',
					systemUrl: '',
					tenantId: '',
					actived: '',
					creator: '',
					createTime: '',
					updator: '',
					updateTime: '',
					extend01: '',
					extend02: '',
					extend03: '',
					extend04: '',
					extend05: '',
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
				// 有值表示是修改方法
				if (this.param.id) { // 传ID表示修改
					this.view()
				}
			},

			// 取消
			handleClose() {
				// 隐藏
				this.closeTpDialog(this.formData.clientId);
			},

			add() {
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						this.formData.passKey = this.param.passKey;
						if (this.formData.clientId) {
							// 调用修改service方法
							this.service.update(this.formData).then((result) => {
								// 判断code
								if (result && result.code == 1) {
									this.$message.success('修改成功');
									this.handleClose(this.formData.clientId);
								} else {
									// 服务器返回失败
									this.$message.error('修改失败:' + result.message)
								};
								this.updateCount = 0;
							})
						} else {
							// 调用新增service方法
							this.service.add(this.formData).then((result) => {
								// 判断code
								if (result && result.code == 1) {
									this.$message.success('新增成功');
									this.handleClose(result.data.clientId);
								} else {
									// 服务器返回失败
									this.$message.error('新增失败:' + result.message)
								};
								this.updateCount = 0;
							})
						}
					}
				})
			},

			// 查询信息
			view() {
				// 调用新增service方法
				this.service.view({"clientId": this.param.id, "passKey": this.param.passKey}).then((result) => {
					// 判断code
					if (result && result.code == 1) {
						this.formData = result.data
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
