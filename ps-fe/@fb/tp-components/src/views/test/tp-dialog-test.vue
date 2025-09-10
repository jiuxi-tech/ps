<template>
  <div>

    <fb-button @on-click="handleCLick">tp-dialog 测试</fb-button>
    <tp-dialog ref="tp-dialog"></tp-dialog>
  </div>
</template>

<script>
export default {
  methods: {
    handleCLick() {
      this.$refs['tp-dialog'].show(import('@fb/tp-components/src/views/test/add.vue'), {
        title: '测试'
      })
    }
  }
}
</script>

<style scoped>

</style>