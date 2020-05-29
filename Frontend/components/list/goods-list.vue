<template>
	<view class="">
		<block v-for="(item, index) in list_data" :key="item.id">
			<view class="bg-white margin-bottom-sm list-radius" v-if="!item.type" >
				<view class="goods-img" @tap="listTap(item.id)">
					<image :src="item.img.length===0 ? '/static/images/comDefault.png':item.img" mode="widthFix" lazy-load />
					<view class="count-view">
						<text class="cu-tag radius sm" :class="['bg-' + (item.count <= 2 ? 'red' : 'blue')]">
							剩余{{ item.count }}件
						</text>
					</view>
				</view>
				<!-- Price -->
				<view class="padding-xs">
					<view class="text-cut-2 text-black">{{ item.name }}</view>
					<view class="margin-top-xs">
						<view class="flex">
							<view class="flex-sub">
								<text class="text-price text-red text-xl text-left">{{ item.expectedPrice }}</text>
								<text class="originalPricce" v-if="item.originalPrice">{{ item.originalPrice }}</text>
							</view>
							<view class="flex-sub text-right" v-if="item.state">
								<text class="cu-tag light bg-red radius sm ">{{ item.state }}</text>
							</view>
						</view>
					</view>
					<!-- end -->
					
					<view class="margin-top-sm margin-bottom-xs text-gray user-info-box">
						<view class="flex">
							<view class="flex-sub">
								<view class="flex flex-wrap user-info">
									<view class="basis-xs" @tap="accTap(item.accId)">
										<image class="cu-avatar sm round img"
									 :src="item.avatar.length===0?'/static/images/avatar/default.png':item.avatar" 
									 lazy-load mode="widthFix" /></view>
									<view class="basis-xl text-cut line-height" @tap="accTap(item.accId)">
										<text class="text-sm margin-left-xs">{{ item.accountName }}</text>
									</view>
									<image class="v-icon" src="/static/zaiui/img/v.png" lazy-load mode="widthFix" v-if="item.avatar.length===0" />
								</view>
							</view>
							<view class="flex-sub text-right text-time">
								<text class="text-sm">{{ item.time }}</text>
							</view>
						</view>
					</view>
				</view>
				<!-- end -->
				
			</view>
		</block>
	</view>
</template>

<script>
import _tool from '@/static/zaiui/util/tools.js'
export default {
	name: 'goods-list',
	props: {
		list_data: {
			type: Array,
			default: () => {
				return []
			}
		},
		show: {
			type: Boolean,
			default: true
		}
	},
	methods: {
		listTap(id) {
			this.$emit('listTap', id)
		},
		accTap(id){
			this.$emit('accTap',id)
		}
	}
}
</script>

<style lang="scss" scoped>
.originalPricce {
	text-decoration-line: line-through;
	margin-left: 10rpx;
}
.zaiui-goods-list-box {
	position: relative;
	width: 100%;
	display: none;
	.list-radius {
		border-radius: 19rpx;
	}
	.goods-img {
		position: relative;
		width: 100%;
		image {
			width: 100%;
			border-radius: 19rpx 19rpx 0 0;
		}
		.pay-view {
			position: absolute;
			top: 20rpx;
			right: 20rpx;
			font-size: 46rpx;
		}
		.service-view {
			position: absolute;
			width: 100%;
			bottom: 7.5rpx;
			right: 10rpx;
		}
		.count-view {
			position: absolute;
			width: 100%;
			bottom: 7.5rpx;
			left: 10rpx;
		}
	}
	.text-time {
		line-height: 47rpx;
	}
	.recommend-list-box {
		.img-aat {
			width: 55rpx;
			margin-top: 10rpx;
		}
		.img-goods {
			font-variant: small-caps;
			margin: 0;
			padding: 0;
			display: inline-flex;
			text-align: center;
			-webkit-box-pack: center;
			justify-content: center;
			-webkit-box-align: center;
			align-items: center;
			white-space: nowrap;
			position: relative;
			width: 137rpx;
			height: 137rpx;
			background-size: cover;
			background-position: center;
			vertical-align: middle;
			border-radius: 12%;
		}
	}
}
.zaiui-goods-list-box.show {
	display: block;
}
</style>
