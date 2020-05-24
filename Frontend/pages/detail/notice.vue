<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor="bg-white" isBack @rightTap="rightTap">
			<block slot="content">通告详情</block>
		</bar-title>
		
		<view class="bg-white margin-top padding radius zaiui-trends">
			<view class="cu-list menu-avatar">
				<view class="cu-item">
						<view class="cu-avatar round" :style="[{ backgroundImage:account.avatar.length===0? 'url(/static/images/avatar/default.png)':account.avatar }]" 
						/>
					<view class="content" @tap="userTap('userTap', account.id)">
						<view class="text-black">
							<view class="text-cut">{{account.username}}</view>
						</view>
						<view class="text-sm flex">
							<text>{{notice.createTime}}</text>
						</view>
					</view>
					<view class="action">
							<text class="cuIcon-calendar" />
							<text class="margin-left-xs">{{notice.expiredTime}}</text>
							<text style="display: block;text-align: right;">时失效</text>
					</view>
				</view>
			</view>
			<!-- 状态 -->
			<view class="notice-title">
				<text>{{notice.title}}</text>
				<view class="cu-tag light state-tag" :class="state.color">
					<text>{{state.text}}</text>
				</view>
			</view>
			<!-- end -->
			<view style="display: flex; justify-content: space-between;">
				<!--下方部分-->
				<view class="cu-tag light round margin-top bg-red" style="padding: 0;background-color: transparent;">
					<text class="cuIcon-locationfill" style="font-size: 1.6em;"></text>
					<text class="margin-left-xs">{{notice.address}}</text>
				</view>
				<text class="margin-top cu-tag" style="background-color: transparent;">
					浏览量:{{notice.browseCount}} 次
				</text>
			</view>			
			<!-- 描述内容 -->
			<view class="margin-tb text-black zaiui-text-content">
				<text>{{notice.description}}</text>
			</view>
				
		</view> 
			
		<checkbox-group class="block" >
			<!--商品列表-->
			<view class="bg-white zaiui-goods-list-view" >						
				<noticeGoodsList
					:list_data="commodities"
					@listTap="listTap"
				/>
			</view>	
		</checkbox-group>
		
		<!--占位底部距离-->
		<view class="height-space" v-if="account.id===userId" />
		
		<!--底部操作-->
		<view class="bg-white zaiui-footer-fixed zaiui-foot-padding-bottom" v-if="account.id===userId">
				<view class= "operation">				
					<button class="cu-btn bg-red lg"  @tap="cancelNotice">关闭通告</button>
					<button class="cu-btn bg-orange lg"  @tap="updateShow=true">更新</button>
				</view>
		</view>
		
		<modal-notice :show="updateShow" @closeModal="updateShow=false" :notice="notice"></modal-notice>
		
	</view>
</template>

<script>
	import barTitle from '@/components/basics/bar-title';
	import _tool from '@/static/zaiui/util/tools.js';	//工具函数
	import _home_data from '@/static/zaiui/data/home.js';
	import noticeGoodsList from '@/components/list/notice-goods-list';
	import modalNotice from '@/components/basics/modal-notice.vue'
	
	import {mapState} from 'vuex'
	
	export default {
		components: {
			barTitle,
			noticeGoodsList,
			modalNotice
		},
		data() {
			return {
				updateShow:false,
				addShow:false,
				notice: {
					title:'通告标题',
					description:'这是i一个大段大段的通告描述这是i一个大段大段的通告描述这是i一个大段大段的通告描述这是i一个大段大段的通告描述这是i一个大段大段的通告描述这是i一个大段大段的通告描述这是i一个大段大段的通告描述这是i一个大段大段的通告描述这是i一个大段大段的通告描述这是i一个大段大段的通告描述这是i一个大段大段的通告描述这是i一个大段大段的通告描述这是i一个大段大段的通告描述',
					createTime:'3小时前',
					expiredTime:' 2020-10-08 10:19',
					condition: 'tonggao条件',
					address:'信息学部大食堂',
					browseCount: 30,
					state_enum:'PUBLISHED' // CANCELLED
				},
				account:{
					id: 1,
					username:'ZaiZ',
					avatar:''
				},
				commodities:[]			
			}
		},
		computed:{
			...mapState(['userId']),	
			state(){ //通告状态
				let texts = ['发布中...','即将失效...','已失效','已关闭'],text=texts[0],color='bg-grey'
				if(this.notice.state_enum === 'CANCELLED')
					text = texts[3]
				else{
					let d = new Date()
					let now = d.getTime()
					let expiredTime = (new Date(this.notice.expiredTime)).getTime()
					if(now >= expiredTime)
						text = texts[2]
					else{
						color = 'bg-orange'
						d.setDate(d.getDate() + 2)
						if(d.getTime() >= expiredTime)
							text = texts[1]
					}
				}
				return { text, color}
			}
		},
		onLoad(params) {			
			// virtual data
			this.commodities = _home_data.goodsList()
			// request
			this.getNotice(params.id)
		},		
		onReady() {
			_tool.setBarColor(true);
			uni.pageScrollTo({
			    scrollTop: 0,
			    duration: 0
			});
		},
		methods: {
			failTip(msg){
				uni.showToast({
					title: msg,
					icon: 'none'
				});
			},
			cancelNotice(){
				// 直接使通告失效
				uni.showModal({
					title: '确认关闭通告',
					content: '关闭后该通告内部所有物品和需求将会同时关闭，无法再被其他用户检索到,且无法再重新开启原通告',
					showCancel: true,
					cancelText: '取消',
					confirmText: '确认关闭',
					success: res => {
						this.$api.cancelNotice(this.notice.id)
							.then(({data})=>{
								if(data.success){
									console.log('关闭成功');
									this.notice.state_enum = 'CANCELLED'
									this.failTip('关闭通告失败')
								}
							})
							.catch(()=>this.failTip('网络连接异常'))
					}
				});
			},
			getNotice(id){
				this.$api.getNotice(id)
					.then(({data})=>{
						this.notice = data
					})
					.catch(()=>{
						console.log('获取通告详情失败');
						this.failTip('获取通告详情失败')
					})
			},
			listTap(id) {
				uni.navigateTo({
					url: '../../pages/detail/commodity?id='+id,
				})
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
@import "../../static/zaiui/style/footmark.scss";

.state-tag{
	display: inline-block;
	margin-left: 30rpx;
	position: relative;
	top: -30rpx;
	height: fit-content;
	border-radius: 20rpx;
	padding: 10rpx 20rpx;
}
.height-space{
	height: 80rpx;
}
.operation{
	display: flex;
	justify-content: space-around;
	.cu-btn{
		width: 48%;
		border-radius: 26rpx;
	}
}
.rate {
	font-size: 25rpx;
}
.notice-title {
	color: #ec4707; 
	font-size: 1.6em;
	line-height: 40rpx;
	font-weight: bold;
	margin: 30rpx 0 10rpx 0;
	word-wrap: break-word;
}
.zaiui-footer-fixed {
	box-shadow: 0 -3.63rpx 10.9rpx 0 #eaeaea;
	.flex-direction {
		padding: 18.18rpx 27.27rpx;
	}
}
.zaiui-trends {
	border-radius: 18.18rpx;
	.cu-list {
		.cu-item {
			padding-right: 0;
			height: 99.99rpx;

			.cu-avatar {
				left: 0;
				width: 81.81rpx;
				height: 81.81rpx;
			}
			.content {
				left: 99.99rpx;
				line-height: 1.5em;
			}
			.action {
				font-size: 25rpx;
				width: 285.54rpx;
				text-align: right;
				color: #e54d42;				
			}
			&:after {
				width: 0;
				height: 0;
				border-bottom: 0;
			}
		}
	}

	.zaiui-text-content {
		line-height: 1.6;

		.cuIcon-right {
			position: relative;
			top: 1rpx;
		}
	}

	.zaiui-img-grid-col {
		position: relative;
		width: 100%;

		.one-img {
			position: relative;

			.img-grid {
				width: 100%;
				height: 363.63rpx;
				border-radius: 9.09rpx;
				background-size: cover;
				background-position: center;
				background-repeat: no-repeat;
			}
		}

		.col-2 {
			.img-grid-view {
				padding: 5.45rpx;

				.img-grid {
					position: relative;
					width: 309.09rpx;
					height: 218.18rpx;
					background-size: cover;
					background-position: center;
					border-radius: 9.09rpx;
				}
			}
		}

		.col-3 {
			.img-grid-view {
				padding: 5.45rpx;

				.img-grid {
					position: relative;
					width: 204.79rpx;
					height: 204.79rpx;
					background-size: cover;
					background-position: center;
					border-radius: 9.09rpx;
				}
			}
		}
	}

	.zaiui-footer-tool {
		margin: 40rpx 0 18.18rpx;

		.margin-right-lg {
			margin-right: 94.54rpx;
		}

		.icon {
			position: relative;
			font-size: 36.36rpx;
			top: 4rpx;
		}
	}
}
</style>
