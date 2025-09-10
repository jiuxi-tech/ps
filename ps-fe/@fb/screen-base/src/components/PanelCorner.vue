<template>

	<div class="panel-corner">
		<div class="panel-corner__tl"></div>
		<div class="panel-corner__tr"></div>
		<div class="panel-corner__bl"></div>
		<div class="panel-corner__br"></div>
		<slot></slot>
	</div>



</template>

<script>
/**
 * PanelCorner
 * (c) 2021 lincong1987
 */

export default {
	name: 'PanelCorner',
}
</script>

<style lang="less" scoped>
@import "../assets/styles/components/panel-corner.less";
</style>
