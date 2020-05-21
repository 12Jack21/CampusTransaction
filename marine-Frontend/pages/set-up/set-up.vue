<template>
	<view>
		<!--标题栏-->
		
	
		<!--设置列表-->
		<view class="cu-list menu sm-border margin-top">
			<view class="cu-item arrow">
				<view class="content">头像</view>
				<view class="action">
					<view class="cu-avatar round sm" :style="[{backgroundImage:'url('+ avatar_img +')'}]"/>
				</view>
			</view>
			<view class="cu-item arrow" @tap="editNameTap">
				<view class="content">昵称</view>
				<view class="action">
					<text class="text-gray">仔仔</text>
				</view>
			</view>
			<view class="cu-item arrow">
				<view class="content">性别</view>
				<view class="action">
					<picker @change="sexPickerChange" :value="sexIndex" :range="sexPicker">
						<view class="picker text-gray">
							{{sexIndex>-1?sexPicker[sexIndex]:'男'}}
						</view>
					</picker>
				</view>
			</view>
			<view class="cu-item arrow">
				<view class="content">出生日期</view>
				<view class="action">
					<picker mode="date" :value="dateValue" start="1920-01-01" end="2020-05-01" @change="datePickerChange">
						<view class="picker text-gray">
							{{dateValue}}
						</view>
					</picker>
				</view>
			</view>
			<view class="cu-item arrow" @tap="synopsisTap">
				<view class="content">个人简介</view>
				<view class="action">
					<text class="text-gray">交个朋友</text>
				</view>
			</view>
		</view>
		
		<view class="cu-list menu sm-border margin-top">
			<view class="cu-item arrow" @tap="editPhoneTap">
				<view class="content">手机号</view>
				<view class="action">
					<text class="text-gray">138****8000</text>
				</view>
			</view>
			<view class="cu-item arrow" @tap="regionTap">
				<view class="content">地区</view>
				<view class="action">
					<text class="text-gray">太阳</text>
				</view>
			</view>
			<view class="cu-item arrow" @tap="addressTap">
				<view class="content">收货地址</view>
			</view>
			<view class="cu-item arrow" @tap="editContactCardsTap">
				<view class="content">联系卡</view>
			</view>
		</view>
		
		<view class="cu-list menu sm-border margin-top">
			<view class="cu-item arrow">
				<view class="content">注销账户</view>
			</view>
		</view>
		
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
				avatar_img: '/static/images/avatar/1.jpg', sexIndex: 0, sexPicker: ['男', '女'], dateValue: '1945-10-01',
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
			editNameTap() {
				uni.navigateTo({
					url: "/pages/my/edit-name"
				});
			},
			sexPickerChange(e) {
				this.sexIndex = e.detail.value;
			},
			datePickerChange(e) {
				this.dateValue = e.detail.value;
			},
			synopsisTap() {
				uni.navigateTo({
					url: "/pages/my/edit-synopsis"
				});
			},
			addressTap() {
				uni.navigateTo({
					url: "/pages/my/address"
				});
			},
			editPhoneTap() {
				uni.navigateTo({
					url: "/pages/my/edit-phone"
				});
			},
			editContactCardsTap() {
				uni.navigateTo({
					url: "/pages/my/contact-cards"
				});
			},
			regionTap() {
				uni.navigateTo({
					url: "/pages/my/region"
				});
			},
		}
	}
</script>

<style lang="scss">
	/* #ifdef APP-PLUS */
		@import "../../static/colorui/main.css";
		@import "../../static/colorui/icon.css";
		@import "../../static/zaiui/style/app.scss";
	/* #endif */
	.zaiui-head-box {
		position: relative;
	    padding: 45.45rpx 90.9rpx;	
	}
	.zaiui-btn-view {
	    position: fixed;
	    width: 100%;
		bottom: 0;
		.flex {
			padding: 18.18rpx;	
		}
	}
</style>
