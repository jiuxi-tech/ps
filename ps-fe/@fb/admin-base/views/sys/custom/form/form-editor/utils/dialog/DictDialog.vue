<template>
	<fb-dialog ref="dialog" title="请复制下方的字典值使用">
		<fb-container>

			<fb-container>
				<fb-input v-model="dicNameOrCode" placeholder="请输入字典名称或编码">
					<fb-button slot="append-button" @on-click="handleQuery" type="primary"
					           icon="search"></fb-button>
				</fb-input>
			</fb-container>

			<fb-container mt="8px">
				<fb-list
					ref="list"
					:container-style="{height: '450px', overflowY: 'auto'}"
					:pager-align="'center'"
					:split-line="false"
					:service="service.list">
					<fb-list-item slot="item" slot-scope="{item, index}" style="border-bottom: 1px solid #eee">
						<fb-list-item-meta :description="item.dicName">
							<fb-input slot="title" width="120px" :value="item.dicCode"></fb-input>
						</fb-list-item-meta>
						<fb-tag slot="extra" :type="item.enabled == 1 ? 'success': 'danger'" effect="light">
							{{ item.enabled == 1 ? '已启用' : '未启用' }}{{ item.enable }}
						</fb-tag>
					</fb-list-item>
				</fb-list>

			</fb-container>

		</fb-container>

	</fb-dialog>
</template>

<script>
/**
 * DictDialog
 * (c) 2021 lincong1987
 */

export default {
	name: 'DictDialog',
	props: {},
	data () {
		return {
			service: app.$svc.sys.dict,
			dicNameOrCode: '',
		}
	},
	methods: {

		show () {
			this.dicNameOrCode = ''
			this.$refs.dialog.show()
		},
		handleQuery () {
			this.$refs.list.doSearch({
				'dicName': this.dicNameOrCode,
				'dicCode': this.dicNameOrCode,
			})
		},
	},

}
</script>

<style lang="less" scoped>

</style>
