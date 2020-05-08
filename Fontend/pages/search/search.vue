<template>
	<view>
		<!-- 搜索框 -->
		<view class="zaiui-bar-search-title-box">
			<view class="cu-bar search bg-white fixed no-shadow">
				<view class="action" @tap="BackPage"><text class="cuIcon-back" /></view>
				<view class="search-form round">
					<text class="cuIcon-search" />
					<input @input="searchInput" :value="searchKey" :adjust-position="false" type="text" placeholder="高数" confirm-type="search" />
					<text class="cuIcon-close close-icon" v-if="search_close" @tap="closeInput" />
				</view>
				<view class="action" @tap="doSearch"><text class="text-red">搜索</text></view>
			</view>
			<!--占位的-->
			<view class="zaiui-seat-height" />
		</view>
		<!-- end -->

		<!-- 搜索界面 -->
		<view v-if='searchView'>
			<!--搜索面板区域-->
			<view class="padding zaiui-search-list-view" v-if="!deleteView">
				<!--搜索记录-->
				<view class="search-list-view">
					<view class="search-bar-view">
						<text class="text-black">历史搜索</text>
						<text class="cuIcon-delete text-gray icon-right" @tap="deleteHistoryTap" />
					</view>
					<view class="btn-view">
						<button class="cu-btn round" v-for="(history, index) in histories" :key="'history-' + index">{{ history }}</button>
					</view>
				</view>
			
				<!--推荐搜索-->
				<view class="search-list-view">
					<view class="search-bar-view"><text class="text-black">推荐搜索</text></view>
					<view class="btn-view">
						<button class="cu-btn round">耳机</button>
						<button class="cu-btn round">苹果手机</button>
						<button class="cu-btn round">电动车</button>
						<button class="cu-btn round">笔记本</button>
						<button class="cu-btn round">衣柜</button>
						<button class="cu-btn round">平板电脑</button>
						<button class="cu-btn round">华为手机</button>
						<button class="cu-btn round">小米</button>
						<button class="cu-btn round">三星</button>
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
							<text class="text-gray">全部删除</text>
							<text class="text-red" @tap="logTap">完成</text>
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
		<view v-else>
			 <!--商品列表-->
			 <goods-list :list_data="goodsData" @listTap="goodsListTap" />
		</view>
		<!-- end -->
	</view>
</template>

<script>
import _tool from '@/static/zaiui/util/tools.js' //工具函数
import goodsList from '@/components/list/goods-list.vue'
import _home_data from '@/static/zaiui/data/home.js'; //虚拟数据

export default {
	components:{
		goodsList
	},
	data() {
		return {
			search_close: false,
			searchKey: '',
			searchView: false,
			deleteView: false, // 删除搜索记录的视图
			histories: ['耳机', '大英课本', '四六级试卷', '电动车'],
			goodsData:[]
		}
	},
	onLoad() {
		this.goodsData = _home_data.goodsList
	},
	onReady() {
		_tool.setBarColor(true)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	methods: {
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
		deleteHistoryTap() {
			this.deleteView = true
		},
		logTap() {
			this.deleteView = false
		},
		doSearch() {
			// 关键词 模糊搜索
			let key = this.searchKey.trim().length == 0 ? '高数' : this.searchKey.trim()
			let that = this
			this.$api
				.getSearchResult(key)
				.then(res => that.searchView = false)
				.catch(err => uni.showToast({
					title:'搜索失败，请检查网络',
					icon:'none',
					duration: 2000
				}))
		},
		getSearchHistory(){
			
		}
	}
}
</script>

<style lang="scss" scoped>
@import '../../static/zaiui/style/search.scss';
</style>
