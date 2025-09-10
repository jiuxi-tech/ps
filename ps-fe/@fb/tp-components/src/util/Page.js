/*!
* Page
* (c) 2020 lincong1987
*/

import autoheight from '@fb/fb-core/src/directive/autoheight'
import permission from '@fb/fb-core/src/directive/permission'

export default {
	directives: {
		// 高度自动调节控制
		autoheight: autoheight,
		// 权限控制
		permission: permission,
	},
	data() {
		return {
			updateCount: 0,//判断用户是否更新当前数据
			//dialog: closest(this, 'FbDialog'),
			// 表单默认值
			defaultData: {},
			defaultForm: {
				enabled: [
					{value: 1, label: '启用'},
					{value: 0, label: '禁用'}
				],
				actived: [
					{value: 1, label: '是'},
					{value: 0, label: '否'}
				],
				sex: [
					{value: '0', label: '保密'},
					{value: '1', label: '男'},
					{value: '2', label: '女'}
				],
				week: [
					{value: '1', label: '星期一'},
					{value: '2', label: '星期二'},
					{value: '3', label: '星期三'},
					{value: '4', label: '星期四'},
					{value: '5', label: '星期五'},
					{value: '6', label: '星期六'},
					{value: '7', label: '星期日'},
				]
			},
			buttons: {
				del: {
					disabled: true,
				},
				edit: {
					disabled: true,
				},
			}
		}

	},
	methods: {

		getRules() {
			return {}
		},

		/**
		 * 当前时间
		 * @param format 时间返回的格式：YYYYMMDDHHmmss 或 YYYYMMDD
		 */
		currentDate(format) {
			let date = new Date();
			return this.formatDate(date, format)
		},
		/**
		 * 格式化字符串, function(date, format)
		 *
		 * @param date
		 *            日期字符串
		 * @param format
		 *            格式化格式如 yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss
		 * @returns
		 */
		formatDate(date, format) {
			if (!date)
				return "";
			if (!format)
				format = "YYYY-MM-DD";
			switch (typeof date) {
				case "string":
					date = date.trim();
					if (date.length == 4) {
						date = date.substring(0, 4) + "/01/01";
					} else if (date.length == 6) {
						date = date.substring(0, 4) + "/" + date.substring(4, 6)
							+ "/01";
					} else if (date.length == 8) {
						date = date.substring(0, 4) + "/" + date.substring(4, 6)
							+ "/" + date.substring(6, 8);
					} else if (date.length == 12) {
						date = date.substring(0, 4) + "/" + date.substring(4, 6)
							+ "/" + date.substring(6, 8) + " "
							+ date.substring(8, 10) + ":"
							+ date.substring(10, 12) + ":01";
					} else if (date.length == 14) {
						date = date.substring(0, 4) + "/" + date.substring(4, 6)
							+ "/" + date.substring(6, 8) + " "
							+ date.substring(8, 10) + ":"
							+ date.substring(10, 12) + ":"
							+ date.substring(12, 14);
					}
					// 为了兼容苹果最新浏览器，把 “-” 替换为 “/”
					date = date.replace(/\-/g, '/')
					date = new Date(date);
					break;
				case "number":
					date = new Date(date);
					break;
			}
			if (!date instanceof Date)
				return;
			var dict = {
				"YYYY": date.getFullYear(),
				"M": date.getMonth() + 1,
				"d": date.getDate(),
				"H": date.getHours(),
				"m": date.getMinutes(),
				"s": date.getSeconds(),
				"MM": ("" + (date.getMonth() + 101)).substr(1),
				"DD": ("" + (date.getDate() + 100)).substr(1),
				"HH": ("" + (date.getHours() + 100)).substr(1),
				"mm": ("" + (date.getMinutes() + 100)).substr(1),
				"ss": ("" + (date.getSeconds() + 100)).substr(1)
			};
			let formatDate = format.replace(/(YYYY|MM?|DD?|HH?|ss?|mm?)/g, function () {
				return dict[arguments[0]];
			});
			return formatDate;
		},

		// 逗号分割的字符串切割成数组，并返回list，废弃------
		formatArr(arr) {
			return arr.split(',');
		},

		// 返回key和value的数组
		formatMap(value) {
			let keys = [];
			let values = [];

			value.forEach(function (value, key) {
				values.push(value);
				keys.push(key);
			})
			return [keys, values];
		},

		// 列表不支持服务时，写url时调用的方法
		getBaseUrl(url) {
			return this.$svc.service.defaults.baseURL + url;
		},
		// 附件的格式化
		formatFile(list) {
			return list.map(item => {
				return {'attachId': item.attachId}
			});
		},

		// 列表界面表格的按钮控制
		handleTableSelect(row, selectedRows) {
			this.buttons.del.disabled = selectedRows.length == 0
			this.buttons.edit.disabled = selectedRows.length != 1
		},
		// 记录原来的默认值，用于表单重置
		createdPage() {
			this.defaultData = JSON.parse(JSON.stringify(this.$data.formData));
		},
		formReset() {
			// 隐藏
			//this.dialog.visible = false;
			// 表单重置
			Object.assign(this.$data.formData, this.defaultData);
		},
		closeTpDialog(e) {
			this.parentPage.closeTpDialog(e);
		},

		stepNext() {
			this.parentPage.next();
		},

		stepPrev() {
			this.parentPage.prev();
		},

		// 设置参数，用于界面传参
		setPageParam(e) {
			this.parentPage.setPageParam(e)
		},
		// 获取参数，用于界面传参
		getPageParam() {
			return this.parentPage.getPageParam()
		},
		// 路由跳转，参数需要加密，同一个页面打开新的tabbar
		pushTab(path, id, tabLabel, options) {
			options = options || '';

			let encOptions = "";
			if (Object.prototype.toString.call(options) == "[object Object]") { // 判断是否是对象
				// 转为json字符串再加密
				encOptions = this.$router.encrypt(JSON.stringify(options))
			} else {
				encOptions = this.$router.encrypt(options)
			}

			this.$router.push({
				path: path,
				query: {'id': this.$router.encrypt(id), 'tabLabel': tabLabel, 'options': encOptions}
			})
		},
		// 路由跳转,参数需要加密的使用
		push(path, options) {
			options = options || '';

			let encOptions = "";
			if (Object.prototype.toString.call(options) == "[object Object]") { // 判断是否是对象
				// 转为json字符串再加密
				encOptions = this.$router.encrypt(JSON.stringify(options))
			} else {
				encOptions = this.$router.encrypt(options)
			}

			this.$router.push({
				path: path,
				query: {'options': encOptions}
			})
		},
		// 获取路由跳转的参数
		getRouteParam() {
			let router = this.$router;
			let route = this.$route;
			let query = route.query;
			let path = route.path;
			let id = router.decrypt(query.id);
			let tabLabel = query.tabLabel;
			let options = router.decrypt(query.options);

			try {
				// 如果是json字符串，则转为对象
				options = JSON.parse(options);
			} catch (e) {
				// 如果不是，则不需要处理
			}

			return {'path': path, 'id': id, 'tabLabel': tabLabel, 'options': options}
		},
		// 行政区划树格式化显示全称
		cityTreeHeaderFormat(node) {
			if (node) {
				return node.extend01;
			} else {
				return ""
			}
		},

		// 部门树异步加载，查询除顶层之外的下级，一层一层查
		loadDeptTreeData(item, callback) {
			if (item.children.length >= 1) {
				callback();
			} else {
				this.$svc.sys.dept.org.tree({deptId: item.id, sync: 0}).then((result) => {
					if (result.code == 1) {
						if (result.data[0].children.length >= 1) {
							callback(result.data[0].children);
						}else{
							callback();
						}
					} else {
						// 服务器返回失败
						this.$message.error('行政区划树加载失败' + result.message)
					}
				})
			}
		},

		// 行政区划树异步加载，查询除顶层之外的下级，一层一层查
		loadCityTreeByCodeData(item, callback) {
			if (item.children.length >= 1) {
				callback();
			} else {
				this.$svc.sys.city.treeByCode({cityId: item.id, sync: 0, expand: true, showTop: 'off'}).then((result) => {
					if (result.code == 1) {
						callback(result.data);
					} else {
						// 服务器返回失败
						this.$message.error('行政区划树加载失败' + result.message)
					}
				})
			}
		},

		// 行政区划树异步加载，查询除顶层之外的下级，一层一层查
		loadCityTreeData(item, callback) {
			if (item.children.length >= 1) {
				callback();
			} else {
				this.$svc.sys.city.tree({cityId: item.id, sync: 0, expand: true, showTop: 'off'}).then((result) => {
					if (result.code == 1) {
						callback(result.data);
					} else {
						// 服务器返回失败
						this.$message.error('行政区划树加载失败' + result.message)
					}
				})
			}
		},
		// 获取当前登录人信息
		getCurrentUser() {
			return app.$datax.get('userInfo')
		},
	},
	// 监听用户是否修改过信息
	updated: function () {
		if (this.updateCount == -1) {
			this.updateCount = 0;
		} else {
			this.updateCount++;
		}
	},
	// watch: {
	// 	/**
	// 	 * 监听父组件的值的变化, 第一种用法watch有一个特点，就是当值第一次绑定的时候，不会执行监听函数，
	// 	 * 只有值发生改变才会执行。如果我们需要在最初绑定值的时候也执行函数，则就需要用到immediate属性。
	// 	 * @param newValue
	// 	 * @param oldValue
	// 	 */
	// 	formData: {
	// 		handler(newValue, oldValue) {
	// 			let that = this;
	// 			if (oldValue) {
	// 				this.updateCount++;
	// 			}else{
	// 			}
	// 		},
	// 		// 立即生效
	// 		immediate: true,
	// 		// 深度监听
	// 		deep: true
	// 	}
	// }
}
