<template>
  <div id="vue">
    Hello Vue{{ this.$parent.indexVal }}
    <el-button class="filter-item" type="primary" @click="getQuery">
      返回
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
      this.$parent.activeName = 'Index'
    }
  }
}
</script>

<style scoped>

</style>
