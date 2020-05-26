<template>
	<view>
		<!--标题栏-->
		<bar-title bgColor="bg-white" isBack><block slot="content">我的预约</block></bar-title>

		<!-- 预约的分类tab -->
		<view class="bg-white zaiui-title-tab-box" style="z-index: 888;margin-left: 10rpx;">
			<view class="flex flex-wrap">
				<view class="basis-l">
					<scroll-view scroll-x class="nav z" scroll-with-animation :scroll-left="headTab.scrollLeft">
						<block v-for="(item, index) in headTab.list" :key="index">
							<view class="cu-item" :class="index == tabCur ? 'select' : ''" @tap="tabSelect" :data-id="index">
								<view :class="index == tabCur ? 'text-black' : ''">{{ item }}</view>
								<view class="tab-dot bg-red" />
							</view>
						</block>
					</scroll-view>
				</view>
			</view>
		</view>

		<you-scroll ref="scroll" @onPullDown="onPullDown">
			<!--预约列表-->
			<view class="bg-white zaiui-goods-list-view" v-show="tabCur === 0">
				<reservationList :list_data="wait.list" @listTap="listTap"></reservationList>
			</view>

			<view class="bg-white zaiui-goods-list-view" v-show="tabCur === 1">
				<reservationList :list_data="on.list" @listTap="listTap"></reservationList>
			</view>

			<view class="bg-white zaiui-goods-list-view" v-show="tabCur === 2">
				<reservationList :list_data="finish.list" @listTap="listTap"></reservationList>
			</view>

			<view class="bg-white zaiui-goods-list-view" v-show="tabCur === 3">
				<reservationList :list_data="total.list" @listTap="listTap"></reservationList>
			</view>
			
			<!-- Loading Text -->
			<uni-load-more :status="loadStatus" class="margin-bottom"></uni-load-more>
			
			<!--占位底部距离-->
			<view class="cu-tabbar-height" />
		</you-scroll>

		<mpopup ref="mpopup" isdistance></mpopup>
	</view>
</template>

<script>
import barTitle from '@/components/basics/bar-title'
import _tool from '@/static/zaiui/util/tools.js' //工具函数
import _reservations_data from '@/static/zaiui/data/reservations.js'
import reservationList from '@/components/list/reservations-list.vue'
import youScroll from '@/components/you-scroll/you-scroll.vue'
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'
import {mapState} from 'vuex'
import handles from '../../utils/handles.js'

const reservationMap = tabCur => {
	switch (tabCur) {
		case 0:
			return 'wait' // 待确认
		case 1:
			return 'on' // 预约中
		case 2:
			return 'finish' // 已完成
		case 3:
			return 'total' // 全部
		default:
			console.log('map case default',tabCur)
	}
}
const iniReservation = ()=> ({
	pageIndex:1,
	pageSize:20,
	endTime:new Date().format('yyyy-MM-dd hh:mm'),
	finish: false,
	list:[]
})
export default {
	components: {
		barTitle,
		reservationList,
		youScroll,
		uniLoadMore
	},
	data() {
		return {
			onRequest:false,
			loadStatus:'more',
			headTab: { scrollLeft: 0, list: [] },
			tabCur: 0,
			wait: { list: [] },
			on: { list: [] },
			finish: { list: [] },
			total: { list: [] },
			reservations: []
		}
	},
	computed:{
		...mapState(['userId'])
	},
	onLoad(params) {
		this.headTab.list = ['待确认', '待交易', '已完成', '全部预约']
		// virtual data
		this.wait.list = _reservations_data.stage1List()
		this.on.list = _reservations_data.stage2List()
		this.finish.list = _reservations_data.stage3List()
		this.total.list = _reservations_data.totalList()
		
		// request data
		this.loadReservations() // 用户 id 和 预约类型
		
		console.log('vuex acc id',this.userId);
	},
	onReady() {
		_tool.setBarColor(true)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	onReachBottom(){
		this.setReachBottom()
	},
	methods: {
		listTap(id) {
			uni.navigateTo({
				url: '../../pages/detail/reservation?id=' + id
			})
		},
		async loadReservations() {
			if(this.onRequest) return
			this.onRequest = true
			this.loadStatus = 'loading'
			let curReservations = this[reservationMap(this.tabCur)]
			let pagination = {
				pageIndex: curReservations.pageIndex,
				pageSize: curReservations.pageSize,
				endTime: curReservations.endTime,
				type:this.tabCur
			}
			
			await this.$api
				.getNotices(this.userId, pagination)
				.then(({ data }) => {
					// 一些没有判断 success,根据后台的 json 来决定要不要加这个判断
					curReservations.list.push(...data.list)
					// 取完了数据
					if (data.pageIndex - 1 >= data.pageCount) curReservations.finish = true
				})
				.catch(() =>{
					uni.showToast({
						title: '通告获取失败',
						icon: 'none'
					})
					this.err()
				})

			console.log('!同步运行到此，设置 loadingText')
			if (curReservations.finish) this.loadStatus = 'noMore'
			else this.loadStatus = 'more'
			this.onRequest = false
		},
		async onPullDown(done) {
			this[reservationMap(this.tabCur)] = iniReservation()
			await this.loadReservations()
			done()
		},
		setReachBottom() {
			console.log('reservations 触底了')
			if (!this[reservationMap(this.tabCur)].finish) this.loadReservations()
		},
		tabSelect(e) {
			let current = parseInt(e.currentTarget.dataset.id)
			if (this.tabCur !== current && this[reservationMap(current)].list.length === 0) {
				this[reservationMap(current)] = iniReservation()
				this.tabCur = current
				this.loadReservations()
			} else this.tabCur = current
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
	}
}
</script>

<style lang="scss">
/* #ifdef APP-PLUS */
@import '../../static/colorui/main.css';
@import '../../static/colorui/icon.css';
@import '../../static/zaiui/style/app.scss';
/* #endif */
@import '../../static/zaiui/style/footmark.scss';
</style>

<style lang="scss" scoped>
.zaiui-title-tab-box {
	position: fixed;
	width: 100%;
	top: 40px;
	z-index: 999999;
	padding: calc(var(--status-bar-height) + 9.09rpx) 27.27rpx 9.09rpx 9.09rpx;
	.flex {
		.basis-l {
			flex-basis: 70%;
		}
		.basis-s {
			flex-basis: 30%;
		}
	}
}
</style>
