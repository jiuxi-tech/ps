<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">

			<fb-property bordered label-width="140px" mode="form">
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="标签名称">
							{{formData.tagName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="排序">
							{{formData.orderIndex}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="标签描述">
							{{formData.tagDesc}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="创建人">
							{{formData.createPersonName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="创建时间">
							{{ formData.createTime }}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="修改人">
							{{formData.updatePersonName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="修改时间">
							{{ formData.updateTime }}
						</fb-property-item>
					</fb-col>
				</fb-row>
			</fb-property>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>
import FbRadioGroup from '../../../../fb-ui/packages/components/radio-group/src/FbRadioGroup.vue';
import dayjs from "dayjs";
	export default {
		name: 'view',
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
			}
		},
		// 组件
		components: {FbRadioGroup},
		// 创建方法
		created() {},
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
					category: '',
					tagColor: '',
					orderIndex: '',
					tagDesc: '',
					actived: 1,
					logDelete: 0,
					createTime: '',
					createPersonName: '',
					updateTime: '',
					updatePersonName: '',
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
				// 关闭，并传递参数
				this.closeTpDialog("xxxx");
			},

			view(tagId, passKey) {
				// 调用查看service方法
				this.service.view({"tagId": tagId, "passKey": passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.formData = result.data;
						// 格式化时间显示
						if (this.formData.createTime) {
							this.formData.createTime = dayjs(this.formData.createTime).format('YYYY-MM-DD HH:mm:ss');
						}
						if (this.formData.updateTime) {
							this.formData.updateTime = dayjs(this.formData.updateTime).format('YYYY-MM-DD HH:mm:ss');
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