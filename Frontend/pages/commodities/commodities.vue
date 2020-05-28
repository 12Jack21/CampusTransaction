<template>
	<view>
			
		<!--标题栏-->
		<bar-title bgColor="bg-white" isBack><block slot="content">我的物品</block></bar-title>

		<!-- 物品的分类tab -->
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

		<view class="cu-tabbar-height" />
		
		<you-scroll ref="scroll" @onPullDown="onPullDown">
			<!--预约列表-->
			<view class="bg-white zaiui-goods-list-view" v-show="tabCur === 0">
				<commodityList :list_data="release.list" @listTap="listTap"></commodityList>
			</view>

			<view class="bg-white zaiui-goods-list-view" v-show="tabCur === 1">
				<commodityList :list_data="receive.list" @listTap="listTap"></commodityList>
			</view>

			<view class="bg-white zaiui-goods-list-view" v-show="tabCur === 2">
				<commodityList :list_data="total.list" @listTap="listTap"></commodityList>
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
import _commodities_data from '@/static/zaiui/data/home.js'
import commodityList from '@/components/list/commodity-list.vue'
import youScroll from '@/components/you-scroll/you-scroll.vue'
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'
import {mapState} from 'vuex'
import handles from '../../utils/handles.js'

const commodityMap = tabCur => {
	switch (tabCur) {
		case 0:
			return 'release' // 发布的
		case 1:
			return 'receive' // 买入的
		case 2:
			return 'total' // 全部
		default:
			console.log('map case default',tabCur)
	}
}
const iniCommodity = ()=> ({
	pageIndex:1,
	pageSize:20,
	endTime:new Date().format('yyyy-MM-dd hh:mm'),
	finish: false,
	list:[]
})
export default {
	components: {
		barTitle,
		commodityList,
		youScroll,
		uniLoadMore
	},
	data() {
		return {
			onRequest:false,
			loadStatus:'more',
			headTab: { scrollLeft: 0, list: [] },
			tabCur: 0,
			release: { list: [] },
			receive: { list: [] },
			total: { list: [] },
			reservations: []
		}
	},
	computed:{
		...mapState(['userId'])
	},
	onLoad(params) {
		this.headTab.list = ['我发布的','我买入的','全部']
		// virtual data
		this.release.list = _commodities_data.goodsList()
		this.receive.list = _commodities_data.goodsList()
		this.total.list = _commodities_data.goodsList()
		
		// request data
		this.loadCommoditiess() // 用户 id 和 预约类型
		
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
			let isSell = false // 是买家
			uni.navigateTo({
				url: `../../pages/detail/commodity?id=${id}`
			})
		},
		async loadCommoditiess() {
			if(this.onRequest) return
			this.onRequest = true
			this.loadStatus = 'loading'
			let curCommodities = this[commodityMap(this.tabCur)]
			let pagination = {
				pageIndex: curCommodities.pageIndex,
				pageSize: curCommodities.pageSize,
				endTime: curCommodities.endTime,
				type:this.tabCur
			}
			
			await this.$api
				.getCommoditiesByAcc(this.userId, pagination)
				.then(({ data }) => {
					// 一些没有判断 success,根据后台的 json 来决定要不要加这个判断
					curCommodities.list.push(...data.list)
					// 取完了数据
					if (data.pageIndex - 1 >= data.pageCount) curCommodities.finish = true
				})
				.catch(() =>{
					uni.showToast({
						title: '通告获取失败',
						icon: 'none'
					})
					this.err()
				})

			console.log('!同步运行到此，设置 loadingText')
			if (curCommodities.finish) this.loadStatus = 'noMore'
			else this.loadStatus = 'more'
			this.onRequest = false
		},
		async onPullDown(done) {
			this[commodityMap(this.tabCur)] = iniCommodity()
			await this.loadCommoditiess()
			done()
		},
		setReachBottom() {
			console.log('reservations 触底了')
			if (!this[commodityMap(this.tabCur)].finish) this.loadCommoditiess()
		},
		tabSelect(e) {
			let current = parseInt(e.currentTarget.dataset.id)
			if (this.tabCur !== current && this[commodityMap(current)].list.length === 0) {
				this[commodityMap(current)] = iniCommodity()
				this.tabCur = current
				this.loadCommoditiess()
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
.bar-height{
	position: fixed;
	top: var(--status-bar-height);
}
.zaiui-title-tab-box {
	position: fixed;
	width: 100%;
	// top: 40px;
	z-index: 9999;
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
