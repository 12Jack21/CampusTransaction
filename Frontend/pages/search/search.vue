<template>
	<view>
		<!-- 搜索框 -->
		<view class="zaiui-bar-search-title-box"> 
			<view class="flex flex-direction top-module" >
				<!-- 小程序端的标题,占据胶囊的高度，但要调整 fixed 的top -->
				<!-- #ifdef MP -->
				<view class="text-center text-black capsule-height" ></view>
				<!-- #endif -->
				<view class="cu-bar search bg-white  no-shadow">
					<view class="action" @tap="BackPage"><text class="cuIcon-back" /></view>
					<view class="search-form round">
						<text class="cuIcon-search" />
						<input @input="searchInput" :value="searchKey" @focus="searchView = true" :adjust-position="false" type="text" :placeholder="search_ph" confirm-type="search" />
						<text class="cuIcon-close close-icon" v-if="search_close" @tap="closeInput" />
					</view>
					<view class="action" @tap="searchTap"><text class="text-red">搜索</text></view>
				</view>
				<HMfilterDropdown
					v-if="!searchView"
					class=""
					:filterData="filterData"
					:updateMenuName="true"
					:defaultSelected="filterDropdownValue"
					@confirm="confirmFilter"
					dataFormat="Object"
				></HMfilterDropdown>
			</view>
			<!-- end -->
			
			<!-- 顶部占位的 -->
			<!-- #ifdef MP -->
			<view class="mp-seat-height"></view>
			<!-- #endif -->
			<!-- #ifndef MP -->
			<view class="zaiui-seat-height" />
			<!-- #endif -->
			<view v-if="!searchView" class="zaiui-seat-height" />
		</view>
		<!-- end -->

		<!-- 筛选界面 -->
		<!-- 		<HMfilterDropdown
			v-if="!searchView"
			class="filterList"
			:filterData="filterData"
			:updateMenuName="true"
			:defaultSelected="filterDropdownValue"
			@confirm="confirmFilter"
			dataFormat="Object"
		></HMfilterDropdown> -->

		<!-- 搜索界面 -->
		<view v-if="searchView">
			<!--搜索面板区域-->
			<view class="padding zaiui-search-list-view" v-if="!deleteView">
				<!--搜索记录-->
				<view class="search-list-view">
					<view class="search-bar-view">
						<text class="text-black">历史搜索</text>
						<text class="cuIcon-delete text-gray icon-right" @tap="deleteView = true" />
					</view>
					<view class="btn-view">
						<button class="cu-btn round" v-for="(history, index) in histories" :key="index" @tap="searchTap(history)">{{ history }}</button>
					</view>
				</view>

				<!--推荐搜索-->
				<view class="search-list-view">
					<view class="search-bar-view"><text class="text-black">推荐搜索</text></view>
					<view class="btn-view">
						<button class="cu-btn round" v-for="(r, index) in recommendations" :key="index" @tap="searchTap(r)">{{ r }}</button>
					</view>
				</view>
			</view>

			<!--处理历史记录-->
			<view class="padding zaiui-search-list-view" v-if="deleteView">
				<!--搜索记录-->
				<view class="search-list-view">
					<view class="search-bar-view">
						<text class="text-black">历史搜索</text>
						<view class="text-sm text-right">
							<text class="text-yellow" @tap="delAllHistory">全部删除</text>
							<text class="text-gray" @tap="cancelDel">取消</text>
							<text class="text-red" @tap="finishDel">完成</text>
						</view>
					</view>
					<view class="btn-view">
						<button class="cu-btn round" v-for="(history, index) in histories" :key="index">
							<text>{{ history }}</text>
							<view class="cuIcon-roundclosefill close-icon" @tap="histories.splice(index, 1)"></view>
						</button>
					</view>
				</view>
			</view>
		</view>
		<!-- end -->

		<!-- 局部下拉刷新范围 -->
		<you-scroll ref="scroll" @onPullDown="onPullDown" v-else>
			<!-- 搜索结果 -->
			<view class="zaiui-goods-list-box">
				<view class="flex flex-wrap ">
					<!--商品列表-->
					<goods-list :list_data="leftGoods" @listTap="goodsListTap" style="width: 49%;padding-right: 1%;" />
					<goods-list :list_data="rightGoods" @listTap="goodsListTap" style="width: 49%;padding-right: 1%;" />
				</view>
			</view>
			<!-- end -->

			<!-- Loading Text -->
			<uni-load-more v-show="!searchView" :status="loadStatus" class="margin-bottom"></uni-load-more>
		</you-scroll>
	</view>
</template>

<script>
import _tool from '@/static/zaiui/util/tools.js' //工具函数
import goodsList from '@/components/list/goods-list.vue'
import HMfilterDropdown from '@/components/HM-filterDropdown/HM-filterDropdown.vue'
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'
import youScroll from '@/components/you-scroll/you-scroll.vue'
import { mapState } from 'vuex'
import handles from '@/utils/handles.js'

import _home_data from '@/static/zaiui/data/home.js' //虚拟数据
import filter_data from '../../static/data/filters.js'

const iniPagination = () => {
	return {
		pageIndex: 1,
		pageSize: 15,
		endtime: new Date().format('yyyy-MM-dd hh:mm'),
		finish: false
	}
}
const searchHistories = () => ['耳机', '大英课本', '四六级试卷', '电动车'] // virtual
const conditionMap = (sort, outdated, price) => {
	let sortVal,
		oVal = -1,
		highPrice = -1,
		lowPrice = -1
	switch (sort) {
		case 'time':
			sortVal = 4
			break
		case 'price':
			sortVal = 5
			break
		case 'outdatedTime':
			sortVal = 6
			break
		case 'evaluation':
			sortVal = 7
			break
	}
	// console.log('outdated',outdated);
	switch (outdated) {
		case '1天内':
			oVal = 1
			break
		case '3天内':
			oVal = 3
			break
		case '1周内':
			oVal = 7
			break
		case '1月内':
			oVal = 30
			break
		default:
			console.log('default outdated')
	}

	// 单价格区间
	price = price[0]
	switch (price) {
		case '50以下':
			highPrice = 50
			lowPrice = 0
			break
		case '50-100':
			highPrice = 100
			lowPrice = 50
			break
		case '100-300':
			highPrice = 300
			lowPrice = 100
			break
		case '300以上':
			lowPrice = 300
			break
		default:
			console.log('default price', price)
	}
	return {
		outdated: oVal,
		sort: sortVal,
		highPrice,
		lowPrice
	}
}

export default {
	components: {
		goodsList,
		HMfilterDropdown,
		uniLoadMore,
		youScroll
	},
	data() {
		return {
			onRequest: false,
			search_close: false,
			search_ph: '高数',
			searchKey: '',
			searchView: true,
			deleteView: false, // 删除搜索记录的视图
			backHistories: searchHistories(),
			histories: searchHistories(),
			recommendations: ['耳机', '电动车', '笔记本', '华为手机', 'AJ鞋', '篮球'],
			goodsData: [],// _home_data.goodsList(),
			filterData: '',
			filterDropdownValue: [],
			filterValues: [],
			loadStatus: 'more',
			pagination: {
				pageIndex: 0,
				pageSize: 20,
				endTime: '',
				finish: false
			},
			searchBody: null
		}
	},
	computed: {
		...mapState(['userId']),
		leftGoods() {
			return this.goodsData.filter((e, index) => index % 2 === 0)
		},
		rightGoods() {
			return this.goodsData.filter((e, index) => index % 2 === 1)
		}
	},
	onLoad(param) {
		console.log('search page onLoad, param:', param)
		// this.goodsData = _home_data.goodsList() //虚拟

		this.pagination = iniPagination()
		let type_index = 0
		if (param.type !== undefined) {
			type_index = parseInt(param.type)
			console.log('type_index', type_index)
			this.searchView = false // switch to result list
			let searchBody = {
				type: param.typeName,
				userAddress: '全校',
				sort: 4, //最新
				outdated: -1, //没有限制
				highPrice: -1,
				lowPrice: -1
			}
			this.searchKey = ''
			this.doSearch(searchBody, true)
			this.searchBody = searchBody
			this.search_ph = ''
		}
		// Im: 必须用 setTimeout才能初始化  TODO: MP-MINIPROGRAM 无法显示
		setTimeout(() => {
			this.filterDropdownValue = [
				[0, type_index || 0], //type
				[0], //第2个菜单选中 一级菜单的第1项
				[0],
				[[], []] //筛选菜单选中 第一个筛选的第0项，第二个筛选的第1,2,7项，第三个筛选的第1,0项
			]
			this.filterData = filter_data
		}, 0)
	},
	onReady() {
		_tool.setBarColor(true)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	onReachBottom() {
		console.log('search 触底')
		this.doSearch(this.searchBody)
	},
	methods: {
		goodsListTap(id) {
			uni.navigateTo({
				url: '/pages/detail/commodity?id=' + id
			})
		},
		finishDel() {
			// Arrow function's this will inherit from its nearest parent
			let delIds = this.backHistories.filter(v => this.histories.indexOf(v) === -1)
			console.log('delIds', delIds)
			if (delIds.length !== 0)
				this.$api
					.delSearchHistory(delIds)
					.then(({ data }) => {
						if (data.success) {
							//删除成功 success == true
							this.backHistories = this.histories.slice(0)
							deleteView = false
						}
						uni.showToast({
							title: '删除搜索记录失败',
							icon: 'none'
						})
					}) // 删除失败，恢复历史记录
					.catch(err => {
						// this.histories = this.backHistories
						uni.showToast({
							title: '删除搜索记录失败',
							icon: 'none'
						})
					})
			else this.deleteView = false
		},
		cancelDel() {
			;[...this.histories] = this.backHistories //数组展开,可作为左值，但对象展开不可作为左值
			this.deleteView = false
		},
		delAllHistory() {
			uni.showModal({
				content: '确定要全部删除吗',
				success: e => {
					if (!e.cancel)
						this.$api
							.clearSearchHistory(this.userId)
							.then(res => {
								if (res.data.success) {
									this.histories = []
									this.backHistories = []
									this.deleteView = false
								}
							})
							.catch(() =>
								uni.showToast({
									title: '删除失败',
									icon: 'none'
								})
							)
				}
			})
		},
		getSearchHistory() {
			this.$appi.getSearchHistory(this.userId).then(res => (this.histories = res.data.data))
		},
		async onPullDown(done) {
			this.pagination = iniPagination() //对象展开不适用
			await this.doSearch(this.searchBody)
			done() // 结束下拉刷新
		},
		searchTap(key) {
			if (typeof key === 'string') {
				this.searchKey = key
			}
			// reset
			this.pagination = iniPagination()
			this.doSearch({
				type: '全部',
				sort: 4,
				outdated: -1,
				highPrice: -1,
				lowPrice: -1
			}) // click '搜索', pass default condition
		},
		confirmFilter(e) {
			if ((this.filterValues.length === 0 && JSON.stringify(this.filterDropdownValue) === JSON.stringify(e.index)) || JSON.stringify(this.filterValues) === JSON.stringify(e.value))
				return

			this.filterValues = e.value
			this.pagination = iniPagination()
			this.goodsData = []

			// do search with filter condition
			let searchBody = Object.assign(
				{
					type: e.value[0][1],
					userAddress: e.value[1][0]
				},
				conditionMap(e.value[2][0], e.value[3][0][e.value[3][0].length - 1] || '', e.value[3][1])
			)
			console.log('searchBody', searchBody)
			this.searchBody = searchBody
			this.doSearch(searchBody, true)
		},
		BackPage() {
			uni.navigateBack()
		},
		searchInput(event) {
			let value = event.detail.value
			this.searchKey = value
			if (value) {
				this.search_close = true
			} else {
				this.search_close = false
			}
		},
		closeInput() {
			this.searchKey = ''
			this.search_close = false
		},
		async doSearch(condition, keyNull = false) {
			if (this.onRequest) return
			this.onRequest = true
			this.loadStatus = 'loading'
			// 虚拟数据加载
			this.searchView = false

			let key
			// 关键词 模糊搜索
			if (!keyNull) key = this.searchKey.trim().length == 0 ? this.search_ph : this.searchKey.trim()
			else key = this.searchKey.trim()
			await this.$api
				.getSearchResult(key, condition, this.pagination)
				.then(res => {
					this.searchView = false
					let resp = res.data.data
					this.goodsData.push(...resp.pageList)
					this.pagination.pageIndex = resp.pageIndex

					if (resp.pageIndex - 1 >= resp.pageCount) {
						// 没有更多数据了
						this.loadStatus = 'noMore'
						this.pagination.finish = true
					}
					this.loadStatus = 'more'
					this.onRequest = false
				})
				.catch(err => {
					this.loadStatus = 'more'
					uni.showToast({
						title: '搜索失败，请检查网络',
						icon: 'none',
						duration: 2000
					})
					this.onRequest = false
				})
		}
	}
}
</script>

<style lang="scss" scoped>
@import '../../static/zaiui/style/search.scss';
.filterList {
	position: sticky;
	top: calc(var(--status-bar-height) + 101rpx);
}
.top-module{
	position: fixed;
	z-index: 1000;
	width: 100%;
}
.capsule-height{
	height: var(--status-bar-height);
}
.mp-seat-height{
	height: calc(var(--status-bar-height) * 2 + 101rpx);
}
</style>
