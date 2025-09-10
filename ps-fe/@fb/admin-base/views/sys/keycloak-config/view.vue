<template>
  <div class="page-container">
    <div class="page-header">
      <h2>Keycloak配置详情</h2>
      <div class="header-actions">
        <fb-button @on-click="handleBack">返回</fb-button>
        <fb-button type="primary" @on-click="handleEdit">编辑</fb-button>
      </div>
    </div>
    
    <div class="page-content" v-loading="loading">
      <div class="config-detail" v-if="configData">
        <div class="detail-section">
          <h3>基本信息</h3>
          <div class="detail-grid">
            <div class="detail-item">
              <label>配置键：</label>
              <span class="config-key">{{ configData.configKey }}</span>
            </div>
            <div class="detail-item">
              <label>配置值：</label>
              <span class="config-value" :class="{ 'is-sensitive': isSensitiveConfig(configData.configKey) }">
                <span v-if="isSensitiveConfig(configData.configKey) && !showSensitive">
                  ********
                  <fb-button type="text" size="small" @on-click="showSensitive = true">显示</fb-button>
                </span>
                <span v-else>
                  {{ configData.configValue }}
                  <fb-button v-if="isSensitiveConfig(configData.configKey)" type="text" size="small" @on-click="showSensitive = false">隐藏</fb-button>
                </span>
              </span>
            </div>
            <div class="detail-item full-width">
              <label>描述：</label>
              <span>{{ configData.description || '暂无描述' }}</span>
            </div>
            <div class="detail-item">
              <label>创建时间：</label>
              <span>{{ formatTime(configData.createTime) }}</span>
            </div>
            <div class="detail-item">
              <label>更新时间：</label>
              <span>{{ formatTime(configData.updateTime) }}</span>
            </div>
          </div>
        </div>
        
        <div class="detail-section" v-if="configUsage">
          <h3>配置说明</h3>
          <div class="usage-info">
            <div class="usage-item">
              <label>配置类型：</label>
              <span class="config-type">{{ configUsage.type }}</span>
            </div>
            <div class="usage-item">
              <label>默认值：</label>
              <span>{{ configUsage.defaultValue || '无' }}</span>
            </div>
            <div class="usage-item full-width">
              <label>说明：</label>
              <p class="usage-description">{{ configUsage.description }}</p>
            </div>
            <div class="usage-item full-width" v-if="configUsage.examples">
              <label>示例：</label>
              <div class="examples">
                <div v-for="(example, index) in configUsage.examples" :key="index" class="example-item">
                  <code>{{ example }}</code>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="detail-section">
          <h3>操作历史</h3>
          <div class="history-info">
            <p class="tip">配置变更历史记录功能开发中...</p>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 编辑弹窗 -->
    <fb-modal v-model="editModal.visible" title="编辑配置" width="600px">
      <fb-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="120px">
        <fb-form-item label="配置键" prop="configKey">
          <fb-input v-model="editForm.configKey" disabled />
        </fb-form-item>
        <fb-form-item label="配置值" prop="configValue">
          <fb-input 
            v-model="editForm.configValue" 
            :type="isSensitiveConfig(editForm.configKey) ? 'password' : 'text'"
            placeholder="请输入配置值" 
          />
        </fb-form-item>
        <fb-form-item label="描述" prop="description">
          <fb-input v-model="editForm.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </fb-form-item>
      </fb-form>
      <template #footer>
        <fb-button @on-click="editModal.visible = false">取消</fb-button>
        <fb-button type="primary" @on-click="handleSave">保存</fb-button>
      </template>
    </fb-modal>
  </div>
</template>

<script>
export default {
  name: 'KeycloakConfigView',
  data() {
    return {
      loading: false,
      configData: null,
      showSensitive: false,
      editModal: {
        visible: false
      },
      editForm: {
        configKey: '',
        configValue: '',
        description: ''
      },
      editRules: {
        configValue: [
          { required: true, message: '请输入配置值', trigger: 'blur' }
        ]
      },
      // 配置说明信息
      configUsageMap: {
        'keycloak.server-url': {
          type: 'URL',
          defaultValue: 'http://localhost:18080',
          description: 'Keycloak服务器地址，用于SSO认证和管理API调用',
          examples: ['http://localhost:18080', 'https://keycloak.example.com']
        },
        'keycloak.realm': {
          type: 'String',
          defaultValue: 'ps-realm',
          description: 'Keycloak Realm名称，用于隔离不同的用户域',
          examples: ['ps-realm', 'master', 'company-realm']
        },
        'keycloak.admin.client-id': {
          type: 'String',
          defaultValue: 'admin-cli',
          description: 'Keycloak管理员客户端ID，用于管理API调用',
          examples: ['admin-cli', 'ps-admin']
        },
        'keycloak.admin.username': {
          type: 'String',
          defaultValue: 'admin',
          description: 'Keycloak管理员用户名',
          examples: ['admin', 'keycloak-admin']
        },
        'keycloak.admin.password': {
          type: 'Password',
          defaultValue: 'admin123',
          description: 'Keycloak管理员密码（敏感信息）',
          examples: ['admin123', 'your-secure-password']
        },
        'keycloak.sso.client-id': {
          type: 'String',
          defaultValue: 'ps-be',
          description: 'SSO客户端ID，用于OAuth2认证',
          examples: ['ps-be', 'my-app']
        },
        'keycloak.sso.client-secret': {
          type: 'Password',
          defaultValue: '',
          description: 'SSO客户端密钥（敏感信息）',
          examples: ['xMXvDGzby4Z48szob7i2fuZlZy5Wlqrh']
        },
        'keycloak.sso.enabled': {
          type: 'Boolean',
          defaultValue: 'true',
          description: '是否启用SSO功能',
          examples: ['true', 'false']
        }
      }
    }
  },
  computed: {
    configUsage() {
      return this.configUsageMap[this.configData?.configKey] || null
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      const configKey = this.$route.query.configKey
      if (!configKey) {
        this.$message.error('缺少配置键参数')
        this.handleBack()
        return
      }
      
      this.loading = true
      try {
        const response = await this.$api.get('/sys/config/view', {
          params: { configKey }
        })
        if (response.code === 1) {
          this.configData = response.data
        } else {
          this.$message.error(response.message || '加载配置失败')
        }
      } catch (error) {
        console.error('加载配置失败:', error)
        this.$message.error('加载配置失败')
      } finally {
        this.loading = false
      }
    },
    
    // 返回
    handleBack() {
      this.$router.go(-1)
    },
    
    // 编辑
    handleEdit() {
      this.editForm = {
        configKey: this.configData.configKey,
        configValue: this.configData.configValue,
        description: this.configData.description || ''
      }
      this.editModal.visible = true
    },
    
    // 保存
    async handleSave() {
      try {
        await this.$refs.editFormRef.validate()
        
        const response = await this.$api.post('/sys/config/save', this.editForm)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.editModal.visible = false
          this.loadData()
        } else {
          this.$message.error(response.message || '保存失败')
        }
      } catch (error) {
        console.error('保存失败:', error)
        this.$message.error('保存失败')
      }
    },
    
    // 判断是否为敏感配置
    isSensitiveConfig(configKey) {
      const sensitiveKeys = ['password', 'secret', 'key', 'token']
      return sensitiveKeys.some(key => configKey?.toLowerCase().includes(key))
    },
    
    // 格式化时间
    formatTime(time) {
      if (!time) return '-'
      return new Date(time).toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.page-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e8e8e8;
}

.page-header h2 {
  margin: 0;
  color: #333;
}

.header-actions .fb-button {
  margin-left: 8px;
}

.page-content {
  background: #fff;
  border-radius: 4px;
  padding: 24px;
}

.detail-section {
  margin-bottom: 32px;
}

.detail-section:last-child {
  margin-bottom: 0;
}

.detail-section h3 {
  margin: 0 0 16px 0;
  color: #333;
  font-size: 16px;
  font-weight: 600;
  border-bottom: 2px solid #409eff;
  padding-bottom: 8px;
}

.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.detail-item {
  display: flex;
  align-items: flex-start;
}

.detail-item.full-width {
  grid-column: 1 / -1;
}

.detail-item label {
  min-width: 80px;
  color: #666;
  font-weight: 500;
  margin-right: 12px;
}

.detail-item span {
  color: #333;
  word-break: break-all;
}

.config-key {
  font-family: 'Courier New', monospace;
  background: #f5f5f5;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 14px;
}

.config-value {
  font-family: 'Courier New', monospace;
  background: #f8f9fa;
  padding: 8px 12px;
  border-radius: 4px;
  border: 1px solid #e9ecef;
  min-height: 20px;
  display: inline-block;
  min-width: 200px;
}

.config-value.is-sensitive {
  background: #fff3cd;
  border-color: #ffeaa7;
}

.usage-info {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.usage-item {
  display: flex;
  align-items: flex-start;
}

.usage-item.full-width {
  grid-column: 1 / -1;
  flex-direction: column;
}

.usage-item label {
  min-width: 80px;
  color: #666;
  font-weight: 500;
  margin-right: 12px;
}

.config-type {
  background: #e7f3ff;
  color: #409eff;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.usage-description {
  margin: 8px 0 0 0;
  color: #333;
  line-height: 1.6;
}

.examples {
  margin-top: 8px;
}

.example-item {
  margin-bottom: 8px;
}

.example-item code {
  background: #f5f5f5;
  padding: 4px 8px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 13px;
  color: #e83e8c;
}

.history-info .tip {
  color: #999;
  font-style: italic;
  margin: 0;
}
</style>