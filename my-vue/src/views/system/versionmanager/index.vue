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
      <el-table-column prop="VERSION" label="版本" />
      <el-table-column prop="FILEURL" label="文件地址" />
      <el-table-column prop="FILETYPE" label="文件类型" />
      <el-table-column prop="OPUSER" label="操作人" />
      <el-table-column prop="OPTIME" label="操作日期" />
      <el-table-column prop="ISEFFECTIVE" label="是否有效" >
        <template slot-scope="{row}">
          <div v-show="row.ISEFFECTIVE=='0'">有效</div>
          <div v-show="row.ISEFFECTIVE=='1'">无效</div>
        </template>
      </el-table-column>
      <el-table-column prop="ISUPDATE" label="是否更新" >
        <template slot-scope="{row}">
          <div v-show="row.ISUPDATE=='1'">强制更新</div>
          <div v-show="row.ISUPDATE=='2'">不强制更新</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="{row}">
          <el-button v-show="edit" type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(row.VERSIONMANAGER_ID)">编辑</el-button>
          <el-button v-show="del" type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(row.VERSIONMANAGER_ID)">删除</el-button>
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
        <el-form-item label="版本" prop="VERSION">
          <el-input id="VERSION" ref="VERSION" v-model="form.VERSION" maxlength="255" placeholder="这里输入版本..." title="版本"/>
        </el-form-item>
        <el-form-item label="文件地址" prop="FILEURL">
          <!--          <el-input id="FILEURL" ref="FILEURL" v-model="form.FILEURL" maxlength="255" placeholder="这里输入文件地址..." title="文件地址"/>-->
          <span v-if="form.FFILE">{{ form.FFILE.name }}</span>
          <el-upload :before-upload="beforeFileUpload" class="avatar-uploader" action="">
            <el-button type="primary">上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="文件类型" prop="FILETYPE">
          <el-input id="FILETYPE" ref="FILETYPE" v-model="form.FILETYPE" maxlength="255" placeholder="这里输入文件类型..." title="文件类型"/>
        </el-form-item>
        <el-form-item label="是否有效" prop="ISEFFECTIVE">
          <el-select v-model="form.ISEFFECTIVE" placeholder="请选择">
            <el-option
              v-for="item in optionsISEFFECTIVE"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="是否更新 " prop="ISUPDATE">
          <el-select v-model="form.ISUPDATE" placeholder="请选择">
            <el-option
              v-for="item in optionsIsUpdate"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
          </el-select>
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
import { upload } from '@/utils/upload'
import waves from '@/directive/waves' // waves directive
import { Message } from 'element-ui'
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
        VERSIONMANAGER_ID: '', // 备注1
        VERSION: '', // 版本
        FILEURL: '', // 文件地址
        FILETYPE: '', // 文件类型
        OPUSER: '', // 操作人
        OPTIME: '', // 操作日期
        ISEFFECTIVE: '', // 是否有效
        ISUPDATE: ''// 是否更新 1，强制更新，2不强制更新
      },
      multipleSelectionAll: [], // 所有选中的数据包含跨页数据
      multipleSelection: [], // 当前页选中的数据
      dialogFormEdit: false,
      dialogType: 'add',
      rules: {
        VERSION: [{ required: true, message: '版本不能为空', trigger: 'blur' }],
        FILETYPE: [{ required: true, message: '文件类型不能为空', trigger: 'blur' }],
        ISEFFECTIVE: [{ required: true, message: '是否有效不能为空', trigger: 'blur' }],
        ISUPDATE: [{ required: true, message: '是否更新不能为空', trigger: 'blur' }]
      },
      optionsISEFFECTIVE: [
        {
          value: '1',
          label: '有效'
        }, {
          value: '0',
          label: '无效'
        }
      ],
      optionsIsUpdate: [
        {
          value: '1',
          label: '强制更新'
        }, {
          value: '2',
          label: '不强制更新'
        }
      ]
    }
  },
  created() {
    this.getList(this.ROLE_ID)
  },
  methods: {
    getRowKey(row) {
      return row.VERSIONMANAGER_ID
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
        '/versionmanager/list?showCount=' + this.listQuery.limit + '&currentPage=' + this.listQuery.page,
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
        '/versionmanager/goEdit',
        {
          VERSIONMANAGER_ID: ID
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
      var _this = this
      this.$refs.form.validate(valid => {
        if (valid) {
          if (!this.form.FFILE) {
            Message({
              message: '请上传附件',
              type: 'error',
              duration: 5 * 1000
            })
            return
          }
          // const loading = this.$loading({
          //   lock: true,
          //   text: '上传中...',
          //   spinner: 'el-icon-loading',
          //   background: 'rgba(0, 0, 0, 0.7)'
          // })
          const formData = new FormData()
          Object.keys(_this.form).map(key => {
            console.info(key + '----------' + _this.form[key])
            formData.append(key, _this.form[key])
          })
          console.info(_this.form)
          console.info(formData)
          this.listLoading = true
          upload(
            '/versionmanager/' + this.dialogType,
            formData
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
          '/versionmanager/delete',
          {
            VERSIONMANAGER_ID: id
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
        return item.VERSIONMANAGER_ID
      }).join(',')

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
      var keys = 'versionmanager:add,versionmanager:del,versionmanager:edit,toExcel'
      requestFN(
        '/head/hasButton',
        {
          keys: keys
        }
      ).then((data) => {
        this.add = data.versionmanagerfhadminadd		// 新增权限
        this.del = data.versionmanagerfhadmindel		// 删除权限
        this.edit = data.versionmanagerfhadminedit	// 修改权限
      }).catch((e) => {
        this.listLoading = false
      })
    },
    // 获取数据字典数据
    getDict: function() {
    },
    resetForm() {
      this.form = {
        VERSIONMANAGER_ID: '', // 备注1
        VERSION: '', // 版本
        FILEURL: '', // 文件地址
        FILETYPE: '', // 文件类型
        OPUSER: '', // 操作人
        OPTIME: '', // 操作日期
        ISEFFECTIVE: '', // 是否有效
        ISUPDATE: ''// 是否更新 1，强制更新，2不强制更新
      }
    },
    beforeFileUpload(file) {
      this.form.FFILE = file
      this.$forceUpdate()
      return false
    }
  }
}

</script>
