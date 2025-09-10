/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default {

	namespaced: true,

	state: () => {
		return {
			userInfo: {},
			token: '',
		}

	},

	getters: {
		getUserInfo (state) {
			return state.userInfo
		},

		getToken (state) {
			return state.token
		},
	},

	mutations: {
		setUserInfo (state, userInfo) {
			state.userInfo = userInfo
		},

		setToken (state, token) {
			state.token = token
		},

		removeToken (state) {
			state.token = ''
		},
	},

	actions: {

		setUserInfo (store, userInfo) {
			store.commit('setUserInfo', userInfo)
		},

		setToken (store, token) {

			store.commit('setToken', token)
		},

		removeToken ({commit}) {
			commit('removeToken')
		},

	},

}
