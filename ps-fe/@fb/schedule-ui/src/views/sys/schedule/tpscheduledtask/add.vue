<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="160">
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="说明:" style="color: red; line-height: 30px;">
							所有参数新增/修改后, 立即生效, 请谨慎操作。
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="22">
						<fb-form-item label="应用名称" prop="appName" :rule="[{required: true}]">
							<fb-input v-model="formData.appName" placeholder="应用名称，取spring.application.name"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
        <fb-row>
          <fb-col span="22">
            <fb-form-item label="任务名称" prop="taskName" :rule="[{required: true}]">
              <fb-input v-model="formData.taskName" placeholder="自定义名称"></fb-input>
            </fb-form-item>
          </fb-col>
        </fb-row>
				<fb-row>
					<fb-col span="22">
						<fb-form-item label="bean名称" prop="className" :rule="[{required: true}]">
							<fb-input v-model="formData.className" placeholder="spring管理的bean名称，短名称。如：userServiceImpl"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="22">
						<fb-form-item label="方法名" prop="methodName" :rule="[{required: true}]">
							<fb-tooltip slot="label-extra" placement="top">
								<fb-container slot="content" width="260px">定时任务执行的业务方法不能有任何参数。</fb-container>
								<fb-icon name="information"/>
							</fb-tooltip>
							<fb-input v-model="formData.methodName" placeholder="不带任何参数和括号。如：addHisData"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="22">
						<fb-form-item label="任务描述" prop="remark" :rule="[{required: false}]">
							<fb-textarea rows="1" v-model="formData.remark"
										 placeholder="请输入任务描述"
										 maxlength="200">
							</fb-textarea>
						</fb-form-item>

					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="16">
						<fb-form-item label="cron表达式" prop="cronExpression" :rule="[{required: true}]">
							<fb-input v-model="formData.cronExpression" placeholder="请输入cron表达式"></fb-input>
						</fb-form-item>
					</fb-col>
					&nbsp;
					<fb-col span="6">
						<fb-button style="margin-left: 10px" @on-click="validateCron(formData.cronExpression)" editor>校验</fb-button>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="10">
						<fb-form-item :rule='{required:true}' label="是否串行执行" prop="serialExecuted">
							<fb-tooltip slot="label-extra" placement="top">
								<fb-container slot="content" width="260px">选择"是"时，如果定时任务的执行时间超过了配置的间隔时间，则会延后执行下次定时任务。</fb-container>
								<fb-icon name="information"/>
							</fb-tooltip>
							<fb-radio-group :data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}"
											v-model="formData.serialExecuted"></fb-radio-group>
						</fb-form-item>
					</fb-col>
					<fb-col span="10" v-if="formData.serialExecuted == 1">
						<fb-form-item label="串行执行阈值(秒)" prop="serialThreshold" :rule="[{required: true}]">
							<fb-tooltip slot="label-extra" placement="top">
								<fb-container slot="content" width="260px">默认10分钟。举例：定时任务配置是每隔30分钟执行一次，但是业务执行比较慢，需要执行50分钟才能完成。
									下个任务到点后，会延时10分钟再执行。</fb-container>
								<fb-icon name="information"/>
							</fb-tooltip>
							<fb-input v-model="formData.serialThreshold" placeholder=""></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="10">
						<fb-form-item :rule='{required:true}' label="是否立即执行" prop="nowExecuted">
							<fb-tooltip slot="label-extra" placement="top">
								<fb-container slot="content" width="260px">选择"是"时，系统重启时, 会立即执行1次“系统关闭期间未执行的定时任务”。
									如果超过执行时间后，再执行任务不符合业务需求，请选择否；90%以上的情况需要选择是。</fb-container>
								<fb-icon name="information"/>
							</fb-tooltip>
							<fb-radio-group :data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}"
											v-model="formData.nowExecuted"></fb-radio-group>

						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item :rule='{required:true}' label="是否启用" prop="enabled">
							<fb-tooltip slot="label-extra" placement="top">
								<fb-container slot="content" width="260px">选择否时，当前定时任务不会被执行。</fb-container>
								<fb-icon name="information"/>
							</fb-tooltip>
							<fb-radio-group :data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}"
											v-model="formData.enabled"></fb-radio-group>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row v-if="showTimeLine">
					<fb-col span="6">
						<fb-form-item label="最近5次运行时间" prop="">
							<fb-radio-group :data="[]"
											:reader="{label:'name', value:'id'}"
											></fb-radio-group>
						</fb-form-item>
					</fb-col>
					<fb-col span="16">
						<fb-timeline>
							<fb-timeline-item describe="" v-for="(val, index) in timelineData" :key="index" :time="val"></fb-timeline-item>
						</fb-timeline>
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

	import Page from "@fb/schedule-ui/src/util/Page";
	import dayjs from "dayjs";
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
				showTimeLine: false,
				timelineData: [],
				// 请求的 service
				service: app.$svc.sys.schedule.tpscheduledtask,
				// 表单数据
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
					enabled: null,
					nowExecuted: '',
					serialExecuted: 1,
					serialThreshold: 600,
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
					this.formData.taskId = param.id;
					this.view()
				}
			},

			// 取消
			handleClose() {
				// 隐藏
				this.closeTpDialog(this.formData.taskId);
			},
            // 校验cron表达式
			validateCron(cron) {
				// 调用新增service方法
				let that = this;
				this.service.validateCron({"cron": cron, "num": 5}).then((result) => {
					this.timelineData = [];
					// 判断code
					if (result.code == 1) {
						this.showTimeLine = true;
						for(var i in result.data){
							var time = dayjs(result.data[i]).format('YYYY-MM-DD HH:mm:ss');
							this.timelineData.push(time);
						}
					} else {
						// 服务器返回失败
						this.$message.error('错误提示:' + result.message)
					}
				})
			},
			add() {
				let that = this
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						if (that.formData.taskId) {
							// 调用修改service方法
							that.service.update(that.formData).then((result) => {
								// 判断code
								if (result.code == 1) {
									that.$message.success('修改成功');
									this.handleClose(that.formData.taskId);
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
									this.handleClose(result.data.taskId);
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
				this.service.view({"taskId": this.formData.taskId}).then((result) => {
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
