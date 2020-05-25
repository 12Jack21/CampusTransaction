<template>
	<view>
		<block v-for="(item) in list_data" :key="item.id">
			<view class="bg-white margin-top padding radius zaiui-trends">
				<!--用户信息-->
				<view class="cu-list menu-avatar">
					<view class="cu-item">
						<view class="cu-avatar round"
						 :style="[{ backgroundImage:item.avatar.length===0? 'url(/static/images/avatar/default.png)' : item.avatar}]" 
						@tap="userTap('userTap', item.userId)" />
						<view class="content" @tap="userTap('userTap', item.userId)">
							<view class="text-black">
								<view class="text-cut">{{ item.username }}</view>
							</view>
							<view class="text-sm flex">
								<text>{{ item.time }}</text>
							</view>
						</view>
						<view class="action">
							<text class="rate">
								<text class=" cuIcon-rank" />
								<text class="margin-left-xs">成功率: {{ item.rate }}%</text>
							</text>
						</view>
					</view>
				</view>

				<view class="notice-title">
					<text>Title-{{ item.title }}</text>
				</view>

				<!--内容-->
				<view class="margin-tb text-black zaiui-text-content" 
				@tap="listTap(item.id)">
					<text>{{ item.description | descFilter}}</text>
					<text class="text-blue margin-left-xs" v-if="item.description.length > MAXLEN">
						<text>查看全文</text>
						<text class="cuIcon-right" />
					</text>
				</view>

				<view class="zaiui-img-grid-col" v-if="item.img.length > 0">
					<!--单图-->
					<view class="one-img" v-if="item.img.length == 1" @tap="imgTap(item.img, item.img)">
						<view class="img-grid" :style="[{ backgroundImage: 'url(' + item.img + ')' }]" />
					</view>

					<!--两图-->
					<view class="grid col-2" v-if="item.img.length == 2">
						<block v-for="(items, indexs) in item.img" :key="indexs">
							<view class="img-grid-view" @tap="imgTap(items, item.img)">
								<view class="img-grid" :style="[{ backgroundImage: 'url(' + items + ')' }]" />
							</view>
						</block>
					</view>

					<!--多图-->
					<view class="grid col-3" v-if="item.img.length > 2">
						<block v-for="(items, indexs) in item.img" :key="indexs" v-if="indexs < 9">
							<view class="img-grid-view" @tap="imgTap(items, item.img)">
								<view class="img-grid" :style="[{ backgroundImage: 'url(' + items + ')' }]" />
							</view>
						</block>
					</view>
				</view>

				<view style="display: flex; justify-content: space-between;">
					<!--下方部分-->
					<view class="cu-tag light bg-red round margin-top">
						<text class="cuIcon-locationfill"></text>
						<text class="margin-left-xs">{{ item.address | addressFilter}}</text>
					</view>
					<text class="margin-top cu-tag" style="background-color: transparent;">
						浏览量:{{ item.browseCount }} 次
					</text>
				</view>
			
			</view>
		</block>
	</view>
</template>

<script>
const MAXLEN = 60
export default {
	name: 'trends-list',
	components: {},
	data(){
		return {
			MAXLEN: MAXLEN
		}
	},
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
			this.$emit('contentTap',id)
		},
		imgTap(img, arr) {
			uni.previewImage({
				current:img,
				urls:arr,
				indicator:'default',
				loop:true
			
			})
		}
	},
	filters:{
		addressFilter(addr){
			return addr
		},
		descFilter(desc){
			if(desc.length > MAXLEN)
				return desc.slice(0,MAXLEN) + '...'
			return desc
		}
	}
}
</script>

<style lang="scss" scoped>
.rate {
	font-size: 25rpx;
}

.notice-title {
	color: #ec4707; 
	font-size: 36rpx;
	line-height: 40rpx;
	font-weight: bold;
	margin: 30rpx 0 10rpx 0;
	word-wrap: break-word;
}

.zaiui-trends {
	border-radius: 18.18rpx;

	.cu-list {
		.cu-item {
			padding-right: 0;
			height: 99.99rpx;

			.cu-avatar {
				left: 0;
				width: 81.81rpx;
				height: 81.81rpx;
			}

			.content {
				left: 99.99rpx;
				line-height: 1.5em;
			}

			.action {
				width: 254.54rpx;
				text-align: right;
				color: #e54d42;

				.cu-btn {
					&:after {
						border-radius: 18.18rpx;
					}

					.cuIcon-add {
						font-size: 27.27rpx;
					}
				}
			}

			&:after {
				width: 0;
				height: 0;
				border-bottom: 0;
			}
		}
	}

	.zaiui-text-content {
		line-height: 1.6;

		.cuIcon-right {
			position: relative;
			top: 1rpx;
		}
	}

	.zaiui-img-grid-col {
		position: relative;
		width: 100%;

		.one-img {
			position: relative;

			.img-grid {
				width: 100%;
				height: 363.63rpx;
				border-radius: 9.09rpx;
				background-size: cover;
				background-position: center;
				background-repeat: no-repeat;
			}
		}

		.col-2 {
			.img-grid-view {
				padding: 5.45rpx;

				.img-grid {
					position: relative;
					width: 309.09rpx;
					height: 218.18rpx;
					background-size: cover;
					background-position: center;
					border-radius: 9.09rpx;
				}
			}
		}

		.col-3 {
			.img-grid-view {
				padding: 5.45rpx;

				.img-grid {
					position: relative;
					width: 204.79rpx;
					height: 204.79rpx;
					background-size: cover;
					background-position: center;
					border-radius: 9.09rpx;
				}
			}
		}
	}

	.zaiui-footer-tool {
		margin: 40rpx 0 18.18rpx;

		.margin-right-lg {
			margin-right: 94.54rpx;
		}

		.icon {
			position: relative;
			font-size: 36.36rpx;
			top: 4rpx;
		}
	}
}
</style>
