import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state:{
		hasLogin: false,
		userId: 1,
		userAddress:'信息学部',
		userName:'高哥',
		token: ''
	},
	mutations:{
		login(state, {userId,userName,userAddress,token}){
			state.userId = userId
			state.userName = userName
			state.userAddress = userAddress
			state.token = token
			state.hasLogin = true
		},
		relogin(state,{userId,userName,userAddress}){
			state.userId = userId,
			state.userName = userName
			state.userAddress = userAddress
			state.hasLogin = true
		},
		logout(state){
			state.userId = -1
			state.userName = state.token = state.userAddress = ''
			state.hasLogin = false
		}
	},
	actions:{
		logout({commit}){
			commit('logout')
		}
	}
})

export default store