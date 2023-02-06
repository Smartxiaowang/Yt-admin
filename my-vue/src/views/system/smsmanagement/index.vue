<template>
  <component :is="activeName" />
</template>

<script>
import Msmlogin from './components/msmlogin'
import CorpList from './components/corpList'
export default {
  components: {
    Msmlogin: Msmlogin,
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
