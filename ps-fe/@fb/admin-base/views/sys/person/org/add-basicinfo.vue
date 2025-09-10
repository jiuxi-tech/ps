<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform">
				<fb-row>
					<fb-col span="12">
						<fb-row>
							<fb-col span="24">
								<fb-form-item label="人员姓名" prop="personName" :rule="[{required: true}]">
									<fb-input v-model="formData.personName" placeholder="请输入用户名"></fb-input>
								</fb-form-item>
							</fb-col>
						</fb-row>
						<fb-row>
							<fb-col span="24">
<!--								<fb-form-item label="证件类型" prop="idtype" :rule='{required:true}'>-->
									<fb-form-item label="证件类型" prop="idtype">
									<fb-select v-model="formData.idtype"
											   :placeholder="'请选择证件类型'"
											   :service="$svc.sys.dict.select"
											   :param="{'pdicCode': 'Y24'}"
											   @on-change="idtypeChange"/>
								</fb-form-item>
							</fb-col>
						</fb-row>
						<fb-row>
							<fb-col span="24">
<!--								<fb-form-item label="证件号" prop="idcard"-->
<!--											  :rule="formData.idtype === 'Y2401' ? [{required: true}, {type: 'idcard'}] : {required: false}">-->
									<fb-form-item label="证件号" prop="idcard">
									<fb-input
										v-model="formData.idcard"
										placeholder="请输入证件号"
										@on-blur="birthdayFormat">
									</fb-input>
								</fb-form-item>
							</fb-col>
						</fb-row>
						<fb-row>
							<fb-col span="24">
<!--								<fb-form-item label="出生日期" prop="birthday" :rule="[{required: true}]">-->
									<fb-form-item label="出生日期" prop="birthday">
									<tp-datepicker v-model="formData.birthday" format="YYYY-MM-DD" value-format="YYYYMMDD" clearable></tp-datepicker>
								</fb-form-item>
							</fb-col>
						</fb-row>
					</fb-col>
					<fb-col span="12">
						<fb-row>
							<fb-col span="17">
								<fb-form-item label="头像">
									<tp-upload
										view="avatar"
										v-model="file.fileList"
										:service="$svc.sys.file"
										:param="{'referType': file.referType}"
										:referid="formData.personId"
										:accept="'.png,.jpeg,.jpg'"
									></tp-upload>
								</fb-form-item>
							</fb-col>
						</fb-row>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
<!--						<fb-form-item label="民族" prop="safeprinNation" :rule="[{required: true}]">-->
							<fb-form-item label="民族" prop="safeprinNation">
							<fb-select v-model="formData.safeprinNation"
									   :placeholder="'请选择'"
									   :service="$svc.sys.dict.select"
									   :param="{'pdicCode': 'Y26'}"
									   filterable
									   clearable/>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row v-if="deptShowFlag">
					<fb-col span="12">
						<fb-form-item label="当前部门">
							<tp-org-async-tree-select
								ref="deptTree"
								v-model="formData.deptId"
								:placeholder="formData.deptId?formData.deptFullName:'请选择'"
								:clearable="false"
							/>

<!--					 同步树<fb-tree-select-->
<!--								ref="currentDeptTree"-->
<!--								v-model="formData.deptId"-->
<!--								:service="$svc.sys.dept.org.tree"-->
<!--								:param="{deptId: ''}"-->
<!--								:reader="{value:'id', label: 'text'}"-->
<!--								:placeholder="'请选择'"-->
<!--								clearable>-->
<!--							</fb-tree-select>-->
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="兼职部门">
							<fb-input v-model="formData.deptFullNames" readonly></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
<!--						<fb-form-item label="性别" prop="sex" :rule="[{required: true}]">-->
							<fb-form-item label="性别" prop="sex" >
							<fb-select v-model="formData.sex"
									   :data="defaultForm.sex"
									   :placeholder="'请选择'"
									   clearable/>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
<!--						<fb-form-item label="手机号码" prop="phone" :rule="[{required: true}, {type: 'mobile'}]">-->
							<fb-form-item label="手机号码" prop="phone">
							<fb-input v-model="formData.phone" placeholder="请输入手机号码"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="职位">
							<fb-input
								type="text"
								v-model="formData.office"
								placeholder="请输入职位">
							</fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
<!--						<fb-form-item label="人员编号" prop="personNo" :rule="[{required: true}]">-->
							<fb-form-item label="人员编号" prop="personNo">
							<fb-input
								v-model="formData.personNo"
								type="text"
								placeholder="请输入人员编号">
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="联系电话" :rule="{required: false, type: 'telmobile'}">
							<fb-input v-model="formData.tel" placeholder="请输入联系电话"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
<!--						<fb-form-item label="Email" prop="email" :rule="[{required: false}, { type: 'email'}]">-->
							<fb-form-item label="Email" prop="email">
							<fb-input
								type="text"
								v-model="formData.email"
								placeholder="请输入email">
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="籍贯">
							<fb-select v-model="formData.nativePlace"
									   :service="$svc.sys.city.selectByCityLevel"
									   :param="{'pcityId': '-1','cityLevel': 'SYS1702'}"
									   :reader="{value:'cityId', label: 'cityFullName'}"
									   :placeholder="'请选择'"
									   :header-format="cityTreeHeaderFormat"
									   clearable/>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="个人简介">
							<fb-textarea rows="2" v-model="formData.resume"
										 placeholder="请输入内容"
										 :maxlength="200">
							</fb-textarea>
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

	import TpOrgAsyncTreeSelect from "../../../../components/tp-org-async-tree-select/TpOrgAsyncTreeSelect"

	export default {
		name: 'add-basicinfo',
		components: {
			TpOrgAsyncTreeSelect
		},
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

			this.init(this.param);
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.person,
				deptShowFlag: false,
				// 表单数据
				formData: {
					deptId: '',
					personId: '',
					personName: '',
					profilePhoto: '',
					defaultDept: '',
					deptFullName: '',
					deptFullNames: '',
					// 人员编号
					personNo: '',
					sex: '',
					idtype: 'Y2401',
					idcard: '',
					safeprinNation: '',
					nativePlace: '',
					resume: '',
					birthday: '',
					phone: '',
					tel: '',
					email: '',
					office: '',
					// 所属机构（单位）id 政府存机构ID，企业存单位id，存在分公司的，存所在分公司单位id
					ascnId: '',
				},
				file: {
					referType: 'SYS1014',
					fileList: [],
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
					this.deptShowFlag = true;
				} else {
					// 新增，asyncId赋值，修改通过后端查询
					this.formData.ascnId = param.ascnId;
				}
			},

			// 取消
			handleClose() {
				this.closeTpDialog(this.formData.personId);
			},
			formReset() {
				// 表单重置
				Object.assign(this.$data.formData, this.defaultData);
			},
			// 新增
			add() {
				this.formData.file = this.file;
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						if (this.formData.personId) {
							// 修改
							this.formData.passKey = this.param.passKey;
							this.service.update(this.formData).then((result) => {
								// 判断code
								if (result.code == 1) {
									this.$message.success('修改成功');
								} else {
									// 服务器返回失败
									this.$message.error('修改失败:' + result.data.message)
								}
								;
								this.updateCount = 0;
							})
						} else {
							// 新增，调用新增service方法
							this.service.org.add(this.formData).then((result) => {
								// 判断code
								if (result.code == 1) {
									this.$message.success('新增成功');

									let personId = result.data.personId;
									this.formData.personId = personId;
									this.setPageParam(personId);
								} else {
									// 服务器返回失败
									//console.log(result.data.message);
									this.$message.error('新增失败')
								}
								;
								this.updateCount = 0;
							})
						}
					}
				})
			},

			view(personId, deptId) {
				// 调用新增service方法
				this.service.view({"personId": personId, "deptId": deptId, "passKey": this.param.passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.formData = result.data
					} else {
						// 服务器返回失败
						this.$message.error('查询失败')
					}
				})
			},

			idtypeChange() {
				this.formData.idcard = '';
			},

			birthdayFormat() {
				let idcard = this.formData.idcard;
				if (this.formData.idtype === 'Y2401' && idcard && (idcard.length === 15 || idcard.length === 18)) {
					this.formData.birthday = idcard.substring(6, 14);
				}
			},
		}
	}
</script>

<style lang="less" scoped>

</style>
