<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form :label-width="160" ref="fbform">

				<fb-row>
					<fb-col span="24">
						<fb-form-item :rule="[{required: true}]" label="合作方名称" prop="partnerName">
							<fb-input placeholder="请输入合作方名称" v-model="formData.partnerName"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item :rule="[{required: false}]" label="合作方描述" prop="partnerDesc">
							<fb-textarea maxlength="150" placeholder="请输入合作方描述" rows="2"
										 v-model="formData.partnerDesc"/>
						</fb-form-item>
					</fb-col>
				</fb-row>

			</fb-form>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button @on-click="add" style="margin-right: 12px" type="primary">保存</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>


<script>



	export default {
		name: 'add-basicinfo',
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
				service: app.$svc.sys.accountthird,
				// 表单数据
				formData: {
					partnerName: '',
					partnerDesc: '',
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
			},

			// 取消
			handleClose() {
				// 隐藏
				this.closeTpDialog();
			},

			add() {
				let that = this
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						// 调用新增service方法
						that.service.add(that.formData).then((result) => {
							// 判断code
							if (result.code == 1) {
								that.$message.success('新增成功');
								this.handleClose();
							} else {
								// 服务器返回失败
								that.$message.error('新增失败:' + result.message)
							}
						})
					}
				})
			},
		}
	}
</script>

<style lang="less" scoped>

</style>
