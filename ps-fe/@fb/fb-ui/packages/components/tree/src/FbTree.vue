<template>
	<ul ref="tree" :class="getClass" :style="getUiStyle">

		<fb-loading v-if="firstLoad" text="加载中..." size="24px"></fb-loading>
		<fb-tree-node
			v-if="!firstLoad"
			v-for="node in root.children"
			:key="node._key"
			:node.sync="node"
			:parent-node.sync="root"
			:multiple="multiple"
			:reader="reader"
			:radio="radio"
			:radio-group="radioGroup"
			:only-leaf="onlyLeaf"
			:leaf-name="leafName"
			:reduce-icon="reduceIcon"
			:expand-icon="expandIcon"
			:twiceClickExpand="twiceClickExpand"
		></fb-tree-node>

	</ul>
</template>
<script>

import FbTreeNode from './FbTreeNode'
import { prefix } from '../../../../src/config'

/**
 * FbTreeNode
 * (c) 2020 lincong1987
 */

let key = 1

import axios from 'axios'
import { assign, isFunction, isArray, map, isUndefined, each } from 'lodash-es'
import FbLoading from '../../loading/src/FbLoading'
import FbCollapseTransition from '../../collapse-transition/src/FbCollapseTransition'
import FbSpin from '../../spin/src/FbSpin'

export default {
	name: 'FbTree',
	components: {
		FbSpin,
		FbCollapseTransition,
		FbLoading,
		FbTreeNode,
	},
	props: {
		reader: {
			type: Object,
			default () {
				return {
					value: 'value',
					label: 'label',
				}
			},
		},

		// 显示图标
		showIcon: {
			type: Boolean,
			default: true,
		},

		// 显示 tooltip
		showTitle: {
			type: Boolean,
			default: true,
		},

		// 显示线
		showLine: {
			type: Boolean,
			default: false,
		},

		inline: {
			type: Boolean,
			default: false,
		},

		// 多选
		multiple: {
			type: Boolean,
			default: false,
		},

		//
		radio: {
			type: [Boolean],
			default: false,
		},

		// radio 的分组范围
		radioGroup: {
			type: String,
			default: 'level',
		},

		// 直接赋值
		data: {
			type: Array,
			default: undefined,
		},
		height: {
			type: [String, Number],
			default: undefined,
		},
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

		loadData: {
			type: Function,
			default: null,
		},

		// 二次点击时，是否继续保持选中状态
		twiceClickSelected: {
			type: Boolean,
			default: true,
		},
		// 二次点击时，是否展开子集
		twiceClickExpand: {
			type: Boolean,
			default: false,
		},

		// 关于 node.childrenCheckedStatus
		//  "none" : 子节点选中数量为0
		//  "half" : 子节点选中数量大于0且小于子节点总数
		//  "all" : 子节点全部选中

		// checkbox 被勾选后的情况
		// "p" 表示操作会影响父级节点；
		// "s" 表示操作会影响子级节点。
		// 请注意大小写，不要改变
		doCheck: {
			type: String,
			default: 'ps',
		},

		// checkbox 取消勾选后的情况
		// "p" 表示操作会影响父级节点；
		// "s" 表示操作会影响子级节点。
		// 请注意大小写，不要改变
		doUnCheck: {
			type: String,
			default: 'ps',
		},

		// 只有叶子结点的选择模式
		onlyLeaf: {
			type: Boolean,
			default: false,
		},
		// 叶子结点 字段名称，根据数据字段判断是否可以选中
		leafName: {
			type: String,
			default: '',
		},

		// 去除半选的选择模式
		noHalf: {
			type: Boolean,
			default: false,
		},

		// 用于传递作用域插槽
		scopedSlots: undefined,
		// 过滤回调函数
		filterNodeMethod: Function,
		expandIcon: {
			type: String,
		},
		reduceIcon: {
			type: String,
		},
	},
	data () {
		return {
			prefix,
			key: 1,
			myService: this.service,
			firstLoad: false,
			myData: [],
			selectedNode: null,
			root: {
				children: [],
			},
			myQueryParam: this.param,
			myPager: {},

		}
	},
	computed: {
		getUiStyle () {
			let style = []

			if (this.height) {
				style.push({'height': parseInt(this.height) + 'px'})
				style.push({'overflowY': 'auto'})
			}
			return style
		},
		getClass () {
			const arr = [`${prefix}-tree`]
			if (this.inline) {
				arr.push(`${prefix}-tree--inline`)
			}

			if (this.showLine) {
				arr.push(`${prefix}-tree--show-line`)
			}
			return arr
		},
	},
	watch: {
		data: {
			handler () {
				this.init()
			},
			deep: true,
		},

//			data(){
//				this.init()
//			}
	},

	created () {
	},
	mounted () {

		this.init()

		this.$on('on-node-click', (node, e) => {
			this.onNodeClick(node, e)

			this.$emit('on-select-change', this.selectedNode, e)
		})

		this.$on('on-check-click', (node, e) => {
			this.onCheckClick(node, !node.checked, e)

			this.$emit('on-check-change', this.getCheckedNodes(), node, e)
		})

		this.$on('on-load-data', (parent, data) => {

			this.setLoadData(parent, data)
			this.$emit('on-data-change', parent, data)
//			this.$emit('on-load-data', parent, data)
		})
	},

	beforeDestroy () {
		this.$off('on-node-click')
		this.$off('on-check-click')
		this.$off('on-load-data')
	},
	methods: {
		// 过滤函数和 filterNodeMethod 属性绑定使用
		filter (value) {
			const filterNodeMethod = this.filterNodeMethod
			if (!filterNodeMethod) {
				throw new Error('[Tree] filterNodeMethod is required when filter')
			}
			const traverse = (node) => {
				const childNodes = node.root ? node.root.children : node.children

				childNodes.forEach((child) => {
					child.visible = filterNodeMethod.call(child, value, child)

					traverse(child)
				})

				if (!node.visible && childNodes.length) {
					let allHidden = true
					allHidden = !childNodes.some(child => child.visible)

					if (node.root) {
						node.root.visible = allHidden === false
					} else {
						node.visible = allHidden === false
					}
				}
				if (!value) return

				if (node.visible) {
					node.expand = true
				}
			}

			traverse(this)
		},

		onNodeClick (node, e) {

			if (this.selectedNode) {
				this.selectedNode.selected = false
			}

			if (!this.twiceClickSelected) {
				if (node !== this.selectedNode) {
					node.selected = true
					this.selectedNode = node
				} else {
					this.selectedNode = null
				}
			} else {
				node.selected = true
				this.selectedNode = node
			}

		},

		onCheckClick (node, checked) {

			node.checked = checked

			if (this.doCheck === '' || this.doUnCheck === '') {
				node.childrenCheckedStatus = checked ? 'all' : 'none'
			}

//				if (node.childrenCheckedStatus == 'none' || node.childrenCheckedStatus == 'half') {
//					this.setChild(node, typeof checked != 'undefined' ? checked : true)
//				} else if (node.childrenCheckedStatus == 'all') {
			this.setChild(node, checked)//typeof checked != 'undefined' ? checked : false)
//				}
//
			this.setParent(node, checked)
		},

		onCheck (node, checked) {

			node.checked = checked

			if (this.doCheck === '' || this.doUnCheck === '') {
				node.childrenCheckedStatus = checked ? 'all' : 'none'
			}

//				if (node.childrenCheckedStatus == 'none' || node.childrenCheckedStatus == 'half') {
//					this.setChild(node, typeof checked != 'undefined' ? checked : true)
//				} else if (node.childrenCheckedStatus == 'all') {
//			this.setChild(node, checked)//typeof checked != 'undefined' ? checked : false)
////				}
////
			this.setParent(node, checked)
		},

		setChild (node, checked) {

			// 当前节点即将选中
			if (checked === true) {
				// 如果影响子节点
				if (this.doCheck.indexOf('s') >= 0) {
					node.checked = checked
					node.childrenCheckedStatus = checked ? 'all' : 'none'
				} else {

				}

			} else {
				// 如果影响子节点
				if (this.doUnCheck.indexOf('s') >= 0) {
					node.checked = checked
					node.childrenCheckedStatus = checked ? 'all' : 'none'
				} else {

				}
			}

			if (node.children && node.children.length > 0) {
				node.children.forEach((child) => {
					this.setChild(child, checked)
				})
			}
		},
		setParent (node, checked) {

			const parent = node.parent

			if (parent) {

//					if (this.doCheck.indexOf('p') >= 0 || this.doUnCheck.indexOf('p') >= 0) {
//						parent.checked = checked
//					}

				if (node.checked === true) {
					if (this.doCheck.indexOf('p') >= 0) {
						parent.checked = checked
					}
				} else {
					if (this.doUnCheck.indexOf('p') >= 0) {
						parent.checked = checked
					}
				}

				let childrenCheckedStatus
				if (parent.children.every(son => !son.checked)) {
					childrenCheckedStatus = 'none'
				} else if (parent.children.every(son => son.checked)) {
					childrenCheckedStatus = 'all'
				} else {
					childrenCheckedStatus = 'half'
				}

				// 半选兜底. 如果子节点
				if (parent.children.some(son => son.childrenCheckedStatus == 'half')) {
					childrenCheckedStatus = 'half'
				}

				if (this.doCheck.indexOf('p') === -1) {
					childrenCheckedStatus = parent.childrenCheckedStatus

				}

				if (this.doUnCheck.indexOf('p') === -1) {
					//	debugger
					childrenCheckedStatus = parent.childrenCheckedStatus
				}

				//	// console.log(parent.label, childrenCheckedStatus)

				parent.childrenCheckedStatus = childrenCheckedStatus

				this.setParent(parent, checked)
			}
		},

		init () {
			this.root.children = []

			if (this.url) {
				this.firstLoad = true
				this.doLoadData()
			} else if (this.myService) {
				this.firstLoad = true
				this.fetchData()
			} else if (this.data) {
				this.firstLoad = false
				this.myData = this.data
				this.myData.forEach((node) => {
					this.recursion(this.root.children, node, this.root)
				})

				this.$emit('on-data-update', this.root)

			}

		},

		reload () {

			if (this.service) {
				this.clear()
				this.firstLoad = true
				this.fetchData()
			}

			if (this.url) {
				this.clear()
				this.firstLoad = true
				this.doLoadData()
			}
		},

		clear () {

			this.root.children = []

		},

		doLoadData () {
			axios.post(this.url, assign({}, this.myQueryParam, this.myPager)).then(data => {

				this.$emit('on-data-load', data)
				let json = this.dataFilter(data)

				this.updateDataAfterQuery(json)
			}).catch(e => {
				this.firstLoad = false
			})

		},

		doSearch (param) {

			if (param) {
				this.myQueryParam = assign({}, this.myQueryParam, param)
			}

			//this.myPager.current = 1

			this.reload()

		},
		doReload () {
			this.reload()
		},

		fetchData () {

			let service = isFunction(this.myService) ? this.myService : this.myService.list

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

				this.firstLoad = false
			})

		},
		updateDataAfterQuery (json) {

			this.firstLoad = false

			this.myData = json.data
			this.myData.forEach((node) => {
				this.recursion(this.root.children, node, this.root)
			})

			this.$emit('on-data-update', this.root)

		},

		getCheckedNodes (data, childrenCheckedStatus) {

			// only-leaf 除去 half all ===> node.checked && node.children && node.children.length === 0

			// half ===> node.checked || node.childrenCheckedStatus === 'all'

			// all ===> node.checked // || node.childrenCheckedStatus === 'half' || node.childrenCheckedStatus === 'all'

			// mode: only-leaf 叶子节点 | no-half 叶子节点+父节点选中且去除半选的 | all-nodes 叶子节点+父节点选中及半选

			data = data || this.root.children
			let res = []
			for (const node of data) {
				if (this.onlyLeaf) {
					if (node.checked && node.children && node.children.length === 0) {
						res.push(node)
					}
				} else if (this.noHalf) {
					if (node.checked && node.childrenCheckedStatus === 'all') {
						res.push(node)
					}
				} else {
					if (node.checked || node.childrenCheckedStatus === 'half' || node.childrenCheckedStatus === 'all') {
						res.push(node)
					}
				}

				// 默认模式
				/*   if (node.checked || node.childrenCheckedStatus === 'half' || node.childrenCheckedStatus === 'all') {
				 res.push(node)
				 }*/

				if (node.children && node.children.length) {
					res = res.concat(this.getCheckedNodes(node.children))
				}
			}
			return res
		},

//			getCheckedNodes (data) {
//				let res = []
//				for (const node of data) {
//					if (node.checked || node.childrenCheckedStatus === 'half' || node.childrenCheckedStatus === 'all') {
//						res.push(node)
//					}
//					if (node.children && node.children.length) {
//						res = res.concat(this.getCheckedNodes(node.children))
//					}
//				}
//				return res
//			},

		getSelectedNodes (data) {
			data = data || this.root.children
			let res = []
			for (const node of data) {
				if (node.selected) {
					res.push(node)
				}
				if (node.children && node.children.length) {
					res = res.concat(this.getSelectedNodes(node.children))
				}
			}
			return res
		},
		recursion (collection, node, parent) {

			const obj = Object.assign({}, node, {children: []})

			//if (isUndefined(obj.value)) {
			obj.value = obj[this.reader.value]
			//}

			//if (isUndefined(obj.label)) {
			obj.label = obj[this.reader.label]
			//}

			this.$set(obj, '_key', this.key++)
			this.$set(obj, 'parent', parent)
			this.$set(obj, 'expand', node.expand || false)
			// 多选状态下，不存在selected
			this.$set(obj, 'selected', this.multiple ? false : node.selected || false)

			if (node.children && node.children.length > 0) {
				const onlyDataChild = node.children.every(item => !item.children || item.children.length == 0)
				if (onlyDataChild) {
					this.$set(obj, 'onlyDataChild', true)
				}

				this.$set(obj, 'checked', typeof node.checked != 'undefined' ? node.checked : false)
				// 控制显示隐藏
				this.$set(obj, 'visible', typeof node.visible != 'undefined' ? node.visible : true)
				this.$set(obj, 'childrenCheckedStatus', 'none')

				node.children.forEach((item) => {
					this.recursion(obj.children, item, obj)
				})

//					let childrenCheckedStatus
//					if (node.children.every(son => !son.checked)) {
//						childrenCheckedStatus = 'none'
//					} else if (node.children.every(son => son.checked)) {
//						childrenCheckedStatus = 'all'
//					} else {
//						childrenCheckedStatus = 'half'
//					}
//
//					// 半选兜底. 如果子节点
//					if (node.children.some(son => son.childrenCheckedStatus == 'half')) {
//						childrenCheckedStatus = 'half'
//					}
//
//					if (this.doCheck.indexOf('p') === -1) {
//						childrenCheckedStatus = node.childrenCheckedStatus
//
//					}
//
//					if (this.doUnCheck.indexOf('p') === -1) {
//						childrenCheckedStatus = node.childrenCheckedStatus
//					}
//
//					// console.log(node.label, childrenCheckedStatus)
//
//					node.childrenCheckedStatus = childrenCheckedStatus

				const checkedChildren = obj.children.filter(item => item.checked)
//					if (checkedChildren.length == obj.children.length) {
//						// 如果子项全部选中，则父选中
//						obj.childrenCheckedStatus = 'all'
//						obj.checked = true
//					} else if (checkedChildren.length > 0 && checkedChildren.length < obj.children.length) {
//
//						obj.childrenCheckedStatus = 'half'
//					}

				let childrenCheckedStatus
				if (obj.children.every(son => !son.checked)) {
					childrenCheckedStatus = 'none'
				} else if (obj.children.every(son => son.checked)) {
					childrenCheckedStatus = 'all'
				} else {
					childrenCheckedStatus = 'half'
				}

				// 半选兜底. 如果子节点
				if (obj.children.some(son => son.childrenCheckedStatus === 'half')) {
					childrenCheckedStatus = 'half'
				}

				if (this.doCheck.indexOf('p') === -1) {
					childrenCheckedStatus = obj.childrenCheckedStatus

				}

				if (this.doUnCheck.indexOf('p') === -1) {
					childrenCheckedStatus = obj.childrenCheckedStatus
				}

				//	// console.log(obj.label, childrenCheckedStatus)

				obj.childrenCheckedStatus = childrenCheckedStatus

			} else {
				// 只是子节点，选中的则默认选中
				this.$set(obj, 'checked', node.checked || false)
				// 只是子节点，控制显示隐藏
				this.$set(obj, 'visible', typeof node.visible != 'undefined' ? node.visible : true)
				// 这里的初始化ps状态需要再研究
				this.$set(obj, 'childrenCheckedStatus', node.checked ? 'all' : 'none')
				if (obj.selected) {
					this.selectedNode = obj
				}
			}

			collection.push(obj)
		},
		setLoadData (parent, data) {
			if (isArray(data)) {
				data.forEach((node) => {
					const obj = Object.assign({}, node, {children: []})

					//if (isUndefined(obj.value)) {
					obj.value = obj[this.reader.value]
					//}

					//if (isUndefined(obj.label)) {
					obj.label = obj[this.reader.label]
					//}

					this.$set(obj, '_key', key++)
					this.$set(obj, 'parent', parent)
					// 控制显示隐藏
					this.$set(obj, 'visible', typeof node.visible != 'undefined' ? node.visible : true)
					this.$set(obj, 'expand', typeof node.expand !== 'undefined' ? node.expand : false)
					this.$set(obj, 'selected', typeof node.selected !== 'undefined' ? node.selected : false)
					// 加载的数据 checked 继承父的状态
					this.$set(obj, 'checked', typeof node.checked !== 'undefined' ? node.checked : parent.checked)
					// 这里的初始化ps状态需要再研究

					this.$set(obj, 'childrenCheckedStatus', parent.checked ? 'all' : 'none')
					if (node.children && node.children.length > 0) {
						this.setLoadData(node, node.children)
					}
					parent.children.push(obj)
				})
			}
		},
		addNode (parent, node) {
			const obj = Object.assign({}, node, {children: []})
			this.$set(obj, '_key', key++)
			this.$set(obj, 'parent', parent)

			// 控制显示隐藏
			this.$set(obj, 'visible', typeof node.visible != 'undefined' ? node.visible : true)
			this.$set(obj, 'expand', typeof node.expand !== 'undefined' ? node.expand : false)
			this.$set(obj, 'selected', typeof node.selected !== 'undefined' ? node.selected : false)
			// 加载的数据 checked 继承父的状态
			this.$set(obj, 'checked', typeof node.checked !== 'undefined' ? node.checked : parent.checked)
			this.$set(obj, 'childrenCheckedStatus', parent.checked ? 'all' : 'none')
			parent.children.push(obj)

		},

		removeNode (node) {
			if (node.parent) {
				const parent = node.parent
				node.parent = null
				const index = parent.children.indexOf(node)
				if (index != -1) {
					parent.children.splice(index, 1)
					const _node = parent.children.length > 0 ? parent.children[0] : parent
					this.setParent(_node, false)
				}
			}
		},

		getAllNodes () {
			return this.root
		},

		updateNode (node, config) {
			for (let key in config) {
				node[key] = config[key]
			}
		},

		checkNode (node, checked = true, expand = true) {
			node.checked = checked

			this.getParents(node).forEach((el) => {
				el.expand = true
			})
		},

		selectNode (node, selected = true, expand = true) {
			node.selected = selected

			this.getParents(node).forEach((el) => {
				el.expand = true
			})
		},

		checkNodeByValue (value, checked = true, expand = true) {
			let nodes = []
			this.findNodesByValue(this.root, value, nodes)
			nodes.forEach((node) => {

				//this.onCheckClick(node, checked)
				this.onCheck(node, checked)

				this.getParents(node).forEach((el) => {
					el.expand = true
				})
			})
		},

		checkNodesByValue (value, checked = true, expand = true) {

			if (isArray(value)) {
				each(value, (v) => {
					this.checkNodeByValue(v, checked, expand)
				})
			} else {
				this.checkNodeByValue(value, checked, expand)
			}

			this.$nextTick(() => {
				this.$emit('on-check-change', this.getCheckedNodes())
			})
		},

		updateNodesByValue (value, options, expand = true) {
			if (isArray(value)) {
				each(value, (v) => {
					if (typeof options.checked != 'undefined') {
						this.checkNodeByValue(v, options.checked, expand)
					}
					if (typeof options.selected != 'undefined') {
						this.selectNodeByValue(v, options.selected, expand)
					}
				})
			} else {
				if (typeof options.checked != 'undefined') {
					this.checkNodeByValue(value, options.checked, expand)
				}
				if (typeof options.selected != 'undefined') {
					this.selectNodeByValue(value, options.selected, expand)
				}
			}

			this.$nextTick(() => {
				this.$emit('on-nodes-change', this.getNodesByValue(value))
			})
		},

		getNodesByValue (value) {

			let collections = []
			if (isArray(value)) {
				each(value, (v) => {
					this.getNodeByValue(v, collections)
				})
			} else {
				this.getNodeByValue(value, collections)
			}
			return collections
		},

		getNodeByValue (value, collections, parent) {
			(parent || this.root).children.forEach((node) => {
				this._getNodeByValue(value, collections, node)
			})
			return collections
		},

		_getNodeByValue (value, collections, node) {

			if (node[this.reader.value] === value) {
				collections.push(node)
			}

			if (node.children && node.children.length > 0) {
				node.children.forEach((child) => {
					this._getNodeByValue(value, collections, child)
				})
			}
		},

		selectNodeByValue (value, selected = true, expand = true) {
			let nodes = []
			this.findNodesByValue(this.root, value, nodes)
			nodes.forEach((node) => {
//					node.selected = selected

				this.onNodeClick(node, selected)
				this.$emit('on-select-change', this.selectedNode)

				this.getParents(node).forEach((el) => {
					el.expand = true
				})
			})
		},

		findNodesByValue (parent, value, nodes) {
			if (parent.children && parent.children.length > 0) {

				each(parent.children, (node) => {

					if (node[this.reader.value] == value) {
						nodes.push(node)
					}
					this.findNodesByValue(node, value, nodes)
				})

			}

		},

		getParents (node) {
			let parents = []
			this._getParents(node, parents)
			return parents
		},

		_getParents (node, parents) {
			if (node.parent) {
				parents.push(node.parent)
				this._getParents(node.parent, parents)
			}
		},

		// 全部
		expandAll (flag = true, data) {
			if (!data) {
				data = this.root.children
			}
			for (const node of data) {

				node.expand = flag

				if (node.children && node.children.length) {

					this.expandAll(flag, node.children)

				}
			}
		},

	},
}
</script>
