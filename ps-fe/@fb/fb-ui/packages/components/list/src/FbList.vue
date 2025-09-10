<template>
	<div :class="[ `${prefix}-list`,
		{
			[`${prefix}-list--bordered`]: bordered,
			[`${prefix}-list--split-line`]: splitLine
		}
	]">

		<div :class="`${prefix}-list-header`" v-if="$slots.header || header">
			<slot name="header">{{ header }}</slot>
		</div>

		<div :class="`${prefix}-list-container`" :style="containerStyle">
			<ul :class="`${prefix}-list-items`">
				<template v-for="(item, i) in myData">
					<slot
						name="item"
						:item="item"
						:index="i"
					>
					</slot>
				</template>
			</ul>
		</div>

		<div :class="`${prefix}-list-footer`" v-if="$slots.footer || footer">
			<slot name="footer">{{ footer }}</slot>
		</div>

		<div :class="`${prefix}-link-pager`">
			<fb-pager
				:align="pagerAlign"
				:max-length="pagerMaxLength"
				:show-total-info="pagerShowTotalInfo"
				:simple="pagerSimple"
				:current="myPager.current"
				:size="myPager.size"
				:pages="myPager.pages"
				:total="myPager.total"

				@on-change="handlePagerChange"
			></fb-pager>
		</div>

		<div v-if="showLoading" :class="`${prefix}-link-loading`">
			<div :class="`${prefix}-link-loading__content`">
				<fb-loading text="加载中..."></fb-loading>
			</div>
		</div>

		<div v-if="showEmpty" :class="`${prefix}-link-empty`">
			<div :class="`${prefix}-link-empty__content`">
				<fb-empty type="data"></fb-empty>
			</div>
		</div>

	</div>
</template>

<script>
import FbListItem from './FbListItem'
import FbPager from '../../pager/src/FbPager'
import { assign, isFunction } from 'lodash-es'

import { prefix } from '../../../../src/config'

/**
 * FbList
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbList',
	components: {
		FbPager,
		FbListItem,
	},
	props: {

		// 头部
		header: {
			type: String,
			default: null,
		},

		// 尾部
		footer: {
			type: String,
			default: null,
		},

		// 数据
		data: {
			type: Array,
			default () {
				return []
			},
		},

		// 横向 todo
		horizontal: {
			type: Boolean,
			default: true,
		},

		// 外框
		bordered: {
			type: Boolean,
			default: false,
		},

		// 分割线
		splitLine: {
			type: Boolean,
			default: true,
		},

		service: {
			type: [Object, Function],
			default: null,
		},

		// 响应过滤器
		dataFilter: {
			type: Function,
			default (data) {
				return (data)
			},
		},

		param: {
			type: [Object, Function],
			default: null,
		},

		// 分页参数
		pager: {
			type: Object,
			default () {
				return {
					current: 1,
					size: 10,
					page: 1,
					pages: 0,
					total: 0,
				}
			},
		},

		// 分页
		pagerAlign: {
			type: String,
			default: 'right',
		},

		pagerMaxLength: {
			type: Number,
			default: 6,
		},

		pagerSimple: {
			type: Boolean,
			default: false,
		},

		pagerShowTotalInfo: {
			type: Boolean,
			default: false,
		},

//			itemClass:{
//				type: Function,
//				default (item, i){return "sss"},
//			},

		containerStyle: {
			type: [Object, Array, String, Function],
			default: null,
		},

	},
	data () {
		return {
			prefix,
			myData: this.data,

			showEmpty: false,
			showLoading: false,

			myPager: this.mixPager(this.pager),
			myQueryParam: this.param,

		}
	},

	watch: {
		data (value) {
			this.myData = value
		},
	},

	methods: {

		// 处理分页参数
		mixPager (pager) {

			return assign({}, {

				// top bottom both
				position: 'bottom',
				// left center right
				align: 'right',
				simple: false,
				current: 1,
				size: 10,
				pages: 0,
				showTotalInfo: true,
				showQuickJumper: false,
				showSizeChanger: false,
				maxLength: 6,

			}, this.myPager, pager)
		},

		handlePagerChange (pager) {
//				this.myPager.current = pager.current
//				this.myPager.size = pager.size
//				this.myPager.pages = pager.pages

			this.myPager = this.mixPager(pager)

			if (this.service) {
				this.fetchData()
			}
			if (this.url) {
				this.loadData()
			}
		},

		fetchData () {

			this.showLoading = true
			this.showEmpty = false

			let service = isFunction(this.service) ? this.service : this.service.list

			let param = assign({}, this.myQueryParam, this.myPager)

			service(param).then(data => {

				this.$emit('on-data-load', data)

				let json = this.dataFilter(data)

				this.updateListAfterQuery(json)
			}).catch(e => {
				this.$message.error(e)
				this.showLoading = false
			})

		},

		updateListAfterQuery (json) {

			this.showLoading = false

			this.updateData(json.data.records || [])

			this.myPager = this.mixPager({
				current: json.data.current,
				pages: json.data.pages,
				size: json.data.size,
				total: json.data.total,
			})

			this.showEmpty = this.myPager.pages === 0

		},

		updateData (data) {
			this.myData = data
		},

		doSearch (param) {

			if (param) {
				this.myQueryParam = assign({}, this.myQueryParam, param)
			}

			this.myPager.current = 1

			if (this.service) {
				this.fetchData()
			}

			if (this.url) {
				this.loadData()
			}
		},

		reload () {

			if (this.service) {
				this.fetchData()
			}
//				if (this.url) {
//					this.loadData()
//				}
		},

	},

	mounted () {
		this.$nextTick(() => {
			if (this.service && this.autoLoad) {
				this.fetchData()
			}
		})
	},

}
</script>

<style scoped>

</style>
