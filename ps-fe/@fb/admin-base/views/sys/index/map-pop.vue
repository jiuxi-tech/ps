<template>
	<div class="map-info" :data-id="id">
		<div class="map-info-head">
			<fb-text size="14">{{title}}</fb-text>
		</div>
		<div class="map-info-body">

			<div style="margin-bottom: 8px">
				<fb-tag :color="colors[t.color]" :style="{color: t.fontColor}" v-for="(t, idx) in tags" :key="idx">
					{{t.label}}
				</fb-tag>
			</div>

			<div>
				<fb-row gutter="4">
					<fb-col span="24">
						<fb-table-layout class="gary-bg">
							<fb-table-layout-cell width="20px"><fb-badge dot :dotColor="colors.grey_7" dotSize="12" vertical="-1px"></fb-badge></fb-table-layout-cell>
							<fb-table-layout-cell>风险点总数</fb-table-layout-cell>
							<fb-table-layout-cell align="right">1000</fb-table-layout-cell>
						</fb-table-layout>
					</fb-col>
				</fb-row>
				<fb-row gutter="4">
					<fb-col span="12">
						<fb-table-layout class="gary-bg">
							<fb-table-layout-cell width="20px"><fb-badge dot :dotColor="colors.red_5" dotSize="12" vertical="-1px"></fb-badge></fb-table-layout-cell>
							<fb-table-layout-cell>重大风险</fb-table-layout-cell>
							<fb-table-layout-cell align="right">1000</fb-table-layout-cell>
						</fb-table-layout>
					</fb-col>
					<fb-col span="12">
						<fb-table-layout class="gary-bg">
							<fb-table-layout-cell width="20px"><fb-badge dot :dotColor="colors.orange_5" dotSize="12" vertical="-1px"></fb-badge></fb-table-layout-cell>
							<fb-table-layout-cell>较大风险</fb-table-layout-cell>
							<fb-table-layout-cell align="right">1000</fb-table-layout-cell>
						</fb-table-layout>
					</fb-col>
				</fb-row>
				<fb-row gutter="4">
					<fb-col span="12">
						<fb-table-layout class="gary-bg">
							<fb-table-layout-cell width="20px"><fb-badge dot :dotColor="colors.yellow_5" dotSize="12" vertical="-1px"></fb-badge></fb-table-layout-cell>
							<fb-table-layout-cell>一般风险</fb-table-layout-cell>
							<fb-table-layout-cell align="right">1000</fb-table-layout-cell>
						</fb-table-layout>
					</fb-col>
					<fb-col span="12">
						<fb-table-layout class="gary-bg">
							<fb-table-layout-cell width="20px"><fb-badge dot :dotColor="colors.blue_5" dotSize="12" vertical="-1px"></fb-badge></fb-table-layout-cell>
							<fb-table-layout-cell>低风险</fb-table-layout-cell>
							<fb-table-layout-cell align="right">1000</fb-table-layout-cell>
						</fb-table-layout>
					</fb-col>
					<fb-col span="12">
						<fb-table-layout class="gary-bg">
							<fb-table-layout-cell width="20px"><fb-badge dot :dotColor="colors.blue_5" dotSize="12" vertical="-1px"></fb-badge></fb-table-layout-cell>
							<fb-table-layout-cell>低风险</fb-table-layout-cell>
							<fb-table-layout-cell align="right">1000</fb-table-layout-cell>
						</fb-table-layout>
					</fb-col>
				</fb-row>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: 'map-pop',
		props: {
			title: '',
			tags: {
				type: [Array],
				default: () => [
					{color:"grey_7", label: '123123'},
					{color:"red_5",  label: '123123'},
					{color:"orange_5", label: '123123'},
					{color:"yellow_5", fontColor: '#111', label: '123123'},
					{color:"blue_5", label: '123123'},
				]
			}
		},
		data () {
			return {
				id: new Date().getTime()
			}
		},

		methods: {

		},

		mounted() {
			console.log(this.colors)
		},

		destroyed() {

		}
	}
</script>

<style lang="less" >
	@import "../../../assets/styles/common.less";
	.tdt-infowindow-content {
		margin: 0;
	}
	.map-info {
		width: 348px;
		.map-info-head {
			padding: 4px 8px;
			font-size: 14px;
			color: #313C47;
			font-weight: bold;
			border-bottom: 1px solid #E8E8E8;
		}

		.map-info-body {
			padding: 8px;

			.@{FbUiPrefix}-property-item {
				padding-bottom: 2px;
			}
		}

		.@{FbUiPrefix}-tags {
			height: auto;
		}

		.gary-bg {
			padding: 5px;
			background: #F9F9F9;
			border-radius: 4px;
			margin-bottom: 4px;
		}
	}
</style>
