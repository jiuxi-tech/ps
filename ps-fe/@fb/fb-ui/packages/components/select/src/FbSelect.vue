<template>
	<div
		ref="reference"
		:id="uuid"
		v-clickoutside="close"
		:class="{
			[`${prefix}-select`]: true,
            [`${prefix}-select--visible`]: showDropdown,
            [`${prefix}-select--disabled`]: disabled,
            [`${prefix}-select--readonly`]: readonly,
            [`${prefix}-select--multiple`]: multiple,
            [`${prefix}-select--size-${size}`]: !!size,
        }"
	>

		<div
			ref="selection"
			@click.stop="toggle"
			@mouseenter="focus"
			@mouseleave="blur"
			@focus="focus"
			@blur="blur"
			@keydown="keydown"
			:class="`${prefix}-select__selection`"
		>
			<template v-if="multiple">
			<span
				v-if="currentValue === '' || currentValue.length == 0"
				:class="`${prefix}-select__placeholder`"
			>
				{{ placeholder || localPlaceholder }}
			</span>
				<template v-else-if="collapseTags">
					<div :class="`${prefix}-select--multiple--selected`">
						<span v-if="selectedOption.length > 1"
						      :class="`${prefix}-select__selected-value ${prefix}-select__collapse-tags-num`">
							已选：{{ selectedOption.length }}
						</span>
						<span
							:title="selectedOption[0].currentLabel"
							:class="`${prefix}-select__selected-value`"
						>
							{{ selectedOption[0].currentLabel }}
							<fb-icon
								@on-click="removeSelectedOption(selectedOption[0])"
								name="close"
								size="12"
								:class="`${prefix}-select__selected-value__clear`"
							/>
						</span>
						<!--						<span v-if="selectedOption.length > 1"-->
						<!--							  :class="`${prefix}-select__selected-value ${prefix}-select__collapse-tags-num`">-->
						<!--							+ {{ selectedOption.length - 1 }}...-->
						<!--						</span>-->
					</div>
				</template>
				<template v-else>
					<div :class="`${prefix}-select--multiple--selected`">
					<span
						v-for="(option, index) in selectedOption"
						:key="index"
						:title="option.currentLabel"
						:class="`${prefix}-select__selected-value`"
					>
						{{ option.currentLabel }}
						<fb-icon
							@on-click="removeSelectedOption(option)"
							name="close"
							size="12"
							:class="`${prefix}-select__selected-value__clear`"
						/>
					</span>
					</div>
				</template>
			</template>
			<template v-else>
			<span :class="getUISelectClass"
			      @mouseenter="handleTitleAttr">
				{{
					(selectedOption && selectedOption.currentLabel) ||
					placeholder ||
					localPlaceholder
				}}
			</span>
			</template>
			<div :class="`${prefix}-select__selection-icons`">
				<fb-icon v-show="!showClear"
				         :class="`${prefix}-select__selection-icons__down`"
				         size="12"
				         :name="iconName"/>
				<fb-icon
					v-if="clearable"
					v-show="showClear"
					@on-click.stop="clear"
					size="12"
					:class="`${prefix}-select__selection-icons__clear`"
					name="close-circle-fill"
				/>
			</div>
		</div>

		<transition name="slide-to-down">
			<div ref="popper" v-show="showDropdown" :class="[`${prefix}-popper-no-arrow`, popperClass]">
				<div :class="[`${prefix}-select__dropdown`,
						 { 'no-option-children': noChildren, 'has-pager': myShowPager }]"
				>
					<template v-if="filterable">
						<input
							ref="searchInput"
							v-model="searchValue"
							:placeholder="'搜索...'"
							@input="changeSearchValue"
							@keydown="keydown"
							:class="`${prefix}-select__selected-input`"
						/>
					</template>

					<ul @click.stop="selectOption"
					    :class="`${prefix}-select__dropdown-list`">
						<slot/>


						<template v-if="noChildren">
							<fb-select-option
								:parent-id="uuid"
								v-for="(item, index) in currentOptions"
								:label="item.label"
								:value="item.value"
								:icon="item.icon"
								:key="item.value"
								:data-key="JSON.stringify(item.value)"
								:class="[`${prefix}-select-option`,
							{
								[`${prefix}-select-option--selected`]: item.selected,
								[`${prefix}-select-option--disabled`]: item.disabled || (disableLimit && !item.selected),
								[`focus`]: item.isFocus
							}, selectOptionClass]"
								:style="[selectOptionStyle]"
								@mouseenter.stop="handleTitleAttr"
								@mouseover.stop="doFocus(item)"
							>
								<slot name="option" :node="item"></slot>
							</fb-select-option>


						</template>

						<!--							<li :class="`${prefix}-select-pager`" v-if="myShowPager">-->

						<!--							</li>-->

					</ul>

					<template v-if="myShowPager">
						<fb-container :class="`${prefix}-select-pager`">
							<fb-pager :simple="myPager.simple" :max-length="myPager.maxLength"
							          :pages="myPager.pages"
							          :size="myPager.size"
							          :current="myPager.current"
							          :total="searchOptions.length"
							          @on-change="handlePageChange"
							></fb-pager>
						</fb-container>
					</template>

					<template v-if="showEmpty">
						<div :class="`${prefix}-select__dropdown__empty`">
							<fb-empty type="data"></fb-empty>
						</div>
					</template>

					<template v-if="showLoading">
						<div :class="`${prefix}-select__dropdown__loading`">
							<fb-loading type="data">加载中...</fb-loading>
						</div>
					</template>
				</div>
			</div>
		</transition>


	</div>
</template>


<script>

import clickoutside from '../../../utils/clickoutside'
import { closest } from '../../../utils/componentUtils'
import { assign, debounce, isFunction, isArray, each, indexOf, filter, every } from 'lodash-es'
import { getViewport, uuid } from '../../../utils/utils'
import FbSelectOption from './FbSelectOption'
import keyCode from '../../../utils/keyCode'
import FbIcon from '../../icon/src/FbIcon'
import axios from 'axios'
import FbEmpty from '../../empty/src/FbEmpty'
import FbLoading from '../../loading/src/FbLoading'
import { prefix } from '../../../../src/config'
// import FbDropDown from '../../../utils/dropdown-popper'
import FbContainer from '../../container/src/FbContainer'
import PopperMixin from '../../../utils/popper-mixin'

/**
 * FbSelect
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbSelect',
	components: {
		FbContainer,
		// FbDropDown,
		FbPager: () => import('../../pager/src/FbPager'),
		FbIcon,
		FbSelectOption,
		FbEmpty,
		FbLoading,

	},
	directives: {
		clickoutside,
	},
	mixins: [PopperMixin],
	props: {
		// 尺寸
		size: {
			type: String,
			default: 'm',
		},
		// 绑定值
		value: {
			type: [String, Number, Array, Boolean],
			default: '',
		},
		// 占位
		placeholder: {
			type: String,
			default () {
				return ''
			},
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
		// 搜索框
		filterable: {
			type: Boolean,
			default: false,
		},
		// 显示清除按钮
		clearable: {
			type: Boolean,
			default: true,
		},
		// 多选
		multiple: {
			type: Boolean,
			default: false,
		},
		// 显示多选图标
		showMultipleIcon: {
			type: Boolean,
			default: false,
		},
		// 最多选几个, 0不限制
		multipleLimit: {
			type: Number,
			default: 0,
		},
		// 多选时是否将选中值按文字的形式展示
		collapseTags: {
			type: Boolean,
			default: false,
		},
		// 节点数据
		data: {
			type: Array,
			default () {
				return []
			},
		},
		// 网络获取节点数据
		url: {
			type: String,
			default: undefined,
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
		// 取值配对
		reader: {
			type: Object,
			default () {
				return {
					value: 'value',
					label: 'label',
				}
			},
		},
		// 自动加载数据
		autoLoad: {
			type: Boolean,
			default: true,
		},

		showPager: {
			type: Boolean,
			default: false,
		},
		// 分页配置
		pager: {

			type: [Object, Boolean, Function],
			default () {
				return {}
			},

		},

		autoPager: {
			type: [Boolean, Number, String],
			default: false,
		},

		// 选项扩展类名
		selectOptionClass: {
			type: [Object, Array],
		},
		// 选项扩展样式
		selectOptionStyle: {
			type: [Object, Array],
		},
		// 选中前的事件
		beforeSelect: {
			type: Function,
			default: undefined,
		},
		// 下拉icon图标
		iconName: {
			type: String,
			default: 'down',
		},
		popperClass: String,
	},

	data () {
		//const options = this.applyData()

		let myShowPager = this.showPager
		if (this.autoPager) {
			myShowPager = true
		}

		return {
			uuid: uuid(),
			prefix,
			focusOption: null,
			currentValue: this.value,
			searchValue: '',
			showClear: false,
			showDropdown: false,
			myData: this.data,
			options: [],
			noChildren: 0,
			searching: false,

			showLoading: false,

			showEmpty: false,

			myPager: this.mixPager(this.pager),

			myShowPager,

			myQueryParam: this.param,
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

		getUISelectClass () {
			if (this.selectedOption && this.selectedOption.currentLabel) {
				return `${this.prefix}-select__selected-value`
			}
			return `${this.prefix}-select__placeholder`
		},
		localPlaceholder () {
			return this.placeholder
		},
		selectedOption () {
			const arr = []
			each(this.options, (option) => {
				if (option.selected) {
					arr.push(option)
				}
			})
			if (this.multiple) {
				return arr
			}
			if (arr.length > 0) {
				return arr[0]
			}
			return null
		},
		disableLimit () {
			const {
				currentValue,
				multiple,
				multipleLimit,
			} = this
			let disableLimit = false
			if (multiple) {
				disableLimit = (currentValue || []).length >= multipleLimit && multipleLimit > 0
			}
			return disableLimit
		},
		searchOptions () {
			const searchOptions = []
			each(this.options, (option) => {

				// if (this.searchValue && indexOf(option.label, this.searchValue) != -1) {
				if (this.searchValue && option.label.indexOf(this.searchValue) != -1) {
					searchOptions.push(option)
				} else if (!this.searchValue) {
					searchOptions.push(option)
				}
			})
			return searchOptions
		},
		currentOptions () {

			if (this.autoPager !== false) {

				// console.log(JSON.stringify(this.myPager))

				const start = this.myPager.size * (this.myPager.current - 1)
				const arr = this.searchOptions.slice(start, start + this.myPager.size)

				this.myPager.pages = Math.ceil(this.searchOptions.length / this.myPager.size)

				return arr
			} else {
				return this.searchOptions
			}

		},
	},
	watch: {
		value (value) {
			// ld 2022-03-23 14:29
			// 两个值都一样，不需要进行判断
			// 如果控件存在联动，进行值一样判断，会导致另一个控件的值无法同步
			if (value === this.currentValue) {
				return
			}

			this.currentValue = value

			if (this.multiple && !isArray(this.currentValue)) {
				this.currentValue = value ? [value] : []
			}

			this.changeCurrentValue()
		},
		showDropdown (value) {
			if (this.filterable && value) {
				this.$nextTick(() => {
					this.$refs.searchInput.focus()
				})
			}
		},
		data: {
			handler () {
				this.myData = this.data
				this.options = this.applyData()

				this.noChildren = this.options.length
			},
			deep: true,
		}
	},
	created () {

		this.formItem = closest(this, 'FbFormItem')
		this.addOptionCache = []

		this.debouncedAppend = debounce(function () {
			//this.options = this.options.concat(this.addOptionCache);
			this.addOptionCache = []
		})

	},

	beforeDestroy () {
		this.formItem = null
		this.options = []
		this.debouncedAppend.cancel()
	},
	methods: {
		addTitleAttr (target, label) {
			if (target.clientWidth < target.scrollWidth) {
				target.setAttribute('title', label)
			} else {
				target.setAttribute('title', '')
			}
		},
		handleTitleAttr (e) {
			this.addTitleAttr(e.target, e.target.innerText)
		},
		addOption (option) {
				// this.addOptionCache.push(option)
//				this.debouncedAppend()

			if (this.data.length === 0 && !this.service) {
				this.options.push(option)
			}
		},
		removeOption (option) {

			if (this.options.length === 0) {
				return
			}
			const index = indexOf(this.options, option)
			if (index != -1) {
				this.options.splice(index, 1)
			}
			// 如果删除的option在选中状态，则清除选中的值
			if (option.selected) {
				if (this.multiple) {
					const i = indexOf(this.currentValue, option.value) //this.currentValue.indexOf(option.value)
					if (i != -1) {
						this.currentValue.splice(index, 1)
					}
				} else {
					this.currentValue = ''
				}
				this.changeCurrentValue()
			}
		},
		applyData () {
			const options = []
			const use = []

			this.myData.forEach((option) => {
				const label = option[this.reader.label]
				const value = option[this.reader.value]
				let selected
				if (this.multiple) {
					selected = indexOf(this.value, value) != -1
				} else {
					selected = this.value === value
				}
				selected && use.push(value)
				options.push(Object.assign({
					selected: selected || false,
					isFocus: false,
					currentLabel: label,
					value,
					label,
				}, option))
			})
			// 如果value中的某个值，不在data中的话，则清除value中的那个值
			if (this.searching) {
				if (this.multiple) {
					if (this.value.length !== use.length || !every(this.value, (val) => {
						return indexOf(use, val) != -1
					})) {
						this.currentValue = use
						this.changeCurrentValue()
					}
				} else if (!use.some(val => val === this.value)) {
					setTimeout(() => {
						this.currentValue = ''
						this.changeCurrentValue()
					}, 0)
				}
				this.searching = false
			}
			return options
		},
		selectOption (e) {

			if (this.disabled || this.readonly) {
				return
			}
			const key = JSON.parse(e.target.getAttribute('data-key'))

			const data = filter(this.options, (option) => {
				return option.value === key
			})
			if (data.length > 0) {
				const option = data[0]
				if (option.disabled) {
					return
				}
				if (this.disableLimit && !option.selected) {
					return
				}
				this.clickOption(option)
			}
		},
		clickOption (option) {
			if (this.disabled || this.readonly) return
			if (this.multiple) {
				if (!isArray(this.currentValue)) {
					this.currentValue = []
				}
				const index = indexOf(this.currentValue, option.value)
				if (index != -1) {
					this.currentValue.splice(index, 1)
				} else if (this.multipleLimit <= 0 || this.currentValue.length < this.multipleLimit) {
					this.currentValue.push(option.value)
				}

				this.changeCurrentValue()
			} else {

				this.handleBeforeSelect(option.value, () => {
					this.showDropdown = false
					this.currentValue = option.value
					this.finished()
					this.changeCurrentValue()
				})

			}

		},

		async handleBeforeSelect (data, success) {
			let canSelect = true
			if (this.beforeSelect && isFunction(this.beforeSelect)) {
				try {
					canSelect = await this.beforeSelect(data)
					// 默认输出为true
					if (canSelect == undefined) {
						canSelect = true
					}
				} catch (e) {
					// 抛出异常信息
					console.error(e)
					canSelect = false
				}
			}

			if (canSelect) {
				success && success()
			}
		},
		changeCurrentValue () {
			if (this.noChildren) {
				each(this.options, (option) => {
					let selected
					if (this.multiple) {
						selected = indexOf(this.currentValue, option.value) !== -1
					} else {
						selected = this.currentValue === option.value
					}
					option.selected = selected
				})
			}

			let options = null

			if (this.multiple) {
				options = this.options.filter(opt => indexOf(this.currentValue, opt.value) !== -1)
			} else {
				let matched = this.options.filter(opt => this.currentValue === opt.value)
				options = matched.length > 0 ? matched[0] : null
			}

			this.updatePosition()
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
		changeSearchValue: debounce(function () {
			this.searching = true
			this.$emit('on-search', this.searchValue)
			this.myPager.current = 0
		}, 200),
		blur () {
			this.$emit('on-blur')
			if (!this.disabled && !this.readonly && this.clearable) {
				this.showClear = false
			}
		},
		focus () {
			this.$emit('on-focus')

			if (!this.disabled && !this.readonly && this.clearable &&
				(this.currentValue !== '' || this.currentValue.length > 0)) {
				this.showClear = true
			}
		},
		toggle () {
			this.$emit('on-click')
			if (this.disabled || this.readonly) return
			this.showDropdown = !this.showDropdown
			this.updatePopper()
			this.updatePosition()
		},
		updatePosition () {
			this.$nextTick(function () {
				let elStyle = this.$el.getBoundingClientRect()
				// this.$refs.list.style.width = `${elStyle.width}px`
				this.$refs.popper && (this.$refs.popper.style.minWidth = `${elStyle.width}px`)
			})
		},
		clear () {
			if (this.disabled || this.readonly) return
			this.showClear = false
			if (!this.multiple) {
				this.currentValue = ''
			} else {
				this.currentValue = []
			}
			this.searchValue = ''
			this.finished()
			this.changeCurrentValue()
		},
		close () {
			if (this.showDropdown) {
				this.finished()
			}
			this.showDropdown = false
			this.searchValue = ''
		},
		finished () {
			this.$emit('on-finished', this.currentValue)
			this.fbFormItem && this.fbFormItem.$emit('on-form-blur', this.currentValue)
		},
		prev () {
			if (this.myPager.current > 0) {
				this.myPager.current -= 1
				this.$nextTick(function () {
					this.$refs.list.scrollTop = 0
				})
			}
		},
		next () {
			if (this.myPager.current < Math.ceil(this.searchOptions.length / this.myPager.size)) {
				this.myPager.current += 1
				this.$nextTick(function () {
					this.$refs.list.scrollTop = 0
				})
			}
		},
		doFocus (option) {
			if (this.focusOption) {
				this.focusOption.isFocus = false
			}
			option.isFocus = true
			this.focusOption = option
		},
		keydown (e) {
			if (this.disabled || this.readonly) return
			if (!this.showDropdown) {
				if (e.keyCode == keyCode.SPACE || e.keyCode == keyCode.ENTER || e.keyCode == keyCode.MAC_ENTER) {
					e.preventDefault()
					this.showDropdown = true
				}
			} else {
				if (e.keyCode == keyCode.DOWN || e.keyCode == keyCode.UP) {
					e.preventDefault()
					this.mouseChooseOption(e.keyCode)
				}
				if (e.keyCode == keyCode.ENTER || e.keyCode == keyCode.MAC_ENTER || e.keyCode == keyCode.SPACE) {
					e.preventDefault()
					if (!this.focusOption) return
					if (this.focusOption.disabled) return
					if (this.disableLimit && !this.focusOption.selected) {
						return
					}
					this.clickOption(this.focusOption)
					this.$refs.selection.focus()
				}
			}
		},
		mouseChooseOption (direction) {
			let index = direction == keyCode.DOWN ? 0 : this.options.length - 1
			this.options.forEach((item, _index) => {
				if (this.focusOption && item.value === this.focusOption.value) {
					index = direction == keyCode.DOWN ? _index + 1 : _index - 1
					if (direction == keyCode.DOWN) {
						if (index > this.options.length - 1) {
							index = 0
						}
					} else if (direction == keyCode.UP) {
						if (index < 0) {
							index = this.options.length - 1
						}
					}
				}
			})
			const focusOption = this.options[index]
			if (this.focusOption) {
				this.focusOption.isFocus = false
				this.focusOption = null
			}
			focusOption.isFocus = true
			this.focusOption = focusOption
			if (this.focusOption.disabled || (this.disableLimit && !this.focusOption.selected)) {
				this.mouseChooseOption(direction)
			}
		},

		loadData () {

			this.loading(true)
			this.showEmpty = false
			axios.post(this.url, assign({}, this.myQueryParam, this.myPager)).then(data => {

				this.$emit('on-data-load', data)
				let json = this.dataFilter(data)

				this.updateDataAfterQuery(json)
			}).catch(e => {
				this.loading(false)
			})
		},

		fetchData () {

			this.loading(true)
			this.showEmpty = false

			let service = isFunction(this.service) ? this.service : this.service.list

			if (isFunction(this.myQueryParam)) {
				this.myQueryParam = this.myQueryParam.apply(this, [])
			}

			if (isArray(this.myQueryParam)) {
				// todo
			}

			service(assign({}, this.myQueryParam, this.myPager)).then(data => {

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
			this.options = this.applyData()
			this.noChildren = this.options.length

			// this.myPager.page = json.data.page
			// this.myPager.total = json.data.total
			// this.mypager.size = json.data.pageSize
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

		// 处理分页参数
		mixPager (pager) {

			return assign({}, {

				// top bottom both
				position: 'bottom',
				// left center right
				align: 'right',
				simple: true,
				current: 1,
				size: typeof this.autoPager === 'number' ? this.autoPager : 10,
				pages: 0,
				showTotalInfo: false,
				maxLength: 3,

			}, this.myPager, pager)
		},

		handlePageChange (pager) {
			this.$emit('on-pager-change', pager)

			this.myPager = this.mixPager(pager)
		},

	},

	mounted () {

		if (this.service && this.autoLoad) {
			this.fetchData()
		} else if (this.url && this.autoLoad) {
			this.loadData({})
		} else {

			if (this.data.length > 0) {
				this.options = this.applyData()
				this.noChildren = this.options.length
				this.$emit('on-data-update')
			}

		}
	},
}
</script>

<style scoped>

</style>
