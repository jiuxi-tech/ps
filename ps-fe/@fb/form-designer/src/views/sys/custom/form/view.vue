<template>
	<fb-property bordered label-width="130px" mode="form">
		<div :style="param.styles">


			<fb-row>
				<fb-col span="12">
					<fb-property-item label="表单id">
						{{formData.fid}}
					</fb-property-item>
				</fb-col>
				<fb-col span="12">
					<fb-property-item label="模块id">
						{{formData.mid}}
					</fb-property-item>
				</fb-col>
			</fb-row>

			<fb-row>
				<fb-col span="12">
					<fb-property-item label="表单归属 app/pc">
						{{formData.fSource}}
					</fb-property-item>
				</fb-col>
				<fb-col span="12">
					<fb-property-item label="表单类型 list/edit/view">
						{{formData.ftype}}
					</fb-property-item>
				</fb-col>
			</fb-row>

			<fb-row>
				<fb-col span="12">
					<fb-property-item label="表单字段">
						{{formData.fjson}}
					</fb-property-item>
				</fb-col>
				<fb-col span="12">
					<fb-property-item label="有效标志">
						{{formData.actived}}
					</fb-property-item>
				</fb-col>
			</fb-row>

			<fb-row>
				<fb-col span="12">
					<fb-property-item label="创建人">
						{{formData.creator}}
					</fb-property-item>
				</fb-col>
				<fb-col span="12">
					<fb-property-item label="创建时间">
						{{formData.createTime}}
					</fb-property-item>
				</fb-col>
			</fb-row>

			<fb-row>
				<fb-col span="12">
					<fb-property-item label="更新人">
						{{formData.updator}}
					</fb-property-item>
				</fb-col>
				<fb-col span="12">
					<fb-property-item label="更新时间">
						{{formData.updateTime}}
					</fb-property-item>
				</fb-col>
			</fb-row>

			<fb-row>
				<fb-col span="12">
					<fb-property-item label="扩展字段01">
						{{formData.extend01}}
					</fb-property-item>
				</fb-col>
				<fb-col span="12">
					<fb-property-item label="扩展字段02">
						{{formData.extend02}}
					</fb-property-item>
				</fb-col>
			</fb-row>

			<fb-row>
				<fb-col span="12">
					<fb-property-item label="扩展字段03">
						{{formData.extend03}}
					</fb-property-item>
				</fb-col>
			</fb-row>

		</div>
		<div
			style="box-sizing: border-box;text-align: right;background-color: #FFFFFF;position: relative;bottom: 0;left: 0;right: 0;padding: 16px;border-top: 1px solid #ccc">
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</fb-property>
</template>

<script>

	import Page from "@/util/Page"

	export default {
		name: 'view',
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
		components: {},
		// 创建方法
		created() {
			// 记录原来的默认值，用于表单重置
		},
		// 初始化方法
		mounted() {
			this.init(this.param);
		},
		data() {
			return {
				// 请求的 service
				service: app.$svc.core.tpcustomform,

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
			init(param) {
				this.formData.fid = param.id;
				this.view();
			},
			// 取消
			handleClose() {
				let param = {};
				this.closeTpDialog(param);
			},
			// 查看
			view() {
				// 调用新增service方法
				let that = this;
				this.service.view({"fid": that.formData.fid}).then((result) => {
					// 判断code
					if (result.code == 1) {
						that.formData = result.data
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
