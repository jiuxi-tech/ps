<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="140">
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="工号">
							<fb-input v-model="formData.jobNumber" type="text" placeholder="请输入工号"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="职称">
							<fb-select v-model="formData.titleCode"
									   :service="$svc.sys.dict.select"
									   :param="{'pdicCode': 'SYS12'}"
									   placeholder="请选择"
									   clearable>
							</fb-select>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="开始从事时间">
							<tp-datepicker v-model="formData.safetyEngineerDate" format="YYYY-MM-DD"
										   value-format="YYYYMMDD"></tp-datepicker>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="安全生产职务代码">
							<fb-select v-model="formData.safetyDutyCode"
									   :service="$svc.sys.dict.select"
									   :param="{'pdicCode': 'X58'}"
									   placeholder="请选择"
									   clearable>
							</fb-select>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="通信地址">
							<fb-input v-model="formData.maddress" type="text" placeholder="请输入通信地址"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="安全生产职务是否专职">
							<fb-radio-group v-model="formData.fullJob"
											:data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}">
							</fb-radio-group>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="是否是注安师">
							<fb-radio-group v-model="formData.safetyEngineer"
											:data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}">
							</fb-radio-group>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="政治面貌">
							<fb-select v-model="formData.politicsCode"
									   :service="$svc.sys.dict.select"
									   :param="{'pdicCode': 'SYS13'}"
									   placeholder="请选择"
									   clearable>
							</fb-select>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="毕业学校">
							<fb-input v-model="formData.school" type="text" placeholder="请输入毕业学校"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="所学专业">
							<fb-input v-model="formData.sepcSubject" type="text" placeholder="请输入所学专业"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="学历">
							<fb-select v-model="formData.diplomaCode"
									   :service="$svc.sys.dict.select"
									   :param="{'pdicCode': 'SYS14'}"
									   placeholder="请选择"
									   clearable>
							</fb-select>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="最高学位">
							<fb-input v-model="formData.degree" type="text" placeholder="请输入最高学位"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="岗位">
							<fb-input v-model="formData.position" type="text" placeholder="请输入岗位"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="参加工作时间">
							<tp-datepicker v-model="formData.partWorkDate" format="YYYY-MM-DD"
										   value-format="YYYYMMDD"></tp-datepicker>
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


	export default {
		name: 'add-exinfo',
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
		// 创建方法
		created() {
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化
			this.init(this.param);
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.person,

				// 表单数据
				formData: {
					deptId: '',
					personId: '',
					// 职称 字典编码SYS12
					titleCode: '',
					// 通信地址
					maddress: '',
					// 是否是注安师
					safetyEngineer: 1,
					// 开始从事时间
					safetyEngineerDate: '',
					// 安全生产植物代码
					safetyDutyCode: '',
					// 安全生产职务是否专职
					fullJob: 1,
					// 工号
					jobNumber: '',
					// 政治面貌
					politicsCode: '',
					// 毕业学校
					school: '',
					// 所学专业
					sepcSubject: '',
					// 学历
					diplomaCode: '',
					// 最高学位
					degree: '',
					// 岗位
					position: '',
					// 参加工作时间
					partWorkDate: '',
					// 扩展字段01
					extend01: '',
					// 扩展字段02
					extend02: '',
					// 扩展字段03
					extend03: '',
					// 扩展字段04
					extend04: '',
					// 扩展字段05
					extend05: '',
				},
			}
		},

		// 方法
		methods: {
			// 设置标题
			init(param) {
				// deptId赋值，修改通过后端查询
				let deptId = param.deptId;
				this.formData.deptId = deptId;
				if (param.id) {
					// 修改
					let personId = param.id;
					this.formData.personId = personId;
					this.view(personId, deptId);
				} else {
					// 新增，asyncId赋值，修改通过后端查询
					this.formData.ascnId = param.ascnId;
				}
			},
			// 取消
			handleClose() {

				// 关闭，并传递参数
				this.closeTpDialog("xxxx");
			},
			// 新增
			add() {
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						if (!this.formData.personId) {
							if (JSON.stringify(this.getPageParam()) == "{}") {
								this.$message.error('请先保存用户基本信息！')
								return;
							}
							this.formData.personId = this.getPageParam();
						}

						// 新增 / 修改，调用service方法
						this.service.expAdd(this.formData).then((result) => {
							// 判断code
							if (result.code == 1) {
								this.$message.success('新增成功');
							} else {
								// 服务器返回失败
								this.$message.error('新增失败');
							}
						})
					}
				})

			},

			view(personId, deptId) {
				// 调用新增service方法
				this.service.expView({"personId": personId, "deptId": deptId, "passKey": this.param.passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						if (result.data !== "") {
							this.formData = result.data
						}
					} else {
						// 服务器返回失败
						this.$message.error('查询失败')
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
