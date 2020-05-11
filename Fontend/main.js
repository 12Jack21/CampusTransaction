import Vue from 'vue'
import App from './App'
import store from './store/index.js'
import api from '@/utils/api.js'

// const http = new Request()

Vue.config.productionTip = false
Vue.prototype.$store = store
Vue.prototype.$api = api

App.mpType = 'app'

const app = new Vue({
	store,
	...App
})
app.$mount()
