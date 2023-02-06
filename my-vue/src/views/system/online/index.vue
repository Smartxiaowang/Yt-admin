<template>
  <div class="app-container">
    <template >
      在线人数: {{ userCount }}
    </template>
    <el-table
      ref="multipleTable"
      :data="userList"
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
        type="selection"
        width="55"
        align="center"/>
      <el-table-column type="index" label="序号" width="50" align="center" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="{row}">
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="goOutTUser(row.username)" >强制下线</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="page-btn-group">
      <div>
        <el-button type="danger" icon="el-icon-delete" plain >强制下线</el-button>
      </div>
      <pagination :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" />
    </div>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination' // 通过 el-pagination二次打包
import waves from '@/directive/waves' // waves directive
export default {
  components: { Pagination, global },
  directives: { waves },
  data() {
    return {
      tableData: [
        {
          numb: '1',
          username: '	15203255716'
        }, {
          numb: '1',
          username: '	15203255716'
        }
      ],
      listQuery: {
        page: 1,
        limit: 10
      },
      total: 0,
      KEYWORDS: '',
      userList: [],
      userCount: 0,
      onlineAdress: '',
      websocketonline: ''
    }
  },
  created() {
    this.onlineAdress = sessionStorage.getItem('onlineAdress')
    this.getList()
  },
  methods: {
    getList() {
      var _this = this
      var onlineAdress = this.onlineAdress
      this.listLoading = true

      if (window.WebSocket) {
        this.loading = true
        _this.websocketonline = new WebSocket(encodeURI('ws://' + onlineAdress)) // onlineAdress在index.js文件定义
        _this.websocketonline.onopen = function() {
          console.info('连接成功')
          _this.websocketonline.send('[QQ313596790]fhadmin')// 连接成功
        }
        _this.websocketonline.onerror = function() {
          // 连接失败
          console.info('连接失败')
        }
        _this.websocketonline.onclose = function() {
          // 连接断开
          console.info('连接断开')
        }
        // 消息接收
        _this.websocketonline.onmessage = function(message) {
          var messageData = JSON.parse(message.data)
          if (messageData.type == 'count') {
            // userCount = message.msg
          } else if (messageData.type == 'userlist') {
            for (let i = 0; i < messageData.list.length; ++i) {
              var data1 = { username: messageData.list[i] }
              _this.userList.push(data1)
            }
            _this.userCount = messageData.list.length
          } else if (messageData.type == 'addUser') {
            var addUserData = { username: messageData.user }
            _this.userList.push(addUserData)
            _this.userList = _this.userList.reverse()
            _this.userCount = _this.userList.length
          }
        }
      }
    },
    // 强制某用户下线
    goOutTUser: function(theuser) {
      var _this = this
      theuser = theuser.replace('mobile-', '')
      if (theuser == 'admin') {
        console.info('操作失败!', '不能强制下线admin用户!')
        // return
      }
      this.$confirm('确定要强制[' + theuser + ']下线吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        console.info('theuser:' + theuser)
        _this.websocketonline.send('[goOut]' + theuser)
      })
    }
  }
}
</script>

