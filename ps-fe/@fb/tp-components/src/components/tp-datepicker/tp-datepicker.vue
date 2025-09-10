<!--

使用说明：

format：显示的文本格式
value-format： 提交的值的格式，支持 YYYYMMDD、YYYYMMDDHHmmss等,不支持YYYYMMDDHH

<tp-datepicker v-model="formData.birthday" :min-date="new Date(2021, 03, 01)" :max-date="new Date(2021, 03, 15)" max-range="3D" mode="range" format="YYYY-MM-DD" value-format="YYYYMMDD" ></tp-datepicker>

-->

<template>
	<div>
		<fb-datepicker
			:clearable="clearable"
			:format="format"
			:max-date="myMaxDate"
			:max-range="maxRange"
			:min-date="myMinDate"
			:mode="mode"
			:placeholder="placeholder"
			:readonly="readonly"
      :disabled="disabled"
			:showFootLeftBtns="showFootLeftBtns"
			@on-change="onChangeDate"
			@on-clear="clear"
			ref="xxx"
			v-model="datepicker.value"></fb-datepicker>
	</div>
</template>

<script>
	export default {
		name: "TpDatepicker",
		mixins: [],
		// 接收父组件的传参
		props: {
			value: {
				type: [Array, String, Date],
				require: false
			},
			valueFormat: {
				type: String,
				require: false
			},
			readonly: {
				type: Boolean,
				require: false,
				default: false
			},
      disabled: {
        type: Boolean,
        default: false
      },
			clearable: {
				type: Boolean,
				require: false,
				default: true
			},
			mode: {
				type: String,
				require: false,
			},
			// 占位符
			placeholder: {
				type: String,
				default: '请选择日期',
			},
			minDate: {
				type: [Date, String],
				default: null,
				require: false,
			},
			maxDate: {
				type: [Date, String],
				default: null,
				require: false,
			},
			// 7D 七天， 1M 一个月， 2Y 两年
			// 以当前日期为中心,向左右延伸可选日期
			maxRange: {
				type: String,
				default: undefined,
			},
			format: {
				type: String,
				default: 'YYYY-MM-DD',
			},
			rangeFormat: {
				type: Function,
				require: false
			},
			showFootLeftBtns: {
				type: [Array],
			},
		},
		model: {
			prop: 'value',
			event: 'listener' //这个事件名可以随意写，它实际上是规定了子组件要更新父组件值需要注册的方法
		},
		// 组件
		components: {
			// 'component-a': ComponentA,
		},
		// 创建方法
		created() {

		},
		// 初始化方法
		mounted() {
			let midMinDate = this.minDate
			let midMaxDate = this.maxDate

			if (typeof midMinDate === "string") {
				this.myMinDate = "" === midMinDate ? null : app.dayjs(midMinDate).toDate();
			}


			if (typeof midMaxDate === "string") {
				this.myMaxDate = "" === midMaxDate ? null : app.dayjs(midMaxDate).toDate();
			}

		},
		data() {

			return {
				// 表单数据
				datepicker: {
					value: '',
				},

				myMinDate: this.minDate,
				myMaxDate: this.maxDate,

			}
		},
		// 方法
		methods: {

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

			/**
			 * 监听日期改变事件
			 * @param dates
			 * @param time
			 */
			onChangeDate(dates, time) {
				this.handlerDate(dates)
				this.$emit('on-change', dates)
			},
			// 处理日期格式，并日期格式化，传给父节点
			handlerDate(dates) {

				if (this.mode && this.mode === 'range') {

					let date0 = this.formatDate(dates[0], this.valueFormat)
					let date1 = this.formatDate(dates[1], this.valueFormat)

					this.$emit('rangFormat', date0, date1)

					this.$emit('listener', [date0, date1])
				} else {
					let date = this.formatDate(this.datepicker.value, this.valueFormat)
					this.$emit('listener', date, this.datepicker.value)
				}
			},
			// 日期格式化 valueFormat
			handlerRangeDate() {

			},
			clear() {
				if (this.mode && this.mode === 'range') {
					this.$emit('listener', [])
				} else {
					this.$emit('listener', "")
				}
			},
		},
		watch: {
			/**
			 * 监听父组件的值的变化, 第一种用法watch有一个特点，就是当值第一次绑定的时候，不会执行监听函数，
			 * 只有值发生改变才会执行。如果我们需要在最初绑定值的时候也执行函数，则就需要用到immediate属性。
			 * @param newValue
			 * @param oldValue
			 */
			value: {
				handler(newValue, oldValue) { // 初始化的时候，手动赋值的时候，需要执行该方法，

					let that = this;

					// 判断是否是范围选
					if (this.mode && this.mode === 'range') {

						// 如果不是数组
						if (!(newValue instanceof Array)) {
							return;
						}

						// 没有初始化数据
						if (newValue.length === 0) {
							return;
						}

						// 取出数据
						let date0 = newValue[0]

						if (typeof date0 === "string") {
							// 横杠表示是选中的，需要格式为没有横杠的
							if (date0.indexOf("-") == -1 || date0.indexOf("/") == -1) {

								// range,传两个参数
								let arr = [];
								newValue.forEach(function (item) {
									if (item && item != '') {
										arr.push(that.formatDate(item, that.format))
									}
								})
								that.datepicker.value = arr;

								return;
							}
						}

						// range,传两个参数
						let arr = [];
						newValue.forEach(function (item) {
							if (item && item != '') {
								arr.push(that.formatDate(item, that.format))
							}
						})
						that.datepicker.value = arr;

					} else {

						// 判断值是否为空
						if (!newValue || newValue == '') {
							try {
								that.$refs.xxx.clear();
							} catch (e) {

							}
							return
						}

						if (typeof newValue === "string") {
							// 横杠表示是选中的，需要格式为没有横杠的
							if (newValue.indexOf("-") > 0 || newValue.indexOf("/") > 0) {
								let date = this.formatDate(newValue, this.valueFormat)
								this.$emit('listener', date, newValue)
							} else {
								// 如果没有横岗，则表示只需要初始化底层组件
								that.datepicker.value = that.formatDate(newValue, that.format)
							}
						} else {
							// 如果不是字符串，那么可能是日期
							let date = this.formatDate(newValue, this.valueFormat)
							this.$emit('listener', date, newValue)
						}
					}
				},
				// 立即生效
				immediate: true
			},

			minDate(value1, value2) {
				if (typeof value1 === "string") {
					this.myMinDate = "" === value1 ? null : app.dayjs(value1).toDate();
				} else {
					this.myMinDate = value1;
				}
			},
			maxDate(value1, value2) {
				if (typeof value1 === "string") {
					this.myMaxDate = "" === value1 ? null : app.dayjs(value1).toDate();
				} else {
					this.myMaxDate = value1;
				}

			},
		}
	}
</script>

<style scoped>

</style>
