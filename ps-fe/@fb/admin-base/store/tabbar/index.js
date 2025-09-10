/*!
* index
* (c) 2020 lincong1987
*/

import {find, indexOf, findIndex} from 'lodash-es'

export default {

	namespaced: true,

	state() {
		return {

			tabs: [],
			current: '',
			keyNum: 0,

		}
	},

	getters: {
		getTabs(state) {
			return state.tabs
		},
	},

	mutations: {

		add(state, route) {
			// 定义label
			route.tabLabel = route.text || route.meta.title || '无名字'

			// 判断不同 path 追加
			let noAdd = state.tabs.some((item) => {
				let isPath = item.fullPath === route.fullPath
				return isPath
			})

			// 判断相同 path 携带独有 id
			// tabLabel 作为导航标题
			if (route.query) {
				let tabLabel  = route.query.tabLabel || route.params.tabLabel || null
				if (tabLabel && tabLabel !== route.tabLabel) {
					route.tabLabel = tabLabel
					let queryId = route.query.id || route.params.id || null
					noAdd = state.tabs.some((item) => {
						if (item.query) {
							let id = item.query.id || item.params.id || null
							return (id === queryId && tabLabel === item.tabLabel)
						}
					})
				}
			}



			if (noAdd) return;

			state.tabs.forEach((t) => {
				t.focus = false
			})
			route.focus = true

			let num = ++state.keyNum
			// 不使用path 使用自己追加 key
			if (!route.fullPath) {
				route.fullPath = route.path
			}
			route.tabName = route.fullPath
			state.current = route.tabName

			state.tabs.push(route)
		},

		focus(state, needFocus) {
			state.current = needFocus
		},

		remove(state, tabName) {

			let index = findIndex(state.tabs, (tab) => {
				return tab.tabName === tabName
			})

			if (index != -1) {
				this.commit('tabbar/changeCurrent', tabName)
				state.tabs.splice(index, 1)
			}
		},

		removeNow(state) {
			let index = findIndex(state.tabs, (tab) => {
				return tab.tabName == state.current
			})

			if (index != 0 && index != -1) {
				this.commit('tabbar/changeCurrent', state.current)
				state.tabs.splice(index, 1)
			}
		},

		removeLeft(state) {
			let index = state.tabs.length;
			state.tabs = state.tabs.filter((item, idx) => {
				if (item.tabName === state.current) {
					index = state.tabs.indexOf(item);
				}
				return (idx == 0) || index <= state.tabs.indexOf(item);
			});
		},
		removeRight(state) {
			let index = state.tabs.length;
			state.tabs = state.tabs.filter((item, idx) => {
				if (item.tabName === state.current) {
					index = state.tabs.indexOf(item);
				}
				return (idx == 0) || index >= state.tabs.indexOf(item);
			});
		},

		removeOthers(state) {
			state.tabs = state.tabs.filter((item, idx) => {
				return (idx == 0) || item.tabName === state.current;
			});
		},

		removeAll(state) {
			state.tabs = state.tabs.filter((item, idx) => idx == 0);
			if (state.tabs[0]) {
				state.current = state.tabs[0].tabName
			}
		},

		changeCurrent(state, tabName) {
			if (state.tabs.length > 0) {
				if (state.current == tabName) {
					state.tabs.forEach((tab, index) => {
						if (tab.tabName === tabName) {
							let nextTab = state.tabs[index + 1] || state.tabs[index - 1];
							if (nextTab) {
								state.current = nextTab.tabName;
							}
						}
					})
				}
			}
		},

		clear(state) {
			state.tabs = []
			state.current = ''
		}

	},
	actions: {

		add() {

		},
		remove ({commit}, code) {
			commit('remove', code)
		},
		removeNow ({commit}, code) {
			commit('removeNow', code)
		},

		async removeLeft({commit, state}) {
			await commit('removeLeft')
		},
		async removeRight({commit, state}) {
			await commit('removeRight')
		},
		async removeOthers({commit, state}) {
			await commit('removeOthers')
		},
		async removeAll({commit, state}) {
			await commit('removeAll')
		},


		clear({commit}) {
			commit('clear')
		}
	}

}
