<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="KEYWORDS" placeholder="搜索" class="filter-item" style="width: 200px;"/>
      <el-input v-model="phonem" placeholder="手机号" class="filter-item" style="width: 200px;"/>
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
      <el-table-column prop="PHONE" label="手机号" />
      <el-table-column prop="mbmc" label="短信模板" />
      <el-table-column prop="COMPANY_AREA" label="状态" >
        <template slot-scope="{row}">
          <div v-if="row.STATE=='OK'">发送成功</div>
          <div v-else-if="row.STATE=='isv.DOMESTIC_NUMBER_NOT_SUPPORTED'">国际/港澳台消息模板不支持发送境内号码</div>
          <div v-else-if="row.STATE=='sv.MOBILE_NUMBER_ILLEGAL'">非法手机号</div>
          <div v-else>发送失败</div>
        </template>
      </el-table-column>
      <el-table-column prop="CREATTIME" label="创建时间" />
      <el-table-column prop="corpName" label="企业名称" />
    </el-table>
    <div class="page-btn-group">
      <div>
        <el-button type="primary" @click="goBack">返回</el-button>

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
      dialogFormEdit: false,
      CORPINFO_ID: this.$parent.CORPINFO_ID
    }
  },
  created() {
    this.getList(this.ROLE_ID)
  },
  methods: {
    // 搜索
    getQuery() {
      this.$refs.multipleTable.clearSelection()
      this.getList()
    },
    // 获取列表
    getList() {
      this.listLoading = true
      requestFN(
        '/msmlogin/list?showCount=' + this.listQuery.limit + '&currentPage=' + this.listQuery.page,
        {
          KEYWORDS: this.KEYWORDS,
          phone: this.phonem,
          CORPINFO_ID: this.CORPINFO_ID,
          tm: new Date().getTime()
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

    // 获取数据字典数据
    getDict: function() {
    },
    goBack() {
      this.$parent.activeName = 'CorpList'
    }
  }
}
</script>

