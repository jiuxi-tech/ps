<!-- 选择部门 -->
<template>
	<transition name="slide-to-down">
		<div class="login-dialog">
			<div class="department-form">
				<div class="department-form__caption">
					选择部门
				</div>
				<fb-list-roll fatherClass="department-form-body" mode="y">
					<div class="department-form-body_item" @click="itemClick(item, idx)"
						 v-for="(item, idx) in departmentList" :key="idx">
						<fb-icon name="node" color="#999"></fb-icon>
							{{item[fieldObj.label]}}
						{{item.isSelect}}
						<fb-icon v-if="!multiple" name="selected" size="12" v-show="item[fieldObj.id] === id"></fb-icon>
						<fb-icon v-if="multiple" name="selected" size="12" v-show="item[fieldObj.id] === ids[idx]"></fb-icon>
					</div>
				</fb-list-roll>
				<fb-button
					type="primary"
					size="l"
					long
					@on-click="confirmDepartment">
					确 定
				</fb-button>
			</div>
		</div>
	</transition>
</template>

<script>
	/**
	 * DefaultApplicationLogin
	 * (c) 2020 lincong1987
	 */

	export default {

		props: {
			departmentList: {
				type: Array,
				default: () => []
			},
			fieldObj: {
				type: Object,
				default: () => {}
			},
			// 多选 暂未完成
			multiple: {
				type: Boolean,
				default: false
			}
		},

		data () {
			return {
				id: '',
				ids: []
			}
		},

		watch: {

		},

		created () {

		},

		methods: {
			confirmDepartment () {
				let value = this.id
				if (this.multiple) {
					value = this.ids
				}
				this.$emit('confirmDepartment', value)
			},
			itemClick (item, idx) {
				if (this.multiple) {
					for (let i = 0; i < this.ids.length; i++) {
						if (this.ids[i] == item[this.fieldObj.id]) {
							this.ids.splice(i, 1, null)
							return false
						}
					}
					if (this.ids.length === this.departmentList.length) {
						this.ids.splice(idx, 1, item[this.fieldObj.id])
					} else {
						this.ids.push(item[this.fieldObj.id])
					}
				} else {
					this.id = item[this.fieldObj.id]
				}
			},
			destroy () {
				this.id = ''
				this.ids = []
			}
		},

		destroyed () {
			this.destroy()
		},
		deactivated () {
			this.destroy()
		}


	}
</script>

<style lang="less" scoped>
	@import ~"../../assets/styles/login/login-card";

	.department-form {
		padding: 0 24px;

		&__caption {
			height:      32px;
			font-size:   24px;
			color:       #313C47;
			line-height: 32px;
			padding:     32px 0;
			text-align:  center;
			margin-bottom: 24px;
		}

		.department-form-body {
			height: 240px;
			margin-bottom: 24px;

			.department-form-body_item {
				color: #313C47;
				height: 36px;
				line-height: 36px;
				background: #F0EFF5;
				border-radius: 4px;
				padding: 0 32px;
				position: relative;
				margin-bottom: 24px;

				&:hover {
					opacity: .8;
					cursor: pointer;
				}

				.fb-icon {
					position: absolute;
					top: 10px;
					left: 9px;

					&:last-child {
						left: unset;
						right: 9px;
						background: rgb(86, 209, 0);
						color: rgb(255, 255, 255);
						padding: 3px;
						border-radius: 50%;
						top: 9px;
					}
				}
			}
		}
	}
</style>
