/*!
 * App
 * (c) 2021 lincong1987
 */

import {closest} from '../util/component'
import autoheight from '../directive/autoheight'
import permission from '../directive/permission'

export const Page = {
	directives: {
		// 高度自动调节控制
		autoheight: autoheight,
		// 权限控制
		permission: permission,
	},
	props: {
		service: {
			type: Object,
			required: false,
		},
		data: {
			type: Object,
			required: false,
		},
	},
	data() {
		return {
			app: 'Page',
			dialog: closest(this, 'FbDialog'),
			myService: this.service || {},
			myReset: null,
		}
	},
	mounted() {
		console.info('[' + this.app + '] mounted')

		this.doFill();
	},
	methods: {
		doFill() {
			console.info('[' + this.app + '] doFill')

			this.$data.myReset = JSON.parse(JSON.stringify(this.data || {}));

			if (this.data) {
				// 不存在service，直接作为数据提供
				if (this.myService && this.myService.get) {
					// 存在service，作为参数，进行查询
					this.myService.get(this.data).then((json) => {
						if (json.code == -1) {
							this.$message.error(json.message)
						} else {
							this.$data.myReset = JSON.parse(JSON.stringify(json.data || {}));
							this.handleReset();
						}
					})
				}
			}

			this.handleReset();
		},
		handleClose() {
			console.info('[' + this.app + '] handleClose')

			this.dialog && this.dialog.hide()
		},
		handleReset() {
			console.info('[' + this.app + '] handleReset')

			// 数据重置
			if (this.$data.myReset && this.$data.form) {
				this.$data.form = JSON.parse(JSON.stringify(this.$data.myReset))

				// 校验重置
				setTimeout(() => {
					this.$refs.form && this.$refs.form.resetFields()
				}, 0)
			}
		},
	},
}

export const SearchPage = {
	mixins: [Page],
	data() {
		return {
			app: 'SearchPage',
			buttons: {
				save: {
					disabled: false,
					show: true,
				},
				edit: {
					disabled: true,
					show: true,
				},
				del: {
					disabled: true,
					show: true,
				},
			},
			table: {
				primaryKey: 'id',
				columns: [],
				data: [],
			},
			reader: {
				table: "table"
			}
		}
	},
	methods: {
		handleQuery() {
			console.log('[SearchPage] handleQuery success')

			this.$refs.table && this.$refs.table.doSearch()
		},
		handleTableSelect(row, selectedRows) {
			console.log('[SearchPage] handleTableSelect success')

			console.log(row, selectedRows)

			this.buttons.del && (this.buttons.del.disabled = selectedRows.length == 0)
			this.buttons.edit && (this.buttons.edit.disabled = selectedRows.length != 1)
		},
		handleSave() {
			console.log('[SearchPage] handleSave success')

			this.$refs.dialog && this.$refs.dialog.show({
				url: "./add",
				close: (data) => {
					if (data) {
						this.$message.success('操作成功...', {time: 1000})

						// 重新获取
						this.handleQuery();
					}
					return Promise.resolve(true)
				}
			})
		},
		handleEdit() {
			console.log('[SearchPage] handleEdit success')

			var rows = this.$refs.table.getSelectedRows()
			if (rows.length != 1) {
				this.$message.error('请选择一条数据进行编辑')
				return
			}

			var model = {};
			model[this.table.primaryKey] = rows[0][this.table.primaryKey];

			this.$refs.dialog && this.$refs.dialog.show({
				url: "./edit",
				data: model,
				close: (data) => {
					if (data) {
						this.$message.success('操作成功...', {time: 1000})
						// 重新获取
						this.handleQuery();
					}
				}
			})
		},
		handleView(row) {
			console.log('[SearchPage] handleView success')

			if (!row) {
				var rows = this.$refs.table.getSelectedRows()
				if (rows.length == 1) {
					row = rows[0];
				}
			}

			if (!row) {
				this.$message.error('请选择一条数据进行查看')
				return
			}

			var model = {};
			model[this.table.primaryKey] = row[this.table.primaryKey];

			this.$refs.dialog && this.$refs.dialog.show({
				url: "./view",
				data: model
			})
		},
		handleDel() {
			console.log('[SearchPage] handleDel success')

			var rows = this.$refs.table.getSelectedRows()
			if (rows.length == 0) {
				this.$message.success('这是一条成功消息，会自动消失！')
				return
			}

			this.$confirm('这是一个危险操作，请确认', () => {
				var model = {};
				model[this.table.primaryKey] = this.$refs.table.getSelectedRows().map((row) => {
					return row[this.table.primaryKey]
				});

				this.myService.del(model).then(() => {
					this.$message.success('操作成功...', {time: 1000})
					// 重新获取
					this.handleQuery();
				})
			})
		},
	},
}

export const SavePage = {
	mixins: [Page],
	data() {
		return {
			app: 'SavePage',
		}
	},
	mounted() {
		console.info('[' + this.app + '] mounted')

		this.dialog && !this.dialog.myTitle && (this.dialog.myTitle = "新增")
	},
	methods: {
		handleSave() {
			this.$refs.form.validate((result) => {
				if (result === true) {
					console.log('[SavePage] handleSave success')

					let data = this.$data.form
					if (!this.myService || !this.myService.save) {
						this.dialog && this.dialog.hide(data)
					} else {
						this.myService.save(data).then((json) => {
							if (json.code == -1) {
								this.$message.error(json.message)
							} else {
								this.dialog && this.dialog.hide(json)
							}
						})
					}
				}
			})
		},
	},
}

export const EditPage = {
	mixins: [Page],
	data() {
		return {
			app: 'EditPage',
		}
	},
	mounted() {
		console.info('[' + this.app + '] mounted')

		this.dialog && (this.dialog.myTitle = "修改")
	},
	methods: {
		handleSave() {
			this.$refs.form.validate((result) => {
				if (result === true) {
					console.log('[EditPage] handleSave success')

					let data = this.$data.form
					if (!this.myService || !this.myService.update) {
						this.dialog && this.dialog.hide(data)
					} else {
						this.myService.update(data).then(() => {
							this.dialog && this.dialog.hide(data)
						})
					}
				}
			})
		},
	},
}

export const ViewPage = {
	mixins: [Page],
	data() {
		return {
			app: 'ViewPage',
		}
	},
	mounted() {
		console.info('[' + this.app + '] mounted')

		this.dialog && (this.dialog.myTitle = "查看")
	},
}

export default {
	Page,
	SavePage,
	EditPage,
	ViewPage
}
