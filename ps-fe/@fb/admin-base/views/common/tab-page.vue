<template>
	<div>
		<fb-tabs-page-search>
			<template slot="tabs">
				<fb-tabs v-model="tabs.currentTab" type="card" :before-change="beforeChange">
					<fb-tab v-for="(item, index) in tabSteps" :key="index" :label="item.label" :icon="item.icon" >
						<component ref="componentPage" :is="item.component" :param="tabs.param" :parentPage="currentPage"></component>
					</fb-tab>
				</fb-tabs>
			</template>
		</fb-tabs-page-search>
	</div>
</template>

<script>

export default {
	name: 'tab-page',
	components: {

	},
	// 初始化方法
	mounted() {
		// 执行界面初始化
		this.initView(this.tabPages)
	},
	data () {
		return {
			// Tab的界面
			tabPages:[
				{
					url: '/sys/demo/add.vue',
					label: '基本信息',
					icon: "chart-line"
				},
				{
					url: '/sys/demo/list.vue',
					label: '扩展信息',
					icon: "progressbar"
				}
			],
			// Tabs
			tabs: {
				// 默认的选中Tab
				currentTab: 1,
				param: {},
				tabChangeConfirm: false, // tab切换时，修改数据后的提示框
			},
			// 当前界面，用于跨组件传递
			currentPage: this,
			tabSteps: [],
			pageParam: {},
		}
	},
	methods: {
		// 界面初始化
		initView (tabArry, options) {
			let componentArry = []
			tabArry.forEach(function (item, index) {
				let component = () => import('../../views' + item.url)
				item.component = component
				componentArry.push(item)
			})
			this.tabSteps = componentArry
			options = options || {};
			this.tabs.tabChangeConfirm = options.tabChangeConfirm || this.tabs.tabChangeConfirm;

			// 默认显示第一个tab
			this.currentTab = 1;
			// 每次打开清空参数
			this.pageParam = {}
		},
		// 监听tab切换(目标tab, 目标tab的下标)
		beforeChange(tab, index){

			// 默认tab切换时，提示confirm
			if(!this.tabs.tabChangeConfirm){
				return true
			}

			let that = this
			let i = this.currentTab - 1;
			// 获取当前界面是否更新
			let updateCount = that.$refs.componentPage[i].$data.updateCount
			// let updateCount = tab.$children[0].$data.updateCount;

			// 等于 0  则跳过
			if(updateCount <= 1) {
				return true
			}

			//更新次数大于等于1 说明用户修改过当前页数据 因为获取详情时会更新一次
			return new Promise((resolve, reject) => {
				this.$confirm('您填写的内容未保存，确定离开吗？', () => {
					// 重置修改计算
					that.$refs.componentPage[i].$data.updateCount = 0;
					resolve(true)
				}, () => {
					resolve(false)
				})
			});
		},
		// 设置参数，用于界面传参
		setPageParam(e) {
			this.pageParam = e
		},
		// 获取参数，用于界面传参
		getPageParam() {
			return this.pageParam
		},
	},
}
</script>

<style scoped lang="less">


</style>
