<template>
	<div
		ref="reference"
		:class="[
		  `${prefix}-cascader`,
		  realSize && `${prefix}-cascader--${realSize}`,
		  { 'is-disabled': isDisabled }
		]"
		v-clickoutside="() => toggleDropDownVisible(false)"
		@mouseenter="inputHover = true"
		@mouseleave="inputHover = false"
		@click="() => toggleDropDownVisible(readonly ? undefined : true)"
		@keydown="handleKeyDown">


		<div :class="[`${prefix}-cascader__selection`, {'is_focus': dropDownVisible}]">
			<template v-if="multiple">
				<div :class="[
					!presentText ? `${prefix}-cascader__placeholder` : `${prefix}-cascader__multiple--selected`
				]">
					<span v-if="!presentTags.length">{{ placeholder }}</span>
					<template v-else>
						<fb-tag
							v-for="tag in presentTags"
							:key="tag.key"
							:label="tag.text"
							type="info"
							:size="tagSize"
							:closable="tag.closable"
							@on-close.stop="deleteTag(tag)">
						</fb-tag>
					</template>
				</div>
			</template>
			<template v-else>
				<span :class="[
					!presentText ? `${prefix}-cascader__placeholder` : `${prefix}-cascader__selected-value`
				]">
					{{ !presentText ? placeholder : presentText }}
				</span>
			</template>

			<div :class="[`${prefix}-cascader__selection-icons`]">
				<fb-icon
					name="close-circle-fill"
					v-if="clearBtnVisible"
					key="clear"
					color="#0284FE"
					:class="`${prefix}-input__icon ${prefix}-cascader__selection-icons__clear ${prefix}-icon-circle-close`"
					@on-click.stop="handleClear"></fb-icon>
				<fb-icon
					v-else
					name="down"
					key="arrow-down"
					:class="[
					`${prefix}-input__icon`,
					`${prefix}-icon-arrow-down`,
					dropDownVisible && 'is-reverse'
				  ]"></fb-icon>
			</div>
		</div>

<!--			<fb-input-->
<!--				ref="input"-->
<!--				v-model="multiple ? presentText : inputValue"-->
<!--				:size="realSize"-->
<!--				readonly-->
<!--				:placeholder="placeholder"-->
<!--				:disabled="isDisabled"-->
<!--				:class="{ 'is-focus': dropDownVisible }"-->
<!--				>-->
<!--				<template slot="suffix">-->
<!--					<fb-icon-->
<!--						name="close-circle-fill"-->
<!--						v-if="clearBtnVisible"-->
<!--						key="clear"-->
<!--						color="#0284FE"-->
<!--						:class="`${prefix}-input__icon ${prefix}-icon-circle-close`"-->
<!--						@on-click.stop="handleClear"></fb-icon>-->
<!--					<fb-icon-->
<!--						v-else-->
<!--						name="down"-->
<!--						key="arrow-down"-->
<!--						:class="[-->
<!--					`${prefix}-input__icon`,-->
<!--					`${prefix}-icon-arrow-down`,-->
<!--					dropDownVisible && 'is-reverse'-->
<!--				  ]"-->
<!--						@on-click.stop="toggleDropDownVisible()"></fb-icon>-->
<!--				</template>-->
<!--			</fb-input>-->



<!--		<div v-if="multiple" :class="`${prefix}-cascader__tags`">-->
<!--			<fb-tag-->
<!--				v-for="tag in presentTags"-->
<!--				:key="tag.key"-->
<!--				:label="tag.text"-->
<!--				type="info"-->
<!--				:size="tagSize"-->
<!--				:hit="tag.hitState"-->
<!--				:closable="tag.closable"-->
<!--				disable-transitions-->
<!--				@on-close.stop="deleteTag(tag)">-->
<!--			</fb-tag>-->
<!--			<input-->
<!--				v-if="filterable && !isDisabled"-->
<!--				readonly-->
<!--				v-model.trim="inputValue"-->
<!--				type="text"-->
<!--				:class="`${prefix}-cascader__search-input`"-->
<!--				:placeholder="presentTags.length ? '' : placeholder"-->
<!--				@input="e => handleInput(inputValue, e)"-->
<!--				@click.stop="toggleDropDownVisible(true)"-->
<!--				@keydown.delete="handleDelete">-->
<!--		</div>-->

		<transition name="zoom-in-top" @after-leave="handleDropdownLeave">
			<div
				v-show="dropDownVisible"
				ref="popper"
				:class="[`${prefix}-popper-no-arrow`, `${prefix}-cascader__dropdown`, popperClass]">
				<div>
					<fb-input v-show="filterable"
							  :style="{width: searchWidth}"
							  v-model="searchValue"
							  prependIcon="search"
							  placeholder="请输入"
							  @on-enter="handleSearchEnter"
							  @focus="handleFocus"
							  @blur="handleBlur"
							  @input="handleInput"></fb-input>
					<fb-cascader-panel
						ref="panel"
						v-show="!filtering"
						v-model="checkedValue"
						:data="data"
						:props="config"
						:border="false"
						:render-label="$scopedSlots.default"
						@expand-change="handleExpandChange"
						@close="toggleDropDownVisible(false)"></fb-cascader-panel>
					<ul v-if="filterable"
						v-show="filtering"
						:class="`${prefix}-cascader__suggestion-panel`"
					>
						<template v-if="suggestions.length">
							<li
								v-for="(item, index) in suggestions"
								:key="item.uid"
								:class="[
								`${prefix}-cascader__suggestion-item`,
								item.checked && 'is-checked'
							  ]"
								:tabindex="-1"
								@click="handleSuggestionClick(index)">
								<span>{{ item.text }}</span>
								<i v-if="item.checked" :class="`${prefix}-icon-check`"></i>
							</li>
						</template>
						<slot v-else name="empty">
							<li :class="`${prefix}-cascader__empty-text`">cascader.noMatch</li>
						</slot>
					</ul>
				</div>
			</div>
		</transition>
	</div>
</template>

<script>

	import { prefix } from '../../../../src/config'
	import PopperMixin from "../../../utils/popper-mixin";
	import Clickoutside from '../../../utils/clickoutside';
	import Emitter from '../../../mixins/emitter';
	// import { addResizeListener, removeResizeListener } from 'element-ui/src/utils/resize-event';
	import FbInput from '../../input/src/FbInput';
	import FbTag from '../../tag/src/FbTag';
	import FbCascaderPanel from '../../cascader-panel/src/FbCascaderPanel';
	import AriaUtils from '../../../utils/aria-utils';
	import { isEqual, isEmpty, isDef, kebabCase } from '../../../utils/utils';

	import { debounce, isUndefined, isFunction } from 'lodash-es';
	import FbIcon from "../../icon/src/FbIcon";

	const { keys: KeyCode } = AriaUtils;
	const MigratingProps = {
		expandTrigger: {
			newProp: 'expandTrigger',
			type: String
		},
		changeOnSelect: {
			newProp: 'checkStrictly',
			type: Boolean
		},
		hoverThreshold: {
			newProp: 'hoverThreshold',
			type: Number
		}
	};

	const InputSizeMap = {
		medium: 36,
		small: 32,
		mini: 28
	};

	/**
	 * FbCascader
	 * (c) 2020 lincong1987
	 */
	export default {
		name: 'FbCascader',
		directives: { Clickoutside },

		mixins: [PopperMixin, Emitter,],

		components: {
			FbIcon,
			FbInput,
			FbTag,
			FbCascaderPanel
		},

		props: {
			value: {},
			data: Array,
			props: Object,
			size: String,
			placeholder: {
				type: String,
				default: '请选择'
			},
			disabled: Boolean,
			clearable: Boolean,
			filterable: Boolean,
			filterMethod: Function,
			separator: {
				type: String,
				default: ' / '
			},
			showAllLevels: {
				type: Boolean,
				default: true
			},
			collapseTags: Boolean,
			debounce: {
				type: Number,
				default: 300
			},
			beforeFilter: {
				type: Function,
				default: () => (() => {})
			},
			popperClass: String
		},

		data() {
			return {
				prefix,
				dropDownVisible: false,
				checkedValue: this.value,
				inputHover: false,
				inputValue: null,
				presentText: null,
				presentTags: [],
				checkedNodes: [],
				filtering: false,
				suggestions: [],
				inputInitialHeight: 0,
				pressDeleteCount: 0,
				searchValue: '',
				searchWidth: '100%'
			};
		},

		computed: {
			realSize() {
				return this.size || (this.$ELEMENT || {}).size;
			},
			tagSize() {
				return ['small', 'mini'].indexOf(this.realSize) > -1
					? 'mini'
					: 'small';
			},
			isDisabled() {
				return this.disabled;
			},
			config() {
				const config = this.props || {};
				const { $attrs } = this;

				Object
					.keys(MigratingProps)
					.forEach(oldProp => {
						const { newProp, type } = MigratingProps[oldProp];
						let oldValue = $attrs[oldProp] || $attrs[kebabCase(oldProp)];
						if (isDef(oldProp) && !isDef(config[newProp])) {
							if (type === Boolean && oldValue === '') {
								oldValue = true;
							}
							config[newProp] = oldValue;
						}
					});

				return config;
			},
			multiple() {
				return this.config.multiple;
			},
			leafOnly() {
				return !this.config.checkStrictly;
			},
			readonly() {
				return !this.filterable || this.multiple;
			},
			clearBtnVisible() {
				if (!this.clearable || this.isDisabled || this.filtering || !this.inputHover) {
					return false;
				}

				return this.multiple
					? !!this.checkedNodes.filter(node => !node.isDisabled).length
					: !!this.presentText;
			},
			panel() {
				return this.$refs.panel;
			}
		},

		watch: {
			disabled() {
				this.computePresentContent();
			},
			value(val) {
				if (!isEqual(val, this.checkedValue)) {
					this.checkedValue = val;
					this.computePresentContent();
				}
			},
			checkedValue(val) {
				const { value, dropDownVisible, props } = this;
				const { checkStrictly, multiple, changeOnSelect } = this.config;

				if (!isEqual(val, value) || isUndefined(value)) {
					this.computePresentContent();
					// hide dropdown when single mode
					if (!multiple && !checkStrictly && !changeOnSelect && dropDownVisible) {
						this.toggleDropDownVisible(false);
					}
					// 选中路径节点
					let	checkNodes = multiple ? this.getCheckedNodes(false) : this.getCheckedNodes(false)[0].pathNodes


					this.$emit('input', val);
					this.$emit('change', val, checkNodes);
					this.$emit('on-change', val, checkNodes);
				}
			},
			data: {
				handler: function() {
					this.$nextTick(this.computePresentContent);
				},
				deep: true
			},
			presentText(val) {
				this.inputValue = val;
			},
			presentTags(val, oldVal) {
				if (this.multiple && (val.length || oldVal.length)) {
					this.$nextTick(this.updateStyle);
				}
			},
			filtering(val) {
				this.$nextTick(this.updatePopper);
			}
		},

		mounted() {
			const { input } = this.$refs;
			if (input && input.$el) {
				this.inputInitialHeight = input.$el.offsetHeight || InputSizeMap[this.realSize] || 40;
			}

			if (!this.isEmptyValue(this.value)) {
				this.computePresentContent();
			}

			this.filterHandler = debounce(() => {
				const { searchValue } = this;

				if (!searchValue) {
					this.filtering = false;
					return;
				}

				const before = this.beforeFilter(searchValue);
				if (before && before.then) {
					before.then(this.getSuggestions);
				} else if (before !== false) {
					this.getSuggestions();
				} else {
					this.filtering = false;
				}
			}, this.debounce);

			// addResizeListener(this.$el, this.updateStyle);
		},

		beforeDestroy() {
			// removeResizeListener(this.$el, this.updateStyle);
		},

		methods: {
			getMigratingConfig() {
				return {
					props: {
						'expand-trigger': 'expand-trigger is removed, use `props.expandTrigger` instead.',
						'change-on-select': 'change-on-select is removed, use `props.checkStrictly` instead.',
						'hover-threshold': 'hover-threshold is removed, use `props.hoverThreshold` instead'
					},
					events: {
						'active-item-change': 'active-item-change is renamed to expand-change'
					}
				};
			},
			toggleDropDownVisible(visible) {
				if (this.isDisabled) return;

				const { dropDownVisible, filterable } = this;
				const { input, panel } = this.$refs;
				visible = isDef(visible) ? visible : !dropDownVisible;
				if (visible !== dropDownVisible) {
					this.dropDownVisible = visible;
					if (visible) {
						this.$nextTick(() => {
							this.updatePopper();

							// 搜索框样式容错
							if (filterable && panel && panel.menus.length === 1) {
								this.searchWidth = panel.$children[0].$el.clientWidth + 'px'
							}
						});
					}
					// input.$refs.input.setAttribute('aria-expanded', visible);
					this.$emit('visible-change', visible);
					this.$emit('on-visible-change', visible);
				}
			},
			handleDropdownLeave() {
				this.filtering = false;
				this.inputValue = this.presentText;
				this.doDestroy();
			},
			handleKeyDown(event) {
				switch (event.keyCode) {
					case KeyCode.enter:
						this.toggleDropDownVisible();
						break;
					case KeyCode.down:
						this.toggleDropDownVisible(true);
						this.focusFirstNode();
						event.preventDefault();
						break;
					case KeyCode.esc:
					case KeyCode.tab:
						this.toggleDropDownVisible(false);
						break;
				}
			},
			handleFocus(e) {
				this.$emit('focus', e);
				this.$emit('on-focus', e);
			},
			handleBlur(e) {
				this.$emit('blur', e);
				this.$emit('on-blur', e);
			},
			handleInput(val, event) {
				// !this.dropDownVisible && this.toggleDropDownVisible(true);
				this.toggleDropDownVisible(true)

				if (event && event.isComposing) return;
				if (val) {
					this.filterHandler();
				} else {
					this.filtering = false;
				}
			},
			handleSearchEnter() {
				if (this.searchValue) {
					this.filterHandler();
				} else {
					this.filtering = false;
				}
			},
			handleClear() {
				this.presentText = '';
				this.panel.clearCheckedNodes();
			},
			handleExpandChange(value) {
				this.$nextTick(this.updatePopper.bind(this));
				if (this.filterable) {
					this.searchWidth = '100%'
				}
				this.$emit('expand-change', value);
				this.$emit('on-expand-change', value);
				this.$emit('active-item-change', value); // Deprecated
			},
			focusFirstNode() {
				this.$nextTick(() => {
					const { filtering } = this;
					const { popper, suggestionPanel } = this.$refs;
					let firstNode = null;

					if (filtering && suggestionPanel) {
						firstNode = suggestionPanel.$el.querySelector(`.${prefix}-cascader__suggestion-item`);
					} else {
						const firstMenu = popper.querySelector(`.${prefix}-cascader-menu`);
						firstNode = firstMenu.querySelector(`.${prefix}-cascader-node[tabindex="-1"]`);
					}

					if (firstNode) {
						firstNode.focus();
						!filtering && firstNode.click();
					}
				});
			},
			computePresentContent() {
				// nextTick is required, because checked nodes may not change right now
				this.$nextTick(() => {
					if (this.config.multiple) {
						this.computePresentTags();
						this.presentText = this.presentTags.length ? ' ' : null;
					} else {
						this.computePresentText();
					}
				});
			},
			isEmptyValue(val) {
				const { multiple } = this;
				const { emitPath } = this.panel.config;
				if (multiple || emitPath) {
					return isEmpty(val);
				}
				return false;
			},
			computePresentText() {
				const { checkedValue, config } = this;
				if (!this.isEmptyValue(checkedValue)) {
					const node = this.panel.getNodeByValue(checkedValue);
					if (node && (config.changeOnSelect || config.checkStrictly || node.isLeaf)) {
						this.presentText = node.getText(this.showAllLevels, this.separator);
						return;
					}
				}
				this.presentText = null;
			},
			computePresentTags() {
				const { isDisabled, leafOnly, showAllLevels, separator, collapseTags } = this;
				const checkedNodes = this.getCheckedNodes(leafOnly);
				const tags = [];

				const genTag = node => ({
					node,
					key: node.uid,
					text: node.getText(showAllLevels, separator),
					hitState: false,
					closable: !isDisabled && !node.isDisabled
				});

				if (checkedNodes.length) {
					const [first, ...rest] = checkedNodes;
					const restCount = rest.length;
					tags.push(genTag(first));

					if (restCount) {
						if (collapseTags) {
							tags.push({
								key: -1,
								text: `+ ${restCount}`,
								closable: false
							});
						} else {
							rest.forEach(node => tags.push(genTag(node)));
						}
					}
				}

				this.checkedNodes = checkedNodes;
				console.log(tags, 'tags')
				this.presentTags = tags;
			},
			getSuggestions() {
				let { filterMethod } = this;

				if (!isFunction(filterMethod)) {
					filterMethod = (node, keyword) => node.text.includes(keyword);
				}

				const suggestions = this.panel.getFlattedNodes(this.leafOnly)
					.filter(node => {
						if (node.isDisabled) return false;
						node.text = node.getText(this.showAllLevels, this.separator) || '';
						return filterMethod(node, this.searchValue);
					});

				if (this.multiple) {
					this.presentTags.forEach(tag => {
						tag.hitState = false;
					});
				} else {
					suggestions.forEach(node => {
						node.checked = isEqual(this.checkedValue, node.getValueByOption());
					});
				}

				this.filtering = true;
				this.suggestions = suggestions;
				this.$nextTick(this.updatePopper);
			},
			handleSuggestionKeyDown(event) {
				const { keyCode, target } = event;
				switch (keyCode) {
					case KeyCode.enter:
						target.click();
						break;
					case KeyCode.up:
						const prev = target.previousElementSibling;
						prev && prev.focus();
						break;
					case KeyCode.down:
						const next = target.nextElementSibling;
						next && next.focus();
						break;
					case KeyCode.esc:
					case KeyCode.tab:
						this.toggleDropDownVisible(false);
						break;
				}
			},
			handleDelete() {
				const { inputValue, pressDeleteCount, presentTags } = this;
				const lastIndex = presentTags.length - 1;
				const lastTag = presentTags[lastIndex];
				this.pressDeleteCount = inputValue ? 0 : pressDeleteCount + 1;

				if (!lastTag) return;

				if (this.pressDeleteCount) {
					if (lastTag.hitState) {
						this.deleteTag(lastTag);
					} else {
						lastTag.hitState = true;
					}
				}
			},
			handleSuggestionClick(index) {
				const { multiple } = this;
				const targetNode = this.suggestions[index];

				if (multiple) {
					const { checked } = targetNode;
					targetNode.doCheck(!checked);
					this.panel.calculateMultiCheckedValue();
				} else {
					this.checkedValue = targetNode.getValueByOption();
					this.toggleDropDownVisible(false);
				}
			},
			deleteTag(tag) {
				const { checkedValue } = this;
				const current = tag.node.getValueByOption();
				const val = checkedValue.find(n => isEqual(n, current));
				this.checkedValue = checkedValue.filter(n => !isEqual(n, current));
				this.$emit('remove-tag', val);
				this.$emit('on-remove-tag', val);
			},
			updateStyle() {
				const { $el, inputInitialHeight } = this;
				if (this.$isServer || !$el) return;
				const { suggestionPanel } = this.$refs;
				const inputInner = $el.querySelector('.jpx-input__el');

				if (!inputInner) return;

				const tags = $el.querySelector(`.${prefix}-cascader__tags`);
				let suggestionPanelEl = null;

				if (suggestionPanel && (suggestionPanelEl = suggestionPanel.$el)) {
					const suggestionList = suggestionPanelEl.querySelector(`.${prefix}-cascader__suggestion-list`);
					suggestionList.style.minWidth = inputInner.offsetWidth + 'px';
				}

				// if (tags) {
				// 	const offsetHeight = Math.round(tags.getBoundingClientRect().height);
				// 	const height = Math.max(offsetHeight + 6, inputInitialHeight) + 'px';
				// 	inputInner.style.height = height;
				// 	if (this.dropDownVisible) {
				// 		this.updatePopper();
				// 	}
				// }
				// if (!this.presentTags.length) { // 容错tag动画
				// 	const height = Math.max(inputInitialHeight) + 'px';
				// 	inputInner.style.height = height;
				// 	if (this.dropDownVisible) {
				// 		this.updatePopper();
				// 	}
				// }
			},

			/**
			 * public methods
			 */
			getCheckedNodes(leafOnly) {
				return this.panel.getCheckedNodes(leafOnly);
			}
		}
	}
</script>

<style scoped>

</style>
