<template>
  <div class="dashboard-editor-container"/>
</template>
<script>
export default {
  data() {
    return {
    }
  },
  created() {
  },
  methods: {
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  // border: 1px solid red;
  padding: 15px;
  background-color: rgb(240, 242, 245);
  position: relative;
  height: 100%;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 0px;
    .chart-search {
      text-align: right;
      align-items: center;
    }
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
