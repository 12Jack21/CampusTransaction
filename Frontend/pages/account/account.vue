<template>
	<view class="zaiui-my-box" :class="show ? 'show' : ''">
		<view class="zaiui-head-box">
			<!--标题栏-->
			<bar-title bgColor="bg-white">
				<block slot="content">用户信息</block>
				<block slot="right">
					<text class="cuIcon-more" />
				</block>
			</bar-title>
			<!-- end -->

			<!--用户信息-->
			<view class="" style="background: linear-gradient(rgb(230, 100, 101), rgb(238, 239, 244));position: relative;z-index: 0;">
				<view class="flex" style="justify-content: center;padding-top: 20rpx;">
					<view class="flex flex-direction user-space" style="align-items: center;">
						<view class="cu-avatar round" :style="{ backgroundImage: account.avartar.length===0? 'url(/static/images/avatar/1.jpg)':account.avartar }" />
						<view class="" >
							<text class="username">{{account.username}}</text>
							<text style="border-radius: 16rpx;vertical-align: top;" :class="[account.gender===0? 'cuIcon-female bg-pink' : 'cuIcon-male bg-blue']"></text>
						</view>
						<view class="address">
							{{account.address}}
						</view>
						<view class="height-spacce"></view>
						<view class="text-lg text-bold" 
						style="text-align: center;margin-bottom: 15rpx;color: #e54d42;">
							{{ (account.gender===0?'她':'他') + '发布的物品'}}
						</view>
					</view>
				</view>
			</view>
			<!-- end -->

			<view class="zaiui-goods-info-view-box">			
				<view class="zaiui-recommend-list-box">				
					<!-- 水平滑动列表 -->
					<view class="recommend-scroll-box comList">
						<scroll-view class="recommend-scroll" scroll-x>
							<block v-for="(item,index) in commodities" :key="index" >
								<view :id="['scroll' + (index + 1 )]" class="recommend-scroll-item comItem" 
								@tap="comTap(item.id)" style="">
									<view class="cu-avatar xl radius" 
									:style="[{backgroundImage:item.img.length===0?'url(/static/images/comDefault.png)':item.img}]"/>
									<view class="text-cut-2 text-sm text-black margin-tb-sm">{{item.name}}</view>
									<view class="text-red text-price margin-tb-sm text-lg">{{item.expectedPrice}}</view>
								</view>
							</block>
						</scroll-view>
					</view>
				</view>
				<!-- <view class="zaiui-border-view"/> -->
			</view>
			
		</view>
	</view>
</template>

<script>
import {mapState} from 'vuex'
import barTitle from '../../components/basics/bar-title.vue'

export default {
	components:{
		barTitle
	},
	data() {
		return {
			account:{
				username:'急可',
				avartar:'',
				gender:0,
				address:'文理学部',
				wechat:''
			},
			aCom:{
					id:1,
					name:'毛巾',
					img:'',
					expectedPrice:'200',
					originalPrice:'300',
					count:2
				},
			commodities:[
				{
					id:1,
					name:'毛巾',
					img:'',
					expectedPrice:'200',
					originalPrice:'300',
					count:2
				}
			]
		}
	},
	computed: {
		...mapState(['userId'])
	},
	onLoad(params) {
		this.getAccount(params.id)
		
		//debug
		for(let i =0;i<10;i++)
			this.commodities.push(this.aCom)
	},
	methods: {
		comTap(id){
			uni.navigateTo({
				url: '../detail/commodity?id=' + id
			});
		},
		getAccount(toAccId){
			this.$api.getOtherAccount(toAccId, this.userId)
				.then(({data})=>{
					console.log('其他账户的信息',data);
					this.account = data
				})
				.catch(()=>{
					console.log('获取其他账户的信息失败');
				})
		}
	}
}
</script>

<style lang="scss">
@import '../../static/zaiui/style/goods.scss';
.height-spacce{
	height: 150rpx;
}
.user-space{
	.cu-avatar{
		margin-top: 40rpx;
		width: 200rpx;
		height: 200rpx;
	}
	.username{
		font-size: 2em;
		margin: 20rpx 0;
	}
	.address{
		border: #007AFF 1px solid;
		border-radius: 10rpx;
		padding: 10rpx;
	}
}
.comList{
	// position: relative;
	// top: -10px;
	// z-index: 9;																				
}
.comItem{
	padding: 10rpx;
	margin:0 4rpx;
	border: #efefef 1px solid;
	border-radius: 30rpx;
}
</style>
