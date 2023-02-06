<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading"
      :data="varList"
      :header-cell-style="{
        'font-weight': 'bold',
        'color': '#000'
      }"
      border
      fit
      highlight-current-row>
      <el-table-column type="index" label="序号" width="50" align="center" />
      <el-table-column label="名称" width="180">
        <template slot-scope="{row}">
          <div v-if="DEPARTMENT_ID == 0" class="link-type" @click="getQuery(row.DEPARTMENT_ID)">
            {{ row.NAME }}
          </div>
          <div v-else>
            {{ row.NAME }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="HEADMAN_NAME" label="部门负责人" />
      <el-table-column label="操作" align="center" width="390">
        <template slot-scope="scope">
          <el-button v-show="edit" type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(scope.row.DEPARTMENT_ID)">编辑</el-button>
          <el-button v-show="edit" type="success" icon="el-icon-edit" size="mini" @click="handleEditHead(scope.row.DEPARTMENT_ID)">设置主管部门</el-button>
          <el-button v-show="del" type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(scope.row.DEPARTMENT_ID, scope.row.NAME)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="page-btn-group">
      <div>
        <el-button v-show="add && DEPARTMENT_ID != '0'" type="primary" icon="el-icon-plus" @click="handleCreate(DEPARTMENT_ID)">新增</el-button>
        <el-button v-show="DEPARTMENT_ID != '0'" icon="el-icon-arrow-left" @click="getQuery(PARENT_ID)">返回</el-button>
      </div>
      <pagination :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </div>
    <el-dialog :visible.sync="dialogFormEdit" :title="dialogType=='edit'?'修改':'新增'" width="600px">
      <el-tag class="mark_up" size="medium">上级部门：{{ DEPARTMENT_ID == '0' ?'(无) 此项为顶级部门':pd.NAME }}</el-tag>
      <el-form ref="roleForm" :model="form" :rules="formRules" label-width="120px" style="width: 500px;">
        <el-form-item label="名称" prop="NAME">
          <el-input v-model="form.NAME" autocomplete="off" placeholder="这里输入名称..." />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormEdit = false">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogHeadEdit" :title="dialogType=='edit'?'修改':'新增'" width="600px">
      <el-tag class="mark_up" size="medium">部门：{{ form.NAME }}</el-tag>
      <el-form ref="roleForm" :model="form" label-width="120px" style="width: 500px;">
        <el-form-item label="主管" prop="NAME">
          <el-select v-model="form.HEADMAN" placeholder="请选择" clearable style="width: 300px" class="filter-item">
            <el-option v-for="item in userList" :key="item.USER_ID" :label="item.NAME" :value="item.USER_ID" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogHeadEdit = false">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import Pagination from '@/components/Pagination' // 通过 el-pagination二次打包
import { requestFN } from '@/utils/request'
export default {
  components: { Pagination },
  data() {
    return {
      treeLoading: false,
      treeData: [],
      defaultProps: {
        children: 'nodes',
        label: 'NAME'
      },
      userList: [], // 本部门人
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10
      },
      DEPARTMENT_ID: '0',
      PARENT_ID: '',	 // 上级ID
      total: 0,
      pd: [], // 上级部门
      varList: [],
      dialogFormEdit: false,
      dialogType: 'add',
      form: {
        DEPARTMENT_ID: '', // ID
        PARENT_ID: '', // 父ID
        NAME: '', // 名称
        DEP_ORDER: ''// 排序
      },
      add: false,
      del: false,
      edit: false,
      formRules: {
        NAME: [
          { required: true, message: '名称不能为空', trigger: 'blur' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ]
      },
      dialogHeadEdit: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getQuery(DEPARTMENT_ID) {
      this.DEPARTMENT_ID = DEPARTMENT_ID
      this.getList()
    },
    getList() {
      this.listLoading = true
      this.varList = []
      requestFN(
        '/department/list?showCount=' + this.listQuery.limit + '&currentPage=' + this.listQuery.page,
        {
          DEPARTMENT_ID: this.DEPARTMENT_ID
        }
      ).then((data) => {
        this.listLoading = false
        this.varList = data.varList
        this.pd = data.pd
        this.total = data.page.totalResult
        this.PARENT_ID = data.PARENT_ID
        this.hasButton()
      }).catch((e) => {
        this.listLoading = false
      })
    },
    handleCreate(parentId) {
      this.dialogType = 'add'
      this.resetForm()
      this.form.PARENT_ID = parentId
      this.dialogFormEdit = true
    },
    async handleEdit(id) {
      this.dialogType = 'edit'
      this.getData(id)
      this.dialogFormEdit = true
    },
    handleEditHead(id) {
      this.dialogType = 'edit'
      this.getData(id)
      this.getUserList(id)
      this.dialogHeadEdit = true
    },
    getData(id) {
      return new Promise((resolve) => {
        requestFN(
          '/department/goEdit',
          {
            DEPARTMENT_ID: id
          }
        ).then((data) => {
          this.form = Object.assign({}, data.pd) // copy obj
          resolve()
        }).catch((e) => {
          this.listLoading = false
        })
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
          '/department/delete',
          {
            DEPARTMENT_ID: id
          }
        ).then(() => {
          this.listLoading = false
          this.getQuery(this.DEPARTMENT_ID)
        }).catch((e) => {
          this.listLoading = false
        })
      }).catch(() => {
      })
    },
    confirm() {
      this.$refs.roleForm.validate(valid => {
        if (valid) {
          this.listLoading = true
          requestFN(
            '/department/' + this.dialogType,
            this.form
          ).then((data) => {
            this.listLoading = false
            this.dialogFormEdit = false
            this.dialogHeadEdit = false
            this.varList = []
            this.listQuery.page = 1
            this.getQuery(this.DEPARTMENT_ID)
          }).catch((e) => {
            this.listLoading = false
          })
        } else {
          return false
        }
      })
    },
    getUserList(id) {
      requestFN(
        '/user/listAll',
        {
          DEPARTMENT_ID: id
        }
      ).then((data) => {
        this.userList = data.userList
      }).catch((e) => {
      })
    },
    resetForm() {
      this.form = {
        DEPARTMENT_ID: '', // ID
        PARENT_ID: '', // 父ID
        NAME: '' // 名称
      }
    },
    hasButton: function() {
      var keys = 'department:add,department:del,department:edit,toExcel'
      requestFN(
        '/head/hasButton',
        {
          keys: keys
        }
      ).then((data) => {
        this.add = data.departmentfhadminadd
        this.del = data.departmentfhadmindel
        this.edit = data.departmentfhadminedit
      }).catch((e) => {
      })
    }
  }
}
</script>
<style lang="scss" scoped>
  .mark_up{
    margin-bottom:20px;
    margin-left: 110px;
  }
</style>
