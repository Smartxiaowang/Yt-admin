import axios from 'axios'
import { Message, MessageBox, Loading } from 'element-ui'

// create an axios instance
axios.defaults.withCredentials = true

let loadingService = null

const service = axios.create({
  // baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // eslint-disable-next-line no-undef
  baseURL: config.httpurl,
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 1800000, // request timeout
  headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    if (
      (
        !config.url.includes('getLevels') &&
        !config.url.includes('hasMenu') &&
        !config.url.includes('hasButton') &&
        !config.url.includes('getInfo') &&
        !config.url.includes('coursestudyvideorecord/save')
      ) &&
      !loadingService
    ) {
      loadingService = Loading.service({
        lock: true,
        text: '加载中......',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    }
    return config
  },
  error => {
    // do something with request error
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    // console.info('response:' + response)
    loadingService && loadingService.close()
    loadingService = null
    const res = response.data
    // if the custom code is not 20000, it is judged as an error.
    if (res.result !== 'success' && res.result !== 'fail' && res.result !== 'faild') {
      if (res.msg) {
        Message({
          message: res.msg,
          type: 'error',
          duration: 5 * 1000
        })
        return Promise.reject(new Error(res.message || 'Error'))
      } else {
        if (res.response == '302') {
          MessageBox.alert('登录失效,请重新登录', {
            confirmButtonText: '确定',
            callback: action => {
              sessionStorage.clear()
              location.reload()
            }
          })
        } else {
          Message({
            message: res.msg ? res.msg : '系统异常！请联系管理员',
            type: 'error',
            duration: 5 * 1000
          })
          return Promise.reject(new Error(res.message || 'Error'))
        }
      }
    } else {
      return res
    }
  },
  error => {
    loadingService && loadingService.close()
    loadingService = null
    if (!error.response || error.response.status == '404') {
      MessageBox.alert('登录失效,请重新登录', {
        confirmButtonText: '确定',
        callback: action => {
          sessionStorage.clear()
          location.reload()
        }
      })
      // }
      // console.info('error:' + error.response.status)
      // if (error.response.status == '302') {
      //   MessageBox.alert('登录失效,请重新登录', {
      //     confirmButtonText: '确定',
      //     callback: action => {
      //       sessionStorage.clear()
      //       location.reload()
      //     }
      //   })
    } else {
      Message({
        message: '系统异常,请重试',
        type: 'error',
        duration: 5 * 1000
      })
    }

    return Promise.reject(error)
  }
)

export function requestFN(url, data) {
  // 基础请求方法
  const Qs = require('qs')
  const method = 'post'
  const obj = {
    method, // 请求的类型
    url // 请求地址
  }
  obj.data = Qs.stringify(data) // data 带参数
  // 返回axios的基础方法
  return service(obj).then(r => {
    return r // 方法请求的数据
  })
}
export default service

