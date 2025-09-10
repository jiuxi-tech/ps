<template>
	<div>

		<div class="screen-bottom-nav-bg-wrapper">
			<div class="screen-bottom-nav-bg-inner">
				<div class="screen-bottom-nav-bg"></div>
			</div>
		</div>

		<div class="screen-bottom-nav">

			<div class="nav nav-1" :class="{active: active==='/jcxx'}"
			     @click="()=>{$router.push('/')}">
				<div class="name">
					<span class="label">基本信息</span>
					<div class="icon-box">
						<fb-icon name="help-fill" size="28"></fb-icon>
					</div>
				</div>
			</div>
			<div class="nav nav-2" :class="{active: active==='/jcbj'}"
			     @click="()=>{$router.push('/')}">
				<div class="name">
					<span class="label">监测报警</span>
					<div class="icon-box">
						<fb-icon name="risk-alarm-fill" size="28"></fb-icon>
					</div>
				</div>
			</div>

			<div class="nav nav-3" :class="{active: active==='/index'}" @click="()=>{$router.push('/')}">
				<div class="name">
					<span class="label">首页</span>
					<div class="icon-box">
						<fb-icon name="refresh" size="28"></fb-icon>
					</div>
				</div>
			</div>

			<div class="nav nav-4" :class="{active: active==='/fxyp'}"
			     @click="()=>{$router.push('/')}">
				<div class="name">
					<span class="label">风险研判</span>
					<div class="icon-box">
						<fb-icon name="bell-fill" size="28"></fb-icon>
					</div>
				</div>
			</div>

			<div class="nav nav-5" :class="{active: active==='/zxcc'}"
			     @click="()=>{$router.push('/')}">
				<div class="name">
					<span class="label">在线抽查</span>
					<div class="icon-box">
						<fb-icon name="menu-used-fill" size="28"></fb-icon>
					</div>
				</div>
			</div>

		</div>
	</div>
</template>

<script>
/**
 * ScreenBottomNav
 * (c) 2021 lincong1987
 */

export default {
	name: 'ScreenBottomNav',

	props: {
		active: String,
	},

}
</script>

<style lang="less" scoped>

</style>
