<template>
	<view class="zaiui-find-box" :class="show ? 'show' : ''">
		<!--分类标题栏-->
		<view class="bg-white zaiui-title-tab-box">
			<!--小程序端的标题-->
			<!-- #ifdef MP -->
			<view class="text-center text-black zaiui-small-routine-title">发现</view>
			<!-- #endif -->

			<!-- 顶层 tab -->
			<view class="flex flex-wrap">
				<view class="basis-l">
					<scroll-view scroll-x class="nav z">
						<block v-for="(item, index) in TabData" :key="index">
							<view class="cu-item text-black" :class="index == TabCur ? 'select' : ''" @tap="tabSelect" :data-id="index">
								<view>{{ item }}</view>
								<view class="tab-dot bg-red" />
							</view>
						</block>
					</scroll-view>
				</view>
			</view>
		</view>

		<!--占位的-->
		<view class="zaiui-seat-height" />

		<!--广场内容区域-->
		<view class="zaiui-view-content" v-if="TabCur == 0">
			
			<!--动态列表-->
			<trends-list
				:list_data="followTrendsData"
				:viewBtn="true"
				@userTap="trends_userTap"
				@followTap="trends_followTap"
				@contentTap="trends_contentTap"
				@imgTap="trends_imgTap"
				@talkTap="trends_talkTap"
				@viewBtnTap="trends_viewBtnTap"
				@commentTap="trends_commentTap"
				@appreciateTap="trends_appreciateTap"
			/>

			<!--占位底部距离-->
			<view class="cu-tabbar-height" />
		</view>

		<!--出售内容区域-->
		<view class="zaiui-view-content" v-if="TabCur == 1">
			<!--动态列表1-->
			<trends-list
				:list_data="trendsData"
				:isMax="2"
				@userTap="trends_userTap"
				@followTap="trends_followTap"
				@contentTap="trends_contentTap"
				@imgTap="trends_imgTap"
				@talkTap="trends_talkTap"
				@forwardTap="trends_forwardTap"
				@commentTap="trends_commentTap"
				@appreciateTap="trends_appreciateTap"
			/>

			<!--动态列表2-->
			<trends-list
				:list_data="trendsData"
				:isMin="2"
				@userTap="trends_userTap"
				@followTap="trends_followTap"
				@contentTap="trends_contentTap"
				@imgTap="trends_imgTap"
				@talkTap="trends_talkTap"
				@forwardTap="trends_forwardTap"
				@commentTap="trends_commentTap"
				@appreciateTap="trends_appreciateTap"
			/>

			<!--占位底部距离-->
			<view class="cu-tabbar-height" />
		</view>

		<!--需求内容区域-->
		<view class="zaiui-view-content" v-show="TabCur == 2">
			<!--动态列表1-->
			<trends-list
				:list_data="trendsData"
				:isMax="2"
				@userTap="trends_userTap"
				@followTap="trends_followTap"
				@contentTap="trends_contentTap"
				@imgTap="trends_imgTap"
				@talkTap="trends_talkTap"
				@forwardTap="trends_forwardTap"
				@commentTap="trends_commentTap"
				@appreciateTap="trends_appreciateTap"
			/>
			<!--占位底部距离-->
			<view class="cu-tabbar-height" />
		</view>
	</view>
</template>

<script>
import trendsList from '@/components/list/trends-list'
//======================================================================
import _find_data from '@/static/zaiui/data/find.js' //虚拟数据
import _tool from '@/static/zaiui/util/tools.js'

export default {
	name: 'find',
	components: {
		trendsList
	},
	data() {
		return {
			TabCur: 0,
			TabData: ['广场', '出售', '需求'],
			followTrendsData: [],
			trendsData: []
		}
	},
	props: {
		show: {
			type: Boolean,
			default: true
		},
		scrollBottom: {
			type: Boolean,
			default: false
		}
	},
	watch: {
		scrollBottom(newVal) {
			if (newVal) {
				this.setReachBottom()
			}
		}
	},
	created() {
		//加载虚拟数据
		this.trendsData = _find_data.trendsData()
		this.followTrendsData = _find_data.followTrendsData()
	},
	mounted() {
		_tool.setBarColor(true)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	methods: {
		//触底了
		setReachBottom() {
			console.log('notice 触底了')
		},
		tabSelect(e) {
			this.TabCur = e.currentTarget.dataset.id
			uni.pageScrollTo({
				scrollTop: 0,
				duration: 0
			})
		},
		trends_userTap(e) {
			console.log('用户区域被点击：' + JSON.stringify(e))
		},
		trends_followTap(e) {
			console.log('关注按钮被点击：' + JSON.stringify(e))
		},
		trends_contentTap(e) {
			console.log('文字内容被点击：' + JSON.stringify(e))
		},
		trends_imgTap(e) {
			console.log('图片被点击：' + JSON.stringify(e))
		},
		trends_talkTap(e) {
			console.log('话题被点击：' + JSON.stringify(e))
		},
		trends_forwardTap(e) {
			console.log('分享被点击：' + JSON.stringify(e))
		},
		trends_commentTap(e) {
			console.log('评论被点击：' + JSON.stringify(e))
		},
		trends_appreciateTap(e) {
			console.log('点赞被点击：' + JSON.stringify(e))
		},
		trends_viewBtnTap(e) {
			console.log('查看TA被点击：' + JSON.stringify(e))
		},
		viewAllTap() {
			console.log('点击了查看全部')
		},
		r_userTap(e) {
			console.log('用户信息被点击：' + JSON.stringify(e))
		},
		r_followTap(e) {
			console.log('关注按钮被点击：' + JSON.stringify(e))
		},
		r_viewAllTap() {
			console.log('点击了查看全部')
		}
	}
}
</script>

<style lang="scss" scoped>
.zaiui-find-box {
	display: none;
}
.zaiui-find-box.show {
	display: block;
}
.zaiui-title-tab-box {
	position: fixed;
	width: 100%;
	top: 0;
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
.zaiui-seat-height {
	width: 100%;

	/* #ifndef MP */
	height: calc(var(--status-bar-height) + 81.81rpx);
	/* #endif */

	/* #ifdef MP */
	margin-top: calc(var(--status-bar-height) + 185rpx);
	/* #endif */
}
.zaiui-view-content {
	padding: 0 27.27rpx 54.54rpx;
}
</style>
