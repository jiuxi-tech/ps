<template>
	<div :class="`${prefix}-collapse`" role="tablist" aria-multiselectable="true">
		<slot></slot>
	</div>
</template>
<script>
	import { prefix } from '../../../../src/config'
	export default {
		name: 'FbCollapse',

		componentName: 'FbCollapse',

		props: {
			accordion: Boolean,
			value: {
				type: [Array, String, Number],
				default() {
					return [];
				}
			},
			collapseIcon: {
				type: [String, Number],
				default: 'down'
			},
			expandIcon: {
				type: [String, Number],
				default: 'up'
			},
		},

		data() {
			return {
				prefix,
				activeKey: [].concat(this.value)
			};
		},

		provide() {
			return {
				collapse: this
			};
		},

		watch: {
			value(value) {
				this.activeKey = [].concat(value);
			}
		},

		methods: {
			setActiveNames(activeKey) {
				activeKey = [].concat(activeKey);
				let value = this.accordion ? activeKey[0] : activeKey;
				this.activeKey = activeKey;
				this.$emit('input', value);
				this.$emit('change', value);
				this.$emit('on-change', value);
			},
			handleItemClick(item) {
				if (this.accordion) {
					this.setActiveNames(
						(this.activeKey[0] || this.activeKey[0] === 0) &&
						this.activeKey[0] === item.itemKey
							? '' : item.itemKey
					);
				} else {
					let activeKey = this.activeKey.slice(0);
					let index = activeKey.indexOf(item.itemKey);

					if (index > -1) {
						activeKey.splice(index, 1);
					} else {
						activeKey.push(item.itemKey);
					}
					this.setActiveNames(activeKey);
				}
			}
		},

		created() {
			this.$on('item-click', this.handleItemClick);
		}
	};
</script>
