<template>
	<div style="margin-top: 20px">
		<h2>我是子组件：{{ name }}</h2>
		<div>财富：{{ money }}</div>
		<fb-button @on-click="giveMoney">给钱</fb-button>
	</div>
</template>

<script>
export default {
	name: "Son",
	props: {
		name: {
			type: String
		},
		money: {
			type: Number,
			default: 0
		}
	},
	methods: {
		giveMoney() {
			this.$emit('on-give-money', {
				msg: '我需要钱',
				number: 10000
			})
		}
	}
}
</script>

<style scoped>

</style>
