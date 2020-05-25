<template>
	<view>
		<block v-for="(item) in list_data" :key="item.id">

			<view class="goods-list-box" @tap="listTap(item.id)">
				
				<view class="cu-avatar radius" :style="[{backgroundImage:'url('+ item.img +')'}]"/>
				<view class="goods-info-view">
					<view class="text-cut-2 text-black">{{item.title}}</view>
					<view class="goods-info-tools">
						<text class="text-price text-red text-lg">{{item.price}}</text>
					</view>
					<view class="action">
							<text class="cuIcon-goods" />
							<text class="margin-left-xs">{{item.expiredTime}}时失效</text>
					</view>
				</view>
				
			</view>
		</block>
	</view>

	
</template>

<script>
	import barTitle from '@/components/basics/bar-title';
	import _tool from '@/static/zaiui/util/tools.js';	//工具函数
	
	export default {
		name: 'reservationList',
		
		props: {
			list_data: {
				type: Array,
				default: () => {
					return []
				}
			},
		},
		
		methods: {
			listTap(id) {
				this.$emit('listTap',id)
			},
		}

	}
</script>



<style lang="scss">
	/* #ifdef APP-PLUS */
		@import "../../static/colorui/main.css";
		@import "../../static/colorui/icon.css";
		@import "../../static/zaiui/style/app.scss";
	/* #endif */


.goods-list-box {
		position: relative;
		padding: 27.27rpx 0;
		.cu-avatar {
			position: absolute;
		    height: 127.27rpx;
		    width: 127.27rpx;
			transition: left .15s;
		}
		.img-lower-box {
			position: absolute;
			height: 90.9rpx;
			width: 90.9rpx;
			background-color: rgba(0, 0, 0, 0.6);
			border-radius: 181.81rpx;
			text-align: center;
			line-height: 90.9rpx;
			font-size: 21.81rpx;
			color: #dedede;
			top: 45.45rpx;
			left: 18.18rpx;
			transition: left .15s;
		}
		.goods-info-view {
			position: relative;
			padding-left: 154.54rpx;
			height: 127.27rpx;
			transition: all .15s;
			.goods-info-tools {
				position: absolute;
				bottom: 0;
				width: calc(100% - 154.54rpx);
				transition: width .15s;
				.cu-tag {
					position: absolute;
				    right: 0;
				    bottom: 3.63rpx;	
				}
			}
			.action {
				position: absolute;
				bottom: 0;
				ransition: width .15s;
				font-size: 25rpx;
				width: 480rpx;
				bottom: 0rpx;
				text-align: right;
				color: #ffaa00;
				
			}
		}
		&:last-child {
			padding-bottom: 0;
		}
	}
</style>

