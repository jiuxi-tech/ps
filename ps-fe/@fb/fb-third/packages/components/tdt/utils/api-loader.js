export default class ApiLoader {
	/**
	 * @param config required 初始化参数
	 */
	constructor(config) {
		this._config = {
			v: "4.0",
			tk: "",
			plugins: [],
			...config
		};
		this._document = document;
		this._window = window;
		this._pluginsUrl = {
			D3: [
				"//cdn.bootcss.com/d3/3.5.17/d3.js",
				"//lbs.tianditu.gov.cn/api/js4.0/opensource/openlibrary/D3SvgOverlay.js"
			],
			CarTrack: [
				"//cdn.bootcss.com/d3/3.5.17/d3.js",
				"//lbs.tianditu.gov.cn/api/js4.0/opensource/openlibrary/D3SvgOverlay.js",
				"//lbs.tianditu.gov.cn/api/js4.0/opensource/openlibrary/CarTrack.js"
			],
			HeatmapOverlay: ["//lbs.tianditu.gov.cn/api/js4.0/opensource/openlibrary/HeatmapOverlay.js"],
			BufferTool: [
				"//cdn.bootcss.com/Turf.js/3.0.14/turf.js",
				"//lbs.tianditu.gov.cn/api/js4.0/opensource/openlibrary/BufferTool.js"
			],
			ImageOverLayer: ["//lbs.tianditu.gov.cn/api/js4.0/opensource/openlibrary/ImageOverlay.js"]
		};
	}

	load() {
		if (this._scriptLoadingPromise) {
			return this._scriptLoadingPromise;
		}
		this._scriptLoadingPromise = new Promise((resolve, reject) => {
			if (!this._config.tk) {
				// console.warn('错误警告：请在FbThird.options.tdt.tk 属性下填写正确的天地图开发者 token')
			}
			if (!window.T && this._config.tk) {
				const script = this._document.createElement("script");
				script.type = "text/javascript";
				script.async = true;
				script.defer = true;
				script.src = `//api.tianditu.gov.cn/api?v=${this._config.v}&tk=${this._config.tk}`;
				this._document.body.appendChild(script);
				script.onload = () => {
				  if (this._config.plugins && this._config.plugins.length) {
					  this.loadPlugins().then(resolve);
				  }
				  resolve();
				};
				script.onerror = e => reject(e);
			}
			if (this._config.plugins && this._config.plugins.length) {
				this.loadPlugins().then(resolve);
			}
			// resolve();
		});
		return this._scriptLoadingPromise;
	}

	loadPlugins() {
		return new Promise(resolve => {
			const promises = [];
			this._config.plugins.forEach(plugin => {
				this._pluginsUrl[plugin] &&
				this._pluginsUrl[plugin].forEach(url => {
					promises.push(this.loadOneScript(url));
				});
			});
			Promise.all(promises).finally(resolve);
		});
	}

	loadOneScript(url) {
		return new Promise((resolve, reject) => {
			const script = document.createElement("script");
			script.src = url;
			script.type = "text/javascript";
			script.async = true;
			script.defer = true;
			this._document.body.appendChild(script);
			script.onload = () => {
				resolve();
			};
			script.onerror = e => reject(e);
		});
	}
}
