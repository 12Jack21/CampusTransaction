<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor="bg-white" isBack><block slot="content">详情</block></bar-title>

		<!--步骤条区域-->
		<view class="bg-white padding solid-top" v-if="basics != 4">
			<!--步骤条-->
			<view class="cu-steps">
				<block v-for="(item, index) in basicsList" :key="index">
					<view class="cu-item" :class="index > basics ? '' : 'select'">
						<view class="icon-view" v-if="index > basics"><text class="text-red" :class="'cuIcon-' + item.cuIcon"></text></view>
						<view class="bg-red icon-view" v-else><text :class="'cuIcon-' + item.cuIcon"></text></view>
						<view class="text-sm text-black" v-if="index > basics">{{ item.name }}</view>
						<view class="text-sm text-black" v-else>{{ item.name_s }}</view>
					</view>
				</block>
			</view>

			<!--提示-->
			<view class="text-sm text-center margin-top" v-if="basics == 0">
				<view class="text-black">预约成功，待卖家确认。</view>
				<view class="text-black">
					<text>若预约未被确认，其将自动取消。</text>
				</view>
			</view>
			<view class="text-sm text-center margin-top" v-if="basics == 1"><view class="text-black">沟通成功，待卖家发货.</view></view>
			<view class="text-sm text-center margin-top" v-if="basics == 2"><view class="text-black">已发货，正在等待收货中.</view></view>
			<view class="text-sm text-center margin-top" v-if="basics == 3">
				<view class="text-black">双方已交易，请双方确认无误后点击交易完成。</view>
				<view class="text-black">
					<text>分后将自动确认交易</text>
				</view>
			</view>
			
		</view>

		<!--状态图标-->
		<view class="bg-white padding solid-top text-center zaiui-status-img-view" v-if="basics == 4 && !cancel">
			<view class="are-img-view" @tap="cancel = true"><image class="are-img" src="/static/zaiui/img/are.png" mode="widthFix" /></view>
			<view class="text-sm text-black">交易成功，待发表评价</view>
		</view>

		<!--状态图标-->
		<view class="bg-white padding solid-top text-center zaiui-status-img-view" v-if="basics == 4 && cancel">
			<view class="are-img-view" @tap="cancel = false"><image class="are-img" src="/static/zaiui/img/arg.png" mode="widthFix" /></view>
			<view class="text-sm text-black">订单已取消</view>
		</view>

		<!--物流信息-->
		<view class="bg-white zaiui-card-box" v-if="basics != 0">
			<view class="zaiui-card-view zaiui-address-view">
				<view class="text-lg text-bold text-black">交易信息</view>
				<view class="solid-line"></view>
				<view class="cu-list menu-avatar">
					<view class="cu-item">
						<view class="bg-grey icon-view"><text class="cuIcon-locationfill" /></view>
						<view class="content">
							<view class="text-black">
								<text>交易双方：</text>
								<text>{{ reservation.buyer }}</text>
								<text>{{ reservation.buyer }}</text>
							</view>
							<view class="text-gray text-sm flex">
								<view class="text-cut">{{ reservation.buyerRegion }}</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!--商品信息-->
		<view class="bg-white zaiui-card-box">
			<view class="zaiui-card-view zaiui-shop-view">
				<view class="shop-info-view">
					<view class="cu-avatar round sm" :style="[{ backgroundImage: avatarURL }]" />
					<view class="text-black text-bold text-lg title-view">{{ reservation.seller }}</view>
				</view>
				<view class="goods-list-view">
					<view class="cu-avatar radius" :style="[{ backgroundImage: comImg }]" />
					<view class="reservation-goods-info-view">
						<view class="text-black text-cut name">{{ reservation.title }}</view>
						<view class="text-gray text-sm text-cut introduce">{{ reservation.description }}</view>
						<view class="text-price text-red text-lg">{{ reservation.price }}</view>
					</view>
				</view>
			</view>
		</view>

		<!--商品金额-->
		<view class="bg-white zaiui-card-box">
			<view class="zaiui-card-view zaiui-price-view">
				<view class="text-black title-view">
					<view class="title">商品总额</view>
					<view class="text-right">
						<text class="text-price">{{ reservation.price }}</text>
					</view>
				</view>

				<view class="text-black text-bold title-right-view">
					<text class="margin-right-xs">应付款：{{ reservation.totalPrice }}</text>
					<text class="text-price"></text>
				</view>
			</view>
		</view>

		<!--订单信息-->
		<view class="bg-white zaiui-card-box">
			<view class="zaiui-card-view zaiui-order-view">
				<view class="text-lg text-bold text-black">订单信息</view>
				<view class="solid-line"></view>
				<view class="text-black title-view">
					<view class="title">订单编号</view>
					<view class="text-right">
						<text class="margin-right-xs">{{ reservation.id }}</text>
						<button class="cu-btn sm line-black">复制</button>
					</view>
				</view>

				<view class="text-black title-view">
					<view class="title">预约时间</view>
					<view class="text-right">
						<text>{{ reservation.createTime }}</text>
					</view>
				</view>
				<view class="text-black title-view" v-if="basics > 0">
					<view class="title">确认时间</view>
					<view class="text-right">
						<text>{{ reservation.transactionTime2 }}</text>
					</view>
				</view>
				<view class="text-black title-view" v-if="basics > 1">
					<view class="title">交易时间</view>
					<view class="text-right">
						<text>{{ reservation.transactionTime3 }}</text>
					</view>
				</view>
			</view>
		</view>

		<view class="bg-white zaiui-card-hight-box" />

		<!--底部-->
		<view class="foot-hight-view" />

		<view class="bg-white zaiui-footer-fixed zaiui-foot-padding-bottom" v-if="basics == 0">
			<button class="cu-btn bg-orange sm" @tap="nextTap">测试下一步</button>
			<button class="cu-btn line-black radius" @click="cancelReservation()">取消预约</button>
			<button class="cu-btn bg-red" @click="confirmReservation()">确认预约</button>
		</view>

		<view class="bg-white zaiui-footer-fixed zaiui-foot-padding-bottom" v-if="basics == 1">
			<button class="cu-btn bg-orange sm" @tap="nextTap">测试下一步</button>
			<button class="cu-btn line-black radius" @click="cancelTransaction()">取消交易</button>
			<button class="cu-btn bg-red" @click="remindTransaction()">确认交易</button>
		</view>

		<view class="bg-white zaiui-footer-fixed zaiui-foot-padding-bottom" v-if="basics == 2">
			<button class="cu-btn bg-orange sm" @tap="nextTap">测试下一步</button>
			<button class="cu-btn line-black radius" @tap="appraiseTap">发表评价</button>
			<button class="cu-btn bg-red" @tap="confirmReceipt" @click="confirmTransaction()">确认交易</button>
		</view>

		<view class="bg-white zaiui-footer-fixed zaiui-foot-padding-bottom" v-if="basics == 3">
			<button class="cu-btn bg-orange sm" @tap="nextTap">测试第一步</button>
		</view>
		
		<!--弹出框-->
		<view class="cu-modal bottom-modal zaiui-bottom-modal-box" :class="modalShow ? 'show' : ''">
			<view class="cu-dialog bg-white updateDialog">
				<view class="text-black text-center margin-tb text-lg title-bar">
					<text>评价</text>
					<text class="cuIcon-close close-icon" @tap="modalShow=false"></text>		
				</view>
		
				<!-- 模态框内容区域 -->
				<view class="zaiui-modal-content">
					<!-- 修改评价 -->
					<view class="zaiui-view-box select">										
						<!-- 新评价 -->
						<view class="usernameItem">
							<view class="flex new">
								<view class="text-black text-lg text-bold">新评级:</view>
								<input type="text" v-model="newEvaluation" placeholder="输入新昵称"  maxlength="12"/>
							</view>
							<view class="flex old text-lg">
								<view class="text-gray ">原评级:</view>
								<text class="text-gray ">{{evaluation}}</text>
							</view>
						</view>
						<!-- end -->
					</view>						
					<!-- 保存更新 -->
					<view class="zaiui-footer-fixed" style="z-index: 10;">
						<view class="flex flex-direction">
							<button class="cu-btn bg-red" style="font-size: 1.4em;padding: 10rpx 0;" @tap="confirm" >
								保存
							</button>
						</view> 
					</view>
					
				</view>
			
			</view>
		</view>
		
</view>
</template>

<script>
import barTitle from '@/components/basics/bar-title'
import _tool from '@/static/zaiui/util/tools.js' //工具函数

export default {
	components: {
		barTitle
	},
	data() {
		return {
			modalShow:false,//评价
			basics: 0,
			basicsList: [
				{ cuIcon: 'cartfill', name: '未预约', name_s: '已预约' },
				{ cuIcon: 'card', name: '待确认', name_s: '已确认' },
				{ cuIcon: 'formfill', name: '待交易', name_s: '已交易' },
				{ cuIcon: 'presentfill', name: '待评价', name_s: '已评价' }
			],
			evaluation:'', //原评价
			newEvaluation:'', //新评价
			avatar: '',
			accountName:'高哥',
			reservation:{
				id: 10, //预约的 id
				price:20.2,// 出价
				count:10,
				note: '最好可以有个包装',
				createTime: '预约时间',
				state_enum: '预约的状态',
				evaluation_sell: 5, // 卖家的评价等级, 1-5
				evaluation_buy: 1, //买家的评价等级
			},
			commodity:{
				id:20,
				name:'',
				img:'',
				description:'描述',
				expectedPrice:10.2
			},
			reservation: {
				seller: '仔仔科技运营部',
				buyer: '小明',
				mold: true,
				price: '2280',
				title: '商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题',
				img: '/static/images/home/goods/1.png',
				expiredTime: '2020-10-09',
				description: '测试介绍内容',
				remainTime1: '14',
				id: '231231231',
				createTime: '2020-04-02 14:55:23',
				transactionTime2: '2020-04-03 14:55:23',
				transactionTime3: '2020-04-04 14:55:23',
				transactionTime4: '2020-04-05 14:55:23',
				transactionTime5: '2020-04-06 14:55:23',
				remainTime2: '30',
				totalPrice: '2280',
				buyerRegion: '信息学部'
			}
		}
	},
	computed:{
		avatarURL(){
			return this.avatar.length===0?'url(/static/avatar/default.png)':('url('+ this.avatar+')')
		},
		comImg(){
			return this.commodity.img.length===0?'url(/static/images/comDefault.png)':( 'url(' + this.commodity.img + ')' )
		}
	},
	onLoad(params) {
		this.getReservation(params.id)
	},
	onReady() {
		_tool.setBarColor(true)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	methods: {
		getReservation(id) {
			this.$api
				.getReservation(id)
				.then(({ data }) => {
					this.reservation = data
				})
				.catch(() => {
					console.log('获取预约详情失败')
				})
		},
		nextTap() {
			this.basics = this.basics == this.basicsList.length - 1 ? 0 : this.basics + 1
		},
		confirmReceipt() {
			uni.showModal({
				title: '收货提醒',
				content: '请务必确认已收到商品后再确认收货，让您提前确认收货的都是骗子、不走平台交易的都是骗子、让您私下打款的都是骗子',
				confirmText: '确认收货',
				confirmColor: '#0081ff',
				class: 'zaiui-modal',
				success: res => {
					if (res.confirm) {
						
					}
				}
			})
		},
		appraiseTap() {
			this.modalShow = true
		},
		cancelReservation() {},
		confirmReservation() {},
		cancelTransaction() {},
		confirmTransaction() {},
		completeTransaction() {}
	}
}
</script>

<style lang="scss">
/* #ifdef APP-PLUS */
@import '../../static/colorui/main.css';
@import '../../static/colorui/icon.css';
@import '../../static/zaiui/style/app.scss';
/* #endif */
@import '../../static/zaiui/style/order-details.scss';

.reservation-goods-info-view {
	position: relative;
	padding-left: 172.72rpx;
	height: 154.54rpx;
	.name {
		position: relative;
		height: 62rpx;
		width: 100%;
	}
	.introduce {
		position: relative;
		height: 58rpx;
		width: 100%;
	}
	.text-price {
		position: relative;
		bottom: 0rpx;
		height: 0rpx;
		width: 500%;
	}
}
</style>
