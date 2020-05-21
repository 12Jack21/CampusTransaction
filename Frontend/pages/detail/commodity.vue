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
		
		<!--提示-->
		<view class="bg-grey text-sm text-center padding-tb-xs text-white">真机实拍部分为真机样张，您购买的机型大致符合图中成色效果</view>
		
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
			<text class="text-price text-xxl">2999</text>
			<view class="text-xs zaiui-cost-price-num price-4">
				<view class="text-through">原价￥6999</view>
				<view>剩余79件</view>
			</view>
			<view class="text-right zaiui-time-right">
				<view>自营限时秒杀</view>
				<view class="text-xs">距结束剩余10时07分50秒</view>
			</view>
		</view>
		
		<!--标题-->
		<view class="bg-white zaiui-view-box zaiui-title-view-box">
			<view class="title-view">
				<text class="cu-tag bg-red radius sm">自营</text>
				<text class="text-black text-lg text-bold">99新苹果iPhoneX 64G深空灰色国行</text>
			</view>
			<view class="light bg-red radius margin-top-sm zaiui-title-tip-box">
				<view class="text-cut">
					<text class="margin-right-sm">官方自营</text>
					<text class="text-sm">官方自营正品保障新品体验售后无忧</text>
				</view>
				<text class="cuIcon-right icon"/>
			</view>
		</view>

		<!--选择-->
		<view class="margin-top bg-white zaiui-view-box zaiui-select-view-box">
			<view class="flex flex-wrap text-sm">
				<view class="basis-1">
					<text class="text-gray">发货</text>
				</view>
				<view class="basis-9">
					<text class="text-sm">16:00前下单，当日发货，顺丰包邮(部分地区除外)</text>
				</view>
			</view>
			
			<view class="zaiui-border-view"/>
			
			<view class="flex flex-wrap text-sm" @tap="selectTap">
				<view class="basis-1">
					<text class="text-gray">已选</text>
				</view>
				<view class="basis-8">
					<text class="text-sm">99新深空灰色64G国行三网通</text>
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
					<text class="text-black text-lg">评价（3699）</text>
				</view>
				<view class="action">
					<view class="text-sm">
						<text class="margin-right-xs">好评率</text>
						<text class="text-black text-lg">97%</text>
						<text class="cuIcon-right icon margin-left-xs"/>
					</view>
				</view>
			</view>
			<view class="zaiui-border-view"/>
			<view class="zaiui-view-box">
				<view class="flex flex-wrap text-sm">
					<view class="basis-1">
						<view class="cu-avatar sm round" style="background-image:url(/static/images/avatar/1.jpg)"/>
					</view>
					<view class="basis-9 text-sm">
						<view>仔仔</view>
						<view class="margin-top-xs">X真的是我觉得性价比最高的机器了，大小合适，全面屏操作流畅，灰色也很漂亮，超喜欢</view>
						<view class="text-gray margin-top-sm">iPhone X 64G深空灰色</view>
					</view>
				</view>
			</view>
			<view class="zaiui-border-view"/>
			<view class="zaiui-view-box">
				<view class="flex flex-wrap text-sm">
					<view class="basis-1">
						<view class="cu-avatar sm round" style="background-image:url(/static/images/avatar/2.jpg)"/>
					</view>
					<view class="basis-9 text-sm">
						<view>仔仔</view>
						<view class="margin-top-xs">X真的是我觉得性价比最高的机器了，大小合适，全面屏操作流畅，灰色也很漂亮，超喜欢</view>
						<view class="text-gray margin-top-sm">iPhone X 64G深空灰色</view>
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
				<button class="cu-btn radius sm line-red">全部商品</button>
			</view>
			<view class="zaiui-border-view"/>
			<view class="zaiui-recommend-list-box">
				<view class="text-sm">发布者的其他物品</view>
				<!--滑动列表-->
				<view class="recommend-scroll-box">
					<scroll-view class="recommend-scroll" scroll-x>
						<block v-for="(items,indexs) in otherComs" :key="indexs">
							<view :id="['scroll' + (indexs + 1 )]" class="recommend-scroll-item">
								<view class="cu-avatar xl radius" :style="[{backgroundImage:'url('+ items.img +')'}]"/>
								<view class="text-cut-2 text-sm text-black margin-tb-sm">{{items.title}}</view>
								<view class="text-red text-price margin-tb-sm text-lg">{{items.price}}</view>
							</view>
						</block>
					</scroll-view>
				</view>
			</view>
			
		</view>
		
		<!--图片详情-->
		<view class="margin-top zaiui-goods-details-box">
			<image src="/static/images/home/goods/goods-1.png" mode="widthFix"/>
			<image src="/static/images/home/goods/goods-2.png" mode="widthFix"/>
		</view>

		<!--相似推荐-->
		<view class="margin-top zaiui-view-box zaiui-recommend-list-view-box">
			<view class="flex flex-wrap">
				<view class="basis-sm text-right">
					<image class="img-aau" src="/static/zaiui/img/aau.png" lazy-load mode="widthFix"/>
				</view>
				<view class="basis-xs text-center">
					<text class="text-black text-lg">相似推荐</text>
				</view>
				<view class="basis-sm text-left">
					<image class="img-aau" src="/static/zaiui/img/aau.png" lazy-load mode="widthFix"/>
				</view>
			</view>
			
			<view class="margin-bottom zaiui-goods-list-box">
				<view class="grid col-2">
					<block v-for="(items,indexs) in goodsList" :key="indexs">
						<view class="list-itme">
							<view class="bg-white list-radius">
								<view class="goods-img">
									<view class="cu-avatar" :style="[{backgroundImage:'url('+ items.img +')'}]"/>
									<view class="mold-view" v-if="items.mold">
										<text class="cu-tag radius sm bg-red">自营</text>
									</view> 
								</view>
								<view class="view-goods-info">
									<view class="text-cut-2 text-black text-sm margin-bottom-sm">{{items.title}}</view>
									<view class="text-price text-red text-lg">{{items.price}}</view>
								</view>
							</view>
						</view>
					</block>
				</view>
			</view>
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
				commodity:{},
				account:{},
				otherComs:[] // 发布者的其他物品
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
