<template>
	<view class="list">
		<view class="goods-list-box" @tap="listTap(item.id)" v-for="(item, index) in list_data" :key="index">
			<view class="cu-avatar radius" :style="[{ backgroundImage: comImg(item.img) }]" />
			<view class="goods-info-view">
				<view class="text-cut-2 text-black">{{ item.name }}</view>
				<view class="goods-info-tools">
					<text class="text-price text-red text-lg">{{ item.expectedPrice }}</text>
					<text class="text-gray text-sm margin-left-sm">￥{{ item.originalPrice }}</text>
				</view>
				<view class="action">
					<text class="margin-left-xs">X {{ item.count }}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import barTitle from '@/components/basics/bar-title'
import _tool from '@/static/zaiui/util/tools.js' //工具函数

export default {
	name: 'commodityList',

	props: {
		list_data: {
			type: Array,
			default: () => {
				return []
			}
		}
	},
	methods: {
		listTap(id) {
			this.$emit('listTap', id)
		},
		comImg(img){
			if(img.length===0) return 'url(/static/images/comDefault.png)'
			else return 'url('+ img + ')'
		}
	}
}
</script>

<style lang="scss">
/* #ifdef APP-PLUS */
@import '../../static/colorui/main.css';
@import '../../static/colorui/icon.css';
@import '../../static/zaiui/style/app.scss';
/* #endif */
.list{
	margin-top: 30rpx;
}
.goods-list-box {
	position: relative;
	padding: 27.27rpx 0;
	border-bottom: #e0e0e0 1px solid;
	.cu-avatar {
		position: absolute;
		height: 127.27rpx;
		width: 127.27rpx;
		transition: left 0.15s;
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
		transition: left 0.15s;
	}
	.goods-info-view {
		position: relative;
		padding-left: 154.54rpx;
		height: 127.27rpx;
		transition: all 0.15s;
		.goods-info-tools {
			position: absolute;
			bottom: 0;
			// width: calc(100% - 154.54rpx);
			transition: width 0.15s;
			.cu-tag {
				position: absolute;
				right: 0;
				bottom: 3.63rpx;
			}
		}
		.action {
			position: absolute;
			bottom: 0;
			ransition: width 0.15s;
			font-size: 25rpx;
			width: 480rpx;
			bottom: 0rpx;
			text-align: right;
			color: #ffaa00;
		}
	}
	&:last-child {
		border-bottom: none;
	}
}
</style>
