<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor="bg-white" isBack>
			<block slot="content">修改密码</block>
			<block slot="right">
				<text class="text-red">保存</text>
			</block>
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
			<input placeholder="请填写您的原始密码" value=""/>
		</view>
		<view class="bg-white padding form-view">
			<view class="text-black title">新密码</view>
			<input placeholder="请填写您的新密码" value=""/>
		</view>
		<view class="bg-white padding form-view">
			<view class="text-black title">确认密码</view>
			<input placeholder="请确认您的新密码" value=""/>
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
		components: {
			barTitle
		},
		data() {
			return {
				
			}
		},
		onLoad() {
			
		},
		onReady() {
			_tool.setBarColor(true);
			uni.pageScrollTo({
			    scrollTop: 0,
			    duration: 0
			});
		},
		methods: {
			
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
