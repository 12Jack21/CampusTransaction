<template>
	<view class="zaiui-my-box" :class="show ? 'show' : ''">
		<view class="bg-gradual-red zaiui-head-box">
			<!--标题栏-->
			<!--小程序端不显示-->
			<!-- #ifndef MP -->
			<bar-title :isBack="false" :fixed="false">
				<block slot="right">
					<text class="cuIcon-camera" />
					<!-- <text class="cuIcon-settings" @tap="setupTap"/> -->
				</block>
			</bar-title>
			<!-- #endif -->

			<!--用户信息-->
			<view class="zaiui-user-info-box">
				<!--已登陆-->
				<view class="cu-list menu-avatar">
					<view class="cu-item">
						<view class="cu-avatar round lg" :style="{ backgroundImage: 'url(/static/images/avatar/1.jpg)' }" />
						<view class="content text-xl">
							<view class="text-white"><text class="margin-right">仔仔</text></view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="zaiui-view-content">
			<!--用户数据-->
			<view class="padding-xs bg-white zaiui-user-info-order-box">
				<view class="text-black text-lg text-bold padding-sm">我的交易</view>
				<view class="cu-list grid col-3 no-border">
					<view class="cu-item" @tap="order_list_tap">
						<view class="text-xxl text-black">0</view>
						<text class="text-sm">我发布的</text>
					</view>
					<view class="cu-item">
						<view class="text-xxl text-black">1</view>
						<text class="text-sm">我卖出的</text>
					</view>
					<view class="cu-item">
						<view class="text-xxl text-black">2</view>
						<text class="text-sm">我买到的</text>
					</view>
				</view>
			</view>

			<!--设置列表-->
			<view class="cu-list menu sm-border margin-top">
				<view class="cu-item arrow">
					<view class="content">头像</view>
					<view class="action"><view class="cu-avatar round sm" :style="[{ backgroundImage: 'url(' + avatar_img + ')' }]" /></view>
				</view>
				<view class="cu-item arrow" @tap="editNameTap">
					<view class="content">昵称</view>
					<view class="action"><text class="text-gray">仔仔</text></view>
				</view>
				<view class="cu-item arrow">
					<view class="content">性别</view>
					<view class="action">
						<picker @change="sexPickerChange" :value="sexIndex" :range="sexPicker">
							<view class="picker text-gray">{{ sexIndex > -1 ? sexPicker[sexIndex] : '男' }}</view>
						</picker>
					</view>
				</view>
				<view class="cu-item arrow" @tap="synopsisTap">
					<view class="content">个人简介</view>
					<view class="action"><text class="text-gray">交个朋友</text></view>
				</view>
			</view>

			<view class="cu-list menu sm-border margin-top">
				<view class="cu-item arrow" @tap="regionTap">
					<view class="content">地区</view>
					<view class="action"><text class="text-gray">信息学部</text></view>
				</view>
				<!-- 			<view class="cu-item arrow" @tap="addressTap">
				<view class="content">收货地址</view>
			</view> -->
				<view class="cu-item arrow" @tap="editContactCardsTap"><view class="content">联系方式</view></view>
			</view>

			<view class="cu-list menu sm-border margin-top">
				<view class="cu-item arrow"><view class="content">注销账户</view></view>
			</view>
		</view>

		<!--占位底部距离-->
		<view class="cu-tabbar-height"></view>
	</view>
</template>

<script>
import barTitle from '@/components/basics/bar-title'

import _my_data from '@/static/zaiui/data/my.js' //虚拟数据
import _tool from '@/static/zaiui/util/tools.js' //工具函数
import {mapState} from 'vuex'
export default {
	name: 'my',
	components: {
		barTitle
	},
	data() {
		return {
			toolsList: [],
			id: 0,
			sexIndex: 0, sexPicker: ['男', '女']
		}
	},
	computed:{
		...mapState(['userId'])
	},
	props: {
		show: {
			type: Boolean,
			default: true
		}
	},
	watch: {},
	created() {
		//加载虚拟数据
		this.toolsList = _my_data.toolsListData()
		
		// Request my account information
		this.getMyAccount()
	},
	mounted() {
		_tool.setBarColor(false)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	methods: {
		getMyAccount(){
			this.$api.getMyAccount(this.userId)
				.then(({data})=>{
					console.log('我的账户',data);
					this.account = data
				})
				.catch(()=>{
					console.log('获取我的账户信息失败');
				})
		},
		//我买到的
		order_list_tap() {
			uni.navigateTo({
				url: '/pages/reservations/reservations'
			})
		},
		loginUrlTap() {
			uni.navigateTo({
				url: '/pages/my/login'
			})
		},
		realNameTap() {
			uni.navigateTo({
				url: '/pages/real_name/index'
			})
		},
		gridTap(item) {
			if (item.name == '设置') {
				this.setupTap()
			}
		},
		sponsoredTap() {
			uni.navigateTo({
				url: '/pages/my/sponsored'
			})
		},
		editNameTap() {
			uni.navigateTo({
				url: '/pages/my/edit-name'
			})
		},
		sexPickerChange(e) {
			this.sexIndex = e.detail.value
		},
		datePickerChange(e) {
			this.dateValue = e.detail.value
		},
		synopsisTap() {
			uni.navigateTo({
				url: '/pages/my/edit-synopsis'
			})
		},
		addressTap() {
			uni.navigateTo({
				url: '/pages/my/address'
			})
		},
		editPhoneTap() {
			uni.navigateTo({
				url: '/pages/my/edit-phone'
			})
		},
		editContactCardsTap() {
			uni.navigateTo({
				url: '/pages/my/contact-cards'
			})
		},
		regionTap() {
			uni.navigateTo({
				url: '/pages/my/region'
			})
		}
	}
}
</script>

<style lang="scss" scoped>
.zaiui-my-box {
	width: 100%;
	display: none;
	.zaiui-head-box {
		padding-top: 0;
		padding-bottom: 72.72rpx;
		.zaiui-user-info-box {
			/* #ifdef MP */
			padding-top: calc(var(--status-bar-height) + 50rpx);
			/* #endif */
			.login-user-view {
				position: relative;
				text-align: center;
				.login-user-avatar-view {
					position: relative;
					margin-bottom: 18.18rpx;
				}
			}
			.cu-list.menu-avatar > .cu-item {
				background-color: inherit;
				.content {
					width: calc(100% - 94.54rpx - 59.99rpx - 20rpx);
					.text-white-bg {
						color: #e8e8e8;
						.text-border-x {
							margin-right: 25.45rpx;
							position: relative;
							&:after {
								position: absolute;
								background: #dddddd;
								top: 5.45rpx;
								width: 1.81rpx;
								right: -12.72rpx;
								height: 16.36rpx;
								content: ' ';
							}
						}
					}
				}
				&:after {
					width: 0;
					height: 0;
					border-bottom: 0;
				}
			}
			.cu-list.menu-avatar > .cu-item .content > view:first-child {
				font-size: 34.54rpx;
			}
		}
		.zaiui-user-info-num-box {
			.cu-list.grid.no-border {
				padding: 0;
			}
			.cu-list.grid.no-border > .cu-item {
				padding-top: 27.27rpx;
				padding-bottom: 9.09rpx;
			}
			.cu-list.grid {
				background-color: inherit;
			}
			.cu-list.grid > .cu-item text {
				color: #e8e8e8;
				font-size: 20rpx;
				line-height: 27.27rpx;
			}
		}
		.zaiui-user-info-tip-box {
			position: relative;
			margin: 18.18rpx 27.27rpx;
			border-radius: 9.09rpx;
			padding: 18.18rpx 27.27rpx;
			background: #ea8d8d;
			background-image: linear-gradient(45deg, #f7615f, #f553b3);
			.text-cut {
				padding-right: 45.45rpx;
			}
			.icon {
				position: absolute;
				right: 27.27rpx;
				top: 23.63rpx;
			}
		}
	}
	.zaiui-view-content {
		padding: 0 27.27rpx 54.54rpx;
		margin-top: -63.63rpx;
		.zaiui-user-info-order-box {
			border-radius: 18.18rpx;
			.cu-list.grid.no-border {
				padding: 0;
			}
			.cu-list.grid.no-border > .cu-item {
				padding-bottom: 9.09rpx;
			}
		}
		.cu-list.grid > .cu-item text {
			color: inherit;
		}
		.zaiui-user-info-money-box {
			border-radius: 18.18rpx;
			.money-col {
				padding: 0 9.09rpx 9.09rpx;
				.money-item {
					position: relative;
					padding: 9.09rpx;
					.money-item-view {
						border: 1.81rpx solid #f3f2f3;
						border-radius: 18.18rpx;
						position: relative;
						padding: 9.09rpx;
						.cu-avatar {
							position: absolute;
							left: 9.09rpx;
						}
						.money-content {
							position: relative;
							margin-left: 109.09rpx;
							margin-bottom: 27.27rpx;
							top: 12.72rpx;
						}
					}
				}
			}
		}
		.zaiui-user-info-tools-box {
			border-radius: 18.18rpx;
			.tools-view {
				position: relative;
				.tools-title {
					padding-right: 81.81rpx;
				}
				.tools-right {
					position: absolute;
					right: 9.09rpx;
					bottom: 23.63rpx;
				}
			}
		}
	}
}
.zaiui-my-box.show {
	display: block;
}
</style>
