<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform">
				<fb-row>
					<fb-col span="24">
						<fb-tree
							ref="tree"
							:service="$svc.sys.role.auth.search"
							:param="formData"
							:reader="{value:'id', label: 'text'}"
							:do-check="'ps'"
							:do-un-check="'ps'"
							@on-data-load="callBack"
							multiple>

							<!--<template v-slot:doxxx="{node, root}">
								<div style="display: inline-block;  vertical-align: middle">
									<fb-icon name="home">{{node.label}} - {{node.checked}}</fb-icon>
								</div>
							</template>-->
						</fb-tree>
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
		name: 'auth',
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
			// 记录原来的默认值，用于表单重置
			this.init(this.param);
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.role,
				// 表单数据
				formData: {
					roleId: '',
					menuIds: '',
				},
			}
		},

		// 方法
		methods: {

			// 设置标题
			init(param) {
				if (param.id) {
					let roleId = param.id;
					this.formData.roleId = roleId;
				}
				if (this.$refs.tree) {
					this.$refs.tree.init();
				}
			},

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog("xxxx");
			},
			// 新增
			add() {
				// 获取全部选中的node
				let allNodes = this.$refs.tree.getAllNodes();
				let checkNodes = this.getCheckedNodes(allNodes.children);

				var that = this
				checkNodes.forEach(function (node) {
					that.formData.menuIds += "," + node.id;
				});

				this.formData.passKey = this.param.passKey;
				// 授权
				this.service.auth.insert(this.formData).then((result) => {
					// 判断code
					if (result.code == 1) {
						that.$message.success('授权成功');
						that.handleClose();
					} else {
						that.$message.error('授权失败:' + result.data.message)
					}
				})

			},
			getCheckedNodes (data) {
				let res = []
				for (const node of data) {
					if (node.checked || node.childrenCheckedStatus === 'half' || node.childrenCheckedStatus === 'all') {
						res.push(node)
					}
					if (node.children && node.children.length) {
						res = res.concat(this.getCheckedNodes(node.children))
					}
				}
				return res
			},
			// 树加载完回调
			callBack(node) {

			},

		}
	}
</script>

<style lang="less" scoped>

</style>
