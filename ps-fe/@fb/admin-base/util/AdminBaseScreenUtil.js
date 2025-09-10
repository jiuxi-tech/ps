// 主题列表
const themeList = [
	{
		value: 'classic_blue',
		label: '经典蓝',
	},
	{
		value: 'sky_blue',
		label: '天空蓝',
	},
	{
		value: 'sea_blue',
		label: '深海蓝',
	},
]

/**
 * 后台屏幕工具类
 * @desc 维护 主题
 * @desc 只维护基本变量 - 使用app.$的jquery操控部分html
 * */
class ScreenUtil {
	constructor(params) {
		// 初始化事件收集器
		this.onObj = {}
		this.oneObj = {}
		// 设置主题
		this.setThemes(params.themes || themeList)
		this.setTheme(app.$datax.get('src.theme') || params.theme || themeList[0].value)

	}

	/**
	 * 获取当前主题
	 * */
	getTheme() {
		return this.theme
	}
	/**
	 * 设置当前主题
	 * @param {String} theme 主题
	 * */
	setTheme(theme) {
		this.theme = theme
		const htmlEl = document.getElementsByTagName('html')[0]
		htmlEl.className = ''
		htmlEl.className = `theme-${theme}`

		app.$datax.set('src.theme', theme)
		this.trigger('src.theme', theme)
	}
	/**
	 * 获取当前主题列表
	 * */
	getThemes() {
		return this.themes
	}
	/**
	 * 设置当前主题列表
	 * @param {Array} themes 主题列表
	 * */
	setThemes(themes) {
		this.themes = themes
	}
	/**
	 * 获取默认提供主题列表
	 * */
	getThemeList() {
		return themeList
	}


	on(key, fn) {
		if (fn && typeof (fn) === 'function') {
			if (this.onObj[key] === undefined) {
				this.onObj[key] = [];
			}
			this.onObj[key].push(fn);
		} else {
			throw new Error('请传入正确的回调函数');
		}
	}
	one(key, fn) {
		if (this.oneObj[key] === undefined) {
			this.oneObj[key] = [];
		}

		this.oneObj[key].push(fn);
	}
	off(key) {
		this.onObj[key] = [];
		this.oneObj[key] = [];
	}
	trigger(...args) {
		if (args.length === 0) {
			return false;
		}
		const key = args[0];
		args = [].concat(Array.prototype.slice.call(args, 1));

		if (this.onObj[key] !== undefined
			&& this.onObj[key].length > 0) {
			Object.keys(this.onObj[key]).forEach((i) => {
				this.onObj[key][i].apply(null, args);
			});
		}
		if (this.oneObj[key] !== undefined
			&& this.oneObj[key].length > 0) {
			Object.keys(this.oneObj[key]).forEach((i) => {
				this.oneObj[key][i].apply(null, args);
				this.oneObj[key][i] = undefined;
			});
			this.oneObj[key] = [];
		}
		return null;
	}
}

export default ScreenUtil
