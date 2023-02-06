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
      <el-table-column prop="NAME" label="名称" />
      <el-table-column prop="SHIRO_KEY" label="权限标识" />
      <el-table-column prop="BZ" label="备注" />
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="{row}">
          <el-button v-show="edit" type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(row)">编辑</el-button>
          <el-button v-show="del" type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(row.FHBUTTON_ID, row.NAME)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="page-btn-group">
      <div>
        <el-button v-show="add" type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button v-show="del" type="danger" icon="el-icon-delete" plain @click="batchDel">删除</el-button>
      </div>
      <pagination :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </div>

    <el-dialog :visible.sync="dialogFormEdit" :title="dialogType==='edit'?'修改':'新增'" width="600px">
      <el-form ref="form" :rules="rules" :model="pd" label-width="110px" style="width: 500px;">
        <el-form-item label="按钮名称" prop="NAME">
          <el-input v-model="pd.NAME" placeholder="这里输入按钮名称..." />
        </el-form-item>
        <el-form-item label="权限标识" prop="SHIRO_KEY">
          <el-input v-model="pd.SHIRO_KEY" placeholder="这里输入权限标识..." />
        </el-form-item>
        <el-form-item label="备注说明">
          <el-input v-model="pd.BZ" placeholder="这里输入备注说明..." />
        </el-form-item>
        <el-form-item label="标签代码">
          <el-input v-model="code" :autosize="{ minRows: 4}" type="textarea" disabled/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormEdit = false">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
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
      dialogType: 'add',
      rules: {
        NAME: [{ required: true, message: '按钮名称不能为空', trigger: 'blur' }],
        SHIRO_KEY: [{ required: true, message: '权限标识不能为空', trigger: 'blur' }]
      }
    }
  },
  computed: {
    code: {
      get() {
        if (this.pd.SHIRO_KEY) {
          return '<a v-show="' + this.pd.SHIRO_KEY + '" class="btn btn-light btn-sm">按钮</a>'
        } else {
          return ''
        }
      },
      set(val) {

      }
    }
  },
  created() {
    this.getList(this.ROLE_ID)
    this.hasButton()
  },
  methods: {
    getRowKey(row) {
      return row.FHBUTTON_ID
    },
    handleAdd() {
      this.dialogType = 'add'
      this.pd = {}
      this.code = ''
      this.dialogFormEdit = true
    },
    handleEdit(row) {
      this.dialogType = 'edit'
      this.pd = Object.assign({}, row)
      this.dialogFormEdit = true
    },
    getQuery() {
      this.$refs.multipleTable.clearSelection()
      this.getList()
    },
    getList() {
      this.listLoading = true
      requestFN(
        '/fhbutton/list?showCount=' + this.listQuery.limit + '&currentPage=' + this.listQuery.page,
        {
          KEYWORDS: this.KEYWORDS
        }
      ).then((data) => {
        this.listLoading = false
        this.varList = data.varList
        this.total = data.page.totalResult
        this.pd = data.pd
      }).catch((e) => {
        this.listLoading = false
      })
    },
    confirm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.listLoading = true
          requestFN(
            '/fhbutton/' + this.dialogType,
            {
              FHBUTTON_ID: this.pd.FHBUTTON_ID,
              NAME: this.pd.NAME,
              SHIRO_KEY: this.pd.SHIRO_KEY,
              BZ: this.pd.BZ
            }
          ).then((data) => {
            this.listLoading = false
            this.dialogFormEdit = false
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
    },
    handleDelete(id, name) {
      this.$confirm('确定要删除[' + name + ']吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/fhbutton/delete',
          {
            FHBUTTON_ID: id
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
        return item.FHBUTTON_ID
      }).join(',')

      this.$confirm('确定要删除选中的数据吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/fhbutton/deleteAll',
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
      var keys = 'fhbutton:add,fhbutton:del,fhbutton:edit'
      requestFN(
        '/head/hasButton',
        {
          keys: keys
        }
      ).then((data) => {
        this.add = data.fhbuttonfhadminadd
        this.del = data.fhbuttonfhadmindel
        this.edit = data.fhbuttonfhadminedit
      }).catch((e) => {
        this.listLoading = false
      })
    }
  }
}
</script>

