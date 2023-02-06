import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
// create an axios instance
axios.defaults.withCredentials = true

const service = axios.create({
  // baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  baseURL: config.httpurl,
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 30000, // request timeout
  headers: { 'Content-Type': 'multipart/form-data' }
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
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
    const res = response.data
    // if the custom code is not 20000, it is judged as an error.
    if (res.result !== 'success' && res.result !== 'fail') {
      // if (res.response == '302') {
      //   MessageBox.alert('登录失效,请重新登录', {
      //     confirmButtonText: '确定',
      //     callback: action => {
      //       sessionStorage.clear()
      //       location.reload()
      //     }
      //   })
      // }
      if (res.isExcel) {
        MessageBox.alert(res.resultStr, {
          confirmButtonText: '确定',
          callback: action => {
          }
        })
      } else {
        Message({
          message: res.exception || 'Error',
          type: 'error',
          duration: 5 * 1000
        })
      }

      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    Message({
      message: '系统异常,请重试',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export function upload(url, data) {
  // 基础请求方法
  const method = 'post'
  const obj = {
    method, // 请求的类型
    url // 请求地址
  }
  obj.data = data // data 带参数
  // 返回axios的基础方法
  return service(obj).then(r => {
    return r // 方法请求的数据
  })
}

export default service

