<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor="bg-white" :isBack="false">
			<block slot="left"><text class="cuIcon-back" @tap="$emit('close')"></text></block>
			<block slot="content">修改密码</block>
			<!-- #ifndef MP -->
			<block slot="right">
				<text class="text-red" @tap="save">保存</text>
			</block>
			<!-- #endif -->
		</bar-title>
		
		<!--提示栏-->
		<view class="bg-red light text-sm padding-sm">
			<text class="cuIcon-warnfill"/>
			<text class="margin-left-xs">
				请不要在公共场合进行密码的设置
			</text>
		</view>

		<view class="bg-white padding form-view">
			<view class="text-black title">原始密码</view>
			<input password placeholder="请填写您的原始密码" v-model="originalPassword"/>
		</view>
		<view class="bg-white padding form-view">
			<view class="text-black title">新密码</view>
			<input password placeholder="请填写您的新密码" v-model="newPassword"/>
		</view>
		<view class="bg-white padding form-view">
			<view class="text-black title">确认密码</view>
			<input password placeholder="请确认您的新密码" v-model="confirmPassword"/>
		</view>
		
		<view style="margin-top: 20rpx;" v-if="onUpdate">
			<loading>密码修改中</loading>
		</view>
		
		<!--小程序端显示-->
		<!-- #ifdef MP -->
		<view class="bg-white wecanui-footer-fixed foot-pb">
			<view class="flex flex-direction padding-sm">
				<button class="cu-btn bg-red" @tap="save">保存</button>
			</view>
		</view>
		<!-- #endif -->
		
	</view>
</template>

<script>
	import barTitle from '@/components/basics/bar-title';
	import _tool from '@/static/zaiui/util/tools.js';	//工具函数
	export default {
		name:'edit-password',
		components: {
			barTitle
		},
		data(){
			return {
				originalPassword:'',
				newPassword:'',
				confirmPassword:''
			}
		},
		props:{
			srcPassword:'',
			onUpdate:false
		},
		methods: {
			save(){
				if(this.checkPsd()){
					// 请求更新密码（单独API）
					this.$emit('updatePassword',{
						originalPassword:this.originalPassword,
						newPassword:this.newPassword
					})
				}
			},
			checkPsd(){
				if(this.newPassword!=this.confirmPassword){
					uni.showToast({
						title: '两次输入的密码不一致',
						icon: 'none'
					});
					return false
				}else if(this.newPassword.length < 6){
					uni.showToast({
						title: '新密码不能少于6位',
						icon: 'none'
					});
					return false
				}
				else if(this.originalPassword != this.srcPassword){
					console.log('src',this.srcPassword);
					console.log('original',this.originalPassword);
					uni.showToast({
						title: '原密码输入错误',
						icon: 'none'
					});
					return false
				}
				return true
			}
		}
	}
</script>

<style lang="scss">
	/* #ifdef APP-PLUS */
		@import "../../static/colorui/main.css";
		@import "../../static/colorui/icon.css";
		@import "../../static/zaiui/style/app.scss";
	/* #endif */
	.form-view {
		border-bottom: 2rpx solid #f5f5f5;
		.title {
			margin-bottom: 27.27rpx;
		}
		input {
			color: #333333;
		}
	}
</style>
