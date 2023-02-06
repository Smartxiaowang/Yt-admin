// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'

import Cookies from 'js-cookie'

import App from './App'
import store from './store'
import router from './router'
import './asyncRouter'
import './icons' // icon
import BaiduMap from 'vue-baidu-map'
import Element from 'element-ui'
import './styles/element-variables.scss'
Element.Dialog.props.closeOnClickModal.default = false // 禁用遮罩关闭

import VueParticles from 'vue-particles'
Vue.use(VueParticles)

import Viewer from 'v-viewer'
import 'viewerjs/dist/viewer.css'

Vue.use(Viewer)
Viewer.setDefaults({
  Options: {
    'inline': true,
    'button': true,
    'navbar': true,
    'title': true,
    'toolbar': true,
    'tooltip': true,
    'movable': true,
    'zoomable': true,
    'rotatable': true,
    'scalable': true,
    'transition': true,
    'fullscreen': true,
    'keyboard': true,
    'url': 'data-source'
  }
})

Vue.use(BaiduMap, {
  ak: 'OElqFYoKiAH8KFtph8ftLKF5NlNrbCUr'
})

import Print from 'vue-print-nb'// 打印
Vue.use(Print) // 注册

import '@/styles/index.scss' // global css
Vue.config.productionTip = false

Vue.use(Element, {
  size: Cookies.get('size') || 'small' // set element-ui default size
})
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
