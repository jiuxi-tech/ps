<template>
  <div id="app" class="root">
    <transition name="fade-transform">
      <!--			<keep-alive>-->
      <router-view />
      <!--			</keep-alive>-->
    </transition>
  </div>
</template>

<script>
/**
 * DefaultApplicationLayout
 * (c) 2020 lincong1987
 */

export default {

  mounted() {


    document.getElementById("appLoading").style.display = "none"

    // 动态设置浏览器标题
    this.loadBrowserTitle()

    // 检查URL参数中是否有SSO token
    this.handleSSOToken()

    // 1、检测本地是否存在 token
    let token = this.$datax.get('token')

    // 2、检查当前的token是否过期
    if (token) {
      this.$svc.platform.checkToken(token).then(data => {
        if (data.code == -1) {
          // token无效，跳转到登录页
          if (this.$route.path != this.$datax.GLOBAL_CONFIG.loginPath) {
            this.$router.replace(this.$datax.GLOBAL_CONFIG.loginPath)
          }
        } else {
          // token有效，保持当前路由，不强制跳转到mainPath
          // 只有当前路由是根路径或登录页时才跳转到mainPath
          if (this.$route.path === '/' || this.$route.path === this.$datax.GLOBAL_CONFIG.loginPath) {
            this.$router.replace(this.$datax.GLOBAL_CONFIG.mainPath)
          }
        }
      })
    }
  },

  methods: {
    // 处理SSO Token
    handleSSOToken() {
      const urlParams = new URLSearchParams(window.location.search)
      const ssoToken = urlParams.get('token')
      
      if (ssoToken) {
        // 保存SSO token到本地存储
        this.$datax.set('token', ssoToken)
        
        // 清除URL中的token参数，避免刷新时重复处理
        const url = new URL(window.location)
        url.searchParams.delete('token')
        window.history.replaceState({}, document.title, url.pathname + url.search)
        
        // 验证token并跳转到主页
        this.$svc.platform.checkToken(ssoToken).then(data => {
          if (data.code == 1) {
            // token有效，跳转到主页
            this.$router.replace(this.$datax.GLOBAL_CONFIG.mainPath)
          } else {
            // token无效，清除并跳转到登录页
            this.$datax.set('token', '')
            this.$router.replace(this.$datax.GLOBAL_CONFIG.loginPath)
          }
        }).catch(() => {
          // 验证失败，清除token并跳转到登录页
          this.$datax.set('token', '')
          this.$router.replace(this.$datax.GLOBAL_CONFIG.loginPath)
        })
      }
    },
    
    // 动态加载浏览器标题
    async loadBrowserTitle() {
      try {
        // 创建超时Promise
        const timeoutPromise = new Promise((_, reject) => {
          setTimeout(() => reject(new Error('请求超时')), 5000)
        })
        
        // 获取浏览器标题配置
        const titlePromise = this.$svc.sys.config.getConfigValue('browser.title')
        
        // 使用Promise.race实现超时控制
        const result = await Promise.race([titlePromise, timeoutPromise])
        
        // 更新浏览器标题
        if (result && result.data) {
          document.title = result.data
        }
        
      } catch (error) {
        console.warn('加载浏览器标题配置失败，使用默认标题:', error)
        // 使用默认标题
        document.title = app.projectConfig.title || 'JPX3.0 管理系统'
      }
    }
  }

}
</script>

<style lang="less">
/*.root {*/
/*	border: 5px solid red;*/
/*}*/

body {
  overflow: hidden;
}
</style>
