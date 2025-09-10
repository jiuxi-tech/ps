<template>
	<div>
		<div :style="param.styles" >
			<fb-form ref="fbform" :label-width="160">
			
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="表单id" prop="fid" :rule="[{required: true}]">
							<fb-input v-model="formData.fid" placeholder="请输入表单id"></fb-input>
						</fb-form-item>
					</fb-col>
									<fb-col span="12">
						<fb-form-item label="模块id" prop="mid" :rule="[{required: true}]">
							<fb-input v-model="formData.mid" placeholder="请输入模块id"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="表单归属 app/pc" prop="fSource" :rule="[{required: true}]">
							<fb-input v-model="formData.fSource" placeholder="请输入表单归属 app/pc"></fb-input>
						</fb-form-item>
					</fb-col>
									<fb-col span="12">
						<fb-form-item label="表单类型 list/edit/view" prop="ftype" :rule="[{required: true}]">
							<fb-input v-model="formData.ftype" placeholder="请输入表单类型 list/edit/view"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="表单字段" prop="fjson" :rule="[{required: true}]">
							<fb-input v-model="formData.fjson" placeholder="请输入表单字段"></fb-input>
						</fb-form-item>
					</fb-col>
									<fb-col span="12">
						<fb-form-item label="有效标志" prop="actived" :rule="[{required: true}]">
							<fb-input v-model="formData.actived" placeholder="请输入有效标志"></fb-input>
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
						<fb-form-item label="更新人" prop="updator" :rule="[{required: true}]">
							<fb-input v-model="formData.updator" placeholder="请输入更新人"></fb-input>
						</fb-form-item>
					</fb-col>
									<fb-col span="12">
						<fb-form-item label="更新时间" prop="updateTime" :rule="[{required: true}]">
							<fb-input v-model="formData.updateTime" placeholder="请输入更新时间"></fb-input>
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
				</fb-row>
						</fb-form>
		</div>

		<div style="box-sizing: border-box;text-align: right;background-color: #FFFFFF;position: relative;bottom: 0;left: 0;right: 0;padding: 16px;border-top: 1px solid #ccc">
			<fb-button style="margin-right: 12px" type="primary" @on-click="add">保存</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>


<script>

	import Page from "@/util/Page"

	export default {
		name: 'add-basicinfo',
		mixins: [
			Page
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
				service: app.$svc.core.tpcustomform,
				// 表单数据
				formData: {
		fid: '',
						mid: '',
						fSource: '',
						ftype: '',
						fjson: '',
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
			init(param) {
				// 有值表示是修改方法
				if (param.id) { // 传ID表示修改
					this.formData.fid = param.id;
					this.view()
				}
			},

			// 取消
			handleClose() {
				// 隐藏
				this.closeTpDialog(this.formData.fid);
			},

			add() {
				let that = this
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						if (that.formData.fid) {
							// 调用修改service方法
							that.service.update(that.formData).then((result) => {
								// 判断code
								if (result.code == 1) {
									that.$message.success('修改成功');
									this.handleClose(that.formData.fid);
								} else {
									// 服务器返回失败
									that.$message.error('修改失败:' + result.message)
								};
								that.updateCount = 0;
							})
						} else {
							// 调用新增service方法
							that.service.add(that.formData).then((result) => {
								// 判断code
								if (result.code == 1) {
									that.$message.success('新增成功');
									this.handleClose(result.data.fid);
								} else {
									// 服务器返回失败
									that.$message.error('新增失败:' + result.message)
								};
								that.updateCount = 0;
							})
						}
					}
				})
			},

			// 查询信息
			view() {

				// 调用新增service方法
				this.service.view({"fid": this.formData.fid}).then((result) => {
					// 判断code
					if (result.code == 1) {
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
