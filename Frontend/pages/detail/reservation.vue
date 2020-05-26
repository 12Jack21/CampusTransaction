<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor="bg-white" isBack><block slot="content">详情</block></bar-title>

		<!--步骤条区域-->
		<view class="bg-white padding solid-top">
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
			<view class="text-sm text-center margin-top">
				<view class="text-black">{{ tips[basics] }}</view>
				<view class="text-black" v-if="basics == 0"><text>若预约未被确认，其将自动取消。</text></view>
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
								<text>{{ reservation.accountName }}</text>
								<text>{{ userName }}</text>
							</view>
							<view class="text-gray text-sm flex">
								<view class="text-cut">{{ reservation.detailedAddress }}</view>
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
					<view class="text-black text-bold text-lg title-view">{{ reservation.accountName }}</view>
				</view>
				<view class="goods-list-view">
					<view class="cu-avatar radius" :style="{ backgroundImage: comImg }" />
					<view class="reservation-goods-info-view">
						<view class="text-black text-cut name">{{ reservation.commodity.name }}</view>
						<view class="text-gray text-sm text-cut introduce">{{ reservation.commodity.description }}</view>
						<view class="text-price text-red text-lg">{{ reservation.commodity.expectedPrice }}</view>
					</view>
				</view>
			</view>
		</view>

		<!--商品金额信息-->
		<view class="bg-white zaiui-card-box">
			<view class="zaiui-card-view zaiui-price-view">
				<view class="text-black title-view">
					<view class="title">商品总额</view>
					<view class="text-right">
						<text class="text-price">{{ reservation.commodity.expectedPrice }}</text>
					</view>
				</view>

				<view class="text-black text-bold title-right-view" style="font-size: 1.1em;">
						应付款：					
						<text class="text-price">{{ reservation.price }}</text>
				</view>
			</view>
		</view>

		<!--预约信息-->
		<view class="bg-white zaiui-card-box">
			<view class="zaiui-card-view zaiui-order-view">
				<view class="text-lg text-bold text-black">预约信息</view>
				<view class="solid-line"></view>
				<view class="text-black title-view">
					<view class="title">预约编号</view>
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
			</view>
		</view>

		<view class="bg-white zaiui-card-hight-box" />

		<!-- 占位底部 -->
		<view class="foot-hight-view" />

		<!-- 底部按钮组 -->
		<view class="bg-white zaiui-footer-fixed zaiui-foot-padding-bottom">
			<button class="cu-btn bg-orange sm" @tap="nextTap">测试下一步</button>
			<button class="cu-btn line-black radius" @click="cancel" v-if="basics < 2">{{ btn1[basics] }}</button>
			<button class="cu-btn bg-red" @click="confirm" v-if="basics < 3&& basics>0">{{ btn2[basics] }}</button>
		</view>

		<!--弹出框-->
		<view class="cu-modal bottom-modal zaiui-bottom-modal-box" :class="modalShow ? 'show' : ''">
			<view class="cu-dialog bg-white updateDialog">
				<view class="text-black text-center margin-tb text-lg title-bar">
					<text>评价</text>
					<text class="cuIcon-close close-icon" @tap="modalShow = false"></text>
				</view>

				<!-- 模态框内容区域 -->
				<view class="zaiui-modal-content">
					<!-- 修改评价 -->
					<view class="zaiui-view-box select">
						<!-- 新评价 -->
						<view class="evaluationItem">
							<view class="flex new">
								<view class="text-black text-lg text-bold">新评级:</view>
								<uni-rate ></uni-rate>
							</view>
							<view class="flex old text-lg">
								<view class="text-gray ">原评级:</view>
								<uni-rate :value="oriEvaluation" disabled=""></uni-rate>
							</view>
						</view>
						<!-- end -->
					</view>
					<!-- 保存更新 -->
					<view class="zaiui-footer-fixed" style="z-index: 10;">
						<view class="flex flex-direction"><button class="cu-btn bg-red" style="font-size: 1.4em;padding: 10rpx 0;" @tap="confirm">保存</button></view>
					</view>
				</view>
			
			</view>
		</view>
	</view>
</template>

<script>
import barTitle from '@/components/basics/bar-title'
import uniRate from '@/components/uni-rate/uni-rate.vue'
import _tool from '@/static/zaiui/util/tools.js' //工具函数
import {mapState} from 'vuex'

const TIPS = ['预约成功，待卖家确认', '确认成功，待双方进行交易', '交易完成,可以更新一次评价', '评价完成']
const BTN1 = ['取消预约', '取消交易']
const BTN2 = ['', '确认交易', '发表评价']
export default {
	components: {
		barTitle,uniRate
	},
	data() {
		return {
			modalShow: false, //评价
			basics: 0,
			basicsList: [
				{ cuIcon: 'cartfill', name: '未预约', name_s: '已预约' },
				{ cuIcon: 'card', name: '待确认', name_s: '已确认' },
				{ cuIcon: 'formfill', name: '待交易', name_s: '已交易' },
				{ cuIcon: 'presentfill', name: '待评价', name_s: '已评价' }
			],
			tips: TIPS,
			btn1: BTN1,
			btn2: BTN2,
			newEvaluation: '', //新评价
			reservation: {
				id: 10, //预约的 id
				price: 20.2, // 出价
				count: 10,
				note: '最好可以有个包装',
				createTime: '预约时间',
				state_enum: 'WAITING', // 'FAIL','CANCELLED','WAITING','VALIDATE','FINISHED','FAILWAITING'
				evaluation_sell: 5, // 卖家的评价等级, 1-5
				evaluation_buy: 1, //买家的评价等级
				commodity: {
					id: 1,
					name: '物品名',
					img: '',
					description: '描述:这是一个怎样的物品呢',
					expectedPrice: 10.2
				},
				accountName: '物品发布者', 
				avatar: '',
				detailedAddress:'信息学部二食堂'
			}
		}
	},
	computed: {
		...mapState(['userName']),
		avatarURL() {
			return this.reservation.avatar.length === 0 ? 'url(/static/images/avatar/default.png)' :('url('+ this.reservation.avatar + ')')
		},
		comImg() {
			return this.reservation.commodity.img.length === 0 ? 'url(/static/images/comDefault.png)' : ('url('+this.reservation.commodity.img + ')')
		},
		oriEvaluation(){
			// 判断是买方还是卖方
			
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
		confirm(){ // 按钮组2
			if(this.basics===0){ //预约未确认
				
			}
			else if(this.basics===1){ //交易未确认		
				uni.showModal({
					title: '交易确认',
					content: '请务必确认已收到商品后再确认交易',
					confirmText: '确认',
					confirmColor: '#0081ff',
					class: 'zaiui-modal',
					success: res => {
						if (res.confirm) {
						}
					}
				})
			}
			else if(this.basics===2){ // 未更新评价
				this.modalShow = true
			}
		},
		cancel(){ //按钮组1
			
		},
		nextTap() { //debug: 测试下一步
			this.basics = this.basics == this.basicsList.length - 1 ? 0 : this.basics + 1
		},
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

$space:20rpx;
$bottom_height:60rpx;

.updateDialog{
	border-radius: 40rpx 40rpx 0 0 !important;
	.title-bar{
		position: relative;
		.cuIcon-close{
			position: absolute;
			right: $space;
		}
	}
}
.evaluationItem{
	padding: 20rpx 0 60rpx 0;
	margin-bottom: $bottom_height;
	display: flex;
	flex-direction: column;
	// justify-content: center;
	align-items: center;
	.new{
		align-items: center;
		justify-content: center;
		.uni-rate{
			padding: 10rpx;
			margin-left: $space;
			height: 2em;
		}
	}
	.old{
		margin: $space * 2 0;
		justify-self: flex-start;
		align-items: flex-end;
		.uni-rate{
			margin-left: $space;
		}
	}
}
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
