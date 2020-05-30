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
		<view class="bg-white padding solid-top text-center zaiui-status-img-view" v-if="basics == 2 && reservation.stateEnumStr!='CANCELLED'">
			<view class="are-img-view" @tap="cancel = true"><image class="are-img" src="/static/zaiui/img/are.png" mode="widthFix" /></view>
			<view class="text-sm text-black">交易成功，可更新一次评价</view>
		</view>

		<!--状态图标-->
		<view class="bg-white padding solid-top text-center zaiui-status-img-view" v-if="reservation.stateEnumStr=='CANCELLED'">
			<view class="are-img-view" @tap="cancel = false"><image class="are-img" src="/static/zaiui/img/arg.png" mode="widthFix" /></view>
			<view class="text-sm text-black text-bold">订单已取消</view>
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
								<text style="margin-left: 20rpx;">{{ reservation.buyerName }}</text>
								<text class="text-gray" style="font-size: 0.6em;">(买方)</text>
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
					<view class="cu-avatar round sm " :style="[{ backgroundImage: avatarURL }]"  @tap="comTap"/>
					<view class="text-black text-bold text-lg title-view">{{ reservation.accountName }}</view>
				</view>
				<view class="goods-list-view">
					<view class="cu-avatar radius" :style="{ backgroundImage: comImg }" />
					<view class="reservation-goods-info-view">
						<view class="text-black text-cut name" @tap="comTap">{{ reservation.commodity.name }}</view>
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
					<view class="title">物品期待价格</view>
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
		
		<!--评价信息-->
		<view class="bg-white zaiui-card-box" v-if="reservation.stateEnumStr=='FINISHED'">
			<view class="zaiui-card-view zaiui-order-view">
				<view class="text-lg text-bold text-black">评价信息</view>
				<view class="solid-line"></view>
				<view class="text-black title-view">
					<view class="title">卖方评价</view>
					<view class="text-right" style="height: 40rpx;">
						<uni-rate :value="reservation.evaluationSell" disabled></uni-rate>
					</view>
				</view>
				<view style="height: 30rpx;"/>
				<view class="text-black title-view">
					<view class="title">买方评价</view>
					<view class="text-right" style="height: 40rpx;">
						<uni-rate :value="reservation.evaluationBuy"  disabled ></uni-rate>
					</view>
				</view>
			</view>
		</view>

		<view class="bg-white zaiui-card-hight-box" />

		<!-- 占位底部 -->
		<view class="foot-hight-view" />

		<!-- 底部按钮组 -->
		<view class="bg-white zaiui-footer-fixed zaiui-foot-padding-bottom">
			<!-- <button class="cu-btn bg-orange sm" @tap="nextTap">测试下一步</button> -->
			<button class="cu-btn line-black radius" @click="cancel" v-if="basics < 2" 
				:disabled="cancelDisable">
				{{ btn1[basics] }}
			</button>
			<button class="cu-btn bg-red" @click="confirm" v-if="basics < 3&& basics>0" 
			:disabled="confirmDisable">{{ btn2[basics] }}</button>
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
								<uni-rate @change="rateChange"></uni-rate>
							</view>
							<view class="flex old text-lg">
								<view class="text-gray ">原评级:</view>
								<uni-rate :value="oriEvaluation" disabled></uni-rate>
							</view>
							<loading v-if="onEvaluation">更新中</loading>
						</view>
						<!-- end -->
					</view>
					<!-- 保存更新 -->
					<view class="zaiui-footer-fixed" style="z-index: 10;">
						<view class="flex flex-direction"><button class="cu-btn bg-red" style="font-size: 1.4em;padding: 10rpx 0;" @tap="confirmEvaluation">保存</button></view>
					</view>
				</view>
			
			</view>
		</view>
		<mpopup ref="mpopup"></mpopup>
	</view>
</template>

<script>
import barTitle from '@/components/basics/bar-title'
import uniRate from '@/components/uni-rate/uni-rate.vue'
import _tool from '@/static/zaiui/util/tools.js' //工具函数
import {mapState} from 'vuex'

const TIPS = ['预约成功，待卖家确认', '确认成功，待双方进行交易', '']
const BTN1 = ['取消预约', '取消交易']
const BTN2 = ['', '确认交易', '更新评价']
export default {
	components: {
		barTitle,uniRate
	},
	data() {
		return {
			onEvaluation: false, // 请求中
			isSell: false, //是否为卖方
			modalShow: false, //评价
			basics: 0,
			valueSync:5,
			basicsList: [
				{ cuIcon: 'cartfill', name: '未预约', name_s: '已预约' },
				{ cuIcon: 'card', name: '待确认', name_s: '已确认' },
				{ cuIcon: 'formfill', name: '待交易', name_s: '已交易' }
			],
			tips: TIPS,
			btn1: BTN1,
			btn2: BTN2,
			newEvaluation: 1, //新评价
			reservation: {
				id: 10, //预约的 id
				price: 20.2, // 出价
				count: 10,
				note: '最好可以有个包装',
				createTime: '2020-10-08 10:06',
				stateEnumStr: 'CANCELLED', // 'FAIL','CANCELLED','WAITING','VALIDATE','FINISHED','FAILWAITING'
				evaluationSell: 5, // 卖家的评价等级, 1-5
				evaluationBuy: 5, //买家的评价等级
				commodity: {
					id: 1,
					name: '物品名',
					img: '',
					description: '描述:这是一个怎样的物品呢',
					expectedPrice: 10.2
				},
				buyerId:12, // 可以拿来判断是否为卖家
				buyerName:'预约者名字', // add to api
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
			return this.isSell? this.reservation.evaluationSell:this.reservation.evaluationBuy
		},
		cancelDisable(){ // 取消按钮 disable
			if(this.basics===0)
				return this.isSell || this.reservation.stateEnumStr!=='WAITING'
			else if(this.basics === 1)
				return !this.isSell || this.reservation.stateEnumStr == 'CANCELLED'
			return false
		},
		confirmDisable(){ // 确认按钮 disable
			return this.basics===1 && (!this.isSell || this.reservation.stateEnumStr!=='VALIDATE')
		}
	},
	watch:{
		value:function(now,old,){
		    if(now != old){
		      this.valueSync = Number(now);
		    }
		  }
	},
	onLoad(params) {
		console.log('reservation onload,params',params);
		this.isSell =  params.isSell ==  'true'
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
		initBasic(state){
			switch(state){
				case 'WAITING':
					this.basics = 0
					break
				case 'CANCELLED':
					this.basics = 0
					break
				case 'VALIDATE': // 已确认
					this.basics = 1
					break
				case 'FINISHED':
					this.basics = 2
					break
				case 'FAILED':
					this.basics = 1
					break
			}
		},
		getReservation(id) {
			this.$api
				.getReservation(id)
				.then(({ data }) => {
					console.log('reservation data from request:',data.data);
					data = data.data
					this.reservation = Object.assign({},this.reservation,data)
					// 根据状态判断 界面的展示值
					let state = data.stateEnumStr // 'FAIL','CANCELLED','WAITING','VALIDATE','FINISHED','FAILWAITING'
					this.initBasic(state)
				})
				.catch(() => {
					console.log('获取预约详情时网络异常')
					this.err()
				})
		},
		rateChange(e){ // 新评价
			this.newEvaluation = e.value
		},
		confirm(){ // 按钮组2
			if(this.basics===0){ //预约未确认,不用操作		
			}
			else if(this.basics===1){ //交易未确认		
				uni.showModal({
					title: '交易确认',
					content: '请务必确认双方已成功完成交易后再确认',
					confirmText: '确认',
					confirmColor: '#0081ff',
					class: 'zaiui-modal',
					success: res => {
						if (res.confirm) {
							this.$api.successReservation(this.reservation.id)
								.then(({data})=>{
									if(data.success){ // 确认交易成功
										this.tip(0,'确认交易成功')
										this.basics += 1 //更新状态
										this.reservation.stateEnumStr = 'FINISHED'
									}
									else
										this.tip(1,'确认交易失败')
								})
								.catch(()=>this.err())
						}
					}
				})
			}
			else if(this.basics===2){ // 未更新评价
				this.modalShow = true
			}
		},
		cancel(){ //按钮组1
			if(this.basics===0){ // 取消预约
				this.$api.cancelReservation(this.reservation.id)
					.then(({data})=>{
						if(data.success){ // 取消预约成功
							this.tip(0,'取消预约成功')
							// this.basics += 1 //更新状态
							this.reservation.stateEnumStr = 'CANCELLED'
						}
						else
							this.tip(1,'取消预约失败')
					})
					.catch(()=>this.err())
			}else if(this.basics===1){ //取消交易
				this.$api.failReservation(this.reservation.id)
					.then(({data})=>{
						if(data.success){ // 取消预约成功
							this.tip(0,'取消交易成功')
							// this.basics += 1 //更新状态
							this.reservation.stateEnumStr = 'FAILED'
						}
						else
							this.tip(1,'取消交易失败')
					})
					.catch(()=>this.err())
			}
		},
	  async	confirmEvaluation(){
			if(this.newEvaluation == this.oriEvaluation || this.onEvaluation) return
			this.onEvaluation = true
			let data = {
				evaluationBuy: this.isSell? this.reservation.evaluationBuy:this.newEvaluation,
				evaluationSell: this.isSell? this.newEvaluation: this.reservation.evaluationSell
			}
			await this.$api.updateEvaluation(this.reservation.id,data)
				.then(({data})=>{
					if(data.success){ // 取消预约成功
							this.tip(0,'更新评价成功')
							// 更新评价
							if(this.isSell)	this.reservation.evaluationSell = this.newEvaluation
							else this.reservation.evaluationBuy = this.newEvaluation
						}
					else
						this.tip(1,'更新评价失败')
				})
				.catch(()=>this.err())
				
				this.onEvaluation = false
				this.modalShow = false
		},
		comTap(){
			uni.navigateTo({
				url: './commodity?id=' + this.reservation.commodity.id
			});
		},
		nextTap() { //debug: 测试下一步
			this.basics = this.basics == this.basicsList.length - 1 ? 0 : this.basics + 1
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
