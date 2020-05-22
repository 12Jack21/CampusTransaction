<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor="bg-white" isBack @rightTap="rightTap">
			<block slot="content">我的商品</block>
		</bar-title>
		
		<!--足迹的tab-->

		<view class="bg-white zaiui-title-tab-box">

			<view class="flex flex-wrap">
				<view class="basis-l">
					<scroll-view scroll-x class="nav z" scroll-with-animation :scroll-left="headTab.scrollLeft">
						<block v-for="(item,index) in headTab.list" :key="index">
							<view class="cu-item" :class="index==headTab.TabCur?'select':''" @tap="tabSelect" :data-id="index">
								<view :class="index==headTab.TabCur?'text-black':''">{{item}}</view>
								<view class="tab-dot bg-red"/>
							</view>
						</block>
					</scroll-view>
				</view>
			</view>
		</view>
		
		<checkbox-group class="block" @change="checkboxChange">
			<!--商品列表-->
			<view class="bg-white zaiui-goods-list-view" >
				<reservationList
					:list_data="goods.list"
					@listTap="listTap"
				/>
			</view>
			
		</checkbox-group>
		
		<!--占位底部距离-->
		<view class="cu-tabbar-height" v-if="goods_checked"/>
		
		
		<!--小程序端显示-->
		<!-- #ifdef MP -->
			<!--编辑-->
			<view class="zaiui-add-btn-view-box" @tap="rightTap">
				<button class="cu-btn cuIcon-check bg-red" v-if="goods_checked"/>
				<button class="cu-btn cuIcon-write bg-red" v-else/>
			</view>
		<!-- #endif -->
	</view>
</template>

<script>
	import barTitle from '@/components/basics/bar-title';
	import _tool from '@/static/zaiui/util/tools.js';	//工具函数
	import _goods_data from '@/static/data/goods.js';
	import reservationList from '@/components/list/reservations-list.vue';
	export default {
		components: {
			barTitle,
			reservationList
		},
		data() {
			return {
				headTab: {TabCur: 0, scrollLeft: 0, list: []},
				goods_img: '/static/images/home/goods/1.png',
				goods_img_a: '/static/images/home/goods/2.png',
				checkbox_list: [], checkbox_all: false, goods_checked: false,
				goods: {list:[]},
				reservations: []
			}
		},
		onLoad(params) {

			this.headTab.list = ['全部预约','预约中','已完成'];
			this.goods.list=_goods_data.goodsList()
			this.getReservations(params.accountId)
		},
		onReady() {
			_tool.setBarColor(true);
			uni.pageScrollTo({
			    scrollTop: 0,
			    duration: 0
			});
		},
		methods: {
			getReservations(accountId){
				this.$api.getReservations(accountId)
					.then(({data})=>{
						this.reservations = data
					})
					.catch(()=>{
						console.log('获取用户的预约列表失败');
					})
			},
			listTap(id){
				uni.navigateTo({
					url: '../../pages/detail/reservation?id=' + id
				});
			},
			//tab菜单被点击
			tabSelect(e) {
				let index = e.currentTarget.dataset.id;
				this.headTab.TabCur = index;
				this.headTab.scrollLeft = (index - 1) * 60;
				
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
			},
			listTap(id) {
				uni.navigateTo({
					url: '../../pages/detail/commodity?id='+id,
				})
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
	

</style>

<style lang="scss" scoped>
	
	.zaiui-title-tab-box {
		position: fixed;
		width: 100%;
		top: 40px;
		z-index: 999999;
		padding: calc(var(--status-bar-height) + 9.09rpx) 27.27rpx 9.09rpx 9.09rpx;
		.flex {
			.basis-l {
				flex-basis: 70%;
			}
			.basis-s {
				flex-basis: 30%;
			}
		}
	}
</style>