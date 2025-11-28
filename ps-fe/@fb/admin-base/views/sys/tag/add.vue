<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform">
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="标签名称" prop="tagName" :rule="[{required: true}]">
							<fb-input v-model="formData.tagName" placeholder="请输入标签名称"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="排序" prop="orderIndex" :rule="[{required: false}, {type: 'integer', min: 0}]">
							<fb-input v-model="formData.orderIndex" placeholder="请输入排序号"  ></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="标签描述" prop="tagDesc">
							<fb-textarea rows="3" v-model="formData.tagDesc"
										 type="text"
										 placeholder="请输入标签描述"
										 :maxlength="200">
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
import FbColorPicker from '../../../../fb-ui/packages/components/color-picker/src/FbColorPicker.vue';
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
		// 组件
		components: {FbColorPicker},
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
				service: this.$svc.sys.tag,
				// 表单数据
				formData: {
					tagId: '',
					tagName: '',
					tagColor: '#1890ff',
					orderIndex: 0,
					tagDesc: '',
					actived: 1,
					logDelete: 0, // 默认未删除
				},
			}
		},

		// 方法
		methods: {
			// 设置标题
			init(param) {
				if (param && param.id) {
					let tagId = param.id;
					this.formData.tagId = tagId;
					this.view(tagId, param.passKey);
				}
			},

			// 取消
			handleClose() {
				// 关闭弹窗，不传递参数（表示未成功保存）
				this.closeTpDialog()
			},
			// 新增/修改
			add() {
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						if (this.formData.tagId) {
							// 修改
							this.formData.passKey = this.param.passKey;
							this.service.update(this.formData).then((result) => {
								// 判断code
								if (result.code == 1) {
									this.$message.success('修改成功');
									
									// 传递成功结果和操作类型
									const action = this.meta?.action || (this.formData.tagId ? 'edit' : 'add')
									this.closeTpDialog({ success: true, action });
								} else {
									this.$message.error('修改失败:' + result.message)
								}
							})
						} else {
							// 新增，调用新增service方法
							this.service.add(this.formData).then((result) => {
								// 判断code
								if (result.code == 1) {
									this.$message.success('新增成功');
									
									// 传递成功结果和操作类型
									const action = this.meta?.action || (this.formData.tagId ? 'edit' : 'add')
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

			view(tagId, passKey) {
				// 调用查看service方法
				this.service.view({"tagId": tagId, "passKey": passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.formData = result.data
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