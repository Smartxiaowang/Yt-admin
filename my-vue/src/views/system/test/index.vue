<template>
  <div class="app-container">
    <transition name="fade" mode="out-in">
      <component :is="activeName"/>
    </transition>

  </div>
</template>
<script>
import Index from './components/index'
import Index2 from './components/index2'
export default {
  components: { Index, Index2 },
  data() {
    return {
      val1: '3',
      activeName: 'Index',
      val: '1',
      indexVal: ''
    }
  },
  methods: {
    getQuery() {
      this.activeName = 'Index2'
    }
  }
}

</script>
