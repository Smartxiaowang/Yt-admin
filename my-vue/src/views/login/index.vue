<template>
  <div class="login-container">
    <div class="login-logo">
      <img :src="imgUrl" alt="">
    </div>
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on" label-position="left">

      <div class="title-container">
        <h3 class="title">企业用户登录</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input ref="username" v-model="loginForm.username" placeholder="用户名" type="text" tabindex="1" autocomplete="on" />
      </el-form-item>

      <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>
        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>

          <el-input ref="password" :key="passwordType" v-model="loginForm.password" :type="passwordType" placeholder="密码" tabindex="2" autocomplete="on" @keyup.native="checkCapslock" @blur="capsTooltip = false" />

        </el-form-item>
      </el-tooltip>
      <validation ref="validation" />
      <el-button :loading="loading" :disabled="flag" type="primary" size="medium" style="width:100%;margin:30px 0;" @click.native.prevent="handleLogin">登录</el-button>
      <!-- <div style="position:relative">
        <div class="tips">
          <span @click="goqrcode">秦安双控APP下载</span>

        </div>
      </div> -->
    </el-form>

    <el-dialog :visible.sync="showDialog" title="Or connect with">
      Can not be simulated on local, so please combine you own business simulation! ! !
      <br>
      <br>
      <br>
      <social-sign />
    </el-dialog>
    <el-dialog :visible.sync="dialogFormQrcode" title="秦安双控APP下载" width="340px">
      <el-form ref="form" label-width="110px" style="width: 300px;">
        <vue-qr :text="qrcodeStr" :margin="0" :size="300" color-dark="#000" color-light="#fff" />
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormQrcode = false">取 消</el-button>
      </div>
    </el-dialog>
    <div class="login-foot">
      河北秦安安全科技股份有限公司 版权所有 BETA1.0 Copy right 2013-2020
    </div>
  </div>
</template>

<script>
import { requestFN } from '@/utils/request'
import validation from './components/validation'
import vueQr from 'vue-qr'
import axios from 'axios'
export default {
  name: 'Login',
  components: { vueQr, validation },
  data() {
    const validateUsername = (rule, value, callback) => {
      if (value.length < 1) {
        callback(new Error('用户名不能为空'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 1) {
        callback(new Error('密码不能为空'))
      } else {
        callback()
      }
    }
    return {
      dialogFormQrcode: false,
      qrcodeStr: '',
      imgUrl: require('@/assets/images/logo-tit2.png'),
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      showDialog: false,
      redirect: undefined,
      otherQuery: {},
      rangeStatus: false,
      flag: false
    }
  },
  watch: {
    $route: {
      // eslint-disable-next-line
      handler: function (route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  created() {
    sessionStorage.clear()

    // window.addEventListener('storage', this.afterQRScan)
  },
  mounted() {
    if (this.loginForm.username === '') {
      this.$refs.username.focus()
    } else if (this.loginForm.password === '') {
      this.$refs.password.focus()
    }
  },
  destroyed() {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {
    checkCapslock(e) {
      const { key } = e
      this.capsTooltip = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.flag = true
      // 设置定时器，1秒后，按钮可用
      setTimeout(() => {
        this.flag = false
      }, 5000)
      this.$refs.loginForm.validate(valid => {
        /*        if (this.rangeStatus) {
          console.info(this.rangeStatus)
        } else {
          console.info(this.rangeStatus)
        }*/

        if (this.$refs.validation.rangeStatus) {
          if (valid) {
            this.loading = true
            requestFN(
              '/admin/check',
              {
                KEYDATA: 'qdkjchina' + this.loginForm.username + ',qd,' + this.loginForm.password
              }
            ).then((data) => {
              sessionStorage.setItem('user', JSON.stringify(data))
              this.$router.push({ path: '/index' })
              this.loading = false
            })
              .catch((e) => {
                this.flag = false
                this.loading = false
              })
          } else {
            this.flag = false
            return false
          }
        } else {
          this.flag = false
          this.$message({
            message: '请进行登录验证',
            type: 'error'
          })
        }
      })
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    },
    goqrcode() {
      axios(
        config.adminurl + '/versionmanager/getVersion'
      ).then(res => {
        this.qrcodeStr = config.fileUrl + res.data.pd.FILEURL
        this.dialogFormQrcode = true
      })
    }
  }
}
</script>

<style>
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */
@supports (-webkit-mask: none) and (not (cater-color: #fff)) {
  .login-container .el-input input {
    color: #fff;
  }
}
/* reset element-ui css */
.login-container .el-input {
  display: inline-block;
  height: 42px;
  width: 85%;
}
.login-container .el-input input {
  background: transparent;
  border: 0px;
  border-radius: 0px;
  font-size: 16px;
  color: #000;
  height: 42px;
  line-height: 42px;
  /* caret-color: #fff; */
}
.login-container .el-input input:-webkit-autofill {
  box-shadow: 0 0 0px 1000px #fff inset !important;
  -webkit-text-fill-color: #000 !important;
}
.login-container .el-form-item {
  border: 1px solid #aad5ff;
  /* background: rgba(0, 0, 0, 0.1); */
  border-radius: 5px;
  color: #454545;
}
</style>

<style >
.login-logo {
  position: absolute;
  top: 50px;
  left: 50px;
}
.login-logo img {
  width: 490px;
  height: 82px;
}
.login-container {
  height: 100vh;
  width: 100%;
  background-color: #2d3a4b;
  overflow: hidden;
  background: url(../../assets/images/login-bg.jpg) no-repeat;
  background-size: 100% 100%;
}
.login-foot {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
  text-align: center;
  position: absolute;
  bottom: 60px;
  left: 42%;
}
.login-container .login-form {
  position: absolute;
  width: 400px;
  max-width: 100%;
  padding: 40px 50px;
  margin: 0 auto;
  overflow: hidden;
  background-color: #fff;
  border-radius: 5px;
  right: 10%;
  top: 24%;
}
.login-name {
  position: absolute;
  right: 9.5%;
  top: 15%;
}
.login-name img {
  width: 422px;
  height: 53px;
}
.login-container .tips {
  font-size: 14px;
  color: #464646;
  margin-bottom: 60px;
  text-align: right;
  cursor: pointer;
}

.login-container .svg-container {
  padding: 2px 5px 6px 15px;
  color: #889aa4;
  vertical-align: middle;
  width: 30px;
  display: inline-block;
}
.login-container .title-container {
  position: relative;
}
.login-container .title-container .title {
  font-size: 20px;
  color: #000;
  font-weight: normal;
  font-family: "微软雅黑", "宋体", "Arial Narrow", Helvetica, sans-serif;
}
.login-container .show-pwd {
  position: absolute;
  right: 10px;
  top: 7px;
  font-size: 16px;
  color: #889aa4;
  cursor: pointer;
  user-select: none;
}
.login-container .thirdparty-button {
  position: absolute;
  right: 0;
  bottom: 6px;
}
@media only screen and (max-width: 470px) {
  .login-container .thirdparty-button {
    display: none;
  }
}
</style>
