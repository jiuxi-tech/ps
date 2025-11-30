<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">

			<fb-property bordered label-width="140px"  >
<!--				<fb-property-item label="历史记录ID">-->
<!--					{{formData.historyId}}-->
<!--				</fb-property-item>-->
<!--				<fb-property-item label="账号ID">-->
<!--					{{formData.accountId}}-->
<!--				</fb-property-item>-->
<!--				<fb-property-item label="人员ID">-->
<!--					{{formData.personId}}-->
<!--				</fb-property-item>-->

				<fb-property-item label="人员姓名">
					{{formData.personName || '-'}}
				</fb-property-item>
          <fb-property-item label="用户名">
              {{formData.username}}
          </fb-property-item>
				<fb-property-item label="修改类型">
					<fb-tag :color="getChangeTypeColor(formData.changeType)">
						{{ getChangeTypeText(formData.changeType) }}
					</fb-tag>
				</fb-property-item>
				<fb-property-item label="修改时间">
					{{ formData.changeTime }}
				</fb-property-item>
				<fb-property-item label="操作人">
					{{formData.changedByName || '-'}}
				</fb-property-item>
				<fb-property-item label="IP地址">
					{{formData.ipAddress || '-'}}
				</fb-property-item>
				<fb-property-item label="用户代理" span="2">
					<div style="white-space: pre-wrap; word-break: break-all;">
						{{formData.userAgent || '-'}}
					</div>
				</fb-property-item>
<!--				<fb-property-item label="修改原因">-->
<!--					<div style="white-space: pre-wrap; word-break: break-all;">-->
<!--						{{formData.changeReason || '-'}}-->
<!--					</div>-->
<!--				</fb-property-item>-->
<!--				<fb-property-item label="备注信息">-->
<!--					<div style="white-space: pre-wrap; word-break: break-all;">-->
<!--						{{formData.remark || '-'}}-->
<!--					</div>-->
<!--				</fb-property-item>-->
<!--				<fb-property-item label="记录创建时间">-->
<!--					{{ formData.createdTime }}-->
<!--				</fb-property-item>-->
			</fb-property>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>
	import dayjs from "dayjs";
	
	export default {
		name: 'password-history-view',
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
		created() {},
		// 初始化方法
		mounted() {
			// 执行界面初始化
			this.init(this.param);
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.passwordHistory,
				// 表单数据
				formData: {
					historyId: '',
					accountId: '',
					personId: '',
					username: '',
					personName: '',
					changeType: null,
					changeReason: '',
					changedBy: '',
					changedByName: '',
					changeTime: '',
					ipAddress: '',
					userAgent: '',
					tenantId: '',
					remark: '',
					createdTime: '',
				},
			}
		},

		// 方法
		methods: {
			// 初始化
			init(param) {
				if (param && param.historyId) {
					let historyId = param.historyId;
					this.formData.historyId = historyId;
					this.view(historyId);
				}
			},

			// 关闭
			handleClose() {
				// 关闭弹窗，不传递参数（表示未成功保存）
				this.closeTpDialog()
			},

			// 查看详情
			view(historyId) {
				// 调用查看service方法
				this.service.view({"historyId": historyId}).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.formData = result.data;
						// 格式化时间显示
						if (this.formData.changeTime) {
							this.formData.changeTime = dayjs(this.formData.changeTime).format('YYYY-MM-DD HH:mm:ss');
						}
						if (this.formData.createdTime) {
							this.formData.createdTime = dayjs(this.formData.createdTime).format('YYYY-MM-DD HH:mm:ss');
						}
					} else {
						// 服务器返回失败
						this.$message.error('查询失败: ' + result.message)
					}
				}).catch((err) => {
					// 服务器返回失败
					console.log(err);
					this.$message.error('查询失败: ' + (err.message || '未知错误'))
				})
			},

			// 获取修改类型文本
			getChangeTypeText(changeType) {
				const typeMap = {
					1: '用户主动修改',
					2: '管理员重置',
					3: '密码重置修改',
					4: '其他'
				};
				return typeMap[changeType] || '未知';
			},

			// 获取修改类型标签颜色
			getChangeTypeColor(changeType) {
				const colorMap = {
					1: 'success',  // 绿色
					2: 'warning',  // 橙色
					3: 'danger',   // 红色
					4: 'default'   // 灰色
				};
				return colorMap[changeType] || 'default';
			},
		}
	}
</script>

<style lang="less" scoped>
</style>
