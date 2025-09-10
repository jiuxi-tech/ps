<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-property bordered label-width="150px" mode="form">

        <fb-row>
          <fb-col span="24">
            <fb-property-item label="应用名称">
              {{formData.appName}}
            </fb-property-item>
          </fb-col>
        </fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-property-item label="任务名称">
							{{formData.taskName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="cron表达式">
							{{formData.cronExpression}}
						</fb-property-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-property-item label="目标类名">
							{{formData.className}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="目标方法名">
							{{formData.methodName}}
						</fb-property-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-property-item label="上次执行时间">
							{{formData.lastTime}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="下次执行时间">
							{{formData.nextTime}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>

					<fb-col span="12">
						<fb-property-item label="任务描述">
							{{formData.remark}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="是否串行执行">
							{{formData.serialExecuted == 1 ? '是': '否'}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="串行执行阈值（秒）">
							{{formData.serialThreshold}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="是否立即执行">
							{{formData.nowExecuted == 1 ? '是': '否'}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="是否启用">
							{{formData.enabled == 1 ? '是': '否'}}
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

	import Page from "@fb/schedule-ui/src/util/Page";
	import dayjs from "dayjs";
	export default {
		name: 'schedule_view',
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

		},
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
				service: app.$svc.sys.schedule.tpscheduledtask,

				formData: {
					taskId: '',
          appName: '',
					taskName: '',
					className: '',
					methodName: '',
					cronExpression: '',
					remark: '',
					lastTime: '',
					nextTime: '',
					enabled: ''
				},
			}
		},

		// 方法
		methods: {
			init(param) {
				this.formData.taskId = param.id;
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
				this.service.view({"taskId": that.formData.taskId}).then((result) => {
					// 判断 code
					if (result.code == 1) {
						if (result.data){
							var task = result.data;
							if (task.lastTime){
								task.lastTime = dayjs(task.lastTime).format('YYYY-MM-DD HH:mm:ss')
							}

							if (task.nextTime){
								task.nextTime = dayjs(task.nextTime).format('YYYY-MM-DD HH:mm:ss')
							}
						}
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
