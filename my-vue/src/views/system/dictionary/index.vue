<template>
  <div class="icons-container">
    <el-container>
      <el-aside width="300px" style="background-color:#fff">
        <el-input v-model="filterText" placeholder="输入关键字进行过滤" style="margin:10px 0" />
        <el-tree ref="tree" :data="nodeData" :props="defaultProps" :filter-node-method="filterNode" :load="getTreeNode" lazy class="filter-tree" accordion @node-click="handleNodeClick" />
      </el-aside>
      <el-main>
        <div class="filter-btn-group">
          <div>
            <el-button v-show="add" type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
            <el-button v-show="DICTIONARIES_ID != '0'" icon="el-icon-arrow-left" @click="getList(PARENT_ID,1)">返回</el-button>
          </div>
        </div>
        <el-table v-loading="listLoading" ref="multipleTable" :data="varList" :row-key="getRowKey" :header-cell-style="{'font-weight': 'bold','color': '#000'}" tooltip-effect="dark" border fit highlight-current-row>
          <el-table-column :reserve-selection="true" type="selection" width="55" align="center" />
          <el-table-column type="index" label="序号" width="50" align="center" />
          <el-table-column label="名称">
            <template slot-scope="{row}">
              <div class="link-type" @click="getList(row.DICTIONARIES_ID,1)">
                <svg-icon :icon-class="row.NAME" class-name="disabled" /> {{ row.NAME }} <i class="el-icon-arrow-right" />
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="NAME_EN" label="英文" />
          <el-table-column prop="BIANMA" label="编码" />
          <el-table-column prop="DICTIONARIES_ID" label="ID" />
          <el-table-column prop="ORDER_BY" label="排序" width="50" align="center" />
          <el-table-column label="操作" align="center" width="200">
            <template slot-scope="{row}">
              <el-button v-show="edit" type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(row)">编辑</el-button>
              <el-button v-show="del" type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(row.DICTIONARIES_ID, row.NAME)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="page-btn-group">
          <div />
          <pagination :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
        </div>
      </el-main>
    </el-container>

    <el-dialog :visible.sync="dialogFormAdd" :title="msg==='edit'?'修改':'新增'" width="580px">
      <el-tag class="mark_up" size="medium">上级菜单：{{ PARENT_NAME == '0' ?'(无) 此项为顶级菜单':PARENT_NAME }}</el-tag>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" style="width: 500px;">
        <el-form-item label="名称" prop="NAME">
          <el-input v-model="form.NAME" placeholder="这里输入名称..." />
        </el-form-item>
        <el-form-item label="英文" prop="NAME_EN">
          <el-input v-model="form.NAME_EN" placeholder="这里输入英文名称..." />
        </el-form-item>
        <el-form-item label="编码" prop="BIANMA">
          <el-input v-model="form.BIANMA" :disabled="msg == 'edit'" placeholder="这里输入编码（不重复，禁止修改）" />
        </el-form-item>
        <el-form-item label="排序" prop="ORDER_BY">
          <el-input v-model.number="form.ORDER_BY" placeholder="这里输入排序..." />
        </el-form-item>
        <el-form-item label="排查表" prop="TBSNAME">
          <el-input v-model="form.TBSNAME" placeholder="这里输入表名，多个用逗号分隔（非必录）" />
        </el-form-item>
        <el-form-item label="关联字段" prop="TBFIELD">
          <el-input v-model="form.TBFIELD" placeholder="这里输入关联字段，默认BIANMA（非必录）" />
        </el-form-item>
        <el-form-item label="备注" prop="BZ">
          <el-input v-model="form.BZ" :autosize="{ minRows: 1}" type="textarea" placeholder="这里输入备注..." />
        </el-form-item>
        <el-form-item label="禁止删除">
          <el-radio-group v-model="form.YNDEL">
            <el-radio label="yes">是</el-radio>
            <el-radio label="no">否</el-radio>
          </el-radio-group>
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
export default {
  components: { Pagination },
  data() {
    return {
      radio: 3,
      form: {
        NAME: '',
        NAME_EN: '',
        BIANMA: '',
        ORDER_BY: '',
        TBSNAME: '',
        TBFIELD: '',
        BZ: '',
        YNDEL: '',
        PARENT_ID: ''
      },
      rules: {
        NAME: [
          { required: true, message: '字典名称不能为空', trigger: 'change' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        NAME_EN: [
          { required: true, message: '字典英文名称不能为空', trigger: 'change' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        BIANMA: [
          { required: true, message: '字典编码名称不能为空', trigger: 'change' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        ORDER_BY: [
          { required: true, message: '字典序号名称不能为空', trigger: 'change' },
          { type: 'number', message: '字典序号必须为数字' }
        ]
      },
      filterText: '',
      nodeData: [],
      defaultProps: {
        value: 'DICTIONARIES_ID',
        children: 'nodes',
        label: 'NAME'
      },
      treeData: [],
      dialogFormAdd: false,
      dialogEditIcon: false,
      varList: [],
      listQuery: {
        page: 1,
        limit: 10
      },
      total: 0,
      DICTIONARIES_ID: '0',
      ALL_ID: '0',
      PARENT_ID: '0',
      PARENT_NAME: '0',
      TYPE: 1,
      msg: 'add',
      add: false,
      del: false,
      edit: false,
      resMsg: ''
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    // 初始化方法
    this.dialogFormEdit = false
    this.varList = []
    this.listQuery.page = 1
    // this.getMenuList()
    this.getList(this.DICTIONARIES_ID, 1)
    this.hasButton()
  },
  methods: {
    getRowKey(row) {
      return row.FHBUTTON_ID
    },
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    handleAdd() {
      this.dialogFormAdd = true
      this.resetForm()
      this.form.YNDEL = 'no'
      if (this.$refsform !== undefined) {
        this.$refs.form.resetFields()
      }
      this.msg = 'add'
    },
    getTreeNode(node, resolve) {
      if (node.level === 0) {
        requestFN(
          '/dictionaries/getLevels',
          {
            DICTIONARIES_ID: 0
          }
        ).then((data) => {
          resolve(data.list)
        }).catch((e) => {
        })
      } else {
        requestFN(
          '/dictionaries/getLevels',
          {
            DICTIONARIES_ID: node.data.DICTIONARIES_ID
          }
        ).then((data) => {
          resolve(data.list)
        }).catch((e) => {
        })
      }
    },
    // 获取左侧树
    getMenuList() {
      this.listLoading = true
      requestFN(
        '/dictionaries/firstListAll',
        {
        }
      ).then((data) => {
        this.listLoading = false
        this.treeData = JSON.parse(data.zTreeNodes)
        console.info(this.treeData)
      })
        .catch((e) => {
          this.listLoading = false
        })
    },
    getList(DICTIONARIES_ID, TYPE) {
      this.listLoading = true
      this.TYPE = TYPE
      if (TYPE == 1) {
        this.DICTIONARIES_ID = DICTIONARIES_ID
      } else if (this.ALL_ID == '') {
        this.DICTIONARIES_ID = ''
      }
      requestFN(
        '/dictionaries/list?showCount=' + this.listQuery.limit + '&currentPage=' + this.listQuery.page,
        {
          KEYWORDS: this.KEYWORDS,
          DICTIONARIES_ID: this.DICTIONARIES_ID
        }
      ).then((data) => {
        this.listLoading = false
        this.varList = data.varList
        this.total = data.page.totalResult
        if (TYPE == 1 || this.ALL_ID != '') {
          this.PARENT_ID = data.PARENT_ID
          if (data.PARENT_NAME) {
            this.PARENT_NAME = data.PARENT_NAME
          }
          this.ALL_ID = this.DICTIONARIES_ID
        } else {
          this.DICTIONARIES_ID = '0'
          this.PARENT_ID = '0'
        }
      })
        .catch((e) => {
          this.listLoading = false
        })
    },
    handleNodeClick(node, data, value) {
      this.getList(node.DICTIONARIES_ID, 1)
    },
    resetForm() {
      this.form = {
        NAME: '',
        NAME_EN: '',
        BIANMA: '',
        ORDER_BY: '',
        TBSNAME: '',
        TBFIELD: '',
        BZ: '',
        YNDEL: '',
        PARENT_ID: '',
        DICTIONARIES_ID: ''
      }
    },
    confirm() {
      this.form.PARENT_ID = this.DICTIONARIES_ID
      this.$refs.form.validate(valid => {
        if (valid) {
          this.listLoading = true
          requestFN(
            '/dictionaries/' + this.msg,
            this.form
          ).then((data) => {
            if (data.result == 'fail') {
              this.$message({
                message: data.resMsg,
                type: 'error'
              })
              this.erroMsg = Math.random()
              this.$nextTick(() => { this.erroMsg = '' })
              return false
            }
            this.listLoading = false
            this.dialogFormAdd = false
            // this.getMenuList()
            this.getList(this.DICTIONARIES_ID, 1)

            this.msg = 'add'
          }).catch((e) => {
            console.info('1111')
            console.info(e)
            this.resMsg = e.resMsg
            this.listLoading = false
          })
        } else {
          console.info('false')
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
          '/dictionaries/delete',
          {
            DICTIONARIES_ID: id
          }
        ).then((data) => {
          if (data.result == 'success') {
            this.listLoading = false
            this.getMenuList()
            this.getList(this.DICTIONARIES_ID, 1)
          }
        }).catch((e) => {
          this.listLoading = false
        })
      }).catch(() => {
      })
    },
    handleEdit(data) {
      this.form = {
        NAME: data.NAME,
        NAME_EN: data.NAME_EN,
        BIANMA: data.BIANMA,
        ORDER_BY: data.ORDER_BY,
        TBSNAME: data.TBSNAME,
        TBFIELD: data.TBFIELD,
        BZ: data.BZ,
        YNDEL: data.YNDEL,
        PARENT_ID: data.PARENT_ID,
        DICTIONARIES_ID: data.DICTIONARIES_ID
      }
      this.dialogFormAdd = true
      this.msg = 'edit'
    },
    hasButton: function() {
      var keys = 'dictionaries:add,dictionaries:del,dictionaries:edit'
      requestFN(
        '/head/hasButton',
        {
          keys: keys
        }
      ).then((data) => {
        this.add = data.dictionariesfhadminadd
        this.del = data.dictionariesfhadmindel
        this.edit = data.dictionariesfhadminedit
      }).catch((e) => {
        this.listLoading = false
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.el-dialog__body {
  padding: 0;
  background: red;
}
.mark_up {
  margin-bottom: 20px;
  margin-left: 90px;
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
