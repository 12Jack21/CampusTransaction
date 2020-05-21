<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor="bg-white">
			<block slot="content">商品详情</block>
			<block slot="right">
				<text class="cuIcon-forward" @tap="openReservations" />
				<text class="cuIcon-more" />
			</block>
		</bar-title>

		<!--轮播图-->
		<view class="zaiui-banner-swiper-box">
			<swiper class="screen-swiper" circular autoplay @change="bannerSwiper">
				<swiper-item v-for="(item, index) in commodity.images" :key="index"><image :src="item" mode="aspectFill" /></swiper-item>
			</swiper>
			<!--页码-->
			<text class="cu-tag bg-grey round sm zaiui-page">{{ bannerCur + 1 }} / {{ commodity.images.length }}</text>
		</view>

		<!-- 顶层价格 -->
		<view class="zaiui-limited-seckill-box">
			<text class="text-price text-xxl">{{ commodity.expectedPrice }}</text>
			<view class="text-xs zaiui-cost-price-num price-4">
				<view class="text-through">￥{{ commodity.originalPrice }}</view>
				<view>剩余{{ commodity.count }}件</view>
			</view>
			<view class="text-right zaiui-time-right font-lg">
				<view class="text-xs">{{ expiredTime }}</view>
				<view class="text-xs">时失效</view>
			</view>
		</view>

		<!--标题-->
		<view class="bg-white zaiui-view-box zaiui-title-view-box">
			<view class="title-view">
				<text class="text-black text-lg text-bold" style="font-size: 40rpx;">{{ commodity.name }}</text>
			</view>
			<br />
			<view>{{ commodity.description }}</view>
		</view>

		<!--选择-->
		<view class="margin-top bg-white zaiui-view-box zaiui-select-view-box">
			<view class="flex flex-wrap text-sm">
				<view class="basis-1"><text class="text-gray">条件</text></view>
				<view class="basis-9">
					<text class="text-sm">只限女生{{ condition }}</text>
				</view>
			</view>

			<view class="zaiui-border-view" />

			<view class="flex flex-wrap text-sm" @tap="selectTap">
				<view class="basis-1"><text class="text-gray">已选</text></view>
				<view class="basis-8">
					<text class="text-sm">{{ selectedCount }} 件</text>
				</view>
				<view class="basis-1">
					<view class="text-gray text-right"><text class="cuIcon-right icon" /></view>
				</view>
			</view>
		</view>

		<!--评论-->
		<view class="margin-top bg-white zaiui-comment-view-box">
			<view class="cu-bar bg-white">
				<view class="action"><text class="text-black text-lg text-bold">评论</text></view>
			</view>

			<!-- 评论列表 -->
			<view v-for="(item, index) in comments" :key="index">
				<view class="zaiui-border-view" />
				<view class="zaiui-view-box">
					<view class="flex flex-wrap">
						<view class="basis-2" @tap="fromAccTap(item.fromId)">
							<view class="cu-avatar round commentAvatar" :style="{ backgroundImage: item.fromImage.length === 0 ? 'url(/static/images/avatar/1.jpg)' : item.fromImage }" />
						</view>
						<view class="basis-8 text-lg">
							<view class="font-lg">{{ item.fromName }}</view>
							<view class="margin-top-xs">{{ item.content }}</view>
						</view>
					</view>
				</view>
			</view>
			<!-- 添加评论 -->

			<!-- end -->
		</view>

		<!-- 发布者信息-->
		<view class="margin-top bg-white zaiui-view-box zaiui-goods-info-view-box">
			<view class="zaiui-shop-view">
				<view class="cu-avatar lg round" :style="{ backgroundImage: account.avatar.length === 0 ? 'url(/static/images/avatar/1.jpg)' : account.avatar }" />
				<view class="text-view">
					<view class="margin-bottom-xs">{{ account.username }}</view>
					<view class="text-sm text-cut">正品保障，售后无忧</view>
				</view>
			</view>
			<view class="zaiui-border-view" />
		</view>

		<!--图片详情-->
		<view class="margin-top zaiui-goods-details-box">
			<image src="/static/images/comDefault.png" mode="widthFix" v-if="commodity.images.length === 0" />
			<image :src="item" v-for="(item, index) in commodity.images" :key="index"></image>
		</view>

		<!--占位底部距离-->
		<view class="cu-tabbar-height" />

		<!--底部操作-->
		<view class="zaiui-footer-fixed reserve_box">
			<view class="flex flex-direction"><button class="cu-btn bg-red reserve_btn" @tap="reserve">立即预约</button></view>
		</view>

		<!--弹出框-->
		<view class="cu-modal bottom-modal zaiui-bottom-modal-box" :class="bottomModal ? 'show' : ''">
			<view class="cu-dialog bg-white">
				<!--标题-->
				<view class="text-black text-center margin-tb text-lg zaiui-title-bar">
					<text>{{ modalTitle }}</text>
					<text class="cuIcon-close close-icon" @tap="hideModal"></text>
				</view>

				<!--内容区域-->
				<view class="zaiui-modal-content">
					<!--选择规格-->
					<view class="zaiui-view-box select" v-if="modalType == 'select'">
						<!--商品信息-->
						<view class="cu-list menu-avatar">
							<view class="cu-item">
								<view class="cu-avatar radius lg" style="background-image:url(/static/images/home/goods/1.png);" />
								<view class="content">
									<view class="text-price-view">
										<text class="text-price text-red margin-right-xs">{{ commodity.expectedPrice }}</text>
										<text class="text-sm text-gray text-through">￥{{ commodity.originalPrice }}</text>
									</view>
									<view class="text-black text-sm flex">
										<view class="text-cut">已选: {{ selectedCount }} 件</view>
									</view>
								</view>
							</view>
						</view>

						<!--规格数据-->
						<view class="zaiui-select-btn-list-boox">
							<view class="select-item" style="margin-left: 20rpx;">
								<view class="text-black text-lg text-bold">数量</view>
								<view class="inputCount"><input type="number" v-model="inputCount" placeholder="输入想要预约的数量" /></view>
							</view>
						</view>
					</view>

					<!--选择排队的预约 -->
					<view class="zaiui-view-box select" v-if="modalType == 'reservations'">
						<!--预约列表-->
						<view class="bg-white zaiui-goods-list-view">
							<view class="zaiui-checkbox-view"><view class="text-black">所有预约</view></view>
							<view class="zaiui-goods-list-box" v-for="(item,index) in reservations" 
								@tap="listTap(item.id)" :key="index">
								<view class="cu-avatar radius" 
								:style="[{ backgroundImage: item.account.avatar.length===0? 'url(/static/images/comDefault.png)' : item.acocunt.avatar}]" />
								<view class="goods-info-view">
									<view class="text-cut-2 text-black">{{item.account.username}}</view>
									<view class="goods-info-tools">
										<text class="text-price text-red text-lg">
											{{item.price}}
										</text>
									</view>
									
									<view class="flex " style="justify-content: space-between;align-items: center;">
										<view style="vertical-align: middle;">{{item.note}}</view>
										<view class="reserve-btn-right">
											<button class="cu-btn bg-red round sm">确认预约</button>
										</view>
									</view>
								</view>
							</view>
						</view>
					</view>

					<!--公共按钮-->
					<view class="zaiui-footer-fixed">
						<view class="flex flex-direction"><button class="cu-btn bg-red reserve_btn" @tap="confirmCount">确定</button></view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import barTitle from '@/components/basics/bar-title'

import _goods_data from '@/static/data/goods.js' //虚拟数据
import _tool from '@/static/util/tools.js' //工具函数

export default {
	components: {
		barTitle
	},
	data() {
		return {
			bannerCur: 0,
			bannerList: [],
			bottomModal: false,
			modalTitle: '预约列表',
			modalType: 'reservations',
			selectType: '',
			selectedCount: 0,
			inputCount: 0,
			commodity: {
				id: 10,
				name: 'iPhone XR',
				expectedPrice: 1000,
				originalPrice: 2000,
				description: '物品描述u五奥尔加去哦为oh',
				count: 79,
				images: []
			},
			account: {
				username: 'CV',
				avatar: ''
			},
			otherComs: [], // 发布者的其他物品,放到其他用户页面中
			comments: [
				{ fromId: 1, fromName: '大牛', fromImage: '', toName: '', content: '真的是我觉得性价比最高的机器了' },
				{ fromId: 2, fromName: '小哈', fromImage: '', toName: '', content: '想问一下这个能便宜一点吗' }
			],
			condition: '只限男生',
			expiredTime: '2020-05-20 10:07',
			reservations:[
				{
					account:{
						username: 'damao',
						avatar:''
					},
					price:2012,
					note: '最好可以有个包装'
				},
				{
					account:{
						username: 'Johnson',
						avatar:''
					},
					price: 12,
					note: '可以附赠一个本子吗'
				},
			]
		}
	},
	onLoad(params) {
		this.bannerList = _goods_data.bannerListData()
		this.otherComs = _goods_data.goodsList()

		console.log('commodity detail params', params)
		this.getCommodityDetail(params.id)
		this.showModal() // DEBUG
	},
	onReady() {
		_tool.setBarColor(true)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	methods: {
		openReservations() {
			this.modalTitle = '预约列表'
			this.modalType = 'reservations'
			this.showModal()
			
			// send reservations request
			this.$api.getReservationsByCommodity(this.commodity.id)
				.then(({data})=>{
					console.log('预约列表数据',data);
					this.reservations = data
				})
				.catch(()=>{
					console.log('获取预约列表失败');
				})
		},
		reserve() {
			if (this.selectedCount === 0) {
				this.selectTap('sell')
				return
			}
			let data = 1
			uni.showLoading({
				title: '预约处理中'
			})
			this.$api
				.addReservation(data)
				.then(({ data }) => {
					console.log('add reservation', data)
					uni.hideLoading()
					uni.showToast({
						title: '预约成功'
					})
				})
				.catch(() => {
					uni.hideLoading()
					uni.showToast({
						title: '预约失败',
						icon: 'none'
					})
				})
		},
		confirmCount() {
			this.selectedCount = this.inputCount
			this.inputCount = 0
			this.hideModal()
		},
		getCommodityDetail(id) {
			// 商品详情数据
			this.$api
				.getCommodity(id)
				.then(({ data }) => {
					console.log('commodity detail', data)
					// this.commodity = data.commodity
				})
				.catch(() => {
					console.log('请求商品数据失败')
					uni.showToast({
						title: '获取商品数据失败',
						icon: 'none'
					})
				})

			// 发布者的其他商品数据 await 拿到发布者的信息后才能操作
		},
		bannerSwiper(e) {
			this.bannerCur = e.detail.current
		},
		selectTap(type) {
			this.selectType = type
			this.modalTitle = '选择数量'
			this.modalType = 'select'
			this.showModal()
		},
		showModal() {
			this.bottomModal = true
		},
		hideModal(e) {
			this.bottomModal = false
			this.modalTitle = ''
			this.modalType = ''
		}
	}
}
</script>

<style lang="scss">
/* #ifdef APP-PLUS */
@import '../../static/colorui/main.css';
@import '../../static/colorui/icon.css';
@import '../../static/zaiui/style/app.scss';
/* #endif */
@import '../../static/zaiui/style/goods.scss';
@import '../../static/zaiui/style/footmark.scss';

.inputCount {
	margin: 10rpx;
}
.font-lg {
	font-size: 16px;
}
.reserve_box {
	bottom: 10rpx;
}
.reserve_btn {
	margin: 0 30rpx;
	font-size: 16px;
	line-height: 16px;
}
.commentAvatar {
	margin-right: 20rpx;
}
.reserve-btn-right {
	text-align: right;
}
</style>
