<template>
	<view class="login">
		<view class="content">
			<!-- 头部logo -->
			<view class="header"><image src="@/static/images/login.png"></image></view>
			<!-- 主体表单 -->
			<view class="main">
				<wInput v-model="usernameData" type="text" maxlength="11" placeholder="用户名"></wInput>
				<wInput v-model="passData" type="password" maxlength="11" placeholder="密码"></wInput>
			</view>
			<wButton text="登 录" :rotate="isRotate" @click.native="startLogin()" class="wbutton"></wButton>

			<!-- 底部信息 -->
			<view class="footer">
				<navigator url="forget" open-type="navigate">找回密码</navigator>
				<text>|</text>
				<navigator url="register" open-type="navigate">注册账号</navigator>
			</view>
		</view>
	</view>
</template>

<script>
var _this
import wInput from '@/components/watch-login/watch-input.vue' //input
import wButton from '@/components/watch-login/watch-button.vue' //button
import { mapMutations } from 'vuex'

export default {
	data() {
		return {
			usernameData: '', //用户/电话
			passData: '', //密码
			isRotate: false //是否加载旋转
		}
	},
	components: {
		wInput,
		wButton
	},
	mounted() {
		// this.isLogin()
	},
	onLoad(param){
		if(param.type == '1'){
			uni.showToast({
				title:'登录token过期，请重新登录',
				icon:'none'
			})
		}else if(param.type == '2'){
			uni.showToast({
				title:'无法登录,请检查网络',
				icon:'none'
			})
		}
	},
	methods: {
		...mapMutations(['login']),
		isLogin() {
			//判断缓存中是否登录过，直接登录(由于跳到该页说明未登录，所以这个判断应置于别处)
			const token = uni.getStorageSync('token') //setUserData
			if (value) {
				//有登录信息
				console.log('已登录用户：', value)
				// this.$store.dispatch("setUserData",value); //存入状态
				uni.navigateTo({
					url: '../index/index',
					animationType: 'fade-in'
				})
			}
		},
		validate(){
			if (this.usernameData.length == '') {
				uni.showToast({
					icon: 'none',
					position: 'bottom',
					title: '用户名不能为空'
				})
				return false
			}
			if (this.passData.length < 5) {
				uni.showToast({
					icon: 'none',
					position: 'bottom',
					title: '密码不正确'
				})
				return false
			}
			return true
		},
		startLogin() {
			//登录
			if (this.isRotate) {
				//判断是否加载中，避免重复点击请求
				return false
			}
			if(!this.validate()) return

			console.log('登录开始')
			this.isRotate = true // 登录按钮动画			
			uni.showLoading({
				title: '登录中'
			});
			
			
			// Debug 不验证登录
			uni.hideLoading()
			uni.navigateTo({
				url:'/pages/index/index',
				animationType:'slide-in-bottom',
				animationDuration:3000
			})
			return true
			// end
			
			
			this.$api.accountLogin({
				username: this.usernameData,
				password: this.passData
			}).then(({data})=>{
				if(data.success){
					uni.hideLoading()
					uni.showToast({
						title:'登录成功',
						position:'bottom'
					})
					this.isRotate = false
					// 存入缓存
					uni.setStorage('token',data.token)
					// 存入 vuex		
					this.login({
						userId: data.userId,
						userAddress: data.userAddress,
						token: data.token
					})
					setTimeout(()=>uni.navigateTo({
						url:'/pages/index/index',
						animationType:'pop-in'
					}),1000)
				}else{
					uni.hideLoading()
					uni.showToast({
						title:'登录失败，用户名或密码错误',
						icon:'none',
						position:'bottom'
					})
					this.isRotate = false
				}
			}).catch(()=>{
					uni.hideLoading()
					uni.showToast({
						title:'登录失败，请检查网络',
						icon:'none',
						position:'bottom'
					})
					this.isRotate = false
			})
			// setTimeout(function() {
			// 	_this.isRotate = false
			// }, 3000)
			// uni.showLoading({
			// 	title: '登录中'
			// });
			// getLogin()
			// .then(res => {
			// 	//console.log(res)
			// 	//简单验证下登录（不安全）
			// 	if(_this.usernameData==res.data.username && _this.passData==res.data.password){
			// 		let userdata={
			// 			"username":res.data.username,
			// 			"nickname":res.data.nickname,
			// 			"accesstoken":res.data.accesstoken,
			// 		} //保存用户信息和accesstoken
			// 		_this.$store.dispatch("setUserData",userdata); //存入状态
			// 		try {
			// 			uni.setStorageSync('setUserData', userdata); //存入缓存
			// 		} catch (e) {
			// 			// error
			// 		}
			// 		uni.showToast({
			// 			icon: 'success',
			// 			position: 'bottom',
			// 			title: '登录成功'
			// 		});
			// 		uni.reLaunch({
			// 			url: '../../../pages/index',
			// 		});
			// 	}else{
			// 		_this.passData=""
			// 		uni.showToast({
			// 			icon: 'error',
			// 			position: 'bottom',
			// 			title: '账号或密码错误，账号admin密码admin'
			// 		});
			// 	}
			// 	uni.hideLoading();
			// }).catch(err => {
			// 	uni.hideLoading();
			// })
		}
	}
}
</script>

<style>
@import url('@/components/watch-login/css/icon.css');
@import url('@/static/styles/login.css');
</style>
