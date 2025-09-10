<template>
	<div :class="`${prefix}-tabs`">
		<div :class="getHeaderClass">
			<div
				ref="container"
				:class="[
					`${prefix}-tabs-header-container`,
					scrollable ? `${prefix}-tabs-header-scroll` : '',
				]"
			>
				<template v-if="scrollable">
					<fb-icon
						:class="{ disabled: scrollIndex === 0 }"
						@on-click="scrollPrev"
						class="button-left"
						name="left"
						size="m"
					/>
					<fb-icon
						:class="{ disabled: scrollIndex === tabs.length - 1 }"
						@on-click="scrollNext"
						class="button-right"
						name="right"
						size="m"
					/>
				</template>
				<div ref="nav" :style="navStyle" :class="`${prefix}-tabs-items`">
					<div v-if="tabs"
						 v-show="!tab.hidden"
						 v-for="(tab, index) in tabs"
						 :ref="tab.tabName"
						 :key="tab.tabName || index"
						 :class="getTabHeaderClass(tab, index)"
						 @click="choose(tab, index)"
						 @dblclick="(e) => dblclick(tab, index, e)"
					>
						<div :class="`${prefix}-tabs-header-label`">
							<fb-icon v-if="tab.icon" :name="tab.icon"/>
							<template>
								<!-- fbTab $slots.label -->
								<template v-if="tab.$slots.label">
									<render-dom :content="tab.$slots.label"></render-dom>
								</template>
								<!-- fbTab props.label -->
								<template v-else-if="isString(tab.label)">
									{{ tab.label }}
								</template>
							</template>
<!--							<template v-if="isObject(tab.label)">-->
<!--								<fb-component :component="tab.label"/>-->
<!--							</template>-->
						</div>
						<fb-icon
							v-if="closable && !tab.noClose"
							@on-click.stop="(e)=>handleTabRemove(tab, e)"
							name="close"
							size="m"
							class="button-close"
						/>
					</div>
				</div>
			</div>
			<div :class="`${prefix}-tabs-buttons`">
				<slot name="actions"/>
			</div>
		</div>
		<div :class="`${prefix}-tabs-body`" :style="getBodyStyle">
			<slot></slot>
			<div v-if="myData.length > 0">
				<fb-tab
					v-for="(tab, i) in myData"
					:key="tab.value + '-' + i"
					:label="tab.label"
					:name="tab.value"
					:hidden="tab.hidden">
					<slot name="tab" :tab="tab"></slot>
				</fb-tab>
			</div>

		</div>
	</div>
</template>

<script>
/**
 * ${prefix} - tabs
 * (c) 2020 lincong1987
 */

import {
	isString,
	isObject, isFunction,
} from 'lodash-es'
import FbIcon from '../../icon/src/FbIcon'
import FbComponent from '../../component/src/FbComponent'
import { prefix } from '../../../../src/config'
import FbTab from '../../tab/src/FbTab'

export default {
	name: 'FbTabs',
	components: {
		FbTab,
		FbComponent,
		FbIcon,
		'render-dom': {
			props: {
				content: null
			},
			render(h, context) {
				return h('span', this.content ? this.content : '')
			}
		}
	},
	props: {
		value: {
			type: [Number, String],
			default: 1,
		},
		// 类型 normal card
		type: {
			type: String,
			default: undefined,
		},
		closable: {
			type: Boolean,
			default: false,
		},
		// 动画
		transition: {
			type: String,
			default: null,
		},

		// 关闭前的回调
		beforeRemove: {
			type: Function,
			default: undefined,
		},

		// 切换前的回调
		beforeChange: {
			type: Function,
			default: undefined,
		},

		data: {
			type: Array,
			default: () => [],
		},

		bodyStyle: {
			type: Object,
			default: undefined,
		}

	},

	data () {
		return {
			prefix,
			uid: 1,
			activeKey: this.value,
			cacheTabs: [],
			scrollable: false,
			navStyle: {
				transform: '',
			},
			scrollIndex: 0,
			tabs: [],
			myData: this.data,
			myBeforeRemove: this.beforeRemove,
			myBeforeChange: this.beforeChange,
		}
	},

	computed: {
		getBodyStyle () {
			let style = []

			style.push({'height': 'calc(100% - 46px)'})
			style.push({'overflowY': 'auto'})

			if (this.bodyStyle) {
				style.push(this.bodyStyle)
			}

			return style

		},
		getHeaderClass () {
			let arr = [`${prefix}-tabs-header`]

			if (this.type == 'card') {
				arr.push(`${prefix}-tabs-header-card`)
			}

			return arr
		},
	},

	watch: {
		value () {
			this.activeKey = this.value
		},

		data (value) {
			this.myData = value
		},

		tabs (value, old) {
			if (this.tabs.length > 0) {
				this.$nextTick(() => {
					this.isScrollable()
				})

				this.$emit('on-tabs-data-change', value)
				// 向前兼容，后期会摒弃，这个命名不规范问题
				this.$emit('on-tab-change', value)
			}
		},
		scrollIndex () {
			let offset = 0
			for (let i = 0; i < this.scrollIndex; i++) {
				const tabDom = this.$refs[this.tabs[i].tabName]
				offset += tabDom ? tabDom[0].offsetWidth : 0
			}
			this.setOffset(offset)
		},
	},
	updated () {
		this.calcTabInstances()
	},

	methods: {
		renderVnode(slot) {
			return this.$createElement('div', slot)
		},
		isString,
		isObject,
		isScrollable () {
			const navWidth = this.$refs.nav ? this.$refs.nav.offsetWidth : 0
			const containerWidth = this.$refs.container
				? this.$refs.container.offsetWidth
				: 0
			this.scrollable = navWidth > containerWidth
			if (!this.scrollable) {
				this.scrollIndex = 0
			}
		},
		getTabHeaderClass (tab, index) {
			const arr = [`${prefix}-tabs-header-item`]

			if (tab.show) {
				arr.push(`${prefix}-tabs-header--current`)
			}
			if (tab.disabled) {
				arr.push(`${prefix}-tabs-header--disabled`)
			}

			if (this.closable && !tab.noClose) {
				arr.push(`${prefix}-tabs-header--closable`)
			}

			return arr
		},
		addTab (tab) {
			tab.tabName = tab.name || this.uid

			this.tabs.push(tab)
			this.cacheTabs.push(tab)
			this.uid++

			if (this.closable) {
				// 接完后跳到最后一个
				this.scrollNext()

				this.choose(tab, this.tabs.length)
			}
		},

		// removeTab (tab, trigger, e) { // trigger是点击触发 / 自我毁灭
		// 	// 改为同步代码， async 会先执行 await 以上的代码 进行缓存 产生切割数组bug
		// 	// 下沉 index 以免再次 async 下出错
		// 	let allowRemove = true
		// 	if (this.beforeRemove && isFunction(this.beforeRemove)) {
		// 		try {
		// 			allowRemove = this.beforeRemove(tab)
		// 			// 默认输出为true
		// 			if (allowRemove == undefined) {
		// 				allowRemove = true
		// 			}
		// 		} catch (e) {
		// 			allowRemove = false
		// 		}
		// 	}
		// 	if (!allowRemove && trigger) {
		// 		return
		// 	}
		//
		// 	const index = this.tabs.indexOf(tab)
		// 	if (index != -1) {
		// 		this.tabs.splice(index, 1)
		// 		// 删除自己
		// 		if (tab.$el) {
		// 			if (tab.$el.parentNode) {
		// 				tab.$el.parentNode.removeChild(tab.$el)
		// 			}
		// 		}
		//
		// 		if (trigger) {
		// 			this.$emit('on-tab-remove', tab, index)
		// 		}
		// 	}
		//
		// 	if (tab.show && this.tabs.length > 0) {
		// 		const newIndex = index - 1 < 0 ? index : index - 1
		// 		// this.activeKey = this.tabs[newIndex].tabName
		// 		// console.log(this.activeKey)
		//
		// 		this.$nextTick(() => {
		// 			this.choose(this.tabs[newIndex], newIndex)
		// 		})
		// 	}
		//
		// },

		// 同步计算 tab组件虚拟 dom
		// 保持 this.tabs = vnode[panes]
		calcTabInstances (isForceUpdate = false) {
			if (this.$slots.default) {
				const paneSlots = this.$slots.default.filter(vnode => vnode.tag &&
					vnode.componentOptions && vnode.componentOptions.Ctor.options.name === 'FbTab')
				// update indeed
				const panes = paneSlots.map(({componentInstance}) => componentInstance)
				const panesChanged = !(panes.length === this.tabs.length &&
					panes.every((pane, index) => pane === this.tabs[index]))
				if (isForceUpdate || panesChanged) {
					// console.log(panes)
					this.tabs = panes
				}
			} else if (this.tabs.length !== 0 && !this.data.length) {
				this.tabs = []
			}
		},
		handleTabRemove (tab, e) {
			if (tab.disabled) return
			if (tab.noClose) return
			let index = this.tabs.indexOf(tab)

			if (index !== -1) {
				this.$emit('on-tab-remove', tab, index)
			} else {
				this.$emit('on-tab-remove', tab)
			}
		},
		async choose (tab, index) {
			// 点击前允许切换
			let allow = true
			if (this.myBeforeChange && isFunction(this.myBeforeChange)) {
				try {
					allow = await this.myBeforeChange(tab, index)
					// 默认输出为true
					if (allow == undefined) {
						allow = true
					}
				} catch (e) {
					allow = false
				}
			}

			if (!allow) {
				return
			}

			if (!tab || index == -1) {
				return
			}

			if (this.activeKey == tab.tabName) {
				return
			}

			if (tab.disabled) {
				return
			}
			this.activeKey = tab.tabName
			this.$emit('input', tab.tabName)
			this.$emit('on-click', tab, index)
			this.$emit('on-tab-choose', tab, index)
		},
		dblclick (tab, index, e) {
			this.$emit('on-tab-dblclick', tab, index, e)
		},
		getCurrentScrollOffset () {
			return this.navStyle.transform
				? Number(
					this.navStyle.transform.match(
						/translateX\(-(\d+(\.\d+)*)px\)/,
					)[1],
				)
				: 0
		},
		scrollNext () {
			if (this.scrollIndex < this.tabs.length - 1) {
				this.scrollIndex += 1
			}
		},
		scrollPrev () {
			if (this.scrollIndex > 0) {
				this.scrollIndex -= 1
			}
		},
		setOffset (newOffset) {
			this.navStyle.transform = `translateX(-${newOffset}px)`
		},
		reset () {
			// 保存开始的顺序，提供接口恢复原状
			this.tabs = this.cacheTabs
			this.cacheTabs = []
			this.tabs.forEach((item) => {
				this.cacheTabs.push(item)
			})
		},
	},

}
</script>

<style scoped>

</style>
