<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="KEYWORDS" placeholder="搜索" class="filter-item" style="width: 200px;"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="getQuery">
        搜索
      </el-button>
    </div>
    <el-table
      v-loading="listLoading"
      ref="multipleTable"
      :data="varList"
      :row-key="getRowKey"
      :header-cell-style="{
        'font-weight': 'bold',
        'color': '#000'
      }"
      :row-class-name="tableRowClassName"
      tooltip-effect="dark"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        :reserve-selection="true"
        type="selection"
        width="55"
        align="center"/>
      <el-table-column type="index" label="序号" width="50" align="center" />
      <el-table-column prop="CORP_NAME" label="企业名称" />
      <el-table-column prop="COMPANY_AREA" label="所属区域" />
      <el-table-column prop="CHECKPERSON" label="主要负责人" />
      <el-table-column prop="CHECKPERSONPHONE" label="主要负责人电话" />
      <el-table-column prop="ISRECEIVE" label="是否接收短息" >
        <template slot-scope="{row}">
          <el-switch
            v-model="row.ISRECEIVE"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="0"
            inactive-value="1"/>
        </template>
      </el-table-column>
      <el-table-column prop="sycount" label="剩余短信数量" />
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="{row}">
          <el-button type="success" icon="el-icon-edit" size="mini" @click="gomslog(row.CORPINFO_ID)">查看日志</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="page-btn-group">
      <div/>
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
      listLoading: true,
      add: false,
      del: false,
      edit: false,
      listQuery: {
        page: 1,
        limit: 10
      },
      total: 0,
      KEYWORDS: '',
      varList: [],
      pd: [],
      multipleSelectionAll: [], // 所有选中的数据包含跨页数据
      multipleSelection: [], // 当前页选中的数据
      dialogFormEdit: false
    }
  },
  created() {
    this.getList(this.ROLE_ID)
  },
  methods: {
    tableRowClassName({ row, rowIndex }) {
      var timeType = this.judgeTime(row.CREATTIME)
      if (timeType == 1) {
        return 'yellow-row'
      } else if (timeType == 2) {
        return 'red-row'
      }
      return ''
    },
    judgeTime(startTime) {
      startTime = startTime.replace(/\-/g, '/')
      var endTime = new Date()
      var sTime = new Date(startTime) // 开始时间
      var eTime = new Date(endTime) // 结束时间
      var timeType = 1000 * 3600 * 24
      var differDay = (eTime.getTime() - sTime.getTime()) / timeType
      var nineMon = 270
      var oneYear = 365
      if (differDay >= oneYear) {
        return 2
      }
      if (differDay >= nineMon) {
        return 1
      }
      return 0
    },
    getRowKey(row) {
      return row.CORPINFO_ID
    },
    // 搜索
    getQuery() {
      this.$refs.multipleTable.clearSelection()
      this.getList()
    },
    // 获取列表
    getList() {
      this.listLoading = true
      requestFN(
        '/corpinfo/list?showCount=' + this.listQuery.limit + '&currentPage=' + this.listQuery.page,
        {
          KEYWORDS: this.KEYWORDS,
          ISSMALL: '2'
        }
      ).then((data) => {
        this.listLoading = false
        this.varList = data.varList
        this.total = data.page.totalResult
        this.hasButton()
        this.pd = data.pd
      }).catch((e) => {
        this.listLoading = false
      })
    },
    // 判断按钮权限，用于是否显示按钮
    hasButton: function() {
      var keys = 'corpinfo:add,corpinfo:del,corpinfo:edit,toExcel'
      requestFN(
        '/head/hasButton',
        {
          keys: keys
        }
      ).then((data) => {
        this.add = data.corpinfofhadminadd		// 新增权限
        this.del = data.corpinfofhadmindel		// 删除权限
        this.edit = data.corpinfofhadminedit	// 修改权限
      }).catch((e) => {
        this.listLoading = false
      })
    },
    // 获取数据字典数据
    getDict: function() {
    },

    gomslog(ID) {
      this.$parent.activeName = 'Msmlogin'
      this.$parent.CORPINFO_ID = ID
    }
  }
}

</script>
<style>
  .el-table .yellow-row {
    background: #f8e71c;
  }

  .el-table .red-row {
    background: #fc0221;
  }
</style>
