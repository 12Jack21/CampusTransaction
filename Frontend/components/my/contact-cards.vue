<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor="bg-white" :isBack="false">
			<block slot="left">
				<text class="cuIcon-back" @tap="$emit('close')"></text>
			</block>
			<block slot="content">联系卡</block>
			<block slot="right">
				<text class="text-red">保存</text>
			</block>
		</bar-title>
		
		<!--提示栏-->
		<view class="bg-orange light text-sm padding-sm">
			<text class="cuIcon-warnfill"/>
			<text class="margin-left-xs">
				重要的联系方式请避免公开
			</text>
		</view>
		
		<view class="bg-white padding">
			<view class="text-lg text-black margin-bottom">我的联系卡</view>
			<view class="text-sm text-gray">
				您可以选择添加以下一种或多种联系方式，根据具体沟通需求选择对应号码发送
			</view>
		</view>
		
		<view class="bg-white padding form-view">
			<view class="text-black title">微信</view>
			<input placeholder="请填写您的微信号" v-model="contact.wechat"/>
		</view>
		<view class="bg-white padding form-view">
			<view class="text-black title">QQ号</view>
			<input placeholder="请填写您的QQ号" v-model="contact.qq"/>
		</view>
		<view class="bg-white padding form-view">
			<view class="text-black title">电子邮箱</view>
			<input placeholder="请填写您的电子邮箱" v-model="contact.email"/>
		</view>
		
		
		<!--小程序端显示-->
		<!-- #ifdef MP -->
		<view class="bg-white wecanui-footer-fixed foot-pb">
			<view class="flex flex-direction padding-sm">
				<button class="cu-btn bg-red">保存</button>
			</view>
		</view>
		<!-- #endif -->
		
	</view>
</template>

<script>
	import barTitle from '@/components/basics/bar-title';
	import _tool from '@/static/zaiui/util/tools.js';	//工具函数
	export default {
		name:'contact-cards',
		components: {
			barTitle
		},
		data() {
			return {
				
			}
		},
		props:{
			contact:{}
		},
		methods: {
			
		}
	}
</script>

<style lang="scss" scoped>
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
