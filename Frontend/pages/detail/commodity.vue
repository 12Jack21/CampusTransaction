<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor="bg-white">
			<block slot="content">商品详情</block>
			<block slot="right" v-if="userId === account.id && !disabled"><text class="cuIcon-notice" @tap="openReservations" /></block>
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
			<view class="text-right zaiui-time-right">
				<view>{{ expiredTime }}</view>
				<view>时失效</view>
			</view>
		</view>

		<!--标题-->
		<view class="bg-white zaiui-view-box zaiui-title-view-box">
			<view class="title-view">
				<text class="text-black text-bold" style="font-size: 1.8em;">{{ commodity.name }}</text>
			</view>
			<view class="address">
				<text class=" cuIcon-locationfill"></text>
				{{ address }}
			</view>
			<view style="font-size: 15px;">{{ commodity.description }}</view>
		</view>

		<!--选择-->
		<view class="margin-top bg-white zaiui-view-box zaiui-select-view-box ">
			<view class="flex flex-wrap text-sm">
				<view class="basis-1"><text class="text-gray">条件</text></view>
				<view class="basis-9">
					<text class="text-sm">{{ condition }}</text>
				</view>
			</view>

			<view v-if="userId !== account.id">
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
				<view class="zaiui-border-view" />

				<view class="flex flex-wrap text-sm" @tap="selectTap">
					<view class="basis-1"><text class="text-gray">备注</text></view>
					<view class="basis-8">
						<text class="text-sm">{{ selectedNote | noteFilter }}</text>
					</view>
					<view class="basis-1">
						<view class="text-gray text-right"><text class="cuIcon-right icon" /></view>
					</view>
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
					<view class="flex ">
						<view style="flex: 0 0 12%;" @tap="fromAccTap(item.fromId)">
							<view class="cu-avatar round commentAvatar" :style="{ backgroundImage: item.fromImage.length === 0 ? 'url(/static/images/avatar/1.jpg)' : item.fromImage }" />
						</view>
						<view style="flex: 0 0 86%;" class="text-lg flex flex-direction">
							<view class="font-lg" @tap="fromAccTap(item.fromId)">{{ item.fromName }}</view>
							<view class="margin-top-xs" style="font-size: 16px;" @tap="commentTap(item.fromId, item.fromName)">
								<text class="text-blue" v-if="item.toName" style="margin-right: 10rpx;">@{{ item.toName }}:</text>
								{{ item.content }}
							</view>
							<view class="text-gray comment-date">{{ item.date }}</view>
						</view>
					</view>
				</view>
			</view>
			<!-- 添加评论 -->
			<view class="add-comment"><textarea style="height: 140rpx;" v-model="myComment" :placeholder="comment_ph" /></view>
			<view class="flex comment-handle">
				<button class="cu-btn c-btn" hover-class="c-btn-hover" :disabled="disabled" @tap="clearComment">清空</button>
				<button class="cu-btn c-btn" hover-class="c-btn-hover" :disabled="disabled" @tap="addComment">发布</button>
			</view>
			<!-- end -->
		</view>

		<!-- 发布者信息-->
		<view class="margin-top bg-white zaiui-view-box zaiui-goods-info-view-box">
			<view class="zaiui-shop-view">
				<view class="cu-avatar lg round" :style="{ backgroundImage: account.avatar.length === 0 ? 'url(/static/images/avatar/1.jpg)' : account.avatar }" @tap="accTap" />
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
		<view class="zaiui-footer-fixed reserve_box" style="z-index: 8;">
			<view class="flex flex-direction">
				<button class="cu-btn bg-orange reserve_btn" @tap="updateModal = true" v-if="userId === account.id" :disabled="disabled">更新信息</button>
				<button class="cu-btn bg-red reserve_btn" @tap="reserve" :disabled="disabled" v-else>立即预约</button>
			</view>
		</view>

		<!--弹出框-->
		<view class="cu-modal bottom-modal zaiui-bottom-modal-box" :class="bottomModal ? 'show' : ''">
			<view class="cu-dialog bg-white">
				<!--标题-->
				<view class="text-black text-center margin-tb text-lg zaiui-title-bar">
					<text>{{ modalTitle }}</text>
					<text class="cuIcon-close close-icon" @tap="hideModal"></text>
				</view>

				<!-- 模态框内容区域 -->
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

						<!-- 备注 -->
						<view class="zaiui-select-btn-list-boox">
							<view class="select-item" style="margin-left: 20rpx;">
								<view class="text-black text-lg text-bold">备注</view>
								<view class="add-comment"><textarea type="text" v-model="inputNote" placeholder="输入备注" /></view>
							</view>
						</view>
						<!-- end -->
					</view>

					<!--选择排队的预约 -->
					<view class="zaiui-view-box select" v-if="modalType == 'reservations'">
						<!--预约列表-->
						<view class="bg-white zaiui-goods-list-view" style="padding: 8rpx;">
							<view class="flex flex-direction" v-for="(item, index) in reservations" :key="index">
								<view class="zaiui-goods-list-box">
									<view
										class="cu-avatar radius"
										@tap="fromAccTap(item.account.id)"
										:style="{ backgroundImage: item.account.avatar.length === 0 ? 'url(/static/images/avatar/default.png)' : item.acocunt.avatar, 'z-index': 10 }"
									/>
									<view class="goods-info-view">
										<view class="flex" style="justify-content: space-between;">
											<text class="text-black" @tap="fromAccTap(item.account.id)">{{ item.account.username }}</text>
											<text class="text-grey">{{ item.createTime }}</text>
										</view>
										<view class="flex " style="justify-content: space-between;align-items: center;">
											<view>
												<view style="vertical-align: middle;" @tap="noteTap(item)">{{ item.note | noteFilter }}</view>
												<view class="text-orange">X {{ item.count }}</view>
											</view>
											<view class="reserve-btn-right" style="min-width: 120rpx;" @tap="confirmReserve(item)">
												<button class="cu-btn bg-red round sm" :disabled="item.disabled">确认预约</button>
											</view>
										</view>
									</view>
								</view>
								<uni-transition :mode-class="['zoom-in']" :show="item.showNote">备注: {{ item.note }}</uni-transition>
							</view>
						</view>
					</view>

					<!--公共按钮-->
					<view class="zaiui-footer-fixed">
						<view class="flex flex-direction"><button class="cu-btn bg-red reserve_btn" @tap="confirmCount" :disabled="disabled">确定</button></view>
					</view>
				</view>
			</view>
		</view>

		<mpopup @uuidCallback="uuidCallback" @closeCallback="closeCallback" ref="mpopup" :isdistance="true"></mpopup>
		<modal-commodity :commodity="commodity" :show="updateModal" @closeUpdate="updateModal = false" @updateCom="updateCom"></modal-commodity>
	</view>
</template>

<script>
import barTitle from '@/components/basics/bar-title'
import uniTransition from '@/components/uni-transition/uni-transition.vue'
import modalCommodity from '@/components/basics/modal-commodity.vue'

import _goods_data from '@/static/data/goods.js' //虚拟数据
import _tool from '@/static/util/tools.js' //工具函数
import { mapState } from 'vuex'
import handles from '../../utils/handles.js'

export default {
	components: {
		barTitle,
		uniTransition,
		modalCommodity
	},
	data() {
		return {
			updateModal: false,
			popup_uuid: [], // for popup control
			bannerCur: 0,
			bannerList: [],
			bottomModal: false,
			modalTitle: '预约列表',
			modalType: 'reservations',
			selectType: '',
			selectedCount: 0,
			selectedNote: '',
			inputCount: null,
			inputNote: '',
			comment_ph: '输入你的评论',
			toCommentId: -1, //发送给的用户的 id
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
				id: 1,
				username: 'CV',
				avatar: ''
			},
			comments: [
				{ fromId: 1, fromName: '大牛', fromImage: '', toName: '', content: '真的是我觉得性价比最高的机器了真的是我觉得性价比最高的机器了', date: '2020-10-09' },
				{ fromId: 2, fromName: '小哈', fromImage: '', toName: '大牛', content: '想问一下这个能便宜一点吗', date: '2020-08-01' }
			],
			myComment: '',
			condition: '只限男生',
			expiredTime: '2020-05-29 10:07',
			address: '信息学部二食堂',
			state_enum: 'PUBLISHED', // CANCELLED PUBLISHED
			reservations: [
				{
					id: 1,
					account: {
						username: 'damao',
						avatar: ''
					},
					price: 2012,
					note: '最好可以有个包装最好可以有个包装最好可以有个包装', //最多24个字符
					createTime: '2020-10-09 10:09',
					count: 10
				},
				{
					id: 20,
					account: {
						username: 'Johnson',
						avatar: ''
					},
					price: 12,
					note: '可以附赠一个本子吗',
					createTime: '2020-01-09 10:09',
					count: 1
				}
			]
		}
	},
	computed: {
		...mapState(['userId']),
		disabled() {
			let now = new Date().getTime()
			let expire = new Date(this.expiredTime).getTime()
			if (now >= expire || this.state_enum == 'CANCELLED') return true
			return false
		}
	},
	onLoad(params) {
		this.bannerList = _goods_data.bannerListData()
		console.log('commodity detail params', params)
		this.getCommodityDetail(params.id)
	},
	onPullDownRefresh() {
		this.getCommodityDetail(this.commodity.id)
	},
	onReady() {
		_tool.setBarColor(true)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	filters: {
		noteFilter(val) {
			if (val.length >= 20) return val.slice(0, 20) + '...'
			return val
		}
	},
	methods: {
		updateCom(body) {
			this.tip(4, '更新中', true)

			this.$api
				.updateCommodity(this.commodity.id, body)
				.then(({ data }) => {
					if (data.success) {
						this.commodity = Object.assign(this.commodity, body)
						this.update(0)
					} else this.update(1, '更新失败')
				})
				.catch(() => this.update(1))
		},
		noteTap(re) {
			console.log('noteTap', re)
			if (typeof re.showNote == 'undefined') this.$set(re, 'showNote', true)
			// 添加响应式属性
			else re.showNote = !re.showNote
		},
		confirmReserve(reservation) {
			// 确认预约
			this.$api
				.confirmReservation(reservation.id)
				.then(({ data }) => {
					if (data.success) {
						thi.$set(reservation, 'disabled', true)
						uni.showToast({
							title: '预约确认成功'
						})
					} else
						uni.showToast({
							title: '预约确认失败',
							icon: 'none'
						})
				})
				.catch(() =>
					uni.showToast({
						title: '网络异常',
						icon: 'none',
						position: 'bottom'
					})
				)
		},
		accTap() {
			uni.navigateTo({
				url: '../../pages/account/account?id=' + this.account.id
			})
		},
		fromAccTap(id) {
			uni.navigateTo({
				url: '../../pages/account/account?id=' + id
			})
		},
		reserveTap(id) {
			this.fromAccTap(id)
		},
		clearComment() {
			this.toCommentId = -1
			this.comment_ph = '输入你的评论'
			this.myComment = ''
		},
		addComment() {
			let body = {
				fromId: parseInt(this.userId),
				toId: this.toCommentId,
				commodityId: this.commodity.id,
				content: this.myComment,
				date: new Date().format('yyyy-MM-dd hh:mm')
			}
			//发布评论
			this.$api
				.addComment(body)
				.then(({ data }) => {
					if (data.success) {
						this.tip(0, '评论发布成功')
						this.comments.push(data.comment)
						this.clearComment()
					}
				})
				.catch(() => this.err())
		},
		commentTap(id, name) {
			//回复
			if (parseInt(this.userId) === this.toCommentId)
				//回复自己
				return
			this.toCommentId = id
			this.comment_ph = '@' + name + ': '
		},
		openReservations() {
			this.modalTitle = '预约列表'
			this.modalType = 'reservations'
			this.showModal()

			// send reservations request
			this.$api
				.getReservationsByCommodity(this.commodity.id)
				.then(({ data }) => {
					console.log('预约列表数据', data)
					this.reservations = data
				})
				.catch(() => {
					console.log('获取预约列表失败')
					this.err()
				})
		},
		reserve() {
			if (this.selectedCount === 0 || this.selectedNote.length === 0) {
				this.selectTap('sell')
				return
			}
			let data = {
				accountId: this.userId,
				count: this.selectedCount,
				note: this.selectedNote,
				commodityId: this.commodity.id
			}
			// can update
			this.tip(4, '预约处理中', true)

			this.$api
				.addReservation(data)
				.then(({ data }) => {
					console.log('add reservation', data)
					this.update(0)
				})
				.catch(() => this.update(1))
		},
		confirmCount() {
			this.selectedCount = this.inputCount
			this.selectedNote = this.inputNote
			this.inputCount = null
			this.hideModal()
		},
		getCommodityDetail(id) {
			// 商品详情数据
			this.$api
				.getCommodity(id)
				.then(({ data }) => {
					console.log('commodity detail', data)
					this.commodity = data.commodity
					this.expiredTime = data.expiredTime
					this.condition = data.condition
					this.address = data.address
					this.state_enum = data.state_enum
					this.account = data.account
					this.comments = data.comments
				})
				.catch(() => {
					console.log('请求商品数据失败')
					this.err()
				})
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
		},
		tip(index, content, isClick = false, timeout = 2000) {
			const types = ['success', 'err', 'warn', 'info', 'loading']
			this.$refs.mpopup.open({
				type: types[index],
				content,
				timeout,
				isClick
			})
		},
		err() {
			this.tip(1, '网络异常')
		},
		uuidCallback(uuid) {
			//uuid回传
			// console.log('uuidCallback');
			//存起来
			this.popup_uuid.push({ uuid })
		},
		closeCallback(uuid) {
			// popup关闭回传
			// console.log('closeCallBack');
			for (var i = 0; i < this.popup_uuid.length; i++) {
				if (this.popup_uuid[i].uuid == uuid) {
					//移除uuid
					this.popup_uuid.splice(i, 1)
					break
				}
			}
		},
		update: function(index, content) {
			//修改
			// console.log('update');
			if (this.popup_uuid.length == 0) return
			this.$refs.mpopup.update({
				uuid: this.popup_uuid[0].uuid,
				type: index === 0 ? 'success' : 'err',
				content: content ? content : index === 0 ? '加载成功' : '网络异常'
			})
			setTimeout(() => this.close(), 2000) // 2s 后自动消失
		},
		close: function() {
			//控制关闭
			// console.log('close');
			if (this.popup_uuid[0]) {
				this.$refs.mpopup.close(this.popup_uuid[0].uuid)
				this.popup_uuid.splice(0, 1)
			}
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

.address {
	color: #e54d42;
	background-color: #fadbd9;
	border-radius: 6px;
	padding: 6px;
	width: fit-content;
	margin: 8px 0;
	font-size: 0.8em;
}
.add-comment {
	padding: 30rpx;
	border: #efebeb 1px solid;
	border-radius: 20rpx;
	margin: 0 10rpx;
}
.comment-handle {
	display: flex;
	justify-content: space-around;
	.c-btn {
		background-color: #a59f9e;
		color: #eee;
		font-size: 15px;
		margin-top: 8rpx;
		width: 40%;
	}
}
.comment-date {
	font-size: 13px;
	margin-top: 10rpx;
}
.c-btn-hover {
	background-color: #e54d42 !important;
}
.inputCount {
	margin: 10rpx;
}
.font-lg {
	font-size: 17px;
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
