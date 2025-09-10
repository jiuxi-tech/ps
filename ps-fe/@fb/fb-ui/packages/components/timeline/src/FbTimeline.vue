
<script>
import FbIcon from '../../icon/src/FbIcon'
import { prefix } from '../../../../src/config'

/**
 * FbTimeline
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTimeline',
	components: {FbIcon},
	props: {
		// 模式 (left/right/alternate)
		mode: {
			type: [String],
			default: 'left'
		},
		// 反转
		reverse: {
			type: [Boolean],
			default: false
		},
		// 开启水平效果（mode 为top/bottom/alternate）
		horizontal: {
			type: [Boolean],
			default: false
		}
	},
	data () {
		return {
			prefix,
			horizontalWidth: 0
		}
	},

	created () {
	},
	mounted() {
		this.horizontalWidth = 0
	},

	beforeDestroy () {
		this.fbTags = null
	},

	methods: {
		caleHorizontalWidth(num) {
			if (num > 0) {
				this.horizontalWidth += num
			}
		}
	},

	computed: {

	},

	render(h) {
		const { prefix, mode, reverse, horizontal, horizontalWidth } = this
		const prefixCls = `${prefix}-timeline`

		const timelineProps = {
			class: [
				prefixCls,
				`${prefixCls}-${mode}`,
				{[`${prefixCls}-horizontal`]: horizontal}
			],
			style: {width: horizontalWidth ? horizontalWidth + 'px' : '100%'}
		}

		let timelineItems = this.$slots.default ? this.$slots.default.filter(vnode => {
			return vnode.tag && vnode.componentOptions.tag === 'fb-timeline-item'
		}) : []

		timelineItems = reverse ? timelineItems.reverse() : timelineItems

		function getPositionCls(vnode, idx) {
			var eleProps = vnode.componentOptions.propsData || {};
			if (mode === 'alternate') {
				if (eleProps.position === 'right') return prefixCls + '-item-right';
				if (eleProps.position === 'left') return prefixCls + '-item-left';
				return idx % 2 === 0 ? prefixCls + '-item-left' : prefixCls + '-item-right';
			}
			if (mode === 'left') return prefixCls + '-item-left';
			if (mode === 'right') return prefixCls + '-item-right';
			if (eleProps.position === 'right') return prefixCls + '-item-right';
			return '';
		}

		// 获取水平展示
		function getHorizontalPositionCls(vnode, idx) {
			const eleProps = vnode.componentOptions.propsData || {};
			if (mode === 'alternate' || mode === 'alternate-center') {
				if (eleProps.position === 'top') return prefixCls + '-item-top';
				if (eleProps.position === 'bottom') return prefixCls + '-item-bottom';
				return idx % 2 === 0 ? prefixCls + '-item-bottom' : prefixCls + '-item-top';
			}
			if (mode === 'top' || mode === 'top-center') return prefixCls + '-item-top';
			if (mode === 'bottom' || mode === 'bottom-center') return prefixCls + '-item-bottom';
			if (eleProps.position === 'bottom') return prefixCls + '-item-bottom';
			return '';
		}

		const itemsCount = timelineItems.length;
		const lastCls = `${prefixCls}-item-last`
		const items = timelineItems.map((vnode, idx) => {
			const readyClass = idx === itemsCount - 1 ? lastCls : '';
			if (horizontal && mode.indexOf('top') >= 0) {
				const eleProps = vnode.componentOptions.propsData || {};
				eleProps.position = 'top'
			}
			vnode.data = Object.assign(vnode.data, {
				class: [
					readyClass,
					horizontal ? getHorizontalPositionCls(vnode, idx) : getPositionCls(vnode, idx)
				]
			})
			return vnode
		})

		return h('ul', timelineProps, [
			items
		])
	}
}
</script>

<style scoped>

</style>
