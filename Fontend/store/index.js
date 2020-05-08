import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state:{
		hasLogin: false,
		userId: '',
		token: ''
	},
	mutations:{
		login(state, {userId,token}){
			state.userId = userId
			state.token = token
			state.hasLogin = true
		},
		logout(state){
			state.userId = state.token = ''
			state.hasLogin = false
		}
	}
})

export default store