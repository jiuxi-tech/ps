<template>
	<div :class="getClass">

		<slot v-if="$slots.default"/>

		<template v-if="showLoading">
			<div :class="`${prefix}-radio-group__loading`">
				<fb-loading type="data" size="12">加载中...</fb-loading>
			</div>
		</template>

		<!--		<template v-if="service && myData.length === 0">-->
		<!--			<div :class="`${prefix}-radio-group__empty`">-->
		<!--				没有数据-->
		<!--			</div>-->
		<!--		</template>-->


		<fb-radio
			v-for="(node, i) in myData" :key="node[reader.value]"
			:label="node[reader.label]"
			:disabled="node[reader.disabled]"
			:readonly="node[reader.readonly]"
			:size="node[reader.size] || size"
			v-model="node[reader.value]"
			:style="{
				[`${vertical ? 'marginBottom' : 'marginRight'}`]:  `${radioSpaceNum}px`
			}"
		>
			<slot :node="node" :index="i"></slot>
		</fb-radio>


	</div>
</template>

<script>
/**
 * FbRadioGroup
 * (c) 2020 lincong1987
 */

import { assign, each, findIndex, isArray, isFunction } from 'lodash-es'
import { closest } from '../../../utils/componentUtils'
import FbRadio from '../../radio/src/FbRadio'

import { prefix } from '../../../../src/config'
import FbLoading from '../../loading/src/FbLoading'
import FbEmpty from '../../empty/src/FbEmpty'

export default {

	name: 'FbRadioGroup',
	components: {
		FbEmpty,
		FbLoading,
		FbRadio,
	},
	props: {
		value: {
			type: [String, Number, Boolean],
			default: '',
		}, // 如果传入一个数组
		data: {
			type: Array,
			default () {
				return []
			},
		},

		// 是否垂直
		vertical: {
			type: Boolean,
			default: false,
		},

		size: {
			type: String,
			default: undefined,
		},

		// 按钮样式
		button: {
			type: Boolean,
			default: false,
		},

		// 100% 宽度
		long: {
			type: Boolean,
			default: false,
		},

		// 列表样式
		list: {
			type: Boolean,
			default: false,
		},

		reader: {
			type: Object,
			default () {
				return {
					value: 'value',
					label: 'label',
					disabled: 'disabled',
					readonly: 'readonly',
					size: 'size',
				}
			},
		},

		disabled: {
			type: Boolean,
			default: false,
		},
		readonly: {
			type: Boolean,
			default: false,
		},

		// radio 间距
		radioSpace: {
			type: [Number, String],
			default: 0,
		},

		// 服务获取节点数据
		service: {
			type: [Object, Array, Function],
			default: undefined,
		},
		// 查询参数
		param: {
			type: [Object, Array, Function],
			default: undefined,
		},
		// 网络数据过滤器
		dataFilter: {
			type: Function,
			default (data) {
				return (data)
			},
		},
		// 自动加载数据
		autoLoad: {
			type: Boolean,
			default: true,
		},

	},
	data () {
		return {
			prefix,
			currentValue: this.value,
			name: `fb_radio_${new Date().getTime()}`,

			selectedRadio: null,

			myData: this.data,
			showLoading: false,
			showEmpty: false,
			myQueryParam: this.param,
		}
	},

	watch: {
		data: {
			handler (value) {
				this.myData = value
				this.update(this.currentValue)
			},
			deep: true,
		},
		value (value) {

			if (value === this.currentValue) return
			this.currentValue = value
			this.changeValue(this.currentValue)
		},
	},
	computed: {
		radioSpaceNum () {
			return parseFloat(this.radioSpace)
		},
		getClass () {
			const arr = [`${prefix}-radio-group`]
			if (this.vertical) {
				arr.push(`${prefix}-radio-group--vertical`)
			}
			if (this.button) {
				arr.push(`${prefix}-radio-group--button`)
			}

			if (this.long) {
				arr.push(`${prefix}-radio-group--long`)
			}

			if (this.button && this.size) {
				arr.push(`${prefix}-radio-group--button--${this.size}`)
			}

			if (this.list) {
				arr.push(`${prefix}-radio-group--list`)
			}
			if (this.list && this.size) {
				arr.push(`${prefix}-radio-group--list--${this.size}`)
			}
			if (this.radioSpaceNum) {
				arr.push(`${prefix}-radio-group--space`)
			}

			return arr
		},
	},

	methods: {
		addItem (radio) {
			//radio.name = this.name
			//radio.group = true
			this.radios.push(radio)
			this.update(this.currentValue)
		},

		removeItem (radio) {

			let radioIndex = findIndex(this.radios, {value: radio.value})
			if (radioIndex) {
				this.radios.splice(radioIndex, 1)
			}

			// this.update('')

		},

		update (value) {
			if (this.radios) {
				this.radios.forEach((radio) => {
					radio.checked = false
					if (radio.value === value) {
						radio.checked = true
						// this.selectedRadio = radio
					}
				})
			}
		},
		// 从子组件修改
		change (value, node, e) {
//			if (this.currentValue === value) {
//				// 清除
//				this.currentValue = ''
//			} else {
//				this.currentValue = value
//			}
			this.currentValue = value
			this.changeValue(value, node, e)
		},
		changeValue (value, node, e) {

			let radio = null
//
//			if (this.selectedRadio) {
//				this.selectedRadio.checked = false
//				this.selectedRadio = null
//			}

//			if (this.currentValue === value) {
//				return
//			}

			this.currentValue = value
			this.update(value)

			if (this.radios) {
				let radios = this.radios.filter((n) => n.value === value)
				radio = radios.length > 0 ? radios[0] : null
			}

			//// console.log("radio group changed", value, radio)

			this.$emit('input', value, radio)
			this.$emit('change', value, radio)
			this.$emit('on-change', value, radio)

			if (this.formItem && e) {
				this.formItem.$emit('on-form-change', value, radio)
			}
		},

//		handleClick (checked, e) {
//			this.$emit('on-click', checked, e)
//		},

		fetchData () {

			this.showLoading = true
			this.showEmpty = false

			let service = isFunction(this.service) ? this.service : this.service.list

			if (isFunction(this.myQueryParam)) {
				this.myQueryParam = this.myQueryParam.apply(this, [])
			}

			if (isArray(this.myQueryParam)) {
				// todo
			}

			service(assign({}, this.myQueryParam)).then(data => {

				this.$emit('on-data-load', data)
				let json = this.dataFilter(data)

				this.updateDataAfterQuery(json)
			}).catch(e => {
				this.loading(false)
			})

		},

		updateDataAfterQuery (json) {

			this.loading(false)

			this.updateData(json && json.data || [])
			// this.myData = json.data || []
//			this.options = this.applyData()
//			this.noChildren = this.options.length

			// this.myPager.page = json.data.page
			// this.myPager.total = json.data.total
			// this.myPager.pageSize = json.data.pageSize
			// this.myPager.total = json.data.size

			this.showEmpty = this.myData.length === 0
			this.$emit('on-data-update')
		},

		updateData (data) {
			this.myData = data
		},
		loading (status) {
			this.showLoading = status
		},
	},

	created () {
		//this.$on('on-click')
		this.radios = []
		this.formItem = closest(this, 'FbFormItem')
	},

	destroyed () {
		this.formItem = null
		this.radios = []
	},

	mounted () {
		if (this.service && this.autoLoad) {
			this.fetchData()
		} else {
			this.$emit('on-data-update')
		}
	},
}
</script>

<style scoped>

</style>
