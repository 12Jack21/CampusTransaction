<template>
	<view class="register">
		<view class="content">
			<!-- 头部logo -->
			<view class="header"><image src="@/static/images/login.png"></image></view>
			<!-- 主体 -->
			<view class="main margins">
				<wInput v-model="usernameData" type="text" maxlength="11" placeholder="用户名"></wInput>
				<wInput v-model="passData" type="password" maxlength="11" placeholder="登录密码" isShowPass></wInput>
				<wInput v-model="wechat" type="text" maxlength="11" placeholder="微信号" isShowPass></wInput>
				<xfl-select
					:list="addrList"
					:clearable="false"
					:showItemNum="5"
					:listShow="false"
					:isCanInput="false"
					placeholder="所在学部"
					:initValue="address"
					selectHideType="hideAll"
					@change="addressChange"
				></xfl-select>
				<wInput v-model="mail" type="text" maxlength="20" placeholder="邮箱" isShowPass></wInput>
				<wInput v-model="verCode" type="number" maxlength="4" placeholder="验证码" isShowCode ref="runCode" @setCode="getVerCode()"></wInput>
			</view>

			<wButton text="注 册" :rotate="isRotate" @click.native="startReg()"></wButton>

			<!-- 底部信息 -->
			<view class="footer">
				<text @tap="isShowAgree" class="cuIcon" :class="showAgree ? 'cuIcon-radiobox' : 'cuIcon-round'">同意</text>
				<!-- 协议地址 -->
				<navigator url="" open-type="navigate">《协议》</navigator>
			</view>
		</view>
	</view>
</template>

<script>
var _this
import wInput from '../../components/watch-login/watch-input.vue' //input
import wButton from '../../components/watch-login/watch-button.vue' //button
import xflSelect from '../../components/xfl-select/xfl-select.vue'

export default {
	data() {
		return {
			usernameData: '', // 用户
			wechat: '', //自动识别是 wechat
			passData: '', //密码
			address: '信息学部',
			addrList: ['信息学部', '文理学部', '工学部', '医学部'],
			mail: '',
			verCode: '', //验证码
			showAgree: true, //协议是否选择
			isRotate: false //是否加载旋转
		}
	},
	components: {
		wInput,
		wButton,
		xflSelect
	},
	mounted() {
		_this = this
	},
	methods: {
		addressChange(e) {
			console.log('select change:', e)
			this.address = e.newVal
		},
		isShowAgree() {
			//是否选择协议
			_this.showAgree = !_this.showAgree
		},
		getVerCode() {
			//获取验证码
			if (_this.usernameData.length != 11) {
				uni.showToast({
					icon: 'none',
					position: 'bottom',
					title: '用户名不正确'
				})
				return false
			}
			console.log('获取验证码')
			this.$refs.runCode.$emit('runCode') //触发倒计时（一般用于请求成功验证码后调用）
			uni.showToast({
				icon: 'none',
				position: 'bottom',
				title: '模拟倒计时触发'
			})

			setTimeout(function() {
				_this.$refs.runCode.$emit('runCode', 0) //假装模拟下需要 终止倒计时
				uni.showToast({
					icon: 'none',
					position: 'bottom',
					title: '模拟倒计时终止'
				})
			}, 3000)
		},
		validate() {
			if (this.showAgree == false) {
				uni.showToast({
					icon: 'none',
					position: 'bottom',
					title: '请先同意《协议》'
				})
				return false
			} 
			if (this.usernameData.length === 0) {
				uni.showToast({
					icon: 'none',
					position: 'bottom',
					title: '用户名不能为空'
				})
				return false
			}
			if (this.passData.length < 6) {
				uni.showToast({
					icon: 'none',
					position: 'bottom',
					title: '密码不能小于6个字符'
				})
				return false
			}
			if(this.wechat.length === 0){
				uni.showToast({
					icon: 'none',
					position: 'bottom',
					title: '微信号不能为空'
				})
				return false
			}
			if (this.verCode.length != 4) {
				uni.showToast({
					icon: 'none',
					position: 'bottom',
					title: '验证码不正确'
				})
				return false
			}
			let mailPattern = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/
			if(!mailPattern.test(this.mail)){
				uni.showToast({
					icon: 'none',
					position: 'bottom',
					title: '邮箱格式不正确'
				})
				return false
			}
			return true
		},
		startReg() {
			//注册
			if (this.isRotate) {
				//判断是否加载中，避免重复点击请求
				return false
			}
			if(!this.validate()) return false

			console.log('注册开始')
			_this.isRotate = true

			let user = {
				username: this.usernameData,
				password: this.passData,
				mail: this.mail,
				wechat: this.wechat,
				address: this.address
			}
			this.$api
				.accountRegister(user)
				.then(({ data }) => {
					_this.isRotate = false
					console.log('注册成功')
					setTimeout(() =>
						uni.navigateTo({
							url: '/pages/login/login',
							animationType: 'slide-in-right'
						})
					)
				})
				.catch(() => {
					_this.isRotate = false
					uni.showToast({
						title: '注册失败,请检查网络',
						icon: 'none'
					})
				})
				
			// setTimeout(function(){
			// 	_this.isRotate=false
			// },3000)
		}
	}
}
</script>

<style>
@import url('@/components/watch-login/css/icon.css');
@import url('@/static/styles/login.css');
.margins .main-list {
	margin: 12rpx 0;
}
</style>
