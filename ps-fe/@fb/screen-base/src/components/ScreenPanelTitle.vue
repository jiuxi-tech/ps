<template>
	<fb-container class="screen-panel-title"
				  v-bind="$attrs"
				  v-on="$listeners"
				  :class="{
                        [`screen-panel-title--theme-${theme}`]: !!theme
                    }"
	>

		<span class="title-text" :class="{'title-click': $listeners['on-title-click']}" @click="$emit('on-title-click')">
			<slot name="title">{{ title }}</slot>
		</span>


		<slot></slot>

		<div class="actions" :style="actionsStyle">
			<slot name="actions"></slot>
		</div>

	</fb-container>
</template>

<script>
/**
 * ScreenPanelTitle
 * (c) 2021 lincong1987
 */

export default {
	name: 'ScreenPanelTitle',

	props: {
		theme: {
			type: [String, Number],
		},

		title: {
			type: [String, Number],
		},

		actionsStyle: {
			type: [String, Object, Array],
			default: () => []
		}
	},
}
</script>

<style lang="less" scoped>

@import "../assets/styles/components/screen-panel-title.less";

</style>
