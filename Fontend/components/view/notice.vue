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
						<block v-for="(item, index) in tabData" :key="index">
							<view class="cu-item text-black" :class="index == tabCur ? 'select' : ''" @tap="tabSelect" :data-id="index">
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
		
		<!-- 局部下拉刷新范围 -->
		<you-scroll ref="scroll" @onPullDown="onPullDown">
			<!--广场内容区域-->
			<view class="zaiui-view-content" v-if="tabCur == 0">			
				<!--动态列表--> 
				<trends-list
					:list_data="notices.list"
					:viewBtn="true"
					@userTap="trends_userTap"
					@contentTap="trends_contentTap"
					@imgTap="trends_imgTap"
				/>
			</view>

			<!--出售内容区域-->
			<view class="zaiui-view-content" v-if="tabCur == 1">
				<trends-list
					:list_data="sellNotices.list"
					:isMax="2"
					@userTap="trends_userTap"
					@contentTap="trends_contentTap"
					@imgTap="trends_imgTap"
				/>
			</view>

			<!--需求内容区域-->
			<view class="zaiui-view-content" v-show="tabCur == 2">
				<trends-list
					:list_data="demandNotices.list"
					:isMax="2"
					@userTap="trends_userTap"
					@contentTap="trends_contentTap"
					@imgTap="trends_imgTap"
				/>
			</view>
		
			<!--我发布的 区域-->
			<view class="zaiui-view-content" v-show="tabCur == 3">
				<trends-list
					:list_data="myNotices.list"
					:isMax="2"
					@userTap="trends_userTap"
					@contentTap="trends_contentTap"
					@imgTap="trends_imgTap"
				/>
			</view>
			<!-- Loading Text -->
			<uni-load-more :status="loadStatus" class="margin-bottom"></uni-load-more>
		</you-scroll>
		<!--占位底部距离-->
		<view class="cu-tabbar-height" />
		
	</view>
</template>

<script>
import trendsList from '@/components/list/trends-list'
import youScroll from '@/components/you-scroll/you-scroll.vue'
//======================================================================
import _find_data from '@/static/zaiui/data/find.js' //虚拟数据
import handles from '@/utils/handles.js'

const iniNotice = ()=> ({
	pageIndex:0,
	pageSize:20,
	startTime:new Date().format('yyyy-MM-dd hh:mm'),
	finish: false,
	list:[]
})
const noticeMap = tabCur => {
	switch(tabCur){
		case 0:
			return 'notices'
		case 1:
			return 'sellNotices'
		case 2:
			return 'demandNotices'
		case 3:
			return 'myNotices'
		default:
			console.log('case default');
	}
}

export default {
	name: 'find',
	components: {
		trendsList,
		youScroll
	},
	data() {
		return {
			tabCur: 0,
			tabData: ['广场', '出售', '需求','我发布的'],
			notices: {list:[]},
			sellNotices:{list:[]},
			demandNotices:{list:[]},
			myNotices:{list:[]},
			loadStatus: 'more'
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
		this.notices = iniNotice()
		//加载虚拟数据
		this.notices.list = _find_data.trendsData()		
		// 请求
		
	},
	mounted() {
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 101
		})
	},
	methods: {
		async loadNotices(){
			this.loadStatus = 'loading'
			await this.$api.getNotices(type, pagination)
				.then(({data})=> {
					// 一些没有判断 success,根据后台的 json 来决定要不要加这个判断
					
				})
				.catch()
			this.loadStatus = 'more'
		},
		async onPullDown(done){
			this[noticeMap(this.tabCur)] = iniNotice()
			await this.loadNotices()
			done()		
		},
		setReachBottom() {
			console.log('notice 触底了')
			this.loadNotices()
		},
		tabSelect(e) {
			let current = parseInt(e.currentTarget.dataset.id)
			
			if(this.tabCur !== current && this[noticeMap(current)].list.length === 0){
				this[noticeMap(current)] = iniNotice()
				this.loadNotices()
			}
			this.tabCur = e.currentTarget.dataset.id
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
