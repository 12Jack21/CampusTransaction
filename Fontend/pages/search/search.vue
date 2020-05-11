<template>
	<view>
		<!-- 搜索框 -->
		<view class="zaiui-bar-search-title-box">
			<view class="cu-bar search bg-white fixed no-shadow">
				<view class="action" @tap="BackPage"><text class="cuIcon-back" /></view>
				<view class="search-form round">
					<text class="cuIcon-search" />
					<input @input="searchInput" :value="searchKey" @focus="searchView = true" :adjust-position="false" type="text" :placeholder="search_ph" confirm-type="search" />
					<text class="cuIcon-close close-icon" v-if="search_close" @tap="closeInput" />
				</view>
				<view class="action" @tap="searchTap"><text class="text-red">搜索</text></view>
			</view>
			<!-- end -->
			<!--占位的-->
			<view class="zaiui-seat-height" />
		</view>
		<!-- end -->

		<!-- 筛选界面 -->
		<HMfilterDropdown
			v-if="!searchView"
			class="filterList"
			:filterData="filterData"
			:updateMenuName="true"
			:defaultSelected="filterDropdownValue"
			@confirm="confirmFilter"
			dataFormat="Object"
		></HMfilterDropdown>

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
						<button class="cu-btn round" v-for="(history, index) in histories" :key="'history-' + index" @tap="searchTap(history)">{{ history }}</button>
					</view>
				</view>

				<!--推荐搜索-->
				<view class="search-list-view">
					<view class="search-bar-view"><text class="text-black">推荐搜索</text></view>
					<view class="btn-view">
						<button class="cu-btn round" v-for="(r, index) in recommendations" :key="'recommend-' + index" @tap="searchTap(r)">{{ r }}</button>
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
							<text class="text-gray" @tap="delAllHistory">全部删除</text>
							<text class="text-red" @tap="deleteView = false">完成</text>
						</view>
					</view>
					<view class="btn-view">
						<button class="cu-btn round" v-for="(history, index) in histories" :key="'del-' + index">
							<text>{{ history }}</text>
							<view class="cuIcon-roundclosefill close-icon"></view>
						</button>
					</view>
				</view>
			</view>
		</view>
		<!-- end -->

		<!-- 搜索结果 -->
		<view class="margin-bottom zaiui-goods-list-box" v-else>
			<view class="flex flex-wrap ">
				<!--商品列表-->
				<goods-list :list_data="leftGoods" @listTap="goodsListTap" class="padding-right-xs" />
				<goods-list :list_data="rightGoods" @listTap="goodsListTap" class="padding-left-xs" />
			</view>
		</view>
		<!-- end -->
		<!-- Loading Text -->
		<uni-load-more :status="loadStatus"></uni-load-more>
	</view>
</template>

<script>
import _tool from '@/static/zaiui/util/tools.js' //工具函数
import goodsList from '@/components/list/goods-list.vue'
import HMfilterDropdown from '@/components/HM-filterDropdown/HM-filterDropdown.vue'
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue'
import { mapState } from 'vuex'
import handles from '@/utils/handles.js'

import _home_data from '@/static/zaiui/data/home.js' //虚拟数据
import filter_data from '../../static/data/filters.js'

export default {
	components: {
		goodsList,
		HMfilterDropdown,
		uniLoadMore
	},
	data() {
		return {
			search_close: false,
			search_ph: '高数',
			searchKey: '',
			searchView: true,
			deleteView: false, // 删除搜索记录的视图
			histories: ['耳机', '大英课本', '四六级试卷', '电动车'],
			recommendations: ['耳机', '电动车', '笔记本', '华为手机', 'AJ鞋', '篮球'],
			goodsData: [],
			filterData: '',
			filterDropdownValue: [],
			filterValues: [],
			loadStatus: 'more',
			pagination:{
				pageIndex:0,
				pageSize:20,
				startTime:'',
				finish: false
			}
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
		this.goodsData = _home_data.goodsList() //虚拟
		
		let type_index = 0
		if (param.type !== undefined) {
			type_index = parseInt(param.type)
			console.log('type_index', type_index)
			this.searchView = false // switch to result list
			let searchBody = {
				type: param.typeName,
				address: '全校',
				sort: '最新',
				outdated: '',
				price: '',
			}
			this.doSearch(searchBody)
			this.search_ph = ''
		}
		// Im: 必须用 setTimeout才能初始化
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
		console.log('ready', this.filterDropdownValue)
		_tool.setBarColor(true)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	methods: {
		//TODO 下拉刷新
		searchTap(key){
			if (typeof key === 'string') {
				this.searchKey = key
			}
			// reset
			this.pagination = {
				pageIndex:0,
				pageSize:20,
				startTime:new Date().format('yyyy-MM-dd hh:mm'),
				finish: false
			}
			doSearch(null) // pass condition
		},
		confirmFilter(e) {
			if ((this.filterValues.length === 0 && JSON.stringify(this.filterDropdownValue) === JSON.stringify(e.index)) || JSON.stringify(this.filterValues) === JSON.stringify(e.value))
				return

			console.log('previous', this.filterValues)
			console.log('current', e.value)
			console.log('dropdown', this.filterDropdownValue)
			this.filterValues = e.value
			
			this.pagination = {
				pageIndex:0,
				pageSize:20,
				startTime:new Date().format('yyyy-MM-dd hh:mm'),
				finish: false
			}
			
			// do search with filter condition
			let searchBody = {
				type: e.value[0][1],
				address: e.value[1][0],
				sort: e.value[2][0],
				outdated: e.value[3][0][e.value[3][0].length - 1] || '',
				price: e.value[3][1],
			}
			console.log('index', e.index)
			console.log('search body', searchBody)
			this.doSearch(searchBody)
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
		delAllHistory() {
			let that = this
			uni.showModal({
				content: '确定要全部删除吗',
				success: () => {
					thi.$api.clearSearchHistory(that.userId).then(res => {
						if (res.data) this.histories = []
					})
				},
				fail() {
					uni.showToast({
						icon: 'none',
						title: '删除失败'
					})
				}
			})
		},
		doSearch(condition) {
			this.loadStatus = 'loading'
			
			// 关键词 模糊搜索
			let key = this.searchKey.trim().length == 0 ? '高数' : this.searchKey.trim()
			let that = this
			this.$api
				.getSearchResult(key, condition, this.pagination)
				.then(res => {
					that.searchView = false
					let resp = res.data.data
					this.goodsData.push(...resp.pageList)
					this.pagination.pageIndex = resp.pageIndex
					
					if(resp.pageIndex === resp.pageCount){
						// 没有更多数据了
						this.loadStatus = 'noMore'
						this.pagination.finish = true
					}
					this.loadStatus = 'more'
				})
				.catch(err => {
					this.loadStatus = 'more'
					uni.showToast({
						title: '搜索失败，请检查网络',
						icon: 'none',
						duration: 2000
					})
				})

			// 虚拟数据加载
			this.searchView = false
		},
		getSearchHistory() {
			let that = this
			this.$appi.getSearchHistory(that.userId).then(res => (that.histories = res.data))
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
</style>
