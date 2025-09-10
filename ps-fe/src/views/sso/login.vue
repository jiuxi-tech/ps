<template>
	<div>
 sso lodaing, please wait...
	</div>
</template>

<script>




	export default {
		name: 'sso-login',
		mixins: [

		],
		// 组件
		components: {
			// 'component-a': ComponentA,
		},
		// 创建方法
		created() {
			// 记录原来的默认值，用于表单重置
		},
		// 初始化方法
		mounted() {
			this.init();
		},
		data() {
			return {}
		},

		// 方法
		methods: {
			init() {
 

			// http://localhost:10801/#/main?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5YWQ3Mzc1M2M0ZmY5YmFjYjI3MTVhNTQzZWVlM2Q1YmRjOTFlYmUxNmM0ZjBlMWUxMzhlZWE0MTM3ODM0YmNmYTcxMzQ5NTBlYWI2NzZlNzRkNzY4MGJkY2MyMGQ1NTIyNGM1Y2RjYmNiMGFjNjQ1MTk5MjAyMjJhNWUyZTJhMjY0ZWRiOTI0YTJlNWNhYzE4ZTQxNTFiZmM4ZjFjNDkxYTA2OGNjMGNjMGQ2NDM5OTU3Y2RhYWJkODlhNzcwYjc3MmI3NDE4M2RiYjQ0NGQ4NzJhOTZkYjk4ZjQ1OTA3Y2UyYWJmYmEzZWZlMDUyYTRjYjA4MDczODM4NDc5ODIxNDhhNjgyNjY2NjUzZmNhOGYyMWQwYTEwYjQyMGMyODY2OTg1OTU5OTQ4MWNjY2I0NGNmYzljZDgyYWM5ZmU5MWViNTRjZDM2MmIwMzkyMmNmMDU2MjUyZWJjNmUzODE1N2UzMWZkMWI2YjQ4N2JhMzdiNmYwNjBkZDM2MDU3NGQ4YjE0NmQyYmRlODY1NjBmMDE1MDA3MjRhNjlkZGM3YzJmMzJiMTI0NzI5YWI0YzYxNDlkM2E5YTIzOTI0NDM2YzJjZDJhODhmOTFmZDNjY2ViNTQ4OTg4MDA2NWQ4NTZmODZjMWFiMTZmNTQzODJlMjQzN2I1ZDliNDA2MGU2NDUzNjYzYTZmZDQ3OGY4YzJmZGMwYzRiYmJkNWFhOWJjZmZlMzkzMWU5ZDE2MThkMTk4OTI2YWMzYTJlMmVjZmFjMmQyNjIwOTBkODY2OWQzNDllNzc0NzNlZTA3OWE2YjczMDI2NWU4NDRlNjBlOGM5NWEzYTAyZDY5ZDAzNjJkOTJlYmYzMTMzYjAwOGM5YzAxMGIzNGIyMTU2MWExNzhiMWVjNDIyY2EwZjNmNDY1ZTA3MWFlMmRjZjhmNDQyNWYwMWFlYTlmNjNmMzhjODlhYzRmZjdmYWE2YTEzYThmMzUzNGI0NDkxZjUxZjk5MjMwYTM1OTgyNDNlZmZmZDdlNjgxM2QyOWRjMWNlOTJjOGU2YzdmY2UzMzFjODg3NDhhYmQzNjA0ZmM4ODA1ZjYxMTk5NGQ1ZTZkYTNiNTI0YzlmY2FmYWUzYWU5MTQ5NzEzNTVjODcyMDBkYjgxNzciLCJpc3MiOiJjb20uaml1eGkiLCJleHAiOjE3NTU5OTQyMzYsInZlcnNpb24iOiJ2MyIsImlhdCI6MTc1NTk4NzAzNiwianRpIjoiNTU0OWJmYzg2OGQwNGU4YzhmYTY2NDNhMWQ3NWExYWMifQ.n92DlRV6757JC0WNhWZdFef5Wm5kVDXU-SMQvRkqsQw
				let token = this.getQueryStr("token");

			//	alert(token)
				this.$datax.set('token', token)
				 

				// 获取登陆人用户信息
				this.$svc.platform.getUserInfo().then(res => {
					this.$datax.set('userInfo', res.data)
					//alert('登陆成功')
					// 跳转页面
					this.$message.success('登陆成功', {
						duration: 3000
					})
					 setTimeout(() => {
					     
						this.$router.replace('/main')

						 

					 },3000)
				}).catch(e=>{
					this.$alert("获取用户信息失败, 请重新登录",()=>{
						this.$router.replace('/login')
					})
				});
			},

			getQueryStr(str) {
				let LocString = String(window.document.location.href);

				var rs = new RegExp("(^|)" + str + "=([^&]*)(&|$)", "gi").exec(LocString), tmp;
				if (tmp = rs) {
					return tmp[2];
				}
			}
		}
	}
</script>

<style lang="less" scoped>

</style>
