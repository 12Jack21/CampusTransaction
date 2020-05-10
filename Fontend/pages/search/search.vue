<template>
	<view>
		<!-- 搜索框 -->
		<view class="zaiui-bar-search-title-box">
			<view class="cu-bar search bg-white fixed no-shadow">
				<view class="action" @tap="BackPage"><text class="cuIcon-back" /></view>
				<view class="search-form round">
					<text class="cuIcon-search" />
					<input @input="searchInput" :value="searchKey" @focus="searchView = true" :adjust-position="false" type="text" placeholder="高数" confirm-type="search" />
					<text class="cuIcon-close close-icon" v-if="search_close" @tap="closeInput" />
				</view>
				<view class="action" @tap="doSearch"><text class="text-red">搜索</text></view>
			</view>
			<!-- end -->
			<!--占位的-->
			<view class="zaiui-seat-height" />
		</view>
		<!-- end -->
		
		<!-- 筛选界面 -->
		<HMfilterDropdown class="filterList" :filterData="filterData" :defaultSelected ="filterDropdownValue" @confirm="confirmFilter"></HMfilterDropdown>

		<!-- 搜索界面 -->
		<view v-if="searchView">
			<!--搜索面板区域-->
			<view class="padding zaiui-search-list-view" v-if="!deleteView">
				<!--搜索记录-->
				<view class="search-list-view">
					<view class="search-bar-view">
						<text class="text-black">历史搜索</text>
						<text class="cuIcon-delete text-gray icon-right" @tap="deleteView=true" />
					</view>
					<view class="btn-view">
						<button class="cu-btn round" v-for="(history, index) in histories" :key="'history-' + index" @tap="doSearch(history)">{{ history }}</button>
					</view>
				</view>

				<!--推荐搜索-->
				<view class="search-list-view">
					<view class="search-bar-view"><text class="text-black">推荐搜索</text></view>
					<view class="btn-view">
						<button class="cu-btn round" v-for="(r, index) in recommendations" 
						:key="'recommend-' + index" @tap="doSearch(r)">{{ r }}</button>
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
							<text class="text-red" @tap="deleteView=false">完成</text>
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
				<goods-list :list_data="leftGoods" @listTap="goodsListTap" class="padding-right-xs"/>
				<goods-list :list_data="rightGoods" @listTap="goodsListTap" class="padding-left-xs"/>
			</view>
		</view>
		<!-- end -->
	</view>
</template>

<script>
import _tool from '@/static/zaiui/util/tools.js' //工具函数
import goodsList from '@/components/list/goods-list.vue'
import _home_data from '@/static/zaiui/data/home.js' //虚拟数据
import HMfilterDropdown from '@/components/HM-filterDropdown/HM-filterDropdown.vue'
import filter_data from '../../static/data/filters.js'
import { mapState } from 'vuex'

export default {
	components: {
		goodsList, HMfilterDropdown
	},
	data() {
		return {
			search_close: false,
			searchKey: '',
			searchView: true,
			deleteView: false, // 删除搜索记录的视图
			histories: ['耳机', '大英课本', '四六级试卷', '电动车'],
			recommendations: ['耳机', '电动车', '笔记本', '华为手机', 'AJ鞋', '篮球'],
			goodsData: [],
			filterData:'',
			filterDropdownValue:[],
			filterValues:[]
		}
	},
	computed:{
		...mapState(['userId']),
		leftGoods(){
			return this.goodsData.filter((e,index) => index % 2 === 0)
		},
		rightGoods(){
			return this.goodsData.filter((e,index) => index % 2 === 1)
		}
	},
	onLoad(param) {
		console.log('search page onLoad, param:', param)
		this.goodsData = _home_data.goodsList()
		let type_index = 0
		if(param.type !== undefined){
			type_index = parseInt(param.type)
			console.log('type_index',type_index)
			this.searchView = false // switch to result list
		}
		this.searchView = false
		this.filterDropdownValue = [
			[0, 3 ],			//type
			[2],					//第2个菜单选中 一级菜单的第1项
			[1],
			[[0],[1]],	//筛选菜单选中 第一个筛选的第0项，第二个筛选的第1,2,7项，第三个筛选的第1,0项
		]
		console.log(this.filterDropdownValue);
		this.filterData = filter_data; 
	},
	onReady() {
		console.log('ready',this.filterDropdownValue);
		_tool.setBarColor(true)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	methods: {
		confirmFilter(e){
			if(this.filterValues.length === 0 && JSON.stringify(this.filterDropdownValue) === JSON.stringify(e.index) ||
				JSON.stringify(this.filterValues) === JSON.stringify(e.value)) return
			
			console.log('previous',this.filterValues);
			console.log('current',e.value);
			console.log('dropdown',this.filterDropdownValue);
			this.filterValues = e.value
			// do search with filter condition
			let searchBody = {
				type: e.value[0][1],
				address: e.value[1][0],
				sort: e.value[2][0],
				outdated: e.value[3][0][e.value[3][0].length - 1] || '',
				price: e.value[3][1],
				body: true
			}
			console.log('index',e.index);
			console.log('search body',searchBody);
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
		delAllHistory(){
			let that = this
			uni.showModal({
				content:'确定要全部删除吗',
				success: () => {
					thi.$api.clearSearchHistory(that.userId)
						.then(res=>{
							if(res.data)
								this.histories = []
						})
				},
				fail() {
					uni.showToast({
						icon:'none',
						title:'删除失败'
					})
				}
			})
		},
		doSearch(_key) {
			uni.showLoading({
				title: '搜索中',
				mask: false
			});
			// 关键词 模糊搜索
			if (typeof _key === 'string') {
				this.searchKey = _key
			}
			let key = this.searchKey.trim().length == 0 ? '高数' : this.searchKey.trim()
			let that = this
			let condition = null
			if(_key.body) condition = _key
			this.$api
				.getSearchResult(key,condition)
				.then(res => {
					this.goodsData = res.data
					that.searchView = false
					uni.hideLoading()
				})
				.catch(err =>{
					uni.hideLoading()
					uni.showToast({
						title: '搜索失败，请检查网络',
						icon: 'none',
						duration: 2000
					})
				}
				)

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
.filterList{
	position: sticky;
	top: calc(var(--status-bar-height) + 101rpx);
}
</style>
