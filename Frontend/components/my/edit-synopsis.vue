<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor="bg-white" :isBack="false">
			<block slot="content">个人简介</block>
			<block slot="left">
				<text class="cuIcon-back" @tap="$emit('close')"></text>
			</block>
			<block slot="right">
				<text class="text-orange"@tap="save">保存</text>
			</block> 
		</bar-title>
		
		<view class="cu-form-group solid-top">
			<textarea placeholder="优秀的用户会这么写:例如:我是XX熊，喜欢收集各种闲置物品。在架的宝贝都可以交易，快来联系我吧!" v-model="introduction" maxlength="15"/>
			<text class="text-gray font-num-view">{{introduction.length}} / 15</text>
		</view>
		
		
		<!--小程序端显示-->
		<!-- #ifdef MP -->
		<view class="bg-white zaiui-footer-fixed zaiui-foot-padding-bottom">
			<view class="flex flex-direction">
				<button class="cu-btn bg-red">保存个人简介</button>
			</view>
		</view>
		<!-- #endif -->
		
	</view>
</template>

<script>
	import barTitle from '@/components/basics/bar-title';
	import _tool from '@/static/zaiui/util/tools.js';	//工具函数
	export default {
		name:'edit-synopsis',
		components: {
			barTitle
		},
		data(){
			return {
				introduction:''
			}
		},
		onReady() {
			_tool.setBarColor(true);
			uni.pageScrollTo({
			    scrollTop: 0,
			    duration: 0
			});
		},
		methods: {
			save(){
				this.$emit('updateIntro',{introduction:this.introduction})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.cu-form-group {
		textarea {
			height: 8.6em;
		}
		.font-num-view {
		    position: absolute;
		    bottom: 9.09rpx;
		    right: 27.27rpx;	
		}
	}
</style>
