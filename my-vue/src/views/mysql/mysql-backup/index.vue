<template>
  <div class="app-container">
    <div class="filter-container">
      <el-tag type="info" size="medium"><i class="el-icon-coin">{{ dbtype }}</i></el-tag>
      <el-tag type="info" size="medium"><i class="el-icon-right">{{ databaseName }}</i></el-tag>
    </div>
    <el-table
      ref="multipleTable"
      :data="varList"
      :header-cell-style="{
        'font-weight': 'bold',
        'color': '#000'
      }"
      tooltip-effect="dark"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        type="selection"
        width="55"
        align="center"/>
      <el-table-column type="index" label="序号" width="50" align="center" />
      <el-table-column label="表名">
        <template slot-scope="scope">
          {{ scope.row }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="{row}">
          <el-button type="primary" icon="el-icon-delete" size="mini" @click="backupTable(row)">备份</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="page-btn-group">
      <div>
        <el-button type="success" icon="el-icon-delete" plain @click="makeBatch">备份表</el-button>
        <el-button type="primary" icon="el-icon-delete" plain @click="backupall">备份整个库</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination' // 通过 el-pagination二次打包
import { requestFN } from '@/utils/request'
export default {
  components: { Pagination },
  data() {
    return {
      varList: [],		// list
      dbtype: '',			// 数据库类型
      databaseName: '',	// 数据库名称
      add: false,
      loading: false		// 加载状态
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      requestFN(
        '/brdb/listAllTable',
        {

        }
      ).then((data) => {
        this.varList = data.varList
        this.dbtype = data.dbtype
        this.databaseName = data.databaseName
        this.hasButton()
        this.loading = false
      })
        .catch((e) => {
          this.listLoading = false
        })
    },
    backupTable(name) {
      this.$confirm('确定要备份[' + name + ']吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/brdb/backupTable',
          {
            fhtable: name
          }
        ).then(() => {
          this.listLoading = false
          this.$message({
            message: '备份完成',
            type: 'success'
          })
        }).catch((e) => {
          this.listLoading = false
          this.$message({
            message: '备份失败,检查配置文件及备份保存路径',
            type: 'error'
          })
        })
      }).catch(() => {
      })
    },
    makeBatch() {
      const _selectData = this.$refs.multipleTable.selection
      if (_selectData == null || _selectData.length == 0) {
        this.$message({
          message: '请选中要备份的表...',
          type: 'error'
        })
        return false
      }
      const names = _selectData.map((item, index) => {
        return item
      })

      this.$confirm('确定要备份选中的数据吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        for (let i = 0; i < names.length; i++) {
          requestFN(
            '/brdb/backupTable',
            {
              fhtable: names[i]
            }
          ).then(() => {
            if (i == names.length - 1) {
              this.listLoading = false
              this.$message({
                message: '备份完成',
                type: 'success'
              })
            }
          }).catch((e) => {
            if (i == names.length - 1) {
              this.listLoading = false
              this.$message({
                message: '备份失败,检查配置文件及备份保存路径',
                type: 'error'
              })
            }
          })
        }
      }).catch(() => {
      })
    },

    backupall() {
      this.$confirm('确定要备份整个数据库吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/brdb/backupAll',
          {
          }
        ).then(() => {
          this.listLoading = false
          this.$message({
            message: '备份成功,在数据还原里面查看记录',
            type: 'success'
          })
        }).catch((e) => {
          this.listLoading = false
          this.$message({
            message: '备份失败,检查配置文件及备份保存路径',
            type: 'error'
          })
        })
      }).catch(() => {
      })
    },
    hasButton: function() {
      var keys = 'brdb:add'
      requestFN(
        '/head/hasButton',
        {
          keys: keys
        }
      ).then((data) => {
        this.add = data.brdbfhadminadd
      }).catch((e) => {

      })
    }
  }
}
</script>
