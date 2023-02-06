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
      <el-table-column prop="CORPINFO_ID" label="公司" />
      <el-table-column prop="BASEPATH" label="企业端路径" />
      <el-table-column prop="ADMINPATH" label="admin端路径" />
      <el-table-column prop="FILEPATH" label="文件路径" />
      <el-table-column prop="CREATOR" label="添加人" />
      <el-table-column prop="CREATTIME" label="添加时间" />
      <el-table-column prop="ISDELETE" label="是否删除" >
        <template slot-scope="{row}">
          <div v-show="row.ISDELETE=='0'">否</div>
          <div v-show="row.ISDELETE=='1'">是</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="{row}">
          <el-button v-show="edit" type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(row.SYSPATH_ID)">编辑</el-button>
          <el-button v-show="del" type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(row.SYSPATH_ID)">删除</el-button>
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
      <el-form ref="form" :rules="rules" :model="form" label-width="110px" style="width: 500px;">
        <el-form-item label="公司" prop="CORPINFO_ID">
          <el-input id="CORPINFO_ID" ref="CORPINFO_ID" v-model="form.CORPINFO_ID" maxlength="255" placeholder="这里输入公司..." title="公司"/>
        </el-form-item>
        <el-form-item label="企业端路径" prop="BASEPATH">
          <el-input id="BASEPATH" ref="BASEPATH" v-model="form.BASEPATH" maxlength="255" placeholder="这里输入企业端路径..." title="企业端路径"/>
        </el-form-item>
        <el-form-item label="admin端路径" prop="ADMINPATH">
          <el-input id="ADMINPATH" ref="ADMINPATH" v-model="form.ADMINPATH" maxlength="255" placeholder="这里输入admin端路径..." title="admin端路径"/>
        </el-form-item>
        <el-form-item label="文件路径" prop="FILEPATH">
          <el-input id="FILEPATH" ref="FILEPATH" v-model="form.FILEPATH" maxlength="255" placeholder="这里输入文件路径..." title="文件路径"/>
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
      form: {
        SYSPATH_ID: '', // 备注1
        CORPINFO_ID: '', // 公司
        BASEPATH: '', // 企业端路径
        ADMINPATH: '', // admin端路径
        FILEPATH: '', // 文件路径
        CREATOR: '', // 添加人
        CREATTIME: '', // 添加时间
        OPERATOR: '', // 修改人
        OPERATTIME: '', // 修改时间
        ISDELETE: ''// 是否删除 1-是  0-否
      },
      multipleSelectionAll: [], // 所有选中的数据包含跨页数据
      multipleSelection: [], // 当前页选中的数据
      dialogFormEdit: false,
      dialogType: 'add',
      rules: {
        SYSPATH_ID: [{ required: true, message: '备注1不能为空', trigger: 'blur' }],
        CORPINFO_ID: [{ required: true, message: '公司不能为空', trigger: 'blur' }],
        BASEPATH: [{ required: true, message: '企业端路径不能为空', trigger: 'blur' }],
        ADMINPATH: [{ required: true, message: 'admin端路径不能为空', trigger: 'blur' }],
        FILEPATH: [{ required: true, message: '文件路径不能为空', trigger: 'blur' }],
        CREATOR: [{ required: true, message: '添加人不能为空', trigger: 'blur' }],
        CREATTIME: [{ required: true, message: '添加时间不能为空', trigger: 'blur' }],
        OPERATOR: [{ required: true, message: '修改人不能为空', trigger: 'blur' }],
        OPERATTIME: [{ required: true, message: '修改时间不能为空', trigger: 'blur' }],
        ISDELETE: [{ required: true, message: '是否删除 1-是  0-否不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList(this.ROLE_ID)
  },
  methods: {
    getRowKey(row) {
      return row.SYSPATH_ID
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
        '/syspath/list?showCount=' + this.listQuery.limit + '&currentPage=' + this.listQuery.page,
        {
          KEYWORDS: this.KEYWORDS
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
    // 添加
    handleAdd() {
      this.dialogType = 'add'
      this.resetForm()
      this.getDict()
      this.dialogFormEdit = true
    },
    // 修改
    handleEdit(ID) {
      this.getDict()
      this.dialogType = 'edit'
      requestFN(
        '/syspath/goEdit',
        {
          SYSPATH_ID: ID
        }
      ).then((data) => {
        this.form = Object.assign({}, data.pd) // copy obj
      }).catch((e) => {
        this.listLoading = false
      })
      this.dialogFormEdit = true
    },
    // 保存
    confirm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.listLoading = true
          requestFN(
            '/syspath/' + this.dialogType,
            this.form
          ).then((data) => {
            this.listLoading = false
            this.dialogFormEdit = false
            this.getList()
          }).catch((e) => {
            this.listLoading = false
          })
        } else {
          return false
        }
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
          '/syspath/delete',
          {
            SYSPATH_ID: id
          }
        ).then(() => {
          this.$message({
            message: '删除成功',
            type: 'success'
          })
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
        return item.SYSPATH_ID
      }).join(',')

      this.$confirm('确定要删除选中的数据吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/syspath/deleteAll',
          {
            DATA_IDS: ids
          }
        ).then(() => {
          this.$message({
            message: '删除成功',
            type: 'success'
          })
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
      var keys = 'syspath:add,syspath:del,syspath:edit,toExcel'
      requestFN(
        '/head/hasButton',
        {
          keys: keys
        }
      ).then((data) => {
        this.add = data.syspathfhadminadd		// 新增权限
        this.del = data.syspathfhadmindel		// 删除权限
        this.edit = data.syspathfhadminedit	// 修改权限
      }).catch((e) => {
        this.listLoading = false
      })
    },
    // 获取数据字典数据
    getDict: function() {
    },
    resetForm() {
      this.form = {
        SYSPATH_ID: '', // 备注1
        CORPINFO_ID: '', // 公司
        BASEPATH: '', // 企业端路径
        ADMINPATH: '', // admin端路径
        FILEPATH: '', // 文件路径
        CREATOR: '', // 添加人
        CREATTIME: '', // 添加时间
        OPERATOR: '', // 修改人
        OPERATTIME: '', // 修改时间
        ISDELETE: ''// 是否删除 1-是  0-否
      }
    }
  }
}

</script>
