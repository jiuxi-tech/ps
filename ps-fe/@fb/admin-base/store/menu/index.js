/*!
 * index
 * (c) 2020 lincong1987
 */

import {each, flatMapDepth} from 'lodash-es'

export default {

	namespaced: true,

	state() {
		return {
			menus: [],
			loadingStauts: false,
			flatMenus: [],
			subMenu: {},
		}
	},

	getters: {
		getMenus(state) {
			return state.menus
		},
		getSubMenu(state) {
			return state.subMenu
		},
		getFlatMenus(state) {
			return state.flatMenus
		},
	},

	mutations: {

		focus(state, menu) {
			each(state.menus, (m) => {
				if (menu.code === m.code) {
					m.focus = true
				}
			})
		},

		add(state, menu) {
			state.menus.push(menu)
		},

		load(state, menus) {
			state.menus = menus

			// 扁平化
			const flatArray = (value) => {
				let result = [];
				value.forEach(item => {
					result.push(item);
					if (item.children) {
						result = result.concat(flatArray(item.children));
					}
				});
				return result;
			}
			state.flatMenus = flatArray(menus)
			state.loadingStauts = true

			state.subMenu = menus.length > 0 ? menus[0] : {}
		},

		fetch(state, menus) {
			state.menus = menus
		},
		subMenu(state, menu) {
			console.log('mutations subMenu')
			state.subMenu = menu
		},
		clear(state) {
			state.menus = []
			state.subMenu = {}
			state.flatMenus = []
			state.loadingStauts = false
		},
	},

	actions: {

		load(store, menus) {
			console.log('menu actions load')
			store.commit('load', menus)
			// store.commit('subMenu', menus.length > 0 ? menus[0] : {})
		},

		fetch(store) {
			app.$svc.admin.sys.getMenus().then(res => {
				store.commit('fetch', res.data)
			})
		},

		subMenu({commit}, menu) {
			commit('subMenu', menu)
		},

		clear({commit}) {
			commit('clear')
		},

	},

}
