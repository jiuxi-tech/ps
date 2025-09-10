<template>
	<div class="tx-demo-page fb-page-search">
		<fb-cardx header="松鼠家有限公司" no-header-border no-border>
			<div slot="tags">
				<fb-tag type="primary" effect="plain">塘西镇</fb-tag>
				<fb-tag type="danger" effect="light">危化品生产</fb-tag>
			</div>
			<div slot="actions">
				<fb-radio-group @on-change="handleChange" button :data="btnOpts" value="2" size="m"/>
			</div>


			<fb-container radius="4px" :background="colors.blue_1" pt="16px">
				<fb-property colon label-width="140px" mode="form" class="card-property">
					<fb-row>
						<fb-col span="12">
							<fb-property-item label="统一社会信用代码" ellipsis>
								91330110731531980B
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="系统注册时间" ellipsis>
								2018-09-21
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="12">
							<fb-property-item label="法定代表人" ellipsis>
								李岚清  15711112222
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="主要负责人" ellipsis>
								何庆峰  15966668888
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="12">
							<fb-property-item label="详细地址" ellipsis>
								杭州余杭区塘西镇唐家埭村 <fb-icon name="location-fill" :color="colors.red_6"></fb-icon>
							</fb-property-item>
						</fb-col>
						<fb-col span="12">
							<fb-property-item label="经营范围" ellipsis>
								稀硝酸、亚硝酸钠、氨水、试剂产品
							</fb-property-item>
						</fb-col>
					</fb-row>



				</fb-property>
			</fb-container>

		</fb-cardx>

		<fb-card>
			<fb-input v-model="searchVal" placeholder="在此搜索一企一档模块">
				<div slot="append-button">
					<fb-button type="primary" icon="search"></fb-button>
				</div>
			</fb-input>
		</fb-card>

		<fb-card header="企业基础管理">
			<fb-row gutter="16" class="breath-row">
				<fb-col span="6" v-for="(item, idx) in businessArr" :key="idx">
					<fb-cardx-breath @on-click="handleBreath(item)" :breathLight="searchVal && item.title.indexOf(searchVal) >= 0" :card-status="`${item.cardStatus}`">
						<fb-container slot="box" position="relative" pl="41px">
							<div class="breath-left">
								<fb-icon :name="item.iconLeft" size="16"></fb-icon>
							</div>
							<div class="breath-title">{{item.title}}</div>
							<div class="breath-text">{{item.text}}</div>
							<div class="breath-right">
								<fb-tooltip v-if="item.tooltip" effect="dark" :content="item.tooltip" placement="top">
									<fb-icon :name="item.iconRight" size="16"></fb-icon>
								</fb-tooltip>
								<fb-icon v-else :name="item.iconRight" size="16"></fb-icon>
							</div>
						</fb-container>
					</fb-cardx-breath>
				</fb-col>
			</fb-row>
		</fb-card>

		<fb-card header="安全生产监管信息">
			<fb-row gutter="16" class="breath-row">
				<fb-col span="6" v-for="(item, idx) in businessArr" :key="idx">
					<fb-cardx-breath @on-click="handleBreath(item)" :breathLight="searchVal && item.title.indexOf(searchVal) >= 0" :card-status="`${item.cardStatus}`">
						<fb-container slot="box" position="relative" pl="41px">
							<div class="breath-left">
								<fb-icon :name="item.iconLeft" size="16"></fb-icon>
							</div>
							<div class="breath-title">{{item.title}}</div>
							<div class="breath-text">{{item.text}}</div>
							<div class="breath-right">
								<fb-tooltip v-if="item.tooltip" effect="dark" :content="item.tooltip" placement="top">
									<fb-icon :name="item.iconRight" size="16"></fb-icon>
								</fb-tooltip>
								<fb-icon v-else :name="item.iconRight" size="16"></fb-icon>
							</div>
						</fb-container>
					</fb-cardx-breath>
				</fb-col>
			</fb-row>
		</fb-card>


		<fb-card header="安全生产管理台账">
			<fb-row gutter="16" class="breath-row">
				<fb-col span="6" v-for="(item, idx) in businessArr" :key="idx">
					<fb-cardx-breath @on-click="handleBreath(item)" :breathLight="searchVal && item.title.indexOf(searchVal) >= 0" :card-status="`${item.cardStatus}`">
						<fb-container slot="box" position="relative" pl="41px">
							<div class="breath-left">
								<fb-icon :name="item.iconLeft" size="16"></fb-icon>
							</div>
							<div class="breath-title">{{item.title}}</div>
							<div class="breath-text">{{item.text}}</div>
							<div class="breath-right">
								<fb-tooltip v-if="item.tooltip" effect="dark" :content="item.tooltip" placement="top">
									<fb-icon :name="item.iconRight" size="16"></fb-icon>
								</fb-tooltip>
								<fb-icon v-else :name="item.iconRight" size="16"></fb-icon>
							</div>
						</fb-container>
					</fb-cardx-breath>
				</fb-col>
			</fb-row>
		</fb-card>
	</div>

</template>

<script>
	import {findIndex} from "lodash-es";

	export default {
		name: 'index3',
		// 接收父组件的传参
		props: {},
		data () {
			return {
				searchVal: '',
				btnOpts: [
					{value: '1', label: '企业画像'},
					{value: '2', label: '一企一档'}
				],
				businessArr: [
					{cardStatus: "blue", title: '基本信息', text: '未完善',  iconLeft: 'attachment', iconRight: 'information-fill', tooltip: '我是一段解释'},
					{cardStatus: "grey", title: '信息', text: '未完善',  iconLeft: 'attachment', iconRight: 'information-fill', tooltip: '我是一段解释'},
					{cardStatus: "yellow", title: '基本信息', text: '未完善', iconLeft: 'attachment', iconRight: 'information-fill'},
					{cardStatus: "orange", title: '信息', text: '未完善',  iconLeft: 'attachment', iconRight: 'information-fill', tooltip: '我是一段解释'},
					{cardStatus: "red", title: '基本信息', text: '未完善',  iconLeft: 'attachment', iconRight: 'information-fill', tooltip: '我是一段解释'},
					{cardStatus: "purple", title: '基本信息', text: '未完善',  iconLeft: 'attachment', iconRight: 'information-fill', tooltip: '我是一段解释'},
				],
			}
		},
		// 初始化方法
		// mounted () {
		// 	this.init()
		// },

		// keeplive下会触发的方法
		activated() {
			this.init()
		},

		watch: {
			// $route: {
			// 	handler(route) {
			// 		this.init()
			// 	},
			// 	deep: true,
			// },
		},

		// 方法
		methods: {
			handleChange (val) {
				console.log(val)
				if (val == '1') {
					this.$router.push('/sys/index/index')
				}
			},
			handleBreath (val) {
				console.log(val)
			},
			init (realRoute) {
				let route = realRoute || this.$route
				console.log('wwwwwwwwwwwwwwww')
				if (route.query.id) {
					this.$message.success(`我的id:${this.$router.decrypt(route.query.id)}，名字：${route.query.tabLabel}`)
				}
			}
		},

		// beforeRouteUpdate(to, from, next) {
		// 	if (to.fullPath !== from.fullPath) {
		// 		this.init(to)
		// 	}
		// 	next()
		// },
		// beforeRouteLeave (to, from, next) {
		// 	console.log(this)
		// }
	}
</script>

<style lang="less" scoped>
	@import "@/assets/styles/common.less";
	.tx-demo-page {

		.card-property {
			.@{FbUiPrefix}-row {
				padding-bottom: 12px;
			}
		}

		.breath-row {
			.@{FbUiPrefix}-col {
				margin-top: 16px;
				transition: all 0.3s;
				cursor: pointer;

				&:nth-child(-n + 4) {
					margin-top: 0;
				}
			}


			.breath-title {
				font-size: 14px;
				line-height: 22px;
				font-weight: 700;
			}
			.breath-text {
				font-size: 13px;
				line-height: 22px;
			}
			.breath-left {
				position: absolute;
				top: 4px;
				left: 10px;
				z-index: 9;
			}
			.breath-right {
				position: absolute;
				top: 4px;
				right: 10px;
				z-index: 9;
			}
		}
	}



</style>

