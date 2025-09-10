<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-fieldset label="企业基本信息"/>
			<fb-property bordered label-width="140px" mode="form">
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="企业名称">
							{{formData.entFullName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="企业简称">
							{{formData.entSimpleName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="统一社会信用代码">
							{{formData.entUnifiedCode}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="企业类型">
							{{formData.entTypeName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="法定代表人">
							{{formData.legalRepr}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="法人号码">
							{{formData.legalReprTel}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="联系人">
							{{formData.linkPsnName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="联系人号码">
							{{formData.linkPsnTel}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="安全管理负责人">
							{{formData.safetyDirectorName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="安全管理负责人号码">
							{{formData.safetyDirectorTel}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="注册资金">
							{{formData.regFund === "" ? 0.00 : formData.regFund}} 万元
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="企业经度坐标">
							<fb-link type="primary" :label="formData.longitude" icon="navigation" @click="longitude"
									 target="_blank"></fb-link>
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="企业纬度坐标">
							<fb-link type="primary" :label="formData.latitude" icon="navigation" @click="longitude"
									 target="_blank"></fb-link>
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="行业类别">
							{{formData.industryTypeCodeName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="企业规模">
							{{formData.scaleTypeName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="注册地址">
							{{formData.entAddrCodeName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="注册详细地址">
							{{formData.entAddr}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="生产地行政区划">
							{{formData.prodAddrCodeName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="生产经营地址">
							{{formData.prodAddress}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="简介">
							{{formData.entDesc}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="是否启用">
							{{formData.enabled === 1 ? "是" : "否"}}
						</fb-property-item>
					</fb-col>
				</fb-row>
			</fb-property>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>

		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
	</div>
</template>


<script>


	import FbRow from "@fb/fb-ui/packages/components/row/src/FbRow";

	export default {
		name: 'ent-view',
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
		// 组件
		components: {
			FbRow
			// 'component-a': ComponentA,

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
				service: this.$svc.sys.ent,
				// preview: true,
				// 表单数据
				formData: {
					entId: "",
					entFullName: "",
					entSimpleName: "",
					entUnifiedCode: "",
					entType: "",
					entTypeName: "",
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
					entAddrCodeName: "",
					entAddr: "",
					prodAddrCodeName: "",
					prodAddress: "",
					industryTypeCodeName: "",
					lineCode: "",
					scaleTypeName: "",
					enabled: 1,
					extend01: "",
					extend02: "",
					extend03: "",
					selectedProvince: '',
					selectedCity: '',
					selectedDistrict: '',
					province: [],
					citys: [],
					districts: [],
					city: false,
					district: false,
				},
				file: {
					referType: "SYS1021",
					fileList: [],
				},
			}
		},

		// 方法
		methods: {

			// 设置标题
			init(param) {
				this.formData.entId = param.id;
				this.view();
			},

			// 取消
			handleClose() {
				this.closeTpDialog();
			},
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
				this.$refs.TpDialog.show(import('../../../views/common/map/view-map.vue'), param, "天地图");
			},

			closeDialog(param) {
				console.log(param);
			},

		}
	}
</script>

<style lang="less" scoped>

</style>
