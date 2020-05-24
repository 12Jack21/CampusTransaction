<template>
	<view class="zaiui-modal-box" :class="show ? 'show' : ''">
		<view class="dialog">
			<form @submit="submit">
				<!-- 描述 -->
				<view class="cu-form-group br-top" style="text-align: left;">
					<textarea name="description" maxlength="1000" 
				placeholder="新的物品描述" key="description" :value="commodity.description"></textarea>
				</view>
				<!-- end -->
				
				<!-- 价钱 -->
				<view class="cu-form-group" >
					<view class="title">出售价:</view>
					<input type="digit" :value="commodity.expectedPrice" key="expectedPrice" placeholder="请输入价钱" maxlength="7" name="expectedPrice" />
				</view>
				<!-- end -->
				
				<!-- 物品数量 -->
				<view class="cu-form-group br-bottom">
					<view class="title">数量</view>
					<input type="number" name="count" key="count" :value="commodity.count"/>
				</view>
				
				<!-- 添加 -->
				<view class="flex flex-direction"><button class="cu-btn bg-orange margin-tb-sm lg" 
				form-type="submit">更新物品</button></view>
			</form>
			<text class="cuIcon-roundclose close" @tap="$emit('closeUpdate')"></text>
		</view>
	</view>
</template>

<script>
export default {
	name: 'modal-commodity',
	components:{
	},
	data(){
		return {
			
		}
	},
	props: {
		commodity:{},
		id:{
			type:Number,
			default:-1
		},
		src: {
			type: String,
			default: ''
		},
		show: {
			type: Boolean,
			default: false
		}
	},
	methods: {
		submit(e) {
			let v = e.detail.value
			let body = {
				description: v.description,
				expectedPrice: parseFloat(v.expectedPrice),
				count: parseInt(v.count)
			}
			this.$emit('updateCom', body)	
		}
	}
}
</script>

<style lang="scss" scoped>
.zaiui-modal-box {
	position: fixed;
	opacity: 0;
	top: inherit;
	left: inherit;
	right: inherit;
	bottom: inherit;
	z-index: 9;
	text-align: center;
	background: rgba(0, 0, 0, 0.6);
	transition: all 0.3s;
	pointer-events: none;
	&::before {
		content: '\200B';
		display: inline-block;
		height: 100%;
		vertical-align: middle;
	}
	.dialog {
		position: relative;
		display: inline-block;
		vertical-align: middle;
		width: 618.18rpx;
		.img {
			width: 100%;
			border-radius: 3%;
		}
		.close {
			color: #dadada;
			top: 18.18rpx;
			position: relative;
			font-size: 54.54rpx;
		}
	}
}
.zaiui-modal-box.show {
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	opacity: 1;
	pointer-events: auto;
}
</style>
