<template>

  <fb-container>
    <fb-container class="header"></fb-container>
    <fb-container class="header-logo"></fb-container>
    <fb-container class="header-shadow"></fb-container>

    <fb-container class="header-menu-date" :style="dateStyle">{{ date }}</fb-container>
    <fb-container class="header-menu-time" :style="timeStyle">{{ time }}</fb-container>

    <fb-container class="header-enter-admin" display="flex">
      <!--          <fb-container @on-click="$router.push('/index')">驾驶舱</fb-container>-->
      <fb-container @on-click="enterAdmin"><fb-icon name="bell-fill"></fb-icon>管理后台</fb-container>
      <!--      <fb-container @on-click="exitAdmin">退出系统 <fb-icon name="exit" size="18"></fb-icon></fb-container>-->
    </fb-container>

    <fb-container v-show="!hideMenu" class="header-menu-left" display="flex">
      <a @click="$router.push('/temp1')" href="javascript:;" :class="{'active': $route.path.includes('temp1')}">temp1</a>
      <a @click="$router.push('/temp1')" href="javascript:;" :class="{'active': $route.path.includes('temp2')}">temp2</a>
      <a @click="$router.push('/temp1')" href="javascript:;" :class="{'active': $route.path.includes('temp3')}">temp3</a>
    </fb-container>

    <fb-container v-show="!hideMenu" class="header-menu-right" display="flex">
      <a @click="$router.push('/temp1')" href="javascript:;" :class="{'active': $route.path.includes('temp4')}">temp4</a>
      <a @click="$router.push('/temp1')" href="javascript:;" :class="{'active': $route.path.includes('temp5')}">temp5</a>
      <a @click="$router.push('/temp1')" href="javascript:;" :class="{'active': $route.path.includes('temp6')}">temp6</a>
    </fb-container>

    <!--        &lt;!&ndash;	驾驶舱	&ndash;&gt;-->
    <!--        <fb-container class="header-cockpit" v-show="$route.path === '/index'"></fb-container>-->

  </fb-container>

</template>

<script>
/**
 * header
 * (c) 2021 lincong1987
 */

export default {
  name: 'ScreenHeader',

  props: {


    noTime: {
      type: Boolean,
      default: false,
    },

    timeStyle: {
      type: [Object, Array, String],
      default() {
        return {}
      },
    },

    noDate: {
      type: Boolean,
      default: false,
    },


    dateStyle: {
      type: [Object, Array, String],
      default() {
        return {}
      },
    },

      hideMenu: { // 不显示菜单
          type: Boolean,
          default: false,
      }

  },
  data() {

    let date = this.$dayjs().format('YYYY年MM月DD日 dddd HH:mm:ss')

    return {
      date,
      time: '',
    }

  },
  methods: {
    enterAdmin() {
      window.open("/qyaqfxpc/fb-classic/main9_ent.jsp")
    },
    exitAdmin() {
      this.$message.success('退出系统')
    }
  },
  mounted() {
    this.timer = setInterval(() => {
      this.date = this.$dayjs().format('YYYY年MM月DD日 dddd HH:mm:ss')
    }, 1000)
  },
  destroyed() {
    clearInterval(this.timer)
  },
}
</script>

<style lang="less" scoped>
</style>
