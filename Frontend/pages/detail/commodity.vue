<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor='bg-white'>
			<block slot="content">商品详情</block>
			<block slot="right">
				<text class="cuIcon-forward"/>
				<text class="cuIcon-more"/>
			</block>
		</bar-title>
		
		<!--轮播图-->
		<view class="zaiui-banner-swiper-box">
			<swiper class="screen-swiper" circular autoplay @change="bannerSwiper">
				<swiper-item v-for="(item,index) in bannerList" :key="index">
					<image :src="item.url" mode="aspectFill"/>
				</swiper-item>
			</swiper>
			<!--页码-->
			<text class="cu-tag bg-grey round sm zaiui-page">{{bannerCur + 1}} / {{bannerList.length}}</text>
		</view>
		
		<!--限时秒杀-->
		<view class="zaiui-limited-seckill-box">
			<text class="text-price text-xxl">{{commodity.expectedPrice}}</text>
			<view class="text-xs zaiui-cost-price-num price-4">
				<view class="text-through">￥{{commodity.originalPrice}}</view>
				<view>剩余{{commodity.count}}件</view>
			</view>
			<view class="text-right zaiui-time-right">
				<view class="text-xs">{{expiredTime}}</view>
				<view class="text-xs">时失效</view>
			</view>
		</view>
		
		<!--标题-->
		<view class="bg-white zaiui-view-box zaiui-title-view-box">
			<view class="title-view">
				<text class="text-black text-lg text-bold" style="font-size: 40rpx;">99新苹果{{commodity.name}}</text>
			</view>
			<br>
			<view>物品描述{{commodity.description}}</view>
		</view>

		<!--选择-->
		<view class="margin-top bg-white zaiui-view-box zaiui-select-view-box">
			<view class="flex flex-wrap text-sm">
				<view class="basis-1">
					<text class="text-gray">条件</text>
				</view>
				<view class="basis-9">
					<text class="text-sm">只限女生{{condition}}</text>
				</view>
			</view>
			
			<view class="zaiui-border-view"/>
			
			<view class="flex flex-wrap text-sm" @tap="selectTap">
				<view class="basis-1">
					<text class="text-gray">已选</text>
				</view>
				<view class="basis-8">
					<text class="text-sm">{{selectedCount}} 件</text>
				</view>
				<view class="basis-1">
					<view class="text-gray text-right">
						<text class="cuIcon-right icon"/>
					</view>
				</view>
			</view>
		</view>
		
		<!--评论-->
		<view class="margin-top bg-white zaiui-comment-view-box">
			<view class="cu-bar bg-white">
				<view class="action">
					<text class="text-black text-lg text-bold">评论</text>
				</view>
			</view>
			<!-- 评论列表 -->
			<view v-for="(item,index) in comments" :key="index">			
				<view class="zaiui-border-view"/>
				<view class="zaiui-view-box">
					<view class="flex flex-wrap text-sm">
						<view class="basis-1" @tap="fromAccTap(item.fromId)">
							<view class="cu-avatar sm round" 
							:style="{backgroundImage:item.fromImage.length===0?'url(/static/images/avatar/1.jpg)':item.fromImage}"/>
						</view>
						<view class="basis-9 text-sm">
							<view>item.fromName</view>
							<view class="margin-top-xs">{{item.content}}</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 发布者信息-->
		<view class="margin-top bg-white zaiui-view-box zaiui-goods-info-view-box">
			<view class="zaiui-shop-view">
				<view class="cu-avatar lg round" style="background-image:url(/static/images/avatar/1.jpg)"/>
				<view class="text-view">
					<view class="margin-bottom-xs">仔仔店铺</view>
					<view class="text-sm text-cut">仔仔店铺，正品保障，售后无忧</view>
				</view>
			</view>
			<view class="zaiui-border-view"/>
			
		</view>
		
		<!--图片详情-->
		<view class="margin-top zaiui-goods-details-box">
			<image src="/static/images/home/goods/goods-1.png" mode="widthFix"/>
			<image src="/static/images/home/goods/goods-2.png" mode="widthFix"/>
		</view>

		<!--占位底部距离-->
		<view class="cu-tabbar-height"/>
		
		<!--底部操作-->
		<view class="zaiui-footer-fixed">
				<view class= "flex flex-direction">				
					<button class="cu-btn bg-red lg"  @tap="selectTap('sell')">立即预约</button>
				</view>
		</view>
		
		<!--弹出框-->
		<view class="cu-modal bottom-modal zaiui-bottom-modal-box" :class="bottomModal?'show':''">
			<view class="cu-dialog bg-white">
				<!--标题-->
				<view class="text-black text-center margin-tb text-lg zaiui-title-bar">
					<text>{{modalTitle}}</text>
					<text class="cuIcon-close close-icon" @tap="hideModal"></text>
				</view>
				
				<!--内容区域-->
				<view class="zaiui-modal-content">
					<!--选择规格-->
					<view class="zaiui-view-box select" v-if="modalType=='select'">
						<!--商品信息-->
						<view class="cu-list menu-avatar">
							<view class="cu-item">
								<view class="cu-avatar radius lg" style="background-image:url(/static/images/home/goods/1.png);"/>
								<view class="content">
									<view class="text-price-view">
										<text class="text-price text-red margin-right-xs">2699</text>
										<text class="text-sm text-gray text-through">￥6999</text>
										<text class="cu-tag bg-gradual-red radius sm">
											<text class="cuIcon-hotfill"/>
											<text>秒杀中</text>
										</text>
									</view>
									<view class="text-black text-sm flex">
										<view class="text-cut">已选: 99新深空灰色64G国行三网通</view>
									</view>
								</view>
							</view>
						</view>
						
						<!--规格数据-->
						<view class="zaiui-select-btn-list-boox">
							<view class="select-item">
								<view class="text-black">成色</view>
								<view class="select-btn">
									<button class="cu-btn">95新</button>
									<button class="cu-btn">9成新</button>
									<button class="cu-btn light bg-red">99新</button>
								</view>
							</view>
							
							<view class="select-item">
								<view class="text-black">颜色</view>
								<view class="select-btn">
									<button class="cu-btn light bg-red">深空灰色</button>
									<button class="cu-btn" disabled>银色</button>
								</view>
							</view>
							
							<view class="select-item">
								<view class="text-black">容量</view>
								<view class="select-btn">
									<button class="cu-btn" disabled>256G</button>
									<button class="cu-btn light bg-red">64G</button>
								</view>
							</view>
							
							<view class="select-item">
								<view class="text-black">版本</view>
								<view class="select-btn">
									<button class="cu-btn">港澳版移动4G/联通4G</button>
									<button class="cu-btn">其他版本移动4G/联通4G</button>
									<button class="cu-btn">其他版本三网通</button>
									<button class="cu-btn light bg-red">国行三网通</button>
								</view>
							</view>
							
						</view>
						
					</view>
					
					<!--公共按钮-->
					<view class="zaiui-footer-fixed">
						<view class="flex flex-direction">
							<button class="cu-btn bg-red lg">确定</button>
						</view>
					</view>
					
				</view>
			</view>
		</view>
		
	</view>
</template>

<script>
	import barTitle from '@/components/basics/bar-title';
	
	import _goods_data from '@/static/data/goods.js';	//虚拟数据
	import _tool from '@/static/util/tools.js';	//工具函数
	export default {
		components: {
			barTitle,
		},
		data() {
			return {
				bannerCur: 0, bannerList: [], bottomModal: false, modalTitle: '', modalType: 'promotion', selectType: '',
				selectedCount: 2,
				commodity:{
					expectedPrice: 1000,
					originalPrice: 2000,
					count: 79,
				},
				account:{},
				otherComs:[], // 发布者的其他物品
				comments:[
					{fromId:1,fromName:'大牛',fromImage:'',toName:'',content:'真的是我觉得性价比最高的机器了'},
					{fromId:2,fromName:'小哈',fromImage:'',toName:'',content:'想问一下这个能便宜一点吗'}
					],
				condition: '只限男生',
				expiredTime: '2020-05-20 10:07',
			}
		},
		onLoad(params) {
			this.bannerList = _goods_data.bannerListData();
			this.otherComs = _goods_data.goodsList();
			
			console.log('commodity detail params', params);
			this.getCommodityDetail(params.id)
		},
		onReady() {
			_tool.setBarColor(true);
			uni.pageScrollTo({
			    scrollTop: 0,
			    duration: 0
			});
		},
		methods: {
			getCommodityDetail(id){
				// 商品详情数据
				this.$api.getCommodity(id)
					.then(({data})=>{
						console.log('commodity detail',data);
						this.commodity = data.commodity
					})
					.catch(()=>{
						console.log('请求商品数据失败');
						uni.showToast({
							title: '获取商品数据失败',
							icon: 'none'
						});
					})
				
				// 发布者的其他商品数据 await 拿到发布者的信息后才能操作
				
			},
			bannerSwiper(e) {
				this.bannerCur = e.detail.current;
			},
			selectTap(type) {
				this.selectType = type;
				this.modalTitle = "选择数量";
				this.modalType = 'select';
				this.showModal();
			},
			showModal() {
				this.bottomModal = true;
			},
			hideModal(e) {
				this.bottomModal = false;
				this.modalTitle = "";
				this.modalType = '';
			},
			myCartTap(){
				uni.navigateTo({
					url: '/pages/goods/my_cart'
				});
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
	@import "../../static/zaiui/style/goods.scss";
</style>
