<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="KEYWORDS" placeholder="搜索" class="filter-item" style="width: 200px;" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="getQuery">
        搜索
      </el-button>
      <el-button v-waves class="filter-item" type="success" icon="el-icon-refresh" @click="goKeyReset">
        重置
      </el-button>
    </div>
    <el-table v-loading="listLoading" ref="multipleTable" :data="varList" :row-key="getRowKey" :header-cell-style="{'font-weight': 'bold','color': '#000'}" tooltip-effect="dark" border fit highlight-current-row>
      <el-table-column :reserve-selection="true" type="selection" width="55" align="center" />
      <el-table-column type="index" label="序号" width="50" align="center" />
      <el-table-column prop="CREATOR" label="发布人" />
      <el-table-column prop="CREATTIME" label="发布时间" />
      <el-table-column prop="CREATOR" label="公告简介">
        <template slot-scope="{row}">
          <div class="link-type" @click="handleShow(row)">{{ (row.SYNOPSIS).substring(0,13) }}...</div>
        </template>
      </el-table-column>
      <el-table-column prop="CREATOR" label="启用状态">
        <template slot-scope="{row}">
          <el-switch v-model="row.STATUS" active-value="1" inactive-value="0" @change="changeStatus(row.NOTICE_ID)" />
        </template>
      </el-table-column>
      <el-table-column prop="NAME" label="查看状态">
        <template slot-scope="{row}">
          <div class="link-type" @click="handleRead(row.NOTICE_ID,row.ISALL)">{{ row.countCk }} / {{ row.countAll }}</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="{row}">
          <el-button v-show="edit && row.ISTOP==0" type="success" size="mini" @click="zhiding(row.NOTICE_ID,1,'置顶')">置顶</el-button>
          <el-button v-show="edit && row.ISTOP==1" type="success" size="mini" @click="zhiding(row.NOTICE_ID,0,'取消置顶')">取消置顶</el-button>
          <el-button v-show="del" type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(row.NOTICE_ID)">删除</el-button>
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

    <el-dialog :visible.sync="dialogFormEdit" :title="dialogType==='edit'?'修改':'新增'" width="800px">
      <el-form ref="form" :rules="rules" :model="form" label-width="110px" style="padding:0 40px 0 20px">
        <el-form-item label="公告简介" prop="SYNOPSIS">
          <el-input id="SYNOPSIS" ref="SYNOPSIS" v-model="form.SYNOPSIS" maxlength="255" placeholder="这里输入公告简介..." title="公告简介" />
        </el-form-item>
        <el-form-item label="是否全部通知" prop="ISALL">
          <el-radio-group id="ISALL" ref="ISALL" v-model="form.ISALL">
            <el-radio :label="'yes'">全部通知</el-radio>
            <el-radio :label="'no'">部分通知</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.ISALL=='no'" label="选择通知人" prop="ISALL">
          <div class="uo-flex">
            <el-input v-model="form.userNames" placeholder="请选择通知人..." disabled />
            <el-button type="primary" style="margin-left:10px" icon="el-icon-arrow-down" @click="handleCorp">请选择</el-button>
          </div>
        </el-form-item>
        <el-form-item label="内容" prop="CONTENT">
          <tinymce v-model="form.CONTENT" :height="300" />
        </el-form-item>
        <el-form-item label="附件路径">
          <div class="uo-flex">
            <el-input v-model="form.fileName" :disabled="true" title="请上传附件" />
            <el-upload :before-upload="beforeFileUpload" class="avatar-uploader" action="">
              <el-button type="primary" icon="el-icon-upload" style="margin-left:10px; ">上传附件</el-button>
            </el-upload>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormEdit = false">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogFormShow" title="查看" width="800px">
      <table class="table-ui">
        <tr>
          <td class="tbg">发布人</td>
          <td>{{ pd.CREATOR }}</td>
        </tr>
        <tr>
          <td class="tbg">发布时间</td>
          <td>{{ pd.CREATTIME }}</td>
        </tr>
        <tr>
          <td class="tbg">内容</td>
          <td>
            <div v-html="pd.CONTENT" />
          </td>
        </tr>
        <tr v-if="pd.FILEPATH">
          <td class="tbg">附件</td>
          <td><a @click="download(pd.FILEPATH)">下载</a></td>
        </tr>
      </table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormShow = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogFormRead" title="查看" width="800px">
      <el-table v-loading="readListLoading" ref="multipleTable_read" :data="readVarList" :header-cell-style="{'font-weight': 'bold','color': '#000'}" tooltip-effect="dark" border fit highlight-current-row>
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column prop="CORP_NAME" label="企业名称" />
        <el-table-column prop="CREATTIME" label="阅读状态">
          <template slot-scope="{row}">
            <span v-if="row.TYPE=='1'">已阅</span>
            <span v-else>未阅</span>
          </template>
        </el-table-column>
        <el-table-column prop="CREATOR" label="阅读时间" />
      </el-table>
      <div class="page-btn-group">
        <div>
          <el-button @click="dialogFormRead = false">关 闭</el-button>
        </div>
        <pagination :total="readTotal" :page.sync="readListQuery.page" :limit.sync="readListQuery.limit" @pagination="getReadList" />
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogFormUser" title="查看" width="800px">
      <div class="filter-container">
        <el-input v-model="KEYWORDS_USER" placeholder="搜索" class="filter-item" style="width: 200px;" />
        <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="getUserList">
          搜索
        </el-button>
      </div>
      <el-table v-loading="userListLoading" ref="multipleTable_corp" :row-key="getCorpRowKey" :data="corpVarList" :header-cell-style="{'font-weight': 'bold','color': '#000'}" tooltip-effect="dark" border fit highlight-current-row>
        <el-table-column :reserve-selection="true" type="selection" width="55" align="center" />
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column prop="CORP_NAME" label="企业名称" />
        <el-table-column prop="COMPANY_AREA" label="所属区域" />
        <el-table-column prop="ADDRESS" label="注册地址" />
        <el-table-column prop="LR_NAME" label="法定代表人" />
      </el-table>
      <div class="page-btn-group">
        <div>
          <el-button type="primary" @click="confirmUser">确 定</el-button>
          <el-button @click="dialogFormUser = false">关 闭</el-button>
        </div>
        <pagination :total="userTotal" :page.sync="userListQuery.page" :limit.sync="userListQuery.limit" @pagination="getUserList" />
      </div>
    </el-dialog>
  </div>
</template>
<script>
import Pagination from '@/components/Pagination' // 通过 el-pagination二次打包
import { requestFN } from '@/utils/request'
import waves from '@/directive/waves' // waves directive
import { upload } from '@/utils/upload'
import Tinymce from '@/components/Tinymce'
export default {
  components: { Pagination, Tinymce },
  directives: { waves },
  data() {
    return {
      listLoading: true,
      readListLoading: true,
      corpListLoading: true,
      add: false,
      del: false,
      edit: false,
      serverurl: 'https://qask.qhdsafety.com/file',
      listQuery: {
        page: 1,
        limit: 10
      },
      total: 0,
      KEYWORDS: '',
      varList: [],

      pd: [],

      NOTICE_ID: '',
      isAll: '',
      readListQuery: {
        page: 1,
        limit: 10
      },
      readTotal: 0,
      readVarList: [],

      KEYWORDS_USER: '',
      userListQuery: {
        page: 1,
        limit: 10
      },
      userTotal: 0,
      corpVarList: [],
      form: {
        SYNOPSIS: '',
        ISALL: 'yes',
        CONTENT: '',
        corpinfoIds: '',
        corpinfoNames: '',
        file: '',
        fileName: ''
      },
      multipleSelectionAll: [], // 所有选中的数据包含跨页数据
      multipleSelection: [], // 当前页选中的数据
      dialogFormEdit: false,
      dialogFormShow: false,
      dialogFormRead: false,
      dialogFormUser: false,
      dialogType: 'add',
      rules: {
        SYNOPSIS: [{ required: true, message: '公告简介不能为空', trigger: 'blur' }],
        CONTENT: [{ required: true, message: '内容不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getRowKey(row) {
      return row.NOTICECORP_ID
    },
    // 搜索
    getQuery() {
      this.$refs.multipleTable.clearSelection()
      this.getList()
    },
    goKeyReset() {
      this.KEYWORDS = ''
      this.getQuery()
    },
    // 获取列表
    getList() {
      this.listLoading = true
      requestFN(
        '/service/notice/list?showCount=' + this.listQuery.limit + '&currentPage=' + this.listQuery.page,
        {
          KEYWORDS: this.KEYWORDS,
          tm: new Date().getTime()
        }
      ).then((data) => {
        this.listLoading = false
        this.varList = data.varList
        var countNotList = data.countNotList
        for (var i = 0; i < data.varList.length; i++) {
          var notMap = data.varList[i]
          var notId = notMap.NOTICE_ID
          var countCk = 0
          var countWCK = 0
          for (var j = 0; j < countNotList.length; j++) {
            var countNotId = countNotList[j].NOTICE_ID
            if (notId == countNotId) {
              if (countNotList[j].type == '1') {
                countCk = countNotList[j].count
              }
              if (countNotList[j].type == '0') {
                countWCK = countNotList[j].count
              }
            }
          }
          if (notMap.ISALL == 'no') {
            notMap.countAll = countCk + countWCK
          } else if (notMap.ISALL == 'yes') {
            notMap.countAll = data.corpInfoCount
          }
          notMap.countCk = countCk
        }
        this.total = data.page.totalResult
        this.hasButton(data.varList)
      }).catch((e) => {
        this.listLoading = false
      })
    },
    handleShow(row) {
      this.dialogFormShow = true
      this.pd = Object.assign({}, row)
    },
    handleRead(id, isAll) {
      this.NOTICE_ID = id
      this.isAll = isAll
      this.dialogFormRead = true
      this.getReadList()
    },
    getReadList() {
      this.readListLoading = true
      requestFN(
        '/service/notice/listCorpRead?showCount=' + this.readListQuery.limit + '&currentPage=' + this.readListQuery.page,
        {
          NOTICE_ID: this.NOTICE_ID,
          isAll: this.isAll,
          tm: new Date().getTime()
        }
      ).then((data) => {
        this.readListLoading = false
        this.readVarList = data.varList
        this.readTotal = data.page.totalResult
      }).catch((e) => {
        this.readListLoading = false
      })
    },

    handleCorp() {
      this.dialogFormUser = true
      this.getCorpList()
    },
    getCorpRowKey(row) {
      return row.CORPINFO_ID
    },
    getCorpList() {
      this.corpListLoading = true
      requestFN(
        '/corpinfo/list?showCount=' + this.userListQuery.limit + '&currentPage=' + this.userListQuery.page,
        {
          KEYWORDS: this.KEYWORDS_corp,
          tm: new Date().getTime()
        }
      ).then((data) => {
        this.corpListLoading = false
        this.corpVarList = data.varList
        this.userTotal = data.page.totalResult
      }).catch((e) => {
        this.corpListLoading = false
      })
    },
    confirmUser() {
      const _selectData = this.$refs.multipleTable_corp.selection
      const ids = _selectData.map((item, index) => {
        return item.CORPINFO_ID
      }).join(',')
      const names = _selectData.map((item, index) => {
        return item.NAME
      }).join(',')
      this.form.corpinfoIds = ids
      this.form.corpinfoNames = names
      this.dialogFormUser = false
    },
    // 添加
    handleAdd() {
      this.dialogType = 'add'
      this.resetForm()
      this.dialogFormEdit = true
    },
    // 保存
    confirm() {
      this.$refs.form.validate(valid => {
        const loading = this.$loading({
          lock: true,
          text: '上传中...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        const formData = new FormData()
        Object.keys(this.form).map(key => {
          formData.append(key, this.form[key])
        })
        upload(
          '/service/notice/add',
          formData
        ).then((data) => {
          this.dialogFormEdit = false
          this.getList()
          loading.close()
        }).catch((e) => {
          loading.close()
        })
      })
    },
    beforeFileUpload(file) {
      this.form.file = file
      this.form.fileName = file.name
      this.$forceUpdate()
      return false
    },
    // 处理按钮点击
    changeStatus(NOTICE_ID) {
      requestFN(
        '/service/notice/changeStatus',
        {
          NOTICE_ID: NOTICE_ID
        }
      ).then(() => {
        this.$message({
          message: '状态修改成功',
          type: 'success'
        })
        this.listLoading = false
        this.varList = []
        this.listQuery.page = 1
        this.getList()
      }).catch((e) => {
        this.listLoading = false
      })
    },
    zhiding(id, ISTOP, str) {
      this.$confirm('确定要' + str + '吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/service/notice/changeIsTop',
          {
            NOTICE_ID: id,
            ISTOP: ISTOP
          }
        ).then(() => {
          this.$message({
            message: '操作成功',
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
    handleDelete(id) {
      this.$confirm('确定要删除吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/service/notice/delete',
          {
            NOTICE_ID: id
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
        return item.TIMINGBACKUP_ID
      }).join(',')
      this.$confirm('确定要删除选中的数据吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/noticecorp/deleteAll',
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
    hasButton() {
      var keys = 'serviceNotice:add,serviceNotice:del,serviceNotice:edit,toExcel'
      requestFN(
        '/head/hasButton',
        {
          keys: keys
        }
      ).then((data) => {
        this.add = data.serviceNoticefhadminadd		// 新增权限
        this.del = data.serviceNoticefhadmindel		// 删除权限
        this.edit = data.serviceNoticefhadminedit	// 修改权限
      }).catch((e) => {
        this.listLoading = false
      })
    },
    // 获取数据字典数据
    getDict() {
    },
    download(filePah) {
      window.location.href = config.fileUrl + filePah
    },
    resetForm() {
      this.form = {
        SYNOPSIS: '',
        ISALL: 'yes',
        CONTENT: '',
        corpinfoIds: '',
        corpinfoNames: '',
        file: '',
        fileName: ''
      }
    }
  }
}

</script>
<style lang="sass" scoped>
.uo-flex
  display: flex
</style>
