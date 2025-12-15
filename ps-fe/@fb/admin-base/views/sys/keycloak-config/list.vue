<template>
  <div class="page-container">
    <div class="page-header">
      <h2>Keycloak配置管理</h2>
      <p>管理Keycloak SSO相关配置项</p>
    </div>
    
    <div class="page-content">
      <!-- 搜索区域 -->
      <div class="search-area">
        <fb-form :model="searchForm" inline>
          <fb-form-item label="配置键">
            <fb-input v-model="searchForm.configKey" placeholder="请输入配置键" clearable />
          </fb-form-item>
          <fb-form-item label="配置值">
            <fb-input v-model="searchForm.configValue" placeholder="请输入配置值" clearable />
          </fb-form-item>
          <fb-form-item>
            <fb-button type="primary" @on-click="handleSearch">搜索</fb-button>
            <fb-button @on-click="handleReset">重置</fb-button>
          </fb-form-item>
        </fb-form>
      </div>
      
      <!-- 操作按钮区域 -->
      <div class="action-area">
        <fb-button type="primary" @on-click="handleAdd">新增配置</fb-button>
        <fb-button type="success" @on-click="handleBatchUpdate">批量更新</fb-button>
        <fb-button type="warning" @on-click="handleExport">导出配置</fb-button>
      </div>
      
      <!-- 表格区域 -->
      <div class="table-area">
        <fb-simple-table
          :columns="columns"
          :data="tableData"
          :loading="loading"
          :pagination="pagination"
          @on-page-change="handlePageChange"
          @on-page-size-change="handlePageSizeChange"
        >
          <template #action="{ row }">
            <fb-button type="text" size="small" @on-click="handleEdit(row)">编辑</fb-button>
            <fb-button type="text" size="small" @on-click="handleView(row)">查看</fb-button>
            <fb-button type="text" size="small" style="color: #f56c6c" @on-click="handleDelete(row)">删除</fb-button>
          </template>
        </fb-simple-table>
      </div>
    </div>
    
    <!-- 编辑弹窗 -->
    <fb-modal v-model="editModal.visible" :title="editModal.title" width="600px">
      <fb-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="120px">
        <fb-form-item label="配置键" prop="configKey">
          <fb-input v-model="editForm.configKey" :disabled="editModal.isEdit" placeholder="请输入配置键" />
        </fb-form-item>
        <fb-form-item label="配置值" prop="configValue">
          <fb-input v-model="editForm.configValue" placeholder="请输入配置值" />
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
    
    <!-- 批量更新弹窗 -->
    <fb-modal v-model="batchModal.visible" title="批量更新配置" width="800px">
      <div class="batch-update-area">
        <p class="tip">请在下方文本框中输入配置项，格式：配置键=配置值，每行一个配置项</p>
        <fb-input
          v-model="batchForm.configText"
          type="textarea"
          :rows="10"
          placeholder="例如：&#10;keycloak.server-url=http://localhost:18080&#10;keycloak.realm=ps-realm&#10;keycloak.admin.username=admin"
        />
      </div>
      <template #footer>
        <fb-button @on-click="batchModal.visible = false">取消</fb-button>
        <fb-button type="primary" @on-click="handleBatchSave">批量保存</fb-button>
      </template>
    </fb-modal>
  </div>
</template>

<script>
export default {
  name: 'KeycloakConfigList',
  data() {
    return {
      loading: false,
      searchForm: {
        configKey: '',
        configValue: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      columns: [
        {
          title: '配置键',
          key: 'configKey',
          width: 250,
          ellipsis: true
        },
        {
          title: '配置值',
          key: 'configValue',
          width: 200,
          ellipsis: true
        },
        {
          title: '描述',
          key: 'description',
          ellipsis: true
        },
        {
          title: '更新时间',
          key: 'updateTime',
          width: 160
        },
        {
          title: '操作',
          key: 'action',
          width: 150,
          slot: 'action'
        }
      ],
      editModal: {
        visible: false,
        title: '新增配置',
        isEdit: false
      },
      editForm: {
        configKey: '',
        configValue: '',
        description: ''
      },
      editRules: {
        configKey: [
          { required: true, message: '请输入配置键', trigger: 'blur' }
        ],
        configValue: [
          { required: true, message: '请输入配置值', trigger: 'blur' }
        ]
      },
      batchModal: {
        visible: false
      },
      batchForm: {
        configText: ''
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          current: this.pagination.current,
          size: this.pagination.size
        }
        
        // 只查询keycloak相关配置
        if (!params.configKey) {
          params.configKey = 'keycloak'
        }
        
        const response = await this.$api.get('/sys/config/list', { params })
        if (response.code === 1) {
          this.tableData = response.data.records || []
          this.pagination.total = response.data.total || 0
          this.pagination.current = response.data.current || 1
        } else {
          this.$message.error(response.message || '加载数据失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    
    // 重置
    handleReset() {
      this.searchForm = {
        configKey: '',
        configValue: ''
      }
      this.pagination.current = 1
      this.loadData()
    },
    
    // 分页变化
    handlePageChange(page) {
      this.pagination.current = page
      this.loadData()
    },
    
    // 页大小变化
    handlePageSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },
    
    // 新增
    handleAdd() {
      this.editModal = {
        visible: true,
        title: '新增配置',
        isEdit: false
      }
      this.editForm = {
        configKey: '',
        configValue: '',
        description: ''
      }
    },
    
    // 编辑
    handleEdit(row) {
      this.editModal = {
        visible: true,
        title: '编辑配置',
        isEdit: true
      }
      this.editForm = {
        configKey: row.configKey,
        configValue: row.configValue,
        description: row.description || ''
      }
    },
    
    // 查看
    handleView(row) {
      this.$router.push({
        path: '/sys/keycloak-config/view',
        query: { configKey: row.configKey }
      })
    },
    
    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除这个配置项吗？', '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          const response = await this.$api.delete(`/sys/config/delete/${row.configKey}`)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      })
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
    
    // 批量更新
    handleBatchUpdate() {
      this.batchModal.visible = true
      this.batchForm.configText = ''
    },
    
    // 批量保存
    async handleBatchSave() {
      if (!this.batchForm.configText.trim()) {
        this.$message.warn('请输入配置内容')
        return
      }
      
      try {
        const lines = this.batchForm.configText.split('\n')
        const configs = {}
        
        for (const line of lines) {
          const trimmedLine = line.trim()
          if (trimmedLine && trimmedLine.includes('=')) {
            const [key, ...valueParts] = trimmedLine.split('=')
            const value = valueParts.join('=')
            if (key.trim() && value.trim()) {
              configs[key.trim()] = value.trim()
            }
          }
        }
        
        if (Object.keys(configs).length === 0) {
          this.$message.warn('没有找到有效的配置项')
          return
        }
        
        const response = await this.$api.post('/sys/config/batch-update', configs)
        if (response.code === 1) {
          this.$message.success('批量更新成功')
          this.batchModal.visible = false
          this.loadData()
        } else {
          this.$message.error(response.message || '批量更新失败')
        }
      } catch (error) {
        console.error('批量更新失败:', error)
        this.$message.error('批量更新失败')
      }
    },
    
    // 导出配置
    async handleExport() {
      try {
        const response = await this.$api.get('/sys/config/all')
        if (response.code === 1) {
          const configs = response.data
          const keycloakConfigs = Object.keys(configs)
            .filter(key => key.startsWith('keycloak'))
            .reduce((obj, key) => {
              obj[key] = configs[key]
              return obj
            }, {})
          
          const configText = Object.entries(keycloakConfigs)
            .map(([key, value]) => `${key}=${value}`)
            .join('\n')
          
          const blob = new Blob([configText], { type: 'text/plain' })
          const url = window.URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = 'keycloak-config.txt'
          a.click()
          window.URL.revokeObjectURL(url)
          
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    }
  }
}
</script>

<style scoped>
.page-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #333;
}

.page-header p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.search-area {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 4px;
  margin-bottom: 16px;
}

.action-area {
  margin-bottom: 16px;
}

.action-area .fb-button {
  margin-right: 8px;
}

.table-area {
  background: #fff;
  border-radius: 4px;
}

.batch-update-area .tip {
  margin-bottom: 12px;
  color: #666;
  font-size: 14px;
}
</style>