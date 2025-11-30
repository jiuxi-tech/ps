<template>
  <div>
    <fb-dialog
        ref="dialog"
        v-if="dialog.create"
        v-show="dialog.visible"
        :title="dialog.title"
        :height="dialog.height"
        :width="dialog.width"
        :top="dialog.top"
        :overflow="dialog.overflow"
        :can-full-screen="dialog.canFullScreen"
        :show-close-btn="dialog.showCloseBtn"
        :disable-esc="true"
        :close-on-click-shadow="false"
        @on-close="closeTpDialog">

      <component v-if="currentComponent" :is="currentComponent" :param="dialog.param" :parentPage="currentPage"></component>
      <!-- 失活的组件将会被缓存！-->
      <!--
      <keep-alive>
      </keep-alive>
      -->

    </fb-dialog>

  </div>
</template>

<script>
import {isString} from "lodash-es"

export default {
  name: 'TpDialog',
  mixins: [],
  // 接收父组件的传参
  props: {},
  // 组件
  components: {
    // 'component-a': ComponentA,
    defaultComponent: {
      render: h => h('div'),
    },
  },
  // 创建方法
  created() {
    // 创建方法
    //this.apps=() => import('./common/' + this.Child + '.vue')
  },
  // 初始化方法
  mounted() {
    // 执行界面初始化

  },
  data() {
    return {
      currentPage: this,
      currentComponent: this.defaultComponent,
      // Dialog 是否显示
      dialog: {
        title: '',
        param: {},
        canFullScreen: false,
        create: false,
        visible: false,
        showCloseBtn: true,
        height: 600,
        width: 800,
        top: '15vh',
        overflow: 'hidden',
        callBack: this.closeTpDialogCallBack,
      },
    }
  },

  // 方法
  methods: {
    // 设置标题
    show(url, param, title, options) {
      this.dialog.create = false
      this.$nextTick(()=>{
        // 屏幕可用工作区宽度
        // let currentWidth = window.screen.width
        // 网页分辨率宽
        let currentWidth = window.innerWidth
        if (currentWidth < 1440) {
          // 小分辨率，使用默认值
          this.dialog.width = 800
          this.dialog.height = 556
        } else if (1440 <= currentWidth && currentWidth < 1600) {
          this.dialog.width = 864
          this.dialog.height = 588
        } else if (1600 <= currentWidth && currentWidth < 1920) {
          this.dialog.width = 960
          this.dialog.height = 656
        } else if (1920 <= currentWidth) {
          this.dialog.width = 1152
          this.dialog.height = 796
        }
        console.log('页面可见区域宽', currentWidth + ',' + this.dialog.width + ',' + this.dialog.height)

        // 处理三种组件加载方式
        if (isString(url)) {
          // 字符串模式：直接使用异步导入
          this.currentComponent = () => ({
            // 需要加载的组件 (应该是一个 `Promise` 对象)
            component: import('@/views' + url),
            // 异步组件加载时使用的组件
            loading: {template: '<div><fb-spin></fb-spin></div>'},
            // 加载失败时使用的组件
            error: {template: '<div>加载组件失败</div>'},
            // 展示加载时组件的延时时间。默认值是 200 (毫秒)
            delay: 1000,
            // 如果提供了超时时间且组件加载也超时了，
            // 则使用加载失败时使用的组件。默认值是：`Infinity`
            timeout: 10000
          })
        } else if (typeof url === 'function') {
          // 函数模式：直接使用，Vue 会自动处理 Promise
          this.currentComponent = url
        } else {
          // Promise 模式：包装成异步组件
          this.currentComponent = () => ({
            component: url,
            loading: {template: '<div><fb-spin></fb-spin></div>'},
            error: {template: '<div>加载组件失败</div>'},
            delay: 1000,
            timeout: 10000
          })
        }


        this.dialog.title = title

        options = options || {}
        this.dialog.height = options.height || this.dialog.height
        this.dialog.width = options.width || this.dialog.width
        if (typeof (options.canFullScreen) != 'undefined' && !options.canFullScreen) {
          this.dialog.canFullScreen = options.canFullScreen
        }

        if (typeof (options.showCloseBtn) != 'undefined' && !options.showCloseBtn) {
          this.dialog.showCloseBtn = options.showCloseBtn
        }

        this.dialog.top = options.top || this.dialog.top
        this.dialog.overflow = options.overflow || this.dialog.overflow
        this.dialog.callBack = options.callBack || this.closeTpDialogCallBack

        this.dialog.param = param

        // 第一次，创建
        this.dialog.create = true
        // 显示
        this.dialog.visible = true
        this.dialog.showCloseBtn = true

        this.$nextTick(() => {
          this.dialog.create && this.$refs.dialog.show()
        })
      })
    },
    // 取消
    closeTpDialog(param) {
      if (param) {
        // 子组件调用父组件方法，并传递参数
        this.$emit('closeTpDialog', param)
        // 另一种回调
        this.dialog.callBack(param)
      } else {
        // 子组件调用父组件方法
        this.$emit('closeTpDialog')
        // 另一种回调
        this.dialog.callBack()
      }
      // 隐藏
      this.dialog.visible = false

    },
    // 默认的弹出窗回调
    closeTpDialogCallBack(param) {
      // 默认不需要做任何事情
    },
  },
}
</script>

<style lang="less" scoped>

</style>
