<template>
	<div
		v-clickoutside="close"
		:class="{
			[`${prefix}-select`]: true,
            [`${prefix}-select--visible`]: showList,
            [`${prefix}-select--disabled`]: disabled,
            [`${prefix}-select--readonly`]: readonly,
            [`${prefix}-select--multiple`]: multiple
        }"
	>

		<div
			ref="selection"
			@click="toggle"
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
                    {{placeholder || localPlaceholder}}
                </span>
				<template v-else>
					<div :class="`${prefix}-select--multiple--selected`">
                        <span
							v-for="(option, index) in selectedOption"
							:key="index"
							:title="option.currentLabel"
							:class="`${prefix}-select__selected-value`"
						>
                            {{option.currentLabel}}
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
                    {{(selectedOption && selectedOption.currentLabel) ||
                        placeholder ||
                        localPlaceholder}}
                </span>
			</template>
			<div :class="`${prefix}-select__selection-icons`">
				<fb-icon v-show="!showClear"
						 :class="`${prefix}-select__selection-icons__down`"
						 size="12"
						 name="down"/>
				<fb-icon
					v-if="clearable"
					v-show="showClear"
					@on-click="clear"
					size="12"
					:class="`${prefix}-select__selection-icons__clear`"
					name="close-circle-fill"
				/>
			</div>
		</div>
		<transition name="slide-to-down">
			<div
				ref="list"
				v-show="showList"
				:class="[`${prefix}-select__dropdown`, { 'no-option-children': noChildren }]"
				transition="slide"
			>
				<template v-if="filterable">
					<input
						ref="searchInput"
						v-model="searchValue"
						:placeholder="'搜索'"
						@input="changeSearchValue"
						@keydown="keydown"
						:class="`${prefix}-select__selected-input`"
					/>
				</template>
				<ul @click="selectOption"
					:class="`${prefix}-select__dropdown-list`">
					<slot/>
					<template v-if="noChildren">
						<fb-select-option
							v-for="(item, index) in currentOptions"
							:label="item.label"
							:value="item.value"
							:key="item.value"
							:data-key="JSON.stringify(item.value)"
							:class="[`${prefix}-select-option`,
							{
	                            [`${prefix}-select-option--selected`]: item.selected,
	                            [`${prefix}-select-option--disabled`]: item.disabled || (disableLimit && !item.selected),
                                [`focus`]: item.isFocus
							}]"
							@mouseenter="handleTitleAttr"
							@mouseover="doFocus(item)"
						>
							<slot name="option" :node="item"></slot>
						</fb-select-option>
						<!--					<li class="${prefix}-select-pager">-->
						<!--						<fb-button-->
						<!--							:disabled="pager.page == 0"-->
						<!--							@click.stop="prev"-->
						<!--							icon="left"-->
						<!--						/>-->
						<!--						<fb-button-->
						<!--							:disabled="-->
						<!--                                pager.page ==-->
						<!--                                Math.ceil(-->
						<!--                                    searchOptions.length / pager.pageSize-->
						<!--                                ) - -->
						<!--                                    1-->
						<!--                            "-->
						<!--							@click.stop="next"-->
						<!--							icon="right"-->
						<!--						/>-->
						<!--					</li>-->
					</template>
				</ul>

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
		</transition>
	</div>
</template>

<script>

	import clickoutside from '../../../directives/clickoutside'
	import {closest} from '../../../utils/componentUtils'
	import {assign, debounce, isFunction, isArray, each, indexOf, filter, every} from 'lodash-es'
	import {getViewport} from '../../../utils/utils'
	import FbSelectOption from './FbSelectOption'
	import keyCode from '../../../utils/keyCode'
	import FbIcon from '../../icon/src/FbIcon'
	import axios from 'axios'
	import FbEmpty from '../../empty/src/FbEmpty'
	import FbLoading from '../../loading/src/FbLoading'
	import {prefix} from '../../../../src/config'

	/**
	 * FbSuggestSelect
	 * (c) 2020 lincong1987
	 */
	export default {
		name: 'FbSuggestSelect',
		components: {
			FbIcon,
			FbSelectOption,
			FbEmpty,
			FbLoading,

		},
		directives: {
			clickoutside,
		},
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
				default() {
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
			// 最多选几个, 0不限制
			multipleLimit: {
				type: Number,
				default: 0,
			},
			// 节点数据
			data: {
				type: Array,
				default() {
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
				default(data) {
					return (data)
				},
			},
			// 取值配对
			reader: {
				type: Object,
				default() {
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

		},

		data() {
			//const options = this.applyData()

			return {
				prefix,
				focusOption: null,
				currentValue: this.value,
				searchValue: '',
				showClear: false,
				showList: false,
				myData: this.data,
				options: [],
				noChildren: 0,
				pager: {
					pageSize: 100,
					page: 0,
				},
				searching: false,

				showLoading: false,

				showEmpty: false,

				myPager: {},

				myQueryParam: this.param,
			}
		},
		computed: {
			// tab 键顺序
			getTabindex() {
				if (this.disabled || this.readonly) {
					return -1
				}
				return 0
			},

			getUISelectClass() {
				if (this.selectedOption && this.selectedOption.currentLabel) {
					return `${this.prefix}-select__selected-value`
				}
				return `${this.prefix}-select__placeholder`
			},
			localPlaceholder() {
				return this.placeholder
			},
			selectedOption() {
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
			disableLimit() {
				const {currentValue, multiple, multipleLimit} = this
				let disableLimit = false
				if (multiple) {
					disableLimit = (currentValue || []).length >= multipleLimit && multipleLimit > 0
				}
				return disableLimit
			},
			searchOptions() {
				const searchOptions = []
				each(this.options, (option) => {
					if (this.searchValue && indexOf(option.label, this.searchValue) != -1) {
						searchOptions.push(option)
					} else if (!this.searchValue) {
						searchOptions.push(option)
					}
				})
				return searchOptions
			},
			currentOptions() {
				// const start = this.pager.pageSize * this.pager.page
				// const arr = this.searchOptions.slice(start, start + this.pager.pageSize)
				// return arr

				return this.searchOptions
			},
		},
		watch: {
			value(value) {
				if (value === this.currentValue) {
					return
				}
				this.currentValue = value

				if (this.multiple && !isArray(this.currentValue)) {
					this.currentValue = value ? [value] : []
				}

				this.changeCurrentValue()
			},
			showList(value) {
				if (this.filterable && value) {
					this.$nextTick(function () {
						this.$refs.searchInput.focus()
					})
				}
			},
			data: {
				handler() {
					this.myData = this.data
					this.options = this.applyData()
					this.noChildren = this.options.length
				},
				deep: true,
			},
		},
		created() {
			this.fbSelect = closest(this, 'FbSelect')
			this.addOptionCache = []


//			const debounce = function (fn) {
//				let waiting;
//				return function () {
//					if (waiting) return;
//					waiting = true;
//					const context = this;
//					const args = arguments;
//					const later = function () {
//						waiting = false;
//						fn.apply(context, args);
//					};
//					this.$nextTick(later);
//				};
//			};

			this.debouncedAppend = debounce(function () {
				//this.options = this.options.concat(this.addOptionCache);
				this.addOptionCache = [];
			})


		},
		beforeDestroy() {
			this.options = []

			this.fbSelect = null
		},
		methods: {
			addTitleAttr(target, label) {
				if (target.clientWidth < target.scrollWidth) {
					target.setAttribute('title', label)
				} else {
					target.setAttribute('title', '')
				}
			},
			handleTitleAttr(e) {
				this.addTitleAttr(e.target, e.target.innerText)
			},
			addOption(option) {
//				this.addOptionCache.push(option)
//				this.debouncedAppend()

				//this.options.concat(option)
			},
			removeOption(option) {
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
			applyData() {
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
			selectOption(e) {

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
			clickOption(option) {
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
				} else {
					this.showList = false
					this.currentValue = option.value
					this.finished()
				}
				this.changeCurrentValue()
			},
			changeCurrentValue() {
				if (this.noChildren) {
					each(this.options, (option) => {
						let selected
						if (this.multiple) {
							selected = indexOf(this.currentValue, option.value) != -1
						} else {
							selected = this.currentValue === option.value
						}
						option.selected = selected
					})
				}
				this.updatePosition()
				this.$emit('input', this.currentValue)
				this.$emit('change', this.currentValue)
				this.$emit('on-change', this.currentValue)
				this.formItem && this.formItem.$emit('on-form-change', [this.currentValue])
			},
			removeSelectedOption(option) {
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
				this.pager.page = 0
			}, 200),
			blur() {
				this.$emit('on-blur')
				if (!this.disabled && !this.readonly && this.clearable) {
					this.showClear = false
				}
			},
			focus() {
				this.$emit('on-focus')

				if (!this.disabled && !this.readonly && this.clearable &&
					(this.currentValue != '' || this.currentValue.length > 0)) {
					this.showClear = true
				}
			},
			toggle() {
				this.$emit('on-click')
				if (this.disabled || this.readonly) return
				this.showList = !this.showList
				this.updatePosition()
			},
			updatePosition() {
				this.$nextTick(function () {
					let elStyle = this.$el.getBoundingClientRect()
					const bottom = getViewport().height - elStyle.bottom

					if (!this.listHeight) {
						this.listHeight = this.$refs.list.clientHeight
					}
					if (bottom - this.listHeight < 0) {
						this.$refs.list.style.top = `-${this.listHeight + 10}px`
					} else {
						this.$refs.list.style.top = `${elStyle.height}px`// '32px'
					}
				})
			},
			clear() {
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
			close() {
				if (this.showList) {
					this.finished()
				}
				this.showList = false
				this.searchValue = ''
			},
			finished() {
				this.$emit('on-finished', this.currentValue)

				this.fbFormItem && this.fbFormItem.$emit('on-form-blur', this.currentValue)
			},
			prev() {
				if (this.pager.page > 0) {
					this.pager.page -= 1
					this.$nextTick(function () {
						this.$refs.list.scrollTop = 0
					})
				}
			},
			next() {
				if (this.pager.page < Math.ceil(this.searchOptions.length / this.pager.pageSize)) {
					this.pager.page += 1
					this.$nextTick(function () {
						this.$refs.list.scrollTop = 0
					})
				}
			},
			doFocus(option) {
				if (this.focusOption) {
					this.focusOption.isFocus = false
				}
				option.isFocus = true
				this.focusOption = option
			},
			keydown(e) {
				if (this.disabled || this.readonly) return
				if (!this.showList) {
					if (e.keyCode == keyCode.SPACE || e.keyCode == keyCode.ENTER || e.keyCode == keyCode.MAC_ENTER) {
						e.preventDefault()
						this.showList = true
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
			mouseChooseOption(direction) {
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

			loadData() {

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

			fetchData() {

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

			updateDataAfterQuery(json) {

				this.loading(false)

				this.updateData(json && json.data || [])
				// this.myData = json.data || []
				this.options = this.applyData()
				this.noChildren = this.options.length

				// this.myPager.page = json.data.page
				// this.myPager.total = json.data.total
				// this.myPager.pageSize = json.data.pageSize
				// this.myPager.total = json.data.size

				this.showEmpty = this.myData.length === 0

			},

			updateData(data) {
				this.myData = data
			},
			loading(status) {
				this.showLoading = status
			},

		},

		mounted() {

			if (this.service && this.autoLoad) {
				this.fetchData()
			} else if (this.url && this.autoLoad) {
				this.loadData({})
			} else {

				this.options = this.applyData()
				this.noChildren = this.options.length
			}
		},
	}
</script>

<style scoped>

</style>
