<template>
	<div>
		<!-- 异步加载 -->
		<fb-tree-select
			ref="deptTree"
			v-model="deptId"
			:data="deptData"
			:load-data="loadOrgTreeData"
			@on-change="handleChange"
			:placeholder="placeholder"
			:reader="reader"
			:clearable="clearable">
		</fb-tree-select>
	</div>
</template>

<script>
	export default {
		name: "TpOrgAsyncTreeSelect",
		mixins: [],
		// 接收父组件的传参
		props: {
			height: {
				type: Number,
				require: false,
				default: 152
			},
			clearable: {
				type: Boolean,
				default: true,
			},
			reader: {
				type: Object,
				default () {
					return {
						value: 'id',
						label: 'text',
					}
				},
			},
			placeholder: {
				type: String,
				default: '',
			},
			value: {
				type: String,
				default: '',
			}
		},
		// 组件
		components: {
			// 'component-a': ComponentA,
		},
		// 创建方法
		created() {

		},
		// 初始化方法
		mounted() {
			this.initOrgTreeData();
		},
		data() {
			return {
				deptId: this.value || null,
				// 部门树加载数据
				deptData: [],
			}
		},
		methods: {

			// 初始化树数据
			initOrgTreeData(deptId) {
				this.doLoadOrgTreeData({deptId: deptId, sync: 0}, (data) => {
					if (data && data.length > 0){
						this.deptData = data
					}
				});
			},

			// 部门树异步加载，查询除顶层之外的下级，一层一层查
			loadOrgTreeData(item, callback) {
				if (item.children.length >= 1) {
					callback();
				} else {
					this.doLoadOrgTreeData({deptId: item.id, sync: 0},(data)=>{
						if (data[0].children.length >= 1) {
							callback(data[0].children);
						}else{
							callback();
						}
					});
				}
			},

			// 部门树异步加载，查询除顶层之外的下级，一层一层查
			doLoadOrgTreeData(param, callback) {
				this.$svc.sys.dept.org.tree(param).then((result) => {
					if (result.code == 1) {
						callback(result.data);
					} else {
						// 服务器返回失败
						this.$message.error('行政区划树加载失败' + result.message)
					}
				})
			},

			// 处理变更事件
			handleChange(){
				this.$nextTick(()=>{
					this.$emit('input', this.deptId)
				})
			}
		},
	}

</script>

<style scoped>

</style>
