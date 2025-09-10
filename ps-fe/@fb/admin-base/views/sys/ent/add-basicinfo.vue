<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
		<fb-form ref="fbform" :label-width="160">
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="企业名称" prop="entFullName" :rule="[{required: true}]">
							<fb-input v-model="formData.entFullName"
									  placeholder="请输入企业名称">
							</fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="企业简称" prop="entSimpleName" :rule="[{required: true}]">
							<fb-input v-model="formData.entSimpleName"
									  placeholder="请输入企业简称">
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="统一社会信用代码" prop="entUnifiedCode"
									  :rule="[{required: true}]">
							<fb-input v-model="formData.entUnifiedCode"
									  placeholder="请输入统一社会信用代码">
							</fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="企业类型">
							<fb-select
								v-model="formData.entType"
								:service="$svc.sys.dict.select"
								:param="{'pdicCode': 'H12'}"
								placeholder="请选择"
								clearable/>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="法定代表人">
							<fb-input
								v-model="formData.legalRepr"
								placeholder="请输入法定代表人">
							</fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="法人号码"  prop="legalReprTel" :rule="{type: 'telmobile'}">
							<fb-input v-model="formData.legalReprTel"
									  placeholder="请输入手机号码">
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="联系人">
							<fb-input v-model="formData.linkPsnName"
									  placeholder="请输入联系人姓名">
							</fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="联系人号码"  prop="linkPsnTel" :rule="{type: 'telmobile'}">
							<fb-input v-model="formData.linkPsnTel"
									  placeholder="请输入手机号码">
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="安全管理负责人">
							<fb-input v-model="formData.safetyDirectorName" placeholder="请输入安全管理负责人姓名"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="安全管理负责人号码">
							<fb-input v-model="formData.safetyDirectorTel" placeholder="安全管理负责人手机号码"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="注册资金">
							<fb-input v-model="formData.regFund" placeholder="请输入注册资金">
								<template slot="append">万元</template>
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="企业经度坐标">
							<fb-input v-model="formData.longitude" @on-click="longitude()" readonly></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="企业纬度坐标">
							<fb-input v-model="formData.latitude" @on-click="longitude()" readonly></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="行业类别">
							<fb-tree-select v-model="formData.industryTypeCode"
									   :service="$svc.sys.dict.tree"
									   :param="{'dicCode': 'SYS16'}"
											:reader="{value: 'id', label: 'text'}"
									   placeholder="请选择"
									   clearable>
								</fb-tree-select>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="企业规模">
							<fb-select v-model="formData.scaleType"
									   :service="$svc.sys.dict.select"
									   :param="{'pdicCode': 'C36'}"
									   placeholder="请选择"
									   clearable/>
						</fb-form-item>
					</fb-col>
					<!--<fb-col span="12">
                        <fb-form-item label="条线" prop="lineCode" :rule="[{required: true}]">
                            <fb-select v-model="formData.lineCode"
                                       :service="$svc.sys.dict.select"
                                       :param="{'pdicCode': 'SYS24'}"
                                       placeholder="请选择"
                                       clearable/>
                        </fb-form-item>
                    </fb-col>-->
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="注册地址" prop="entAddrCode">
							<fb-tree-select
								ref="entAddrCodeTree"
								v-model="formData.entAddrCode"
								:service="$svc.sys.city.tree"
								:param="{'sync': 1, 'expand': false, 'cityId': '-1'}"
								:reader="{value:'extend02', label: 'text'}"
								:placeholder="'请选择'"
								:header-format="cityTreeHeaderFormat"
								clearable>
							</fb-tree-select>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="注册详细地址" prop="entAddr">
							<fb-input v-model="formData.entAddr"
									  placeholder="请输入注册详细地址，如：门牌号等">
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="生产地行政区划"  prop="prodAddrCode" :rule="[{required: true}]">
							<fb-tree-select
								ref="cityTree"
								v-model="formData.prodAddrCode"
								:service="$svc.sys.city.tree"
								:param="{'sync': 1, 'expand': false, 'cityId': '-1'}"
								:reader="{value:'extend02', label: 'text'}"
								:placeholder="'请选择'"
								:header-format="cityTreeHeaderFormat"
								clearable>
							</fb-tree-select>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="生产经营地址" prop="prodAddr" :rule="[{required: true}]">
							<fb-input v-model="formData.prodAddr"
									  placeholder="请输入生产经营地址">
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="简介">
							<fb-textarea
								rows="5"
								v-model="formData.entDesc"
								placeholder="请输入简介"
								maxlength="200">
							</fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="是否启用">
							<fb-radio-group v-model="formData.enabled"
											:data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}"></fb-radio-group>
						</fb-form-item>
					</fb-col>
				</fb-row>
			</fb-form>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="add">保存</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>

		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
	</div>
</template>


<script>



	export default {
		name: 'add-basicinfo',
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
				service: this.$svc.sys.ent,
				cityParam: {
					pcityId: '',
					cityLevel: '',
				},
				// 表单数据
				formData: {
					// 企业id
					entId: '',
					entFullName: "",
					entSimpleName: "",
					entUnifiedCode: "",
					entType: "",
					entDesc: "",
					legalRepr: "",
					legalReprTel: "",
					linkPsnName: "",
					linkPsnTel: "",
					// 安全管理负责人姓名
					safetyDirectorName: '',
					// 安全管理负责人联系方式
					safetyDirectorTel: '',
					regFund: "",
					longitude: "",
					latitude: "",
					entAddrCode: "",
					entAddr: "",
					prodAddrCode: "",
					prodAddr: "",
					industryTypeCode: "",
					lineCode: "",
					scaleType: "",
					enabled: 1,
					extend01: "",
					extend02: "",
					extend03: "",
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
					this.formData.entId = param.id;
					this.view()
				}
			},

			// 取消
			handleClose() {
				// 隐藏
				this.closeTpDialog(this.formData.entId);
			},

			add() {
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						if (this.formData.entId) {
							// 调用新增service方法
							this.formData.passKey = this.param.passKey;
							this.service.update(this.formData).then((result) => {
								// 判断code
								if (result.code == 1) {
									this.$message.success('修改成功');
									this.handleClose(this.formData.entId);
								} else {
									// 服务器返回失败
									this.$message.error('修改失败:' + result.message)
								};
								this.updateCount = 0;
							})
						} else {
							// 调用新增service方法
							this.service.add(this.formData).then((result) => {
								// 判断code
								if (result.code == 1) {
									this.$message.success('新增成功');
									let entId = result.data;
									this.handleClose(entId);
								} else {
									// 服务器返回失败
									this.$message.error('新增失败:' + result.message)
								};
								this.updateCount = 0;
							})
						}
					}
				})
			},

			// 查询信息
			view() {
				// 调用新增service方法
				this.service.view({"entId": this.formData.entId, "passKey": this.param.passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.formData = result.data
					} else {
						// 服务器返回失败
						this.$message.error('错误提示:' + result.message)
					}
				})
			},

			longitude() {
				var param = {'longitude': this.formData.longitude, 'latitude': this.formData.latitude}
				this.$refs.TpDialog.show(import('../../../views/common/map/pickup-map.vue'), param, "天地图");
			},
			closeDialog(param) {
				// 坐标回显
				if (param) {
					this.formData.longitude = param.longitude;
					this.formData.latitude = param.latitude;
				}
			},
		}
	}
</script>

<style lang="less" scoped>

</style>
