<template>
	<div>
		<div :style="param.styles">
			<fb-form ref="fbform" :label-width="160">

				<fb-row>
					<!--					<fb-col span="12">-->
					<!--						<fb-form-item label="模块id" prop="mid" :rule="[{required: true}]">-->
					<!--							<fb-input v-model="formData.mid" placeholder="请输入模块id"></fb-input>-->
					<!--						</fb-form-item>-->
					<!--					</fb-col>-->
					<fb-col span="12">
						<fb-form-item label="模块名称" prop="mname" :rule="[{required: true}]">
							<fb-input v-model="formData.mname" placeholder="请输入模块名称"></fb-input>
						</fb-form-item>
					</fb-col>

					<fb-col span="12">
						<fb-form-item label="模块编码" prop="mcode" :rule="[{required: true}]">
							<fb-input v-model="formData.mcode" placeholder="请输入模块编码 表名,自动添加前缀: tp_json_"></fb-input>
						</fb-form-item>
					</fb-col>

				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="模块简介" prop="mdesc" :rule="[{required: true}]">
							<fb-textarea v-model="formData.mdesc" placeholder="请输入模块简介"></fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="有效标志" prop="actived" :rule="[{required: true}]">
							<fb-radio-group v-model="formData.actived" :data="[
								{label:'是', value:1 },
								{label:'否', value:0 },
							]"></fb-radio-group>
						</fb-form-item>
					</fb-col>
					<!--					<fb-col span="12">-->
					<!--						<fb-form-item label="创建人" prop="creator" :rule="[{required: true}]">-->
					<!--							<fb-input v-model="formData.creator" placeholder="请输入创建人"></fb-input>-->
					<!--						</fb-form-item>-->
					<!--					</fb-col>-->
				</fb-row>

				<!--				<fb-row>-->
				<!--					<fb-col span="12">-->
				<!--						<fb-form-item label="创建时间" prop="createTime" :rule="[{required: true}]">-->
				<!--							<fb-input v-model="formData.createTime" placeholder="请输入创建时间"></fb-input>-->
				<!--						</fb-form-item>-->
				<!--					</fb-col>-->
				<!--					<fb-col span="12">-->
				<!--						<fb-form-item label="更新人" prop="updator" :rule="[{required: true}]">-->
				<!--							<fb-input v-model="formData.updator" placeholder="请输入更新人"></fb-input>-->
				<!--						</fb-form-item>-->
				<!--					</fb-col>-->
				<!--				</fb-row>-->

				<!--				<fb-row>-->
				<!--					<fb-col span="12">-->
				<!--						<fb-form-item label="更新时间" prop="updateTime" :rule="[{required: true}]">-->
				<!--							<fb-input v-model="formData.updateTime" placeholder="请输入更新时间"></fb-input>-->
				<!--						</fb-form-item>-->
				<!--					</fb-col>-->
				<!--					<fb-col span="12">-->
				<!--						<fb-form-item label="扩展字段01" prop="extend01" :rule="[{required: true}]">-->
				<!--							<fb-input v-model="formData.extend01" placeholder="请输入扩展字段01"></fb-input>-->
				<!--						</fb-form-item>-->
				<!--					</fb-col>-->
				<!--				</fb-row>-->

				<!--				<fb-row>-->
				<!--					<fb-col span="12">-->
				<!--						<fb-form-item label="扩展字段02" prop="extend02" :rule="[{required: true}]">-->
				<!--							<fb-input v-model="formData.extend02" placeholder="请输入扩展字段02"></fb-input>-->
				<!--						</fb-form-item>-->
				<!--					</fb-col>-->
				<!--					<fb-col span="12">-->
				<!--						<fb-form-item label="扩展字段03" prop="extend03" :rule="[{required: true}]">-->
				<!--							<fb-input v-model="formData.extend03" placeholder="请输入扩展字段03"></fb-input>-->
				<!--						</fb-form-item>-->
				<!--					</fb-col>-->
				<!--				</fb-row>-->
			</fb-form>
		</div>

		<div
			style="box-sizing: border-box;text-align: right;background-color: #FFFFFF;position: relative;bottom: 0;left: 0;right: 0;padding: 16px;border-top: 1px solid #ccc">
			<fb-button style="margin-right: 12px" type="primary" @on-click="add">保存</fb-button>
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
			require: false,
		},
		parentPage: {
			type: Object,
			default: null,
		},
	},
	// 组件
	components: {
		// 'component-a': ComponentA,
	},
	// 创建方法
	created () {
		// 记录原来的默认值，用于表单重置
	},
	// 初始化方法
	mounted () {
		// 执行界面初始化
		this.init(this.param)
	},
	data () {
		return {
			// 请求的 service
			service: app.$svc.sys.custom.module,

			// 表单数据
			formData: {
				mid: '',
				mname: '',
				mcode: '',
				mdesc: '',
				actived: '',
				creator: '',
				createTime: '',
				updator: '',
				updateTime: '',
				extend01: '',
				extend02: '',
				extend03: '',
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
		init (param) {
			// 有值表示是修改方法
			if (param.id) { // 传ID表示修改
				this.formData.mid = param.id
				this.view()
			}
		},

		// 取消
		handleClose () {
			// 隐藏
			this.closeTpDialog(this.formData.mid)
		},

		add () {
			let that = this
			// 界面校验
			this.$refs.fbform.validate((result) => {
				if (result === true) {
					if (that.formData.mid) {
						// 调用修改service方法
						that.service.update(that.formData).then((result) => {
							// 判断code
							if (result.code == 1) {
								that.$message.success('修改成功')
								this.handleClose(that.formData.mid)
							} else {
								// 服务器返回失败
								that.$message.error('修改失败:' + result.message)
							}

							that.updateCount = 0
						})
					} else {
						// 调用新增service方法
						that.service.add(that.formData).then((result) => {
							// 判断code
							if (result.code == 1) {
								that.$message.success('新增成功')
								this.handleClose(result.data.mid)
							} else {
								// 服务器返回失败
								that.$message.error('新增失败:' + result.message)
							}

							that.updateCount = 0
						})
					}
				}
			})
		},

		// 查询信息
		view () {

			// 调用新增service方法
			this.service.view({'mid': this.formData.mid}).then((result) => {
				// 判断code
				if (result.code == 1) {
					this.formData = result.data
				} else {
					// 服务器返回失败
					this.$message.error('错误提示:' + result.message)
				}
			})
		},
	},
}
</script>

<style lang="less" scoped>

</style>
