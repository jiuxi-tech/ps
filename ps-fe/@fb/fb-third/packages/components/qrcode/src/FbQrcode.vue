<template>
	<div class="fb-qrcode" :style="{
		height: `${height}px`,
		width: `${width}px`,
	}">
		<template v-if="svg !== ''">
			<div v-html="svg" class="fb-qrcode__code"
				 :style="{
		height: `${height}px`,
		width: `${width}px`,
	}"></div>
		</template>
		<template v-else>
			<div class="fb-qrcode__error">
				{{ error }}
			</div>
		</template>

	</div>
</template>

<script>
/**
 * FbQrcode
 * (c) 2021 lincong1987
 */

import QRCode from './QRCode'

export default {
	name: 'FbQrcode',

	props: {

		value: {
			type: [String, Number],
			default: '',
		},
		height: {
			type: [String, Number],
			default: 256,
		},

		width: {
			type: [String, Number],
			default: 256,
		},
		padding: {
			type: [String, Number],
			default: 2,
		},
		color: {
			type: [String],
			default: '#000000',
		},
		background: {
			type: [String],
			default: '#ffffff',
		},
		/**
		 * 容错等级
		 * 可选属性值 L, M, Q, H
		 *  L最低，H最高
		 */
		level: {
			type: [String],
			default: 'Q',
		},
		errorMessage: {
			type: [String],
			default: '二维码内容为空',
		},
	},

	data () {
		return {
			svg: '',
			error: '',
			imgUrl: ''
		}
	},

	watch: {
		value: {
			immediate: true,
			handler () {
				this.genCode()
			},
		},
		height: {
			handler () {
				this.genCode()
			},
		},
		width: {
			handler () {
				this.genCode()
			},
		},
		padding: {
			handler () {
				this.genCode()
			},
		},
		color: {
			handler () {
				this.genCode()
			},
		},
		background: {
			handler () {
				this.genCode()
			},
		},
		level: {
			handler () {
				this.genCode()
			},
		},
		errorMessage: {
			handler () {
				this.genCode()
			},
		},
	},

	mounted () {

	},

	beforeDestroy () {
	},

	destroyed () {

	},

	methods: {
		genCode () {
			console.log('genCode')
			if (!this.value) {
				this.svg = ''
				this.error = this.errorMessage
			} else {
				this.error = ''
				this.svg = new QRCode({
					content: this.value,
					padding: this.padding,
					width: this.width - 2,
					height: this.height - 2,
					color: this.color,
					background: this.background,
					ecl: this.level.toUpperCase(),
				}).svg()
			}
		},
		downloadImg(downloadName) {

			const image = new Image();
			this.imgUrl = image.src = 'data:image/svg+xml;base64,' + window.btoa(unescape(encodeURIComponent(this.svg))); //给图片对象写入base64编码的svg流

			const canvas = document.createElement('canvas');  //准备空画布
			canvas.width = this.width;
			canvas.height = this.height;
			const context = canvas.getContext('2d');  //取得画布的2d绘图上下文

			image.onload = () => {
				context.drawImage(image, 0, 0);
				const a = document.createElement('a')
				a.href = canvas.toDataURL('image/png')
				a.download = downloadName || '二维码'
				a.click()
			}
		}
	},

}
</script>

<style lang="less" scoped>

.fb-qrcode {
	border:          1px solid #ccc;
	border-radius:   4px;
	box-sizing:      border-box;

	display:         flex;
	flex-wrap:       wrap;
	justify-content: center;
	align-items:     center;

	&__code {
		border-radius: 4px;
		overflow:      hidden;
	}

	&__error {
		border-radius: 4px;
	}
}
</style>
