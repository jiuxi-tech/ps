<template>
	<div style="color: #fff; font-size: 32px">
		<h1>我是父组件</h1>
		<div>财富：{{ money }}</div>
		<div>
			<fb-input v-model="sonName"></fb-input>
		</div>

		<Son :name="sonName" :money="childMoney" @on-give-money="giveMoney"></Son>
	</div>
</template>

<script>
import Son from './Son'
export default {
	name: "Parent",
	components: {
		Son
	},
	data() {
		return {
			sonName: 'tom',
			money: 30001,
			childMoney: 0
		}
	},
	methods: {
		giveMoney(params) {
			console.log(params)
			this.$message.warn('son msg: ' + params.msg)
			if (this.money > 1) {
				this.money = this.money - params.number
				this.childMoney = this.childMoney + params.number
			} else {
				this.$message.error('parent: 身体被掏空！！！')
			}

		}
	}
}
</script>

<style>
	h1, h2, h3{
		color: #fff;
	}
</style>
