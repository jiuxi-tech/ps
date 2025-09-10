export default {
	inject: ['rootMenu'], // 注入
	computed: {
		indexPath() {
			const path = [this.index];
			let parent = this.$parent;
			while (parent.$options.componentName !== 'FbMenu') {
				if (parent.index) {
					path.unshift(parent.index);
				}
				parent = parent.$parent;
			}
			return path;
		},
		parentMenu() {
			let parent = this.$parent;
			while (
				parent &&
				['FbMenu', 'FbSubMenu'].indexOf(parent.$options.componentName) === -1
				) {
				parent = parent.$parent;
			}
			return parent;
		},
		paddingStyle() {
			// if (this.rootMenu.mode !== 'vertical') return {};
			if (this.rootMenu.mode !== 'inline') return {}

			let padding = this.rootMenu.paddingLeftNum;
			let parent = this.$parent;

			if (this.rootMenu.collapse) {
				padding = 20;
			} else {
				while (parent && parent.$options.componentName !== 'FbMenu') {
					if (parent.$options.componentName === 'FbSubMenu') {
						padding += this.rootMenu.paddingLeftNum || 20;
					}
					parent = parent.$parent;
				}
			}
			return {paddingLeft: padding + 'px'};
		}
	}
};
