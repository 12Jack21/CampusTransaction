<template>
	<view>
		<block v-for="item in list_data" :key="item.id">
			<view class="zaiui-goods-list-box" @tap="listTap(item.id)">
				<view class="cu-avatar radius" :style="[{ backgroundImage: item.img.length === 0 ? 'url(/static/images/comDefault.png)' : item.img }]" />
				<view class="goods-info-view">
					<view class="text-cut-2 text-black">{{ item.name }}</view>
					<view class="goods-info-tools">
						<text class="text-price text-red text-lg">{{ item.expectedPrice }}</text>
						<text class="text-grey " style="text-decoration: line-through;margin-left: 20rpx;">
						{{ item.originalPrice }}
						</text>
						<text class="text-orange" style="position: absolute;right: 10rpx;">X {{item.count}}</text>
					</view>
				</view>
			</view>
		</block>
	</view>
</template>

<script>
import barTitle from '@/components/basics/bar-title'
import _tool from '@/static/zaiui/util/tools.js' //工具函数

export default {
	name: 'noticeGoodsList',
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
@import '../../static/zaiui/style/footmark.scss';
</style>
