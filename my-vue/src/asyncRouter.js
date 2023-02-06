//  进度条引入设置如上面第一种描述一样
import router from './router'
import request from '@/utils/request'
import store from './store'
import 'nprogress/nprogress.css' // progress bar style
import Layout from '@/layout'
var routers
router.beforeEach((to, from, next) => {
  if (to.path == `/login`) {
    sessionStorage.clear()
  }
  if (sessionStorage.getItem('user')) {
    if (!routers) {
      if (!sessionStorage.getItem('router')) {
        // 异步获取store中的路由
        request.post('main/index')
          .then((res) => {
            sessionStorage.setItem('router', JSON.stringify(res.menuList))
            routers = res.menuList
            try {
              const accessRoutes = formatRoutes(routers, '')
              // 动态添加格式化过的路由
              store.dispatch('permission/generateRoutes', accessRoutes)
              router.addRoutes(accessRoutes)
              next({ ...to, replace: true })
            } catch (error) {
              console.info(error)
              next(`/login`)
            }
          })
      } else {
        routers = JSON.parse(sessionStorage.getItem('router'))
        try {
          const accessRoutes = formatRoutes(routers, '')
          // 动态添加格式化过的路由
          store.dispatch('permission/generateRoutes', accessRoutes)
          router.addRoutes(accessRoutes)
          next({ ...to, replace: true })
        } catch (error) {
          console.info(error)
          next(`/login`)
        }
      }
    } else {
      next()
    }
  } else {
    if (to.path == `/login`) {
      next()
    } else {
      next(`/login`)
    }
  }
})

function formatRoutes(routes, baseUrl) {
  const res = []
  routes.forEach(item => {
    if (!item.hasMenu) {
      return res
    }
    const newItem = {}
    const meta = {}
    if (item.parent_ID == '0') {
      newItem.component = Layout
      newItem.path = '/' + item.menu_URL
    } else {
      newItem.component = resolve => require([`@/views/${item.component}`], resolve)
      newItem.path = item.menu_URL
    }
    newItem.hidden = item.menu_STATE == '0'
    const _baseUrl = baseUrl + '/' + item.menu_URL
    newItem.name = item.menu_NAME
    newItem.children = item.subMenu
    meta.title = item.menu_NAME
    meta.icon = item.menu_ICON
    meta.showModel = item.show_MODEL
    newItem.meta = meta
    // 若遍历的当前路由存在子路由，需要对子路由进行递归遍历
    if (newItem.children && newItem.children.length) {
      newItem.children = formatRoutes(item.subMenu, _baseUrl)
    }
    res.push(newItem)
  })
  // 返回处理好且可用的路由数组
  return res
}
