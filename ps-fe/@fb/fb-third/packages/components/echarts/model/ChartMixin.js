import {merge, assign} from "lodash-es";

export default {
	props: {
		options: {
			type: Object,
			default: () => {
				return {
					animationEasing: 'cubicOut',
					animationDelayUpdate(idx) {
						return idx * 100 + 300;
					},
					animationDelay(idx) {
						return idx * 100 + 100;
					}
				}
			}
		},
		theme: [String, Object],
		initOptions: Object,
		group: String,
		autoresize: {
			type: Boolean,
			default: true
		},
		watchShallow: Boolean,
		manualUpdate: Boolean,
		aria: Boolean
	},
	watch: {
		options: {
			deep: true,
			handler() {
				this.init()
			}
		},
		aria(newVal) {
			this.ariaOpt.aria.enabled = newVal
			this.opt = merge({}, this.opt, this.ariaOpt)
		}
	},
	mounted() {
		this.myChart = this.chart = this.$refs['fb-ec'].chart
		this.init()
	},
	beforeDestroy() {
		this.myChart = this.chart = null
	},
	methods: {
		init() {
			// 合并数据
			if (this.options) {
				this.opt = merge(this.opt, this.options)
			}
		},
	}

};


