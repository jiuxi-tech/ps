<template>
  <div class="test-wrapper">
    <div class="test-header">
      tp-components 组件包 测试
    </div>
    <div class="test-body clearfix">
      <div class="test-menu">
        <div class="test-menu-item" :class="{active: $route.path.indexOf(item.path) >= 0}" v-for="item in menus" :key="item.path" @click="handleCLick(item)">
          {{ item.label }}
        </div>
      </div>
      <div class="test-content">
        <transition name="fade-in">
          <router-view></router-view>
        </transition>

      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "test",
  data() {
    return {
      menus: [
        {label: 'tp-dialog', path: '/test/tp-dialog-test'}
      ]
    }
  },
  methods: {
    handleCLick(item) {
      this.$router.push(item.path)
    }
  }
}
</script>

<style lang="less">
  html, body, #app, .wrapper {
    width: 100%;
    height: 100%;
  }

  .test-wrapper {
    width: 100%;
    height: 100%;

    .test-header {
      height: 90px;
      line-height: 90px;
      padding-left: 20px;
      font-size: 22px;
      color: #fff;
      background: #0284FE;
      font-weight: bold;
    }

    .test-body {
      width: 100%;
      height: calc(100% - 90px);


      &:after {
        content: '';
        display: block;
        clear: both;
      }
    }

    .test-menu {
      width: 200px;
      height: 100%;
      overflow: auto;
      border-right: 1px solid #eee;
      float: left;

      &-item {
        height: 40px;
        line-height: 40px;
        padding: 0 10px;
        font-size: 16px;
        color: #111;
        cursor: pointer;

        &:hover, &.active {
          opacity: .8;
          background: #ccc;
        }
      }
    }
    .test-content {
      width: calc(100% - 200px);
      height: 100%;
      padding: 8px;
      float: left;
      overflow: auto;
    }
  }
</style>