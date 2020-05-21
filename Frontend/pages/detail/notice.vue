<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor="bg-white" isBack @rightTap="rightTap">
			<block slot="content">通告清单</block>
			<block slot="right" v-if="goods_checked">完成</block>
		</bar-title>
		
 
		
		<view class="bg-white margin-top padding radius zaiui-trends">
			<view class="cu-list menu-avatar">
				<view class="cu-item">
						<view class="cu-avatar round" :style="[{ backgroundImage: 'url(' + '/static/images/avatar/5.jpg' + ')' }]" 
						/>
					<view class="content" @tap="userTap('userTap', item.userId)">
						<view class="text-black">
							<view class="text-cut"> 仔仔ZaiZ</view>
						</view>
						<view class="text-sm flex">
							<text>1年前</text>
						</view>
					</view>
					<view class="action">
							<text class="cuIcon-goods" />
							<text class="margin-left-xs">2020-5-20 10:00时失效</text>
					</view>
				</view>
			</view>
			
			<view class="notice-title">
				<text>Title-测试测试测试</text>
			</view>
			
			<!--内容-->
			<view class="margin-tb text-black zaiui-text-content">
				<text>哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈</text>
			</view>
			
			<view style="display: flex; justify-content: space-between;">
				<!--下方部分-->
				<view class="cu-tag light bg-red round margin-top">
					<text class="cuIcon-creativefill"></text>
					<text class="margin-left-xs">工学部</text>
				</view>
				<text class="margin-top cu-tag" style="background-color: transparent;">
					浏览量:20次
				</text>
			</view>
		</view> 
		


		
		
		<checkbox-group class="block" @change="checkboxChange">
			<!--商品列表-->
			<view class="bg-white zaiui-goods-list-view" >				

				
				<noticeGoodsList
					:list_data="goods.list"
					@listTap="listTap"
				/>
				


			</view>
						
		</checkbox-group>
		
		<!--占位底部距离-->
		<view class="cu-tabbar-height" v-if="goods_checked"/>
		
		<!--底部操作-->
		<view class="bg-white zaiui-footer-fixed zaiui-foot-padding-bottom">
				<view class= "flex flex-direction">				
					<button class="cu-btn bg-orange lg"  @tap="update(id)">更新</button>
				</view>
		</view>
	</view>
</template>

<script>
	import barTitle from '@/components/basics/bar-title';
	import _tool from '@/static/zaiui/util/tools.js';	//工具函数
	import _goods_data from '@/static/zaiui/data/goods.js';
	import noticeGoodsList from '@/components/list/notice-goods-list';
	export default {
		components: {
			barTitle,
			noticeGoodsList
		},
		data() {
			return {
				goods_img: '/static/images/home/goods/1.png',
				goods_img_a: '/static/images/home/goods/2.png',
				notice: {} ,//保持调用时 跟后端传来的属性一致，用于 v-bind
				goods: {list:[]},
			}
		},
		onLoad(params) {
			
			this.goods.list = _goods_data.goodsList()
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
			getNotice(id){
				this.$api.getNotice(id)
					.then(({data})=>{
						this.notice = data
					})
					.catch(()=>{
						console.log('获取通告详情失败');
						uni.showToast({
							title: '获取通告详情失败',
							icon: 'none'
						});
					})
			},
			//tab菜单被点击
			tabSelect(e) {
				let index = e.currentTarget.dataset.id;
				
				//滚动到顶部
				uni.pageScrollTo({
				    scrollTop: 0,
				    duration: 100
				});
			},
			//编辑
			rightTap() {
				if (this.goods_checked) {
					this.goods_checked = false;
				} else {
					this.goods_checked = true;
				}
			},
			listTap(id) {
				uni.navigateTo({
					url: '../../pages/detail/commodity?id='+id,
				})
			},
			//选择
			checkboxChange(e) {
				let items = this.checkbox_list, values = e.detail.value;
				for (let i = 0; i < items.length; i++) {
					//店铺处理
					let result = values.includes(items[i].id + '');
					if (result) {
						items[i].checked = true;
					} else {
						items[i].checked = false;
					}
				}
			},
			tapChecked() {
				if (this.checkbox_all) {
					this.checkbox_all = false;
				} else {
					this.checkbox_all = true;
				}
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
	@import "../../static/zaiui/style/footmark.scss";
	
	.rate {
		font-size: 25rpx;
	}
	
	.notice-title {
		color: #ec4707; 
		font-size: 36rpx;
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
