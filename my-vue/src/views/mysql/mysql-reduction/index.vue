<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="form.keywords" placeholder="搜索" class="filter-item" style="width: 200px;"/>
      <el-date-picker
        v-model="form.dates"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        class="filter-item"/>
      <el-select v-model="form.TYPE" placeholder="全部" clearable style="width: 100px" class="filter-item">
        <el-option v-for="item in typeOptions" :key="item.value" :label="item.name" :value="item.value" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search">
        搜索
      </el-button>
    </div>
    <el-table
      ref="multipleTable"
      :row-key="getRowKey"
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
        :reserve-selection="true"
        type="selection"
        width="55"
        align="center"/>
      <el-table-column type="index" label="序号" width="50" align="center" />
      <el-table-column prop="TABLENAME" label="表名" />
      <el-table-column prop="BACKUP_TIME" label="备份时间" />
      <el-table-column prop="USERNAME" label="操作用户" />
      <el-table-column prop="SQLPATH" label="存储位置" />
      <el-table-column v-show="remoteDB == 'no'" prop="DBSIZE" label="数据大小">
        <template slot-scope="{row}">
          {{ row.DBSIZE }}KB
        </template>
      </el-table-column>
      <el-table-column prop="BZ" label="备注" />
      <el-table-column label="操作" align="center" width="290">
        <template slot-scope="{row}">
          <el-button type="success" icon="el-icon-refresh-left" size="mini" @click="dbRecover(row.TABLENAME,row.SQLPATH)">还原</el-button>
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(row.FHDB_ID)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="page-btn-group">
      <div>
        <el-button type="danger" icon="el-icon-delete" plain>删除</el-button>
      </div>
      <pagination :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </div>
    <el-dialog :visible.sync="dialogFormEdit" title="编辑" width="580px">
      <el-form :model="pd" label-width="100px" style="width: 500px;">
        <el-form-item label="备注说明" prop="BZ">
          <el-input v-model="pd.BZ" :autosize="{ minRows: 1}" type="textarea" placeholder="这里输入备注说明..." />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormEdit = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormEdit = false">确 定</el-button>
      </div>
    </el-dialog>
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
      dialogFormEdit: false,
      form: {
        keywords: '',
        dates: [],
        TYPE: ''
      },
      dbtype: '',		// 数据库类型
      remoteDB: '',	// 是否远程备份
      typeOptions: [{ 'name': '全部', 'value': '' }, { 'name': '整库', 'value': '1' }, { 'name': '单表', 'value': '2' }],
      listQuery: {
        page: 1,
        limit: 10
      },
      varList: [],	// list
      pd: [],
      total: 0
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getRowKey(row) {
      return row.FHDB_ID
    },
    getQuery() {
      this.$refs.multipleTable.clearSelection()
      this.getList()
    },
    getList() {
      this.listLoading = true
      requestFN(
        '/brdb/list?showCount=' + this.listQuery.limit + '&currentPage=' + this.listQuery.page,
        {
          keywords: this.form.keywords,
          lastStart: this.form.dates[0],
          lastEnd: this.form.dates[1],
          TYPE: this.form.TYPE
        }
      ).then((data) => {
        this.listLoading = false
        this.varList = data.varList
        this.dbtype = data.dbtype
        this.remoteDB = data.remoteDB
        this.total = data.page.totalResult
        this.hasButton()
      }).catch((e) => {
        this.listLoading = false
      })
    },
    dbRecover(TABLENAME, SQLPATH) {
      this.$confirm('确定要进行还原["+TABLENAME+"]吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/brdb/dbRecover',
          {
            TABLENAME: TABLENAME,
            SQLPATH: SQLPATH
          }
        ).then(() => {
          this.listLoading = false
          this.$message({
            message: '还原成功',
            type: 'success'
          })
        }).catch((e) => {
          this.listLoading = false
          this.$message({
            message: '还原失败,检查配置文件及是否远程数据库',
            type: 'error'
          })
        })
      }).catch(() => {
      })
    },
    handleEdit(row) {
      this.pd = Object.assign({}, row)
      this.dialogFormEdit = true
    },
    handleDelete(id) {
      this.$confirm('确定要删除吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/brdb/delete',
          {
            FHDB_ID: id
          }
        ).then(() => {
          this.listLoading = false
          this.varList = []
          this.listQuery.page = 1
          this.getList()
        }).catch((e) => {
          this.listLoading = false
        })
      }).catch(() => {
      })
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
        return item.FHDB_ID
      }).join(',')

      this.$confirm('确定要删除选中的数据吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/brdb/deleteAll',
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
    },
    // 判断按钮权限，用于是否显示按钮
    hasButton: function() {
      var keys = 'brdb:del,brdb:edit'
      requestFN(
        '/head/hasButton',
        {
          keys: keys
        }
      ).then((data) => {
        this.del = data.fhbuttonfhadmindel
        this.edit = data.fhbuttonfhadminedit
      }).catch((e) => {
        this.listLoading = false
      })
    }
  }
}
</script>

