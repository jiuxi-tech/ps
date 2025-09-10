<template>
	<span :class="[
	  `${prefix}-dot` ,
	]">
		 {{myText}}
	</span>
</template>

<script>
	import { prefix } from '../../../../src/config'
	import { filter } from 'lodash-es'

	/**
	 * FbDot
	 * (c) 2020 lincong1987
	 */
	export default {
		name: 'FbDot',
		props: {
			text: {
				type: String,
				default: '...',
			},

			// 分割符
			separator: {
				type: String,
				default: '',
			},

			// 定时
			interval: {
				type: Number,
				default: 1000,
			},

		},

		data () {
			return {
				prefix,
				myText: this.text,
				timer: null,
				textArray: [],
				index: 0,
			}
		},
		computed: {},

		watch: {
			text () {

			},
		},

		methods: {
			init () {

				this.textArray = this.myText.split(this.separator)
				this.index = 1

				clearInterval(this.timer)
				this.timer = setInterval(() => {

					if (this.index === this.textArray.length) {
						this.index = 1
					}

					this.myText = this.index

					console.log(this.index, filter(this.textArray, (n, i) => {
						return i < this.index
					}))

					this.index++

//					if (this.textArray.length - 1 >= this.index) {
//						this.index = 0
//					}

//					this.myText = filter(this.textArray, (n, i) => {
//						return i < this.index
//					}).join('')

				}, this.interval)
			},
		},

		mounted () {
			this.init()
		},

		destroyed () {
			clearInterval(this.timer)
		}
	}
</script>

<style scoped>

</style>
