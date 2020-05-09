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
		<HMfilterDropdown v-if="!searchView" class="filterList" :filterData="filterData" :defaultSelected ="filterDropdownValue" @confirm="confirmFilter"></HMfilterDropdown>

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
	onLoad() {
		console.log('search page onLoad')
		this.goodsData = _home_data.goodsList(),
		this.filterDropdownValue = [
			// [1,1,0],				//第0个菜单选中 一级菜单的第1项，二级菜单的第1项，三级菜单的第3项
			[0,0],			//第1个菜单选中 都不选中
			[0],					//第2个菜单选中 一级菜单的第1项
			[0],
			[[0],[1,2,7],[1,0]],	//筛选菜单选中 第一个筛选的第0项，第二个筛选的第1,2,7项，第三个筛选的第1,0项
		];
		this.filterData = filter_data; 
	},
	onReady() {
		_tool.setBarColor(true)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	methods: {
		confirmFilter(){
			
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
			// 关键词 模糊搜索
			let key
			if (typeof _key === 'string') {
				this.searchKey = _key
			}
			key = this.searchKey.trim().length == 0 ? '高数' : this.searchKey.trim()
			let that = this
			this.$api
				.getSearchResult(key)
				.then(res => {
					this.goodsData = res.data
					that.searchView = false
				})
				.catch(err =>
					uni.showToast({
						title: '搜索失败，请检查网络',
						icon: 'none',
						duration: 2000
					})
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
