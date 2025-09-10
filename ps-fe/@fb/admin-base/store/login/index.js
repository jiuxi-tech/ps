/*!
* login
* (c) 2020 lincong1987
*/


export default {

	namespaced: true,

	state: () => {
		return {

			// 拖拽卡片状态
			dragImgStatus: '',

			test: 'lc',

			loginForm: {
				username: '',
				password: '',
				capture: '',
			},

		}
	},
	getters: {
		getTest(state){
			return state.test
		},
		getDragImgStatus (state) {
			return state.dragImgStatus
		}
	},
	mutations: {
		test (state, value) {
			state.test = value
		},
		setDragImgStatus (state, value) {
			state.dragImgStatus = value
		}
	},
	actions: {

		test (store, value) {
			store.commit('test', value)
		},

		// 登录
		login (store, loginForm) { //定义 Login 方法，在组件中使用 this.$store.dispatch("Login") 调用
			//  const username = loginForm.username.trim()

			this.$bizService

			return new Promise((resolve, reject) => { //封装一个 Promise
				loginService.doLogin(loginForm).then(response => { //使用 login 接口进行网络请求
					commit('') //提交一个 mutation，通知状态改变
					resolve(response) //将结果封装进 Promise
				}).catch(error => {
					reject(error)
				})
			})
		},

	},

}
