import { constantRoutes } from '@/router'
const state = {
  routes: JSON.parse(sessionStorage.getItem('addRouter')) || [],
  addRoutes: [],
  showModel: sessionStorage.getItem('showModel') || 'prevention'
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
    sessionStorage.setItem('addRouter', JSON.stringify(routes))
  },
  SET_SHOW_MODEL: (state, showModel) => {
    state.showModel = showModel
    sessionStorage.setItem('showModel', showModel)
  }
}

const actions = {
  generateRoutes({ commit }, routes) {
    commit('SET_ROUTES', routes)
  },
  setShowModel({ commit }, showModel) {
    commit('SET_SHOW_MODEL', showModel)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
