<template>
  <div id="vue">
    Hello Vue{{ this.$parent.val1 }}
    <el-button class="filter-item" type="primary" @click="getQuery">
      搜索
    </el-button>
  </div>
</template>
<script>
export default {
  data() {
    return {

    }
  },
  methods: {
    getQuery() {
      this.$parent.indexVal = '2333'
      this.$parent.activeName = 'Index2'
    }
  }
}
</script>

<style scoped>

</style>
