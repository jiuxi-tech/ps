<template>
  <div class="config-error-wrapper">
    <div class="config-error-container">
      <!-- 错误图标 -->
      <div class="error-icon">
        <svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="32" cy="32" r="28" stroke="#ff6b6b" stroke-width="3" fill="#fff5f5"/>
          <circle cx="32" cy="32" r="20" fill="#ff6b6b" opacity="0.1"/>
          <path d="M32 20v16M32 44h.01" stroke="#ff6b6b" stroke-width="3" stroke-linecap="round"/>
        </svg>
      </div>
      
      <!-- 主要内容 -->
      <div class="error-content">
        <h1 class="error-title">{{ errorTitle }}</h1>
        <!-- <p class="error-message">{{ errorMessage }}</p> -->
        
        <!-- 错误详情 -->
        <div v-if="showDetails" class="error-details">
          <div class="detail-item">
            <span class="detail-label">错误时间：</span>
            <span class="detail-value">{{ currentTime }}</span>
          </div>
          <div class="detail-item" v-if="errorCode">
            <span class="detail-label">错误代码：</span>
            <span class="detail-value">{{ errorCode }}</span>
          </div>
          <div class="detail-item" v-if="requestId">
            <span class="detail-label">请求ID：</span>
            <span class="detail-value">{{ requestId }}</span>
          </div>
        </div>
        
        <!-- 操作按钮 -->
        <div class="error-actions">
          <fb-button 
            type="primary" 
            size="l"  
            :loading="retrying"
            @on-click="handleRetry"
            class="retry-btn"
          >
            <fb-icon name="refresh" v-if="!retrying" mr="8px" ></fb-icon>
            {{ retrying ? '重试中...' : '重新加载' }}
          </fb-button>
          
          <!-- <fb-button 
            v-if="showContactSupport"
            type="default" 
            size="large"
            @click="handleContactSupport"
            class="support-btn"
          >
            <fb-icon name="customer-service"></fb-icon>
            联系技术支持
          </fb-button> -->
        </div>
        
        <!-- 帮助信息 -->
        <div v-if="showHelp" class="help-section">
          <div class="help-toggle" @click="helpExpanded = !helpExpanded">
            <fb-icon :name="helpExpanded ? 'up' : 'down'"></fb-icon>
            <span>{{ helpExpanded ? '收起帮助' : '查看帮助' }}</span>
          </div>
          
          <transition name="help-expand">
            <div v-show="helpExpanded" class="help-content">
              <h4>为什么会出现这个问题？</h4>
              <ul>
                <li>服务器暂时无法响应请求</li>
                <li>网络连接可能存在问题</li>
                <li>系统正在进行维护升级</li>
                <li>配置服务暂时不可用</li>
              </ul>
              
              <h4>您可以尝试：</h4>
              <ul>
                <li>点击"重新加载"按钮重试</li>
                <li>检查您的网络连接</li>
                <li>稍后再试</li>
                <li>如果问题持续存在，请联系技术支持</li>
              </ul>
            </div>
          </transition>
        </div>
        
        <!-- 自动重试倒计时 -->
        <div v-if="autoRetry && countdown > 0" class="auto-retry">
          <fb-icon name="clock-circle"></fb-icon>
          <span>{{ countdown }}秒后自动重试</span>
          <fb-button type="link" size="small" @on-click="cancelAutoRetry">取消</fb-button>
        </div>
      </div>
    </div>
    
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="decoration-circle circle-1"></div>
      <div class="decoration-circle circle-2"></div>
      <div class="decoration-circle circle-3"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ConfigError',
  
  props: {
    // 错误标题
    errorTitle: {
      type: String,
      default: '服务正在恢复'
    },
    
    // 错误消息
    errorMessage: {
      type: String,
      default: '无法获取系统配置信息，请稍后重试或联系技术支持'
    },
    
    // 错误代码
    errorCode: {
      type: String,
      default: ''
    },
    
    // 请求ID
    requestId: {
      type: String,
      default: ''
    },
    
    // 是否显示详细信息
    showDetails: {
      type: Boolean,
      default: true
    },
    
    // 是否显示联系支持按钮
    showContactSupport: {
      type: Boolean,
      default: true
    },
    
    // 是否显示帮助信息
    showHelp: {
      type: Boolean,
      default: true
    },
    
    // 是否启用自动重试
    autoRetry: {
      type: Boolean,
      default: false
    },
    
    // 自动重试间隔（秒）
    autoRetryInterval: {
      type: Number,
      default: 30
    }
  },
  
  data() {
    return {
      retrying: false,
      helpExpanded: false,
      countdown: 0,
      countdownTimer: null,
      currentTime: this.formatCurrentTime()
    }
  },
  
  mounted() {
    if (this.autoRetry) {
      this.startAutoRetry()
    }
  },
  
  beforeDestroy() {
    this.clearCountdownTimer()
  },
  
  methods: {
    // 处理重试
    async handleRetry() {
      this.retrying = true
      this.cancelAutoRetry()
      
      try {
        await this.$emit('retry')
      } catch (error) {
        console.error('重试失败:', error)
      } finally {
        this.retrying = false
        
        // 如果启用自动重试，重新开始倒计时
        if (this.autoRetry) {
          setTimeout(() => {
            this.startAutoRetry()
          }, 2000)
        }
      }
    },
    
    // 联系技术支持
    handleContactSupport() {
      this.$emit('contact-support', {
        errorCode: this.errorCode,
        requestId: this.requestId,
        timestamp: new Date().toISOString(),
        userAgent: navigator.userAgent,
        url: window.location.href
      })
    },
    
    // 开始自动重试倒计时
    startAutoRetry() {
      this.countdown = this.autoRetryInterval
      this.countdownTimer = setInterval(() => {
        this.countdown--
        if (this.countdown <= 0) {
          this.handleRetry()
        }
      }, 1000)
    },
    
    // 取消自动重试
    cancelAutoRetry() {
      this.clearCountdownTimer()
      this.countdown = 0
    },
    
    // 清除倒计时定时器
    clearCountdownTimer() {
      if (this.countdownTimer) {
        clearInterval(this.countdownTimer)
        this.countdownTimer = null
      }
    },
    
    // 格式化当前时间
    formatCurrentTime() {
      const now = new Date()
      return now.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    }
  }
}
</script>

<style scoped lang="less">
.config-error-wrapper {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(180deg,#fde7d8,#d10000,#ffad99 35%,#fbe6d7 65%,#fdf4ed);
  padding: 20px;
  overflow: hidden;
}

.config-error-container {
  position: relative;
  z-index: 10;
  background: rgba(255, 255, 255, 0.98);
  border-radius: 16px;
  padding: 48px 40px;
  box-shadow: 
    0 20px 60px rgba(0, 0, 0, 0.15),
    0 8px 32px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  width: 100%;
  text-align: center;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.error-icon {
  margin-bottom: 32px;
}

.error-icon svg {
  width: 80px;
  height: 80px;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

.error-content .error-title {
  font-size: 28px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 16px;
  line-height: 1.3;
}

.error-content .error-message {
  font-size: 16px;
  color: #7f8c8d;
  line-height: 1.6;
  margin-bottom: 32px;
}

.error-details {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 32px;
  text-align: left;
}

.error-details .detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.error-details .detail-item:last-child {
  margin-bottom: 0;
}

.error-details .detail-item .detail-label {
  font-weight: 500;
  color: #5a6c7d;
}

.error-details .detail-item .detail-value {
  font-family: 'Courier New', monospace;
  color: #2c3e50;
  font-size: 14px;
}

.error-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-bottom: 32px;
  flex-wrap: wrap;
}

.error-actions .retry-btn {
  min-width: 140px;
}

.error-actions .support-btn {
  min-width: 140px;
}

.help-section {
  text-align: left;
}

.help-section .help-toggle {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #667eea;
  font-weight: 500;
  margin-bottom: 16px;
  transition: color 0.3s ease;
}

.help-section .help-toggle:hover {
  color: #5a67d8;
}

.help-section .help-content {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
}

.help-section .help-content h4 {
  color: #2c3e50;
  font-size: 16px;
  margin-bottom: 12px;
  margin-top: 0;
}

.help-section .help-content h4:not(:first-child) {
  margin-top: 20px;
}

.help-section .help-content ul {
  margin: 0;
  padding-left: 20px;
}

.help-section .help-content ul li {
  color: #5a6c7d;
  line-height: 1.6;
  margin-bottom: 8px;
}

.help-section .help-content ul li:last-child {
  margin-bottom: 0;
}

.help-expand-enter-active,
.help-expand-leave-active {
  transition: all 0.3s ease;
  overflow: hidden;
}

.help-expand-enter,
.help-expand-leave-to {
  opacity: 0;
  max-height: 0;
}

.help-expand-enter-to,
.help-expand-leave {
  opacity: 1;
  max-height: 300px;
}

.auto-retry {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 20px;
  background: #e3f2fd;
  border-radius: 8px;
  color: #1976d2;
  font-size: 14px;
  margin-top: 24px;
}

.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  overflow: hidden;
}

.background-decoration .decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: none;
}

.background-decoration .decoration-circle.circle-1 {
  width: 120px;
  height: 120px;
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.background-decoration .decoration-circle.circle-2 {
  width: 80px;
  height: 80px;
  top: 60%;
  right: 15%;
  animation-delay: 2s;
}

.background-decoration .decoration-circle.circle-3 {
  width: 60px;
  height: 60px;
  bottom: 20%;
  left: 20%;
  animation-delay: 4s;
}

/* 移除背景装饰动画：保留静态装饰元素，无动画效果 */

@media (max-width: 768px) {
  .config-error-container {
    padding: 32px 24px;
    margin: 16px;
  }
  
  .error-content .error-title {
    font-size: 24px;
  }
  
  .error-actions {
    flex-direction: column;
    align-items: center;
    
    .retry-btn,
    .support-btn {
      width: 100%;
      max-width: 280px;
    }
  }
  
  .error-details .detail-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
}

@media (max-width: 480px) {
  .config-error-wrapper {
    padding: 12px;
  }
  
  .config-error-container {
    padding: 24px 20px;
  }
  
  .error-icon svg {
    width: 60px;
    height: 60px;
  }
  
  .error-content .error-title {
    font-size: 20px;
  }
}
</style>