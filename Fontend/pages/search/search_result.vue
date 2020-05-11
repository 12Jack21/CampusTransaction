<template>
	<view>
		<view style="height: 200rpx;">11</view>

		<!-- 筛选界面 -->
		<HMfilterDropdown :filterData="filterData" :updateMenuName="true" :defaultSelected="filterDropdownValue" @confirm="confirmFilter" dataFormat="Object"></HMfilterDropdown>
	
		<HMfilterDropdown style="margin-top: 200rpx;" :filterData="filterData" :defaultSelected ="filterDropdownValue" :updateMenuName="true" @confirm="confirm" dataFormat="Object"></HMfilterDropdown>
	</view>
</template>

<script>
import HMfilterDropdown from '@/components/HM-filterDropdown/HM-filterDropdown.vue'
import filter_data from '@/static/data/filters.js'

export default {
	components: {
		HMfilterDropdown
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
			filterData: filter_data,
			filterDropdownValue: [
				[0, 3], //type
				[2], //第2个菜单选中 一级菜单的第1项
				[1],
				[[0], [1]] //筛选菜单选中 第一个筛选的第0项，第二个筛选的第1,2,7项，第三个筛选的第1,0项
			],
			filterValues: filter_data
		}
	},
	onReady() {
		console.log('ready', this.filterDropdownValue)
	},
	methods: {
		confirmFilter(e) {
			if ((this.filterValues.length === 0 && JSON.stringify(this.filterDropdownValue) === JSON.stringify(e.index)) || JSON.stringify(this.filterValues) === JSON.stringify(e.value))
				return

			console.log('previous', this.filterValues)
			console.log('current', e.value)
			console.log('dropdown', this.filterDropdownValue)
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
			console.log('index', e.index)
			console.log('search body', searchBody)
			this.doSearch(searchBody)
		},
		doSearch(_key) {
			uni.showLoading({
				title: '搜索中',
				mask: false
			})
			// 关键词 模糊搜索
			if (typeof _key === 'string') {
				this.searchKey = _key
			}
			let key = this.searchKey.trim().length == 0 ? '高数' : this.searchKey.trim()
			let that = this
			let condition = null
			if (_key.body) condition = _key
			this.$api
				.getSearchResult(key, condition)
				.then(res => {
					this.goodsData = res.data
					that.searchView = false
					uni.hideLoading()
				})
				.catch(err => {
					uni.hideLoading()
					uni.showToast({
						title: '搜索失败，请检查网络',
						icon: 'none',
						duration: 2000
					})
				})

			// 虚拟数据加载
			this.searchView = false
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
