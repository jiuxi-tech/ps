<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="160">
			
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="记录id" prop="recordId" :rule="[{required: true}]">
							<fb-input v-model="formData.recordId" placeholder="请输入记录id"></fb-input>
						</fb-form-item>
					</fb-col>
									<fb-col span="12">
						<fb-form-item label="收件人姓名" prop="personName" :rule="[{required: true}]">
							<fb-input v-model="formData.personName" placeholder="请输入收件人姓名"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="电子邮箱" prop="email" :rule="[{required: true}]">
							<fb-input v-model="formData.email" placeholder="请输入电子邮箱"></fb-input>
						</fb-form-item>
					</fb-col>
									<fb-col span="12">
						<fb-form-item label="发送时间" prop="sendTime" :rule="[{required: true}]">
							<fb-input v-model="formData.sendTime" placeholder="请输入发送时间"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="发送内容" prop="message" :rule="[{required: true}]">
							<fb-input v-model="formData.message" placeholder="请输入发送内容"></fb-input>
						</fb-form-item>
					</fb-col>
									<fb-col span="12">
						<fb-form-item label="发送状态（1:成功，0：失败）" prop="status" :rule="[{required: true}]">
							<fb-input v-model="formData.status" placeholder="请输入发送状态（1:成功，0：失败）"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="租户id" prop="tenantId" :rule="[{required: true}]">
							<fb-input v-model="formData.tenantId" placeholder="请输入租户id"></fb-input>
						</fb-form-item>
					</fb-col>
									<fb-col span="12">
						<fb-form-item label="是否有效" prop="actived" :rule="[{required: true}]">
							<fb-input v-model="formData.actived" placeholder="请输入是否有效"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="创建人" prop="creator" :rule="[{required: true}]">
							<fb-input v-model="formData.creator" placeholder="请输入创建人"></fb-input>
						</fb-form-item>
					</fb-col>
									<fb-col span="12">
						<fb-form-item label="创建时间" prop="createTime" :rule="[{required: true}]">
							<fb-input v-model="formData.createTime" placeholder="请输入创建时间"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="修改人" prop="updator" :rule="[{required: true}]">
							<fb-input v-model="formData.updator" placeholder="请输入修改人"></fb-input>
						</fb-form-item>
					</fb-col>
									<fb-col span="12">
						<fb-form-item label="修改时间" prop="updateTime" :rule="[{required: true}]">
							<fb-input v-model="formData.updateTime" placeholder="请输入修改时间"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="扩展字段01" prop="extend01" :rule="[{required: true}]">
							<fb-input v-model="formData.extend01" placeholder="请输入扩展字段01"></fb-input>
						</fb-form-item>
					</fb-col>
									<fb-col span="12">
						<fb-form-item label="扩展字段02" prop="extend02" :rule="[{required: true}]">
							<fb-input v-model="formData.extend02" placeholder="请输入扩展字段02"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="扩展字段03" prop="extend03" :rule="[{required: true}]">
							<fb-input v-model="formData.extend03" placeholder="请输入扩展字段03"></fb-input>
						</fb-form-item>
					</fb-col>
									<fb-col span="12">
						<fb-form-item label="扩展字段04" prop="extend04" :rule="[{required: true}]">
							<fb-input v-model="formData.extend04" placeholder="请输入扩展字段04"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="扩展字段05" prop="extend05" :rule="[{required: true}]">
							<fb-input v-model="formData.extend05" placeholder="请输入扩展字段05"></fb-input>
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
				service: app.$svc.core.tpsendmailrecord,
				// 表单数据
				formData: {
		recordId: '',
						personName: '',
						email: '',
						sendTime: '',
						message: '',
						status: '',
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
				this.closeTpDialog(this.formData.recordId);
			},

			add() {
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						this.formData.passKey = this.param.passKey;
						if (this.formData.recordId) {
							// 调用修改service方法
							this.service.update(this.formData).then((result) => {
								// 判断code
								if (result && result.code == 1) {
									this.$message.success('修改成功');
									this.handleClose(this.formData.recordId);
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
									this.handleClose(result.data.recordId);
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
				this.service.view({"recordId": this.param.id, "passKey": this.param.passKey}).then((result) => {
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
