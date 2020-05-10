<template>
	<view class="zaiui-home-box" :class="show ? 'show' : ''">
		<!--欢迎-->
		<welcome-tip content="中午好, John" :show="true" :c_s="3000" @closeFinish="welcomeClose" />

		<!--轮播背景-->
		<swiper-background :list_data="swiperInfo.list" :indexs="swiperInfo.index" :show="swiperInfo.show" :welcome="swiperInfo.welcome" />

		<view class="zaiui-head-search-box" :class="headInfo.Class" :style="[{ backgroundColor: 'rgba(229, 77, 66,' + headInfo.opacity + ')' }]">
			<!--小程序端的标题-->
			<!-- #ifdef MP -->
			<view class="text-center text-white zaiui-small-routine-title">首页</view>
			<!-- #endif -->

			<!--搜索框-->
			<view class="cu-bar search zaiui-search-box">
				<view class="search-form round" @tap="searchTap">
					<text class="cuIcon-search" />
					<text>高等數學</text>
				</view>
				<view class="filter">
					<view class="sort-icon" @tap="moreTypeTap"><text class="cuIcon-sort" /></view>
				</view>
			</view>
		</view>

		<!--中间内容区域-->
		<view class="zaiui-view-content" :class="[viewContent.welcome ? 'welcome' : '', 'show']">
			<!--轮播图-->
			<view class="zaiui-swiper-box">
				<swiper class="screen-swiper square-dot c" autoplay circular indicator-dots :current="swiperInfo.index" @change="swiperChange">
					<swiper-item v-for="(item, index) in swiperInfo.list" :key="index">
						<view class="swiper-padding"><image :src="item.swiper" mode="widthFix" /></view>
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
						<goods-list :list_data="leftGoods" @listTap="goodsListTap" class="padding-right-xs" />
						<goods-list :list_data="rightGoods" @listTap="goodsListTap" class="padding-left-xs" />
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
import welcomeTip from '@/components/basics/welcome-tip.vue'
import swiperBackground from '@/components/basics/swiper-background'
import gridMenuList from '@/components/list/grid-menu-list'
import identifyList from '@/components/list/identify-list'
import goodsList from '@/components/list/goods-list'
import footerTabbar from '@/components/footer/footer-tabbar'
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'
//======================================================================
import _home_data from '@/static/zaiui/data/home.js' //虚拟数据

import { mapState } from 'vuex'

export default {
	name: 'home',
	components: {
		welcomeTip,
		swiperBackground,
		gridMenuList,
		identifyList,
		goodsList,
		footerTabbar,
		uniLoadMore
	},
	data() {
		return {
			loadStatus: 'more', //'loading'、 'noMore'
			swiperInfo: {
				index: 0,
				show: true,
				welcome: true,
				list: []
			},
			headInfo: {
				Class: '',
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

			// four tabs' goods list: new, near, outdated, cheap
			storeGoods:[]
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
		let GoodsData = _home_data.goodsList()
		//推荐感兴趣数据
		let recommendData = _home_data.recommend()
		//把推荐感兴趣的数据，添加到商品数据里，可扩展为随机位置显示。
		GoodsData.splice(1, 0, recommendData)
		this.goodsData = GoodsData
		this.headInfo.Class = 'welcome'
		
		for(let i = 0;i < 4;i++) this.storeGoods.push({pageNo:0,pageSize:10,data:[]})
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
			console.log('home 触底了')
		},
		getCommodityList(){
			// TODO: 区分上拉和下拉
			let self = this
			let tab = this.goodsTabData.tabCur
			let pagination = {
				pageNo: this.storeGoods[tab].pageNo,
				pageSize: this.storeGoods[tab].pageSize,
				lastId: this.storeGoods[tab].data[this.storeGoods[tab].data.length - 1] || '',
			}
			// request commodity list data with pagination
			this.$api.getCommodityList(tab, pagination)
				.then(res=>{
					self.storeGoods[tab] = res.data
					self.goodsData = res.data.data
				})
				.catch(()=>uni.showToast({
					title:'获取物品失败，请检查网络',
					icon:'none'
				}))
		},
		//欢迎提示关闭事件
		welcomeClose(bol) {
			this.swiperInfo.welcome = bol
			let Class = this.headInfo.Class
			this.headInfo.Class = Class.replace(/welcome/g, '')
			this.viewContent.welcome = bol
			console.log('Welcome close: ', bol)
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
		//商品列表上的分类tab被点击
		goodsTab(e) {
			this.goodsTabData.tabCur = e.currentTarget.dataset.id
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
		goodsListTap(e) {
			console.log('goodListTab', e)
			uni.navigateTo({
				url: `/pages/detail/com?id=${e.id}`
			})
			// TODO: 物品详情界面来加载数据
			// this.$api.getCommodity(e.id)
			// 	.then(res=>)
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
.filter {
	color: white;
	font-size: 55rpx;
	line-height: 60rpx;
	text-align: center;
}

.zaiui-head-search-box {
	position: fixed;
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

.zaiui-head-search-box.welcome {
	top: calc(var(--status-bar-height) + 101rpx);
	transition: top 0.25s;
}

.zaiui-view-content {
	display: none;
	width: 100%;

	.zaiui-tab-list {
		position: relative;
		width: 100%;
	}
}

.zaiui-view-content.welcome {
	/* #ifdef APP-PLUS */
	margin-top: calc(var(--status-bar-height) + 180rpx);
	/* #endif */

	/* #ifdef H5 */
	margin-top: calc(var(--status-bar-height) + 220rpx);
	/* #endif */

	/* #ifdef MP */
	margin-top: calc(var(--status-bar-height) + 220rpx);
	/* #endif */

	transition: all 0.25s;
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
	z-index: 9999;
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
}

.zaiui-home-box.show {
	display: block;
}
</style>
