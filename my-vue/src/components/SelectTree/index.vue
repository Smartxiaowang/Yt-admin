<template>
  <el-select ref="elSelect" v-model="valueId" :value="valueId" :filter-method="filterMethod" :clearable="clearable" filterable class="selectTree" @clear="clearHandle">
    <el-option :value="valueId" :label="valueTitle" >
      <el-tree
        id="tree-option"
        ref="selectTree"
        :filter-node-method="filterNode"
        :accordion="accordion"
        :data="options"
        :props="props"
        :node-key="props.value"
        :default-expanded-keys="defaultExpandedKey"
        @node-click="handleNodeClick"/>
    </el-option>
  </el-select>
</template>
<script>
export default {
  name: 'SelectTree',
  props: {
    /* 配置项 */
    props: {
      type: Object,
      default: () => {
        return {
          value: 'id', // ID字段名
          label: 'label', // 显示名称
          children: 'children' // 子级字段名
        }
      }
    },
    /* 选项列表数据(树形结构的对象数组) */
    options: {
      type: Array,
      default: () => { return [] }
    },
    /* 初始值 */
    value: {
      type: String,
      default: () => { return null }
    },
    /* 可清空选项 */
    clearable: {
      type: Boolean,
      default: () => { return true }
    },
    /* 可清空选项 */
    canparent: {
      type: Boolean,
      default: () => { return true }
    },
    /* 自动收起 */
    accordion: {
      type: Boolean,
      default: () => { return false }
    }
  },
  data() {
    return {
      valueId: this.value, // 初始值
      valueTitle: '',
      defaultExpandedKey: []
    }
  },
  watch: {
    valueId(newValue, oldValue) {
      this.valueId = newValue
      this.$emit('input', newValue)
      this.$emit('change', newValue)

      this.initHandle()
    }
  },
  mounted() {
    this.initHandle()
  },
  methods: {
    // 选择器检索过滤方法
    filterMethod(query) {
      // 调用树形控件的过滤
      this.$refs.selectTree.filter(query)
      // 忽略选择器本身的过滤
      return true
    },
    // 树节点过滤方法
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    // 初始化值
    initHandle() {
      if (this.valueId) {
        this.valueTitle = this.$refs.selectTree.getNode(this.valueId).data[this.props.label] // 初始化显示
        this.$refs.selectTree.setCurrentKey(this.valueId) // 设置默认选中
        this.defaultExpandedKey = [this.valueId] // 设置默认展开
      }
      this.$nextTick(() => {
        const scrollWrap = document.querySelectorAll('.el-scrollbar .el-select-dropdown__wrap')[0]
        // const scrollBar = document.querySelectorAll('.el-scrollbar .el-scrollbar__bar')
        scrollWrap.style.cssText = 'margin: 0px; max-height: none; overflow: hidden;'
        // scrollBar.forEach(ele => ele.style.width = 0)
      })
    },
    // 切换选项
    handleNodeClick(node) {
      if (node.children && node.children.length > 0 && !this.canparent) {
        return
      }
      this.valueTitle = node[this.props.label]
      this.valueId = node[this.props.value]
      this.$emit('input', this.valueId)
      this.$emit('change', this.valueId)

      this.defaultExpandedKey = []

      const scrollWrap = document.querySelectorAll('.el-select-dropdown.el-popper')
      scrollWrap.forEach(ele => {
        ele.style.display = 'none'
        ele.style.position = ''
        ele.style.top = ''
        ele.style.left = ''
      })
      this.clearSelected()
      this.$refs.elSelect.blur()
    },
    // 清除选中
    clearHandle() {
      this.valueTitle = ''
      this.valueId = null
      this.defaultExpandedKey = []
      this.clearSelected()
      this.$emit('input', null)
      this.$emit('change', null)
    },
    /* 清空选中样式 */
    clearSelected() {
      const allNode = document.querySelectorAll('#tree-option .el-tree-node')
      allNode.forEach((element) => element.classList.remove('is-current'))
    }
  }
}
</script>
<style scoped>
  .el-scrollbar .el-scrollbar__view .el-select-dropdown__item{
    height: auto;
    max-height: 274px;
    padding: 0;
    overflow: hidden;
    overflow-y: auto;
  }
  .el-select-dropdown__item.selected{
    font-weight: normal;
  }
  ul li >>>.el-tree .el-tree-node__content{
    height:auto;
    padding: 0 20px;
  }
  .el-tree-node__label{
    font-weight: normal;
  }
  .el-tree >>>.is-current .el-tree-node__label{
    color: #409EFF;
    font-weight: 700;
  }
  .el-tree >>>.is-current .el-tree-node__children .el-tree-node__label{
    color:#606266;
    font-weight: normal;
  }
</style>
