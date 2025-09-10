<template>
	<li :class="[
			`${prefix}-tree-node`,
			 {selected: node.selected, checked: node.checked, disabled: node.disabled}
		]"
		v-show="node.visible"
	>
		<div :class="[
			`${prefix}-tree-node-title`,
			 {selected: node.selected, checked: node.checked, disabled: node.disabled}
		]" @dblclick="handleTitleDbClick(node)">
			<template v-if="showArrow">
				<fb-icon v-if="!dataLoading"
				         :name="showArrow ? getArrowName(node) : ''"
				         @on-click.stop="toggle(node)"
				         :class="`${prefix}-expand-arrow`">
				</fb-icon>
				<i v-if="dataLoading" class="loading">
					<fb-loading size="14px"/>
				</i>
			</template>

			<i v-else :class="`${prefix}-expand-null`"/>

			<div v-if="multiple && radio && !node.noRadio" @click="clickCheckBox" :class="[`${prefix}-radio`, {[`${prefix}-radio--disabled`]: node.disabled}]">
                <span :class="getRadioClass(node)">
                    <span :class="`${prefix}-radio__el__inner`"></span>
                </span>
			</div>

			<div v-else-if="multiple && !node.noCheckbox" @click="clickCheckBox" :class="[`${prefix}-checkbox`, {[`${prefix}-checkbox--disabled`]: node.disabled}]">
                <span :class="getCheckClass(node)" v-if="!node.noCheckbox">
                    <span :class="`${prefix}-checkbox__el__inner`"></span>
                </span>
			</div>

			<fb-icon v-if="rootTree.showIcon"
			         :class="{
							[`${prefix}-tree-node-title-icon`]: true,
							[`${prefix}-tree-node-title-icon-parent`]: showArrow,
							[`${prefix}-tree-node-title-icon-son`]: !showArrow
			         }"
			         :name="node.icon || (showArrow ? getExpandIconName(node) : 'file')">
			</fb-icon>

			<fb-tree-node-text
				:reader="reader"
				:root="rootTree"
				:node="node"
				@on-click="clickNode"
			/>

		</div>

		<transition name="slide-to-down">
			<ul v-if="node.children && node.children.length > 0 && node.expand"

			    :class="getClass"
			>
				<!--				v-show="node.expand"-->
				<fb-tree-node
					v-for="sonNode in node.children"
					:key="sonNode._key"
					:parent-node.sync="node"
					:node.sync="sonNode"
					:multiple="multiple"
					:radio="radio"
					:radio-group="radioGroup"
					:only-leaf="onlyLeaf"
					:leaf-name="leafName"
					:reader="reader"
					:reduce-icon="reduceIcon"
					:expand-icon="expandIcon"
					:twiceClickExpand="twiceClickExpand"
				/>
			</ul>
		</transition>
	</li>
</template>

<script>/**
 * FbTreeNode
 * (c) 2020 lincong1987
 */

import { closest } from '../../../utils/componentUtils'
import FbTreeNodeText from './FbTreeNodeText'
import FbIcon from '../../icon/src/FbIcon'
import FbLoading from '../../loading/src/FbLoading'
import FbCollapseTransition from '../../collapse-transition/src/FbCollapseTransition'
import { prefix } from '../../../../src/config'

export default {
	name: 'FbTreeNode',
	components: {
		FbCollapseTransition,
		FbLoading,
		FbIcon,
		FbTreeNodeText,
	},
	props: {
		multiple: {
			type: Boolean,
			default: false,
		},

		radio: {
			type: [Boolean],
			default: false,
		},

		// radio 的分组范围
		radioGroup: {
			type: String,
			default: 'level',
		},

		parentNode: { // 上级节点
			type: Object,
			default: undefined,
		},
		node: { // 本节点
			type: Object,
			default: undefined,
		},
		reader: {
			type: Object,
			default () {
				return {
					value: 'value',
					label: 'label',
				}
			},
		},
		onlyLeaf: {
			type: Boolean,
			default: false,
		},
		// 叶子结点 字段名称，根据数据字段判断是否可以选中
		leafName: {
			type: String,
			default: '',
		},
		expandIcon: {
			type: String,
			default: 'down-fill',
		},
		reduceIcon: {
			type: String,
			default: 'right-fill',
		},
		// 二次点击时，是否展开子集
		twiceClickExpand: {
			type: Boolean,
			default: false,
		},
	},
	data () {
		return {
			prefix,
			dataLoading: false,
			dataLoaded: typeof this.node.dataLoaded !== 'undefined' ? this.node.dataLoaded : false,
		}
	},
	created () {
		this.rootTree = closest(this, 'FbTree')
	},

	beforeDestroy () {
		this.rootTree = null
	},

	computed: {
		getClass () {
			const arr = [`${prefix}-tree`, `${prefix}-child-tree`]
			if (this.node.onlyDataChild) {
				arr.push(`${prefix}-tree-just-data`)
			}
			return arr
		},
		showArrow () {

			// // console.log()

			if (!this.rootTree.loadData) {
				return this.node.children && this.node.children.length > 0
			}
			if (this.dataLoaded) {
				return this.node.children && this.node.children.length > 0
			}
			return true
		},
	},
	methods: {
		getArrowName (node) {
			let name = ''
			if (!node.expand) {
				name = node.reduceIcon || this.reduceIcon || 'add-square'
			} else {
				name = node.expandIcon || this.expandIcon || 'reduce-square'
			}
			return name
		},
		getExpandIconName (node) {
			let name = ''
			if (!node.expand) {
				name = 'folder-closed'
			} else {
				name = 'folder-expansion'
			}
			return name
		},

		getRadioClass (node) {
			const arr = [`${prefix}-radio__el`]

			if (node.checked) {
				arr.push(`${prefix}-radio__el--checked`)
			}

			if (node.childrenCheckedStatus == 'half') {
				arr.push(`${prefix}-radio__el--indeterminate`)
			}

			return arr
		},

		getCheckClass (node) {
			const arr = [`${prefix}-checkbox__el`]

			if (node.checked) {
				arr.push(`${prefix}-checkbox__el--checked`)
			}

			if (node.childrenCheckedStatus == 'half') {
				arr.push(`${prefix}-checkbox__el--indeterminate`)
			}

//			if (node.childrenCheckedStatus == 'all') {
//				arr.push('fb-checkbox__el--checked')
//			}
//			if (node.childrenCheckedStatus == 'none') {
//				arr.push('fb-checkbox__el--unchecked')
//			}
			return arr
		},
		toggle (node) {
			if (this.rootTree.loadData && !this.dataLoaded) {
				this.dataLoading = true
				this.rootTree.loadData(this.node, this.addData)
			} else {
				node.expand = !node.expand
			}

			this.rootTree && this.rootTree.$emit('on-expand', node.expand, node)

		},
		addData (data) {
			this.rootTree && this.rootTree.$emit('on-load-data', this.node, data)
			this.node.expand = true
			this.dataLoading = false
			this.dataLoaded = true
		},
		clickNode (e) {


			if (this.node.disabled) {
				return
			}

			// 当没有
			if (this.node.noRadio || this.node.noCheckbox) {
				return
			}

			// 不是叶子阻止选中
			if (this.onlyLeaf && this.leafName && !this.node[this.leafName]) {
				return
			}
			/*console.log(this.onlyLeaf, this.node.children.length , this.node.children.length > 0)
			// 如果是 只选叶子结点 状态时，判断当前节点是否为叶子节点*/
			if (this.onlyLeaf && this.node.children && this.node.children.length > 0) {
				return
			}

			if (!this.rootTree.multiple) {
				this.rootTree && this.rootTree.$emit('on-node-click', this.node, e)
			} else {
				this.rootTree && this.rootTree.$emit('on-check-click', this.node, e)
			}
		},
		clickCheckBox (e) {

			if (this.node.disabled) {
				return
			}

			// 不是叶子阻止选中
			if (this.onlyLeaf && this.leafName && !this.node[this.leafName]) {
				return
			}

			if (this.onlyLeaf && this.node.children && this.node.children.length > 0) {
				return
			}

			if (this.rootTree.multiple) {
				this.rootTree && this.rootTree.$emit('on-check-click', this.node, e)
			}
		},
		handleTitleDbClick(node) {
			if (this.twiceClickExpand) {
				this.toggle(node)
			}
		}
	},
}
</script>

<style lang="less" scoped>

</style>
