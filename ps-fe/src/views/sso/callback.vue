<template>
  <div class="sso-callback">
    <div class="loading-container">
      <fb-spin size="large" :spinning="true">
        <div class="loading-content">
          <fb-icon name="loading" size="32" class="loading-icon"></fb-icon>
          <h3>正在处理SSO登录...</h3>
          <p>{{ statusMessage }}</p>
        </div>
      </fb-spin>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SSOCallback',
  data() {
    return {
      statusMessage: '正在验证身份信息...'
    }
  },
  mounted() {
    this.handleCallback()
  },
  methods: {
    async handleCallback() {
      try {
        // 获取URL参数
        const urlParams = new URLSearchParams(window.location.search)
        const code = urlParams.get('code')
        const state = urlParams.get('state')
        const error = urlParams.get('error')
        const errorDescription = urlParams.get('error_description')

        // 检查是否有错误
        if (error) {
          console.error('SSO登录错误:', error, errorDescription)
          this.statusMessage = '登录失败: ' + (errorDescription || error)
          setTimeout(() => {
            this.$router.replace(this.$datax.get('GLOBAL_CONFIG').loginPath)
          }, 3000)
          return
        }

        // 检查是否有授权码
        if (!code) {
          console.error('未获取到授权码')
          this.statusMessage = '登录失败: 未获取到授权码'
          setTimeout(() => {
            this.$router.replace(this.$datax.get('GLOBAL_CONFIG').loginPath)
          }, 3000)
          return
        }

        this.statusMessage = '正在获取访问令牌...'

        // 调用后端接口交换token
        const result = await app.service.post('/api/sso/callback', {
          code: code,
          state: state
        })

        if (result.code === 1 && result.data && result.data.token) {
          this.statusMessage = '登录成功，正在跳转...'
          
          // 保存token到本地存储
          this.$datax.set('token', result.data.token)
          
          // 保存用户信息（如果有）
          if (result.data.userInfo) {
            this.$datax.set('userInfo', result.data.userInfo)
          }
          
          // 清除URL参数
          window.history.replaceState({}, document.title, window.location.pathname)
          
          // 跳转到主页
          setTimeout(() => {
            this.$router.replace(this.$datax.get('GLOBAL_CONFIG').mainPath)
          }, 1000)
        } else {
          console.error('SSO回调处理失败:', result)
          this.statusMessage = '登录失败: ' + (result.message || '服务器错误')
          setTimeout(() => {
            this.$router.replace(this.$datax.get('GLOBAL_CONFIG').loginPath)
          }, 3000)
        }
      } catch (error) {
        console.error('SSO回调处理异常:', error)
        this.statusMessage = '登录失败: 网络错误或服务器异常'
        setTimeout(() => {
          this.$router.replace(this.$datax.get('GLOBAL_CONFIG').loginPath)
        }, 3000)
      }
    }
  }
}
</script>

<style lang="less" scoped>
.sso-callback {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  
  .loading-container {
    background: white;
    border-radius: 12px;
    padding: 40px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
    text-align: center;
    min-width: 320px;
    
    .loading-content {
      h3 {
        margin: 16px 0 8px 0;
        color: #333;
        font-size: 18px;
        font-weight: 500;
      }
      
      p {
        margin: 0;
        color: #666;
        font-size: 14px;
      }
      
      .loading-icon {
        color: #667eea;
        animation: spin 1s linear infinite;
      }
    }
  }
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>