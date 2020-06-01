import Vue from 'vue'
import App from './App'
import store from './store/index.js'
import api from '@/utils/api.js'
import mpopup from '@/components/xuan-popup/xuan-popup.vue'
import loading from '@/components/basics/loading.vue'

/**
 * 本项目使用了 UNI-APP 仿转转二手市场APP页面模板 进行开发: https://github.com/itianc/uni-turnTurn-Shop-Templet
 * 在此感谢 @仔仔ZaiZ 提供的非常齐全的模板
 * */

Vue.config.productionTip = false
Vue.component('mpopup',mpopup)
Vue.component('loading',loading)

Vue.prototype.$store = store
Vue.prototype.$api = api

App.mpType = 'app'

const app = new Vue({
	store,
	...App
})
app.$mount()
