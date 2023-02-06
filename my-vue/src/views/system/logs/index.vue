<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="KEYWORDS" placeholder="搜索" class="filter-item" style="width: 200px;"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="getQuery">
        搜索
      </el-button>
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
      <el-table-column prop="USERNAME" label="用户名" />
      <el-table-column prop="CONTENT" label="事件" />
      <el-table-column prop="CZTIME" label="操作时间"/>
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="{row}">
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(row.FHLOG_ID)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="page-btn-group">
      <div>
        <template >
          <el-button type="danger" icon="el-icon-delete" plain @click="batchDel">删除</el-button>
        </template>
      </div>
      <pagination :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </div>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination' // 通过 el-pagination二次打包
import { requestFN } from '@/utils/request'
import waves from '@/directive/waves' // waves directive
export default {
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      listQuery: {
        page: 1,
        limit: 10
      },
      total: 0,
      varList: [],
      KEYWORDS: ''
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      requestFN(
        '/fhlog/list?showCount=' + this.listQuery.limit + '&currentPage=' + this.listQuery.page,
        {
          KEYWORDS: this.KEYWORDS
        }
      ).then((data) => {
        this.listLoading = false
        this.varList = data.varList
        this.total = data.page.totalResult
        this.pd = data.pd
      })
        .catch((e) => {
          this.listLoading = false
        })
    },
    handleDelete(id) {
      this.$confirm('确定要删除吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/fhlog/delete',
          {
            FHLOG_ID: id
          }
        ).then((data) => {
          if (data.result == 'success') {
            this.listLoading = false
            this.getList()
          }
        }).catch((e) => {
          this.listLoading = false
        })
      }).catch(() => {
      })
    },
    getQuery() {
      this.getList()
      this.$refs.multipleTable.clearSelection()
    },
    batchDel() {
      const _selectData = this.$refs.multipleTable.selection
      if (_selectData == null || _selectData.length == 0) {
        this.$message({
          message: '请选中要删除的项...',
          type: 'error'
        })
        return false
      }
      const ids = _selectData.map((item, index) => {
        return item.FHLOG_ID
      }).join(',')
      this.$confirm('确定要删除选中的数据吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/fhlog/deleteAll',
          {
            DATA_IDS: ids
          }
        ).then(() => {
          this.listLoading = false
          this.varList = []
          this.listQuery.page = 1
          this.$refs.multipleTable.clearSelection()
          this.getList()
        }).catch((e) => {
          this.listLoading = false
        })
      }).catch(() => {
      })
    }
  }
}
</script>

