<template>
	<div class="tx-demo-page ">
		<div>
			{{ content }}
		</div>




		<div>

			<button @click="setContent">set</button>
			<router-link target="_blank" :to="{path:'/sys/index/index2',query:{_blank: 'open'}}">新页面打开home页</router-link>
		</div>

		<fb-form ref="loginForm">
			<fb-form-item prop="username" label="用户名">
				<fb-editor v-model="content"></fb-editor>
			</fb-form-item>
		</fb-form>
	</div>

</template>

<script>


	export default {
		name: 'index-editor',
		// 接收父组件的传参
		props: {},
		data () {
			return {
				content: '',
			}

		},
		methods: {

			setContent () {
				this.content = '<p>打算d</p> <p>asdasda</p> <p>&nbsp;</p> <p>asd</p> <p>&nbsp;</p> <p>安师大</p>'

			},
		},
	}
</script>

<style lang="less" scoped>
	.tx-demo-page {
		color:     #313C47;
		font-size: 14px;
		width: 100%;
		height: 100%;

	}

</style>

