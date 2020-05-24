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
					>
					
					<!-- 头像区 -->
					<view class="cu-avatar round"
					v-bind:style="[{'background-image':'url('+ (msg.sender==-1? 
					((msg.action===5 || msg.action===10)? '/static/images/news/azg.png': '/static/images/news/az3.png') : (msg.avatar.length === 0? '/static/images/avatar/default.png':msg.avatar) ) + ')' }]" 
					@tap="accTap(msg.sender, msg.avatar.length!==0)">
						<!-- 已读/未读 -->
						<view class="cu-tag badge" v-if="!msg.isRead"></view>
					</view>
					<!-- 内容区 -->
					<view class="content">
						<view class="text-black">		
							<!-- 标题 -->
							<text class="margin-right-xs" v-if="msg.accountName">{{msg.accountName}}</text>
							<text class="margin-right-xs" v-else-if="msg.action===7">失效通知</text>
							<text class="margin-right-xs" v-else-if="msg.action===5||msg.action===10">交易通知</text>
							
							<!-- 某用户 评论了某用户,某用户预约了 -->
							<text class="cu-tag sm radius" :class="msg.accountGender === 0 ? 'bg-pink':'bg-blue'" 
								v-if="msg.accountName">
								<text :class="msg.accountGender === 0 ? 'cuIcon-female':'cuIcon-male'">
								</text>
							</text>
						</view>
						<view class="text-gray text-sm text-cut">
							{{preSentence(msg.action, msg.commodity.name.length!==0)}} 
							<text style="color: red;">
								{{msg.commodity.name}}
							</text>
							{{postSentence(msg.action,msg.commodity.name.length!==0)}}
						</view>
						<view class="text-gray text-sm">{{msg.createTime}}</view>
					</view>
					<!-- action 区-->
					<view class="action" @tap="actionTap(msg.targetId,msg.targetType)">
						<view class="cu-avatar radius"
						:style="'background-image:url(' + (msg.commodity.imageURL.length!==0?msg.commodity.imageURL:'/static/images/comDefault.png') + ');'" v-if="msg.commodity.name">
						</view>
						<text style="vertical-align: middle;" v-else>去看看
							<text class="cuIcon-right"></text>
						</text>
					</view>
					
					<view class="move">
						<view class="bg-red" @tap="readTap(index)">已读</view>
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
import news from '@/static/data/messages.js'
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
		pageIndex:1,
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
				data: []
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
		this.newsData.data = news
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
		actionTap(targetId,targetType){
			console.log('actionTap targetType',targetType);
			switch(targetType){
				case 0:
					uni.navigateTo({
						url: `/pages/commodity?id=${targetId}`
					});
					break
				case 1:
					uni.navigateTo({
						url: `/pages/reservation?id=${targetId}`
					});
					break
				case 2:
					uni.navigateTo({
						url: `/pages/commodity?id=${targetId}&comment=1` //评论区
					});
					break
				case 3:
					uni.navigateTo({
						url: `/pages/notice?id=${targetId}`
					});
					break
			}
		},
		accTap(sender, bool){
			console.log('account tap');
			if(bool){
				uni.navigateTo({
					url: `/pages/account?id=${sender}`
				});
			}
		},
		readTap(index){
			//点击已读
			let msg = this.newsData.data[index]
			let ids = []
			ids.push(msg.id)
			this.$api.readMessages(ids)
				.then(({data})=>{
					if(data.success){
						msg.isRead = true
						this.newsData.data.splice(index,1,msg) // 更新已读状态
					}
				})
		},
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
				type: this.type,
				endTime: this.newsData.endTime
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
		preSentence(action,isCom){
			switch(action){
				case 1:
					return '取消了你的预约'
				case 2:
					return '评论了你的物品'
				case 3:
					return '确认了你的预约'
				case 3:
					return '在' // 回复评论
				case 5:
					return '您的交易已完成'
				case 7:
					if(isCom) //物品
						return '您预约的物品'
					else //通告
						return '您的通告已失效'
				case 8:
					return '在'
				case 9:
					return '预约了你的物品'
				case 10:
					return '您的交易已失败'
				default:
					return ''
			}
		},
		postSentence(action,isCom){
			switch(action){
				case 7:
					if(isCom)
						return '已失效'
					else
						return ''
				case 8:
					return '里回复了你的评论' // 回复评论
				default:
					return ''
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
