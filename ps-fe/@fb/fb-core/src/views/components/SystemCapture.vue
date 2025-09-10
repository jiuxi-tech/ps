<template>
	<div class="login-capture" :style="getStyle" @click="handleClick">
		<fb-icon v-if="loading" size="xl" name="loading2" rotating class="login-capture__loading"></fb-icon>
	</div>
</template>

<script>
	/**
	 * SystemCapture
	 * (c) 2020 lincong1987
	 */

	export default {
		name: 'SystemCapture',

		props: {
			api: {
				type: String,
				default: '/capture',
			},
		},

		data () {
			return {
				loading: false,
				backgroundImage: '',
			}
		},

		computed: {
			getStyle () {
				return {
					backgroundImage: this.loading ? 'none' : `url(${this.backgroundImage})`,
				}
			},
		},

		methods: {

			getCapture () {
				this.loading = true
				this.backgroundImage = this.api + new Date().getTime()

				this.$nextTick(() => {
					setTimeout(() => {
						this.loading = false
					}, 1000)
				})

				// this.$systemService.get(this.api).then(res => {
				// 	this.backgroundImage = res.data.data
				// })
			},
			handleClick () {
				this.getCapture()
			},
		},

		mounted () {

			this.getCapture()

		},

	}
</script>

<style lang="less" scoped>

	.login-capture {
		position:         relative;
		font-size:        0;
		display:          inline-block;
		width:            90px;
		height:           36px;
		background-color: #fff;
		border-radius:    4px;
		border:           1px solid #D3D3D3;
		cursor:           pointer;
		margin-left:      8px;
		vertical-align:   bottom;
		background-size:  cover;

		.login-capture__loading {
			position:    absolute;
			left:        50%;
			top:         50%;
			margin-top:  -10px;
			margin-left: -10px
		}
	}

</style>
