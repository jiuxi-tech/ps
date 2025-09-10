<template>

	<fb-container>
		<fb-container class="header"></fb-container>
		<fb-container class="header-logo"></fb-container>
		<fb-container class="header-shadow"></fb-container>

		<fb-container class="header-menu-date" :style="dateStyle">{{ date }}</fb-container>
		<fb-container class="header-menu-time" :style="timeStyle">{{ time }}</fb-container>

		<fb-container class="header-enter-admin" display="flex">
			<fb-container @on-click="jumpUrl('http://192.168.90.24:8080/fb-screen-docs/#/')">文档</fb-container>
			<fb-container @on-click="enterAdmin">
				<fb-icon name="bell-fill"></fb-icon>
				管理后台
			</fb-container>
			<!--      <fb-container @on-click="exitAdmin">退出系统 <fb-icon name="exit" size="18"></fb-icon></fb-container>-->
		</fb-container>

		<fb-container v-show="!hideMenu" class="header-menu-left" display="flex">
			<router-link to="/index" :active-class="'active'">首页</router-link>
			<a href="javascript:;">left2</a>
			<a href="javascript:;">left3</a>
		</fb-container>

		<fb-container v-show="!hideMenu" class="header-menu-right" display="flex">
			<a href="javascript:;">right2</a>
			<a href="javascript:;">right2</a>
			<div class="header-downbox">
				<a href="javascript:;">测试页 <fb-icon name="down-fill"></fb-icon></a>
				<div class="header-downbox-content">
					<a class="header-downbox-content-a" @click="$router.push('/theme/midnight-blue')" href="javascript:;">午夜蓝</a>
					<a class="header-downbox-content-a" @click="$router.push('/theme/cornflower-blue')" href="javascript:;">车矢菊兰</a>
					<a class="header-downbox-content-a" @click="$router.push('/theme/navy-blue')" href="javascript:;">海军蓝</a>
				</div>
			</div>
		</fb-container>
	</fb-container>

</template>

<script>
/**
 * header
 * (c) 2021 lincong1987
 */

export default {
	name: 'ScreenHeader',

	props: {


		noTime: {
			type: Boolean,
			default: false,
		},

		timeStyle: {
			type: [Object, Array, String],
			default() {
				return {}
			},
		},

		noDate: {
			type: Boolean,
			default: false,
		},


		dateStyle: {
			type: [Object, Array, String],
			default() {
				return {}
			},
		},

		hideMenu: { // 不显示菜单
			type: Boolean,
			default: false,
		}

	},
	data() {

		let date = this.$dayjs().format('YYYY年MM月DD日 dddd HH:mm:ss')

		return {
			date,
			time: '',
		}

	},
	methods: {
		enterAdmin() {
			this.$message.success('进入后台')
		},
		exitAdmin() {
			this.$message.success('退出系统')
		},
		selectChange(value, node) {
			console.log(value, node)
		},
		jumpUrl(url) {
			window.open(url)
		}
	},
	mounted() {
		this.timer = setInterval(() => {
			this.date = this.$dayjs().format('YYYY年MM月DD日 dddd HH:mm:ss')
		}, 1000)
	},
	destroyed() {
		clearInterval(this.timer)
	},
}
</script>

<style lang="less" scoped>
@import "../assets/styles/components/screen-header.less";
</style>
