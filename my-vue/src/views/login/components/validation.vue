<template>
  <div class="jc-component__range">
    <div :class="rangeStatus?'success':''" class="jc-range">
      <i :class="rangeStatus?successIcon:startIcon" @mousedown="rangeMove" />
      {{ rangeStatus?successText:startText }}
    </div>
  </div>
</template>
<script>
export default {
  props: {
    successFun: {
      type: Function,
      default: () => { return [] }
    },
    // 成功图标
    successIcon: {
      type: String,
      default: 'el-icon-success'
    },
    // 成功文字
    successText: {
      type: String,
      default: '验证成功'
    },
    // 开始的图标
    startIcon: {
      type: String,
      default: 'el-icon-d-arrow-right'
    },
    // 开始的文字
    startText: {
      type: String,
      default: '拖动滑块到最右边'
    },
    // 失败之后的函数
    errorFun: {
      type: Function,
      default: () => { return [] }
    },
    // 或者用值来进行监听
    status: {
      type: String,
      default: () => { return [] }
    }
  },
  data() {
    return {
      disX: 0,
      rangeStatus: false
    }
  },
  methods: {
    // 滑块移动
    rangeMove(e) {
      const ele = e.target
      const startX = e.clientX
      const eleWidth = ele.offsetWidth
      const parentWidth = ele.parentElement.offsetWidth
      const MaxX = parentWidth - eleWidth
      if (this.rangeStatus) { // 不运行
        return false
      }
      document.onmousemove = (e) => {
        const endX = e.clientX
        this.disX = endX - startX
        if (this.disX <= 0) {
          this.disX = 0
        }
        if (this.disX >= MaxX - eleWidth) { // 减去滑块的宽度,体验效果更好
          this.disX = MaxX
        }
        ele.style.transition = '.1s all'
        ele.style.transform = 'translateX(' + this.disX + 'px)'
        e.preventDefault()
      }
      document.onmouseup = () => {
        if (this.disX !== MaxX) {
          ele.style.transition = '.5s all'
          ele.style.transform = 'translateX(0)'
          // 执行成功的函数
          this.errorFun && this.errorFun()
        } else {
          this.rangeStatus = true
          if (this.status) {
            this.$parent[this.status] = true
          }

          this.$parent.rangeStatus = true
          // 执行成功的函数
          this.successFun && this.successFun()
        }
        document.onmousemove = null
        document.onmouseup = null
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@mixin jc-flex {
  display: flex;
  justify-content: center;
  align-items: center;
}
.jc-component__range {
  .jc-range {
    background-color: #ffcccc;
    position: relative;
    transition: 1s all;
    user-select: none;
    color: #333;
    @include jc-flex;
    height: 45px; /*no*/
    &.success {
      background-color: #7ac23c;
      color: #fff;
      i {
        color: #7ac23c;
      }
    }
    i {
      position: absolute;
      left: 0;
      width: 60px; /*no*/
      height: 100%;
      color: #919191;
      background-color: #fff;
      border: 1px solid #bbb;
      cursor: pointer;
      @include jc-flex;
    }
  }
}
</style>
