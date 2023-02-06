<template>
  <component :is="activeName" />
</template>

<script>
import corpEdit from './components/corpEdit'
import CorpList from './components/corpList'
export default {
  components: {
    corpEdit: corpEdit,
    CorpList: CorpList
  },
  data() {
    return {
      activeName: 'CorpList',
      CORPINFO_ID: ''
    }
  }
}
</script>

<style scoped>
</style>
