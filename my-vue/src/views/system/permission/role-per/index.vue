<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button-group>
        <el-button type="primary" icon="el-icon-document-add" @click="handleCreate(0)">新增组</el-button>
        <el-button type="primary" icon="el-icon-edit" @click="handleUpdate()">修改组</el-button>
        <el-button v-if="ROLE_ID!=1" type="primary" icon="el-icon-delete" @click="handleDelete()">删除组</el-button>
        <el-button type="primary" icon="el-icon-collection-tag" @click="handleTree('menuqx',activeTab)">账号菜单权限</el-button>
      </el-button-group>
    </div>
    <el-tabs v-model="activeTab" type="border-card" @tab-click="handleClick">
      <el-tab-pane v-for="role in roleList" :name="role.role_ID" :key="role.role_ID" :label="role.role_NAME">
        <el-table
          v-loading="listLoading"
          :data="roleList_z"
          :header-cell-style="{
            'font-weight': 'bold',
            'color': '#000'
          }"
          border
          fit
          highlight-current-row>
          <el-table-column type="index" label="序号" width="50" align="center" />
          <el-table-column prop="role_NAME" label="角色" width="180" />
          <el-table-column prop="rnumber" label="编码" />
          <el-table-column label="增" align="center" width="60">
            <template slot-scope="{row}">
              <el-button icon="el-icon-document-add" circle @click="handleTree('b4Button',row.role_ID,'add_qx')" />
            </template>
          </el-table-column>
          <el-table-column label="删" align="center" width="60">
            <template slot-scope="{row}">
              <el-button icon="el-icon-delete" circle @click="handleTree('b4Button',row.role_ID,'del_qx')"/>
            </template>
          </el-table-column>
          <el-table-column label="改" align="center" width="60">
            <template slot-scope="{row}">
              <el-button icon="el-icon-edit" circle @click="handleTree('b4Button',row.role_ID,'edit_qx')"/>
            </template>
          </el-table-column>
          <el-table-column label="查" align="center" width="60">
            <template slot-scope="{row}">
              <el-button icon="el-icon-view" circle @click="handleTree('b4Button',row.role_ID,'cha_qx')"/>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="290">
            <template slot-scope="scope">
              <el-button type="success" icon="el-icon-collection-tag" size="mini" @click="handleTree('menuqx',scope.row.role_ID)">权限</el-button>
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleUpdate(scope.row.role_ID, scope.row.role_NAME)">编辑</el-button>
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(scope.row.role_ID, scope.row.role_NAME)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
    <div class="page-btn-group">
      <div>
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate(activeTab)">新增</el-button>
      </div>
      <pagination :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getRoleList" />
    </div>

    <el-dialog :visible.sync="dialogFormVisible" :title="dialogType==='edit'?'修改组':'新增组'" width="600px">
      <el-form ref="roleForm" :model="form" :rules="formRules" label-width="120px" style="width: 500px;">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" autocomplete="off" placeholder="这里输入名称..." />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmRole">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogFormTree" title="授权'新增'权限" width="500px">
      <el-tree
        ref="tree"
        :data="treeData"
        :props="defaultProps"
        show-checkbox
        node-key="id"/>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormTree = false">取 消</el-button>
        <el-button type="primary" @click="confirmTree">确 定</el-button>
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
      activeTab: '1',
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10
      },
      total: 0,
      roleList: [], // list 列出组(页面横向排列的一级组)
      roleList_z: [], // list 列出此组下架角色
      pd: [], // map
      ROLE_ID: '1',	// 角色ID
      dialogFormVisible: false,
      dialogType: 'add',
      dialogFormTree: false,
      form: {
        id: '',
        parent_id: '',
        name: ''
      },
      formRules: {
        name: [
          { required: true, message: '名不能为空', trigger: 'change' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ]
      },
      treeData: [],
      confirmTreeForm: {
        url: '',
        id: '',
        msg: ''
      },
      defaultProps: {
        children: 'nodes',
        label: 'name'
      }
    }
  },
  created() {
    this.getRoleList(this.ROLE_ID)
  },
  methods: {
    handleClick(tab) {
      this.ROLE_ID = tab.name
      this.roleList_z = []
      this.listQuery.page = 1
      this.getRoleList(this.ROLE_ID)
    },
    getRoleList(ROLE_ID) {
      this.listLoading = true
      this.ROLE_ID = ROLE_ID
      requestFN(
        '/role/list',
        {
          ROLE_ID: ROLE_ID
        }
      ).then((data) => {
        this.listLoading = false
        this.activeTab = ROLE_ID
        this.roleList = data.roleList
        this.roleList_z = data.roleList_z.filter((item, index) => index < this.listQuery.limit * this.listQuery.page && index >= this.listQuery.limit * (this.listQuery.page - 1))
        this.total = data.roleList_z.length
        this.pd = data.pd
      }).catch((e) => {
        this.listLoading = false
      })
    },
    handleCreate(parentId) {
      this.dialogType = 'add'
      this.form.name = ''
      this.form.parent_id = parentId
      this.dialogFormVisible = true
    },
    handleUpdate(roleId, roleName) {
      this.dialogType = 'edit'
      if (roleId) {
        this.form.id = roleId
        this.form.name = roleName
      } else {
        for (const role of this.roleList) {
          if (role.role_ID === this.activeTab) {
            this.form.id = role.role_ID
            this.form.name = role.role_NAME
          }
        }
      }
      this.dialogFormVisible = true
    },
    handleDelete(roleId, roleName) {
      if (roleId) {
        this.form.id = roleId
        this.form.name = roleName
      } else {
        for (const role of this.roleList) {
          if (role.role_ID === this.activeTab) {
            this.form.id = role.role_ID
            this.form.name = role.role_NAME
          }
        }
      }
      this.$confirm('确定要删除[' + this.form.name + ']吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/role/delete',
          {
            ROLE_ID: this.form.id
          }
        ).then(() => {
          this.listLoading = false
          this.roleList = []
          this.roleList_z = []
          this.listQuery.page = 1
          this.getRoleList('1')
        }).catch((e) => {
          this.listLoading = false
        })
      }).catch(() => {
      })
    },
    handleTree(url, roldId, msg) {
      if (url === 'menuqx') {
        this.confirmTreeForm.str = 'saveMenuqx'
      } else {
        this.confirmTreeForm.str = 'saveB4Button'
      }
      this.confirmTreeForm.id = roldId
      this.confirmTreeForm.msg = msg || ''
      this.listLoading = true
      requestFN(
        '/role/' + url,
        {
          ROLE_ID: roldId,
          msg: msg
        }
      ).then((data) => {
        this.listLoading = false
        this.treeData = JSON.parse(data.zTreeNodes)
        this.dialogFormTree = true
        this.$nextTick(() => {
          var checkedMenuIds = []
          this.getCheckNode(this.treeData, checkedMenuIds)
          this.$refs.tree.setCheckedKeys(checkedMenuIds) // 设置目前勾选的节点
        })
      }).catch((e) => {
        this.listLoading = false
      })
    },
    confirmRole() {
      this.$refs.roleForm.validate(valid => {
        if (valid) {
          this.listLoading = true
          requestFN(
            '/role/' + this.dialogType,
            {
              ROLE_ID: this.form.id,
              ROLE_NAME: this.form.name,
              PARENT_ID: this.form.parent_id
            }
          ).then((data) => {
            this.listLoading = false
            this.dialogFormVisible = false
            this.roleList = []
            this.roleList_z = []
            this.listQuery.page = 1
            this.getRoleList(this.form.parent_id)
            // if (data.pd.PARENT_ID === '0') {
            //
            // } else {
            //   this.getRoleList(this.form.parent_id)
            // }
          }).catch((e) => {
            this.listLoading = false
          })
        } else {
          return false
        }
      })
    },
    confirmTree() {
      this.listLoading = true
      const ids = this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys())
      requestFN(
        '/role/' + this.confirmTreeForm.str,
        {
          ROLE_ID: this.confirmTreeForm.id,
          menuIds: ids.join(','),
          msg: this.confirmTreeForm.msg
        }
      ).then((data) => {
        this.listLoading = false
        this.dialogFormTree = false
      }).catch((e) => {
        this.listLoading = false
      })
    },
    getCheckNode(menuList, checkedMenuIds) {
      if (menuList != null && menuList.length > 0) {
        for (let i = 0; i < menuList.length; i++) {
          const menu = menuList[i]
          if ((menu.nodes == null || menu.nodes.length === 0) && menu.checked.toString() === 'true') {
            checkedMenuIds.push(menu.id)
          }
          if (menu.nodes != null && menu.nodes.length > 0) {
            this.getCheckNode(menu.nodes, checkedMenuIds)
          }
        }
      }
    }
  }
}
</script>

