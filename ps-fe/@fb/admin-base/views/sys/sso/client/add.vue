<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform">
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="应用ID" prop="clientId" :rule="[{required: true}]">
							<fb-input v-model="formData.clientId" placeholder="请输入应用ID" :disabled="isEdit || readonly"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="应用名称" prop="name">
							<fb-input v-model="formData.name" placeholder="请输入应用名称" :disabled="readonly"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="描述" prop="description">
							<fb-textarea rows="2" v-model="formData.description"
										 type="text"
										 placeholder="请输入应用描述"
										 :maxlength="200"
										 :disabled="readonly">
							</fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="协议" prop="protocol">
							<fb-select v-model="formData.protocol" placeholder="请选择协议" :disabled="readonly" :data="[
								{value: 'openid-connect', label: 'OpenID Connect'},
								{value: 'saml', label: 'SAML'}
							]">
							</fb-select>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="应用类型" prop="publicClient">
							<fb-select v-model="formData.publicClient" placeholder="请选择应用类型" :disabled="readonly"
							:data="[{
								value: false,
								label: '机密应用'
							},{
								value: true,
								label: '公共应用'
							}]"
							>
							</fb-select>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="启用状态" prop="enabled">
							<fb-switch v-model="formData.enabled" :disabled="readonly"></fb-switch>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="标准流程" prop="standardFlowEnabled">
							<fb-switch v-model="formData.standardFlowEnabled" :disabled="readonly"></fb-switch>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="隐式流程" prop="implicitFlowEnabled">
							<fb-switch v-model="formData.implicitFlowEnabled" :disabled="readonly"></fb-switch>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="直接访问授权" prop="directAccessGrantsEnabled">
							<fb-switch v-model="formData.directAccessGrantsEnabled" :disabled="readonly"></fb-switch>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="基础URL" prop="baseUrl">
							<fb-input v-model="formData.baseUrl" placeholder="请输入基础URL" :disabled="readonly"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="重定向URI 支持多行" prop="redirectUris">
							<fb-textarea rows="3" v-model="redirectUrisText"
										 type="text"
										 placeholder="请输入重定向URI，多个URI请用换行分隔"
										 :disabled="readonly">
							</fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="Web Origins" prop="webOrigins">
							<fb-textarea rows="2" v-model="webOriginsText"
										 type="text"
										 placeholder="请输入Web Origins，多个Origins请用换行分隔"
										 :disabled="readonly">
							</fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row v-if="!formData.publicClient">
					<fb-col span="24">
						<fb-form-item label="应用密钥" prop="secret">
							<fb-input v-model="formData.secret" placeholder="应用密钥（机密应用必填）" :disabled="readonly"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

			</fb-form>
		</div>

		<div class="tp-dialog-bottom" v-if="!readonly">
			<fb-button style="margin-right: 12px" type="primary" @on-click="save">保存</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
		<div class="tp-dialog-bottom" v-else>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>
	export default {
		name: 'ClientAdd',
		mixins: [],
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
		// 创建方法
		created() {
			// 记录原来的默认值，用于表单重置
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化
			this.init(this.param);
		},
		data() {
			return {
				// 表单数据
				formData: {
					id: '',
					clientId: '',
					name: '',
					description: '',
					protocol: 'openid-connect',
					publicClient: false,
					enabled: true,
					standardFlowEnabled: true,
					implicitFlowEnabled: false,
					directAccessGrantsEnabled: true,
					baseUrl: '',
					redirectUris: [],
					webOrigins: [],
					secret: ''
				},
				isEdit: false,
				readonly: false,
				redirectUrisText: '',
				webOriginsText: ''
			}
		},

		watch: {
			// 监听重定向URI文本变化
			redirectUrisText(newVal) {
				this.formData.redirectUris = newVal ? newVal.split('\n').filter(uri => uri.trim()) : [];
			},
			// 监听Web Origins文本变化
			webOriginsText(newVal) {
				this.formData.webOrigins = newVal ? newVal.split('\n').filter(origin => origin.trim()) : [];
			}
		},

		// 方法
		methods: {
			// 设置标题
			init(param) {
				if (param) {
					this.readonly = param.readonly || false;
					
					if (param.id) {
						this.isEdit = true;
						this.formData.id = param.id;
						this.loadClientData(param.id);
					}
				}
			},

			// 加载应用数据
			loadClientData(clientId) {
				app.$svc.sys.sso.admin.client.get(clientId).then((result) => {
					if (result.code == 1) {
						const data = result.data;
						this.formData = {
							id: data.id,
							clientId: data.clientId,
							name: data.name || '',
							description: data.description || '',
							protocol: data.protocol || 'openid-connect',
							publicClient: data.publicClient || false,
							enabled: data.enabled !== false,
							standardFlowEnabled: data.standardFlowEnabled !== false,
							implicitFlowEnabled: data.implicitFlowEnabled || false,
							directAccessGrantsEnabled: data.directAccessGrantsEnabled !== false,
							baseUrl: data.baseUrl || '',
							redirectUris: data.redirectUris || [],
							webOrigins: data.webOrigins || [],
							secret: data.secret || ''
						};
						
						// 设置文本域内容
						this.redirectUrisText = (data.redirectUris || []).join('\n');
						this.webOriginsText = (data.webOrigins || []).join('\n');
					} else {
						this.$message.error('获取应用信息失败: ' + result.message);
					}
				}).catch((err) => {
					console.error('获取应用信息失败', err);
					this.$message.error('获取应用信息失败');
				});
			},

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog("close");
			},
			
			// 保存
			save() {
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						// 准备保存数据
						const saveData = {
							...this.formData
						};
						
						// 如果是新增，删除id字段
						if (!this.isEdit) {
							delete saveData.id;
						}
						
						// 调用保存接口
				app.$svc.sys.sso.admin.client.save(saveData).then((result) => {
							if (result.code == 1) {
								this.$message.success(this.isEdit ? '修改成功' : '新增成功');
								this.handleClose();
							} else {
								this.$message.error((this.isEdit ? '修改' : '新增') + '失败: ' + result.message);
							}
						}).catch((err) => {
							console.error('保存应用失败', err);
							this.$message.error('保存应用失败');
						});
					}
				})
			}
		}
	}
</script>

<style lang="less" scoped>
	.tp-dialog {
		padding: 20px;
	}
	
	.tp-dialog-top {
		max-height: 500px;
		overflow-y: auto;
	}
	
	.tp-dialog-bottom {
		margin-top: 20px;
		text-align: center;
		border-top: 1px solid #e8e8e8;
		padding-top: 15px;
	}
</style>