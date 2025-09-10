<script>
	import { prefix } from '../../../../src/config'
import CascaderNode from './FbCascaderNode';
import { generateId } from '../../../utils/utils';

export default {
  name: 'FbCascaderMenu',

  inject: ['panel'],

  components: {
    CascaderNode
  },

  props: {
    nodes: {
      type: Array,
      required: true
    },
    index: Number
  },

  data() {
    return {
		prefix,
      activeNode: null,
      hoverTimer: null,
      id: generateId()
    };
  },

  computed: {
    isEmpty() {
      return !this.nodes.length;
    },
    menuId() {
      return `cascader-menu-${this.id}-${this.index}`;
    }
  },

  methods: {
    handleExpand(e) {
      this.activeNode = e.target;
    },
    handleMouseMove(e) {
      const { activeNode, hoverTimer } = this;
      const { hoverZone } = this.$refs;

      if (!activeNode || !hoverZone) return;

      if (activeNode.contains(e.target)) {
        clearTimeout(hoverTimer);

        const { left } = this.$el.getBoundingClientRect();
        const startX = e.clientX - left;
        const { offsetWidth, offsetHeight } = this.$el;
        const top = activeNode.offsetTop;
        const bottom = top + activeNode.offsetHeight;

        hoverZone.innerHTML = `
          <path style="pointer-events: auto;" fill="transparent" d="M${startX} ${top} L${offsetWidth} 0 V${top} Z" />
          <path style="pointer-events: auto;" fill="transparent" d="M${startX} ${bottom} L${offsetWidth} ${offsetHeight} V${bottom} Z" />
        `;
      } else if (!hoverTimer) {
        this.hoverTimer = setTimeout(this.clearHoverZone, this.panel.config.hoverThreshold);
      }
    },
    clearHoverZone() {
      const { hoverZone } = this.$refs;
      if (!hoverZone) return;
      hoverZone.innerHTML = '';
    },

    renderEmptyText(h) {
    	return h('div', {
    		class: [`${prefix}-cascader-menu__empty-text`],
		}, ['cascader.noData'])
    },
    renderNodeList(h) {
      const { menuId } = this;
      const { isHoverMenu } = this.panel;
      const events = { on: {} };

      if (isHoverMenu) {
        events.on.expand = this.handleExpand;
      }

      const nodes = this.nodes.map((node, index) => {
        const { hasChildren } = node;
        return h('cascader-node', {
			props: {
				node: node,
			},
			key: node.uid,
        	attrs: {
				'node-id': `${menuId}-${index}`,
				'aria-haspopup': hasChildren,
				'aria-owns': hasChildren ? menuId : null,
			},
			...events
		})
      });

		return [
			...nodes,
			isHoverMenu ? h('svg', {class: `${prefix}-cascader-menu__hover-zone`}) : null
		];
    }
  },

  render(h) {
    const { isEmpty, menuId } = this;
    // 指定绑个 自定义组件 el-scrollbar， html原生标签不支持
    const events = { nativeOn: {} };

    // optimize hover to expand experience (#8010)
    if (this.panel.isHoverMenu) {
		events.nativeOn.mousemove = this.handleMouseMove;
      // events.nativeOn.mouseleave = this.clearHoverZone;
    }

    return h('ul', {
    	attrs: {
			role: 'menu',
			id: menuId,
		},
		class: [`${prefix}-cascader-menu`],
	}, [isEmpty ? this.renderEmptyText(h) : this.renderNodeList(h)])
  }
};
</script>
