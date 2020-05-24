import Vue from 'vue'
import App from './App'
import store from './store/index.js'
import api from '@/utils/api.js'
import mpopup from '@/components/xuan-popup/xuan-popup.vue'

// const http = new Request()

Vue.config.productionTip = false
Vue.component('mpopup',mpopup)

Vue.prototype.$store = store
Vue.prototype.$api = api

App.mpType = 'app'

const app = new Vue({
	store,
	...App
})
app.$mount()
