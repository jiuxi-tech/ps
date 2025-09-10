<template>
  <div class="ip-access-denied">
    <div class="denied-container">
      <!-- 图标区域 -->
      <div class="icon-section">
        <svg class="denied-icon" viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="32" cy="32" r="30" stroke="#ff4757" stroke-width="4" fill="#fff5f5"/>
          <path d="M20 20L44 44M44 20L20 44" stroke="#ff4757" stroke-width="4" stroke-linecap="round"/>
        </svg>
      </div>
      
      <!-- 主要内容 -->
      <div class="content-section">
        <h1 class="title">访问受限</h1>
        <p class="message">{{ message }}</p>
        <div class="details">
          <p class="ip-info">
            <span class="label">您的IP地址：</span>
            <span class="value">{{ clientIp || '获取中...' }}</span>
          </p>
          <p class="time-info">
            <span class="label">访问时间：</span>
            <span class="value">{{ currentTime }}</span>
          </p>
        </div>
      </div>
      
      <!-- 操作按钮 -->
      <div class="action-section">
        <fb-button type="primary" @on-click="handleRefresh" :loading="refreshing">
          <fb-icon type="refresh"></fb-icon>
          重新尝试
        </fb-button>
        <fb-button @on-click="handleContact">
          <fb-icon type="phone"></fb-icon>
          联系管理员
        </fb-button>
      </div>
      
      <!-- 帮助信息 -->
      <div class="help-section">
        <fb-collapse>
          <fb-collapse-panel name="help" title="为什么会出现这个页面？">
            <div class="help-content">
              <p>出现此页面的可能原因：</p>
              <ul>
                <li>您的IP地址不在系统允许的访问范围内</li>
                <li>系统管理员已限制从您当前网络位置的访问</li>
                <li>您可能正在使用代理或VPN服务</li>
              </ul>
              <p class="help-note">
                如需访问系统，请联系系统管理员将您的IP地址添加到白名单中。
              </p>
            </div>
          </fb-collapse-panel>
        </fb-collapse>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'IpAccessDenied',
  props: {
    message: {
      type: String,
      default: '您的IP地址不在允许访问范围内，请联系管理员'
    },
    clientIp: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      refreshing: false,
      currentTime: ''
    }
  },
  mounted() {
    this.updateTime()
    this.getClientIp()
    // 每秒更新时间
    this.timeInterval = setInterval(this.updateTime, 1000)
  },
  beforeDestroy() {
    if (this.timeInterval) {
      clearInterval(this.timeInterval)
    }
  },
  methods: {
    // 更新当前时间
    updateTime() {
      const now = new Date()
      this.currentTime = now.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    },
    
    // 获取客户端IP
    async getClientIp() {
      if (this.clientIp) return
      
      try {
        // 尝试从多个服务获取IP
        const ipServices = [
          'https://api.ipify.org?format=json',
          'https://httpbin.org/ip',
          'https://api.ip.sb/ip'
        ]
        
        for (const service of ipServices) {
          try {
            const response = await fetch(service, { timeout: 3000 })
            const data = await response.json()
            this.clientIp = data.ip || data.origin || data
            break
          } catch (e) {
            continue
          }
        }
      } catch (error) {
        console.warn('无法获取客户端IP:', error)
        this.clientIp = '无法获取'
      }
    },
    
    // 刷新页面
    async handleRefresh() {
      this.refreshing = true
      try {
        // 等待一秒后刷新页面
        await new Promise(resolve => setTimeout(resolve, 1000))
        window.location.reload()
      } catch (error) {
        this.refreshing = false
      }
    },
    
    // 联系管理员
    handleContact() {
      const subject = encodeURIComponent('IP访问权限申请')
      const body = encodeURIComponent(
        `尊敬的管理员：\n\n` +
        `我在访问系统时遇到IP访问限制，请协助处理。\n\n` +
        `访问信息：\n` +
        `- IP地址：${this.clientIp}\n` +
        `- 访问时间：${this.currentTime}\n` +
        `- 错误信息：${this.message}\n\n` +
        `谢谢！`
      )
      
      // 尝试打开邮件客户端
      const mailtoLink = `mailto:admin@company.com?subject=${subject}&body=${body}`
      
      try {
        window.open(mailtoLink)
      } catch (error) {
        // 如果无法打开邮件客户端，显示联系信息
        this.$modal.info({
          title: '联系管理员',
          content: `
            <div style="text-align: left;">
              <p><strong>请通过以下方式联系系统管理员：</strong></p>
              <p>📧 邮箱：admin@company.com</p>
              <p>📞 电话：400-000-0000</p>
              <p>💬 QQ：123456789</p>
              <br>
              <p><strong>请提供以下信息：</strong></p>
              <p>• IP地址：${this.clientIp}</p>
              <p>• 访问时间：${this.currentTime}</p>
            </div>
          `,
          width: 500
        })
      }
    }
  }
}
</script>

<style lang="less" scoped>
.ip-access-denied {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.denied-container {
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  padding: 40px;
  max-width: 600px;
  width: 100%;
  text-align: center;
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.icon-section {
  margin-bottom: 30px;
}

.denied-icon {
  width: 80px;
  height: 80px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

.content-section {
  margin-bottom: 30px;
}

.title {
  font-size: 28px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 16px 0;
}

.message {
  font-size: 16px;
  color: #7f8c8d;
  line-height: 1.6;
  margin: 0 0 24px 0;
}

.details {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  margin: 20px 0;
  text-align: left;
}

.ip-info, .time-info {
  margin: 8px 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.label {
  font-weight: 500;
  color: #495057;
}

.value {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  background: #e9ecef;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 14px;
  color: #495057;
}

.action-section {
  margin-bottom: 30px;
  
  .fb-button {
    margin: 0 8px;
    min-width: 120px;
  }
}

.help-section {
  text-align: left;
  
  .help-content {
    color: #6c757d;
    font-size: 14px;
    line-height: 1.6;
    
    ul {
      margin: 12px 0;
      padding-left: 20px;
      
      li {
        margin: 6px 0;
      }
    }
    
    .help-note {
      background: #e3f2fd;
      border-left: 4px solid #2196f3;
      padding: 12px 16px;
      margin: 16px 0 0 0;
      border-radius: 0 4px 4px 0;
      font-style: italic;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .denied-container {
    padding: 24px;
    margin: 10px;
  }
  
  .title {
    font-size: 24px;
  }
  
  .denied-icon {
    width: 60px;
    height: 60px;
  }
  
  .action-section {
    .fb-button {
      display: block;
      width: 100%;
      margin: 8px 0;
    }
  }
  
  .details {
    .ip-info, .time-info {
      flex-direction: column;
      align-items: flex-start;
      gap: 4px;
    }
  }
}
</style>