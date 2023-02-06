<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="KEYWORDS" placeholder="搜索" class="filter-item" style="width: 200px;" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="getQuery">
        搜索
      </el-button>
    </div>
    <el-table ref="multipleTable" :data="varList" :header-cell-style="{'font-weight': 'bold','color': '#000'}" tooltip-effect="dark" border fit highlight-current-row>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" label="序号" width="50" align="center" />
      <el-table-column prop="USERNAME" label="用户名" />
      <el-table-column prop="NAME" label="姓名" />
      <!--      <el-table-column prop="DEPARTMENT_NAME" label="部门" />-->
      <!--      <el-table-column prop="POST_NAME" label="岗位" />-->
      <el-table-column prop="ROLE_NAME" label="角色" />
      <el-table-column prop="DEPARTMENT_NAME" label="部门" />
      <el-table-column prop="EMAIL" label="邮箱" />
      <el-table-column prop="CREATTIME" label="创建时间" />
      <el-table-column prop="IP" label="上次登录IP" />
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="{row}">
          <el-button v-show="edit" type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(row)">编辑</el-button>
          <el-button v-show="del" type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(row.USER_ID,row.USERNAME)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="page-btn-group">
      <div>
        <template>
          <el-button v-show="add" type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
          <el-button v-show="del" type="danger" icon="el-icon-delete" plain @click="makeAll('0')">删除</el-button>
        </template>
      </div>
      <pagination :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </div>
    <el-dialog :visible.sync="dialogFormEdit" :title="dialogType==='editUser'?'修改':'新增'" width="600px">
      <el-form ref="form" :rules="rules" :model="pd" label-width="110px" style="width: 500px;">
        <el-form-item :disabled="dialogType == 'editUser'" label="角色" prop="ROLE_ID">
          <el-select v-model="pd.ROLE_ID" :disabled="dialogType == 'editUser'" clearable placeholder="请选择角色">
            <el-option v-for="item in roleList" :key="item.role_ID" :label="item.role_NAME" :value="item.role_ID" />
          </el-select>
        </el-form-item>
        <el-form-item label="部门" prop="DEPARTMENT_ID">
          <SelectTree
            ref="deptTree"
            :clearable="false"
            :options="treeData"
            :props="defaultProps"
            v-model="pd.DEPARTMENT_ID"
            placeholder="请选择部门"
          />
        </el-form-item>
        <el-form-item label="用户名" prop="USERNAME">
          <el-input v-model="pd.USERNAME" :disabled="dialogType == 'editUser'" placeholder="这里输入用户名..." />
        </el-form-item>
        <el-form-item label="姓名" prop="NAME">
          <el-input v-model="pd.NAME" placeholder="这里输入姓名..." />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="pd.EMAIL" placeholder="这里输入备注邮箱..." />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="pd.BZ" type="textarea" placeholder="这里输入备注说明..." />
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
import SelectTree from '@/components/SelectTree'
export default {
  components: { Pagination, SelectTree },
  directives: { waves },
  data() {
    var hasUser = (rule, value, callback) => {
      requestFN(
        '/user/hasUser',
        {
          USERNAME: value,
          tm: new Date().getTime()
        }
      ).then((data) => {
        if (data.result == 'success') {
          callback()
        } else {
          // console.info('用户名重复')
          callback(new Error('用户名重复'))
        }
      }).catch((e) => {

      })
    }
    return {
      listQuery: {
        page: 1,
        limit: 10
      },
      total: 0,
      varList: [],
      KEYWORDS: '',
      dialogFormEdit: false,
      dialogType: 'add',
      treeData: [],
      defaultProps: {
        value: 'id',
        children: 'nodes',
        label: 'name'
      },
      rules: {
        ROLE_ID: [
          { required: true, message: '角色不能为空', trigger: 'blur' }
        ],
        USERNAME: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' },
          { validator: hasUser, trigger: 'blur' }
        ],
        NAME: [
          { required: true, message: '姓名不能为空', trigger: 'blur' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }]

      },
      pd: [],
      roleList: [],
      multipleSelectionAll: [], // 所有选中的数据包含跨页数据
      multipleSelection: [], // 当前页选中的数据
      fromExcel: false, // 从excel导入权限
      toExcel: false,	// 导出到excel权限
      add: false,		// 新增按钮
      del: false,		// 删除按钮
      edit: false,	// 修改按钮
      hasUserRet: false
    }
  },
  created() {
    this.getList()
    this.getTreeList()
    this.hasButton()
  },
  methods: {
    getList() {
      this.listLoading = true
      requestFN(
        '/user/list?showCount=' + this.listQuery.limit + '&currentPage=' + this.listQuery.page,
        {
          KEYWORDS: this.KEYWORDS
        }
      ).then((data) => {
        this.listLoading = false
        this.varList = data.userList
        console.log(data.page)
        this.total = data.page.totalResult
      })
        .catch((e) => {
          this.listLoading = false
        })
    },
    getroleList() {
      requestFN(
        '/user/goAddUser',
        {
        }
      ).then((data) => {
        this.roleList = data.roleList
      })
        .catch((e) => {
        })
    },
    getTreeList() {
      requestFN(
        '/department/listTree',
        {
        }
      ).then((data) => {
        this.treeData = JSON.parse(data.zTreeNodes)
      }).catch((e) => {
      })
    },
    handleAdd() {
      this.dialogFormEdit = true
      this.pd = {}
      this.getroleList()
      this.dialogType = 'saveUser'
    },
    confirm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.listLoading = true
          requestFN(
            '/user/' + this.dialogType,
            {
              USER_ID: this.pd.USER_ID,
              ROLE_ID: this.pd.ROLE_ID,
              DEPARTMENT_ID: this.pd.DEPARTMENT_ID,
              USERNAME: this.pd.USERNAME,
              NAME: this.pd.NAME,
              PHONE: this.pd.PHONE,
              EMAIL: this.pd.EMAIL,
              BZ: this.pd.BZ,
              tm: new Date().getTime()
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
          '/user/deleteUser',
          {
            USER_ID: id
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
    makeAll(msg) {
      const _selectData = this.$refs.multipleTable.selection
      console.info('_selectData')
      console.info(_selectData)
      if (_selectData == null || _selectData.length == 0) {
        this.$message({
          message: '请选中要删除的项...',
          type: 'error'
        })
        return false
      }
      const ids = _selectData.map((item, index) => {
        return item.USER_ID
      }).join(',')
      var tishi = ''
      if (msg == '0') {
        tishi = '确定要删除选中的数据吗?'
      } else if (msg == '1') {
        tishi = '确定要给选中的用户发送站内信吗?'
      } else {
        this.$message({
          message: '请选择操作类型...',
          type: 'error'
        })
        return false
      }
      this.$confirm(tishi, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (msg == '0') {
          this.listLoading = true
          requestFN(
            '/user/deleteAllUser',
            {
              USER_IDS: ids
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
        }
      }).catch(() => {
      })
    },
    getQuery() {
      this.$refs.multipleTable.clearSelection()
      this.getList()
    },
    handleEdit(row) {
      this.getroleList()
      this.dialogType = 'editUser'
      this.pd = Object.assign({}, row)
      this.dialogFormEdit = true
    },
    hasButton() {
      var keys = 'user:add,user:del,user:edit,fhSms,email,fromExcel,toExcel'
      requestFN(
        '/head/hasButton',
        {
          keys: keys, tm: new Date().getTime()
        }
      ).then((data) => {
        if (data.result == 'success') {
          this.add = data.userfhadminadd	// 增
          this.del = data.userfhadmindel	// 删
          this.edit = data.userfhadminedit	// 改
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
    }
  }
}
</script>

