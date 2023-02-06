<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="KEYWORDS" placeholder="搜索" class="filter-item" style="width: 200px;"/>
      <el-date-picker
        v-model="dateArray"
        :picker-options="pickerOptions"
        type="daterange"
        value-format="yyyy-MM-dd"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        class="filter-item"/>
      <el-select v-model="STATUS" placeholder="全部状态" clearable style="width: 100px" class="filter-item">
        <el-option v-for="item in statusOptions" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="getQuery">
        搜索
      </el-button>
    </div>
    <el-table
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
      <el-table-column prop="JOBNAME" label="任务名称" />
      <el-table-column prop="CREATE_TIME" label="创建时间" />
      <el-table-column prop="TABLENAME" label="备份对象" />
      <el-table-column prop="TIMEEXPLAIN" label="规则说明" />
      <el-table-column prop="STATUS" label="状态" >
        <template slot-scope="{row}">
          <div v-show="row.STATUS=='1'">正在运行</div>
          <div v-show="row.STATUS=='2'">已经停止</div>
        </template>
      </el-table-column>
      <el-table-column prop="BZ" label="备注" />

      <el-table-column label="操作" align="center" width="300">
        <template slot-scope="{row}">
          <el-button v-show="edit && row.STATUS=='1'" type="primary" icon="el-icon-close" size="mini" @click="onoff(row)">停止</el-button>
          <el-button v-show="edit && row.STATUS=='2'" type="primary" icon="el-icon-check" size="mini" @click="onoff(row)">启动</el-button>
          <el-button v-show="edit" type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(row)">编辑</el-button>
          <el-button v-show="del" type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(row.TIMINGBACKUP_ID, row.JOBNAME)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="page-btn-group">
      <div>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="el-icon-delete" plain @click="batchDel">删除</el-button>
      </div>
      <pagination :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </div>
    <el-dialog :visible.sync="dialogFormEdit" :title="dialogType==='edit'?'修改':'新增'" width="580px">
      <el-form ref="form" :rules="rules" :model="pd" label-width="100px" style="width: 500px;">
        <el-form-item label="备份对象" prop="TABLENAME">
          <el-select v-model="pd.TABLENAME" clearable>
            <el-option value="all" >整库</el-option>
            <el-option v-for="item in tableList" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间规则" prop="FHTIME">
          <el-input v-model="pd.FHTIME" disabled placeholder="这里输入时间规则..." />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="pd.BZ" :autosize="{ minRows: 1}" type="textarea" placeholder="这里输入备注..." />
        </el-form-item>
        <el-divider content-position="center">规则设定</el-divider>
        <div class="inline-flex" style="padding:0 20px">
          <div class="inline-item">
            <el-select v-model="month" placeholder="每" style="width: 60px" @change="setTimegz()">
              <el-option v-for="item in monthOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
            <span>月</span>
          </div>
          <div class="inline-item">
            <el-select v-model="week" :disabled="weekFlag" placeholder="每" style="width: 60px" @change="setTimegz()">
              <el-option v-for="item in weekOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
            <span>周</span>
          </div>
          <div class="inline-item">
            <el-select v-model="day" :disabled="dayFlag" placeholder="每" style="width: 60px" @change="setTimegz()">
              <el-option v-for="item in dayOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
            <span>日</span>
          </div>
          <div class="inline-item">
            <el-select v-model="hour" placeholder="每" style="width: 60px" @change="setTimegz()">
              <el-option v-for="item in hourOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
            <span>时</span>
          </div>
        </div>
        <el-divider content-position="center">规则说明</el-divider>
        <el-alert
          :closable="false"
          :title="pd.TIMEEXPLAIN"

          type="success"/>
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
      add: false,
      del: false,
      edit: false,
      varList: [], // 列表数据
      tableList: [], // 备份表数据
      pd: [],
      dbtype: [], // 数据库类型
      multipleSelectionAll: [], // 所有选中的数据包含跨页数据
      multipleSelection: [], // 当前页选中的数据
      dialogFormEdit: false,
      dialogType: 'add',
      rules: {
        TABLENAME: [{ required: true, message: '备份对象不能为空', trigger: 'blur' }],
        FHTIME: [{ required: true, message: '时间规则不能为空', trigger: 'blur' }]
      },
      dateArray: '',
      form: '',
      statusOptions: [
        { id: '', name: '全部状态' },
        { id: '1', name: '正在运行' },
        { id: '2', name: '已经停止' }
      ],
      monthOptions: [// 月下拉框
        { id: '*', name: '每' },
        { id: '1', name: '一' },
        { id: '2', name: '二' },
        { id: '3', name: '三' },
        { id: '4', name: '四' },
        { id: '5', name: '五' },
        { id: '6', name: '六' },
        { id: '7', name: '七' },
        { id: '8', name: '八' },
        { id: '9', name: '九' },
        { id: '10', name: '十' },
        { id: '11', name: '十一' },
        { id: '12', name: '十二' }
      ],
      weekOptions: [// 周下拉框
        { id: '*', name: '每' },
        { id: 'MON', name: '一' },
        { id: 'TUES', name: '二' },
        { id: 'WED', name: '三' },
        { id: 'THUR', name: '四' },
        { id: 'FTI', name: '五' },
        { id: 'SAT', name: '六' },
        { id: 'SUN', name: '七' }
      ],
      dayOptions: [// 日下拉框
        { id: '*', name: '每' },
        { id: '1', name: '1' },
        { id: '2', name: '2' },
        { id: '3', name: '3' },
        { id: '4', name: '4' },
        { id: '5', name: '5' },
        { id: '6', name: '6' },
        { id: '7', name: '7' },
        { id: '8', name: '8' },
        { id: '9', name: '9' },
        { id: '10', name: '10' },
        { id: '11', name: '11' },
        { id: '12', name: '12' },
        { id: '13', name: '13' },
        { id: '14', name: '14' },
        { id: '15', name: '15' },
        { id: '16', name: '16' },
        { id: '17', name: '17' },
        { id: '18', name: '18' },
        { id: '19', name: '19' },
        { id: '20', name: '20' },
        { id: '21', name: '21' },
        { id: '22', name: '22' },
        { id: '23', name: '23' },
        { id: '24', name: '24' },
        { id: '25', name: '25' },
        { id: '26', name: '26' },
        { id: '27', name: '27' },
        { id: '28', name: '28' }
      ],
      hourOptions: [// 小时下拉框
        { id: '*', name: '每' },
        { id: '1', name: '1' },
        { id: '2', name: '2' },
        { id: '3', name: '3' },
        { id: '4', name: '4' },
        { id: '5', name: '5' },
        { id: '6', name: '6' },
        { id: '7', name: '7' },
        { id: '8', name: '8' },
        { id: '9', name: '9' },
        { id: '10', name: '10' },
        { id: '11', name: '11' },
        { id: '12', name: '12' },
        { id: '13', name: '13' },
        { id: '14', name: '14' },
        { id: '15', name: '15' },
        { id: '16', name: '16' },
        { id: '17', name: '17' },
        { id: '18', name: '18' },
        { id: '19', name: '19' },
        { id: '20', name: '20' },
        { id: '21', name: '21' },
        { id: '22', name: '22' },
        { id: '23', name: '23' }

      ],

      pickerOptions: {

        shortcuts: [{
          text: '最近一周',

          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      dayFlag: false, // 日下拉框禁用
      weekFlag: false, // 周下拉框禁用
      // tableData: [
      //   {
      //     numb: '1',
      //     qname: '7月定时备份任务',
      //     qtime: '2010-10-22',
      //     bz: '一套为开发者、设计师和产品经理准备的基于 Vue 2.0 的组件库，提供了配套设计资源，帮助你的网站快速成型。'
      //   }, {
      //     numb: '1',
      //     qname: '7月定时备份任务',
      //     qtime: '2010-10-22',
      //     bz: '一套为开发者、设计师和产品经理准备的基于 Vue 2.0 的组件库，提供了配套设计资源，帮助你的网站快速成型。'
      //   }
      // ],
      listQuery: {
        page: 1,
        limit: 10
      },
      total: 0,
      listLoading: false,			// 加载状态
      KEYWORDS: '',
      LASTSTART: '',
      LASTEND: '',
      STATUS: '',
      hour: '*',
      day: '*',
      week: '*',
      month: '*'

    }
  },
  created() {
    this.getList()
    this.hasButton()
  },
  methods: {
    getList() {
      console.info(this.dateArray)
      if (this.dateArray != null && this.dateArray.length > 0) {
        this.LASTSTART = this.dateArray[0]
        this.LASTEND = this.dateArray[1]
      }
      this.listLoading = true
      requestFN(
        '/timingbackup/list?showCount=' + this.listQuery.limit + '&currentPage=' + this.listQuery.page,
        {
          KEYWORDS: this.KEYWORDS,
          LASTSTART: this.LASTSTART,
          LASTEND: this.LASTEND,
          STATUS: this.STATUS

        }
      ).then((data) => {
        this.listLoading = false
        this.varList = data.varList
        this.total = data.page.totalResult
        this.pd = data.pd
      })
        .catch((e) => {
          this.listLoading = false
        })
    },
    getQuery() {
      this.$refs.multipleTable.clearSelection()
      this.getList()
    },
    onoff(job) {
      // eslint-disable-next-line no-unused-vars
      var str = ''
      var statu = 1
      if (job.STATUS == '1') {
        statu = 2
        str = '停止'
      } else {
        str = '启动'
      }
      console.info(str)
      this.$confirm('确定要' + str + '吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/timingbackup/changeStatus',
          {
            TIMINGBACKUP_ID: job.TIMINGBACKUP_ID,
            STATUS: statu
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
    handleDelete(id, name) {
      this.$confirm('确定要删除[' + name + ']吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        requestFN(
          '/timingbackup/delete',
          {
            TIMINGBACKUP_ID: id
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
    handleAdd() {
      this.dialogType = 'add'
      this.pd = { TIMEEXPLAIN: '这里是规则说明' }
      this.hour = '*'
      this.day = '*'
      this.week = '*'
      this.month = '*'
      this.dayFlag = false // 日下拉框禁用
      this.weekFlag = false // 周下拉框禁用
      this.code = ''
      this.dialogFormEdit = true
      this.getDataByAdd()
    },
    handleEdit(row) {
      this.dialogType = 'edit'
      this.hour = '*'
      this.day = '*'
      this.week = '*'
      this.month = '*'
      this.dayFlag = false // 日下拉框禁用
      this.weekFlag = false // 周下拉框禁用
      this.pd = Object.assign({}, row)
      this.dialogFormEdit = true
    },
    getRowKey(row) {
      return row.TIMINGBACKUP_ID
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
          '/timingbackup/deleteAll',
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
    confirm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.listLoading = true
          requestFN(
            '/timingbackup/' + this.dialogType,
            {
              TIMINGBACKUP_ID: this.pd.TIMINGBACKUP_ID,
              TABLENAME: this.pd.TABLENAME,
              FHTIME: this.pd.FHTIME,
              BZ: this.pd.BZ,
              TIMEEXPLAIN: this.pd.TIMEEXPLAIN
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
    setTimegz() {
      console.log(this.month)
      var strM = ''
      var strW = ''
      var strD = ''
      var strH = ''
      if (this.month == '*') {
        strM = '每个月'
      } else {
        strM = '每年 ' + this.month + ' 月份'
      }
      if (this.week != '?') {
        if (this.week == '*') {
          strW = '每周'
          strD = '每天'
          this.dayFlag = false
          // $('#dayId').removeAttr('disabled')
          // $('#dayId').css('background', '#FFFFFF')
        } else {
          this.dayFlag = true
          // $('#dayId').attr('disabled', 'disabled')
          // $('#dayId').css('background', '#F5F5F5')
          this.day = '?'
          strD = ''
          strW = '每个星期' + this.getWeek(this.week)
        }
      }
      if (this.day != '?') {
        if (this.day == '*') {
          strD = '每天'
          strW = '每周'
          this.weekFlag = false
          // $('#weekId').removeAttr('disabled')
          // $('#weekId').css('background', '#FFFFFF')
        } else {
          this.weekFlag = true
          // $('#weekId').attr('disabled', 'disabled')
          // $('#weekId').css('background', '#F5F5F5')
          this.week = '?'
          strW = ''
          strD = this.day + '号'
        }
      }
      if (this.hour == '*') {
        strH = '每小时'
      } else {
        strH = this.hour + '点时'
      }
      if (this.day == '*' && this.week == '*') {
        this.day = '*'
        this.week = '?'
      }
      this.pd.FHTIME = '0 0 ' + this.hour + ' ' + this.day + ' ' + this.month + ' ' + this.week
      this.pd.TIMEEXPLAIN = strM + '的 ' + strW + ' ' + strD + ' ' + strH + '执行一次'
    },
    getWeek(value) {
      // eslint-disable-next-line no-array-constructor
      var arrW = new Array('MON', 'TUES', 'WED', 'THUR', 'FTI', 'SAT', 'SUN')
      // eslint-disable-next-line no-array-constructor
      var arrH = new Array('一', '二', '三', '四', '五', '六', '日')
      for (var i = 0; i < arrW.length; i++) {
        if (value == arrW[i]) return arrH[i]
      }
    },
    getDataByAdd() {
      this.listLoading = true
      requestFN(
        '/timingbackup/goAdd'
      ).then((data) => {
        this.listLoading = false
        this.tableList = data.varList
        this.dbtype = data.dbtype	// 数据库类型
        console.info(this.pd)
      })
        .catch((e) => {
          this.listLoading = false
        })
    },
    getData() {
      this.listLoading = true
      requestFN(
        '/timingbackup/goAdd',
        {
          TIMINGBACKUP_ID: this.TIMINGBACKUP_ID
        }
      ).then((data) => {
        this.listLoading = false
        this.tableList = data.varList
        this.dbtype = data.dbtype	// 数据库类型
        this.pd = data.pd			// 参数map
      })
        .catch((e) => {
          this.listLoading = false
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

// // 获取星期汉字

</script>

