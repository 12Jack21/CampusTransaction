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
							<view class="cu-item" :class="index==tabCur?'select':''" @tap="tabSelect" :data-id="index">
								<view :class="index==tabCur?'text-black':''">{{item}}</view>
								<view class="tab-dot bg-red"/>
							</view>
						</block>
					</scroll-view>
				</view>
			</view>
		</view>
		
		<you-scroll ref="scroll" @onPullDown="onPullDown">
			<!--广场内容区域-->
			<checkbox-group class="block" @change="checkboxChange" v-show="tabCur === 0">
				<!--商品列表-->
				<view class="bg-white zaiui-goods-list-view" >
					<reservationList
						:list_data="stage1.list"
						@listTap="listTap"
					/>
				</view>
				
			</checkbox-group>
			
			<checkbox-group class="block" @change="checkboxChange" v-show="tabCur === 1">
				<!--商品列表-->
				<view class="bg-white zaiui-goods-list-view" >
					<reservationList
						:list_data="stage2.list"
						@listTap="listTap"
					/>
				</view>
				
			</checkbox-group>
			
			<checkbox-group class="block" @change="checkboxChange" v-show="tabCur === 2">
				<!--商品列表-->
				<view class="bg-white zaiui-goods-list-view" >
					<reservationList
						:list_data="stage3.list"
						@listTap="listTap"
					/>
				</view>
				
			</checkbox-group>
			
			<checkbox-group class="block" @change="checkboxChange" v-show="tabCur === 3">
				<!--商品列表-->
				<view class="bg-white zaiui-goods-list-view" >
					<reservationList
						:list_data="totalList.list"
						@listTap="listTap"
					/>
				</view>
				
			</checkbox-group>
		
		</you-scroll>
		
		
		
		
		
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
	import _reservations_data from '@/static/zaiui/data/reservations.js';
	import reservationList from '@/components/list/reservations-list.vue';
	import youScroll from '@/components/you-scroll/you-scroll.vue'
	
	const noticeMap = tabCur => {
		switch(tabCur){
			case 0:
				return 'stage1'
			case 1:
				return 'stage2'
			case 2:
				return 'stage3'
			case 3:
				return 'totalList'
			default:
				console.log('case default');
		}
	}
	
	
	export default {
		components: {
			barTitle,
			reservationList,
			youScroll
		},
		data() {
			return {
				headTab: {scrollLeft: 0, list: []},
				tabCur:0,
				goods_img: '/static/images/home/goods/1.png',
				goods_img_a: '/static/images/home/goods/2.png',
				checkbox_list: [], checkbox_all: false, goods_checked: false,
				stage1: {list:[]},
				stage2: {list:[]},
				stage3: {list:[]},
				totalList: {list:[]},
				reservations: []
			}
		},
		onLoad(params) {

			this.headTab.list = ['待确认','预约中','已完成','全部预约'];
			this.stage1.list=_reservations_data.stage1List()
			this.stage2.list=_reservations_data.stage2List()
			this.stage3.list=_reservations_data.stage3List()
			this.totalList.list=_reservations_data.totalList()
			this.loadNotices()
			//this.getReservations(params.accountId)
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
			async loadNotices(){
				this.loadStatus = 'loading'
				let curNotices = this[noticeMap(this.tabCur)]
				let pagination = {
					pageIndex:curNotices.pageIndex,
					pageSize: curNotices.pageSize,
					startTime:curNotices.startTime
				}
				if(this.tabCur > 2){
					await this.$api.getMyNotices(this.userId,pagination)
						.then(({data})=>{
							curNotices.list.push(...data.list)
							// 取完了数据
							if(data.pageIndex === data.pageCount)
								curNotices.finish = true
						})
						.catch(()=>uni.showToast({
							title: '我的通告获取失败',
							icon: 'none'
						}))
				}
				else
					await this.$api.getNotices(this.tabCur, pagination)
						.then(({data})=> {
							// 一些没有判断 success,根据后台的 json 来决定要不要加这个判断
							curNotices.list.push(...data.list)
							// 取完了数据
							if(data.pageIndex === data.pageCount)
								curNotices.finish = true
						})
						.catch(() => uni.showToast({
							title: '通告获取失败',
							icon: 'none'
						}))
				
				console.log('运行到此，设置 loadingText');
				if(curNotices.finish)
					this.loadStatus = 'noMore'
				else
					this.loadStatus = 'more'
			},
			async onPullDown(done){
				this[noticeMap(this.tabCur)] = iniNotice()
				await this.loadNotices()
				done()		
			},
			setReachBottom() {
				console.log('notice 触底了')
				if(!this[noticeMap(this.tabCur)].finish)
					this.loadNotices()
			},
			tabSelect(e) {
				let current = parseInt(e.currentTarget.dataset.id)		
				if(this.tabCur !== current && this[noticeMap(current)].list.length === 0){
					this[noticeMap(current)] = iniNotice()
					this.tabCur = current
					this.loadNotices()
				}else
					this.tabCur = current
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