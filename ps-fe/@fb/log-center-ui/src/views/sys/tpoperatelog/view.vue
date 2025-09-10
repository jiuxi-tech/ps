<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-property bordered label-width="130px" mode="form">
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="操作人员id">
							{{formData.personId}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="操作记录ID">
							{{formData.operterRid}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="模块code">
							{{formData.moduleCode}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="操作时间">
							{{formData.operterTime}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<!-- <fb-row>
					<fb-col span="12">
						<fb-property-item label="操作结果">
							{{operterResultName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="操作信息">
							{{formData.operterMsg}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="原始数据">
							{{formData.extend01}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="异常信息">
							{{formData.extend02}}
						</fb-property-item>
					</fb-col>
				</fb-row> -->
			</fb-property>
		</div>
		<div class="tp-dialog-bottom">
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>

	import Page from '@fb/log-center-ui/src/util/Page'
	import dayjs from "dayjs";
	export default {
		name: 'sys_view',
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

			// 测试新增日志
			// this.addLog();
		},
		data() {
			return {
				// 请求的 service
				service: app.$svc.sys.tpoperatelog,

				formData: {
					logId: '',
					personId: '',
					moduleCode: '',
					operterTime: '',
					operterType: '',
					operterRid: '',
					operterIp: '',
					operterBrowser: '',
					username: '',
					appName: '',
					source: '',
					ascnId: '',
					category: '',
					cityCode: '',
					actived: '',
					creator: '',
					createTime: '',
					updator: '',
					updateTime: '',
					extend01: '',
					extend02: '',
					extend03: '',
					operterResult: '',
					operterMsg: '',
				},
			}
		},
        computed: {
			operterResultName () {
				let val = this.formData.operterResult;
				if ('1' == val) {
					return '成功';
				}
				if ('0' == val) {
					return '失败';
				}
				return val;
			},
		},
		// 方法
		methods: {
			init(param) {
				this.view(param);
			},
			// 取消
			handleClose() {
				let param = {};
				this.closeTpDialog(param);
			},
			// 查看
			view(param) {
				let id = param.logId;
				// 调用新增service方法
				this.service.view({"logId": id, "personId": param.personId}).then((result) => {
					// 判断code
					if (result.code == 1) {
						if (result.data){
							let val = result.data.operterTime;
							if (val){
								result.data.operterTime = dayjs(val).format('YYYY-MM-DD HH:mm:ss')
							}
							this.formData = result.data
						} else {
							this.formData = param;
						}

					} else {
						// 服务器返回失败
						console.error('错误提示:' + result.message);
						this.formData = param;
					}
				})
			},
            // 测试新增日志
			addLog(){

				// 日志埋点
				let data = {
					// 模块编码
					moduleCode: 'entInfo',
					// 模块名称
					moduleName: '企业信息',
					// 操作类型： login/logout/add/delete/edit/query/pass/unpass, 可以自己定义
					operateType: 'add',
					// 操作记录id
					operterRid: '123456789',
					// 操作信息
					operterMsg: '新增了一个企业',
					// 操作结果
					operterResult: '1',
					// 原始信息
					extend01: JSON.stringify(this.formData)
				}

                setTimeout(()=> {
					// 日志埋点
					this.$logger.send(data);
				}, 3000)

			}
		}
	}
</script>

<style lang="less" scoped>

</style>
