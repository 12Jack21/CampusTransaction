import Vue from 'vue'
import App from './App'
import api from '@/utils/api.js' // 下载的插件

// const http = new Request()

Vue.config.productionTip = false
Vue.prototype.$api= api

App.mpType = 'app'

const app = new Vue({
    ...App
})
app.$mount()
