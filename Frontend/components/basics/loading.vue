<template>
	<view class="my-loading">
		<image src="../../static/images/loading.png" mode="aspectFit" class="icon"></image>
		<text class="text-gray"><slot></slot></text>
	</view>
</template>

<script>
export default{
}
</script>

<style lang="scss">
$loadingSize: 50upx;
$space:20rpx;
.my-loading{
		display: flex;
		align-items: center;
		justify-content: center;
		image{
			width: $loadingSize;
			height: $loadingSize;
			margin-right: $space;
			animation: rotate360 1.5s ease infinite;
		}
	}
@keyframes rotate360
	{
		from {
			transform: rotate(0);
		}
		to{
			transform: rotate(360deg);
		}
	}
</style>
