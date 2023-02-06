<template>
  <div class="ui-login">
    <div class="ui-logo">
      <img :src="imgLogo">
    </div>
    <video autoplay loop muted>
      <source :src="imgVideo" type="video/mp4">
    </video>
    <div class="ui-main">
      <div class="ui-all">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on" label-position="left">
          <div class="ui-row">
            <el-form-item prop="username">
              <span class="svg-container">
                <svg-icon icon-class="user" />
              </span>
              <el-input ref="username" v-model="loginForm.username" placeholder="用户名" type="text" tabindex="1" autocomplete="on" />
            </el-form-item>
          </div>
          <div class="ui-row">
            <el-form-item prop="password">
              <span class="svg-container">
                <svg-icon icon-class="password" />
              </span>
              <el-input ref="password" :key="passwordType" v-model="loginForm.password" :type="passwordType" placeholder="密码" tabindex="2" autocomplete="on" @keyup.native="checkCapslock" @blur="capsTooltip = false" />
            </el-form-item>
          </div>
          <div class="ui-row">
            <el-form-item>
              <span class="svg-container">
                <svg-icon icon-class="s-code" />
              </span>
              <el-input v-model="code" placeholder="验证码" type="text" tabindex="3" autocomplete="on" />
            </el-form-item>
          </div>
          <div class="login-code" @click="refreshCode">
            <!--验证码组件-->
            <s-identify :identify-code="identifyCode" />
          </div>

          <div>
            <el-button :loading="loading" :disabled="flag" type="success" size="medium" @click.native.prevent="handleLogin">登录</el-button>
          </div>

        </el-form>
      </div>

      <div class="tips">
        <span class="svg-container">
          <svg-icon icon-class="app" />
        </span>
        <span @click="goqrcode">秦安双控APP下载</span>

      </div>
    </div>
    <el-dialog :visible.sync="dialogFormQrcode" title="秦安双控APP下载" width="340px">
      <el-form ref="form" label-width="110px" style="width: 300px;">
        <vue-qr :text="qrcodeStr" :margin="0" :size="300" color-dark="#000" color-light="#fff" />
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormQrcode = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { requestFN } from '@/utils/request'
import SIdentify from './components/sidentify'
import vueQr from 'vue-qr'
import axios from 'axios'
export default {
  name: 'Login',
  components: { vueQr, SIdentify },
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
      identifyCodes: '1234567890',
      identifyCode: '',
      code: '', // text框输入的验证码
      imgUrl: require('@/assets/images/logo-tit-y.png'),
      imgLogo: require('@/assets/images/ui-logo.png'),
      imgVideo: 'https://qask.qhdsafety.com/file/uploadFiles/file/20210707/login-video.mp4',
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
    this.refreshCode()
    // window.addEventListener('storage', this.afterQRScan)
  },
  mounted() {
    if (this.loginForm.username === '') {
      this.$refs.username.focus()
    } else if (this.loginForm.password === '') {
      this.$refs.password.focus()
    }
    this.identifyCode = ''
    this.makeCode(this.identifyCodes, 4)
  },
  destroyed() {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {
    // 验证码
    randomNum(min, max) {
      return Math.floor(Math.random() * (max - min) + min)
    },

    refreshCode() {
      this.identifyCode = ''
      this.makeCode(this.identifyCodes, 4)
    },
    makeCode(o, l) {
      for (let i = 0; i < l; i++) {
        this.identifyCode += this.identifyCodes[
          this.randomNum(0, this.identifyCodes.length)
        ]
      }
      console.log(this.identifyCode)
    },

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
        if (valid) {
          if (this.code == '') {
            this.$message({
              message: '请输入验证码',
              type: 'error'
            })
            this.flag = false
            return false
          }
          if (this.identifyCode !== this.code) {
            this.code = ''
            this.refreshCode()
            this.$message({
              message: '请输入正确的验证码',
              type: 'error'
            })
            this.flag = false
            return false
          }
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
    // afterQRScan() {
    //   if (e.key === 'x-admin-oauth-code') {
    //     const code = getQueryObject(e.newValue)
    //     const codeMap = {
    //       wechat: 'code',
    //       tencent: 'code'
    //     }
    //     const type = codeMap[this.auth_type]
    //     const codeName = code[type]
    //     if (codeName) {
    //       this.$store.dispatch('LoginByThirdparty', codeName).then(() => {
    //         this.$router.push({ path: this.redirect || '/' })
    //       })
    //     } else {
    //       alert('第三方登录失败')
    //     }
    //   }
    // }
  }
}
</script>

<style lang="scss" scoped>
.ui-login {
  position: relative;
  .ui-main {
    position: absolute;
    top: 640px;
    left: 50%;
    margin-left: -430px;
    .ui-all {
      background-color: rgba($color: #03294f, $alpha: 0.8);
      border-radius: 5px;
      padding: 15px 30px;
      width: 860px;
    }
    .tips {
      cursor: pointer;
      color: #d3eaf4;
      padding-top: 20px;
      // font-size: 14px;
      text-align: center;
      text-shadow: 0px 0px 4px #000;
    }
    .login-form {
      display: flex;
      align-items: start;
    }
    .ui-row {
      width: 200px;
      .svg-container {
        padding: 0px 0px 2px 12px;
        color: #3cb4e2;
        vertical-align: middle;
        width: 30px;
        display: inline-block;
        // border: 1px solid red;
      }
    }
  }
  .ui-logo {
    position: fixed;
    top: 50px;
    left: 4%;
  }
  video {
    position: fixed;
    right: 0px;
    bottom: 0px;
    min-width: 100%;
    min-height: 100%;
    height: auto;
    width: auto;
    /*加滤镜*/
    /*filter: blur(15px); //背景模糊设置 */
    /*-webkit-filter: grayscale(100%);*/
    /*filter:grayscale(20%); //背景灰度设置*/
    z-index: -11;
  }
  source {
    min-width: 100%;
    min-height: 100%;
    height: auto;
    width: auto;
  }
}
</style>
<style>
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */
@supports (-webkit-mask: none) and (not (cater-color: #fff)) {
  .ui-login .el-input input {
    color: #fff;
  }
}
/* reset element-ui css */
.ui-login .el-input {
  display: inline-block;
  height: 32px;
  width: 75%;
}
.ui-login .el-input input {
  background: transparent;
  border: 0px;
  padding-left: 0;
  border-radius: 0px;
  font-size: 16px;
  color: #fff;
  height: 32px;
  line-height: 32px;
  /* caret-color: #fff; */
}
.ui-login .el-input input:-webkit-autofill {
  box-shadow: 0 0 0px 1000px #fff inset !important;
  -webkit-text-fill-color: #000 !important;
}
.ui-login .el-form-item {
  border: 1px solid #4abfec;
  /* background: rgba(0, 0, 0, 0.1); */
  border-radius: 5px;
  color: #454545;
  margin-bottom: 0;
  margin-right: 10px;
}
</style>
