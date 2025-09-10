<!-- 底部 app 下载 -->
<template>
	<transition name="slide-to-down">
		<div class="appdown-card">
			<h1>APP下载</h1>
			<div class="login-code">
				<div class="login-scan-code">
					<div class="login-scan-code--img">
						<fb-qrcode v-if="qrcodeUrl" v-bind="qrcodeOption" value="qrcodeUrl" :width="182" :height="182"></fb-qrcode>
						<img v-else :src="$svc.sys.qr.appDownload()" style="width: 182px;">
					</div>
				</div>
				<div class="texts">
					<fb-icon name="scan"></fb-icon>
					<span>打开微信扫一扫下方二维码下载APP</span>
				</div>
			</div>

			<login-card-bottom :show-phone-text="showPhoneText">
				<fb-link type="primary" label="关闭二维码" @click="toLogin"></fb-link>
			</login-card-bottom>
		</div>
	</transition>
</template>

<script>
/**
 * DefaultApplicationLogin
 * (c) 2020 lincong1987
 */

import LoginCardBottom from "./LoginCardBottom";

export default {

	components: {
		LoginCardBottom
	},

	props: {
		inForgetProcess: {
			type: Boolean,
			default: false
		},
		showPhoneText: {
			type: Boolean,
			default: true
		},
		qrcodeUrl: {
			type: [String, Number],
			default: ''
		},
		qrcodeOption: {
			type: [Object],
			default: () => ({})
		}
	},

	data () {
		return {}
	},

	mounted () {
	},

	methods: {
		toLogin () {
			this.$emit('toLogin')
		},
	}
}
</script>

<style lang="less" scoped>
@import "../../assets/styles/common.less";
.appdown-card {
	.loginCardCom();
	text-align: center;

	h1 {
		margin: 0;
		padding-top: 32px;
		padding-bottom: 36px;
		font-size: 24px;
		color: #313C47;
		line-height: 32px;
	}

	.login-code {

		.login-scan-code {
			position:         relative;
			width: 200px;
			height: 200px;
			border-radius: 2px;
			border: 1px solid #D3D3D3;
			padding: 8px;
			margin: 0 auto;


			.login-scan-code--img {
				width: 100%;
				height: 100%;
				background: url("../../assets/img/login/logo-blue.png") center no-repeat;
				background-size: 100% 100%;
			}

		}

		.texts {
			margin-top: 24px;
			margin-bottom: 32px;
			text-align: center;

			span {
				margin-left: 8px;
			}
		}
	}


}


</style>
