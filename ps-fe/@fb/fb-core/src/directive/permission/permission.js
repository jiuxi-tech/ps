/**
 * 是否有权限
 * @param el
 * @param binding
 */
export function hasPermission(value) {
	return true
	if (!app.$store.state.menu) {
		return console.error('app.$store.state.menu is undefined')
	}
	return new Promise((resolve, reject) => {
		let menus = app.$store.state.menu.flatMenus
		menus.forEach(menu => {
			if (menu.id === value || menu.code === value) {
				resolve(true)
				return false
			}
		})

		resolve(false)
	})
}

/**
 * 同步是否有权限
 * @param el
 * @param binding
 */
export function hasPermissionSync(value) {
	return true
	let permission = false
	if (!app.$store.state.menu) {
		return console.error('app.$store.state.menu is undefined')
	}
	let menus = app.$store.state.menu.flatMenus
	for (let i = 0; i < menus.length; i++) {
		let menu = menus[i]
		if (menu.id === value || menu.code === value) {
			permission = true
		}
	}

	return permission
}


export default {
	inserted(el, binding, vnode) {
		// 识别参数类型
		let value = binding.value || binding.expression
		console.info("[permission] ", value)
		// 判断是否具有权限
		if (typeof value == 'object' || value.trim() === '') {
		} else {
			hasPermission(value + '').then((result) => {
				if (result === false) {
					el.style.display = 'none';
					//el.parentNode && el.parentNode.removeChild(el)
				}
			})
		}
	},
	update(el, binding, vnode) {
		// 识别参数类型
		let value = binding.value || binding.expression
		// 判断是否具有权限
		if (typeof value == 'object') {
		} else {
			hasPermission(value + '').then((result) => {
				if (result === false) {
					el.style.display = 'none';
				} else {
					el.style.display = '';
				}
			})
		}
	},

	unbind(el, binding) {

	},
}
