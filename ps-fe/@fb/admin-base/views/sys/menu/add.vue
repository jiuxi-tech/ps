<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="140" v-if="this.inited">
 
					<fb-row>
					<fb-col span="20">
						<fb-form-item label="菜单类型" prop="menuType" :rule="[{required: true}]">
							<fb-tooltip slot="label-extra" placement="top">
								<fb-container slot="content" width="260px">'菜单分类节点的菜单不会展示给用户，且保存后不能修改为其它菜单类型'</fb-container>
								<fb-icon name="information"/>
							</fb-tooltip>
							<fb-select v-model="formData.menuType"
									   :service="$svc.sys.dict.select"
									   :param="{'pdicCode': 'SYS19'}"
									   @on-data-load="handleDataLoad"
									   :disabled="formData.menuType == 'SYS1907' && formData.menuId != ''"
									   placeholder="请选择"
									   clearable/>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="20">
						<fb-form-item :label="`菜单名称`" prop="menuName" :rule="[{required: true}]">
							<fb-input v-model="formData.menuName"
									  placeholder="请输入菜单名称">
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="20">
						<fb-form-item label="菜单编码">
							<fb-tooltip slot="label-extra" placement="top">
								<fb-container slot="content" width="260px">'请在父级编码的后面追加'</fb-container>
								<fb-icon name="information"/>
							</fb-tooltip>
							<fb-input v-model="formData.menuCode"
									  placeholder="请输入菜单编码">
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
			
				<fb-row v-show="showMenuUri()">
					<fb-col span="20">
						<fb-form-item label="菜单url" prop="menuUri">
							<fb-input v-model="formData.menuUri"
									  placeholder="请输入菜单url">
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="20">
						<fb-form-item label="菜单归属 (app/pc)" prop="menuSource"
									  :rule='{required: true, type: "integer"}'>
							<fb-select v-model.number="formData.menuSource"
									   :data="[{value:1, label: 'pc'}, {value:2, label: 'app'}]"
									   :placeholder="'请选择'"
									   clearable/>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row v-show="showMenuIcon()">
					<fb-col span="20">
						<fb-form-item label="菜单图标" prop="menuIcon">
							<!--<fb-input v-model="formData.menuIcon"
									  placeholder="请输入菜单图标">-->
							<fb-icon-select v-model="formData.menuIcon"></fb-icon-select>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="20">
						<fb-form-item label="菜单介绍">
							<fb-textarea rows="2"
										 v-model="formData.menuDesc"
										 type="text"
										 placeholder="请输入内容"
										 :maxlength="200">
							</fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="20">
						<fb-form-item label="排序" prop="orderIndex" :rule='{required: true}'>
							<fb-input v-model="formData.orderIndex" placeholder="请输入排序号"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="20">
						<fb-form-item label="是否启用">
							<fb-radio-group v-model="formData.enabled"
											:data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}"></fb-radio-group>
						</fb-form-item>
					</fb-col>
				</fb-row>
			</fb-form>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="add">保存</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>


<script>



	export default {
		mixins: [

		],
		// 接收父组件的传参
		props: {
			param: {
				type: Object,
				require: false
			},
			parentPage: {
				type: Object,
				default: null
			}
		},
		// 组件
		components: {
			// 'component-a': ComponentA,

		},
		// 创建方法
		created() {
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化
			this.init(this.param);
		},
		data() {
			return {
				inited: false,
				// 请求的 service
				service: this.$svc.sys.menu,
				menuParam: {
					menuTreePid: '',
					menuId: '',
				},
				// 表单数据
				formData: {
					menuId: '',
					menuName: '',
					menuCode: '',
					menuUri: '',
					menuTreePid: '',
					menuSource: 1,
					menuType: [],
					menuIcon: '',
					menuDesc: '',
					orderIndex: 0.0,
					enabled: 1,
				},
			}
		},

		// 方法
		methods: {
			/**
			 * 显示窗口
			 * param 参数
			 */
			init(param) {
				// 有值表示是修改方法
				if (param.id) {
					// 修改
					this.formData.menuId = param.id;
					this.view()
				} else {
					// 新增
					this.formData.menuTreePid = param.menuPid;
					if (param.menuCode) {
						this.formData.menuCode = param.menuCode;
					}
					this.inited = true;
				}
			},
			// 查询信息
			view() {
				// 调用新增service方法
				this.service.view({"menuId": this.formData.menuId}).then((result) => {

					// 判断code
					if (result.code == 1) {
						this.formData = result.data;
						this.inited = true;
					} else {
						// 服务器返回失败
						this.$message.error('错误提示:' + result.message)
					}
				})
			},
			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog();
			},
			// 判断是否显示menuUri
			showMenuUri(){
				// 按钮和菜单分类节点时，不显示menuUri
				if (this.formData.menuType == 'SYS1907'){
					return false;
				}
				return true;
			},
			// 判断是否显示icon
			showMenuIcon(){
				// 菜单和大屏菜单时，显示icon
				if (this.formData.menuType == 'SYS1901' || this.formData.menuType == 'SYS1906'){
					return true;
				}
				return false;
			},
			add() {
				let originalUri = this.formData.menuUri;
				// url编码 - 只编码一次，避免双重编码
				if (this.formData.menuUri) {
					this.formData.menuUri = encodeURIComponent(this.formData.menuUri);
				}
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						// 创建清理后的数据副本，过滤null值和无效参数
						const cleanFormData = this.cleanFormData(this.formData);
						
						if (this.formData.menuId) {
							// 修改
							this.service.update(cleanFormData).then((result) => {
								// 判断code
								if (result && result.code == 1) {
									this.$message.success('修改成功');
									// 关闭，并传递参数
									this.closeTpDialog(result.data.menuId);
								} else {
									// 报错后需要还原
									this.formData.menuUri = originalUri;
									if (result && result.message) {
										this.$message.error('修改失败: ' + result.message);
									}
								}
							}).catch((error) => {
								// 异常处理
								this.formData.menuUri = originalUri;
								this.$message.error('修改失败，请检查参数是否正确');
								console.error('Menu update error:', error);
							})
						} else {
							// 新增
							this.service.add(cleanFormData).then((result) => {
								// 判断code
								if (result && result.code == 1) {
									this.$message.success('新增成功');
									// 关闭，并传递参数
									this.closeTpDialog(result.data.menuId);
								} else {
									// 报错后需要还原
									this.formData.menuUri = originalUri;
									if (result && result.message) {
										this.$message.error('新增失败: ' + result.message);
									}
								}
							}).catch((error) => {
								// 异常处理
								this.formData.menuUri = originalUri;
								this.$message.error('新增失败，请检查参数是否正确');
								console.error('Menu add error:', error);
							})
						}

					}
				})
			},

			/**
			 * 清理表单数据，过滤null值和无效参数
			 * 避免参数绑定失败
			 */
			cleanFormData(rawData) {
				const cleanedData = {};
				
				// 遍历原始数据
				for (const key in rawData) {
					if (rawData.hasOwnProperty(key)) {
						const value = rawData[key];
						
						// 过滤条件：
						// 1. 不为null和undefined
						// 2. 不为字符串'null'
						// 3. 不为空字符串（除非是必须的字段）
						if (value !== null && 
							value !== undefined && 
							value !== 'null' && 
							value !== '') {
							cleanedData[key] = value;
						} else if (this.isRequiredField(key) && value === '') {
							// 必需字段即使为空字符串也保留（后端会进行验证）
							cleanedData[key] = value;
						}
					}
				}
				
				// 确保orderIndex为Double类型
				if (cleanedData.orderIndex !== undefined) {
					cleanedData.orderIndex = parseFloat(cleanedData.orderIndex) || 0.0;
				}
				
				// 确保menuSource为Integer类型
				if (cleanedData.menuSource !== undefined) {
					cleanedData.menuSource = parseInt(cleanedData.menuSource) || 1;
				}
				
				console.log('原始表单数据:', rawData);
				console.log('清理后数据:', cleanedData);
				
				return cleanedData;
			},

			/**
			 * 判断是否为必需字段
			 */
			isRequiredField(fieldName) {
				const requiredFields = ['menuId', 'menuName', 'menuTreePid', 'menuSource', 'menuType', 'orderIndex'];
				return requiredFields.includes(fieldName);
			},

		    // 下拉选项数据加载完成后
			handleDataLoad(json) {
				// 修改时
				if (this.formData.menuId){
					// 如果之前选择的是“分类节点则不处理”
					if (this.formData.menuType == 'SYS1907'){
						return
					}
					// 将选项中分类节点移除
					let options = [];
					for (let i = 0; i < json.data.length; i++) {
						let row = json.data[i];
						if (row.value == 'SYS1907'){
							continue;
						}
						options.push(row);
					}
					json.data = options;
				}
			},

		}
	}
</script>

<style lang="less" scoped>

</style>
