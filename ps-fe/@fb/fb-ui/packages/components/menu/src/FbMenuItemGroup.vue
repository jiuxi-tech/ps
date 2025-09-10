<template>
  <li role="menuitemgroup" :class="[`${prefix}-menu-item-group`]">
    <div :class="[`${prefix}-menu-item-group-title`]" :style="[{paddingLeft: levelPadding + 'px'}, {backgroundColor}]">
      <template v-if="!$slots.title">{{title}}</template>
      <slot v-else name="title"></slot>
    </div>
    <ul>
      <slot></slot>
    </ul>
  </li>
</template>
<script>
	import {prefix} from '../../../../src/config'
  export default {
    name: 'FbMenuItemGroup',

    componentName: 'FbMenuItemGroup',

    inject: ['rootMenu'],
    props: {
      title: {
        type: String
      }
    },
    data() {
      return {
		  prefix,
        paddingLeft: 20
      };
    },
    computed: {
		backgroundColor() {
			return this.rootMenu.backgroundColor || '';
		},
      levelPadding() {
      	let padding = this.rootMenu.paddingLeftNum - 8;
        let parent = this.$parent;
        if (this.rootMenu.inlineCollapse) return padding;
        while (parent && parent.$options.componentName !== 'FbMenu') {
          if (parent.$options.componentName === 'FbSubMenu') {
            padding += this.rootMenu.paddingLeftNum;
          }
          parent = parent.$parent;
        }
        return padding;
      }
    }
  };
</script>

