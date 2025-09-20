<template>

	<ul v-show="myPages > 0" :class="getClass">
		<li @click="last" :class="`${prefix}-pager-prev`">
			<fb-icon name="left" :size="12"/>
		</li>
		<li
			v-if="showFirst"
			@click="go(1)"
			:class="`${prefix}-pager-item`"
		>
			1
		</li>
		<li
			v-if="showFirst"
			@click="prev()"
			:class="`${prefix}-icon ${prefix}-pager-item-jump-prev`"
		>
			<fb-icon name="left" :size="12"/>
			<fb-icon class="second-icon" name="left" :size="12"/>
		</li>


		<li
			v-for="(code, index) in codes"
			:key="index"
			:class="[
				`${prefix}-pager-item`,
				{ [`${prefix}-pager-item-active`]: myCurrent === code }
			]"
			@click="go(code)"
		>
			{{ code }}
		</li>
		<li
			v-if="showLast"
			@click="nnext()"
			:class="`${prefix}-icon ${prefix}-pager-item-jump-next`"
		>
			<fb-icon name="right" :size="12"/>
			<fb-icon class="second-icon" name="right" :size="12"/>
		</li>
		<li
			v-if="showLast"
			@click="go(myPages)"
			:class="`${prefix}-pager-item`"
		>
			{{ myPages }}
		</li>
		<li @click="next" :class="`${prefix}-pager-next`">
			<fb-icon name="right" :size="12"/>
		</li>
		<li v-if="showSizeChanger" :class="`${prefix}-pager-select`">
			<fb-select
				v-model="mySize"
				:clearable="false"
				@on-change="changePageSize"
				:data="myPageSizeList"
			>
			</fb-select>
		</li>
		<li
			v-if="showQuickJumper"
			@keydown.enter="jumper"
			:class="`${prefix}-pager-quick-jumper`"
		>
			跳至
			<fb-input v-model="jumperPage" class="jumper-input" width="44"/>
			页
		</li>
		<li v-if="showTotalInfo && total > 0"
		    :class="`${prefix}-pager-total-info`">
			共 {{ total }} 条
		</li>
	</ul>

</template>

<script>
/**
 * FbPager
 * (c) 2020 lincong1987
 */

import FbIcon from '../../icon/src/FbIcon'
import FbInput from '../../input/src/FbInput'

import { prefix } from '../../../../src/config'

export default {
	name: 'FbPager',
	components: {
		FbSelect: () => import('../../select/src/FbSelect'),
		FbInput,
		FbIcon,
	},
	props: {
		// reader: {
		// 	type: Object,
		// 	default (val) {
		//
		// 		return {}
		// 	},
		// 	validate (val) {
		// 		return true
		// 	},
		// },

		// 对齐方式
		align: {
			type: String,
			default: 'center',
		},
		// 分页大小
		size: {
			type: [Number, String],
			default: 10,
		},
		// 当前页面
		current: {
			type: [Number, String],
			default: 1,
		},

		// 总页数
		pages: {
			type: [Number, String],
			default: 0,
		},

		// 总记录数
		total: {
			type: [Number, String],
			default: 0,
		},

		// 锁定
		locking: {
			type: Boolean,
			default: false,
		},
		// 分页数下拉
		showSizeChanger: {
			type: Boolean,
			default: true,
		},
		// 分页数列表
		sizeList: {
			type: Array,
			default () {
				return [10, 20, 50, 100]
			},
		},
		// 跳转框
		showQuickJumper: {
			type: Boolean,
			default: false,
		},

		// 显示总页数
		showTotalInfo: {
			type: Boolean,
			default: true,
		},

		simple: {
			type: Boolean,
			default: false,
		},

		maxLength: {
			type: [Number, String],
			default: 6,
		},

	},
	data () {
		return {
			prefix,
			pageSizeList: this.sizeList,
			len: this.maxLength,
			myCurrent: this.current,
			mySize: this.size,
			myPages: this.pages,
			jumperPage: '',
		}
	},
	computed: {
		myPageSizeList () {
			return this.pageSizeList.map((pageSize) => {
				return {
					value: pageSize,
					label: pageSize,
				}
			})
		},
		getClass () {
			let arr = [`${prefix}-pager`]

			arr.push(`${prefix}-pager--align-${this.align}`)
			if (this.simple) {
				arr.push(`${prefix}-pager--simple`)
			}

			return arr
		},
		pageCode () {
			const pageCode = []
			for (let i = 1; i <= this.myPages; i++) {
				pageCode.push(i)
			}
			return pageCode
		},
		codes () {
			const result = []
			if (this.pageCode.length > 0) {
				this.pageCode.slice(0).forEach((item) => {
					if (Math.floor((this.myCurrent - 1) / this.len) * this.len < item) {
						result.push(item)
					}
				})
			}

//			let codes = result.slice(0, this.len)
//
//			let showFirst = codes[0] > this.len
//			let showLast = codes[this.len - 1] < this.myPages
//
//			if (showFirst || showLast) {
//				return result.slice(0, this.len - 1)
//			}
//
//			if (!showFirst && !showLast) {
//				return result.slice(0, this.len)
//			}

			return result.slice(0, this.len)
		},
		showFirst () {
			return this.codes[0] > this.len
		},
		showLast () {
			return this.codes[this.len - 1] < this.myPages
		},
	},
	watch: {
		current (value) {
			if (this.myCurrent === value) {
				return
			}
			this.myCurrent = value
		},

		pages (value) {
			if (this.myPages === value) {
				return
			}
			this.myPages = value
		},

	},
	mounted () {
		if (this.pageSizeList.indexOf(this.size) == -1) {
			this.pageSizeList.push(this.size)
		}
	},
	methods: {
		dispatch () {
			this.$emit('on-change', {
				current: this.myCurrent,
				page: this.myCurrent,
				size: this.mySize,
				pageSize: this.mySize,
				myPages: this.myPages,
				pages: this.myPages,
				total: this.total,
			})
			this.$emit('update:current', this.myCurrent)
			this.$emit('update:size', this.mySize)
		},
		last () {
			if (this.myCurrent > 1 && !this.locking) {
				this.myCurrent -= 1
				this.dispatch()
			}
		},
		next () {
			if (this.myCurrent < this.myPages && !this.locking) {
				this.myCurrent += 1
				this.dispatch()
			}
		},
		prev () {
			if (this.myCurrent > 1 && !this.locking) {
				this.myCurrent -= this.len
				if (this.myCurrent < 1) {
					this.myCurrent = 1
				}
				this.dispatch()
			}
		},
		nnext () {
			if (this.myCurrent < this.myPages && !this.locking) {
				this.myCurrent += this.len
				if (this.myCurrent > this.myPages) {
					this.myCurrent = this.myPages
				}
				this.dispatch()
			}
		},
		go (code) {
			if (this.myCurrent >= 1 && this.myCurrent <= this.myPages && !this.locking) {
				this.myCurrent = code
				this.dispatch()
			}
		},
		jumper () {
			this.go(this.jumperPage)
		},
		changePageSize () {
			this.myCurrent = 1
			this.dispatch()
		},
	},
}
</script>

<style scoped>

</style>
