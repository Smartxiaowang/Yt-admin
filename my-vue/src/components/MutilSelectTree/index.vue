<template>
  <div style="width:284px;display: inline-block;margin-left: -5px">
    <!-- <div class="mask"
         v-show="isShowSelect"
         @click="isShowSelect = !isShowSelect"></div> -->
    <el-popover
      :width="width"
      v-model="isShowSelect"
      placement="bottom-start"
      trigger="manual"
      clearable
      @hide="popoverHide"
      @show="popoverShow">
      <el-tree
        ref="tree"
        :data="data"
        :props="defaultProps"
        :node-key="nodeKey"
        :check-strictly="checkStrictly"
        :expand-on-click-node="false"
        :check-on-click-node="true"
        :default-checked-keys="defaultCheckedKeys"
        :highlight-current="true"
        :show-checkbox="true"
        :clearable="clearable"
        class="common-tree"
        default-expand-all
        @check-change="handleCheckChange"/>
      <el-select
        slot="reference"
        ref="select"
        :style="selectStyle"
        v-model="selectedData"
        :clearable="clearable"
        multiple
        class="tree-select"
        @click.native="isShowSelect = !isShowSelect">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"/>
      </el-select>
    </el-popover>
  </div>
</template>

<script>
export default {
  name: 'TreeSelect',
  // props: ["clear"],
  props: {
    // 树结构数据
    data: {
      type: Array,
      default() {
        return []
      }
    },
    // 是否可选根节点
    rootNodeChick: {
      type: Boolean,
      default: () => { return true }
    },
    // 是否清空数据
    clearable: {
      type: Boolean,
      default: () => { return true }
    },
    defaultProps: {
      type: Object,
      default() {
        return {}
      }
    },
    nodeKey: {
      type: String,
      default() {
        return 'id'
      }
    },
    // 显示复选框情况下，是否严格遵循父子不互相关联
    checkStrictly: {
      type: Boolean,
      default() {
        return false
      }
    },
    // 默认选中的节点key数组
    checkedKeys: {
      type: Array,
      default() {
        return []
      }
    },
    // 显示复选框情况下，是否严格遵循父子不互相关联
    defaultCallback: {
      type: Boolean,
      default() {
        return true
      }
    },
    width: {
      type: Number,
      default() {
        return 260
      }
    },
    height: {
      type: Number,
      default() {
        return 360
      }
    }
  },
  data() {
    return {
      defaultCheckedKeys: [],
      isShowSelect: false, // 是否显示树状选择器
      options: [],
      selectedData: [], // 选中的节点
      selectStyle: 'width:' + (this.width + 24) + 'px;',
      checkedIds: [],
      checkedData: [],
      tempCheckedKeys: []
    }
  },
  watch: {
    isShowSelect(val) {
      // 隐藏select自带的下拉框
      this.$refs.select.blur()
    },
    selectedData: function(newData, oldData) {
      this.popoverHide()
      if (
        newData == undefined ||
          newData == null ||
          newData == [] ||
          newData.length == 0
      ) { this.$refs.tree.setCheckedKeys([]) }
    }
  },
  mounted() {
    if (this.checkedKeys.length > 0) {
      this.defaultCheckedKeys = this.checkedKeys
      this.selectedData = this.checkedKeys.map(item => {
        var node = this.$refs.tree.getNode(item)
        return node.label
      })
    }
  },
  methods: {
    loadCheckedKeys(checkedKeys) {
      if (checkedKeys.length > 0) {
        if (this.defaultCallback) {
          this.defaultCheckedKeys = Object.assign([], checkedKeys)
          this.selectedData = checkedKeys.map(item => {
            var node = this.$refs.tree.getNode(item)
            return node.label
          })
        } else {
          this.tempCheckedKeys = checkedKeys
        }
      }
    },
    handleClear() {
      this.selectedData = []
      this.$nextTick(() => {
        this.$refs.tree.setCheckedKeys([])
      })
    },
    popoverShow() {
      if (!this.defaultCallback) {
        this.defaultCheckedKeys = Object.assign([], this.tempCheckedKeys)
        this.selectedData = this.tempCheckedKeys.map(item => {
          var node = this.$refs.tree.getNode(item)
          return node.label
        })
      }
    },
    popoverHide() {
      this.checkedIds = this.$refs.tree.getCheckedKeys() // 所有被选中的节点的 key 所组成的数组数据
      this.checkedData = this.$refs.tree.getCheckedNodes() // 所有被选中的节点所组成的数组数据
      this.$emit('handleTreeSelected', this.checkedIds, this.checkedData)
    },
    // 节点选中状态发生变化时的回调
    handleCheckChange() {
      var checkedKeys = this.$refs.tree.getCheckedKeys() // 所有被选中的节点的 key 所组成的数组数据
      this.options = []
      if (!this.rootNodeChick) {
        checkedKeys.forEach(item => {
          var node = this.$refs.tree.getNode(item) // 所有被选中的节点对应的node
          const tmpMap = {}
          if (node.childNodes.length == 0) {
            tmpMap.value = node.key
            tmpMap.label = node.label
            this.options.push(tmpMap)
          }
        })
      } else {
        this.options = checkedKeys.map(item => {
          var node = this.$refs.tree.getNode(item) // 所有被选中的节点对应的node
          const tmpMap = {}
          tmpMap.value = node.key
          tmpMap.label = node.label
          return tmpMap
        })
      }
      this.selectedData = this.options.map(item => {
        return item.label
      })
    }
  }
}
</script>

<style scoped>
  .mask {
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    opacity: 0;
    z-index: 999;
  }
  .common-tree {
    overflow: auto;
  }
</style>

<style>
  .tree-select .el-select__tags .el-tag .el-tag__close {
    display: none;
  }
  .tree-select .el-select__tags .el-tag .el-icon-close {
    display: none;
  }
</style>
