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
      <el-table-column prop="URL" label="访问路径" />
      <el-table-column prop="VERSION" label="app版本" />
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="{row}">
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(row.VERSIONMANAGER_ID)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="page-btn-group">
      <div>
        <template >
          <el-button v-show="add" type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
          <el-button v-show="del" type="danger" icon="el-icon-delete" plain @click="batchDel">删除</el-button>
        </template>
      </div>
      <pagination :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </div>
    <el-dialog :visible.sync="dialogFormAdd" :title="msg==='edit'?'修改':'新增'" width="580px">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" style="width: 500px;">
        <el-form-item label="访问路径" prop="URL">
          <el-input v-model="form.URL" placeholder="这里输入访问路径..." />
        </el-form-item>
        <el-form-item label="app版本" prop="VERSION">
          <el-input v-model="form.VERSION" placeholder="这里输入app版本..." />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormAdd = false">取 消</el-button>
        <el-button type="primary" @click="confirm()">确 定</el-button>
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
      listQuery: {
        page: 1,
        limit: 10
      },
      total: 0,
      varList: [],
      KEYWORDS: '',
      dialogFormAdd: false,
      add: false,		// 新增按钮
      del: false,		// 删除按钮
      edit: false,	// 修改按钮
      rules: {
        URL: [
          { required: true, message: '访问路径不能为空', trigger: 'blur' }
        ],
        VERSION: [
          { required: true, message: 'app版本不能为空', trigger: 'blur' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ]
      },
      form: {},
      msg: 'add'
    }
  },
  created() {
    this.getList()
    this.hasButton()
  },
  methods: {
    getList() {
      this.listLoading = true
      requestFN(
        '/versionurl/list?showCount=' + this.listQuery.limit + '&currentPage=' + this.listQuery.page,
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
    hasButton() {
      var keys = 'versionurl:add,versionurl:del,versionurl:edit,fhSms,email,fromExcel,toExcel'
      requestFN(
        '/head/hasButton',
        {
          keys: keys, tm: new Date().getTime()
        }
      ).then((data) => {
        if (data.result == 'success') {
          this.add = data.versionurlfhadminadd	// 增
          this.del = data.versionurlfhadmindel	// 删
          this.edit = data.versionurlfhadminedit	// 改
          this.fromExcel = data.fromExcel	// 从excel导入权限
          this.toExcel = data.toExcel		// 导出到excel权限
        } else if (data.result == 'exception') {
          // showException('按钮权限', data.exception)// 显示异常
          console.info('按钮权限:', data.exception)
        }
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
          '/versionmanager/delete',
          {
            VERSIONMANAGER_ID: id
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
        return item.VERSIONMANAGER_ID
      }).join(',')
      console.info(ids)
      this.$confirm('确定要删除选中的数据吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/versionmanager/deleteAll',
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
    handleAdd() {
      this.dialogFormAdd = true
      this.dialogType = 'add'
    },
    confirm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.listLoading = true
          requestFN(
            '/versionurl/' + this.dialogType,
            {
              URL: this.form.URL,
              VERSION: this.form.VERSION,
              tm: new Date().getTime()
            }
          ).then((data) => {
            this.listLoading = false
            this.dialogFormEdit = false
            this.dialogFormAdd = false
            this.varList = []
            this.listQuery.page = 1
            this.getList()
          }).catch((e) => {
            this.listLoading = false
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

