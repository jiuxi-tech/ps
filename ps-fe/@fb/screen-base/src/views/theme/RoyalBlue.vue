<template>
	<fb-container className="screen-page screen-page-index">
		<screen-header/>
		<fb-container height="1080px" relative>
<!--			<motion-point-wave size="18"></motion-point-wave>-->
<!--			<motion-rotate-star></motion-rotate-star>-->
<!--			<motion-star-line></motion-star-line>-->
			<motion-victor theme="primary"></motion-victor>
		</fb-container>

	</fb-container>
</template>

<script>
/**
 * index
 * (c) 2021 lincong1987
 */
import ScreenHeader from '../../components/ScreenHeader'
import ScreenPanel from "../../components/ScreenPanel";
import ScreenFlopBoard from "@/components/ScreenFlopBoard";
import ScreenPanelTitle from "@/components/ScreenPanelTitle";
import ScreenPanelLeft from "@/components/ScreenPanelLeft";
import ScreenPanelRight from "@/components/ScreenPanelRight";
export default {
	route: {
		path: '/royal-blue'
	},
	components: {
		ScreenPanelLeft,
		ScreenPanelRight,
		ScreenPanelTitle,
		ScreenFlopBoard,
		ScreenHeader,
		ScreenPanel
	},
	mounted() {
		this.$screenUtil.setTheme('royal-blue')
	},
	methods: {
		handleClick() {

		}
	}
}
</script>
<style lang="less" scoped>
</style>
