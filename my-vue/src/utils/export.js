import axios from 'axios'
import { Message, MessageBox } from 'element-ui'

// create an axios instance
axios.defaults.withCredentials = true

const service = axios.create({
  // baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // eslint-disable-next-line no-undef
  baseURL: config.httpurl,
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 300000, // request timeout
  responseType: 'blob',
  headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
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
    // console.info('response:' + response)
    // const res = response.data
    return response
  },
  error => {
    if (error.response.status == '404') {
      MessageBox.alert('登录失效,请重新登录', {
        confirmButtonText: '确定',
        callback: action => {
          sessionStorage.clear()
          location.reload()
        }
      })
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

export function requestExport(url, data) {
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

export function exportExcel(data, res) {
  if (!data) { // 如果没有data数据就不进行操作
    return
  }
  // 获取headers中的filename文件名
  const tempName = res.headers['content-disposition'].split(';')[1].split('filename=')[1]
  // const tempName = '数据导出'
  // iconv-lite解决中文乱码
  const iconv = require('iconv-lite')
  iconv.skipDecodeWarning = true// 忽略警告
  const fileName = iconv.decode(tempName, 'utf-8')
  const blob = new Blob([data], { type: 'application/vnd.ms-excel' })// 转换成二进制对象
  if ('download' in document.createElement('a')) { // 不是IE浏览器
    const url = window.URL.createObjectURL(blob)// 二进制对象转换成url地址
    const link = document.createElement('a')
    link.style.display = 'none'
    link.href = url
    link.setAttribute('download', fileName)
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link) // 下载完成移除元素
    window.URL.revokeObjectURL(url) // 释放掉blob对象
  } else {
    window.navigator.msSaveBlob(blob, fileName)
  }
}

export default service

