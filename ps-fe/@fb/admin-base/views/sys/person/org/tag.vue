<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform">
				<fb-row>
					<fb-col span="24">
						<fb-transfer
							:list-style="{height: '500px', width: '330px'}"
							:data="sourceData"
							:titles="['待选标签', '已拥有标签']"
							:target-keys="targetKeys"
							:selected-keys="selectedKeys"
							:reader="{label: 'tagName', value:'tagId'}"
							@on-change="handleChange"
						></fb-transfer>
					</fb-col>
				</fb-row>
			</fb-form>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="save">保存</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>

	export default {
		name: 'tag',
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
		// 页面元素没有实例化之前执行方法
		created() {
			this.init(this.param);
		},
		// 页面元素实例化完成之后执行方法
		mounted() {
		},
		data() {

			return {
				// 请求的 service
				tagService: this.$svc.sys.tag,
				personTagService: this.$svc.sys.personTag,
				sourceData: [],
				targetKeys: [],
				selectedKeys: [],
				disabled: false,
				// 表单数据
				formData: {
					personId: '',
					deptId: '',
					tagIds: '',
				},
			}
		},

		// 方法
		methods: {
			// 初始化参数
			init(param) {
				let personId = param.id;
				let deptId = param.deptId;
				this.formData.personId = personId;
				this.formData.deptId = deptId;

				this.initData(deptId, personId);
			},

			initData(deptId, personId) {
				// 查询所有可用标签
				let that = this;
				that.sourceData = [];
				that.targetKeys = [];
				
				// 查询所有标签
				this.tagService.list({size: -1}).then((result) => {
					// 判断code
					if (result.code == 1) {
						if (result.data && result.data.records) {
							result.data.records.forEach(function (data) {
								that.sourceData.push(data);
							});
						}

						// 查询用户已经拥有的标签
						this.personTagService.personTags({'deptId': deptId, 'personId': personId}).then((res) => {
							// 判断code
							if (res.code == 1) {
								res.data.forEach(function (data) {
									that.targetKeys.push(data.tagId);
									// 有一种情况，已选列有数据，待选列没数据，需要把已选列的数据加入到待选列充当原始数据.
									// 另一种情况，待选列没有已选列的数据，没有原始数据，已选列展示不出数据
									let hasFlag = true;
									for (let i = 0; i < that.sourceData.length; i++) {
										if (that.sourceData[i].tagId === data.tagId) {
											// 如果原始数据已经有当前数据，直接结束，否则如果一直到最后都没有，将该数据加入到原始数据中。
											hasFlag = false;
											break;
										}
									}
									if (hasFlag) {
										// 原始数据没有，加入到原始数据中。原始数据是为了显示待选列和已选列。
										that.sourceData.push(data);
									}

								});
							} else {
								this.$message.error('已拥有标签查询失败:' + res.data.message)
							}
						});

					} else {
						this.$message.error('可选标签查询失败:' + result.data.message)
					}
				});
			},

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog("param");
			},

			// 保存
			save() {
				// 获取全部选中的标签
				console.log("选中标签数量：" + this.targetKeys.length);
				this.handleSave();
			},

			handleSave() {
				// 构建标签ID字符串
				let tagIds = '';
				this.targetKeys.forEach((tagId) => {
					if (tagIds) {
						tagIds += "," + tagId;
					} else {
						tagIds = tagId;
					}
				});
				
				// 保存用户标签
				this.personTagService.save({
					personId: this.formData.personId,
					deptId: this.formData.deptId,
					tagIds: tagIds
				}).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.$message.success('标签设置成功');
						// 关闭，并传递参数
						this.handleClose();
					} else {
						this.$message.error('标签设置失败:' + result.data.message)
					}
				})
			},

			handleChange(targetKeys, targetList, sourceKeys, sourceList) {
				let oldTargetKeys = this.targetKeys;
				try {
					// console.log(targetKeys, targetList, sourceKeys, sourceList)
					this.targetKeys = [];
					var that = this;
					targetKeys.forEach(function (data) {
						that.targetKeys.push(data.tagId);
					})
				} catch (e) {
					this.targetKeys = oldTargetKeys;
				}
			},

		}
	}
</script>

<style lang="less" scoped>

</style>