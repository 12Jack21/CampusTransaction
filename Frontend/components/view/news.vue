<template>
	<view class="zaiui-news-box" :class="show ? 'show' : ''">
		<!--标题栏-->
		<bar-title bgColor="bg-white" :isBack="false" @rightTap="barTitleRightTap">
			<block slot="content">消息中心</block>
			<block slot="right"><text class="cuIcon-info" /></block>
		</bar-title>

		<!--宫格菜单-->
		<view class="margin-bottom zaiui-grid-menu">
			<view class="bg-white cu-list grid col-4 no-border">
				<view class="cu-item" v-for="(item,index) in newsMenuItems" :key="index" @tap="menuTap(item.id)">
					<view class="cu-avatar lg round" :style="'background-image:url(/static/images/news/' + item.url + ');'" />
					<view class="margin-top-sm text-sm">{{item.text}}</view>
				</view>
			</view>
		</view>

		<!--消息列表-->
		<view class="bg-white zaiui-news-list-box">
			<view class="margin-bottom cu-list menu-avatar">
				<view
					v-for="(msg,index) in newsData.data"
					:key="index"
					class="cu-item goods"
					:class="modalName == 'move-box-' + index ? 'move-cur' : ''"
					@touchstart="ListTouchStart"
					@touchmove="ListTouchMove"
					@touchend="ListTouchEnd"
					:data-target="'move-box-' + index"
					@tap="tapNews(index)">
					
					<!-- 头像区 -->
					<view class="cu-avatar round"
					:style="'background-image:url(/static/images' + msg.src.length===0? 
					'/news/az3.png:' : '/avatar/1.jpg' +');'">
						<view class="cu-tag badge"></view>
					</view>
					<!-- 内容区 -->
					<view class="content">
						<view class="text-black">		
							<!-- 标题 -->
							<text class="margin-right-xs" v-if="msg.notify.targetType===3">失效通知</text>
							<text class="margin-right-xs" v-else-if="msg.notify.targetType===1">交易通知</text>
							<text class="margin-right-xs" v-else>{{msg.src}}</text>
							
							<!-- 某用户 评论了某用户,某用户预约了 -->
							<text class="cu-tag bg-pink sm radius" v-if="msg.notify.sender.length!==0">
								<text :class="msg.notify.gender ===0 ? 'cuIcon-female':'cuIcon-male'"></text>
							</text>
						</view>
						<view class="text-gray text-sm text-cut">
							预约了你的物品: 
							<text style="text-align: right;width: 100%;color: red;">
								华为手机
							</text>
						</view>
						<view class="text-gray text-sm">{{msg.time}}</view>
					</view>
					<!-- action 区-->
					<view class="action">
						<view class="cu-avatar radius"
						:style="'background-image:url(' + msg.comImage + ');'" v-if="msg.comImage"></view>
						<text style="vertical-align: middle;" v-else>去看看
							<text class="cuIcon-right"></text>
						</text>
					</view>
					
					<view class="move">
						<view class="bg-red">已读</view>
					</view>
					
					
				</view>
					
				
				<view
					class="cu-item goods"
					:class="modalName == 'move-box-' + 1 ? 'move-cur' : ''"
					@touchstart="ListTouchStart"
					@touchmove="ListTouchMove"
					@touchend="ListTouchEnd"
					:data-target="'move-box-' + 1"
					@tap="tapNews(1)">
					
					<view class="cu-avatar round" 
					style="background-image:url(/static/images/avatar/1.jpg);">
						<view class="cu-tag badge"></view>
					</view>
					<view class="content">
						<view class="text-black">
							<text class="margin-right-xs">仔仔</text>
							<text class="cu-tag bg-pink sm radius"><text class="cuIcon-female">
							</text></text>
						</view>
						<view class="text-gray text-sm text-cut">
							预约了你的物品: 
							<text style="text-align: right;width: 100%;color: red;">华为手机</text>
						</view>
						<view class="text-gray text-sm">4小时前</view>
					</view>
					<view class="action">
						<view class="cu-avatar radius" style="background-image:url(/static/images/home/goods/11.png);" />
					</view>
					<view class="move">
						<view class="bg-red">已读</view>
					</view>
				</view>
					
				<view
					class="cu-item goods"
					:class="modalName == 'move-box-' + 1 ? 'move-cur' : ''"
					@touchstart="ListTouchStart"
					@touchmove="ListTouchMove"
					@touchend="ListTouchEnd"
					:data-target="'move-box-' + 1"
					@tap="tapNews(1)">
					
					<view class="cu-avatar round" 
					style="background-image:url(/static/images/avatar/1.jpg);">
						<view class="cu-tag badge"></view>
					</view>
					<view class="content">
						<view class="text-black">
							<text class="margin-right-xs">仔仔</text>
							<text class="cu-tag bg-pink sm radius"><text class="cuIcon-male">
							</text></text>
						</view>
						<view class="text-gray text-sm text-cut">
							评论了你的物品: 
							<text style="text-align: right;width: 100%;color: red;">一加手机</text>
						</view>
						<view class="text-gray text-sm">4小时前</view>
					</view>
					<view class="action">
						<view class="cu-avatar radius" style="background-image:url(/static/images/home/goods/11.png);" />
					</view>
					<view class="move">
						<view class="bg-red">已读</view>
					</view>
				</view>

				<view
					class="cu-item goods"
					:class="modalName == 'move-box-' + 1 ? 'move-cur' : ''"
					@touchstart="ListTouchStart"
					@touchmove="ListTouchMove"
					@touchend="ListTouchEnd"
					:data-target="'move-box-' + 1"
					@tap="tapNews(1)">
					
					<view class="cu-avatar round" 
					style="background-image:url(/static/images/avatar/1.jpg);">
						<view class="cu-tag badge"></view>
					</view>
					<view class="content">
						<view class="text-black">
							<text class="margin-right-xs">仔仔</text>
							<text class="cu-tag bg-pink sm radius"><text class="cuIcon-female">
							</text></text>
						</view>
						<view class="text-gray text-sm text-cut">
							在
							<text style="text-align: right;width: 100%;color: red;">华为手机</text>
							里回复了你的评论 
						</view>
						<view class="text-gray text-sm">4小时前</view>
					</view>
					<view class="action">
						<text style="vertical-align: middle;">去看看 ></text>
					</view>
					<view class="move">
						<view class="bg-red">已读</view>
					</view>
				</view>

				<view
					class="cu-item goods"
					:class="modalName == 'move-box-' + 2 ? 'move-cur' : ''"
					@touchstart="ListTouchStart"
					@touchmove="ListTouchMove"
					@touchend="ListTouchEnd"
					:data-target="'move-box-' + 2"
					@tap="tapNews(2)">
					
					<view class="cu-avatar round" 
					style="background-image:url(/static/images/avatar/1.jpg);">
						<view class="cu-tag badge"></view>
					</view>
					<view class="content">
						<view class="text-black">
							<text class="margin-right-xs">仔仔</text>
							<text class="cu-tag bg-pink sm radius"><text class="cuIcon-female">
							</text></text>
						</view>
						<view class="text-gray text-sm text-cut">
							确认了你的预约: 
						</view>
						<view class="text-gray text-sm">4小时前</view>
					</view>
					<view class="action">
						<text style="vertical-align: middle;">去看看 ></text>
					</view>
					<view class="move">
						<view class="bg-red">已读</view>
					</view>
				</view>

				<view
					class="cu-item goods"
					:class="modalName == 'move-box-' + 3 ? 'move-cur' : ''"
					@touchstart="ListTouchStart"
					@touchmove="ListTouchMove"
					@touchend="ListTouchEnd"
					:data-target="'move-box-' + 3"
					@tap="tapNews(2)">
					
					<view class="cu-avatar round" 
					style="background-image:url(../../static/images/news/az3.png);">
						<view class="cu-tag badge"></view>
					</view>
					<view class="content">
						<view class="text-orange">
							<text class="margin-right-xs">失效通知</text>
						</view>
						<view class="text-gray text-sm text-cut">
							您预约的物品 
							 <text class="text-red">小米手机</text>
							 已失效
						</view>
						<view class="text-gray text-sm">4小时前</view>
					</view>
					<view class="action">
						<text style="vertical-align: middle;">去看看
							<text class="cuIcon-right"></text>
						</text>
					</view>
					<view class="move">
						<view class="bg-red">已读</view>
					</view>
				</view>

				<!-- 通告 已失效 -->
				<view
					class="cu-item goods"
					:class="modalName == 'move-box-' + 3 ? 'move-cur' : ''"
					@touchstart="ListTouchStart"
					@touchmove="ListTouchMove"
					@touchend="ListTouchEnd"
					:data-target="'move-box-' + 3"
					@tap="tapNews(2)">
					
					<view class="cu-avatar round" style="background-image:url(../../static/images/news/az3.png);">
						<view class="cu-tag badge"></view>
					</view>
					<view class="content">
						<view class="text-orange">
							<text class="margin-right-xs">失效通知</text>
						</view>
						<view class="text-gray text-sm text-cut">
							您的通告已失效
						</view>
						<view class="text-gray text-sm">4小时前</view>
					</view>
					<view class="action">
						<text style="vertical-align: middle;">去看看
							<text class="cuIcon-right"></text>
						</text>
					</view>
					<view class="move">
						<view class="bg-red">已读</view>
					</view>
				</view>
				
				<!-- 交易已完成/失败 -->
				<view
					class="cu-item goods"
					:class="modalName == 'move-box-' + 3 ? 'move-cur' : ''"
					@touchstart="ListTouchStart"
					@touchmove="ListTouchMove"
					@touchend="ListTouchEnd"
					:data-target="'move-box-' + 3"
					@tap="tapNews(2)">
					
					<view class="cu-avatar round" 
					style="background-image:url(../../static/images/news/azg.png);">
						<view class="cu-tag badge"></view>
					</view>
					<view class="content">
						<view class="text-orange">
							<text class="margin-right-xs">交易通知</text>
						</view>
						<view class="text-gray text-sm text-cut">
							您的交易已成功完成/失败
						</view>
						<view class="text-gray text-sm">4小时前</view>
					</view>
					<view class="action">
						<text style="vertical-align: middle;">去看看 
							<text class="cuIcon-right"></text>
						</text>
					</view>
					<view class="move">
						<view class="bg-red">已读</view>
					</view>
				</view>

			</view>
		</view>

		<uni-load-more :status="loadStatus" style="margin-bottom: 30rpx;margin-top: -20rpx;"></uni-load-more>
		<!--占位底部距离-->
		<view class="cu-tabbar-height" />
	</view>
</template>

<script>
import _tool from '@/static/zaiui/util/tools.js' //工具函数
import barTitle from '@/components/basics/bar-title.vue'
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'

import handles from '@/utils/handles.js' // handle date format
import {mapState} from 'vuex'

 /*
	 * 预约:
	 *   a 预约了 你的商品(b)
	 *   a 取消了预约 (b)
	 *   a 确认了你的预约 (b)  去看看 >
	 *   你(买家卖家)的交易(b)成功结束了
	 *   您预约的商品(b)已失效
	 *
	 * 评论:
	 *   a评论了你的商品(b)
	 *   a评论了你"     "
	 *
	 * 通告:
	 *   您的通告(b)已失效
	 *   您的通告(b)即将失效
	 *
	 * websocket 消息推送
	 * */
		 
const iniPagination = ()=>{
	return {
		pageIndex:0,
		endTime: new Date().format('yyyy-MM-dd hh:mm'),
		finish:false,
	}
}
export default {
	name: 'news',
	components: {
		barTitle,
		uniLoadMore
	},
	data() {
		return {
			newsMenuItems:[
				{id:0,url:'1.png',text:'未读消息'},
				{id:1,url:'2.png',text:'全部消息'},
				{id:2,url:'3.png',text:'交易消息'},
				{id:3,url:'4.png',text:'通告消息'}
			],
			modalName: null,
			listTouchStart: 0,
			listTouchDirection: null,
			loadStatus:'more',
			type:0,
			newsData:{
				...iniPagination(),
				data:[]
			},
		}
	},
	computed:{
		...mapState(['userId'])
	},
	props: {
		show: {
			type: Boolean,
			default: true
		},
		scrollBottom: {
			type: Boolean,
			default: false
		}
	},
	created(){
		this.getMessages()
	},
	watch: {
		scrollBottom(newVal){
			if(newVal){
				console.log('news 触底了');
				this.setReachBottom()
			}
		}
	},
	mounted() {
		_tool.setBarColor(true)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	methods: {
		menuTap(type){
			if(!(this.type == type)){
				this.type = type
				this.newsData = {...iniPagination(), data:[]}
				this.getMessages()
			}
		},
		//触底了
		setReachBottom() {
			if(!this.storeNews[this.type].finish){
				this.getMessages()
			}
		},
		getMessages(){
			this.loadStatus = 'loading'
			let params = {
				pageIndex: this.newsData.pageIndex,
				type: this.type
			}
			console.log('userId', this.userId);
			this.$api.getMessages(this.userId, params)
				.then(({data})=>{
					console.log('messages',data);
					if(data.pageIndex === data.pageCount){
						this.newsData.finish = true
						this.loadStatus = 'noMore'
					}
					
					this.newsData.data.push(...data.data)
					this.loadStatus = 'more'
				})
				.catch(()=>{
					uni.showToast({
						title: '获取消息失败,请检查网络',
						icon: 'none'
					});
					this.loadStatus = 'more'
				})
		},
		barTitleRightTap() {
			console.log('标题栏右边被点击')
		},
		// ListTouch触摸开始
		ListTouchStart(e) {
			this.listTouchStart = e.touches[0].pageX
		},
		// ListTouch计算方向
		ListTouchMove(e) {
			this.listTouchDirection = e.touches[0].pageX - this.listTouchStart > 0 ? 'right' : 'left'
		},
		// ListTouch计算滚动
		ListTouchEnd(e) {
			if (this.listTouchDirection == 'left') {
				this.modalName = e.currentTarget.dataset.target
			} else {
				this.modalName = null
			}
			this.listTouchDirection = null
		},
		//被点击
		tapNews(index) {
			console.log(index)
			if (index == 0) {
				uni.navigateTo({
					url: '/pages/news/notice'
				})
			} else if (index == 1) {
				uni.navigateTo({
					url: '/pages/news/chat'
				})
			}
		}
	}
}
</script>

<style lang="scss" scoped>
.zaiui-news-box {
	width: 100%;
	display: none;
	.zaiui-follow-box {
		.action-text-cut {
			width: 70%;
		}
	}
	.zaiui-grid-menu {
		.cu-list.grid.no-border > .cu-item {
			.cu-avatar {
				margin: 0 auto;
			}
		}
	}
	.zaiui-news-list-box {
		padding: 0 9.09rpx;
		.cu-list.menu-avatar > .cu-item > .cu-avatar {
			width: 81.81rpx;
			height: 81.81rpx;
		}
		.cu-list.menu-avatar > .cu-item {
			height: 163.63rpx;
			align-items: inherit;
			.cu-avatar {
				margin-top: 25.45rpx;
				.cu-tag.badge {
					width: 21.81rpx;
					height: 21.81rpx;
					top: 0;
					right: 0;
					border: 1.81rpx solid #fff;
				}
			}
			.content {
				left: 136.36rpx;
				margin-top: 18.18rpx;
				width: calc(100% - 90.9rpx - 54.54rpx - 18.18rpx);
				line-height: 1.7em;
				.cu-tag {
					padding: 0 3.63rpx;
					text {
						position: relative;
						top: -2rpx;
					}
				}
			}
			&:after {
				width: 0;
				height: 0;
				border-bottom: 0;
			}
		}
		.cu-list.menu-avatar > .cu-item.goods {
			.content {
				width: calc(100% - 309.09rpx);
			}
			.action {
				position: absolute;
				right: 23.63rpx;
				width: 127.27rpx;
				display: flex;
				height: 100%;
				align-items: center;
				.cu-avatar {
					width: 127.27rpx;
					height: 127.27rpx;
					margin-top: 18.18rpx;
				}
			}
		}
	}
}
.zaiui-news-box.show {
	display: block;
}
</style>
