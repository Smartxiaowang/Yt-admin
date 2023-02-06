<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />

    <div class="right-menu">
      <!--      <div class="right-menu-item hover-effect" @click="setShowModel('system')">-->
      <!--        <svg-icon icon-class="example" />-->
      <!--        <span>系统管理</span>-->
      <!--      </div>-->
      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="imgUrl" alt="" class="user-avatar">
          <!-- <img src="https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif?imageView2/1/w/80/h/80" class="user-avatar"> -->
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="changeMessage">修改信息</el-dropdown-item>
          <el-dropdown-item @click.native="changePhoto">修改头像</el-dropdown-item>
          <!--          <el-dropdown-item @click.native="changePassword">修改密码</el-dropdown-item>-->
          <el-dropdown-item @click.native="download">平台操作手册</el-dropdown-item>
          <el-dropdown-item divided @click.native="goOut">
            <span style="display:block;">注销</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <el-dialog :visible.sync="dialogMessageEdit" :append-to-body="true" title="修改信息" width="600px">
      <div style="padding:0 40px">
        <el-form ref="userForm" :model="userForm" :rules="userFormRules" status-icon label-width="100px">
          <el-form-item label="用户名" >
            <el-input v-model="userForm.USERNAME" disabled placeholder="默认用户手机号码..." />
          </el-form-item>
          <el-form-item label="新密码：" prop="newpwd">
            <el-input v-model="userForm.newpwd" type="password" auto-complete="off" />
          </el-form-item>
          <el-form-item label="确认密码：" prop="newpassword1">
            <el-input v-model="userForm.newpassword1" type="password" auto-complete="off" />
          </el-form-item>
          <el-form-item label="姓名" prop="NAME">
            <el-input v-model="userForm.NAME" placeholder="这里输入姓名..." />
          </el-form-item>
          <el-form-item label="邮箱" prop="EMAIL">
            <el-input v-model="userForm.EMAIL" placeholder="这里输入邮箱..." />
          </el-form-item>
          <el-form-item label="备注">
            <el-input :rows="3" v-model="userForm.BZ" type="textarea" placeholder="这里输入备注..." />
          </el-form-item>

        </el-form>
        <div style="text-align: center;margin-top:30px">
          <el-button type="primary" @click.native.prevent="editUser">确认修改</el-button>

        </div>

      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogForm" :append-to-body="true" title="上传头像" width="600px">
      <el-form ref="photoForm" :model="photoForm" label-width="110px" style="width: 500px;">

        <el-form-item label="附件">
          <el-upload
            ref="upload"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-change="handleEditChange"
            :before-upload="beforeFileUpload"
            :auto-upload="false"
            :limit="1"
            :class="{hide:hideUpload}"
            action="#"
            accept=".jpg,.jpeg,.png"
            list-type="picture-card">
            <i class="el-icon-plus" />
          </el-upload>
          <el-dialog :visible.sync="dialogVisible">
            <img :src="dialogImageUrl" width="100%" alt="">
          </el-dialog>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogForm = false">取 消</el-button>
        <el-button type="primary" @click="upload">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogFormEdit" :append-to-body="true" title="修改密码" width="600px">
      <div style="padding:0 40px">
        <el-form ref="resetForm" :model="resetForm" :rules="resetFormRules" status-icon label-width="100px">
          <el-form-item label="旧密码：" prop="password">
            <el-input v-model="resetForm.password" type="password" auto-complete="off" />
          </el-form-item>
          <el-form-item label="新密码：" prop="newpwd">
            <el-input v-model="resetForm.newpwd" type="password" auto-complete="off" />
          </el-form-item>
          <el-form-item label="确认密码：" prop="newpassword1">
            <el-input v-model="resetForm.newpassword1" type="password" auto-complete="off" />
          </el-form-item>

        </el-form>
        <div style="text-align: center;margin-top:30px">
          <el-button type="primary" @click.native.prevent="confirm">确认修改</el-button>

        </div>

      </div>
    </el-dialog>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { Message, MessageBox } from 'element-ui'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import { requestFN } from '@/utils/request'
import { upload } from '@/utils/upload'
export default {
  components: {
    Breadcrumb,
    Hamburger,
    Screenfull,
    SizeSelect
  },
  data() {
    const validatePass = (rule, value, callback) => {
      if (value) {
        if (value.toString().length < 1 || value.toString().length > 18) {
          callback(new Error('密码长度为6-18位'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    const validatePass2 = (rule, value, callback) => {
      if (this.userForm.newpwd != '' && value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.userForm.newpwd) {
        console.info(this.userForm.newpwd)
        console.info(value)
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    var hasEmail = (rule, value, callback) => {
      if (value) {
        requestFN(
          '/user/hasEmail',
          {
            EMAIL: value,
            USERNAME: this.userForm.USERNAME
          }
        ).then((data) => {
          if (data.result == 'success') {
            callback()
          } else {
            callback(new Error('邮箱重复'))
          }
        }).catch((e) => {

        })
      } else {
        callback()
      }
    }
    return {
      imgUrl: require('@/assets/images/user-logo.png'),
      config: config,
      fwebsocket: {},
      dialogFormEdit: false,
      dialogMessageEdit: false,
      dialogForm: false,
      resetForm: {
        newpwd: '',
        newpassword1: '',
        password: '',
        username: ''
      },
      photoForm: {},
      userForm: {
        USERNAME: '',
        NAME: '',
        EMAIL: '',
        newpwd: '',
        newpassword1: ''
      },
      resetFormRules: {
        password: [
          { required: true, message: '请输入旧密码', trigger: 'blur' }
        ],
        newpwd: [
          { required: true, validator: validatePass, trigger: 'blur' }
        ],
        newpassword1: [
          { required: true, validator: validatePass2, trigger: 'blur' }
        ]
      },
      userFormRules: {
        NAME: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],

        EMAIL: [
          { required: false, message: '请输入邮箱', trigger: 'blur' },
          {
            pattern: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/,
            message: '请输入正确的邮箱'
          },
          { validator: hasEmail, trigger: 'blur' }
        ],

        newpwd: [
          { required: false, validator: validatePass, trigger: 'blur' }
        ],
        newpassword1: [
          { required: false, validator: validatePass2, trigger: 'blur' }
        ]

      },
      roleList: [],
      USER_ID: '',
      ROLE_IDS: '',
      DEPARTMENT_ID: '',
      hideUpload: false,
      dialogImageUrl: '',
      dialogVisible: false,
      FFILE: {}
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'device'
    ])
  },
  created() {
    this.getInfo()
    this.setShowModel('system')
  },
  methods: {
    gotoBI() {
      window.location.href = this.config.weburl + 'static/bi/index.html'
    },
    setShowModel(model) {
      this.$store.dispatch('permission/setShowModel', model)
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    getInfo() {
      var _this = this
      requestFN(
        '/head/getInfo',
        {}
      ).then((data) => {
        // 加入在线列表
        sessionStorage.setItem('onlineAdress', data.onlineAdress)
        if (window.WebSocket) {
          this.fwebsocket = new WebSocket(encodeURI('ws://' + data.onlineAdress)) // oladress在main.jsp页面定义
          this.fwebsocket.onopen = () => {
            this.fwebsocket.send('[join]' + data.USERNAME) // 连接成功
          }
          this.fwebsocket.onerror = function() {
            // 连接失败
          }
          this.fwebsocket.onclose = function() {
            // 连接断开
          }
          // 消息接收
          this.fwebsocket.onmessage = function(message) {
            var messageMsg = JSON.parse(message.data)
            if (messageMsg.type == 'goOut') {
              _this.goOut('1')
            } else if (messageMsg.type == 'thegoout') {
              _this.goOut('2')
            }
          }
        }
        this.imgUrl = config.httpurl + data.userPhoto
      })
        .catch((e) => {
          MessageBox.alert('登录失效,请重新登录', {
            confirmButtonText: '确定',
            callback: action => {
              sessionStorage.clear()
              location.reload()
            }
          })
        })
    },
    download() {
      this.$confirm('确定要下载此文件吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = false
        // '/mfolder/download?RISKWARNING_ID=' + RISKWARNING_ID,
        window.location.href = config.httpurl + 'uploadFiles/file/秦安平台企业端使用说明书.doc'
      }).catch(() => {
        this.listLoading = false
      })
      this.listLoading = false
    },
    // 从修改个人资料
    getEditMyInfo: function() {
      requestFN(
        '/user/goEditMyInfo',
        {}
      ).then((data) => {
        this.roleList = data.roleList				// 角色列表
        this.USER_ID = data.pd.USER_ID				// 主职角色ID
        this.ROLE_IDS = data.pd.ROLE_IDS				// 副职角色ID
        this.DEPARTMENT_ID = data.pd.DEPARTMENT_ID
        data.pd.newpwd = ''
        data.pd.newpassword1 = ''
        this.userForm = Object.assign({}, data.pd) // copy obj
      }).catch((e) => {

      })
    },

    // 下线
    goOut: function(msg) {
      requestFN(
        '/main/logout',
        {}
      ).then((data) => {
        this.fwebsocket.close()
        sessionStorage.clear()
        location.reload()
      })
        .catch((e) => {
          this.fwebsocket.close()
          sessionStorage.clear()
          location.reload()
        })
    },
    changePassword() {
      this.dialogFormEdit = true
    },
    changeMessage() {
      this.getEditMyInfo()

      this.dialogMessageEdit = true
    },
    changePhoto() {
      this.dialogForm = true
    },
    editUser() {
      this.$refs.userForm.validate(valid => {
        if (valid) {
          requestFN(
            '/user/editUserOwn',
            { USER_ID: this.USER_ID,
              ROLE_ID: this.userForm.ROLE_ID,
              ROLE_IDS: this.ROLE_IDS,
              USERNAME: this.userForm.USERNAME,
              // NUMBER:this.userForm.NUMBER,
              PASSWORD: this.userForm.newpwd,
              NAME: this.userForm.NAME,
              PHONE: this.userForm.PHONE,
              EMAIL: this.userForm.EMAIL,
              DEPARTMENT_ID: this.DEPARTMENT_ID,
              isEditUserName: this.isEditUserName,
              POST_ID: this.userForm.POST_ID,
              BZ: this.userForm.BZ,
              OPERATIONTYPE: '1' }
          ).then((data) => {
            Message({
              message: '信息修改成功',
              type: 'success',
              duration: 5 * 1000
            })
            this.dialogMessageEdit = false
          }).catch((e) => {
          })
        } else {
          return false
        }
      })
    },
    confirm() {
      this.$refs.resetForm.validate(valid => {
        if (valid) {
          requestFN(
            '/user/editUserPwd',
            this.resetForm
          ).then((data) => {
            Message({
              message: '密码修改成功',
              type: 'success',
              duration: 5 * 1000
            })
            this.dialogFormEdit = false
          }).catch((e) => {
          })
        } else {
          return false
        }
      })
    },
    handlePictureCardPreview(file, fileList) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    handleEditChange(file, fileList) {
      this.hideUpload = fileList.length >= 1
    },
    handleRemove(file, fileList) {
      this.hideUpload = fileList.length >= 1
    },
    // 保存
    upload() {
      if (this.$refs.upload.uploadFiles.length < 1) {
        this.$message({
          message: '请上传图片',
          type: 'error'
        })
        return false
      }
      this.$refs.upload.submit()
      const loading = this.$loading({
        lock: true,
        text: '上传中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      const formData = new FormData()

      formData.append('FFILE', this.FFILE)
      upload(
        '/photo/saveNew',
        formData
      ).then((data) => {
        this.dialogForm = false
        this.imgUrl = config.httpurl + data.userPhoto
        loading.close()
      }).catch((e) => {
        loading.close()
      })
    },
    beforeFileUpload(file) {
      const types = ['image/jpeg', 'image/jpg', 'image/png']
      const isImage = types.includes(file.type)
      if (!isImage) {
        this.$message.error('上传图片只能是 JPG、JPEG、PNG 格式!')
      } else {
        this.FFILE = file
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 54px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 16px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
<style>
.hide .el-upload--picture-card {
  display: none;
}
</style>
