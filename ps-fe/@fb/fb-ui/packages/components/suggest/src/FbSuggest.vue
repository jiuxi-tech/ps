<template>
	<div
		ref="reference"
		v-clickoutside="close"
		:class="{
			[`${prefix}-suggest`]: true,
            [`${prefix}-suggest--visible`]: showDropdown,
            [`${prefix}-suggest--disabled`]: disabled,
            [`${prefix}-suggest--readonly`]: readonly,
        }"
	>


			<div
				ref="selection"
				:class="`${prefix}-suggest__container`"
				@click.stop="toggle"
			>
				<fb-input
					ref="input"
					:clearable="clearable"
					v-model="currentValue"
					:placeholder="placeholder"
					:class="`${prefix}-suggest__input`"
					:icon="icon"
					@on-icon-click="handleIconClick"
					@on-input="handleInput"

					v-bind="$attrs"
				>
				</fb-input>

			</div>

			<transition name="slide-to-down">
				<div ref="popper" v-show="showDropdown" :class="[`${prefix}-popper-no-arrow`, popperClass]">
					<div :class="[`${prefix}-suggest__dropdown`, { 'no-options': filteredOptions.length === 0,  }]">
						<ul v-if="filteredOptions.length > 0"
							:class="`${prefix}-suggest__dropdown-list`">
							<li
								v-for="(option, index) in filteredOptions"
								@click.stop="selectOption(option.uuid)"
								:key="option.uuid"
								:class="[`${prefix}-suggest-option`,
													{
														//[`${prefix}-suggest-option--selected`]: selectedOption && selectedOption[filterBy] && selectedOption[filterBy] === currentValue,
														[`focus`]:focusOption && focusOption.uuid &&  focusOption.uuid === option.uuid
													}]"
								@mouseover.stop="doOptionFocus(option)"
							>
								<template v-if="$scopedSlots.option">
									<slot name="option" :option="option" :index="index"></slot>
								</template>
								<template v-else>
									{{ option[filterBy] }}
								</template>
							</li>
						</ul>
						<div v-if="showEmpty" :class="`${prefix}-suggest__dropdown__empty`">
							{{ emptyText }}
						</div>
						<div v-if="showLoading" :class="`${prefix}-suggest__dropdown__loading`">
							{{ loadingText }}
						</div>
					</div>
				</div>
			</transition>

	</div>
</template>


<script>

import clickoutside from '../../../utils/clickoutside'
import { closest } from '../../../utils/componentUtils'
import { assign, debounce, isFunction, isArray, each } from 'lodash-es'
import { getViewport, uuid } from '../../../utils/utils'
import FbIcon from '../../icon/src/FbIcon'
import { prefix } from '../../../../src/config'
import FbDropDown from '../../../utils/dropdown-popper'
import FbContainer from '../..//container/src/FbContainer'
import FbInput from '../..//input/src/FbInput'
import PopperMixin from '../../../utils/popper-mixin'

/**
 * FbSuggest
 * (c) 2023 lincong1987
 */
export default {
	name: 'FbSuggest',
	mixins: [PopperMixin],
	components: {
		FbInput,
		FbContainer,
		FbDropDown,
		FbPager: () => import('../../pager/src/FbPager'),
		FbIcon,
	},
	directives: {
		clickoutside,
	},
	props: {
		/**
		 * @param {'s'|'m'|'l'} size 尺寸
		 */
		size: {
			type: String,
			default: 'm',
		},
		icon: {
			type: String,
			default: 'more',
		},
		// 绑定值
		value: {
			type: [String],
			default: '',
		},
		// 占位
		placeholder: {
			type: String,
			default: '',
		},
		// 禁用
		disabled: {
			type: Boolean,
			default: false,
		},
		// 只读
		readonly: {
			type: Boolean,
			default: false,
		},

		// 显示清除按钮
		clearable: {
			type: Boolean,
			default: true,
		},
		// 数据，[1,2,3] 或 [{}，{}，{}]
		data: {
			type: Array,
			default () {
				return null
			},
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

		// 每次键入内容后，都向后台发送查询请求
		always: {
			type: Boolean,
			default: false,
		},

		// 向服务端查询时提供的字段名称
		keywordName: {
			type: String,
			default: 'keyword',
		},
		// 网络数据过滤器
		dataFilter: {
			type: Function,
			default (data) {
				return (data)
			},
		},

		// 请求数据时的提示
		loadingText: {
			type: String,
			default: '加载中...',
		},

		// 未找到匹配选项时的提示
		emptyText: {
			type: String,
			default: '无匹配项',
		},

		// 按哪个字段匹配内容
		filterBy: {
			type: String,
			default: 'label',
		},

		// 自定义选项过滤器，默认按 filterBy 过滤
		filter: {
			type: Function,
		},

		popperClass: String,
	},

	data () {

		let myFilter = this.filter || ((data, value, filterBy) => {
			return data.filter(option => {
				return option[filterBy].indexOf(value) !== -1
			})
		})

		return {
			uuid: uuid(),
			mode: '',
			prefix,
			focusOption: null,
			currentValue: this.value,
			showClear: false,
			showDropdown: false,
			myData: this.data,
			options: [],
			filteredOptions: [],
			noOptions: true,
			showLoading: false,
			showEmpty: false,
			myQueryParam: this.param,
			myFilter,

		}
	},
	computed: {
		// tab 键顺序
		getTabindex () {
			if (this.disabled || this.readonly) {
				return -1
			}
			return 0
		},

	},
	watch: {
		value (value) {
			this.currentValue = value
			// this.changeCurrentValue()
		},
		showDropdown (value) {
			this.$nextTick(() => {
				if (value) {
					//this.$refs.input.$refs.input.focus()
				}
			})
		},
		data: {
			handler (value) {
				this.mixData(value)
			},
			deep: true,
		},

		param (val) {
			this.myQueryParam = val
		},
	},

	methods: {
		toggle () {
			if (this.disabled || this.readonly) return
			this.showDropdown = !this.showDropdown
			this.updatePopper()
			this.updatePosition()
		},
		selectOption (uuid) {

			if (this.disabled || this.readonly) {
				return
			}
			//const uuid = e.target.getAttribute('data-uuid')

			const option = this.filteredOptions.find((option) => {
				return option.uuid === uuid
			})
			if (option) {
				this.clickOption(option)
			}
		},
		clickOption (option) {
			if (this.disabled || this.readonly) return
			this.showDropdown = false
			this.currentValue = option[this.filterBy]
			this.finished()
			this.changeCurrentValue()
		},
		changeCurrentValue () {
			if (this.noOptions) {
				each(this.options, (option) => {
					let selected
					selected = this.currentValue === option.value
					option.selected = selected
				})
			}

			let options = null

			let matched = this.options.filter(opt => this.currentValue === opt[this.filterBy])
			options = matched.length > 0 ? matched[0] : null

			this.$emit('input', this.currentValue, options)
			this.$emit('change', this.currentValue, options)
			this.$emit('on-change', this.currentValue, options)

			this.formItem && this.formItem.$emit('on-form-change', this.currentValue, options)
		},
		removeSelectedOption (option) {
			if (this.disabled || this.readonly) return
			const index = this.currentValue.indexOf(option.value)
			if (index != -1) {
				this.currentValue.splice(index, 1)
			}
			this.finished()
			this.changeCurrentValue()
		},

		updatePosition () {
			console.log('this.updatePosition()')
			this.$nextTick(() => {
				let elStyle = this.$el.getBoundingClientRect()
				this.$refs.popper && (this.$refs.popper.style.width = `${elStyle.width}px`)
			})
		},
		close () {
			if (this.showDropdown) {
				this.finished()
			}
			this.showDropdown = false
		},
		finished () {
			this.$emit('on-finished', this.currentValue)

			this.fbFormItem && this.fbFormItem.$emit('on-form-blur', this.currentValue)
		},

		doOptionFocus (option) {
			if (this.focusOption) {
				this.focusOption.focused = false
			}
			option.focused = true
			this.focusOption = option
		},

		fetchData () {

			return new Promise((resolve, reject) => {
				this.loading(true)
				//	this.showEmpty = false

				let service = isFunction(this.service) ? this.service : this.service.list

				if (isFunction(this.myQueryParam)) {
					this.myQueryParam = this.myQueryParam.apply(this, [])
				}

				if (isArray(this.myQueryParam)) {
					// todo
				}

				service(assign({}, this.myQueryParam, {
					[`${this.keywordName}`]: this.currentValue,
					[`${this.filterBy}`]: this.currentValue,

				})).then(json => {
					this.$emit('on-data-load', json)
					this.mixData(this.dataFilter(json))
					this.loading(false)
					resolve()
				}).catch(e => {
					this.loading(false)

					reject()
				})
			})

		},

		updateData (data) {
			this.mixData(data)
		},

		onceFetchData () {

			return new Promise((resolve, reject) => {
				let service = isFunction(this.service) ? this.service : this.service.list

				if (isFunction(this.myQueryParam)) {
					this.myQueryParam = this.myQueryParam.apply(this, [])
				}

				service(assign({}, this.myQueryParam)).then(json => {

					this.$emit('on-data-load', json)
					resolve(this.dataFilter(json))

				}).catch(e => {
					reject()
				})
			})

		},

		loading (status) {
			this.showLoading = status
		},

		mixData (data) {
			this.options = data.map((option, index, array) => {
				if (typeof option === 'object') {
					return {
						uuid: uuid(),
						label: option[this.filterBy],
						selected: false,
						focused: false,
						...option,
					}
				} else if (typeof option === 'string' || typeof option === 'number') {
					return {
						uuid: uuid(),
						label: `${option}`,
						selected: false,
						focused: false,
					}
				}

			})

		},

		init () {

			if (this.mode === 'data') {
				this.mixData(this.data)
			}
			if (this.mode === 'once') {
				this.onceFetchData().then((data) => {
					this.mixData(data || [])
				})
			}
		},

		/*	handleInputBlur () {
		 console.log('handleInputBlur')
		 this.$emit('on-blur')
		 if (!this.disabled && !this.readonly && this.clearable) {
		 this.$nextTick(() => {
		 this.showClear = false
		 })
		 }
		 },
		 handleInputFocus () {
		 console.log('handleInputFocus')
		 this.$emit('on-focus')

		 if (!this.disabled && !this.readonly && this.clearable &&
		 (this.currentValue !== '' || this.currentValue.length > 0)) {
		 this.$nextTick(() => {
		 this.showClear = true
		 })
		 }

		 this.startFilter()

		 },*/

		handleInput () {
			console.log('handleInput')
			this.$nextTick(() => {
				this.$emit('on-input', this.currentValue)
				this.$emit('on-change', this.currentValue)
				this.$emit('input', this.currentValue)
			})
		},
		handleIconClick () {
			this.startFilter()
		},
		handleKeydown (event) {
			this.startFilter()
		},

		startFilter () {
			if (this.mode === 'data') {
				this.showFilter()
			} else if (this.mode === 'once') {
				this.showFilter()
			} else if (this.mode === 'always') {
				this.filteredOptions = []
				this.options = []
				this.updatePosition()
				// this.showDropdown = true
				this.showEmpty = false
				this.loading(true)
				this.fetchData().then(() => {
					this.showFilter()
				})
			}
		},

		showFilter () {
			this.updatePosition()
			// this.showDropdown = true
			let value = this.currentValue.trim()
			if (value === '') {
				this.filteredOptions = this.options
			} else {
				this.filteredOptions = this.myFilter(this.options, value, this.filterBy)
			}

			this.showEmpty = this.filteredOptions.length === 0
		},

	},

	mounted () {

		this.$refs.input.$refs.input.addEventListener('keydown', this.debounceHandleKeydown, false)

		// 确定模式
		if (this.data) {
			this.mode = 'data'
		} else if (this.service) {
			if (this.always) {
				this.mode = 'always'
			} else {
				this.mode = 'once'
			}
		}

		this.init()

	},
	created () {
		this.formItem = closest(this, 'FbFormItem')
		this.debounceHandleKeydown = debounce(this.handleKeydown, 320, {})
	},

	beforeDestroy () {
		this.formItem = null
		this.debounceHandleKeydown.cancel()
		this.$refs.input.$refs.input.removeEventListener('keydown', this.debounceHandleKeydown, false)
	},
}


</script>

<style scoped>

</style>
