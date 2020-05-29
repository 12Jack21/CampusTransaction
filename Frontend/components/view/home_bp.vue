<template>
	<view class="zaiui-home-box" :class="show ? 'show' : ''">	
		<view class="bar-title"></view>
		<!--轮播背景-->
		<swiper-background :list_data="swiperInfo.list" :indexs="swiperInfo.index" :show="true" />
		
		<view class="zaiui-head-search-box"  :style="[{ backgroundColor: 'rgba(229, 77, 66,' + headInfo.opacity + ')' }]">
			
			<!--小程序端的标题-->
			<!-- #ifdef MP -->
			<view class="text-center text-white bar-title">首页</view>
			<!-- #endif -->
			<!--搜索框-->
			<view class="cu-bar search zaiui-search-box">
				<view class="search-form round" @tap="searchTap">
					<text class="cuIcon-search" />
					<text>高等数学</text>
				</view>
				<view class="filter">
					<view class="sort-icon" @tap="moreTypeTap"><text class="cuIcon-sort" /></view>
				</view>
			</view>
		</view>

		<!--中间内容区域-->
		<view class="zaiui-view-content show">
			<!--轮播图-->
			<view class="zaiui-swiper-box">
				<swiper class="screen-swiper square-dot c" :autoplay="true" circular indicator-dots :current="swiperInfo.index" @change="swiperChange">
					<swiper-item v-for="(item, index) in swiperInfo.list" :key="index">
						<view class="swiper-content swiper-padding" style="padding-bottom: 16rpx;">
							<image :src="item.swiper" mode="widthFix" />
						</view>
					</swiper-item>
				</swiper>
			</view>

			<!--Grid 滑动菜单-->
			<grid-menu-list :list_data="gridMenuData" @listTap="gridMenuTap" />

			<!--商品tab-->
			<view class="zaiui-goods-tab-box">
				<scroll-view scroll-x class="nav z margin-tb-sm">
					<view class="flex text-center">
						<block v-for="(item, index) in goodsTabData.list" :key="index">
							<view class="cu-item flex-sub nf" :class="index == goodsTabData.tabCur ? 'select' : ''" @tap="goodsTab" :data-id="index">
								<view class="cu-tag badge z bg-gradual-pink" v-if="item.tag">{{ item.tag }}</view>
								<view :class="index == goodsTabData.TabCur ? 'text-red' : ''">{{ item.title }}</view>
								<view class="tab-dot bg-red" />
							</view>
						</block>
					</view>
				</scroll-view>
			</view>

			<!--商品列表-->
			<view class="zaiui-tab-list">
				<view class="zaiui-goods-list-box">
					<view class="flex flex-wrap ">
						<goods-list :list_data="leftGoods" @listTap="goodsListTap" @accTap="accTap"
						 class=" " style="width: 49%;padding-right: 1%;" />
						<goods-list :list_data="rightGoods" @listTap="goodsListTap" @accTap="accTap"
						class=" " style="width: 49%;padding-left: 1%;"/>
					</view>
				</view>
			</view>
			<!-- end --> 
			
			<!-- Loading Text -->
			<uni-load-more :status="loadStatus"></uni-load-more>
			<!-- end -->
			<!--占位底部距离-->
			<view class="cu-tabbar-height margin-bottom" />
			
		</view>
	</view>
</template>
 
<script>
//加载组件
import swiperBackground from '@/components/basics/swiper-background'
import gridMenuList from '@/components/list/grid-menu-list'
import goodsList from '@/components/list/goods-list'
import footerTabbar from '@/components/footer/footer-tabbar'
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'

//======================================================================
import _home_data from '@/static/zaiui/data/home.js' //虚拟数据

import handles from '@/utils/handles.js'

import { mapState } from 'vuex'

export default {
	name: 'home',
	components: {
		swiperBackground,
		gridMenuList,
		goodsList,
		footerTabbar,
		uniLoadMore
	},
	data() {
		return {
			onRequest:false, //其他同步方法
			loadStatus: 'more', //'loading'、 'noMore'
			swiperInfo: {
				index: 0,
				show: true,
				welcome: true,
				list: []
			},
			headInfo: {
				opacity: 0
			},
			goodsShow: true,
			viewContent: {
				welcome: true
			},
			gridMenuData: [],
			goodsTabData: {
				tabCur: 0,
				list: []
			},
			// showing goods data
			goodsData: [],
			newComs:{
				pageIndex:1,
				endTime:'',
				finish:false,
				data:[]
			},
			nearComs:{
				pageIndex:1,
				endTime:'',
				finish:false,
				data:[]
			},
			expiredComs:{ //即将失效
				pageIndex:1,
				endTime:'',
				finish:false,
				data:[]
			},
			cheapComs:{
				pageIndex:1,
				endTime:'',
				finish:false,
				data:[]
			},
			// four tabs' goods list: new, near, outdated, cheap
			storeGoods:[ // 为了让 Vue初始化时把这些 property 加入响应式处理
				{pageIndex:1,pageSize:10,endTime:'',finish:false,data:[]},
				{pageIndex:1,pageSize:10,endTime:'',finish:false,data:[]},
				{pageIndex:1,pageSize:10,endTime:'',finish:false,data:[]},
				{pageIndex:1,pageSize:10,endTime:'',finish:false,data:[]},
			],
			modalShow:true
		}
	},
	props: {
		show: {
			type: Boolean,
			default: true
		},
		scrollY: {
			type: Number,
			default: 0
		},
		scrollBottom: {
			type: Boolean,
			default: false
		}
	},
	computed: {
		...mapState(['userAddress']),
		leftGoods() {
			return this.goodsData.filter((e, index) => index % 2 === 0)
		},
		rightGoods() {
			return this.goodsData.filter((e, index) => index % 2 === 1)
		}
	},
	watch: {
		scrollY() {
			this.setPageScroll(this.scrollY)
		},
		scrollBottom(newVal) {
			if (newVal) {
				this.setReachBottom()
			}
		}
	},
	created() {
		//加载虚拟数据
		this.swiperInfo.list = _home_data.swiper()
		this.gridMenuData = _home_data.nav()
		this.goodsTabData.list = _home_data.goodsTab()
		//商品列表数据
		// this.goodsData = _home_data.goodsList()
		
		
		this.storeGoods[0].endTime = new Date().format('yyyy-MM-dd hh:mm')
		this.getCommodityList()
		
		// #ifdef MP	
			// 小程序的胶囊信息
			let capsuleInfo = uni.getMenuButtonBoundingClientRect()
			console.log('胶囊信息',capsuleInfo);
		// #endif
	},
	mounted() {
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	methods: {
		//页面被滚动
		setPageScroll(scrollTop) {
			//console.log(scrollTop);
			if (scrollTop <= 100) {
				let num = scrollTop / 100
				this.headInfo.opacity = num
			} else if (scrollTop > 100) {
				this.headInfo.opacity = 1
			}
		},
		//触底了
		setReachBottom() {
			console.log('home 触底了,加载更多物品列表')
			// 全部加载时
			if(!this.storeGoods[this.goodsTabData.tabCur].finish)
				this.getCommodityList()
		},		
		//商品列表上的分类tab被点击 
		goodsTab(e) {
			let current = e.currentTarget.dataset.id
			this.goodsData = this.storeGoods[current].data
			if(this.goodsData.length === 0){ 
				this.storeGoods[current].endTime = new Date().format('yyyy-MM-dd hh:mm')
				this.goodsTabData.tabCur = current // TODO 陷阱
				this.getCommodityList()
			}// 点击相同的 tab 则相当于下拉刷新当前列表
			else if(this.goodsTabData.tabCur === current){
				this.storeGoods.splice(current,1,{
					pageIndex:1,
					endTime: new Date().format('yyyy-MM-dd hh:mm'),
					data:[],
					finish:false
				})
				this.goodsTabData.tabCur = current
				this.getCommodityList()
			}
			else
				this.goodsTabData.tabCur = current 
			
			
			// #ifdef H5
			uni.pageScrollTo({
				scrollTop: 410,
				duration: 200
			})
			// #endif
			// #ifdef APP-PLUS
			uni.pageScrollTo({
				scrollTop: 360,
				duration: 200
			})
			// #endif		
		},
		goodsListTap(id) {
			uni.navigateTo({
				url: `../../pages/detail/commodity?id=${id}`,			
			})
		},
		accTap(id){
			uni.navigateTo({
				url:'../../pages/account/account?id=' + id
			})
		},
		getCommodityList(){ // 获取物品列表主函数
			if(this.onRequest) return 
			this.onRequest = true
			this.loadStatus = 'loading'
			let self = this
			let tab = this.goodsTabData.tabCur
			let pagination = {
				pageIndex: this.storeGoods[tab].pageIndex,
				pageSize: this.storeGoods[tab].pageSize,
				endTime: this.storeGoods[tab].endTime,
				userAddress: this.goodsTabData.tabCur===1? this.userAddress:''
			}
			// request commodity list data with pagination
			this.$api.getCommodities(tab, pagination)
				.then(res=>{
					let resp = res.data.data
					console.log('get commodity list, home resp=', res.data.data);
					this.storeGoods[tab].pageIndex = resp.pageIndex
					this.storeGoods[tab].pageSize = resp.pageSize
					console.log('before push, after set page property:',{...this.storeGoods[tab]});
					this.storeGoods[tab].data.push(...resp.pageList)
					console.log('after push:',{...this.storeGoods[tab]});
					this.storeGoods.splice(tab,1,this.storeGoods[tab])
					console.log('after add to responsive env:',{...this.storeGoods[tab]});
					this.$nextTick(function(){
						console.log('after nextTick:',{...this.storeGoods[tab]});
						this.goodsData = [...this.storeGoods[tab].data]		
						console.log('goodsData after nextTick',[...this.goodsData]);
						this.$forceUpdate()
					})
					
					// 取完了数据
					if(resp.pageIndex - 1 >= resp.pageCount) {
						self.storeGoods[tab].finish = true
						this.loadStatus = 'noMore'
					}
					else
						this.loadStatus = 'more'
					this.onRequest = false
				})
				.catch(()=>{
					uni.showToast({
					title:'获取物品失败,请检查网络',
					icon:'none'
				})
					this.loadStatus = 'more'
					this.onRequest = false
				})
		},
		swiperChange(e) {
			this.swiperInfo.index = e.detail.current
		},
		gridMenuTap(e) {
			// jump to corresponding commodity type
			uni.navigateTo({
				url: `/pages/search/search?type=${e.data.id}&typeName=${e.data.name}`
			})
		},
		moreTypeTap() {
			// 搜索框旁的具体分类按钮
			uni.navigateTo({
				url: '/pages/home/sort'
			})
		},
		searchTap() {
			uni.navigateTo({
				url: '/pages/search/search'
			})
		}
	}
}
</script>

<style lang="scss" scoped>
.swiper-content{
	display: flex;
	height: 100%;
	justify-content: center;
	align-items: flex-end;
}
.filter {
	color: white;
	font-size: 55rpx;
	line-height: 60rpx;
	text-align: center;
}

.zaiui-head-search-box {
	position: sticky;
	width: 100%;
	top: 0;
	z-index: 9999;
	background-color: rgba(229, 77, 66, 0);
	padding-top: var(--status-bar-height);
	transition: top 0.25s;
	padding-bottom: 10rpx;
	padding-left: 30rpx;
	padding-right: 30rpx;

	.zaiui-search-box {
		position: relative;
	}
}

.zaiui-view-content {
	display: none;
	width: 100%;

	.zaiui-tab-list {
		position: relative;
		width: 100%;
	}
}

.zaiui-view-content.show {
	display: block;
}

.zaiui-swiper-box {
	width: 100%;

	.screen-swiper {
		height: 230rpx;
		min-height: 230rpx;

		.swiper-padding {
			padding: 0 25rpx;
		}
	}
}

.red-envelopes {
	width: 100%;
}

.zaiui-goods-tab-box {
	position: sticky;
	padding: 2rpx 0;
	transition: all 0.25s;
	z-index: 9;
	background: #fff;
 
	/* #ifndef MP */
	top: calc(var(--status-bar-height) + 101rpx);
	/* #endif */

	/* #ifdef MP */
	top: calc(var(--status-bar-height) + 161rpx);
	/* #endif */

	.cu-tag.z {
		top: 0px;
		right: -32.72rpx;
		font-size: 20rpx;
		padding: 19rpx 6rpx;
		transform: scale(0.8);
	}
}

.zaiui-tab-list-title {
	.img-aau {
		width: 101.81rpx;
		margin-top: 12.72rpx;
	}

	.text-right {
		.img-aau {
			margin-right: 14.54rpx;
		}
	}

	.text-left {
		.img-aau {
			margin-left: 14.54rpx;
		}
	}
}

.zaiui-add-btn-view-box {
	position: fixed;
	z-index: 999999;
	bottom: 181.81rpx;
	right: 27.27rpx;

	.cu-btn {
		margin: auto;
		width: 81.81rpx;
		height: 81.81rpx;
		font-weight: 800;
		border-radius: 50%;
		font-size: 36.36rpx;
		border: 9.09rpx solid #fff;
		box-shadow: 0 0 14.54rpx 7.27rpx #d0d0d0;
	}
}

.zaiui-home-box {
	display: none;
	// top: var(--status-bar-height)
}

.zaiui-home-box.show {
	display: block;
}
</style>
