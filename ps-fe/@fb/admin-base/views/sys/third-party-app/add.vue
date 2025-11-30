<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top" style="    padding-top: 0;">
			
			<fb-form ref="fbform">
				<!-- 如果是新增，显示提示信息已移除 -->

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="应用名称" prop="appName" :rule="[{required: true}]">
							<fb-input v-model="formData.appName" placeholder="请输入应用名称" :maxlength="50"></fb-input>
						</fb-form-item>
					</fb-col>
						</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="状态" prop="status" :rule="[{required: true}]">
							<fb-select v-model="formData.status" :data="statusOptions"></fb-select>
						</fb-form-item>
					</fb-col>
					<fb-col span="14">
						<fb-form-item label="过期时间" prop="expireTime">
							<fb-datepicker 
								v-model="formData.expireTime" 
								placeholder="请选择过期时间（留空表示永不过期）"
								format="YYYY-MM-DD HH:mm:ss"
								clearable
								 >
							</fb-datepicker>
						</fb-form-item>
					</fb-col>
				</fb-row>



				<fb-row>
					<fb-col span="24">
						<fb-form-item label="应用描述" prop="description">
							<fb-textarea 
								rows="3" 
								v-model="formData.description"
								placeholder="请输入应用描述"
								:maxlength="500">
							</fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>

				

			</fb-form>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="save">保存</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>
	import dayjs from "dayjs";

	export default {
		name: 'add',
		mixins: [],
		// 接收父组件的传参
		props: {
			param: {
				type: Object,
				require: false
			},
			parentPage: {
				type: Object,
				default: null
			},
			meta: {
				type: Object,
				default: () => ({})
			}
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
				service: this.$svc.sys.thirdPartyApp,
				// 状态选项
				statusOptions: [
					{ value: 1, label: '启用' },
					{ value: 0, label: '禁用' }
				],
				// 表单数据
				formData: {
					appId: '',
					appName: '',
					status: 1, // 默认启用
					ipWhitelist: '',
					expireTime: '',
					description: '',
					logDelete: 0, // 默认未删除
				},
			}
		},

		// 方法
		methods: {
			// 设置标题
			init(param) {
				if (param && param.id) {
					let appId = param.id;
					this.formData.appId = appId;
					this.view(appId, param.passKey);
				}
			},

			// 取消
			handleClose() {
				// 关闭弹窗，不传递参数（表示未成功保存）
				this.closeTpDialog()
			},
			
			// 新增/修改
			save() {
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						// 处理过期时间格式
						const submitData = { ...this.formData };
						if (!submitData.expireTime) {
							submitData.expireTime = ''
							// 将日期对象转换为 yyyyMMddHHmmss 格式
							//submitData.expireTime = dayjs(submitData.expireTime).format('YYYYMMDDHHmmss');
						}
						if(submitData.expireTime){
								submitData.expireTime = dayjs(submitData.expireTime).format('YYYYMMDDHHmmss');

						}

						if (this.formData.appId) {
							// 修改
							submitData.passKey = this.param.passKey;
							this.service.update(submitData).then((result) => {
								// 判断code
								if (result.code == 1) {
									this.$message.success('修改成功');
									
									// 传递成功结果和操作类型
									const action = this.meta?.action || (this.formData.appId ? 'edit' : 'add')
									this.closeTpDialog({ success: true, action });
								} else {
									this.$message.error('修改失败:' + result.message)
								}
							})
						} else {
							// 新增，调用新增service方法
							this.service.add(submitData).then((result) => {
								// 判断code
								if (result.code == 1) {
									this.$message.success('新增成功');
																	
									// 传递成功结果和操作类型
									const action = this.meta?.action || (this.formData.appId ? 'edit' : 'add')
									this.closeTpDialog({ success: true, action });
								} else {
									// 服务器返回失败
									this.$message.error('新增失败: ' + result.message)
								}
							})
						}
					}
				})
			},



			view(appId, passKey) {
				// 调用查看service方法
				this.service.view({"appId": appId, "passKey": passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.formData = result.data;
						
						// 将时间字符串转换为日期对象
						if (this.formData.expireTime) {
							this.formData.expireTime = dayjs(this.formData.expireTime, 'YYYYMMDDHHmmss').toDate();
						}
					} else {
						// 服务器返回失败
						this.$message.error('查询失败: ' + result.message)
					}
				}).catch((err) => {
					// 服务器返回失败
					console.log(err);
				})
			},
		}
	}
</script>

<style lang="less" scoped>

</style>
