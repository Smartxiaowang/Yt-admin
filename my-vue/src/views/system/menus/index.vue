<template>
  <div class="icons-container">
    <el-container>
      <el-aside width="300px" style="background-color:#fff">
        <el-input
          v-model="filterText"
          placeholder="输入关键字进行过滤"
          style="margin:10px 0"/>
        <el-tree
          v-loading="treeLoading"
          ref="tree"
          :data="treeData"
          :props="defaultProps"
          :filter-node-method="filterNode"
          class="filter-tree"
          @node-click="handleNodeClick"/>
      </el-aside>
      <el-main>
        <div class="filter-btn-group">
          <div>
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
            <el-button v-show="MENU_ID != '0'" icon="el-icon-arrow-left" @click="getList(pd.PARENT_ID)">返回</el-button>
          </div>
        </div>
        <el-table
          v-loading="listLoading"
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
          <el-table-column type="index" prop="numb" label="序号" width="50" align="center" />
          <el-table-column label="名称" >
            <template slot-scope="{row}">
              <div class="link-type" @click="getList(row.menu_ID)">
                <svg-icon :icon-class="row.menu_ICON" class-name="disabled" /> {{ row.menu_NAME }} <i class="el-icon-arrow-right" />
              </div>
            </template>
          </el-table-column>
          <el-table-column label="资源路径">
            <template slot-scope="{row}">
              {{ row.menu_URL == '#' ? '&nbsp;&nbsp;&nbsp;&nbsp;(无)': row.menu_URL }}
            </template>
          </el-table-column>
          <el-table-column label="权限标识">
            <template slot-scope="{row}">
              {{ '' == row.shiro_KEY || null == row.shiro_KEY ?'(无)': row.shiro_KEY }}
            </template>
          </el-table-column>
          <el-table-column label="状态" width="100" >
            <template slot-scope="{row}">
              <template v-if="row.menu_STATE==1">
                <el-tag size="mini">
                  显示
                </el-tag>
              </template>
              <template v-else>
                <el-tag size="mini" type="info">
                  隐藏
                </el-tag>
              </template>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="300">
            <template slot-scope="{row}">
              <el-button v-show="MENU_ID == '0'" type="success" icon="el-icon-picture-outline" size="mini" @click="handleEditIcon(row.menu_ID)">图标</el-button>
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(row.menu_ID)">编辑</el-button>
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(row.menu_ID, row.menu_NAME)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

      </el-main>
    </el-container>

    <el-dialog :visible.sync="dialogFormAdd" :title="dialogType==='edit'?'修改':'新增'" width="600px">
      <el-tag class="mark_up" size="medium">上级菜单：{{ MENU_ID == '0' ?'(无) 此项为顶级菜单':pds.MENU_NAME }}</el-tag>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px" style="width: 500px;">
        <el-form-item label="菜单名称" prop="MENU_NAME">
          <el-input v-model="form.MENU_NAME" placeholder="这里输入菜单名称..." />
        </el-form-item>
        <el-form-item label="菜单链接">
          <el-input v-model="form.MENU_URL" placeholder="这里输入菜单链接..." />
        </el-form-item>
        <el-form-item label="组件路径">
          <el-input v-model="form.COMPONENT" placeholder="这里输入组件路径..." />
        </el-form-item>
        <el-form-item label="权限标识" prop="SHIRO_KEY">
          <el-input v-model="form.SHIRO_KEY" placeholder="这里输入权限标识..." />
        </el-form-item>
        <el-form-item v-show="MENU_ID == '0'" label="显示模式" prop="SHOW_MODEL">
          <el-input v-model="form.SHOW_MODEL" placeholder="这里输入显示模式..." />
        </el-form-item>
        <el-form-item label="菜单序号" prop="MENU_ORDER">
          <el-input v-model.number="form.MENU_ORDER" placeholder="这里输入菜单序号..." />
        </el-form-item>
        <el-form-item label="菜单状态">
          <el-radio-group v-model="form.MENU_STATE">
            <el-radio :label="1">显示</el-radio>
            <el-radio :label="0">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormAdd = false">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogEditIcon" title="编辑图标" width="960px">
      <div class="grid">
        <div v-for="item of svgIcons" :key="item">
          <div class="icon-item" @click="confirmIcon(item)">
            <svg-icon :icon-class="item" class-name="disabled" />
            <span>{{ item }}</span>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogEditIcon = false">取 消</el-button>
        <!--        <el-button type="primary" @click="confirm">确 定</el-button>-->
      </div>
    </el-dialog>
  </div>
</template>

<script>

import svgIcons from './svg-icons'
import { requestFN } from '@/utils/request'
export default {
  name: 'Icons',
  data() {
    return {
      listLoading: false,			// 加载状态
      treeLoading: false,
      svgIcons,
      radio: 1,
      dialogFormAdd: false,
      dialogEditIcon: false,
      dialogType: 'add',
      // 树形菜单
      filterText: '',
      treeData: [],
      defaultProps: {
        children: 'nodes',
        label: 'name'
      },
      // 列表
      varList: [],	// list
      MENU_ID: '0',	// 主键ID
      pd: [],
      form: {
        MENU_ID: '',	 // 主键ID
        MENU_TYPE: '1', // 类型
        MENU_STATE: 1,	 // 状态
        PARENT_ID: '',	 // 上级ID
        MENU_NAME: '',	 // 菜单名称
        MENU_URL: '',	 // 菜单链接
        COMPONENT: '', // 组件路径
        SHIRO_KEY: '(无)', // 权限标识
        SHOW_MODEL: '', // 显示模式
        MENU_ORDER: ''	 // 菜单序号
      },
      pds: [],
      add: false,
      del: false,
      edit: false,
      rules: {
        MENU_NAME: [
          { required: true, message: '菜单名称不能为空', trigger: 'change' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        MENU_ORDER: [
          { required: true, message: '菜单序号不能为空', trigger: 'blur' },
          { type: 'number', message: '菜单序号必须为数字' }
        ]
      }
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.getTreeList()
    this.getList(this.MENU_ID)
  },
  methods: {
    filterNode(value, data) {
      if (!value) return true
      return data.name.indexOf(value) !== -1
    },
    handleNodeClick(node, data, value) {
      this.handleEdit(node.id)
    },
    handleAdd() {
      this.dialogType = 'add'
      this.resetForm()
      requestFN(
        '/menu/toAdd',
        {
          MENU_ID: this.MENU_ID
        }
      ).then((data) => {
        this.form.PARENT_ID = this.MENU_ID
        this.pds = data.pds	|| []					// 父级菜单信息
      }).catch((e) => {

      })
      this.dialogFormAdd = true
    },
    handleEdit(MENU_ID) {
      this.dialogType = 'edit'
      requestFN(
        '/menu/toEdit',
        {
          MENU_ID: MENU_ID
        }
      ).then((data) => {
        this.form = Object.assign({}, data.pd) // copy obj
        this.form.MENU_ID = MENU_ID
        this.form.PARENT_ID = data.pd.PARENT_ID
        this.pds = data.pds || []						// 父级菜单信息
      }).catch((e) => {
        this.listLoading = false
      })
      this.dialogFormAdd = true
    },
    confirm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.listLoading = true
          requestFN(
            '/menu/' + this.dialogType,
            this.form
          ).then((data) => {
            this.listLoading = false
            this.dialogFormAdd = false
            this.getTreeList()
            this.getList(this.MENU_ID)
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
          '/menu/delete',
          {
            MENU_ID: id
          }
        ).then(() => {
          this.listLoading = false
          this.getTreeList()
          this.getList(this.MENU_ID)
        }).catch((e) => {
          this.listLoading = false
        })
      }).catch(() => {
      })
    },
    handleEditIcon(MENU_ID) {
      this.dialogEditIcon = true
      this.form.MENU_ID = MENU_ID
    },
    confirmIcon(symbol) {
      const MENU_ICON = symbol
      this.listLoading = true
      requestFN(
        '/menu/editicon',
        {
          MENU_ID: this.form.MENU_ID,
          MENU_ICON: MENU_ICON
        }
      ).then((data) => {
        this.listLoading = false
        this.dialogEditIcon = false
        this.getList(this.MENU_ID)
      }).catch((e) => {
        this.listLoading = false
      })
    },
    generateIconCode(symbol) {
      return `<svg-icon icon-class="${symbol}" />`
    },
    getTreeList() {
      this.treeLoading = true
      requestFN(
        '/menu/listAllMenu',
        {

        }
      ).then((data) => {
        this.treeLoading = false
        this.treeData = JSON.parse(data.zTreeNodes)
      }).catch((e) => {
        this.treeLoading = false
      })
    },
    getList(MENU_ID) {
      this.listLoading = true
      this.varList = []
      this.MENU_ID = MENU_ID
      requestFN(
        '/menu/list',
        {
          MENU_ID: this.MENU_ID
        }
      ).then((data) => {
        this.listLoading = false
        this.varList = data.menuList
        this.pd = data.pd || []
        this.hasButton()
      }).catch((e) => {
        this.listLoading = false
      })
    },
    resetForm() {
      this.form = {
        MENU_ID: '',	 // 主键ID
        MENU_TYPE: '1', // 类型
        MENU_STATE: 1,	 // 状态
        PARENT_ID: '',	 // 上级ID
        MENU_NAME: '',	 // 菜单名称
        MENU_URL: '',	 // 菜单链接
        COMPONENT: '', // 组件路径
        SHIRO_KEY: '(无)', // 权限标识
        SHOW_MODEL: '', // 显示模式
        MENU_ORDER: ''	 // 菜单序号
      }
    },
    hasButton: function() {
      var keys = 'menu:add,menu:del,menu:edit'
      requestFN(
        '/head/hasButton',
        {
          keys: keys
        }
      ).then((data) => {
        this.add = data.menufhadminadd
        this.del = data.menufhadmindel
        this.edit = data.menufhadminedit
      }).catch((e) => {
        this.listLoading = false
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.el-dialog__body{
  padding: 0;
  background: red;
}
.mark_up{
  margin-bottom:20px;
  margin-left: 110px;
}
.icons-container {
  margin: 0;
  overflow: hidden;

  .grid {
    position: relative;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  }

  .icon-item {
    margin-bottom: 10px;
    height: 70px;
    text-align: center;
    width: 100px;
    float: left;
    font-size: 24px;
    color: #24292e;
    cursor: pointer;
    span {
    display: block;
    font-size: 14px;
    margin-top: 10px;
    }

  }

  .disabled {
    pointer-events: none;
  }
}
</style>
