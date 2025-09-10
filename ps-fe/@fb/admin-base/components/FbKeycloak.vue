<template>
  <div class="fb-keycloak-wrapper">
    <!-- 加载状态 -->
    <div v-if="authState === 'loading'" class="auth-loading">
      <div class="loading-spinner">
        <div class="spinner"></div>
      </div>
      <p class="loading-text">正在验证身份...</p>
    </div>

    <!-- 认证失败状态 -->
    <div v-else-if="authState === 'error'" class="auth-error">
      <div class="error-icon">
        <svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="32" cy="32" r="28" stroke="#ff6b6b" stroke-width="3" fill="#fff5f5"/>
          <circle cx="32" cy="32" r="20" fill="#ff6b6b" opacity="0.1"/>
          <path d="M32 20v16M32 44h.01" stroke="#ff6b6b" stroke-width="3" stroke-linecap="round"/>
        </svg>
      </div>
      <h3 class="error-title">认证失败</h3>
      <p class="error-message">{{ errorMessage }}</p>
      <div class="error-actions">
        <fb-button type="primary" @on-click="handleRetry" :loading="retrying">
          {{ retrying ? '重试中...' : '重新认证' }}
        </fb-button>
        <fb-button v-if="!autoLogin" type="default" @on-click="handleLogin">
          手动登录
        </fb-button>
      </div>
    </div>

    <!-- 未认证状态 -->
    <div v-else-if="authState === 'unauthenticated'" class="auth-unauthenticated">
      <div class="login-prompt">
        <div class="login-icon">
          <svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="32" cy="32" r="28" stroke="#1890ff" stroke-width="3" fill="#f0f8ff"/>
            <path d="M24 32h16M32 24v16" stroke="#1890ff" stroke-width="3" stroke-linecap="round"/>
          </svg>
        </div>
        <h3 class="login-title">需要登录</h3>
        <p class="login-message">请登录以继续使用系统</p>
        <fb-button type="primary" @on-click="handleLogin" :loading="loggingIn">
          {{ loggingIn ? '登录中...' : '立即登录' }}
        </fb-button>
      </div>
    </div>

    <!-- 认证成功状态 - 渲染插槽内容 -->
    <div v-else-if="authState === 'authenticated'" class="auth-success">
      <slot name="authenticated" :user="userData" :token="userToken" :keycloak="keycloakInstance">
        <!-- 默认内容 -->
        <div class="default-authenticated-content">
          <p>欢迎，{{ userData.name || userData.preferred_username }}！</p>
        </div>
      </slot>
    </div>
  </div>
</template>

<script>
import { resetKeycloakInstance, setKeycloakConfig, setKeycloakInitConfig, getKeycloakInstance, isAuthenticated, getUserInfo, getToken, login, logout } from '@fb/fb-core/src/util/keycloak'

export default {
  name: 'FbKeycloak',
  props: {
    // 自动登录配置
    autoLogin: {
      type: Boolean,
      default: true
    },
    // Keycloak配置参数
    keycloakConfig: {
      type: Object,
      default: () => ({})
    },
    // 初始化配置参数
    initConfig: {
      type: Object,
      default: () => ({})
    },
    // 是否显示加载状态
    showLoading: {
      type: Boolean,
      default: true
    },
    // 错误重试次数
    maxRetries: {
      type: Number,
      default: 3
    }
  },
  data() {
    return {
      // 认证状态: loading, authenticated, unauthenticated, error
      authState: 'loading',
      // 用户数据
      userData: null,
      // 用户令牌
      userToken: null,
      // Keycloak实例
      keycloakInstance: null,
      // 错误信息
      errorMessage: '',
      // 重试状态
      retrying: false,
      // 登录状态
      loggingIn: false,
      // 重试次数
      retryCount: 0,
      // 认证检查定时器
      authCheckTimer: null
    }
  },
  async mounted() {
    await this.initializeKeycloak()
    this.startAuthMonitoring()
  },
  beforeDestroy() {
    this.stopAuthMonitoring()
  },
  methods: {
    /**
     * 初始化Keycloak
     */
    async initializeKeycloak() {
      try {
        this.authState = 'loading'
        
        // 重置Keycloak实例以确保配置生效
        resetKeycloakInstance()
        
        // 设置Keycloak配置
        if (Object.keys(this.keycloakConfig).length > 0) {
          setKeycloakConfig(this.keycloakConfig)
        }
        
        // 设置初始化配置
        if (Object.keys(this.initConfig).length > 0) {
          setKeycloakInitConfig(this.initConfig)
        }
        
        // 获取Keycloak实例
        this.keycloakInstance = await getKeycloakInstance()
        
        // 检查认证状态
        await this.checkAuthenticationStatus()
        
      } catch (error) {
        console.error('Keycloak初始化失败:', error)
        this.handleAuthError(error)
      }
    },
    
    /**
     * 检查认证状态
     */
    async checkAuthenticationStatus() {
      try {
        const authenticated = await isAuthenticated()
        
        if (authenticated) {
          // 获取用户信息和令牌
          this.userData = await getUserInfo()
          this.userToken = await getToken()
          this.authState = 'authenticated'
          this.retryCount = 0
          
          // 触发认证成功事件
          this.$emit('authenticated', {
            user: this.userData,
            token: this.userToken,
            keycloak: this.keycloakInstance
          })
        } else {
          this.authState = 'unauthenticated'
          
          // 如果启用自动登录，则自动跳转登录
          if (this.autoLogin) {
            await this.handleLogin()
          }
        }
      } catch (error) {
        console.error('认证状态检查失败:', error)
        this.handleAuthError(error)
      }
    },
    
    /**
     * 处理登录
     */
    async handleLogin() {
      try {
        this.loggingIn = true
        await login()
      } catch (error) {
        console.error('登录失败:', error)
        this.handleAuthError(error)
      } finally {
        this.loggingIn = false
      }
    },
    
    /**
     * 处理重试
     */
    async handleRetry() {
      if (this.retryCount >= this.maxRetries) {
        this.errorMessage = `已达到最大重试次数(${this.maxRetries})，请刷新页面重试`
        return
      }
      
      try {
        this.retrying = true
        this.retryCount++
        await this.initializeKeycloak()
      } catch (error) {
        console.error('重试失败:', error)
        this.handleAuthError(error)
      } finally {
        this.retrying = false
      }
    },
    
    /**
     * 处理认证错误
     */
    handleAuthError(error) {
      this.authState = 'error'
      this.errorMessage = error.message || '认证过程中发生未知错误'
      
      // 触发错误事件
      this.$emit('error', {
        error,
        message: this.errorMessage,
        retryCount: this.retryCount
      })
    },
    
    /**
     * 开始认证监控
     */
    startAuthMonitoring() {
      // 每30秒检查一次认证状态
      this.authCheckTimer = setInterval(async () => {
        if (this.authState === 'authenticated') {
          try {
            const authenticated = await isAuthenticated()
            if (!authenticated) {
              this.authState = 'unauthenticated'
              this.userData = null
              this.userToken = null
              
              // 触发认证失效事件
              this.$emit('unauthenticated')
              
              // 如果启用自动登录，则重新登录
              if (this.autoLogin) {
                await this.handleLogin()
              }
            }
          } catch (error) {
            console.error('认证状态监控失败:', error)
          }
        }
      }, 30000)
    },
    
    /**
     * 停止认证监控
     */
    stopAuthMonitoring() {
      if (this.authCheckTimer) {
        clearInterval(this.authCheckTimer)
        this.authCheckTimer = null
      }
    },
    
    /**
     * 手动刷新认证状态
     */
    async refreshAuth() {
      await this.checkAuthenticationStatus()
    },
    
    /**
     * 登出
     */
    async logout() {
      try {
        await logout()
        this.authState = 'unauthenticated'
        this.userData = null
        this.userToken = null
        
        // 触发登出事件
        this.$emit('logout')
      } catch (error) {
        console.error('登出失败:', error)
        this.handleAuthError(error)
      }
    }
  }
}
</script>

<style lang="less" scoped>
.fb-keycloak-wrapper {
  width: 100%;
  height: 100%;
  
  .auth-loading {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 200px;
    
    .loading-spinner {
      margin-bottom: 16px;
      
      .spinner {
        width: 40px;
        height: 40px;
        border: 4px solid #f3f3f3;
        border-top: 4px solid #1890ff;
        border-radius: 50%;
        animation: spin 1s linear infinite;
      }
    }
    
    .loading-text {
      color: #666;
      font-size: 14px;
      margin: 0;
    }
  }
  
  .auth-error {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 200px;
    padding: 24px;
    
    .error-icon {
      width: 64px;
      height: 64px;
      margin-bottom: 16px;
    }
    
    .error-title {
      color: #ff6b6b;
      font-size: 18px;
      font-weight: 600;
      margin: 0 0 8px 0;
    }
    
    .error-message {
      color: #666;
      font-size: 14px;
      text-align: center;
      margin: 0 0 24px 0;
      max-width: 400px;
    }
    
    .error-actions {
      display: flex;
      gap: 12px;
    }
  }
  
  .auth-unauthenticated {
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 200px;
    
    .login-prompt {
      text-align: center;
      padding: 24px;
      
      .login-icon {
        width: 64px;
        height: 64px;
        margin: 0 auto 16px;
      }
      
      .login-title {
        color: #1890ff;
        font-size: 18px;
        font-weight: 600;
        margin: 0 0 8px 0;
      }
      
      .login-message {
        color: #666;
        font-size: 14px;
        margin: 0 0 24px 0;
      }
    }
  }
  
  .auth-success {
    width: 100%;
    height: 100%;
    
    .default-authenticated-content {
      padding: 24px;
      text-align: center;
      
      p {
        color: #52c41a;
        font-size: 16px;
        margin: 0;
      }
    }
  }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

// 响应式设计
@media (max-width: 768px) {
  .fb-keycloak-wrapper {
    .auth-error,
    .auth-unauthenticated {
      padding: 16px;
      
      .error-actions {
        flex-direction: column;
        width: 100%;
        
        .fb-button {
          width: 100%;
        }
      }
    }
  }
}
</style>