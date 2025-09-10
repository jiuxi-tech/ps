<!-- 底部 app 下载 -->
<template>
	<div class="login-card-bottom">
		<div v-show="showPhoneText" class="login-card-bottom-left">
			<fb-icon name="telephone-fill" size="16"></fb-icon>
			<span>客服：</span>
		</div>
		<div class="login-card-bottom-right">
			<slot>
				<fb-icon name="mobilephone" size="16"></fb-icon>
				<fb-link type="primary" label="APP下载" @click="toAppDownload"></fb-link>
			</slot>
		</div>
	</div>
</template>

<script>
	/**
	 * DefaultApplicationLogin
	 * (c) 2020 lincong1987
	 */
	export default {

		components: {},

		props: {
			inForgetProcess: {
				type: Boolean,
				default: false
			},
			showPhoneText: {
				type: Boolean,
				default: true
			}
		},

		data () {
			return {}
		},

		mounted () {
		},

		methods: {
			toAppDownload () {
				this.$emit('toAppDownload')
			},
		}
	}
</script>

<style lang="less" scoped>
	@import ~"../../assets/styles/common";
	.login-card-bottom {
		position: absolute;
		left: 0;
		bottom: 0;
		width: 360px;
		height: 56px;
		background: #F9F9F9;
		border-radius: 0px 0px 4px 4px;
		padding: 17px 24px;

		.@{FbUiPrefix}-icon {
			color: #0284FE;
			margin-right: 8px;
			vertical-align: -1px;
		}

		.login-card-bottom-left {
			float: left;
			line-height: 22px;

			span {
				color: #8F8E94;
				font-size: 13px;
			}
		}

		.login-card-bottom-right {
			float: right;
			line-height: 22px;

			span {
				color: #0284FE;
				font-size: 13px;
			}
		}
	}


</style>
