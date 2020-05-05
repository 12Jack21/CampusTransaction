<template>
	<view class="release_box" :class="show ? 'show' : ''">
		<view class="zaiui-sell-box" v-if="scene == -1">
			<view class="zaiui-bar-view-box">
				<!--小程序端的标题-->
				<!-- #ifdef MP -->
				<view class="text-center text-black zaiui-small-routine-title">卖二手</view>
				<!-- #endif -->

				<!--标题栏-->
				<view class="text-gray zaiui-bar-box">
					<text class="text-red text-large">发布通告</text>
					<text class="margin-left-sm">及时发布 及时交易</text>
					<text class="cuIcon-close text-right close" @tap="closeTap"></text>
				</view>
			</view>

			<!--占位的-->
			<view class="zaiui-seat-height"></view>

			<!--中间内容区域-->
			<view class="zaiui-view-content"><type-list :list_data="typeListData" @listTap="typeListTap"></type-list></view>
		</view>

		<view v-else>
			<view v-if="focus == 0">
				<!--标题栏-->
				<view class="text-gray detail-topbox">
					<view class="cuIcon-back text-back" @tap="backTap">返回</view>
					<view class="text-black text-large" >通告详细信息</view>
				</view>
				<form @submit="formSubmit" @reset="resetNotice">
					<!-- 标题 -->
					<view class="cu-form-group margin-top">
						<view class="title">标题</view>
						<input bindinput="getTitleValue" name="title" placeholder="品类品牌型号都是买家喜欢搜索的" />
					</view>
					<!-- end -->

					<!-- 内容 -->
					<view class="cu-form-group margin-top">
						<textarea @tap="getContentValue" name="content" maxlength="1000" @input="textareaAInput" placeholder="描述宝贝的转手原因,入手渠道和使用感受"></textarea>
					</view>
					<!-- end -->

					<!-- 地址选择 -->
					<view class="cu-form-group">
						<view class="title">地址选择</view>
						<picker mode="multiSelector" @change="MultiChange" @columnchange="MultiColumnChange" :value="multiIndex" :range="multiArray">
							<view class="picker">{{ multiArray[0][multiIndex[0]] }}，{{ multiArray[1][multiIndex[1]] }}，{{ multiArray[2][multiIndex[2]] }}</view>
						</picker>
					</view>
					<!-- end -->

					<!-- 确定发布 -->
					<view class="padding flex flex-direction"><button class="cu-btn bg-green margin-tb-sm lg" form-type="submit">确定发布</button></view>
					<!-- end -->
				</form>
			</view>

			<view v-else>
				<!--标题栏-->
				<view class="text-gray detail-topbox">
					<view class="text-black text-large">物品详细信息</view>
					<view class="cuIcon-back text-back" @tap="backTap">返回</view>
				</view>
				<form @submit="formSubmit" @reset="resetObject">
					<!-- 物品名称 -->
					<view class="cu-form-group margin-top">
						<view class="title">名称</view>
						<input bindinput="getTitleValue" name="title" placeholder="品类品牌型号都是买家喜欢搜索的" />
					</view>
					<!-- 图片 -->
					<view class="cu-bar bg-white margin-top">
						<view class="action">图片上传</view>
						<view class="action">{{ imgList.length }}/5</view>
					</view>
					<view class="cu-form-group">
						<view class="grid col-4 grid-square flex-sub">
							<view class="bg-img" v-for="(item, index) in imgList" :key="index" @tap="ViewImage" :data-url="imgList[index]">
								<image :src="imgList[index]" mode="aspectFill"></image>
								<view class="cu-tag bg-red" @tap.stop="DelImg" :data-index="index"><text class="cuIcon-close"></text></view>
							</view>
							<view class="solids" @tap="ChooseImage" v-if="imgList.length < 1"><text class="cuIcon-cameraadd"></text></view>
						</view>
					</view>
					<!-- end -->

					<!-- 价钱 -->
					<view class="cu-form-group margin-top">
						<view class="title">出售价:</view>
						<input type="digit" @input="moneyInput" :value="money" placeholder="请输入价钱" maxlength="7" name="newPrice" />

						<view class="title">原价:</view>
						<input type="digit" @input="newInput" :value="newMoney" placeholder="请输入原价" maxlength="7" name="oriPrice" />
					</view>
					<!-- end -->

					<!-- 选择分类  -->
					<view class="cu-form-group">
						<view class="title">分类:</view>
						<input disabled="true" name="classify" :value="classify" />
						<button class="cu-btn  bg-green" role="button" aria-disabled="false" @tap="showModal" data-target="DrawerModalL">选择</button>
					</view>
					<!-- end -->

					<!-- 新旧 -->
					<view class="cu-form-group">
						<view class="title">新旧:</view>
						<input disabled="true" name="itemLists" :value="itemLists[itemListsIndex]" />
						<button class="cu-btn  bg-green" role="button" aria-disabled="false" @tap="newState">选择</button>
					</view>
					<!-- end -->
					<!-- 添加 -->
					<view class="padding flex flex-direction"><button class="cu-btn bg-green margin-tb-sm lg" form-type="submit">添加</button></view>
				</form>
			</view>
		</view>
		<view style="height: 100rpx;"></view>
	</view>
</template>

<script>
import typeList from '@/components/list/type-list'

import _sell_data from '@/static/zaiui/data/sell.js' //虚拟数据
import _tool from '@/static/zaiui/util/tools.js' //工具函数

export default {
	name: 'sell',
	components: {
		typeList
	},
	data() {
		return {
			focus: 0, //0 为通告信息，1为物品详细信息填写
			scene: 1, // 1,2,3 分别代表发布带物品的闲置通告、需求通告、任务通告
			typeListData: [],

			modalName: '', //模态框开关
			picker: [
				{
					classify_id: 1,
					classify_name: '手机'
				}
			],
			itemListsIndex: 0, //几层新下标（默认全新）
			itemLists: ['全新', '99新', '95新', '85新', '8新'], //几次新
			classify: '其他闲置', //分类选择默认
			money: '', //出售价
			newMoney: '', //原价
			imgList: [], //图片上传
			multiIndex: [0, 0, 0], //地址选择下标
			multiArray: [
				[
					'北京市',
					'重庆市',
					'福建省',
					'江苏省',
					'广东省',
					'辽宁省',
					'内蒙古',
					'山西省',
					'青海省',
					'四川省',
					'贵州省',
					'云南省',
					'陕西省',
					'西藏',
					'宁夏',
					'新疆',
					'广西',
					'海南省',
					'湖南省',
					'湖北省',
					'河南省',
					'山东省',
					'江西省',
					'安徽省',
					'浙江省',
					'上海',
					'黑龙江省',
					'吉林省',
					'甘肃省',
					'天津市',
					'河北省'
				],
				['北京市'],
				[
					'北京大学',
					'中国人民大学',
					'清华大学',
					'北京交通大学',
					'北京工业大学',
					'北京航空航天大学',
					'北京理工大学',
					'北京科技大学',
					'北方工业大学',
					'北京化工大学',
					'北京工商大学',
					'北京服装学院',
					'北京邮电大学',
					'北京印刷学院',
					'北京建筑大学',
					'北京石油化工学院',
					'北京电子科技学院',
					'中国农业大学',
					'北京农学院',
					'北京林业大学',
					'北京协和医学院',
					'首都医科大学',
					'北京中医药大学',
					'北京师范大学',
					'首都师范大学',
					'首都体育学院',
					'北京外国语大学',
					'北京第二外国语学院',
					'北京语言大学',
					'中国传媒大学',
					'中央财经大学',
					'对外经济贸易大学',
					'北京物资学院',
					'首都经济贸易大学',
					'外交学院',
					'中国人民公安大学',
					'国际关系学院',
					'北京体育大学',
					'中央音乐学院',
					'中国音乐学院',
					'中央美术学院',
					'中央戏剧学院',
					'中国戏曲学院',
					'北京电影学院',
					'北京舞蹈学院',
					'中央民族大学',
					'中国政法大学',
					'华北电力大学',
					'中华女子学院',
					'北京信息科技大学',
					'中国矿业大学（北京）',
					'中国石油大学（北京）',
					'中国地质大学（北京）',
					'北京联合大学',
					'北京城市学院',
					'中国青年政治学院',
					'首钢工学院',
					'中国劳动关系学院',
					'北京吉利学院',
					'首都师范大学科德学院',
					'北京工商大学嘉华学院',
					'北京邮电大学世纪学院',
					'北京工业大学耿丹学院',
					'北京警察学院',
					'北京第二外国语学院中瑞酒店管理学院',
					'北京工业职业技术学院',
					'北京信息职业技术学院',
					'北京电子科技职业学院',
					'北京京北职业技术学院',
					'北京交通职业技术学院',
					'北京青年政治学院',
					'北京农业职业学院',
					'北京政法职业学院',
					'北京财贸职业学院',
					'北京北大方正软件职业技术学院',
					'北京经贸职业学院',
					'北京经济技术职业学院',
					'北京戏曲艺术职业学院',
					'北京汇佳职业学院',
					'北京科技经营管理学院',
					'北京科技职业学院',
					'北京培黎职业学院',
					'北京经济管理职业学院',
					'北京劳动保障职业学院',
					'北京社会管理职业学院',
					'北京艺术传媒职业学院',
					'北京体育职业学院',
					'北京交通运输职业学院',
					'北京卫生职业学院',
					'北京网络职业学院',
					'其他'
				]
			], //默认选择地址
			region: ['贵州省', '毕节市', '毕节职业技术学院'], //选择地址
			// 交易方式
			checkboxs: [
				{
					value: '自提',
					checked: false
				},
				{
					value: '同城面交',
					checked: false
				},
				{
					value: '邮寄',
					checked: false
				}
			]
		}
	},
	props: {
		show: {
			type: Boolean,
			default: true
		}
	},
	watch: {},
	created() {
		//加载虚拟数据
		this.typeListData = _sell_data.typeListData()
	},
	mounted() {
		_tool.setBarColor(true)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	methods: {
		backTap() {
			console.log('click back tap', 'ok?')
			if (this.focus == 0) this.scene = -1
			else this.focus = 0
		},
		closeTap() {
			this.$emit('closeReleaseTap')
		},
		typeListTap(e) {
			console.log('click type', e)
			this.scene = e.index
		}
	}
}
</script>

<style lang="scss" scoped>
.detail-topbox {
	position: relative;
	margin-top: 20rpx;
	display: flex;
	align-items: center;
	height: 60rpx;

	.text-back {
		z-index: 10;
		margin-left: 16rpx;
	}

	.text-large {
		position: absolute;
		width: 100%;
		text-align: center;
	}
}

.release_box {
	display: none;
}

.release_box.show {
	display: block;
}

.text-large {
	font-size: 36rpx;
}

.zaiui-sell-box {
	background: #fafafa;
	position: relative;
	min-height: 100vh;
	z-index: 99999;
	width: 100%;
	display: block;

	.zaiui-bar-view-box {
		position: fixed;
		top: 0;
		width: 100%;
		z-index: 999999;
		background: #fafafa;

		/* #ifndef MP */
		height: calc(var(--status-bar-height) + 99.99rpx);
		/* #endif */

		/* #ifdef MP */
		height: calc(var(--status-bar-height) + 189.99rpx);
		/* #endif */

		padding: var(--status-bar-height) 27.27rpx 0 27.27rpx;
		align-items: center;

		.zaiui-bar-box {
			position: relative;
			width: 100%;
			align-items: center;
			line-height: 99.99rpx;

			.close {
				position: absolute;
				right: 27.27rpx;
				font-size: 40rpx;
				bottom: 9.09rpx;
			}
		}
	}

	.zaiui-seat-height {
		width: 100%;

		/* #ifndef MP */
		height: calc(var(--status-bar-height) + 139.99rpx);
		/* #endif */

		/* #ifdef MP */
		height: calc(var(--status-bar-height) + 239.99rpx);
		/* #endif */
	}

	.zaiui-view-content {
		padding: 0 27.27rpx 27.27rpx;
	}
}

.zaiui-sell-box.show {
	display: block;
}
</style>
