<template>
	<div class="not-page">
		<div class="img">
			<img :src="require(`../../${imgSrc}`)" alt="">
		</div>

<!--		<div class="text">{{text}}</div>-->
		<h4 class="beat-text">
			<span v-for="(item, index) in textArr" :key="`text_${index}`">
				{{ item }}
			</span>
		</h4>

		<div class="message">{{message}}</div>

		<div class="btn-box">
			<slot name="btnBox">
				<fb-button @on-click="emitClick" type="primary" >点击返回</fb-button>
			</slot>
		</div>

	</div>
</template>

<script>
	/**
	 * UnknowPage
	 * (c) 2020 lincong1987
	 */

	export default {
		name: 'NotPage',
		props: {
			imgSrc: {
				type: [String],
				default: 'assets/img/notpage/404.png'
			},
			text: {
				type: [String],
				default: '404'
			},
			message: {
				type: [String],
				default: '你要找的页面不见了！'
			}
		},
		methods: {
			emitClick () {
				this.$emit('on-click')
			}
		},
		computed: {
			textArr() {
				return this.text.split('')
			}
		}
	}
</script>

<style lang="less" scoped>

	.not-page {
		text-align: center;

		>div {
			margin: 0 auto;
		}

		.text {
			font-size: 24px;
			color: #313C47;
			line-height: 32px;
			margin-bottom: 8px;
		}

		.message {
			font-size: 14px;
			color: #666666;
			line-height: 22px;
			margin-bottom: 24px;
		}

	}
	.beat-text {
		margin: 10px 0;
	}
	.beat-text span {
		top: 20px; /*字符上下浮动*/
		position: relative;
		animation: loading 0.3s ease 0s infinite alternate; /*引用动画*/
		/* animation: 动画名称 动画时间 动画曲线 何时开始 播放次数 是否反方向;
		alternate:表示动画先正常运行再反方向运行，并持续交替
		infinite:表示无限循环*/
		font-size: 60px;
		color: white;
		text-shadow: 0 1px 0  #CCC,
		0 2px 0 #CCC,
		0 3px 0 #CCC,
		0 4px 0 #CCC,
		0 5px 0 #CCC,
		0 6px 0 #CCC,
		0 7px 0 #CCC,
		0 8px 0 #CCC,
		0 9px 0 #CCC,
		0 10px 10px rgba(0, 0, 0, 0.5);
		/*text-shadow: 水平阴影位置(必须) 垂直阴影位置(必须) 模糊距离(可选) 阴影颜色(可选);
		  text-shadow: h-shadow v-shadow blur color; */
	}
	.beat-text span:nth-child(2) {
		animation-delay: 0.1s;
	}
	.beat-text span:nth-child(3) {
		animation-delay: 0.2s;
	}
	.beat-text span:nth-child(4) {
		animation-delay: 0.3s;
	}
	.beat-text span:nth-child(5) {
		animation-delay: 0.4s;
	}
	.beat-text span:nth-child(6) {
		animation-delay: 0.5s;
	}
	@keyframes loading {  /* 定义动画*/
		100% {
			top: -20px;  /*字符上下浮动*/
			text-shadow: 0 1px 0 #CCC,
			0 2px 0 #CCC,
			0 3px 0 #CCC,
			0 4px 0 #CCC,
			0 5px 0 #CCC,
			0 6px 0 #CCC,
			0 7px 0 #CCC,
			0 8px 0 #CCC,
			0 9px 0 #CCC,
			0 50px 25px rgba(0, 0, 0, 0.3);
			/*上升的时候阴影要在文字下面一些，所以垂直阴影的位置坐标和模糊距离要大一些*/
		}
	}
</style>
