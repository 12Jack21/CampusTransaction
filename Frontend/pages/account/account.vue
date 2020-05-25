<template>
	<view class="zaiui-my-box" style="color: #FFFFFF;">
		<view class="zaiui-head-box">
			<!--标题栏-->
			<bar-title bgColor="bg-white">
				<block slot="content">用户信息</block>
				<block slot="right"><text class="cuIcon-more" /></block>
			</bar-title>
			<!-- end -->

			<!--用户信息-->
			<view class="" style="background-image: url(../../static/images/accBg.png) ;position: relative;z-index: 0;">
				<view class="flex" style="justify-content: center;padding-top: 20rpx;">
					<view class="flex flex-direction user-space" style="align-items: center;">
						<view class="cu-avatar round" :style="{ backgroundImage: account.avartar.length === 0 ? 'url(/static/images/avatar/1.jpg)' : account.avartar }" />
						<view class="">
							<text class="username">{{ account.username }}</text>
							<text style="border-radius: 16rpx;vertical-align: top;" :class="[account.gender === 0 ? 'cuIcon-female bg-pink' : 'cuIcon-male bg-blue']"></text>
						</view>
						<view class="address"><text class="cuIcon-locationfill"></text>{{ account.address }}</view>
						<view class="introduction">
							简介： {{account.introduction.length===0?'该用户还未填写':account.introduction}}
						</view>
						<view class=" wechat">
							<view v-if="account.wechat" class="we">
								微信号：
								<text selectable>{{ account.wechat }}</text>
							</view>
							<view v-if="account.qq" class="qq">
								QQ号：
								<text selectable>{{ account.qq }}</text>
							</view>
						</view>
						<view class="cu-list grid col-2 showItem">
							<view class="cu-item ">
								<view class="text-xxl text-white">{{ account.rate }}%</view>
								<text class="text-sm">交易率</text>
							</view>
							<view class="cu-item ">
								<view class="text-xxl text-white">{{ account.evaluation }}%</view>
								<text class="text-sm">信誉值</text>
							</view>
						</view>
						<view class="height-spacce flex flex-direction" style="justify-content: center;"><view class="border-view" /></view>
						<view class="text-lg denote">{{ who }}发布的物品</view>
					</view>
				</view>
			</view>
			<!-- end -->

			<view class="zaiui-goods-info-view-box">
				<view class="zaiui-recommend-list-box">
					<!-- 水平滑动列表 -->
					<view class="comList recommend-scroll-box">
						<view class="noCom" v-if="commodities.length === 0">
							<image src="../../static/images/noCom.png" mode="aspectFit"></image>
							<view>暂无发布中的物品或需求</view>
						</view>
						<scroll-view class="recommend-scroll" scroll-x @scrolltolower="loadMore" v-else>
							<block v-for="(item, index) in commodities" :key="index">
								<view :id="['scroll' + (index + 1)]" class="recommend-scroll-item comItem" @tap="comTap(item.id)" style="">
									<view class="cu-avatar xl radius" :style="{ backgroundImage: item.img.length === 0 ? 'url(/static/images/comDefault.png)' : item.img, position: 'relative' }">
										<text class=" count-view cu-tag sm" :class="['bg-' + (item.count <= 2 ? 'red' : 'blue')]">剩余{{ item.count }}件</text>
									</view>
									<view class="text-cut-2 text-sm text-black margin-tb-sm">{{ item.name }}</view>
									<view class="text-red text-price margin-tb-sm text-lg">{{ item.expectedPrice }}</view>
								</view>
							</block>
						</scroll-view>
					</view>
				</view>
			</view>
		</view>
		<mpopup ref="mpopup" :isdistance="true"></mpopup>
	</view>
</template>

<script>
import { mapState } from 'vuex'
import barTitle from '../../components/basics/bar-title.vue'
import handles from '../../utils/handles.js'

export default {
	components: {
		barTitle
	},
	data() {
		return {
			onRequest:false, // 防止重复分页请求
			accId:-1,
			pageIndex:1,
			endTime:'',
			finish:false,
			account: {
				username: '急可',
				avartar: '',
				gender: 0,
				introduction:'我就是我，是不一样的高哥',
				address: '文理学部',
				wechat: '2020wechat2020',
				qq: '99221101',
				rate: 90, // 交易成功率
				evaluation: 90.2 // 信誉值
			},
			aCom: {
				id: 1,
				name: '毛巾',
				img: '',
				expectedPrice: '200',
				originalPrice: '300',
				count: 3
			},
			commodities: []
		}
	},
	computed: {
		...mapState(['userId']),
		who() {
			return this.account.gender === 0 ? '她' : '他'
		}
	},
	onPullDownRefresh() {
		console.log('accId',this.accId);
		this.getAccount(this.accId)
		this.commodities = []
		this.initPagination()
		this.getCommodities()
	},
	onLoad(params) {
		this.accId = params.id
		this.getAccount(params.id)
		
		//debug
		for (let i = 0; i < 10; i++) this.commodities.push(this.aCom)

		// request
		this.initPagination()
		this.getCommodities()
	},
	methods: {
		initPagination(){
			this.pageIndex = 1
			this.finish = false
			this.endTime = new Date().format('yyyy-MM-dd hh:mm')
		},
		loadMore(e){
			this.getCommodities()
		},
		comTap(id) {
			uni.navigateTo({
				url: '../detail/commodity?id=' + id
			})
		},
		getAccount(toAccId) {
			this.$api
				.getOtherAccount(this.accId, this.userId)
				.then(({ data }) => {
					console.log('其他账户的信息', data)
					this.account = data
					this.tip(1,'用户信息获取成功')
				})
				.catch(() => this.tip(1,"网络异常"))
		},
		getCommodities(){
			if(this.finish || this.onRequest) return
			this.onRequest = true
			console.log('请求中');
			this.$api
				.getCommoditiesByAcc(this.accId)
				.then(({ data }) => {
					this.pageIndex = data.pageIndex
					if(data.pageIndex + 1 >= data.pageCount){
						this.finish = true
						this.tip(3,'已获取全部发布中的物品')
					}
					this.commodities.push(...data.pageList)
					this.onRequest = false
				})
				.catch(() =>{
					 this.tip(1,"网络异常")
					 this.onRequest = false
				})
		},
		tip(index, content, isClick=false,timeout = 2000) {
			let types = ['success', 'err', 'warn', 'info', 'loading']
			this.$refs.mpopup.open({
				type: types[index],
				content,
				timeout,
				isClick
			})
		},
	}
}
</script>

<style lang="scss">
@import '../../static/zaiui/style/goods.scss';
$offset: -80rpx;
.count-view {
	position: absolute;
	bottom: 4rpx;
	left: 4rpx;
	vertical-align: bottom;
	border-radius: 16rpx;
}
.noCom {
	display: flex;
	flex-direction: column;
	border-radius: 30rpx;
	width: 370rpx;
	height: 400rpx;
	background-color: #fff;
	color: #b9b9b9;
	text-align: center;
	padding: 0 10rpx 20rpx 10rpx;
}
.showItem {
	border: #fff 1px solid;
	background-color: transparent;
	border-radius: 10px;
	width: 100%;
}
.border-view {
	width: 550rpx;
	height: 2rpx;
}
.introduction{
	text-align: center;
	padding: 6px 0;
	font-size: 1.2em;
	font-weight: 500;
	color: #ffd9cd;
	text-shadow: #cd725b 1px 1px 1px;
}
.wechat {
	padding: 14rpx 0;
	text-align: center;
	.we{
		color: #abdca2;
	}
	.qq{
		color: #BEDAF3;
	}
}
.height-spacce {
	height: 100rpx;
}
.denote {
	position: relative;
	text-align: center;
	margin-bottom: 15rpx;
	color: #ffeeea;
	top: $offset;
}
.user-space {
	.cu-avatar {
		margin-top: 40rpx;
		width: 180rpx;
		height: 180rpx;
	}
	.username {
		font-size: 2em;
		margin: 20rpx 0;
	}
	.address {
		color: #e54d42;
		background-color: #fadbd9;
		border-radius: 6px;
		padding: 6px;
	}
}
.comList {
	position: relative;
	display: flex;
	justify-content: center;
	margin: $offset auto 0 auto !important;
	z-index: 9;
	width: 96%;
}
.comItem {
	padding: 10rpx;
	margin: 0 8rpx;
	border: #efefef 1px solid;
	border-radius: 30rpx;
	box-shadow: 0 2px 4px 0 #ebc0b6;
	background-color: #fff;
}
</style>
