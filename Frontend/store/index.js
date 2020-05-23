import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state:{
		hasLogin: false,
		userId: 1,
		userAddress:'信息学部',
		token: ''
	},
	mutations:{
		login(state, {userId,userAddress,token}){
			state.userId = userId
			state.userAddress = userAddress
			state.token = token
			state.hasLogin = true
		},
		relogin(state,{userId,userAddress}){
			state.userId = userId,
			state.userAddress = userAddress
			state.hasLogin = true
		},
		logout(state){
			state.userId = state.token = ''
			state.hasLogin = false
		}
	}
})

export default store