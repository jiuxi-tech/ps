<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="160">

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="参数名称" prop="pmName" :rule="[{required: true}]">
							<fb-input v-model="formData.pmName" placeholder="请输入参数名称"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="参数key" prop="pmKey" :rule="[{required: true}]">
							<fb-input v-model="formData.pmKey" placeholder="请输入参数key"></fb-input>
						</fb-form-item>
					</fb-col>

				</fb-row>

				<fb-row>

					<fb-col span="24">
						<fb-form-item label="参数值" prop="pmVal" :rule="[{required: true}]">
							<fb-input v-model="formData.pmVal" placeholder="请输入参数值"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="参数配置描述" prop="pmDesc">
							<fb-textarea :rows="2" v-model="formData.pmDesc" placeholder="请输入参数配置描述"
										 :maxlength="200">
							</fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>
                <fb-row>
					<fb-col span="12">
						<fb-form-item label="排序号" prop="orderIndex" :rule="[{required: true}, {type: 'number'}]">
							<fb-input v-model="formData.orderIndex" placeholder="请输入字典排序"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="是否启用" :rule="[{required: true}]">
							<fb-radio-group v-model="formData.enabled"
											:data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}"></fb-radio-group>
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
				service: app.$svc.sys.parameterconfig,
				// 表单数据
				formData: {
					pmId: '',
					pmKey: '',
					pmName: '',
					pmVal: '',
					pmDesc: '',
					orderIndex: '',
					creator: '',
					extend01: '',
					extend02: '',
					extend03: ''
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
				this.closeTpDialog(this.formData.pmId);
			},

			add() {
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						this.formData.passKey = this.param.passKey;
						if (this.formData.pmId) {
							// 调用修改service方法
							this.service.update(this.formData).then((result) => {
								// 判断code
								if (result && result.code == 1) {
									this.$message.success('修改成功');
									this.handleClose(this.formData.pmId);
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
									this.handleClose(result.data.pmId);
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
				this.service.view({"pmId": this.param.id, "passKey": this.param.passKey}).then((result) => {
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
