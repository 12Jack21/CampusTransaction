<template>
	<view class="price" :style="{ color: status === 'del' ? delColor : priceColor }">
		<view class="price-icon" :class="[icon ? 'price-icon-' + icon : '', status ? 'price-icon-' + status : '']" :style="{ fontSize: symbolSize + 'px' }">{{ symbol }}</view>

		<view class="price-count" :class="[status ? 'price-count-' + status : '']" :style="{ fontSize: countSize + 'px' }">
			<slot v-if="!selfValue"></slot>

			{{ selfValue && decimal !== 'small' ? selfValue : '' }}

			<text v-if="selfValue && decimal === 'small'">{{ selfValue }}</text>

			<text v-if="selfValue && decimal === 'small'" class="price-count-decimal-small">.{{ decimalNum }}</text>
		</view>
	</view>
</template>

<script>
function getDecimal(priceNum, len, dir) {
	if (!priceNum || !len) {
		return false;
	}

	dir = dir || 'f';
	priceNum = parseFloat(priceNum, 10);
	len = parseInt(len, 10);

	if (dir === 'f') {
		let intNumStr = priceNum.toString().split('.')[0];

		let decimalNumStr = priceNum.toString().split('.')[1] || '00';

		if (decimalNumStr.length < 2) {
			decimalNumStr += '0';
		}

		return intNumStr + '.' + decimalNumStr.substring(0, len);
	} else {
		return priceNum.toFixed(len);
	}
}

export default {
	props: {
		symbol: {
			type: String,
			default: '￥'
		},
		value: {
			type: [Number, String],
			default: ''
		},
		symbolSize: {
			type: [Number, String],
			default: 11
		},
		countSize: {
			type: [Number, String],
			default: 13
		},
		icon: {
			type: [String],
			default: ''
		},
		status: {
			type: String,
			default: ''
		},
		delColor: {
			type: String,
			default: '#999'
		},
		priceColor: {
			type: String,
			default: '#ec1717'
		},
		decimal: {
			type: String,
			default: '2'
		}
	},
	data() {
		return {};
	},
	computed: {
		selfValue() {
			console.log('computed: ', this.value);
			
			if (this.value) {
				let value = this.value;

				switch (this.decimal) {
					// 保留一位小数
					case '1': {
						value = getDecimal(this.value, 1);
						break;
					}

					// 只显示整数
					case 'none': {
						value = parseInt(this.value);
						break;
					}

					// 小数部分缩小
					case 'small': {
						value = parseInt(this.value)
							.toString()
							.trim();

						break;
					}

					default: {
						value = getDecimal(this.value, 2);
						break;
					}
				}

				return value;
			}
		},
		decimalNum() {
			if (this.value && this.decimal === 'small') {
				return (this.value.toString().split('.')[1] || '00').trim();
			}
		}
	},
	mounted() {}
};
</script>

<style>
.price,
.price-icon,
.price-count {
	display: inline-block;
	line-height: 1;
}

.price-icon {
	font-size: 22upx;
}

.price-count {
	font-size: 26upx;
}

.price-icon-sup,
.price-icon-sub {
	font-size: 52%;
}

.price-icon-sup {
	vertical-align: super;
	line-height: 1.1;
}

.price-icon-del,
.price-count-del {
	font-weight: normal;
	text-decoration: line-through;
}

.price-count-decimal-small {
	display: inline;
	font-size: 62%;
}
</style>
