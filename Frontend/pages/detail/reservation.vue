<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor="bg-white" isBack><block slot="content">订单详情</block></bar-title>

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
					<text class="text-red">{{ reservation.remainTime1 }}</text>
					<text>分后未支付，订单将自动关闭。</text>
				</view>
			</view>
			<view class="text-sm text-center margin-top" v-if="basics == 1"><view class="text-black">沟通成功，待卖家发货.</view></view>
			<view class="text-sm text-center margin-top" v-if="basics == 2"><view class="text-black">已发货，正在等待收货中.</view></view>
			<view class="text-sm text-center margin-top" v-if="basics == 3">
				<view class="text-black">双方已交易，请双方确认无误后点击交易完成。</view>
				<view class="text-black">
					<text class="text-red">{{ reservation.remainTime2 }}</text>
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
								<text>收货人：</text>
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
					<view class="cu-avatar round sm" :style="[{ backgroundImage: 'url(' + avatar + ')' }]" />
					<view class="text-black text-bold text-lg title-view">{{ reservation.seller }}</view>
				</view>
				<view class="goods-list-view">
					<view class="cu-avatar radius" :style="[{ backgroundImage: 'url(' + bg_img + ')' }]" />
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

				<view class="solid-line"></view>

				<view class="text-center text-black">联系客服</view>
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
						<text class="margin-right-xs">{{ reservation.reservationId }}</text>
						<button class="cu-btn sm line-black">复制</button>
					</view>
				</view>

				<view class="text-black title-view">
					<view class="title">预约时间</view>
					<view class="text-right">
						<text>{{ reservation.transactionTime1 }}</text>
					</view>
				</view>
				<view class="text-black title-view" v-if="basics > 0">
					<view class="title">沟通时间</view>
					<view class="text-right">
						<text>{{ reservation.transactionTime2 }}</text>
					</view>
				</view>
				<view class="text-black title-view" v-if="basics > 1">
					<view class="title">发货时间</view>
					<view class="text-right">
						<text>{{ reservation.transactionTime3 }}</text>
					</view>
				</view>
				<view class="text-black title-view" v-if="basics > 2">
					<view class="title">收货时间</view>
					<view class="text-right">
						<text>{{ reservation.transactionTime4 }}</text>
					</view>
				</view>
				<view class="text-black title-view" v-if="basics > 3">
					<view class="title">完成时间</view>
					<view class="text-right">
						<text>{{ reservation.transactionTime5 }}</text>
					</view>
				</view>
			</view>
		</view>

		<view class="bg-white zaiui-card-box"></view>
		<view class="bg-white zaiui-card-box"></view>

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
			<button class="cu-btn line-black radius" @click="cancelTransaction()">申请取消</button>
			<button class="cu-btn bg-red" @click="remindTransaction()">提醒交易</button>
		</view>

		<view class="bg-white zaiui-footer-fixed zaiui-foot-padding-bottom" v-if="basics == 2">
			<button class="cu-btn bg-orange sm" @tap="nextTap">测试下一步</button>
			<button class="cu-btn line-black radius" @click="cancelTransaction()">申请取消</button>
		</view>

		<view class="bg-white zaiui-footer-fixed zaiui-foot-padding-bottom" v-if="basics == 3">
			<button class="cu-btn bg-orange sm" @tap="nextTap">测试下一步</button>
			<button class="cu-btn line-black radius" @click="cancelTransaction()">申请取消</button>
			<button class="cu-btn bg-red" @tap="confirmReceipt" @click="confirmTransaction()">确认交易</button>
		</view>

		<view class="bg-white zaiui-footer-fixed zaiui-foot-padding-bottom" v-if="basics == 4">
			<button class="cu-btn bg-orange sm" @tap="nextTap">测试第一步</button>
			<button class="cu-btn line-black radius" @tap="appraiseTap">发表评价</button>
			<button class="cu-btn bg-red" @click="completeTransaction()">交易完成</button>
		</view>

		<!--弹出框-->
		<view class="cu-modal bottom-modal" :class="bottomModal ? 'show' : ''">
			<view class="cu-dialog">
				<view class="cu-bar bg-white solid-bottom">
					<view class="text-black text-center title">手机安全验证</view>
					<text class="text-gray cuIcon-close close" @tap="closeModalTap"></text>
				</view>
				<view class="bg-white modal-view">
					<view class="content">
						<view class="tel-btn-view">
							<view class="text-black tel-view">验证码已发至：138****8000</view>
							<button class="cu-btn sm" @tap="getCodeKey" v-if="btnKey">获取</button>
							<button class="cu-btn sm" v-else>56s</button>
						</view>
						<view class="text-sm text-black margin-tb">
							<text>确认收货后，交易将结束。您之前付款到平台的</text>
							<text class="text-red">￥1.00</text>
							<text>，将会打给卖家。</text>
						</view>
						<view class="text-sm text-gray margin-bottom">提醒:确认收货后钱款将脱离平台,届时平台无法保障您的钱款安全,请务必谨慎点击确认收货，谨防诈骗。</view>
						<view class="code-view">
							<text class="code" v-if="!codeKey[0]">—</text>
							<text class="code" v-else>{{ codeKey[0] }}</text>

							<text class="code" v-if="!codeKey[1]">—</text>
							<text class="code" v-else>{{ codeKey[1] }}</text>

							<text class="code" v-if="!codeKey[2]">—</text>
							<text class="code" v-else>{{ codeKey[2] }}</text>

							<text class="code" v-if="!codeKey[3]">—</text>
							<text class="code" v-else>{{ codeKey[3] }}</text>
						</view>
					</view>

					<!--数字键盘-->
					<view class="num-lock-view">
						<view class="cu-list grid col-3 solid-top">
							<block v-for="(item, index) in 9" :key="index">
								<view class="cu-item" @tap="codeKeyTap(item)">
									<text class="text-black num">{{ item }}</text>
								</view>
							</block>
							<view class="cu-item"><text class="text-black num"></text></view>
							<view class="cu-item" @tap="codeKeyTap(0)"><text class="text-black num">0</text></view>
							<view class="cu-item" @tap="codeKeyDelTap"><text class="cuIcon-close close"></text></view>
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
			basics: 0,
			bg_img: '/static/images/home/goods/1.png',
			avatar: '/static/images/avatar/1.jpg',
			cancel: false,
			basicsList: [
				{ cuIcon: 'cartfill', name: '未预约', name_s: '已预约' },
				{ cuIcon: 'card', name: '待沟通', name_s: '已沟通' },
				{ cuIcon: 'deliver_fill', name: '待发货', name_s: '已发货' },
				{ cuIcon: 'formfill', name: '待收货', name_s: '已收货' },
				{ cuIcon: 'presentfill', name: '待完成', name_s: '已完成' }
			],
			bottomModal: false,
			codeKey: [],
			btnKey: true,
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
				reservationId: '231231231',
				transactionTime1: '2020-04-02 14:55:23',
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
						this.bottomModal = true
					}
				}
			})
		},
		closeModalTap() {
			this.bottomModal = false
		},
		codeKeyTap(index) {
			if (this.codeKey.length < 4) {
				this.codeKey.push(index)
			}
		},
		codeKeyDelTap() {
			this.codeKey.pop()
		},
		getCodeKey() {
			this.btnKey = false
		},
		appraiseTap() {
			uni.navigateTo({
				url: '../../pages/appraise/appraise'
			})
		},
		cancelReservation() {},
		confirmReservation() {},
		remindTransaction() {},
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
