<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :rule="rules">
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="原密码" prop="oldUserpwd" :rule='[{required:true}]'>
							<fb-input v-model="formData.oldUserpwd" placeholder="请输入密码" type="password"/>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="新密码" prop="userpwd" :rule='[{required:true},{ type: "password"}]'>
							<fb-input v-model="formData.userpwd" placeholder="请输入密码" type="password"/>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="确认密码" prop="confirmUserPwd" :rule='[{required:true},{ type: "password"}]'>
							<fb-input v-model="formData.confirmUserPwd" placeholder="请确认密码" type="password"/>
						</fb-form-item>
					</fb-col>
				</fb-row>
			</fb-form>
		</div>
		<div style="text-align: right; margin-top: 10px">
			<fb-button style="margin-right: 12px" @on-click="handleClose">取消</fb-button>
			<fb-button style="margin-right: 12px" type="primary" @on-click="updatePwd">确定</fb-button>
		</div>
	</div>
</template>

<script>


	export default {
		name: 'update-pwd',
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
		// 创建方法
		created() {
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化

		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.person,
				rules: {
					"confirmUserPwd": {
						validator: (rule, value, callback, source, options) => {
							// 比较两控件的值
							if (this.formData.userpwd != this.formData.confirmUserPwd) {
								// 校验未通过，返回错误信息
								return callback('确认密码不一致！');
							}
							// 校验通过
							return callback();
						}
					}
				},

				// 表单数据
				formData: {
					personId: '',
					oldUserpwd: '',
					userpwd: '',
					confirmUserPwd: '',
				},
			}
		},

		// 方法
		methods: {

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog();
			},

			updatePwd() {
				// 获取当前用户信息
				let currentUser = this.$datax.get('user');
				if (currentUser) {
					this.formData.personId = currentUser.personId
				}

				// 先校验原密码是否正确
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						this.service.updatePwd(this.formData).then((result) => {
							// 判断code
							if (result.code == 1) {
								this.$message.success('修改成功');
								this.handleClose();
							} else {
								// 服务器返回失败
								this.$message.error('修改失败')
							}
						}).catch((err) => {
							// 服务器返回失败
							console.log(err);
						})
					}
				});
			},

		}
	}
</script>

<style lang="less" scoped>

</style>
