<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-fieldset label="人员基本信息"/>
			<fb-property bordered label-width="140px" mode="form">
				<fb-row>
					<fb-col span="12">
						<fb-row>
							<fb-col span="24">
								<fb-property-item label="人员姓名">
									{{formData.personName}}
								</fb-property-item>
							</fb-col>
						</fb-row>
						<fb-row>
							<fb-col span="24">
								<fb-property-item label="证件类型">
									{{formData.idtypeName}}
								</fb-property-item>
							</fb-col>
						</fb-row>
						<fb-row>
							<fb-col span="24">
								<fb-property-item label="证件号">
									{{formData.idcard}}
								</fb-property-item>
							</fb-col>
						</fb-row>
						<fb-row>
							<fb-col span="24">
								<fb-property-item label="出生日期">
									{{formData.birthday}}
								</fb-property-item>
							</fb-col>
						</fb-row>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="头像">
							<tp-upload
								view="avatar"
								v-model="file.fileList"
								:service="$svc.sys.file"
								:param="{'referType': file.referType}"
								:referid="formData.personId"
								:accept="'.png,.jpeg,.jpg'"
								readonly
							></tp-upload>
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="民族">
							{{formData.safeprinNationName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="性别">
							{{formData.sexName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="手机号码">
							{{formData.phone}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="当前部门">
							{{formData.deptFullName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="兼职部门">
							{{formData.deptFullNames}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="人员编号">
							{{formData.personNo}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="籍贯">
							{{formData.nativePlaceName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="联系电话">
							{{formData.tel}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="email">
							{{formData.email}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="职位">
							{{formData.office}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="个人简介">
							{{formData.resume}}
						</fb-property-item>
					</fb-col>
				</fb-row>
			</fb-property>
			<fb-fieldset label="用户扩展信息"/>
			<fb-property bordered label-width="140px" mode="form">
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="工号">
							{{expFormData.jobNumber}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="职称">
							{{expFormData.titleCodeName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="开始从事时间">
							{{expFormData.safetyEngineerDate}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="安全生产职务代码">
							{{expFormData.safetyDutyCodeName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="通信地址">
							{{expFormData.maddress}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="安全生产职务是否专职">
							{{expFormData.fullJob === 0 ? "否":"是"}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="是否是注安师">
							{{expFormData.safetyEngineer === 0 ? "否":"是"}}
						</fb-property-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-property-item label="政治面貌">
							{{expFormData.politicsCodeName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="毕业学校">
							{{expFormData.school}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="所学专业">
							{{expFormData.sepcSubject}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="学历">
							{{expFormData.diplomaCodeName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="最高学位">
							{{expFormData.degree}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="岗位">
							{{expFormData.position}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="参加工作时间">
							{{expFormData.partWorkDate}}
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


	export default {
		name: 'person-ent-view',
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
				service: this.$svc.sys.person,

				file: {
					referType: 'SYS1014',
					fileList: [],
				},
				// 表单数据
				formData: {
					deptId: '',
					personId: '',
					personName: '',
					profilePhoto: '',
					deptFullName: '',
					deptFullNames: '',
					// 人员编号
					personNo: '',
					sexName: '',
					idtypeName: '',
					idcard: '',
					safeprinNationName: '',
					nativePlaceName: '',
					resume: '',
					birthday: '',
					phone: '',
					tel: '',
					email: '',
					office: '',
					// 所属机构（单位）id 政府存机构ID，企业存单位id，存在分公司的，存所在分公司单位id
					ascnId: '',
				},
				// 表单数据
				expFormData: {
					deptId: '',
					personId: '',
					// 职称 字典编码SYS12
					titleCodeName: '',
					// 通信地址
					maddress: '',
					// 是否是注安师
					safetyEngineer: 1,
					// 开始从事时间
					safetyEngineerDate: '',
					// 安全生产植物代码
					safetyDutyCodeName: '',
					// 安全生产职务是否专职
					fullJob: 1,
					// 工号
					jobNumber: '',
					// 政治面貌
					politicsCodeName: '',
					// 毕业学校
					school: '',
					// 所学专业
					sepcSubject: '',
					// 学历
					diplomaCodeName: '',
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
			// 赋值
			init(param) {
				// deptId赋值
				this.formData.deptId = param.deptId;
				if (param.id) {
					let personId = param.id;
					this.formData.personId = personId;
					this.view(personId, param.deptId);
				}
			},

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog("xxxx");
			},

			view(personId, deptId) {
				// 调用新增service方法
				this.service.view({"personId": personId, "deptId": deptId, "passKey": this.param.passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						result.data.birthday = this.formatDate(result.data.birthday, "YYYY-MM-DD");
						this.formData = result.data
					} else {
						// 服务器返回失败
						this.$message.error('查询失败：' + result.message)
					}
				})

				// 调用新增service方法
				this.service.expView({"personId": personId, "deptId": deptId, "passKey": this.param.passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						result.data.safetyEngineerDate = this.formatDate(result.data.safetyEngineerDate, "YYYY-MM-DD");
						result.data.partWorkDate = this.formatDate(result.data.partWorkDate, "YYYY-MM-DD");
						this.expFormData = result.data
					} else {
						// 服务器返回失败
						this.$message.error('查询失败')
					}
				})
			},

		}
	}
</script>

<style lang="less" scoped>

</style>
