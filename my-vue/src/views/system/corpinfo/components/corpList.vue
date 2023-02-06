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
      <el-table-column prop="CORP_NAME" label="企业名称" />
      <!--      <td v-if="GetNumberOfDays(data.USE_DATE_END) < 90&&GetNumberOfDays(data.USE_DATE_END) > 0"><span class="color-yellow">{{data.CORP_NAME}}</span></td>-->
      <!--      <td v-else-if="GetNumberOfDays(data.USE_DATE_END) <=0"><span class="color-red">{{data.CORP_NAME}}</span></td>-->
      <!--      <td v-else>{{data.CORP_NAME}}</td>-->
      <el-table-column prop="USE_DATE_END" label="有效使用结束时间" />
      <el-table-column prop="COMPANY_AREA" label="所属区域" />
      <el-table-column prop="ADDRESS" label="注册地址" />
      <el-table-column label="操作" align="center" width="600">
        <template slot-scope="{row}">
          <el-button v-show="edit" type="success" icon="el-icon-edit" size="mini" @click="goLogin(row.CORPINFO_ID)">登入</el-button>
          <el-button v-show="edit" type="primary" icon="el-icon-edit" size="mini" @click="resetPwd(row.CORPINFO_ID)">重置密码</el-button>
          <el-button v-show="edit" type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(row.CORPINFO_ID)">修改</el-button>
          <el-button v-show="del" type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(row.CORPINFO_ID)">删除</el-button>
          <el-button v-if="row.ISUSE=='0'" type="danger" icon="el-icon-delete" size="mini" @click="updateISuse(row.CORPINFO_ID)">禁用</el-button>
          <el-button v-if="row.ISUSE=='1'" type="success" icon="el-icon-delete" size="mini" @click="goupdateISuse(row.CORPINFO_ID)">启用</el-button>
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
        <el-form-item label="有效使用起始时间" prop="USE_DATE_START">
          <el-date-picker
            v-model="form.USE_DATE_START"
            type="date"
            placeholder="选择日期"/>
        </el-form-item>
        <el-form-item label="有效使用结束时间" prop="USE_DATE_END">
          <el-date-picker
            v-model="form.USE_DATE_END"
            type="date"
            placeholder="选择日期"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormEdit = false">取 消</el-button>
        <el-button type="primary" @click="goupdateISuseSave">确 定</el-button>
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
      form: { },
      multipleSelectionAll: [], // 所有选中的数据包含跨页数据
      multipleSelection: [], // 当前页选中的数据
      dialogFormEdit: false,
      dialogType: 'add',
      rules: {
        USE_DATE_START: [{ required: true, message: '有效使用起始时间不能为空', trigger: 'blur' }],
        USE_DATE_END: [{ required: true, message: '有效使用结束时间不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList(this.ROLE_ID)
  },
  methods: {
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
    // 添加
    handleAdd() {
      this.dialogType = 'add'
      this.resetForm()
      this.getDict()
      this.dialogFormEdit = true
    },
    // 修改
    handleEdit(ID) {
      this.$parent.CORPINFO_ID = ID
      this.$parent.activeName = 'corpEdit'
      // this.getDict()
      // this.dialogType = 'edit'
      // requestFN(
      //   '/corpinfo/goEdit',
      //   {
      //     CORPINFO_ID: ID
      //   }
      // ).then((data) => {
      //   this.form = Object.assign({}, data.pd) // copy obj
      // }).catch((e) => {
      //   this.listLoading = false
      // })
      // this.dialogFormEdit = true
    },
    // 保存
    confirm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.listLoading = true
          requestFN(
            '/corpinfo/' + this.dialogType,
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
          '/corpinfo/delete',
          {
            CORPINFO_ID: id
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
        return item.CORPINFO_ID
      }).join(',')

      this.$confirm('确定要删除选中的数据吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/corpinfo/deleteAll',
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
    resetForm() {
      this.form = {
        CORPINFO_ID: '' // 备注1
      }
    },
    // 登录企业
    goLogin: function(id) {
      this.$confirm('确定要进入系统吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/corpinfo/goLogin',
          {
            CORPINFO_ID: id
          }
        ).then((data) => {
          this.listLoading = false
          if (data.USERNAME != 'admin') {
            window.open('https://sk.xtsxjzx.com/bi/loading.html?qa=' + encodeURI(data.users.USERNAME) + '&zy=' + encodeURI(data.users.PASSWORD), '_blank')
          } else {
            window.open('https://qask.qhdsafety.com/bi/loading.html?qa=' + encodeURI(data.users.USERNAME) + '&zy=' + encodeURI(data.users.PASSWORD), '_blank')
          }
        }).catch((e) => {
          this.listLoading = false
        })
      }).catch(() => {
      })
    },
    resetPwd(id) {
      this.listLoading = true
      requestFN(
        '/corpinfo/resetPwd',
        {
          corpInfo: id,
          tm: new Date().getTime()
        }
      ).then((data) => {
        if (data.result == 'success') {
          this.$message({
            message: '重置成功',
            type: 'success'
          })
        } else {
          alert(data.msg)
          this.$message({
            message: data.msg,
            type: 'success'
          })
        }
        this.listLoading = false
      }).catch((e) => {
        this.listLoading = false
      })
    },
    updateISuse(id) {
      this.$confirm('确定要禁用吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/corpinfo/updateISuse',
          {
            CORPINFO_ID: id
          }
        ).then(() => {
          this.$message({
            message: '禁用成功',
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
    goupdateISuse(id) {
      // dialogFormEdit" :title="dialogType==='edit'
      this.dialogFormEdit = true
      this.dialogType = 'edit'
      this.form.CORPINFO_ID = id
    },
    goupdateISuseSave() {
      if (this.form.USE_DATE_END < this.form.USE_DATE_START) {
        this.$message({
          message: '结束时间不能早于开始时间',
          type: 'error'
        })
        this.form.USE_DATE_END = ''
        return false
      }
      this.listLoading = true
      requestFN(
        '/corpinfo/updateuse',
        {
          CORPINFO_ID: this.form.CORPINFO_ID,
          USE_DATE_START: this.form.USE_DATE_START,
          USE_DATE_END: this.form.USE_DATE_END
        }
      ).then((data) => {
        if (data.result == 'success') {
          this.$message({
            message: '保存成功',
            type: 'info'
          })
          this.varList = []
          this.getList()
        }
        this.listLoading = false
        this.dialogFormEdit = false
      }).catch((e) => {
        this.listLoading = false
      })
    }
  }
}

</script>
