<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-result
				v-if="code === 1"
				type="success"
				message="重置成功"
				:describe="message"
			>
			</fb-result>
		</div>
	</div>
</template>
<script>



	export default {
		name: 'account-restpwd',
		mixins: [

		],
		// 接收父组件的传参
		props: {
			param: {
				type: Object,
				require: false
			},
			parentPage: {
				type: Object,
				default: null
			}
		},
		// 组件
		components: {
			// 'component-a': ComponentA,
		},
		// 创建方法
		created() {
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化
			this.init(this.param);
		},
		data() {
			return {
				code: 0,
				message: "",
			}
		},

		methods: {
			// 初始化参数
			init(param) {
				if (param) {
					this.code = param.code;
					this.message = "敏感信息，请注意保密！新密码：" + param.data;
				}
			},
		}
	}
</script>
