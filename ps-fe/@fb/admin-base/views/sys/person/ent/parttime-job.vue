<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform">
				<fb-row>
					<fb-col span="24">
						<fb-tree
							ref="tree"
							:service="$svc.sys.dept.ent.parttimejobTree"
							:param="treeParamData"
							:reader="{value:'id', label: 'text'}"
							:do-check="''"
							:do-un-check="''"
							@on-data-load="callBack"
							multiple></fb-tree>
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
		name: 'parttime-job',
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
			// 'component-a': ComponentA,

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
				treeParamData: {
					personId: '',
				},
				formData: {
					personId: '',
					deptIds: '',
				},
			}
		},

		// 方法
		methods: {
			// 初始化页面参数
			init(param) {
				if (param.id) {
					let personId = param.id;
					this.treeParamData.personId = personId;
					this.formData.personId = personId;
				}
				if (this.$refs.tree) {
					this.$refs.tree.init();
				}
			},

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog("param");
			},
			// 新增
			add() {
				// 获取全部选中的node
				let checkNodes = this.$refs.tree.getCheckedNodes();
				this.formData.deptIds = '';
				checkNodes.forEach((node)=> {
					this.formData.deptIds += "," + node.id;
				});
				this.formData.passKey = this.param.passKey;
				// 授权
				this.service.parttimeAdd(this.formData).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.$message.success('成功');
						// 关闭，并传递参数
						this.closeTpDialog("param");
					} else {
						this.$message.error('失败:' + result.data.message)
					}
				})

			},
			// 树加载完回调
			callBack(node) {

			},
		}
	}
</script>

<style lang="less" scoped>

</style>
